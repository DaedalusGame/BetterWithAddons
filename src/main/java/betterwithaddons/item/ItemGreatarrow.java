package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.util.IDisableable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class ItemGreatarrow extends Item implements IDisableable {
    private boolean disabled;

    public ItemGreatarrow() {
        super();
    }

    public EntityGreatarrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityGreatarrow entityarrow = new EntityGreatarrow(worldIn, shooter);
        return entityarrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
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