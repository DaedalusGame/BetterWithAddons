package betterwithaddons.block;

import betterwithaddons.util.FusumaPart;
import betterwithaddons.util.FusumaPicture;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFusumaPainted extends BlockModPane {
    public static PropertyInteger PAINT = PropertyInteger.create("paint",0,15);

    int offset = 0;

    protected BlockFusumaPainted(String name, int offset) {
        super(name, Material.WOOD);
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, PAINT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(PAINT,meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(PAINT);
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

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(FusumaPicture.EMPTY_FUSUMA.getBlock());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);

        if(heldItem == null)
        {
            paint(worldIn,pos);
            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
    }

    private void checkAndBlank(World worldIn, IBlockState state, BlockPos pos) {
        if(!isValidPart(worldIn,pos))
        {
            FusumaPart part = FusumaPicture.EMPTY_FUSUMA;
            worldIn.setBlockState(pos,part.getBlock().getDefaultState().withProperty(PAINT,part.getMeta()));
        }
    }

    public void paint(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockFusumaPainted) {
            FusumaPart oldpicture = FusumaPicture.getFusumaBlock(this, state.getValue(PAINT));

            if (oldpicture == null)
                return;

            if(oldpicture.getY() > 0) {
                paint(world, pos.down(oldpicture.getY()));
                return;
            }

            int currid = oldpicture.getPicture().getPictureID();
            int len = FusumaPicture.getTotalPictures();
            int nextid = (currid + 1) % len;
            int oldheight = oldpicture.getPicture().getHeight();

            FusumaPicture picture;

            for(picture = FusumaPicture.getPicture(nextid); !canPaint(world,pos,picture); picture = FusumaPicture.getPicture(nextid = (nextid + 1) % len));
            int height = picture.getHeight();

            for(int i = 0; i < Math.max(height,oldheight); i++)
            {
                FusumaPart part = picture.getSubblock(i);
                BlockPos paintpos = pos.up(i);

                if(part != null)
                {
                    world.setBlockState(paintpos,part.getBlock().getDefaultState().withProperty(PAINT,part.getMeta()),2);
                }
            }
        }
    }

    public boolean canPaint(World world, BlockPos pos, FusumaPicture picture)
    {
        boolean hasCanvas = true;
        int height = picture.getHeight();

        for(int i = 0; i < height; i++)
        {
            IBlockState state = world.getBlockState(pos.up(i));
            if(!(state.getBlock() instanceof BlockFusumaPainted)) {
                hasCanvas = false;
            }
        }

        if(hasCanvas)
        {
            IBlockState state = world.getBlockState(pos.up(height-1));
            FusumaPart oldpart = FusumaPicture.getFusumaBlock((BlockFusumaPainted) state.getBlock(), state.getValue(PAINT));
            if(oldpart.getY() == oldpart.getPicture().getHeight() - 1)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isValidPart(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockFusumaPainted) {
            FusumaPart oldpart = FusumaPicture.getFusumaBlock(this, state.getValue(PAINT));
            FusumaPicture oldpicture = oldpart.getPicture();
            int y = oldpart.getY();

            if(y < oldpicture.getHeight()-1 && isValidPart(world,pos.up(),oldpicture,y+1))
            {
                return false;
            }
            if(y > 0 && isValidPart(world,pos.down(),oldpicture,y-1))
            {
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean isValidPart(World world, BlockPos pos, FusumaPicture picture, int y)
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockFusumaPainted) {
            FusumaPart part = FusumaPicture.getFusumaBlock(this, state.getValue(PAINT));
            return part.getPicture() == picture && part.getY() == y;
        }

        return false;
    }
}
