package betterwithaddons.block.BetterRedstone;

import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import betterwithaddons.lib.Reference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class BlockWirePCB extends Block implements IColorable {
    public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
    public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
    protected static final AxisAlignedBB[] REDSTONE_WIRE_AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.8125D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.1875D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.1875D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 0.8125D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D)};
    private boolean canProvidePower = true;
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

    public BlockWirePCB() {
        super(Material.CIRCUITS);
        this.setDefaultState(getDefaultState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE).withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE).withProperty(BlockRedstoneWire.POWER, Integer.valueOf(0)));

        this.setUnlocalizedName("pcb_wire");
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "pcb_wire"));
        //this.setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public IBlockColor getBlockColor() {
        return ColorHandlers.REDSTONE_COLORING;
    }

    @Override
    public IItemColor getItemColor() {
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return REDSTONE_WIRE_AABB[getAABBIndex(state.getActualState(world, pos))];
    }

    private static int getAABBIndex(IBlockState state) {
        int i = 0;
        boolean flag = state.getValue(NORTH) != EnumAttachPosition.NONE;
        boolean flag1 = state.getValue(EAST) != EnumAttachPosition.NONE;
        boolean flag2 = state.getValue(SOUTH) != EnumAttachPosition.NONE;
        boolean flag3 = state.getValue(WEST) != EnumAttachPosition.NONE;
        if(flag || flag2 && !flag && !flag1 && !flag3) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if(flag1 || flag3 && !flag && !flag1 && !flag2) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if(flag2 || flag && !flag1 && !flag2 && !flag3) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if(flag3 || flag1 && !flag && !flag2 && !flag3) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.withProperty(WEST, this.getAttachPosition(world, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(world, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(world, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(world, pos, EnumFacing.SOUTH));
        return state;
    }

    private EnumAttachPosition getAttachPosition(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        BlockPos blockpos = pos.offset(facing);
        IBlockState iblockstate = world.getBlockState(pos.offset(facing));
        if(this.pcbAllowsConnection(world,pos,facing)) {
            return EnumAttachPosition.SIDE;
        } else {
            return EnumAttachPosition.NONE;
        }
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return this.isOnPCB(world,pos);
    }

    private IBlockState updateSurroundingRedstone(World world, BlockPos pos, IBlockState state) {
        state = this.calculateCurrentChanges(world, pos, pos, state);
        ArrayList<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        for (BlockPos blockpos : list) {
            world.notifyNeighborsOfStateChange(blockpos, this, true);
        }

        return state;
    }

    private boolean isRedstoneWire(IBlockState state)
    {
        return state.getBlock() == this || state.getBlock() instanceof BlockRedstoneWire;
    }

    private IBlockState calculateCurrentChanges(World world, BlockPos thispos, BlockPos p_calculateCurrentChanges_3_, IBlockState state) {
        IBlockState origState = state;
        int power = state.getValue(BlockRedstoneWire.POWER);
        int maxCurrPower = this.getMaxCurrentStrength(world, p_calculateCurrentChanges_3_, 0);
        /*this.canProvidePower = false;
        int indirectPower = world.isBlockIndirectlyGettingPowered(thispos);
        this.canProvidePower = true;
        if(indirectPower > 0 && indirectPower > maxCurrPower - 1) {
            maxCurrPower = indirectPower;
        }*/
        int maxinpower = 0;
        int maxadjacentpower = 0;
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos blockpos = thispos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();
            if(!pcbAllowsConnection(world,thispos,enumfacing) || (block == this && !pcbAllowsConnection(world,blockpos,enumfacing.getOpposite())))
                continue;
            if(isRedstoneWire(world.getBlockState(blockpos))) {
                maxadjacentpower = this.getMaxCurrentStrength(world, blockpos, maxadjacentpower);
            }
            else {
                int redstonein = Math.max(world.getRedstonePower(blockpos, enumfacing), world.getStrongPower(blockpos, enumfacing));
                maxinpower = Math.max(maxinpower, redstonein);
            }
        }

        if(maxadjacentpower > maxCurrPower) {
            maxCurrPower = maxadjacentpower - 1;
        } else if(maxCurrPower > 0) {
            --maxCurrPower;
        } else {
            maxCurrPower = 0;
        }

        if(maxinpower > maxCurrPower - 1) {
            maxCurrPower = maxinpower;
        }

        maxCurrPower = MathHelper.clamp(maxCurrPower, 0, 15);

        if(power != maxCurrPower) {
            state = state.withProperty(BlockRedstoneWire.POWER, maxCurrPower);
            if(world.getBlockState(thispos) == origState) {
                world.setBlockState(thispos, state, 2);
            }

            this.blocksNeedingUpdate.add(thispos);
            for (EnumFacing enumfacing : EnumFacing.values()) {
                if(!pcbAllowsConnection(world,thispos,enumfacing))
                    continue;
                this.blocksNeedingUpdate.add(thispos.offset(enumfacing));
            }
        }

        return state;
    }

    private void notifyWireNeighborsOfStateChange(World world, BlockPos pos) {
        if(isRedstoneWire(world.getBlockState(pos))) {
            world.notifyNeighborsOfStateChange(pos, this, true);
            for (EnumFacing enumfacing:EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
            }
        }

    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        BlockPos bottompos = pos.down();
        IBlockState bottomstate = world.getBlockState(bottompos);
        return bottomstate.getBlock().onBlockActivated(world,bottompos,bottomstate,player,hand,facing,x,y,z);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if(!world.isRemote) {
            this.updateSurroundingRedstone(world, pos, state);
            Iterator var4 = EnumFacing.Plane.VERTICAL.iterator();

            EnumFacing enumfacing2;
            while(var4.hasNext()) {
                enumfacing2 = (EnumFacing)var4.next();
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing2), this, true);
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var4.hasNext()) {
                enumfacing2 = (EnumFacing)var4.next();
                this.notifyWireNeighborsOfStateChange(world, pos.offset(enumfacing2));
            }

            var4 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var4.hasNext()) {
                enumfacing2 = (EnumFacing)var4.next();
                BlockPos blockpos = pos.offset(enumfacing2);
                if(world.getBlockState(blockpos).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(world, blockpos.up());
                } else {
                    this.notifyWireNeighborsOfStateChange(world, blockpos.down());
                }
            }
        }

    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        if(!world.isRemote) {
            for (EnumFacing enumfacing : EnumFacing.values()) {
                world.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, true);
            }

            this.updateSurroundingRedstone(world, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(world, pos.offset(enumfacing));
            }

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                BlockPos var10 = pos.offset(enumfacing);
                if(world.getBlockState(var10).isNormalCube()) {
                    this.notifyWireNeighborsOfStateChange(world, var10.up());
                } else {
                    this.notifyWireNeighborsOfStateChange(world, var10.down());
                }
            }
        }

    }

    private int getMaxCurrentStrength(World world, BlockPos pos, int strength) {
        IBlockState state = world.getBlockState(pos);

        if(isRedstoneWire(state)) {
            int i = state.getValue(BlockRedstoneWire.POWER);
            return i > strength?i:strength;
        } else {
            return strength;
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(!world.isRemote) {
            if(fromPos.equals(pos.down()))
                world.notifyNeighborsOfStateChange(pos, this, false);

            if(this.canPlaceBlockAt(world, pos)) {
                this.updateSurroundingRedstone(world, pos, state);
            } else {
                this.dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }
        }

    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int p_getItemDropped_3_) {
        return Items.REDSTONE;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return !this.canProvidePower?0:state.getWeakPower(world, pos, facing);
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if(!this.canProvidePower) {
            return 0;
        } else {
            int power = state.getValue(BlockRedstoneWire.POWER);
            if(power == 0 || facing.getHorizontalIndex() < 0) {
                return 0;
            } else if(pcbAllowsConnection(world,pos,facing.getOpposite())) {
                return power-1;
            } else {
                return 0;
            }
        }
    }

    private boolean isOnPCB(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos.down()).getBlock() instanceof BlockPCB;
    }

    private boolean pcbAllowsConnection(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = world.getBlockState(pos.down());
        if(state.getBlock() instanceof BlockPCB)
        {
            return BlockPCB.allowsFacing(state,facing);
        }
        return false;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if(facing != null)
            return pcbAllowsConnection(world,pos,facing.getOpposite());
        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return this.canProvidePower;
    }

    @SideOnly(Side.CLIENT)
    public static int colorMultiplier(int strength) {
        float f = (float)strength / 15.0F;
        float f1 = f * 0.6F + 0.4F;
        if(strength == 0) {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;
        if(f2 < 0.0F) {
            f2 = 0.0F;
        }

        if(f3 < 0.0F) {
            f3 = 0.0F;
        }

        int i = MathHelper.clamp((int)(f1 * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(f2 * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(f3 * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        int i = ((Integer)state.getValue(BlockRedstoneWire.POWER)).intValue();
        if(i != 0) {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            world.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
        }

    }

    @Override
    public ItemStack getItem(World p_getItem_1_, BlockPos p_getItem_2_, IBlockState p_getItem_3_) {
        return new ItemStack(Items.REDSTONE);
    }

    @Override
    public IBlockState getStateFromMeta(int p_getStateFromMeta_1_) {
        return this.getDefaultState().withProperty(BlockRedstoneWire.POWER, Integer.valueOf(p_getStateFromMeta_1_));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState p_getMetaFromState_1_) {
        return ((Integer)p_getMetaFromState_1_.getValue(BlockRedstoneWire.POWER)).intValue();
    }

    @Override
    public IBlockState withRotation(IBlockState p_withRotation_1_, Rotation p_withRotation_2_) {
        switch(p_withRotation_2_.ordinal()) {
            case 1:
                return p_withRotation_1_.withProperty(NORTH, p_withRotation_1_.getValue(SOUTH)).withProperty(EAST, p_withRotation_1_.getValue(WEST)).withProperty(SOUTH, p_withRotation_1_.getValue(NORTH)).withProperty(WEST, p_withRotation_1_.getValue(EAST));
            case 2:
                return p_withRotation_1_.withProperty(NORTH, p_withRotation_1_.getValue(EAST)).withProperty(EAST, p_withRotation_1_.getValue(SOUTH)).withProperty(SOUTH, p_withRotation_1_.getValue(WEST)).withProperty(WEST, p_withRotation_1_.getValue(NORTH));
            case 3:
                return p_withRotation_1_.withProperty(NORTH, p_withRotation_1_.getValue(WEST)).withProperty(EAST, p_withRotation_1_.getValue(NORTH)).withProperty(SOUTH, p_withRotation_1_.getValue(EAST)).withProperty(WEST, p_withRotation_1_.getValue(SOUTH));
            default:
                return p_withRotation_1_;
        }
    }

    @Override
    public IBlockState withMirror(IBlockState p_withMirror_1_, Mirror p_withMirror_2_) {
        switch(p_withMirror_2_.ordinal()) {
            case 1:
                return p_withMirror_1_.withProperty(NORTH, p_withMirror_1_.getValue(SOUTH)).withProperty(SOUTH, p_withMirror_1_.getValue(NORTH));
            case 2:
                return p_withMirror_1_.withProperty(EAST, p_withMirror_1_.getValue(WEST)).withProperty(WEST, p_withMirror_1_.getValue(EAST));
            default:
                return super.withMirror(p_withMirror_1_, p_withMirror_2_);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{NORTH, EAST, SOUTH, WEST, BlockRedstoneWire.POWER});
    }

    enum EnumAttachPosition implements IStringSerializable {
        UP("up"),
        SIDE("side"),
        NONE("none");

        private final String name;

        EnumAttachPosition(String p_i45689_3_) {
            this.name = p_i45689_3_;
        }

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
