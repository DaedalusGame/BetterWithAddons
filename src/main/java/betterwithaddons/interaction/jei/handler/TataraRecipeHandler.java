package betterwithaddons.interaction.jei.handler;

import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * Created by Christian on 26.09.2016.
 */
public class TataraRecipeHandler implements IRecipeHandler<TataraRecipeWrapper> {
    @Nonnull
    @Override
    public Class<TataraRecipeWrapper> getRecipeClass() {
        return TataraRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return "bwa.tatara";
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull TataraRecipeWrapper recipe) {
        return getRecipeCategoryUid();
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull TataraRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull TataraRecipeWrapper recipe) {
        return true;
    }
}