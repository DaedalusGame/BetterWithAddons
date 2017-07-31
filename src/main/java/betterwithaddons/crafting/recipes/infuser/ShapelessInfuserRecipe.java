package betterwithaddons.crafting.recipes.infuser;

import betterwithaddons.crafting.recipes.IInfuserRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class ShapelessInfuserRecipe extends ShapelessOreRecipe implements IInfuserRecipe {
    int requiredSpirit;

    public ShapelessInfuserRecipe(ResourceLocation group,@Nonnull ItemStack result, int requiredSpirit, Object... recipe) {
        super(group ,result, recipe);
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
