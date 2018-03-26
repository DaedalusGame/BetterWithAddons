package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.SpindleRecipe;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

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
        this.recipes = new ArrayList<>();
    }

    public static CraftingManagerSpindle getInstance()
    {
        return instance;
    }

    public void addRecipe(ItemStack[] outputs, Ingredient input, boolean consumesSpindle)
    {
        recipes.add(createRecipe(outputs, input, consumesSpindle));
    }

    public void addRecipe(SpindleRecipe recipe)
    {
        recipes.add(recipe);
    }

    public List<SpindleRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
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

        return recipes.stream().filter(recipe -> recipe.matches(inv)).collect(Collectors.toCollection(ArrayList::new));
    }

    private SpindleRecipe createRecipe(ItemStack[] outputs, Ingredient input, boolean consumesSpindle)
    {
        return new SpindleRecipe(consumesSpindle, input, outputs);
    }

    public List<SpindleRecipe> getRecipes()
    {
        return this.recipes;
    }
}
