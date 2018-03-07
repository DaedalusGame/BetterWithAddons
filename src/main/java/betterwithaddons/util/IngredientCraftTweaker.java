package betterwithaddons.util;

import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientCraftTweaker extends Ingredient {
    IIngredient predicate;

    public IngredientCraftTweaker(IIngredient ingredient)
    {
        predicate = ingredient;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        List<IItemStack> stacks = predicate != null ? predicate.getItems() : new ArrayList<>();
        return InputHelper.toStacks(stacks.toArray(new IItemStack[stacks.size()]));
    }

    @Override
    public boolean apply(ItemStack stack) {
        if(predicate == null)
            return stack.isEmpty();
        return predicate.matches(InputHelper.toIItemStack(stack));
    }
}