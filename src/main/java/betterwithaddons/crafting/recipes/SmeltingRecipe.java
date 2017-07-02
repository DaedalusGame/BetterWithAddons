package betterwithaddons.crafting.recipes;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.util.ItemUtil;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SmeltingRecipe {
    public Object input = ItemStack.EMPTY;
    public ItemStack output = ItemStack.EMPTY;

    public SmeltingRecipe(Object input, ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    public ItemStack getOutput()
    {
        return this.output;
    }

    public Object getInput()
    {
        return this.input;
    }

    public List<ItemStack> getRecipeInputs() {
        Object o = getInput();
        if(o instanceof ItemStack)
            return Lists.newArrayList((ItemStack) o);
        if(o instanceof OreStack)
            return ItemUtil.getOreList((OreStack)o);
        return null;
    }

    public boolean matches(SmeltingRecipe recipe)
    {
        if(!isInputEmpty(this.getInput()) && !isInputEmpty(recipe.getInput()))
        {
            boolean match = this.stacksMatch(this.getInput(), recipe.getInput());
            return match;
        }
        return false;
    }

    public boolean matchesInput(ItemStack item)
    {
        if(isInputEmpty(input))
            return false;
        if(input instanceof ItemStack)
        {
            ItemStack stack = (ItemStack)input;
            if(item.isItemEqual(stack) && item.getCount() >= stack.getCount())
                return true;
        }
        else if(input instanceof OreStack)
        {
            OreStack stack = (OreStack)input;
            if(stack.matches(item))
                return true;
        }
        return false;
    }

    private boolean isInputEmpty(Object stack) {
        return stack == null || stack instanceof ItemStack && ((ItemStack)stack).isEmpty();
    }

    private boolean stacksMatch(Object first, Object second)
    {
        if(first instanceof ItemStack && second instanceof ItemStack) {
            ItemStack firstitem = (ItemStack) first;
            ItemStack seconditem = (ItemStack) second;
            if(firstitem.isEmpty() || seconditem.isEmpty())
                return false;
            return firstitem.getItem() == seconditem.getItem() && firstitem.getItemDamage() == seconditem.getItemDamage() && firstitem.getCount() == seconditem.getCount();
        }
        if(first instanceof OreStack && second instanceof OreStack)
        {
            OreStack firstitem = (OreStack) first;
            OreStack seconditem = (OreStack) second;
            return firstitem.getOreName() == seconditem.getOreName() && firstitem.getStackSize() == seconditem.getStackSize();
        }

        return false;
    }
}
