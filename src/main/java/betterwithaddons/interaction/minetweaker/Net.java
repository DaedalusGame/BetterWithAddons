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
        private String jeicategory;

        public Add(String name, String jeicategory, CraftingManagerNet manager, NetRecipe spindleRecipe) {
            super(name, manager.getRecipes(), Lists.newArrayList(spindleRecipe));
            this.jeicategory = jeicategory;
        }

        @Override
        protected String getRecipeInfo(NetRecipe recipe) {
            return recipe.getInput().toString();
        }

        @Override
        public String getJEICategory(NetRecipe recipe) {
            return jeicategory;
        }
    }

    public static class Remove extends BaseListRemoval<NetRecipe>
    {
        private String jeicategory;

        protected Remove(String name, String jeicategory, CraftingManagerNet manager, ItemStack input) {
            super(name, manager.getRecipes(), manager.findRecipeForRemoval(input));
            this.jeicategory = jeicategory;
        }

        @Override
        protected String getRecipeInfo(NetRecipe recipe) {
            return recipe.getInput().toString();
        }

        @Override
        public String getJEICategory(NetRecipe recipe) {
            return jeicategory;
        }
    }
}
