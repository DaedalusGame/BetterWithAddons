package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.PackingRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

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

    public void addRecipe(PackingRecipe recipe) {
        recipes.add(recipe);
    }

    public void addRecipe(IBlockState output, ItemStack jeiOutput, Ingredient input)
    {
        PackingRecipe recipe = createRecipe(output, input);
        recipe.setJeiOutput(jeiOutput);
        recipes.add(recipe);
    }

    public void addRecipe(IBlockState output, Ingredient input)
    {
        recipes.add(createRecipe(output, input));
    }

    public List<PackingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
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

    private PackingRecipe createRecipe(IBlockState output, Ingredient input)
    {
        return new PackingRecipe(input, output);
    }

    public List<PackingRecipe> getRecipes()
    {
        return this.recipes;
    }
}
