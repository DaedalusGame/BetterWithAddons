package betterwithaddons.interaction.jei.handler;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class NetRecipeHandler implements IRecipeHandler<NetRecipeWrapper> {
    @Nonnull
    @Override
    public Class<NetRecipeWrapper> getRecipeClass() {
        return NetRecipeWrapper.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return null;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull NetRecipeWrapper recipe) {
        return getSifterTypeUid(recipe.type);
    }

    private String getSifterTypeUid(SifterType type)
    {
        switch(type)
        {
            case FIRE: return "bwa.firenet";
            case SAND: return "bwa.sandnet";
            case WATER: return "bwa.waternet";
        }

        return null;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull NetRecipeWrapper recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull NetRecipeWrapper recipe) {
        return true;
    }
}