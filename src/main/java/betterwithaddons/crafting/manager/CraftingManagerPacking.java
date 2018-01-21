package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.crafting.recipes.PackingRecipe;
import betterwithaddons.crafting.recipes.SpindleRecipe;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.state.IBlockState;
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

    public static CraftingManagerPacking getInstance()
    {
        return instance;
    }

    public void addRecipe(IBlockState output, Object jeiOutput, Object input)
    {
        PackingRecipe recipe = createRecipe(output, input);
        recipe.setJeiOutput(jeiOutput);
        recipes.add(recipe);
    }

    public void addRecipe(IBlockState output, Object input)
    {
        recipes.add(createRecipe(output, input));
    }

    public List<PackingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    public boolean removeRecipe(IBlockState output, Object input)
    {
        PackingRecipe recipe = createRecipe(output, input);
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

    public PackingRecipe getMostValidRecipe(IBlockState compressState, List<EntityItem> inv)
    {
        List<PackingRecipe> recipes = getValidCraftingRecipes(compressState,inv);

        if(recipes == null || recipes.size() == 0)
            return null;

        return recipes.get(0);
    }

    public List<PackingRecipe> getValidCraftingRecipes(IBlockState compressState, List<EntityItem> inv)
    {

        return recipes.stream().filter(recipe -> recipe.matches(compressState,inv)).collect(Collectors.toCollection(ArrayList::new));
    }

    private PackingRecipe createRecipe(IBlockState output, Object input)
    {
        return new PackingRecipe(input, output);
    }

    public List<PackingRecipe> getRecipes()
    {
        return this.recipes;
    }
}
