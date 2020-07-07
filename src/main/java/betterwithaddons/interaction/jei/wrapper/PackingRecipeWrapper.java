package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.crafting.recipes.PackingRecipe;
import betterwithaddons.interaction.jei.BWAJEIPlugin;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public class PackingRecipeWrapper extends BlankRecipeWrapper {
    PackingRecipe recipe;

    public PackingRecipeWrapper(PackingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<List<ItemStack>> inputStacks = BWAJEIPlugin.expand(recipe.inputs);
        ingredients.setInputLists(ItemStack.class,inputStacks);
        ingredients.setOutputs(ItemStack.class,recipe.output.getJEIItems());
    }

    public PackingRecipe getRecipe() {
        return recipe;
    }

    public List<Ingredient> getInputs() {
        return recipe.inputs;
    }
}
