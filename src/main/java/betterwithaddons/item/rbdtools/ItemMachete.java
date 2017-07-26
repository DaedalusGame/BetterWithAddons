package betterwithaddons.item.rbdtools;

import betterwithaddons.util.ItemUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMachete extends ItemSwordConvenient {
    public ItemMachete(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {
        return isFoliage(stack, state);
    }

    @Override
    public boolean canShear(ItemStack stack, IBlockState state) {
        return isFoliage(stack, state);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return ItemUtil.matchesOreDict(stack, "vine");
    }

    @Override
    public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
        return isFoliage(stack, state) || isWeb(stack,state);
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {
        return isFoliage(stack, state) ? 15.0f : (isWeb(stack,state) ? 1.5f : 1.0f);
    }

    public static boolean isFoliage(ItemStack tool, IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.VINE || material == Material.LEAVES || material == Material.PLANTS;
    }

    public static boolean isWeb(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.WEB;
    }
}
