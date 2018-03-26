package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerPacking;
import betterwithaddons.crafting.recipes.PackingRecipe;
import betterwithaddons.util.IngredientCraftTweaker;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenRegister
@ZenClass(Packing.clazz)
public class Packing {
    public static final String clazz = "mods.betterwithaddons.Packing";

    @ZenMethod
    public static void add(IItemStack output, IIngredient input) {
        ItemStack stack = CraftTweakerMC.getItemStack(output);
        if(stack.getItem() instanceof ItemBlock) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            PackingRecipe r = new PackingRecipe(new IngredientCraftTweaker(input), block.getStateFromMeta(stack.getMetadata()));
            r.setJeiOutput(stack);
            CraftTweaker.LATE_ACTIONS.add(new Add(r));
        }
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftingManagerPacking.getInstance().findRecipeForRemoval(CraftTweakerMC.getItemStack(input))));
    }

    public static class Add implements IAction
    {
        PackingRecipe recipe;

        public Add(PackingRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerPacking.getInstance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Hardcore Packing recipe:"+recipe.toString();
        }
    }

    public static class Remove implements IAction
    {
        List<PackingRecipe> recipes;

        public Remove(List<PackingRecipe> recipes) {
            this.recipes = recipes;
        }

        @Override
        public void apply() {
            CraftingManagerPacking.getInstance().getRecipes().removeAll(recipes);
        }

        @Override
        public String describe() {
            return "Removing "+recipes.size()+" Hardcore Packing recipes";
        }
    }
}
