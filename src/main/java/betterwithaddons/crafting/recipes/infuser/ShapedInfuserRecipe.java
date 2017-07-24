package betterwithaddons.crafting.recipes.infuser;

import betterwithaddons.crafting.recipes.IInfuserRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;

public class ShapedInfuserRecipe extends ShapedOreRecipe implements IInfuserRecipe {
    int requiredSpirit;

    public ShapedInfuserRecipe(@Nonnull ItemStack result, int requiredSpirit, Object... recipe) {
        super(result, recipe);
        this.requiredSpirit = requiredSpirit;
    }

    @Override
    public int getRequiredSpirit(InventoryCrafting craftMatrix) {
        return requiredSpirit;
    }

    @Override
    public int getRecipeRequiredSpirit() {
        return requiredSpirit;
    }
}
