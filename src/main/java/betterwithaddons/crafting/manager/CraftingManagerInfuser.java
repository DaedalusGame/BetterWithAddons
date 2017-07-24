package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.IInfuserRecipe;
import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.List;

public class CraftingManagerInfuser {
    /** The static instance of this class */
    private static final CraftingManagerInfuser INSTANCE = new CraftingManagerInfuser();
    /** A list of all the recipes added */
    private final List<IInfuserRecipe> recipes = Lists.<IInfuserRecipe>newArrayList();

    /**
     * Returns the static instance of this class
     */
    public static CraftingManagerInfuser getInstance()
    {
        /** The static instance of this class */
        return INSTANCE;
    }

    private CraftingManagerInfuser()
    {
    }

    /**
     * Adds an IRecipe to the list of crafting recipes.
     */
    public void addRecipe(IInfuserRecipe recipe)
    {
        this.recipes.add(recipe);
    }

    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public IInfuserRecipe findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn)
    {
        for (IInfuserRecipe irecipe : this.recipes)
        {
            if (irecipe.matches(craftMatrix, worldIn))
            {
                return irecipe;
            }
        }

        return null;
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn)
    {
        for (IInfuserRecipe irecipe : this.recipes)
        {
            if (irecipe.matches(craftMatrix, worldIn))
            {
                return irecipe.getRemainingItems(craftMatrix);
            }
        }

        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(craftMatrix.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            nonnulllist.set(i, craftMatrix.getStackInSlot(i));
        }

        return nonnulllist;
    }

    /**
     * returns the List<> of all recipes
     */
    public List<IInfuserRecipe> getRecipeList()
    {
        return this.recipes;
    }
}
