package betterwithaddons.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLattice extends BlockBase {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected BlockLattice() {
        super("lattice", Material.IRON);

        this.setHardness(4.5F);
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.withProperty(WEST, !this.connectsTo(world, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, !this.connectsTo(world, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, !this.connectsTo(world, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, !this.connectsTo(world, pos, EnumFacing.SOUTH));
        state = state.withProperty(UP, !this.connectsTo(world, pos, EnumFacing.UP));
        state = state.withProperty(DOWN, !this.connectsTo(world, pos, EnumFacing.DOWN));
        return state;
    }

    private boolean connectsTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = world.getBlockState(pos.offset(facing));
        return state.isSideSolid(world,pos.offset(facing),facing.getOpposite()) || state.getBlock() instanceof BlockLattice;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    /*@Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if(state.getValue(NORTH) || state.getValue(SOUTH) || state.getValue(WEST) || state.getValue(EAST) || state.getValue(UP) || state.getValue(DOWN))
            return true;
        return false;
    }*/

    @Override
    public int getMetaFromState(IBlockState p_getMetaFromState_1_) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int p_getStateFromMeta_1_) {
        return this.getDefaultState();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { WEST, EAST, NORTH, SOUTH, UP, DOWN });
    }
}
