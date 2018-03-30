package betterwithaddons.handler;

import betterwithmods.common.BWMItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class PortalHandler {
    @GameRegistry.ObjectHolder("minecraft:priest")
    public static VillagerRegistry.VillagerProfession PRIEST;

    @SubscribeEvent
    public void onScrollClick(PlayerInteractEvent.EntityInteractSpecific event)
    {
        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        Entity entity = event.getTarget();
        ItemStack tool = event.getItemStack();
        if(!entity.isBurning() || tool.getItem() != BWMItems.ARCANE_SCROLL)
            return;
        if(!(entity instanceof EntityVillager))
            return;
        EntityVillager villager = (EntityVillager) entity;
        VillagerRegistry.VillagerProfession career = villager.getProfessionForge();
        if(career != PRIEST)
            return;
        //event.setCancellationResult(EnumActionResult.PASS);
        event.setCanceled(true);
        if(world.isRemote)
            return;
        BlockPos pos = entity.getPosition();

        boolean success = trySpawnPortal(world,pos);
        if(success)
        {
            world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, villager.posX, villager.posY, villager.posZ, 1.0D, 0.0D, 0.0D);
            villager.setDead();
        }
    }

    public static boolean trySpawnPortal(World worldIn, BlockPos pos)
    {
        Size sizeX = new Size(worldIn, pos, EnumFacing.Axis.X);

        if (sizeX.isValid() && sizeX.portalBlockCount == 0)
        {
            sizeX.placePortalBlocks();
            return true;
        }
        else
        {
            Size sizeZ = new Size(worldIn, pos, EnumFacing.Axis.Z);

            if (sizeZ.isValid() && sizeZ.portalBlockCount == 0)
            {
                sizeZ.placePortalBlocks();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;
        private Block frame = Blocks.EMERALD_BLOCK;

        public Size(World world, BlockPos pos, EnumFacing.Axis axis)
        {
            this.world = world;
            this.axis = axis;

            if (axis == EnumFacing.Axis.X)
            {
                this.leftDir = EnumFacing.EAST;
                this.rightDir = EnumFacing.WEST;
            }
            else
            {
                this.leftDir = EnumFacing.NORTH;
                this.rightDir = EnumFacing.SOUTH;
            }

            BlockPos blockpos = pos;
            while (pos.getY() > blockpos.getY() - 21 && pos.getY() > 0 && this.isEmptyBlock(world.getBlockState(pos.down()))) {
                pos = pos.down();
            }

            int i = this.getDistanceUntilEdge(pos, this.leftDir) - 1;

            if (i >= 0)
            {
                this.bottomLeft = pos.offset(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);

                if (this.width < 2 || this.width > 21)
                {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }

            if (this.bottomLeft != null)
            {
                this.height = this.calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(BlockPos pos, EnumFacing facing)
        {
            int i;

            for (i = 0; i < 22; ++i)
            {
                BlockPos blockpos = pos.offset(facing, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != frame)
                {
                    break;
                }
            }

            Block block = this.world.getBlockState(pos.offset(facing, i)).getBlock();
            return block == frame ? i : 0;
        }

        public int getHeight()
        {
            return this.height;
        }

        public int getWidth()
        {
            return this.width;
        }

        protected int calculatePortalHeight()
        {
            label56:

            for (this.height = 0; this.height < 21; ++this.height)
            {
                for (int i = 0; i < this.width; ++i)
                {
                    BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    IBlockState blockState = this.world.getBlockState(blockpos);
                    Block block = blockState.getBlock();

                    if (!this.isEmptyBlock(blockState))
                    {
                        break label56;
                    }

                    if (block == Blocks.PORTAL)
                    {
                        ++this.portalBlockCount;
                    }

                    if (i == 0)
                    {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();

                        if (block != frame)
                        {
                            break label56;
                        }
                    }
                    else if (i == this.width - 1)
                    {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();

                        if (block != frame)
                        {
                            break label56;
                        }
                    }
                }
            }

            for (int j = 0; j < this.width; ++j)
            {
                if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != frame)
                {
                    this.height = 0;
                    break;
                }
            }

            if (this.height <= 21 && this.height >= 3)
            {
                return this.height;
            }
            else
            {
                this.bottomLeft = null;
                this.width = 0;
                this.height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(IBlockState state)
        {
            return state.getMaterial() == Material.AIR || state.getBlock() == Blocks.FIRE || state.getBlock() == Blocks.PORTAL;
        }

        public boolean isValid()
        {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }

        public void placePortalBlocks()
        {
            IBlockState portalState = Blocks.PORTAL.getDefaultState().withProperty(BlockPortal.AXIS, this.axis);
            IBlockState frameState = Blocks.OBSIDIAN.getDefaultState();

            for (int y = 0; y < this.height; ++y)
            {
                BlockPos blockpos = this.bottomLeft.up(y);
                this.world.setBlockState(blockpos.offset(this.leftDir), frameState, 2);
                this.world.setBlockState(blockpos.offset(this.rightDir, this.width), frameState, 2);
            }

            for (int i = 0; i < this.width; ++i)
            {
                BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);

                this.world.setBlockState(blockpos.down(), frameState, 2);
                this.world.setBlockState(blockpos.up(this.height), frameState, 2);

                for (int j = 0; j < this.height; ++j)
                {
                    this.world.setBlockState(blockpos.up(j), portalState, 2);
                }
            }
        }
    }
}
