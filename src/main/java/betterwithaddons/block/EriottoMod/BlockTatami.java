package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class BlockTatami extends BlockBase {
    protected static final AxisAlignedBB MAT_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);
    public static PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockTatami()
    {
        super("tatami", Material.CARPET);
        this.setSoundType(SoundType.CLOTH);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return MAT_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return MAT_AABB;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.LEAVES.getBlockLayer();
    }*/

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos posfrom) {
        if(!world.isRemote) {
            checkAndDrop(world,state,pos);
        }
    }

    public void checkAndDrop(World worldIn, IBlockState state, BlockPos pos)
    {
        if(worldIn.isRemote || canBlockStay(worldIn, state, pos))
            return;

        breakOtherBlock(worldIn,state,pos,true);
        worldIn.setBlockToAir(pos);
    }

    public void breakOtherBlock(World world, IBlockState state, BlockPos pos, boolean drop)
    {
        EnumFacing block1facing = state.getValue(FACING);
        BlockPos block2pos = pos.offset(block1facing);
        Block block = world.getBlockState(block2pos).getBlock();
        if(block == this || block1facing == EnumFacing.UP) {
            world.setBlockToAir(block2pos);
            if(drop)
                this.dropBlockAsItem(world, pos, state, 0);
        }
    }

    public boolean canBlockStay(World worldIn, IBlockState state, BlockPos pos)
    {
        EnumFacing block1facing = state.getValue(FACING);
        return worldIn.isSideSolid(pos.down(),EnumFacing.UP) && (block1facing == EnumFacing.UP || worldIn.isSideSolid(pos.offset(block1facing).down(),EnumFacing.UP));
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        breakOtherBlock(worldIn,state,pos,false);

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float x, float y, float z, int meta, EntityLivingBase placer, EnumHand hand) {
        x -= 0.5f;
        z -= 0.5f;

        boolean isEast = x > 0 && Math.abs(z) < Math.abs(x);
        boolean isWest = x < 0 && Math.abs(z) < Math.abs(x);
        boolean isSouth = z > 0 && Math.abs(x) < Math.abs(z);
        boolean isNorth = z < 0 && Math.abs(x) < Math.abs(z);

        EnumFacing block1facing = EnumFacing.UP;

        if(isEast) block1facing = EnumFacing.EAST;
        if(isWest) block1facing = EnumFacing.WEST;
        if(isSouth) block1facing = EnumFacing.SOUTH;
        if(isNorth) block1facing = EnumFacing.NORTH;
        if(!canPlaceBlockAt(world,pos.offset(block1facing)))
        {
            block1facing = EnumFacing.UP;
        }

        return getDefaultState().withProperty(FACING,block1facing);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing block1facing = state.getValue(FACING);

        if(block1facing != EnumFacing.UP)
        {
            BlockPos block2pos = pos.offset(block1facing);
            EnumFacing block2facing = block1facing.getOpposite();
            if(canPlaceBlockAt(worldIn,block2pos))
            {
                worldIn.setBlockState(block2pos,state.withProperty(FACING,block2facing));
            }
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isAirBlock(pos) && world.isSideSolid(pos.down(),EnumFacing.UP);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return world == null || world.getBlockState(pos.offset(side)).getBlock() != this;
    }

    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
}
