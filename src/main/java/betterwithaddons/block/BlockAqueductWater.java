package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class BlockAqueductWater extends BlockLiquid {
    protected BlockAqueductWater() {
        super(Material.WATER);

        String name = "aqueduct_water";

        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        this.setTickRandomly(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityAqueductWater();
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    /*@Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }*/

    /*public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }*/

    public boolean checkAndDry(World world, BlockPos pos, IBlockState state) {
        IBlockState bottomstate = world.getBlockState(pos.down());

        if(world.isAirBlock(pos.down()) || !(bottomstate.getBlock() instanceof BlockAqueduct))
        {
            dry(world,pos);
            return false;
        }
        else
        {
            TileEntity te = world.getTileEntity(pos);

            if(te instanceof TileEntityAqueductWater)
            {
                int currdist = ((TileEntityAqueductWater) te).getDistanceFromSource();
                int dist = TileEntityAqueductWater.getMinDistance(world,pos)+1;
                if(dist > currdist || dist > InteractionBWA.AQUEDUCT_MAX_LENGTH)
                {
                    dry(world,pos);
                    return false;
                }
                ((TileEntityAqueductWater) te).setDistanceFromSource(dist);
            }
            else
            {
                dry(world,pos);
                return false;
            }
        }

        return true;
    }

    public void dry(World world, BlockPos pos)
    {
        world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState().withProperty(LEVEL, 8));
    }

    /*public EnumFacing getAdjacentWaterSource(World world, BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if(hasWaterSource(world,pos.offset(facing),facing))
                return facing;
        }

        return EnumFacing.NORTH;
    }*/

    /*public boolean hasWaterSource(World world, BlockPos pos, EnumFacing flowdir)
    {
        IBlockState state = world.getBlockState(pos);

        if(state.getBlock() == this && state.getValue(FLOWFROM) == flowdir.getOpposite())
            return false;

        return state.getMaterial() == Material.WATER && state.getValue(LEVEL) == 0;
    }*/


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int level = state.getValue(LEVEL);

        boolean keep = checkAndDry(worldIn, pos, state);

        if(!keep)
            return;

        IBlockState stateBelow = worldIn.getBlockState(pos.down());

        if (this.canFlowInto(worldIn, pos.down(), stateBelow)) {
            if (level >= 8) {
                this.tryFlowInto(worldIn, pos.down(), stateBelow, level);
            } else {
                this.tryFlowInto(worldIn, pos.down(), stateBelow, level + 8);
            }
        } else if (level >= 0 && (level == 0 || this.isBlocked(worldIn, pos.down(), stateBelow))) {
            Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
            int nextLevel = level + 1;

            if (level >= 8) {
                nextLevel = 1;
            }

            if (nextLevel >= 8) {
                return;
            }

            for (EnumFacing facing : set) {
                this.tryFlowInto(worldIn, pos.offset(facing), worldIn.getBlockState(pos.offset(facing)), nextLevel);
            }
        }

        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    private boolean canFlowInto(World worldIn, BlockPos pos, IBlockState state) {
        Material material = state.getMaterial();
        return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
    }

    private void tryFlowInto(World worldIn, BlockPos pos, IBlockState state, int level) {
        if (this.canFlowInto(worldIn, pos, state)) {
            if (state.getMaterial() != Material.AIR) {
                state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
            }

            worldIn.setBlockState(pos,
                    Blocks.FLOWING_WATER.getDefaultState().withProperty(LEVEL, level), 3);
        }
    }

    private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return !(!(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER
                && block != Blocks.REEDS) || (!(state.getMaterial() != Material.PORTAL
                && state.getMaterial() != Material.STRUCTURE_VOID) || state.getMaterial().blocksMovement());
    }

    private Set<EnumFacing> getPossibleFlowDirections(World worldIn, BlockPos pos) {
        int i = 1000;
        Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset(enumfacing);
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial
                    || iblockstate.getValue(LEVEL) > 0)) {
                int j;

                if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down()))) {
                    j = this.getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
                } else {
                    j = 0;
                }

                if (j < i) {
                    set.clear();
                }

                if (j <= i) {
                    set.add(enumfacing);
                    i = j;
                }
            }
        }

        return set;
    }

    private int getSlopeDistance(World worldIn, BlockPos pos, int distance, EnumFacing calculateFlowCost) {
        int i = 1000;

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != calculateFlowCost) {
                BlockPos blockpos = pos.offset(enumfacing);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);

                if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial
                        || iblockstate.getValue(LEVEL) > 0)) {
                    if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
                        return distance;
                    }

                    if (distance < 4) {
                        int j = this.getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());

                        if (j < i) {
                            i = j;
                        }
                    }
                }
            }
        }

        return i;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.checkForMixing(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }

    //@Override
    //protected Vec3d getFlow(IBlockAccess world, BlockPos pos, IBlockState state) {
        //EnumFacing flowfrom = state.getValue(FLOWFROM);

    //    return new Vec3d(flowfrom.getFrontOffsetX(),flowfrom.getFrontOffsetY(),flowfrom.getFrontOffsetZ());
    //}

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        return direction == EnumFacing.UP && plantable.getPlantType(world,pos.up()) == EnumPlantType.Water;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LEVEL,8);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { LEVEL });
    }
}
