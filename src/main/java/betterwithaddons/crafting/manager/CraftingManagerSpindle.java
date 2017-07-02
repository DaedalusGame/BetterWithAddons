package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.crafting.recipes.SpindleRecipe;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerSpindle
{
    private static final CraftingManagerSpindle instance = new CraftingManagerSpindle();
    private List<SpindleRecipe> recipes;

    public CraftingManagerSpindle()
    {
        this.recipes = new ArrayList<SpindleRecipe>();
    }

    public static final CraftingManagerSpindle getInstance()
    {
        return instance;
    }

    public void addRecipe(ItemStack[] outputs, ItemStack input, boolean consumesSpindle)
    {
        addRecipe(outputs, (Object)input, consumesSpindle);
    }

    public void addRecipe(ItemStack[] outputs, OreStack input, boolean consumesSpindle)
    {
        addRecipe(outputs, (Object)input, consumesSpindle);
    }

    public void addRecipe(ItemStack[] outputs, Object input, boolean consumesSpindle)
    {
        recipes.add(createRecipe(outputs, input, consumesSpindle));
    }

    public List<SpindleRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    public boolean removeRecipe(ItemStack[] outputs, Object input)
    {
        SpindleRecipe recipe = createRecipe(outputs, input, true);
        int matchingIndex = getMatchingRecipeIndex(recipe);

        if(matchingIndex >= 0)
        {
            this.recipes.remove(matchingIndex);
            return true;
        }
        return false;
    }

    private int getMatchingRecipeIndex(SpindleRecipe recipe)
    {
        for(int i = 0; i < this.recipes.size(); i++)
        {
            SpindleRecipe tempRecipe = this.recipes.get(i);
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

    public SpindleRecipe getMostValidRecipe(List<EntityItem> inv)
    {
        List<SpindleRecipe> recipes = getValidCraftingRecipes(inv);

        if(recipes == null || recipes.size() == 0)
            return null;

        return recipes.get(0);
    }

    public List<SpindleRecipe> getValidCraftingRecipes(List<EntityItem> inv)
    {
        ArrayList<SpindleRecipe> validrecipes = new ArrayList<SpindleRecipe>();

        for (SpindleRecipe recipe: recipes) {
            if(recipe.matches(inv))
            {
                validrecipes.add(recipe);
            }
        }

        return validrecipes;
    }

    private SpindleRecipe createRecipe(ItemStack[] outputs, Object input, boolean consumesSpindle)
    {
        return new SpindleRecipe(consumesSpindle, input, outputs);
    }

    public List<SpindleRecipe> getRecipes()
    {
        return this.recipes;
    }

    //Lazy way of ensuring the ore dictionary entries were properly implemented.
    public void refreshRecipes()
    {
        List<SpindleRecipe> recipes = getRecipes();
        if(!recipes.isEmpty())
        {
            this.recipes = new ArrayList<SpindleRecipe>();
            for(SpindleRecipe r : recipes) {
                this.recipes.add(createRecipe(r.getOutput().toArray(new ItemStack[0]), r.input, r.willConsumeSpindle()));
            }
        }
    }
}
