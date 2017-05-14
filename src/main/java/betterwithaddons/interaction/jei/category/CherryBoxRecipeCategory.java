package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class CherryBoxRecipeCategory extends BlankRecipeCategory<CherryBoxRecipeWrapper> {
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawableStatic flame;
    @Nonnull
    private final IDrawable arrow;

    public CherryBoxRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = getBackgroundResource();
        background = helper.createDrawable(location, 55, 16, 82, 54);

        flame = helper.createDrawable(location, 176, 0, 14, 14);

        IDrawableStatic arrowDrawable = helper.createDrawable(location, 176, 14, 24, 17);
        arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    protected abstract ResourceLocation getBackgroundResource();

    @Override
    public void drawExtras(Minecraft minecraft) {
        flame.draw(minecraft, 29, 2);
        arrow.draw(minecraft, 24, 19);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CherryBoxRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 19);
        guiItemStacks.init(1, false, 60, 18);

        guiItemStacks.set(0, recipeWrapper.getInputs());
        guiItemStacks.set(1, recipeWrapper.getOutputs());
    }
}
