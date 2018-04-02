package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.INabeRecipe;
import betterwithaddons.crafting.recipes.ShapelessNabeRecipe;
import betterwithaddons.tileentity.TileEntityNabe;
import betterwithaddons.util.NabeResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerNabe
{
    private static final CraftingManagerNabe instance = new CraftingManagerNabe();
    private List<INabeRecipe> recipes;

    public CraftingManagerNabe()
    {
        this.recipes = new ArrayList<>();
    }

    public static CraftingManagerNabe getInstance()
    {
        return instance;
    }

    public void addRecipe(String name, NabeResult result, List<Ingredient> input, int time)
    {
        recipes.add(createRecipe(name, result, input, time));
    }

    public void addRecipe(INabeRecipe recipe)
    {
        recipes.add(recipe);
    }

    public List<INabeRecipe> findRecipeForRemoval(@Nonnull String name) {
        return recipes.stream().filter(recipe -> recipe.getName().equals(name)).collect(Collectors.toList());
    }

    public INabeRecipe getMostValidRecipe(TileEntityNabe tile, List<ItemStack> inv)
    {
        List<INabeRecipe> recipes = getValidCraftingRecipes(tile, inv);

        INabeRecipe picked = null;
        for (INabeRecipe recipe : recipes) {
            if(picked == null || recipe.compareTo(picked) > 0)
                picked = recipe;
        }

        return picked;
    }

    public List<INabeRecipe> getValidCraftingRecipes(TileEntityNabe tile,List<ItemStack> inv)
    {
        return recipes.stream().filter(recipe -> recipe.matches(tile,inv)).collect(Collectors.toCollection(ArrayList::new));
    }

    private INabeRecipe createRecipe(String name, NabeResult result, List<Ingredient> input, int time)
    {
        return new ShapelessNabeRecipe(name,result,input,time);
    }

    public List<INabeRecipe> getRecipes()
    {
        return this.recipes;
    }

    public boolean isValidItem(ItemStack stack) {
        return recipes.stream().anyMatch(recipe -> recipe.isValidItem(stack));
    }
}
