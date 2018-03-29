package betterwithaddons.item;

import betterwithaddons.util.IHasVariants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ItemMaterial extends Item implements IHasVariants {
    String[] subItemNames;
    public String[] subItemUnlocalizedNames;
    ItemStack container = ItemStack.EMPTY;
    HashSet<Integer> disabledSubtypes = new HashSet<>();

    public ItemMaterial(String[] subnames) {
        subItemNames = subnames;
        subItemUnlocalizedNames = Arrays.copyOf(subnames,subnames.length);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public ItemMaterial setContainer(ItemStack stack)
    {
        container = stack;
        return this;
    }

    public ItemStack getContainer()
    {
        return container;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {

        return container.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return !container.isEmpty();
    }

    public void setDisabled(String material)
    {
        int meta = getMaterialMeta(material);
        disabledSubtypes.add(meta);
    }

    private int getMaterialMeta(String material) {
        int meta = 0;
        for(int i = 0; i < subItemNames.length; i++) {
            if(subItemNames[i].toLowerCase().equals(material)) {
                meta = i;
                break;
            }
        }
        return meta;
    }

    public ItemStack getMaterial(String material) {
        return getMaterial(material,1);
    }

    public ItemStack getMaterial(String material,int count) {
        return new ItemStack(this, count, getMaterialMeta(material));
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        if(i >= subItemNames.length)
            return super.getUnlocalizedName();
        return super.getUnlocalizedName() + "." + subItemUnlocalizedNames[i];
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

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab))
            for(int i = 0; i < subItemNames.length; ++i) {
                if(!disabledSubtypes.contains(i))
                    items.add(new ItemStack(this, 1, i));
            }
    }
}
