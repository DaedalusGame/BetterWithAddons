package betterwithaddons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockScaffold extends BlockBase {
    protected BlockScaffold(String name) {
        super(name, Material.WOOD);
        this.setHardness(2.5f);
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
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(.0625f, 0, .0625f, .9375f, 1, .9375f));
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityLivingBase && !((EntityLivingBase) entityIn).isOnLadder() && isLadder(state, worldIn, pos, (EntityLivingBase) entityIn)) {
            float f5 = 0.15F;
            if (entityIn.motionX < -f5)
                entityIn.motionX = -f5;
            if (entityIn.motionX > f5)
                entityIn.motionX = f5;
            if (entityIn.motionZ < -f5)
                entityIn.motionZ = -f5;
            if (entityIn.motionZ > f5)
                entityIn.motionZ = f5;

            entityIn.fallDistance = 0.0F;
            if (entityIn.motionY < -0.15D)
                entityIn.motionY = -0.15D;

            if (entityIn.motionY < 0 && entityIn instanceof EntityPlayer && entityIn.isSneaking()) {
                entityIn.motionY = .05;
                return;
            }
            if (entityIn.isCollidedHorizontally)
                entityIn.motionY = .2;
        }
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

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        IBlockState bottomState = world.getBlockState(pos.down());
        if (bottomState.getBlock() != this && !bottomState.isSideSolid(world, pos.down(), EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        IBlockState bottomState = world.getBlockState(pos.down());
        return bottomState.getBlock() == this || bottomState.isSideSolid(world, pos.down(), EnumFacing.UP);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn == null)
            return false;

        ItemStack stack = playerIn.getHeldItem(hand);

        if(!playerIn.isSneaking() && stack.getItem() == Item.getItemFromBlock(this))
        {
            if(worldIn.isRemote)
                return true;
            else
            {
                if(placeOnTop(worldIn,pos.up(),this.getDefaultState(),getLowerSupport(worldIn,pos)))
                    stack.shrink(1);

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    public boolean placeOnTop(World world, BlockPos pos, IBlockState stateToPlace, int support)
    {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(pos);
        while(support > 0) {
            IBlockState checkState = world.getBlockState(checkPos);

            if (checkState.getBlock().isReplaceable(world, checkPos)) {
                world.setBlockState(checkPos,stateToPlace);
                return true;
            } else if (checkState.getBlock() == this) {
                support = Math.max(support,getSurroundingSupport(world, checkPos));
            } else
                break;

            support--;
            checkPos.move(EnumFacing.UP);
        }

        return false;
    }

    private int getLowerSupport(World world, BlockPos pos)
    {
        int support = 0;
        for(int i = 0; i < 8; i++) {
            support = Math.max(support,getSurroundingSupport(world, pos.down(i)) - i);
        }

        return support;
    }

    private int getSurroundingSupport(World world, BlockPos pos) {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            if(world.isSideSolid(pos.offset(facing),facing.getOpposite()))
                return 8;
        }

        return 0;
    }
}
