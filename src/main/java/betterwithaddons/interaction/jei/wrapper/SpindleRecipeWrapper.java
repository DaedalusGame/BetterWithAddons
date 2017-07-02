package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.crafting.recipes.SpindleRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by primetoxinz on 6/29/17.
 */
public class SpindleRecipeWrapper extends BlankRecipeWrapper {
    private SpindleRecipe recipe;

    public SpindleRecipeWrapper(SpindleRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class,getInputs());
        ingredients.setOutput(ItemStack.class,getOutputs());
    }

    public List<ItemStack> getInputs() {
        return recipe.getRecipeInputs();
    }

    public List<ItemStack> getOutputs() {
        return recipe.getOutput();
    }
}
