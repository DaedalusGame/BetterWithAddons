package betterwithaddons.crafting.recipes;

import betterwithaddons.util.IHasSize;
import betterwithaddons.util.ItemUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class PackingRecipe
{
    public IBlockState output;
    public ItemStack jeiOutput = ItemStack.EMPTY;
    public Ingredient input = Ingredient.EMPTY;

    public PackingRecipe(Ingredient input, IBlockState output)
    {
        this.output = output;
        this.input = input;
    }

    public void setJeiOutput(ItemStack output)
    {
        this.jeiOutput = output;
    }

    public IBlockState getOutput(IBlockState compressState, List<EntityItem> inv)
    {
        return this.output;
    }

    public ItemStack getJeiOutput()
    {
        return jeiOutput;
    }

    public List<ItemStack> getRecipeInputs() {
        return Lists.newArrayList(input.getMatchingStacks());
    }

    public List<ItemStack> getRecipeOutputs() {
        return Lists.newArrayList(getJeiOutput());
    }

    public boolean matchesInput(EntityItem ent)
    {
        return matchesInput(ent.getItem());
    }

    public boolean matchesInput(ItemStack item) {
        return input.apply(item);
    }

    public boolean matches(IBlockState compressState, List<EntityItem> inv)
    {
        for (EntityItem ent: inv) {
            if(matchesInput(ent))
                return true;
        }
        return false;
    }

    public boolean consumeIngredients(List<EntityItem> inv)
    {
        for (EntityItem ent: inv) {
            if(matchesInput(ent))
            {
                ItemStack stack = ent.getItem();
                int count = getInputCount();
                stack.shrink(count);
                if(stack.isEmpty())
                    ent.setDead();
                else
                    ent.setItem(stack);
                return true;
            }
        }
        return false;
    }

    public int getInputCount() {
        return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
    }
}