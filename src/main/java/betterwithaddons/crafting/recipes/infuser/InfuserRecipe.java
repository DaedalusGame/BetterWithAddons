package betterwithaddons.crafting.recipes.infuser;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public class InfuserRecipe {
    public IRecipe internal;
    public int requiredSpirit;

    public InfuserRecipe(IRecipe recipe,int spirits)
    {
        internal = recipe;
        requiredSpirit = spirits;
    }

    public int getRequiredSpirit(InventoryCrafting craftMatrix) {
        return requiredSpirit;
    }

    public int getRecipeRequiredSpirit() { return requiredSpirit; }
}
