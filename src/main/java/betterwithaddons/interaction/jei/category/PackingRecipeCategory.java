package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.PackingRecipeWrapper;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class PackingRecipeCategory extends BlankRecipeCategory<PackingRecipeWrapper> {
    public static final String UID = "bwa.packing";
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;
    public PackingRecipeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/packing.png");
        background = helper.createDrawable(location, 0, 0, 145, 80);

        localizedName = Translator.translateToLocal("inv.packing.name");
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
    public void setRecipe(IRecipeLayout recipeLayout, PackingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 20, 31);
        guiItemStacks.init(1, false, 94, 31);

        guiItemStacks.set(0, recipeWrapper.getInputs());
        guiItemStacks.set(1, recipeWrapper.getOutputs());
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }
}
