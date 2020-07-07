package betterwithaddons.crafting.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class NabeRecipeVisual {
    List<Ingredient> ingredients;
    Ingredient dipInput;
    ItemStack dipOutput = ItemStack.EMPTY;
    FluidStack fluidOutput;

    protected NabeRecipeVisual() {
    }

    public NabeRecipeVisual(List<Ingredient> ingredients, Ingredient dipInput, ItemStack dipOutput) {
        this.ingredients = ingredients;
        this.dipInput = dipInput;
        this.dipOutput = dipOutput;
    }

    public NabeRecipeVisual(List<Ingredient> ingredients, FluidStack fluidOutput) {
        this.ingredients = ingredients;
        this.fluidOutput = fluidOutput;
    }

    public NabeRecipeVisual(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getDipInput() {
        return dipInput;
    }

    public ItemStack getDipOutput() {
        return dipOutput;
    }

    public FluidStack getFluidOutput() {
        return fluidOutput;
    }

    public String getInfo() {
        return null;
    }
}
