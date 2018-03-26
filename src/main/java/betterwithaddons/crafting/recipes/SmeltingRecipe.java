package betterwithaddons.crafting.recipes;

import betterwithaddons.util.IHasSize;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SmeltingRecipe {
    public Ingredient input = Ingredient.EMPTY;
    public ItemStack output = ItemStack.EMPTY;

    public SmeltingRecipe(Ingredient input, ItemStack output)
    {
        this.input = input;
        this.output = output;
    }

    public ItemStack getOutput(ItemStack input)
    {
        return this.output;
    }

    public List<ItemStack> getRecipeInputs() {
        return Lists.newArrayList(input.getMatchingStacks());
    }

    public List<ItemStack> getRecipeOutputs() {
        List<ItemStack> inputs = getRecipeInputs();

        return inputs.stream().map(this::getOutput).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean matchesInput(ItemStack item) {
        return input.apply(item);
    }

    public int getInputCount() {
        return input instanceof IHasSize ? ((IHasSize) input).getSize() : 1;
    }
}
