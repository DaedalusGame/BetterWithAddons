package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.crafting.recipes.SpindleRecipe;
import betterwithaddons.util.IngredientCraftTweaker;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass(Spindle.clazz)
public class Spindle {
    public static final String clazz = "mods.betterwithaddons.Spindle";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input, boolean consumesSpindle) {
        SpindleRecipe r = new SpindleRecipe(consumesSpindle, new IngredientCraftTweaker(input), CraftTweakerMC.getItemStacks(outputs));
        CraftTweaker.LATE_ACTIONS.add(new Add(r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        CraftTweaker.LATE_ACTIONS.add(new Remove(input));
    }

    public static class Add implements IAction
    {
        SpindleRecipe recipe;

        public Add(SpindleRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerSpindle.getInstance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Drying Unit recipe:"+recipe.toString();
        }
    }

    public static class Remove implements IAction
    {
        IItemStack stack;

        public Remove(IItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            List<SpindleRecipe> recipes = CraftingManagerSpindle.getInstance().findRecipeForRemoval(CraftTweakerMC.getItemStack(stack));
            CraftingManagerSpindle.getInstance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing Spindle recipe for "+stack.getDisplayName();
        }
    }
}
