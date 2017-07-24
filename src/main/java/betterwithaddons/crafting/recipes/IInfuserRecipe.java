package betterwithaddons.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public interface IInfuserRecipe extends IRecipe {
    int getRequiredSpirit(InventoryCrafting craftMatrix);

    int getRecipeRequiredSpirit();
}
