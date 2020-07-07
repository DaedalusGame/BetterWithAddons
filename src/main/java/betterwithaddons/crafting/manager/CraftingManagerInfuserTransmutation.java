package betterwithaddons.crafting.manager;

import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerInfuserTransmutation {
    private static final CraftingManagerInfuserTransmutation instance = new CraftingManagerInfuserTransmutation();

    public static CraftingManagerInfuserTransmutation getInstance() {
        return instance;
    }

    private final ArrayList<TransmutationRecipe> recipes = new ArrayList<>();

    private CraftingManagerInfuserTransmutation() {
    }

    public void addRecipe(TransmutationRecipe recipe)
    {
        this.recipes.add(recipe);
    }

    public void addRecipe(Ingredient input, int spirits, ItemStack output) {
        this.recipes.add(createRecipe(input, spirits, output));
    }
    
    public void addRecipe(Ingredient input, int spirits, ItemStack[] outputs) {
        this.recipes.add(createRecipe(input, spirits, outputs));
    }

    public void removeRecipe(TransmutationRecipe recipe) {
        this.recipes.remove(recipe);
    }

    public void clearRecipes() {
        this.recipes.clear();
    }

    protected TransmutationRecipe createRecipe(Ingredient input, int spirits, ItemStack output)
    {
        return createRecipe(input, spirits, new ItemStack[]{output});
    }
    
    protected TransmutationRecipe createRecipe(Ingredient input, int spirits, ItemStack[] outputs)
    {
        return new TransmutationRecipe(input, spirits, outputs);
    }

    public List<TransmutationRecipe> findRecipeForRemoval(@Nonnull ItemStack output) {
        return recipes.stream().filter(recipe -> recipe.getRecipeOutputs().contains(output)).collect(Collectors.toList());
    }

    public TransmutationRecipe getSmeltingRecipe(ItemStack input, int spirits) {
        Iterator<TransmutationRecipe> var2 = this.recipes.iterator();

        TransmutationRecipe entry;
        do {
            if(!var2.hasNext()) {
                return null;
            }

            entry = var2.next();
        } while(!entry.matchesInput(input, spirits));

        return entry;
    }

    public List<TransmutationRecipe> getRecipes() {
        return this.recipes;
    }
}
