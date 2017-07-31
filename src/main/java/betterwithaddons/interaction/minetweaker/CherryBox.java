package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import java.util.List;

public class CherryBox {
    public static class Add extends BaseListAddition<CherryBoxRecipe>
    {
        public Add(String name, CraftingManagerCherryBox manager, List<CherryBoxRecipe> recipes) {
            super(name, manager.getRecipes(), recipes);
        }

        @Override
        protected String getRecipeInfo(CherryBoxRecipe recipe) {
            return recipe.getInput().toString();
        }
    }

    public static class Remove extends BaseListRemoval<CherryBoxRecipe>
    {
        protected Remove(String name, CraftingManagerCherryBox manager, List<CherryBoxRecipe> recipes) {
            super(name, manager.getRecipes(), recipes);
        }

        @Override
        protected String getRecipeInfo(CherryBoxRecipe recipe) {
            return recipe.getInput().toString();
        }
    }
}
