package betterwithaddons.item;

import betterwithaddons.util.IHasVariants;
import betterwithmods.api.util.IColorProvider;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.List;

public class ItemColored extends Item implements IHasVariants, IColorProvider
{
    public ItemColored() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public ItemStack getByColor(EnumDyeColor color)
    {
        return getByColor(color,1);
    }

    public ItemStack getByColor(EnumDyeColor color, int count)
    {
        return new ItemStack(this,count,color.getMetadata());
    }

    public String getUnlocalizedName(ItemStack p_getUnlocalizedName_1_) {
        int i = p_getUnlocalizedName_1_.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(i).getUnlocalizedName();
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        for(int i = 0; i < EnumDyeColor.values().length; ++i) {
            EnumDyeColor dye = EnumDyeColor.byMetadata(i);
            rlist.add(new ModelResourceLocation(getRegistryName()+"_"+dye.getName(), "inventory"));
        }

        return rlist;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(this.isInCreativeTab(tab))
        for(int i = 0; i < EnumDyeColor.values().length; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    @Override
    public int getColor(ItemStack stack) {
        return EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue();
    }
}
