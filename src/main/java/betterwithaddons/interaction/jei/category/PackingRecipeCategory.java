package betterwithaddons.interaction.jei.category;

import betterwithaddons.crafting.ICraftingResult;
import betterwithaddons.interaction.jei.BWAJEIPlugin;
import betterwithaddons.interaction.jei.ChangeHandler;
import betterwithaddons.interaction.jei.ChangeHandlerResult;
import betterwithaddons.interaction.jei.OutputRenderer;
import betterwithaddons.interaction.jei.wrapper.PackingRecipeWrapper;
import betterwithaddons.lib.Reference;
import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

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

        IGuiIngredient<ItemStack> inputSlot = guiItemStacks.getGuiIngredients().get(0);
        ChangeHandlerResult handler = new ChangeHandlerResult() {
            @Override
            public ICraftingResult getResult() {
                ItemStack input = inputSlot.getDisplayedIngredient();
                return recipeWrapper.getRecipe().getOutput(Lists.newArrayList(input),null);
            }

            @Override
            public void setup(ICraftingResult result) {
                List<ItemStack> outputs = result.getJEIItems();
                if(outputs.size() > 0)
                    guiItemStacks.set(1, outputs.get(0));
            }
        };
        handler.add(inputSlot);

        guiItemStacks.init(1, false, new OutputRenderer<>(ItemStack.class, handler), 95, 32, 16, 16, 0, 0);

        List<List<ItemStack>> itemInputs = BWAJEIPlugin.expand(recipeWrapper.getInputs());
        guiItemStacks.set(0, itemInputs.get(0));
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }
}
