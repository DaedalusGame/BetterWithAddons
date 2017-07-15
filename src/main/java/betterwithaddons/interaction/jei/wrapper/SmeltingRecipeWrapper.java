package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SmeltingRecipeWrapper extends BlankRecipeWrapper {
    SmeltingRecipe recipe;

    public SmeltingRecipeWrapper(SmeltingRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class,getInputs());
        ingredients.setOutputs(ItemStack.class,getOutputs());
    }

    public List<ItemStack> getInputs() {
        return recipe.getRecipeInputs();
    }

    public List<ItemStack> getOutputs() { return recipe.getRecipeOutputs(); }
}
