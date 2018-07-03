package betterwithaddons.util;

import betterwithaddons.item.ItemTea;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.TeaType.ItemType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class IngredientTea extends Ingredient {
    TeaType type;
    ItemType itemType;

    public IngredientTea(TeaType type, ItemType itemType) {
        super(0);
        this.type = type;
        this.itemType = itemType;
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemTea && ItemTea.getType(stack) == type && ItemTea.getItemType(stack) == itemType;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        return new ItemStack[]{getTeaStack()};
    }

    public ItemStack getTeaStack() {
        switch (itemType) {
            case Leaves:
                return ModItems.TEA_LEAVES.getStack(type);
            case Soaked:
                return ModItems.TEA_SOAKED.getStack(type);
            case Wilted:
                return ModItems.TEA_WILTED.getStack(type);
            case Powder:
                return ModItems.TEA_POWDER.getStack(type);
            default:
                return ItemStack.EMPTY;
        }
    }
}
