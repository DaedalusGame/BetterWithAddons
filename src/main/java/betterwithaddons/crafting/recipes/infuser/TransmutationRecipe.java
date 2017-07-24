package betterwithaddons.crafting.recipes.infuser;

import betterwithaddons.crafting.recipes.SmeltingRecipe;
import net.minecraft.item.ItemStack;

public class TransmutationRecipe extends SmeltingRecipe {
    private int requiredSpirit;

    public TransmutationRecipe(Object input, int requiredSpirit, ItemStack output) {
        super(input, output);
        this.requiredSpirit = requiredSpirit;
    }

    public int getRequiredSpirit(ItemStack input) {
        return requiredSpirit;
    }

    public int getRecipeRequiredSpirit() { return requiredSpirit; }

    public boolean matchesInput(ItemStack item, int spirits) {
        if(spirits < requiredSpirit)
            return false;

        return matchesInput(item);
    }
}
