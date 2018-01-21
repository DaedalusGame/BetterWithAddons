package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerPacking;
import betterwithaddons.crafting.recipes.PackingRecipe;
import betterwithaddons.interaction.InteractionCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Packing.clazz)
public class Packing {
    public static final String clazz = "mods.betterwithaddons.Packing";

    @ZenMethod
    public static void add(IItemStack output, IIngredient input) {
        ItemStack stack = InputHelper.toStack(output);
        if(InputHelper.isABlock(stack)) {
            Block block = ((ItemBlock) stack.getItem()).getBlock();
            PackingRecipe r = new PackingRecipe(InputHelper.toObject(input), block.getStateFromMeta(stack.getMetadata()));
            r.setJeiOutput(stack);
            InteractionCraftTweaker.LATE_ADDITIONS.add(new Add(r));
        }
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        InteractionCraftTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }

    public static class Add extends BaseListAddition<PackingRecipe>
    {
        public Add(PackingRecipe packingRecipe) {
            super("Packing", CraftingManagerPacking.getInstance().getRecipes(), Lists.newArrayList(packingRecipe));
        }

        @Override
        protected String getRecipeInfo(PackingRecipe recipe) {
            return recipe.getInput().toString();
        }
    }

    public static class Remove extends BaseListRemoval<PackingRecipe>
    {
        protected Remove(ItemStack input) {
            super("Packing", CraftingManagerPacking.getInstance().getRecipes(), CraftingManagerPacking.getInstance().findRecipeForRemoval(input));
        }

        @Override
        protected String getRecipeInfo(PackingRecipe recipe) {
            return recipe.getInput().toString();
        }
    }
}
