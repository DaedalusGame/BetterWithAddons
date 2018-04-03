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
        CraftTweaker.LATE_ACTIONS.add(new Remove(input));
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
        private IItemStack input;

        public Remove(IItemStack input) {
            this.input = input;
        }

        @Override
        public void apply() {
            List<CherryBoxRecipe> recipes = CraftingManagerDryingBox.instance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input));
            CraftingManagerDryingBox.instance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing Drying Unit recipe for "+input.getDisplayName();
        }
    }
}
