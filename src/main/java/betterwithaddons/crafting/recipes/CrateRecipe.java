package betterwithaddons.crafting.recipes;

import betterwithmods.common.registry.bulk.recipes.BulkRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CrateRecipe extends BulkRecipe {
    public CrateRecipe(@Nonnull ItemStack output, Object... inputs) {
        super(output, inputs);
    }
}
