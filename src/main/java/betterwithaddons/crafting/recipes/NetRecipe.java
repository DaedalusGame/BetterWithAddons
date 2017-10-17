package betterwithaddons.crafting.recipes;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.OreStack;
import betterwithaddons.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NetRecipe
{
    public ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
    public Object input = ItemStack.EMPTY;
    public Object jeiInput = ItemStack.EMPTY;
    public int sandrequired = 0;

    public SifterType type = SifterType.NONE;

    public NetRecipe(SifterType type, Object input, int sand, ItemStack... outputs)
    {
        this.sandrequired = sand;
        this.type = type;

        for (ItemStack out: outputs) {
            if(out.isEmpty())
                this.outputs.add(ItemStack.EMPTY);
            else
                this.outputs.add(out.copy());
        }

        if(input instanceof ItemStack) {
            this.input = ((ItemStack) input).copy();
            this.jeiInput = ((ItemStack) input).copy();
        }
        else if(input instanceof Item) {
            this.input = new ItemStack((Item) input, 1, OreDictionary.WILDCARD_VALUE);
            this.jeiInput = new ItemStack((Item) input, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof Block) {
            this.input = new ItemStack((Block) input, 1, OreDictionary.WILDCARD_VALUE);
            this.jeiInput = new ItemStack((Block) input, 1, OreDictionary.WILDCARD_VALUE);
        }
        else if(input instanceof OreStack) {
            this.input = ((OreStack) input).copy();
            this.jeiInput = ItemUtil.getOreList(((OreStack)input).copy());
        }
        else
        {
            String ret = "Invalid " + type.getName() + " recipe: ";
            for(Object tmp : outputs)
                ret += tmp + ", ";
            ret += "Input: " + input;
            throw new RuntimeException(ret);
        }
    }

    public ArrayList<ItemStack> getOutput()
    {
        return this.outputs;
    }

    public Object getInput()
    {
        return this.jeiInput;
    }

    public Object getRecipeInput() {
        return this.input;
    }

    public int getSandRequired()
    {
        return this.sandrequired;
    }

    public SifterType getType() {
        return type;
    }

    public boolean matches(NetRecipe recipe)
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
            if(stack.matches(item))
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