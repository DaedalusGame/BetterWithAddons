package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by Christian on 25.09.2016.
 */
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
    }

    @Override
    public void drawAnimations(Minecraft minecraft) {
        arrow.draw(minecraft, 24, 19);
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull CherryBoxRecipeWrapper wrapper) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 0, 19);
        guiItemStacks.init(1, false, 60, 18);

        guiItemStacks.setFromRecipe(0, wrapper.getInputs());
        guiItemStacks.setFromRecipe(1, wrapper.getOutputs());
    }
}
