package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTatamiRecessed extends BlockTatami implements ISlat {
    public BlockTatamiRecessed(String name) {
        super(name);
        this.setResistance(0.2F);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos1, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.tatami_full"));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBlockStay(World worldIn, IBlockState state, BlockPos pos) {
        return true;
    }

    @Override
    public void breakOtherBlock(World world, IBlockState state, BlockPos pos, boolean drop) {
        EnumFacing block1facing = state.getValue(FACING);
        BlockPos block2pos = pos.offset(block1facing);
        Block block = world.getBlockState(block2pos).getBlock();
        if(block == this && block1facing != EnumFacing.UP) {
            world.setBlockState(block2pos,ModBlocks.BAMBOO_SLATS.getDefaultState());
            if(drop)
                spawnAsEntity(world,pos,new ItemStack(ModBlocks.TATAMI));
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(ModBlocks.BAMBOO_SLATS));
        drops.add(new ItemStack(ModBlocks.TATAMI));
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.SOLID;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        if(world == null)
            return true;

        IBlockState otherstate = world.getBlockState(pos.offset(side));
        Block otherblock = otherstate.getBlock();

        return !(otherblock instanceof ISlat);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
