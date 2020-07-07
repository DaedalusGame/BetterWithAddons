package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.crafting.recipes.NabeRecipeVisual;
import betterwithaddons.interaction.jei.BWAJEIPlugin;
import betterwithaddons.lib.Reference;
import com.google.common.collect.Lists;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class NabeWrapper implements IRecipeWrapper {
    @Nonnull
    public final IDrawable slot;
    @Nonnull
    public final IDrawable arrow;

    NabeRecipeVisual recipe;

    public NabeWrapper(NabeRecipeVisual recipe) {
        this.recipe = recipe;

        ResourceLocation location = new ResourceLocation(Reference.MOD_ID,"textures/gui/jei/nabe.png");
        slot = BWAJEIPlugin.HELPER.getGuiHelper().createDrawable(location, 146,0, 18, 18);
        arrow = BWAJEIPlugin.HELPER.getGuiHelper().createDrawable(location, 164, 0, 16, 16);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();
        inputs.addAll(BWAJEIPlugin.flatExpand(recipe.getIngredients()));
        inputs.addAll(BWAJEIPlugin.flatExpand(recipe.getDipInput()));

        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, Lists.newArrayList(recipe.getDipOutput()));
        ingredients.setOutputs(FluidStack.class, Lists.newArrayList(recipe.getFluidOutput()));
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if(recipe.getDipInput() != null)
            drawLeft(minecraft);
        if(!recipe.getDipOutput().isEmpty() || recipe.getFluidOutput() != null)
            drawRight(minecraft);
        if(recipe.getInfo() != null) {
            List<String> strings = minecraft.fontRenderer.listFormattedStringToWidth(recipe.getInfo(), 140);
            for(int i = 0; i < strings.size(); i++) {
                minecraft.fontRenderer.drawString(strings.get(i),3, 64+i*(minecraft.fontRenderer.FONT_HEIGHT+3), 4210752);
            }
        }
    }

    private void drawLeft(Minecraft minecraft) {
        slot.draw(minecraft,7 + 16,43);
        arrow.draw(minecraft, 28 + 16,44);
    }

    private void drawRight(Minecraft minecraft) {
        slot.draw(minecraft,89 + 16,43);
        arrow.draw(minecraft, 70 + 16,44);
    }

    public List<Ingredient> getInputs() {
        return recipe.getIngredients();
    }

    public Ingredient getDipInput() {
        return recipe.getDipInput();
    }

    public ItemStack getDipOutput() {
        return recipe.getDipOutput();
    }

    public FluidStack getFluidOutput() {
        return recipe.getFluidOutput();
    }
}
