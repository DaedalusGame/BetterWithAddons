package betterwithaddons.item;

import betterwithaddons.util.IHasVariants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ItemMaterial extends Item implements IHasVariants{
    String[] subItemNames;
    ItemStack container;

    public ItemMaterial(String[] subnames) {
        subItemNames = subnames;
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public ItemMaterial setContainer(ItemStack stack)
    {
        container = stack;
        return this;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return container;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return container != null;
    }

    public ItemStack getMaterial(String material) {
        return getMaterial(material,1);
    }

    public ItemStack getMaterial(String material,int count) {
        int meta = 0;
        for(int i = 0; i < subItemNames.length; i++) {
            if(subItemNames[i].toLowerCase().equals(material)) {
                meta = i;
                break;
            }
        }
        return new ItemStack(this, count, meta);
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        if(i >= subItemNames.length)
            return super.getUnlocalizedName();
        return super.getUnlocalizedName() + "." + subItemNames[i];
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        for (String name: subItemNames) {
            rlist.add(new ModelResourceLocation(getRegistryName()+"_"+name, "inventory"));
        }

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subitems) {
        for(int i = 0; i < subItemNames.length; ++i) {
            subitems.add(new ItemStack(item, 1, i));
        }
    }
}
