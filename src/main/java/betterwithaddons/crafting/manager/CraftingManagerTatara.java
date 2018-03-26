package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.SmeltingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerTatara {
    private static final CraftingManagerTatara instance = new CraftingManagerTatara();

    public static CraftingManagerTatara instance() {
        return instance;
    }

    private final ArrayList<SmeltingRecipe> recipes = new ArrayList<>();

    private CraftingManagerTatara() {
    }

    public void addRecipe(Ingredient input, ItemStack output) {
        this.recipes.add(createRecipe(input, output));
    }

    public void addRecipe(SmeltingRecipe recipe) {
        this.recipes.add(recipe);
    }

    protected SmeltingRecipe createRecipe(Ingredient input, ItemStack output)
    {
        return new SmeltingRecipe(input,output);
    }

    public SmeltingRecipe getSmeltingRecipe(ItemStack input) {
        Iterator<SmeltingRecipe> var2 = this.recipes.iterator();

        SmeltingRecipe entry;
        do {
            if(!var2.hasNext()) {
                return null;
            }

            entry = var2.next();
        } while(!entry.matchesInput(input));

        return entry;
    }

    public List<SmeltingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    public List<SmeltingRecipe> getRecipes() {
        return this.recipes;
    }
}
