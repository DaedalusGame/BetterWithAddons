package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TataraRecipeCategory extends BlankRecipeCategory<TataraRecipeWrapper> {
    public static final String UID = "bwa.tatara";
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final IDrawable flame;
    @Nonnull
    private final IDrawable arrow;
    @Nonnull
    private final String localizedName;
    public TataraRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, "textures/gui/tatara.png");
        background = helper.createDrawable(location, 55, 16, 82, 54);

        IDrawableStatic flameDrawable = helper.createDrawable(location, 176, 0, 14, 14);
        flame = helper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = helper.createDrawable(location, 176, 14, 24, 17);
        arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        localizedName = Translator.translateToLocal("inv.tatara.name");
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        flame.draw(minecraft, 2, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Nonnull
    @Override
    public String getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, TataraRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 0, 36);
        guiItemStacks.init(2, false, 60, 18);

        guiItemStacks.set(0, recipeWrapper.getInputs());
        guiItemStacks.set(1, ModItems.materialJapan.getMaterial("rice_ash"));
        guiItemStacks.set(2, recipeWrapper.getOutputs());
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }
}
