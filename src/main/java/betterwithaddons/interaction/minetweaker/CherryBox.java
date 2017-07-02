package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import java.util.List;

public class CherryBox {
    public static class Add extends BaseListAddition<CherryBoxRecipe>
    {
        private String jeicategory;

        public Add(String name, String jeicategory, CraftingManagerCherryBox manager, List<CherryBoxRecipe> recipes) {
            super(name, manager.getRecipes(), recipes);
            this.jeicategory = jeicategory;
        }

        @Override
        public String getJEICategory(CherryBoxRecipe recipe) {
            return jeicategory;
        }

        @Override
        protected String getRecipeInfo(CherryBoxRecipe recipe) {
            return recipe.getInput().toString();
        }
    }

    public static class Remove extends BaseListRemoval<CherryBoxRecipe>
    {
        private String jeicategory;

        protected Remove(String name, String jeicategory, CraftingManagerCherryBox manager, List<CherryBoxRecipe> recipes) {
            super(name, manager.getRecipes(), recipes);
            this.jeicategory = jeicategory;
        }

        @Override
        public String getJEICategory(CherryBoxRecipe recipe) {
            return jeicategory;
        }

        @Override
        protected String getRecipeInfo(CherryBoxRecipe recipe) {
            return recipe.getInput().toString();
        }
    }
}
