package betterwithaddons.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemInkAndQuill extends Item {
    public ItemInkAndQuill()
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(30);
        setNoRepair();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
