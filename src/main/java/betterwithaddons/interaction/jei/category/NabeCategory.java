package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.BWAJEIPlugin;
import betterwithaddons.interaction.jei.wrapper.NabeWrapper;
import betterwithaddons.lib.Reference;
import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class NabeCategory implements IRecipeCategory<NabeWrapper> {
    public static final String UID = "bwa.nabe";

    @Nonnull
    private final IDrawable background;

    public NabeCategory(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation(Reference.MOD_ID,"textures/gui/jei/nabe.png");
        background = helper.createDrawable(location,0,0, 146, 94);
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.nabe.name");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, NabeWrapper recipeWrapper, IIngredients ingredients) {
        List<List<ItemStack>> inputs = BWAJEIPlugin.expand(recipeWrapper.getInputs());
        List<ItemStack> dipInput = BWAJEIPlugin.flatExpand(recipeWrapper.getDipInput());
        List<ItemStack> dipOutput = Lists.newArrayList(recipeWrapper.getDipOutput());
        FluidStack fluidOutput = recipeWrapper.getFluidOutput();

        recipeLayout.getItemStacks().init(0, true, 8-1 + 16, 44-1);
        recipeLayout.getItemStacks().set(0, dipInput);
        recipeLayout.getItemStacks().init(1, false, 90-1 + 16, 44-1);
        recipeLayout.getItemStacks().set(1, dipOutput);
        recipeLayout.getFluidStacks().init(0, false, 90 + 16, 44, 16, 16, fluidOutput != null ? fluidOutput.amount : 0, false, null);
        if(fluidOutput != null)
            recipeLayout.getFluidStacks().set(1, fluidOutput);

        for (int i = 0; i < 6; i++) {
            recipeLayout.getItemStacks().init(2+i, true, 4 + 18 * i - 1 + 16, 4 - 1);
            if(i < inputs.size())
                recipeLayout.getItemStacks().set(2+i, inputs.get(i));
        }
    }
}
