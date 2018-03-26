package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CraftingManagerCherryBox {
    //private final Map<ItemStack, ItemStack> workingList = Maps.newHashMap();
    private final ArrayList<CherryBoxRecipe> recipes = new ArrayList<>();

    public CraftingManagerCherryBox() {
    }

    public CherryBoxType getType() {
        return CherryBoxType.NONE;
    }

    public void addRecipe(Ingredient input, ItemStack output) {
        this.recipes.add(createRecipe(input, output));
    }

    public void addRecipe(CherryBoxRecipe recipe) {
        this.recipes.add(recipe);
    }

    protected CherryBoxRecipe createRecipe(Ingredient input, ItemStack output)
    {
        return new CherryBoxRecipe(getType(),input,output);
    }

    public List<CherryBoxRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    @Nullable
    public ItemStack getWorkResult(ItemStack input) {
        Iterator<CherryBoxRecipe> var2 = this.recipes.iterator();

        CherryBoxRecipe entry;
        do {
            if(!var2.hasNext()) {
                return ItemStack.EMPTY;
            }

            entry = var2.next();
        } while(!entry.matchesInput(input));

        return entry.getOutput(input);
    }

    public List<CherryBoxRecipe> getRecipes() {
        return this.recipes;
    }
}
