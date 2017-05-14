package betterwithaddons.interaction.jei.handler;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CherryBoxRecipeHandler implements IRecipeHandler<CherryBoxRecipeWrapper> {
    @Nonnull
    @Override
    public Class<CherryBoxRecipeWrapper> getRecipeClass() {
        return CherryBoxRecipeWrapper.class;
    }
    
    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull CherryBoxRecipeWrapper recipe) {
        return getSifterTypeUid(recipe.type);
    }

    private String getSifterTypeUid(CherryBoxType type)
    {
        switch(type)
        {
            case SOAKING: return "bwa.soakingbox";
            case DRYING: return "bwa.dryingbox";
        }

        return null;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull CherryBoxRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull CherryBoxRecipeWrapper recipe) {
        return true;
    }
}