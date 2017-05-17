package betterwithaddons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFusumaPainted extends BlockModPane {
    public static PropertyInteger PAINT = PropertyInteger.create("paint",0,22);

    int[] meta2paint = new int[] {0,2,4,5,6,7,10,11,12,13,14,17,18,21,22};
    int[] paint2meta = new int[23];
    int[] upperpaint = new int[23];
    //int offset = 0;

    protected BlockFusumaPainted(String name) {
        super(name, Material.WOOD);

        paint2meta = new int[23];
        for(int i = 0;i < meta2paint.length;i++)
        {
            paint2meta[meta2paint[i]] = i;
        }

        upperpaint[2] = 1;
        upperpaint[4] = 3;
        upperpaint[10] = 8;
        upperpaint[11] = 9;
        upperpaint[17] = 15;
        upperpaint[18] = 16;
        upperpaint[21] = 19;
        upperpaint[22] = 20;
    }

    //public int getOffset() {
        //return offset;
    //}

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, PAINT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(PAINT,meta2paint[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return paint2meta[state.getValue(PAINT)];
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        checkAndBlank(worldIn,state,pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos posfrom) {
        super.neighborChanged(state, worldIn, pos, blockIn,posfrom);
        checkAndBlank(worldIn,state,pos);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState actstate = super.getActualState(state, world, pos);

        int paint = actstate.getValue(PAINT);
        if(paint == 0)
        {
            IBlockState bottom = world.getBlockState(pos.down());
            if(bottom.getBlock() == this)
            {
                int bottompaint = bottom.getValue(PAINT);
                actstate = actstate.withProperty(PAINT,upperpaint[bottompaint]);
            }
        }

        return actstate;
    }

    public void paint(IBlockState state, World world, BlockPos pos)
    {
        IBlockState bottom = world.getBlockState(pos.down());
        if(bottom.getBlock() == this)
        {
            int bottompaint = bottom.getValue(PAINT);
            if(upperpaint[bottompaint] != 0) {
                paint(bottom, world, pos.down());
                return;
            }
        }

        int paint = state.getValue(PAINT);
        int nextpaint = meta2paint[(paint2meta[paint]+1) % meta2paint.length];
        if(upperpaint[nextpaint] != 0 && world.getBlockState(pos.up()).getBlock() == this)
            world.setBlockState(pos.up(),this.getDefaultState(), 2);
        world.setBlockState(pos,state.withProperty(PAINT,nextpaint), 2);
    }

    public void checkAndBlank(World world, IBlockState state, BlockPos pos)
    {
        int paint = state.getValue(PAINT);
        if(upperpaint[paint] != 0 && world.getBlockState(pos.up()).getBlock() != this)
            world.setBlockState(pos,this.getDefaultState(), 2);
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if(heldItem.isEmpty())
        {
            paint(state,worldIn,pos);
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }
}
