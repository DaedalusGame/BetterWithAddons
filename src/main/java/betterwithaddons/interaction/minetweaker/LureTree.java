package betterwithaddons.interaction.minetweaker;

import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithaddons.tileentity.TileEntityLureTree.TreeFood;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(LureTree.clazz)
public class LureTree {
    public static final String clazz = "mods.betterwithaddons.LureTree";

    @ZenMethod
    public static void add(@NotNull IItemStack input, int food) {
        ItemStack stack = CraftTweakerMC.getItemStack(input);
        TreeFood r = new TreeFood(stack,food);
        CraftTweaker.LATE_ACTIONS.add(new Add(r));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        ItemStack stack = CraftTweakerMC.getItemStack(input);
        CraftTweaker.LATE_ACTIONS.add(new Remove(stack));
    }

    public static class Add implements IAction
    {
        TreeFood recipe;

        public Add(TreeFood recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            TileEntityLureTree.addTreeFood(recipe);
        }

        @Override
        public String describe() {
            return "Adding Lure Tree food: "+recipe.stack.toString();
        }
    }

    public static class Remove implements IAction
    {
        TreeFood recipe;

        protected Remove(ItemStack stack) {
            recipe = TileEntityLureTree.getTreeFood(stack);
        }

        @Override
        public void apply() {
            TileEntityLureTree.getTreeFoods().remove(recipe);
        }

        @Override
        public String describe() {
            return "Removing Lure Tree food:"+recipe.stack.toString();
        }
    }
}