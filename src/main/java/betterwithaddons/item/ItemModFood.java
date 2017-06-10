package betterwithaddons.item;

import betterwithaddons.util.IDisableable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemModFood extends ItemFood implements IDisableable {
    private boolean disabled;

    public ItemModFood(int amount, boolean isWolfFood) {
        super(amount, isWolfFood);
    }

    public ItemModFood(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(!disabled)
            super.getSubItems(itemIn, tab, subItems);
    }
}
