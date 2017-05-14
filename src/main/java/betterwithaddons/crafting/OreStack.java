package betterwithaddons.crafting;

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
            if(stack.isItemEqual(ore) && stack.getCount() >= stackSize)
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
        if(OreDictionary.getOres(oreName).size() > 0)
            return OreDictionary.getOres(oreName);
        return null;
    }

    public int getStackSize()
    {
        return this.stackSize;
    }
}