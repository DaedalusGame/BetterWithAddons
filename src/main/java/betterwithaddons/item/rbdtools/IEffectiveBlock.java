package betterwithaddons.item.rbdtools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public interface IEffectiveBlock {
    boolean isEffective(ItemStack stack, IBlockState state);
}
