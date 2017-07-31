package betterwithaddons.interaction.minetweaker;

import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithaddons.tileentity.TileEntityLureTree.TreeFood;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(LureTree.clazz)
public class LureTree {
    public static final String clazz = "mods.betterwithaddons.LureTree";

    @ZenMethod
    public static void add(@NotNull IItemStack input, int food) {
        ItemStack stack = InputHelper.toStack(input);
        TreeFood r = new TreeFood(stack,food);
        CraftTweakerAPI.apply(new Add(r));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        ItemStack stack = InputHelper.toStack(input);
        CraftTweakerAPI.apply(new Remove(stack));
    }

    public static class Add extends BaseListAddition<TreeFood>
    {
        public Add(TreeFood recipe) {
            super("LureTree", TileEntityLureTree.getTreeFoods(), Lists.newArrayList(recipe));
        }

        @Override
        protected String getRecipeInfo(TreeFood recipe) {
            return recipe.stack.toString();
        }
    }

    public static class Remove extends BaseListRemoval<TreeFood>
    {
        protected Remove(ItemStack stack) {
            super("LureTree", TileEntityLureTree.getTreeFoods(), Lists.newArrayList(TileEntityLureTree.getTreeFood(stack)));
        }

        @Override
        protected String getRecipeInfo(TreeFood recipe) {
            return recipe.stack.toString();
        }
    }
}