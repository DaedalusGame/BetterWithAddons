package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CraftingManagerInfuserTransmutation {
    private static final CraftingManagerInfuserTransmutation instance = new CraftingManagerInfuserTransmutation();

    public static CraftingManagerInfuserTransmutation instance() {
        return instance;
    }

    private final ArrayList<SmeltingRecipe> recipes = new ArrayList<>();

    private CraftingManagerInfuserTransmutation() {
    }

    public void addRecipe(SmeltingRecipe recipe)
    {
        this.recipes.add(recipe);
    }

    public void addRecipe(Object input, ItemStack output) {
        this.recipes.add(createRecipe(input, output));
    }

    protected SmeltingRecipe createRecipe(Object input, ItemStack output)
    {
        return new SmeltingRecipe(input,output);
    }

    public List<SmeltingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
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

    public ItemStack getSmeltingResult(ItemStack input) {
        SmeltingRecipe entry = getSmeltingRecipe(input);
        return entry != null ? entry.getOutput(input) : ItemStack.EMPTY;
    }

    public List<SmeltingRecipe> getRecipes() {
        return this.recipes;
    }
}
