package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
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

    private final ArrayList<TransmutationRecipe> recipes = new ArrayList<>();

    private CraftingManagerInfuserTransmutation() {
    }

    public void addRecipe(TransmutationRecipe recipe)
    {
        this.recipes.add(recipe);
    }

    public void addRecipe(Object input, int spirits, ItemStack output) {
        this.recipes.add(createRecipe(input, spirits, output));
    }

    protected TransmutationRecipe createRecipe(Object input, int spirits, ItemStack output)
    {
        return new TransmutationRecipe(input, spirits, output);
    }

    public List<TransmutationRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
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
