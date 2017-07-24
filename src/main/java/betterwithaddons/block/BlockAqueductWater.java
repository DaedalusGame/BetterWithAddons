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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockAqueductWater extends BlockFluidClassic {
    protected BlockAqueductWater() {
        super(FluidRegistry.WATER,Material.WATER);

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
                if(dist != currdist)
                    world.setBlockState(pos,state);
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
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean keep = checkAndDry(world, pos, state);

        if(!keep)
            return;

        int quantaRemaining = quantaPerBlock - state.getValue(LEVEL) % 8;

        // Flow vertically if possible
        if (canDisplace(world, pos.up(densityDir)))
        {
            flowIntoBlock(world, pos.up(densityDir), 1);
            return;
        }

        // Flow outward if possible
        int flowMeta = quantaPerBlock - quantaRemaining + 1;
        if (flowMeta >= quantaPerBlock)
        {
            return;
        }

        if (isSourceBlock(world, pos) || !isFlowingVertically(world, pos))
        {
            if (world.getBlockState(pos.down(densityDir)).getBlock() == this)
            {
                flowMeta = 1;
            }
            boolean flowTo[] = getOptimalFlowDirections(world, pos);

            if (flowTo[0]) flowIntoBlock(world, pos.add(-1, 0,  0), flowMeta);
            if (flowTo[1]) flowIntoBlock(world, pos.add( 1, 0,  0), flowMeta);
            if (flowTo[2]) flowIntoBlock(world, pos.add( 0, 0, -1), flowMeta);
            if (flowTo[3]) flowIntoBlock(world, pos.add( 0, 0,  1), flowMeta);
        }

        world.scheduleUpdate(pos, this, this.tickRate(world));
    }

    @Override
    protected void flowIntoBlock(World world, BlockPos pos, int meta)
    {
        if (meta < 0) return;
        if (displaceIfPossible(world, pos) && world.getBlockState(pos).getMaterial() != Material.WATER)
        {
            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState().withProperty(LEVEL, meta), 3);
        }
    }

    /*private boolean isBlocked(World worldIn, BlockPos pos, IBlockState state) {
        Block block = worldIn.getBlockState(pos).getBlock();
        return !(!(block instanceof BlockDoor) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER
                && block != Blocks.REEDS) || (!(state.getMaterial() != Material.PORTAL
                && state.getMaterial() != Material.STRUCTURE_VOID) || state.getMaterial().blocksMovement());
    }*/

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    private int getDepth(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        int level = 0;
        if(state.getMaterial() != Material.WATER)
            level = -1;
        else if(state.getBlock() == this)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileEntityAqueductWater)
                return ((TileEntityAqueductWater) te).getDistanceFromSource();
        }
        else if(state.getValue(LEVEL) != 0)
        {
            level = state.getValue(LEVEL) + InteractionBWA.AQUEDUCT_MAX_LENGTH;
        }

        return level;
    }

    @Override
    public Vec3d getFlowVector(IBlockAccess world, BlockPos pos) {
        double flowx = 0.0D;
        double flowy = 0.0D;
        double flowz = 0.0D;
        int thislevel = this.getDepth(world,pos);
        PooledMutableBlockPos checkpos = PooledMutableBlockPos.retain();
        EnumFacing flowDir = EnumFacing.DOWN;
        HashSet<BlockPos> corners = new HashSet<>();

        for (EnumFacing enumfacing : EnumFacing.HORIZONTALS)
        {
            checkpos.setPos(pos).move(enumfacing);
            int otherlevel = this.getDepth(world,checkpos);

            if (otherlevel >= 0)
            {
                int l = otherlevel - thislevel;
                flowx += (double)(enumfacing.getFrontOffsetX() * l);
                flowy += (double)(enumfacing.getFrontOffsetY() * l);
                flowz += (double)(enumfacing.getFrontOffsetZ() * l);
                corners.add(checkpos.offset(enumfacing.rotateY(),1));
                corners.add(checkpos.offset(enumfacing.rotateY(),-1));
            }
        }

        for(BlockPos cornerpos : corners)
        {
            int xdiff = cornerpos.getX() - pos.getX();
            int zdiff = cornerpos.getZ() - pos.getZ();

            int otherlevel = this.getDepth(world,cornerpos);

            if (otherlevel >= 0)
            {
                int l = otherlevel - thislevel;
                flowx += (double)(xdiff * l);
                flowz += (double)(zdiff * l);
            }
        }

        flowDir = EnumFacing.getFacingFromVector((float)flowx,(float)flowy,(float)flowz);

        if(flowDir.getAxis().isHorizontal())
        {
            EnumFacing right = flowDir.rotateY();
            EnumFacing left = flowDir.rotateYCCW();
            boolean rightDone = false;
            boolean leftDone = false;
            int leftDist = 0;
            int rightDist = 0;

            for(int i = 1; i < 8; i++)
            {
                if(!rightDone)
                {
                    int rightDepth = this.getDepth(world,pos.offset(right,i));
                    if(rightDepth > thislevel)
                        rightDist = 8-1;
                    rightDone = rightDepth == -1 || rightDepth > thislevel;
                }

                if(!leftDone)
                {
                    int leftDepth = this.getDepth(world,pos.offset(left,i));
                    if(leftDepth > thislevel)
                        leftDist = 8-i;
                    leftDone = leftDepth == -1 || leftDepth > thislevel;
                }
            }

            flowx += left.getFrontOffsetX() * leftDist + right.getFrontOffsetX() * rightDist;
            flowz += left.getFrontOffsetZ() * leftDist + right.getFrontOffsetZ() * rightDist;
        }

        Vec3d vec3d = new Vec3d(flowx, flowy, flowz);

        checkpos.release();
        return vec3d.normalize();
    }

    @Override
    public boolean canCollideCheck(@Nonnull IBlockState state, boolean fullHit)
    {
        return fullHit;
    }

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

    /*@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { LEVEL });
    }*/

    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return stack.copy();
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        return true;
    }
}
