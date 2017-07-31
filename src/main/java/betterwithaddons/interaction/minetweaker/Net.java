package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

public class Net {
    public static class Add extends BaseListAddition<NetRecipe>
    {
        public Add(String name, CraftingManagerNet manager, NetRecipe spindleRecipe) {
            super(name, manager.getRecipes(), Lists.newArrayList(spindleRecipe));
        }

        @Override
        protected String getRecipeInfo(NetRecipe recipe) {
            return recipe.getInput().toString();
        }
    }

    public static class Remove extends BaseListRemoval<NetRecipe>
    {
        private String jeicategory;

        protected Remove(String name, CraftingManagerNet manager, ItemStack input) {
            super(name, manager.getRecipes(), manager.findRecipeForRemoval(input));
        }

        @Override
        protected String getRecipeInfo(NetRecipe recipe) {
            return recipe.getInput().toString();
        }
    }
}
