package betterwithaddons.lib;

import net.minecraft.item.ItemStack;

public interface IRTItemColor
{
	int getColorFromItemstack(ItemStack stack, int tintIndex);
}
