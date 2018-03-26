package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerSoakingBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
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
@ZenClass(SoakingBox.clazz)
public class SoakingBox {
    public static final String clazz = "mods.betterwithaddons.SoakingBox";

    @ZenMethod
    public static void add(IItemStack output, IItemStack input) {
        CherryBoxRecipe recipe = new CherryBoxRecipe(BlockCherryBox.CherryBoxType.SOAKING, new IngredientCraftTweaker(input),CraftTweakerMC.getItemStack(output));
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        List<CherryBoxRecipe> recipes = CraftingManagerSoakingBox.instance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input));
        CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
    }

    public static class Add implements IAction
    {
        CherryBoxRecipe recipe;

        public Add(CherryBoxRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerSoakingBox.instance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Soaking Unit recipe:"+recipe.toString();
        }
    }

    public static class Remove implements IAction
    {
        List<CherryBoxRecipe> recipes;

        public Remove(List<CherryBoxRecipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        public void apply() {
            CraftingManagerSoakingBox.instance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing "+recipes.size()+" Soaking Unit recipes";
        }
    }
}