package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.crafting.recipes.PackingRecipe;
import betterwithaddons.crafting.recipes.SpindleRecipe;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerPacking
{
    private static final CraftingManagerPacking instance = new CraftingManagerPacking();
    private List<PackingRecipe> recipes;

    public CraftingManagerPacking()
    {
        this.recipes = new ArrayList<>();
    }

    public static final CraftingManagerPacking getInstance()
    {
        return instance;
    }

    public void addRecipe(ItemStack[] outputs, ItemStack input)
    {
        addRecipe(outputs, (Object)input);
    }

    public void addRecipe(ItemStack[] outputs, OreStack input)
    {
        addRecipe(outputs, (Object)input);
    }

    public void addRecipe(ItemStack[] outputs, Object input)
    {
        recipes.add(createRecipe(outputs, input));
    }

    public List<PackingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    public boolean removeRecipe(ItemStack[] outputs, Object input)
    {
        PackingRecipe recipe = createRecipe(outputs, input);
        int matchingIndex = getMatchingRecipeIndex(recipe);

        if(matchingIndex >= 0)
        {
            this.recipes.remove(matchingIndex);
            return true;
        }
        return false;
    }

    private int getMatchingRecipeIndex(PackingRecipe recipe)
    {
        for(int i = 0; i < this.recipes.size(); i++)
        {
            PackingRecipe tempRecipe = this.recipes.get(i);
            if(tempRecipe.matches(recipe))
                return i;
        }
        return -1;
    }

    private boolean containsIngredient(Object input, ItemStack stack)
    {
        if(input instanceof ItemStack)
        {
            if(ItemStack.areItemsEqual((ItemStack)input, stack) || (((ItemStack)input).getItemDamage() == OreDictionary.WILDCARD_VALUE && stack.getItem() == ((ItemStack)input).getItem()))
                return true;
        }
        else if(input instanceof OreStack)
        {
            if(InventoryUtil.listContains(stack, ((OreStack)input).getOres()))
                return true;
        }
        return false;
    }

    public PackingRecipe getMostValidRecipe(List<EntityItem> inv)
    {
        List<PackingRecipe> recipes = getValidCraftingRecipes(inv);

        if(recipes == null || recipes.size() == 0)
            return null;

        return recipes.get(0);
    }

    public List<PackingRecipe> getValidCraftingRecipes(List<EntityItem> inv)
    {
        ArrayList<PackingRecipe> validrecipes = recipes.stream().filter(recipe -> recipe.matches(inv)).collect(Collectors.toCollection(ArrayList::new));

        return validrecipes;
    }

    private PackingRecipe createRecipe(ItemStack[] outputs, Object input)
    {
        return new PackingRecipe(input, outputs);
    }

    public List<PackingRecipe> getRecipes()
    {
        return this.recipes;
    }
}
