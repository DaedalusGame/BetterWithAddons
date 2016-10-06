package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Created by Christian on 25.09.2016.
 */
public class TataraRecipeCategory extends BlankRecipeCategory<TataraRecipeWrapper> {
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
    public void drawAnimations(Minecraft minecraft) {
        flame.draw(minecraft, 2, 20);
        arrow.draw(minecraft, 24, 18);
    }

    @Nonnull
    @Override
    public String getUid() {
        return "bwa.tatara";
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
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull TataraRecipeWrapper wrapper) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 0, 36);
        guiItemStacks.init(2, false, 60, 18);

        guiItemStacks.setFromRecipe(0, wrapper.getInputs());
        guiItemStacks.setFromRecipe(1, ModItems.japanMaterial.getMaterial("rice_ash"));
        guiItemStacks.setFromRecipe(2, wrapper.getOutputs());
    }
}
