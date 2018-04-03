package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.crafting.manager.CraftingManagerFireNet;
import betterwithaddons.crafting.recipes.NetRecipe;
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
@ZenClass(FireNet.clazz)
public class FireNet {
    public static final String clazz = "mods.betterwithaddons.FireNet";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input) {
        NetRecipe r = new NetRecipe(BlockNettedScreen.SifterType.FIRE, new IngredientCraftTweaker(input), 0, CraftTweakerMC.getItemStacks(outputs));
        CraftTweaker.LATE_ACTIONS.add(new Add(r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {

        CraftTweaker.LATE_ACTIONS.add(new Remove(input));
    }

    public static class Add implements IAction
    {
        NetRecipe recipe;

        public Add(NetRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerFireNet.getInstance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Fire Net recipe:"+recipe.toString();
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

            List<NetRecipe> recipes = CraftingManagerFireNet.getInstance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input));
            CraftingManagerFireNet.getInstance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing Fire Net recipe for "+input.getDisplayName();
        }
    }
}
