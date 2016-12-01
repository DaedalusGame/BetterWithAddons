package betterwithaddons.block;

import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockModPane extends BlockBase {
    public static PropertyBool NORTH = PropertyBool.create("north");
    public static PropertyBool EAST = PropertyBool.create("east");
    public static PropertyBool SOUTH = PropertyBool.create("south");
    public static PropertyBool WEST = PropertyBool.create("west");

    private ArrayList<Block> compatiblePanes = new ArrayList<>();

    public void addCompatiblePane(Block block)
    {
        compatiblePanes.add(block);
    }

    protected BlockModPane(String name, Material materialIn) {
        super(name, materialIn);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity) {
        state = state.getActualState(world, pos);
        float minY = 0.001F;
        float maxY = 0.999F;
        float minX = 0.4375F;
        float maxX = 0.5625F;
        float minZ = 0.4375F;
        float maxZ = 0.5625F;
        if(((Boolean)state.getValue(DirUtils.NORTH)).booleanValue()) {
            minZ = 0.0F;
        }

        if(((Boolean)state.getValue(DirUtils.SOUTH)).booleanValue()) {
            maxZ = 1.0F;
        }

        if(((Boolean)state.getValue(DirUtils.WEST)).booleanValue()) {
            minX = 0.0F;
        }

        if(((Boolean)state.getValue(DirUtils.EAST)).booleanValue()) {
            maxX = 1.0F;
        }

        AxisAlignedBB stick = new AxisAlignedBB(0.4375D, 0.0D, 0.4375D, 0.5625D, 1.0D, 0.5625D);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, stick);
        AxisAlignedBB extX;
        if(minZ == 0.0F || maxZ == 1.0F) {
            extX = new AxisAlignedBB(0.4375D, (double)minY, (double)minZ, 0.5625D, (double)maxY, (double)maxZ);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, extX);
        }

        if(minX == 0.0F || maxX == 1.0F) {
            extX = new AxisAlignedBB((double)minX, (double)minY, 0.4375D, (double)maxX, (double)maxY, 0.5625D);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, extX);
        }

    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        state = state.getActualState(world, pos);
        float minY = 0.0F;
        float maxY = 1.0F;
        float minX = 0.4375F;
        float maxX = 0.5625F;
        float minZ = 0.4375F;
        float maxZ = 0.5625F;
        if(((Boolean)state.getValue(DirUtils.NORTH)).booleanValue()) {
            minZ = 0.0F;
        }

        if(((Boolean)state.getValue(DirUtils.SOUTH)).booleanValue()) {
            maxZ = 1.0F;
        }

        if(((Boolean)state.getValue(DirUtils.WEST)).booleanValue()) {
            minX = 0.0F;
        }

        if(((Boolean)state.getValue(DirUtils.EAST)).booleanValue()) {
            maxX = 1.0F;
        }

        return new AxisAlignedBB((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);
    }

    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return !(block instanceof BlockPane) && block == this || state.isOpaqueCube() || block.isSideSolid(state, world, pos, facing.getOpposite()) || compatiblePanes.contains(block);
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !canConnectTo(blockAccess,pos.offset(side),side);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = this.canConnectTo(world, pos.north(), EnumFacing.NORTH);
        boolean east = this.canConnectTo(world, pos.east(), EnumFacing.EAST);
        boolean south = this.canConnectTo(world, pos.south(), EnumFacing.SOUTH);
        boolean west = this.canConnectTo(world, pos.west(), EnumFacing.WEST);
        return state.withProperty(DirUtils.NORTH, Boolean.valueOf(north)).withProperty(DirUtils.EAST, Boolean.valueOf(east)).withProperty(DirUtils.SOUTH, Boolean.valueOf(south)).withProperty(DirUtils.WEST, Boolean.valueOf(west));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}
