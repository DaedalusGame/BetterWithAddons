package betterwithaddons.block;

import betterwithaddons.item.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockThorns extends BlockBase {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    protected BlockThorns() {
        super("thorns", Material.WOOD);
        this.setHardness(4.0F);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, UP, DOWN, FACING});
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        return;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        EnumFacing facing = state.getValue(FACING);
        return facing.getIndex() & 7;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING,EnumFacing.getFront(meta & 7));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        boolean hasStem = hasStem(worldIn,pos,state);

        return state.withProperty(DOWN, canConnectTo(worldIn,pos,EnumFacing.DOWN,hasStem))
                .withProperty(UP, canConnectTo(worldIn,pos,EnumFacing.UP,hasStem))
                .withProperty(NORTH, canConnectTo(worldIn,pos,EnumFacing.NORTH,hasStem))
                .withProperty(EAST, canConnectTo(worldIn,pos,EnumFacing.EAST,hasStem))
                .withProperty(SOUTH, canConnectTo(worldIn,pos,EnumFacing.SOUTH,hasStem))
                .withProperty(WEST, canConnectTo(worldIn,pos,EnumFacing.WEST,hasStem));
    }

    public EnumFacing getStem(IBlockState state)
    {
        if(state.getBlock() != this) return EnumFacing.DOWN;
        EnumFacing facing = state.getValue(FACING);
        return facing != null ? facing : EnumFacing.DOWN;
    }

    public IBlockState setStem(IBlockState state, EnumFacing facing)
    {
        return state.withProperty(FACING,facing);
    }

    private boolean hasStem(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        EnumFacing stemDir = getStem(state);
        BlockPos stemPos = pos.offset(stemDir);
        IBlockState stemState = world.getBlockState(stemPos);
        return isProperSoil(world,stemPos,stemDir.getOpposite()) || isVine(stemState.getBlock());
    }

    public boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing connectDir, boolean hasStem) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing stemDir = getStem(state);
        BlockPos otherpos = pos.offset(connectDir);
        IBlockState otherState = world.getBlockState(otherpos);
        Block otherblock = otherState.getBlock();

        if(otherblock == this) {
            if (!hasStem) {
                return true;
            } else if(stemDir == connectDir) {
                return true;
            } else {
                if(hasStem(world,otherpos,otherState))
                    return getStem(otherState) == connectDir.getOpposite();
                else
                    return true;
            }
        }
        else if(isVine(otherblock) || isProperSoil(world,otherpos,connectDir.getOpposite()))
            return true;

        return false;
    }

    public boolean isProperSoil(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        return ModBlocks.thornrose.isProperSoil(world,pos,facing);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) ? this.canSurvive(worldIn, pos) : false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(entityIn instanceof EntityLivingBase)
            entityIn.attackEntityFrom(DamageSource.cactus, 5.0F);
    }

    /*public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (!this.canSurvive(worldIn, pos))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.canSurvive(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
    }*/

    public boolean canSurvive(World world, BlockPos pos)
    {
        int adjacentVines = 0;
        int adjacentSoil = 0;

        for (EnumFacing enumfacing : EnumFacing.VALUES)
        {
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (isVine(block))
            {
                adjacentVines++;
            }
            else if(isProperSoil(world,blockpos,enumfacing))
            {
                adjacentSoil++;
            }
        }

        return (adjacentSoil > 0 || adjacentVines > 0) && adjacentVines <= 3;
    }

    private boolean isVine(Block block)
    {
        return block == this || block == ModBlocks.thornrose;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = state.getActualState(source, pos);
        float f = 0.1875F;
        float f1 = ((Boolean)state.getValue(WEST)).booleanValue() ? 0.0F : 0.1875F;
        float f2 = ((Boolean)state.getValue(DOWN)).booleanValue() ? 0.0F : 0.1875F;
        float f3 = ((Boolean)state.getValue(NORTH)).booleanValue() ? 0.0F : 0.1875F;
        float f4 = ((Boolean)state.getValue(EAST)).booleanValue() ? 1.0F : 0.8125F;
        float f5 = ((Boolean)state.getValue(UP)).booleanValue() ? 1.0F : 0.8125F;
        float f6 = ((Boolean)state.getValue(SOUTH)).booleanValue() ? 1.0F : 0.8125F;
        return new AxisAlignedBB((double)f1, (double)f2, (double)f3, (double)f4, (double)f5, (double)f6);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        state = state.getActualState(worldIn, pos);
        float f = 0.1875F;
        float f1 = 0.8125F;
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D));

        if (((Boolean)state.getValue(WEST)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0D, 0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.8125D));
        }

        if (((Boolean)state.getValue(EAST)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.8125D, 0.1875D, 0.1875D, 1.0D, 0.8125D, 0.8125D));
        }

        if (((Boolean)state.getValue(UP)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.8125D, 0.1875D, 0.8125D, 1.0D, 0.8125D));
        }

        if (((Boolean)state.getValue(DOWN)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.1875D, 0.8125D));
        }

        if (((Boolean)state.getValue(NORTH)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D));
        }

        if (((Boolean)state.getValue(SOUTH)).booleanValue())
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.1875D, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D));
        }
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(ModItems.material.getMaterial("midori"));
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !canConnectTo(blockAccess,pos.offset(side),side,hasStem(blockAccess,pos,blockState));
    }



}
