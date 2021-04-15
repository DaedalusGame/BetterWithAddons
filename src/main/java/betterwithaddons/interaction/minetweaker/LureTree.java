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
    public static void add(@NotNull IItemStack input, int amount) {
        CraftTweaker.LATE_ACTIONS.add(new Add(CraftTweakerMC.getItemStack(input), amount));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(input)));
    }

    public static class Add implements IAction
    {
        ItemStack stack;
        int amount;

        public Add(ItemStack stack, int amount) {
            this.stack = stack;
            this.amount = amount;
        }
        
        // Backwards compat snce it's public
        @Deprecated
        public Add(TreeFood recipe) {
            this.stack = recipe.stack;
            this.amount = recipe.amount;
        }

        @Override
        public void apply() {
            TileEntityLureTree.addTreeFood(stack, amount);
        }

        @Override
        public String describe() {
            return "Adding Lure Tree food: "+stack.toString();
        }
    }

    public static class Remove implements IAction
    {
        ItemStack stack;

        protected Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            TreeFood recipe = TileEntityLureTree.getTreeFood(stack);
            if (recipe != null) TileEntityLureTree.getTreeFoods().remove(recipe);
        }

        @Override
        public String describe() {
            return "Removing Lure Tree food: "+stack.toString();
        }
    }
}
