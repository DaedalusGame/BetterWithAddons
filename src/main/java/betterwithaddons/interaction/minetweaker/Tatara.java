package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerTatara;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass(Tatara.clazz)
public class Tatara {
    public static final String clazz = "mods.betterwithaddons.Tatara";

    @ZenMethod
    public static void add(IItemStack output, IItemStack input) {
        SmeltingRecipe recipe = new SmeltingRecipe(InputHelper.toObject(input),InputHelper.toStack(output));
        CraftTweakerAPI.apply(new Add(Lists.newArrayList(recipe)));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        List<SmeltingRecipe> recipes = CraftingManagerTatara.instance().findRecipeForRemoval(InputHelper.toStack(input));
        CraftTweakerAPI.apply(new Remove(recipes));
    }

    public static class Add extends BaseListAddition<SmeltingRecipe>
    {
        public Add(List<SmeltingRecipe> recipes) {
            super("Tatara", CraftingManagerTatara.instance().getRecipes(), recipes);
        }

        @Override
        protected String getRecipeInfo(SmeltingRecipe recipe) {
            return recipe.getInput().toString();
        }
    }

    public static class Remove extends BaseListRemoval<SmeltingRecipe>
    {
        protected Remove(List<SmeltingRecipe> recipes) {
            super("Tatara", CraftingManagerTatara.instance().getRecipes(), recipes);
        }

        @Override
        protected String getRecipeInfo(SmeltingRecipe recipe) {
            return recipe.getInput().toString();
        }
    }
}
