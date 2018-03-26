package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerTatara;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import betterwithaddons.util.IngredientCraftTweaker;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass(Tatara.clazz)
public class Tatara {
    public static final String clazz = "mods.betterwithaddons.Tatara";

    @ZenMethod
    public static void add(IItemStack output, IItemStack input) {
        SmeltingRecipe recipe = new SmeltingRecipe(new IngredientCraftTweaker(input), CraftTweakerMC.getItemStack(output));
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        List<SmeltingRecipe> recipes = CraftingManagerTatara.instance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input));
        CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
    }

    public static class Add implements IAction
    {
        SmeltingRecipe recipe;

        public Add(SmeltingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerTatara.instance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Tatara recipe:"+recipe.toString();
        }
    }

    public static class Remove implements IAction
    {
        List<SmeltingRecipe> recipes;

        public Remove(List<SmeltingRecipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        public void apply() {
            CraftingManagerTatara.instance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing "+recipes.size()+" Tatara recipes";
        }
    }
}
