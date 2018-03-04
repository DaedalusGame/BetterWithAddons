package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.infuser.InfuserRecipe;
import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerInfuser {
    /** The static instance of this class */
    private static final CraftingManagerInfuser INSTANCE = new CraftingManagerInfuser();
    /** A list of all the recipes added */
    private final List<InfuserRecipe> recipes = Lists.newArrayList();

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
    public void addRecipe(InfuserRecipe recipe)
    {
        recipes.add(recipe);
    }

    public void addRecipe(IRecipe recipe, int spirits)
    {
        addRecipe(new InfuserRecipe(recipe,spirits));
    }

    public void removeRecipe(InfuserRecipe recipe)
    {
        recipes.remove(recipe);
    }

    public void clearRecipes() {
        recipes.clear();
    }
    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public InfuserRecipe findMatchingRecipe(InventoryCrafting craftMatrix, World worldIn)
    {
        for (InfuserRecipe irecipe : this.recipes)
        {
            if (irecipe.internal.matches(craftMatrix, worldIn))
            {
                return irecipe;
            }
        }

        return null;
    }

    public List<InfuserRecipe> getRecipesByOutput(ItemStack stack)
    {
        return this.recipes.stream().filter(irecipe -> irecipe.internal.getRecipeOutput().isItemEqual(stack)).collect(Collectors.toCollection(ArrayList::new));
    }

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting craftMatrix, World worldIn)
    {
        for (InfuserRecipe irecipe : this.recipes)
        {
            if (irecipe.internal.matches(craftMatrix, worldIn))
            {
                return irecipe.internal.getRemainingItems(craftMatrix);
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
    public List<InfuserRecipe> getRecipeList()
    {
        return this.recipes;
    }
}
