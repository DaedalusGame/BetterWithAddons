package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.CrateRecipe;
import betterwithmods.common.registry.bulk.manager.CraftingManagerBulk;
import net.minecraft.item.ItemStack;

public class CraftingManagerCrate extends CraftingManagerBulk<CrateRecipe> {
    private static final CraftingManagerCrate instance = new CraftingManagerCrate();

    public static CraftingManagerCrate getInstance() {
        return instance;
    }

    public CrateRecipe addRecipe(ItemStack output, Object[] inputs) {
        return addRecipe(new CrateRecipe(output, inputs));
    }

    @Override
    public CrateRecipe addRecipe(CrateRecipe recipe) {
        return super.addRecipe(recipe);
    }
}
