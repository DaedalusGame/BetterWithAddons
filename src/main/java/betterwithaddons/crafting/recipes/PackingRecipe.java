package betterwithaddons.crafting.recipes;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.util.ItemUtil;
import com.google.common.collect.Lists;
import com.ibm.icu.util.Output;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackingRecipe
{
    public IBlockState output;
    public Object jeiOutput = ItemStack.EMPTY;
    public Object input = ItemStack.EMPTY;

    public PackingRecipe(Object input, IBlockState output)
    {
        this.output = output;

        if(input instanceof ItemStack) {
            this.input = ((ItemStack) input).copy();
        }
        else if(input instanceof Item) {
            this.input = new ItemStack((Item) input, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof Block) {
            this.input = new ItemStack((Block) input, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof OreStack) {
            this.input = ((OreStack) input).copy();
        }
        else
        {
            String ret = "Invalid packing recipe: ";
            ret += "Output: " + output;
            ret += "Input: " + input;
            throw new RuntimeException(ret);
        }
    }

    public void setJeiOutput(Object output)
    {
        if(input instanceof ItemStack) {
            this.jeiOutput = ((ItemStack) output).copy();
        }
        else if(input instanceof Item) {
            this.jeiOutput = new ItemStack((Item) output, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof Block) {
            this.jeiOutput = new ItemStack((Block) output, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof OreStack) {
            this.jeiOutput = ItemUtil.getOreList(((OreStack)output).copy());
        }
    }

    public Object getOutput()
    {
        return this.jeiOutput;
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

    public List<ItemStack> getRecipeOutputs() {
        Object o = getOutput();
        if(o instanceof ItemStack)
            return Lists.newArrayList((ItemStack) o);
        if(o instanceof OreStack)
            return ItemUtil.getOreList((OreStack)o);
        return null;
    }

    public boolean matches(PackingRecipe recipe)
    {
        if(!isInputEmpty(this.getInput()) && !isInputEmpty(recipe.getInput()))
        {
            boolean match = this.stacksMatch(this.getInput(), recipe.getInput());
            return match;
        }
        return false;
    }
    public boolean matchesInput(EntityItem ent)
    {
        return matchesInput(ent.getItem());
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
            if(stack.matches(item) && item.getCount() >= stack.getStackSize())
                return true;
        }
        return false;
    }

    private boolean isInputEmpty(Object stack) {
        return stack == null || stack instanceof ItemStack && ((ItemStack)stack).isEmpty();
    }

    public boolean matches(List<EntityItem> inv)
    {
        for (EntityItem ent: inv) {
            if(matchesInput(ent))
                return true;
        }
        return false;
    }

    public int getInputCount()
    {
        if(input instanceof ItemStack)
            return ((ItemStack) input).getCount();
        else if(input instanceof OreStack)
            return ((OreStack) input).getStackSize();
        return 0;
    }

    public boolean consumeIngredients(List<EntityItem> inv)
    {
        for (EntityItem ent: inv) {
            if(matchesInput(ent))
            {
                ItemStack stack = ent.getItem();
                int count = getInputCount();
                if(stack.getCount() - count <= 0)
                    ent.setDead();
                else {
                    stack.shrink(count);
                    ent.setItem(stack);
                }
                return true;
            }
        }
        return false;
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
            return Objects.equals(firstitem.getOreName(), seconditem.getOreName()) && firstitem.getStackSize() == seconditem.getStackSize();
        }

        return false;
    }
}