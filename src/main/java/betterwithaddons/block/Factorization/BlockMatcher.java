package betterwithaddons.block.Factorization;

import betterwithaddons.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMatcher extends BlockBase {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final IProperty<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    public BlockMatcher() {
        super("block_matcher", Material.ROCK);
        this.setHardness(3.5f);
        this.setTickRandomly(true);
        this.setDefaultState(getDefaultState().withProperty(ACTIVE, false));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing facing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE,state.getValue(AXIS));

        if(fromPos.equals(pos.offset(facing,1)) || fromPos.equals(pos.offset(facing,-1)))
            worldIn.scheduleUpdate(pos,this,1);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        byte match = match(worldIn,pos,EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE,state.getValue(AXIS)));
        if(match > 2 != state.getValue(ACTIVE))
            worldIn.setBlockState(pos,state.withProperty(ACTIVE, match > 2));
        else
            worldIn.markAndNotifyBlock(pos,worldIn.getChunkFromBlockCoords(pos),state,state,3);


        worldIn.scheduleUpdate(pos,this,1);
    }

    //Taken from Factorization
    private byte match(World world, BlockPos pos, EnumFacing axis) {
        // 0: Not at all similar
        // 1: Same solidity
        // 2: Same material
        // 3: Same block
        // 4: Same blockstate
        BlockPos frontpos = pos.offset(axis);
        IBlockState frontbs = world.getBlockState(frontpos);
        Block frontBlock = frontbs.getBlock();
        boolean frontAir = frontBlock.isAir(frontbs, world, frontpos);

        BlockPos backpos = pos.offset(axis.getOpposite());
        IBlockState backbs = world.getBlockState(backpos);
        Block backBlock = backbs.getBlock();
        boolean backAir = backBlock.isAir(backbs, world, backpos);

        if (frontAir && backAir) return 0;

        if (frontBlock == backBlock) {
            if (sameState(frontbs, backbs)) return 4;
            return 3;
        }
        if (frontbs.getMaterial() == backbs.getMaterial()) {
            return 2;
        }
        if (frontbs.getBlockHardness(world,frontpos) == backbs.getBlockHardness(world,backpos)) {
            return 1;
        }
        return 0;
    }

    public static boolean sameState(IBlockState abs, IBlockState bbs) {
        return abs == bbs;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS,EnumFacing.getDirectionFromEntityLiving(pos,placer).getAxis());
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(ACTIVE) ? 15 : 0;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return match(worldIn,pos,EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE,blockState.getValue(AXIS)));
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ACTIVE, (meta & 4) > 0).withProperty(AXIS, EnumFacing.Axis.values()[meta & 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(ACTIVE) ? 4 : 0) | state.getValue(AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { ACTIVE, AXIS });
    }
}
