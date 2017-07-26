package betterwithaddons.item.rbdtools;

import betterwithaddons.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKukri extends ItemAxeConvenient {
    public ItemKukri(ToolMaterial material, float damage, float speed) {
        super(material, damage, speed);
    }

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {
        return isTree(stack, state);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return ItemUtil.matchesOreDict(stack, "treeSapling");
    }

    @Override
    public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
        return isTree(stack, state);
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {
        return isTree(stack, state) ? 1.5f : 1.0f;
    }

    public static boolean isTree(ItemStack tool, IBlockState state) {
        Block block = state.getBlock();

        return block instanceof BlockLog || block instanceof BlockLeaves;
    }
}
