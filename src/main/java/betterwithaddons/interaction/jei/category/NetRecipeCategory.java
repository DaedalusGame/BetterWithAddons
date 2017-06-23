package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class NetRecipeCategory extends BlankRecipeCategory<NetRecipeWrapper> {
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawable arrow;
    public NetRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = getBackgroundResource();
        background = helper.createDrawable(location,0,0,119,75);

        IDrawableStatic arrowDrawable = helper.createDrawable(location, 120, 0, 29, 58);
        arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        arrow.draw(minecraft, 43, 11);
    }

    protected abstract ResourceLocation getBackgroundResource();

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, NetRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 19, 11);
        guiItemStacks.init(1, true, 51, 51);
        guiItemStacks.init(2, false, 83, 11);
        guiItemStacks.init(3, false, 83, 51);

        guiItemStacks.set(0, recipeWrapper.getInputWithoutSand());
        guiItemStacks.set(1, recipeWrapper.getSandInput());
        guiItemStacks.set(2, recipeWrapper.getUpperOutputs());
        guiItemStacks.set(3, recipeWrapper.getLowerOutputs());
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }
}
