package betterwithaddons.interaction.jei;

import mezz.jei.api.gui.IGuiIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public abstract class ChangeHandler {
    List<IGuiIngredient> ingredients = new ArrayList<>();
    List<Object> ingredientCurrent = new ArrayList<>();

    public void add(IGuiIngredient ingredient) {
        ingredients.add(ingredient);
        ingredientCurrent.add(null);
    }

    public abstract void recalculate();

    public void update() {
        boolean dirty = false;
        for (int i = 0; i < ingredients.size(); i++) {
            IGuiIngredient ingredient = ingredients.get(i);
            Object current = ingredientCurrent.get(i);
            Object next = ingredient.getDisplayedIngredient();
            if(current != next) {
                ingredientCurrent.set(i, next);
                dirty = true;
            }
        }
        if(dirty) {
            recalculate();
        }
    }
}
