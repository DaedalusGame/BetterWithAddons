package betterwithaddons.crafting;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreStack
{
    private String oreName;
    private int stackSize;

    public OreStack(String name)
    {
        this(name, 1);
    }

    public OreStack(String name, int stack)
    {
        this.oreName = name;
        this.stackSize = stack;
    }

    public OreStack copy()
    {
        return new OreStack(oreName, stackSize);
    }

    public boolean matches(ItemStack stack)
    {
        for (ItemStack ore: getOres()) {
            if((stack.isItemEqual(ore) || (ore.getItemDamage() == OreDictionary.WILDCARD_VALUE && ore.getItem() == stack.getItem())) && stack.getCount() >= stackSize)
                return true;
        }

        return false;
    }

    public String getOreName()
    {
        return this.oreName;
    }

    public List<ItemStack> getOres()
    {
        List<ItemStack> ores = OreDictionary.getOres(oreName);
        if(ores.size() > 0)
            return ores;
        return Lists.newArrayList();
    }

    public int getStackSize()
    {
        return this.stackSize;
    }

    public void setCount(int stackSize) {
        this.stackSize = stackSize;
    }

    @Override
    public String toString() {
        return oreName+"@"+stackSize;
    }
}