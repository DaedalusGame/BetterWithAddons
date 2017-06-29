package betterwithaddons.interaction.jei.handler;

import betterwithaddons.interaction.jei.wrapper.SpindleRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 * Created by primetoxinz on 6/29/17.
 */
public class SpindleRecipeHandler implements IRecipeHandler<SpindleRecipeWrapper> {
    @Override
    public Class<SpindleRecipeWrapper> getRecipeClass() {
        return SpindleRecipeWrapper.class;
    }

    @Override
    public String getRecipeCategoryUid(SpindleRecipeWrapper recipe) {
        return "bwa.spindle";
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SpindleRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(SpindleRecipeWrapper recipe) {
        return true;
    }
}
