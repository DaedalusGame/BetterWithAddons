package betterwithaddons.crafting;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeWithReturn implements IRecipe {
    ItemStack[] inputItems;
    ItemStack outputItem;
    ItemStack[] remainItems;

    boolean onlyFilledSlots;

    public RecipeWithReturn(ItemStack out, ItemStack[] remain, ItemStack in[], boolean onlyFilled)
    {
        inputItems = in;
        outputItem = out;
        remainItems = remain;
        onlyFilledSlots = onlyFilled;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        List<ItemStack> list = Lists.newArrayList(this.inputItems);

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                boolean flag = false;

                for (ItemStack itemstack1 : list)
                {
                    if (matchItem(itemstack, itemstack1))
                    {
                        flag = true;
                        list.remove(itemstack1);
                        break;
                    }
                }

                if (!flag)
                {
                    return false;
                }
            }
        }

        return list.isEmpty();
    }

    private boolean matchItem(ItemStack in, ItemStack match)
    {
        return in.isItemEqual(match) || (in.getItem() == match.getItem() && in.getMetadata() == OreDictionary.WILDCARD_VALUE);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Nullable
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        return outputItem.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 10;
    }

    @Nullable
    public ItemStack getRecipeOutput()
    {
        return outputItem;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> aitemstack = NonNullList.withSize(inv.getSizeInventory(),ItemStack.EMPTY);


        for (int i = 0; i < remainItems.length; ++i)
        {
            if(!remainItems[i].isEmpty())
            for (int e = 1; e < aitemstack.size(); ++e)
            {
                ItemStack itemstack = inv.getStackInSlot(e);
                if (itemstack.isEmpty() && onlyFilledSlots)
                    continue;
                aitemstack.set(i,remainItems[i].copy());
            }
        }

        return aitemstack;
    }
}
