package betterwithaddons.crafting.recipes;

import betterwithaddons.handler.RotHandler;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public class FoodCombiningRecipe extends ShapelessOreRecipe {
    public FoodCombiningRecipe() {
        super(ItemStack.EMPTY, new Object[0]);
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack prevMatch = ItemStack.EMPTY;
        int matches = 0;

        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);

            if(stack.isEmpty())
                continue;

            if(RotHandler.isRottingItem(stack))
            {
                if(prevMatch.isEmpty())
                    prevMatch = stack.copy();
                else
                {
                    RotHandler.setCreationDate(prevMatch,RotHandler.getCreationDate(stack));
                    if (stack.getItem() != prevMatch.getItem() || stack.getMetadata() != prevMatch.getMetadata() || !ItemStack.areItemStackTagsEqual(prevMatch, stack)) {
                        return false;
                    }
                }
                matches++;
            }
        }

        if(matches > prevMatch.getMaxStackSize())
            return false;

        return matches >= 2;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack prevMatch = ItemStack.EMPTY;
        int matches = 0;
        long latestDate = Long.MAX_VALUE;

        for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);

            if(stack.isEmpty())
                continue;

            if(RotHandler.isRottingItem(stack))
            {
                if(prevMatch.isEmpty())
                    prevMatch = stack.copy();
                else
                {
                    long date = RotHandler.getCreationDate(stack);
                    latestDate = Math.min(latestDate,date);
                    RotHandler.setCreationDate(prevMatch,date);
                    if (stack.getItem() != prevMatch.getItem() || stack.getMetadata() != prevMatch.getMetadata() || !ItemStack.areItemStackTagsEqual(prevMatch, stack)) {
                        return ItemStack.EMPTY;
                    }
                }
                matches++;
            }
        }

        if(matches > prevMatch.getMaxStackSize())
            return ItemStack.EMPTY;

        prevMatch.setCount(matches);
        RotHandler.setCreationDate(prevMatch,latestDate);

        return matches >= 2 ? prevMatch : ItemStack.EMPTY;
    }
}
