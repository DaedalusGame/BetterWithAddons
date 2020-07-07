package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.ResultBlock;
import betterwithaddons.crafting.recipes.PackingRecipe;
import com.google.common.collect.Lists;
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

    public void addRecipe(IBlockState output, ItemStack jeiOutput, Ingredient input) {
        recipes.add(new PackingRecipe(Lists.newArrayList(input), new ResultBlock(output,jeiOutput)));
    }

    public List<PackingRecipe> getRecipes()
    {
        return this.recipes;
    }
}
