package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerDryingBox;
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
@ZenClass(DryingBox.clazz)
public class DryingBox {
    public static final String clazz = "mods.betterwithaddons.DryingBox";

    @ZenMethod
    public static void add(IItemStack output, IItemStack input) {
        CherryBoxRecipe recipe = new CherryBoxRecipe(BlockCherryBox.CherryBoxType.DRYING, new IngredientCraftTweaker(input),CraftTweakerMC.getItemStack(output));
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        List<CherryBoxRecipe> recipes = CraftingManagerDryingBox.instance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input));
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
            CraftingManagerDryingBox.instance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Drying Unit recipe:"+recipe.toString();
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
            CraftingManagerDryingBox.instance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing "+recipes.size()+" Drying Unit recipes";
        }
    }
}
