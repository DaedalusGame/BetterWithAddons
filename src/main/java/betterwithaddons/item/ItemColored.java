package betterwithaddons.item;

import betterwithaddons.util.IHasVariants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemColored extends Item implements IHasVariants
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

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item p_getSubItems_1_, CreativeTabs p_getSubItems_2_, NonNullList<ItemStack> p_getSubItems_3_) {
        for(int i = 0; i < EnumDyeColor.values().length; ++i) {
            p_getSubItems_3_.add(new ItemStack(p_getSubItems_1_, 1, i));
        }
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
