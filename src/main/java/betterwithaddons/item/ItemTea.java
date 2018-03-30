package betterwithaddons.item;

import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import betterwithaddons.util.TeaType;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;

import java.util.stream.Collectors;

public class ItemTea extends Item implements IColorable {
    TeaType.ItemType itemType;

    public ItemTea(TeaType.ItemType itemType) {
        super();
        this.itemType = itemType;
    }

    public ItemStack getStack(TeaType type)
    {
        return getStack(type,1);
    }

    public ItemStack getStack(TeaType type, int n)
    {
        ItemStack stack = new ItemStack(this, n);
        stack.setTagInfo("type",new NBTTagString(type.getName()));
        return stack;
    }

    public TeaType getType(ItemStack stack)
    {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null)
            return TeaType.getType(compound.getString("type"));
        return TeaType.WHITE;
    }

    public int getColor(ItemStack stack)
    {
        TeaType type = getType(stack);
        switch (itemType)
        {
            case Leaves: return type.getLeafColor();
            case Soaked: return type.getSoakedColor();
            case Wilted: return type.getWiltedColor();
            case Powder: return type.getPowderColor();
            default: return 0xFFFFFF;
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab))
            items.addAll(TeaType.getTypesByItem(itemType).stream().map(this::getStack).collect(Collectors.toList()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        TeaType type = getType(stack);
        return super.getUnlocalizedName(stack)+"."+type.getName();
    }

    @Override
    public IBlockColor getBlockColor() {
        return null;
    }

    @Override
    public IItemColor getItemColor() {
        return ColorHandlers.TEA_ITEM_COLORING;
    }
}
