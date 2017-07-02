package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftingManagerTatara {
    private static final CraftingManagerTatara instance = new CraftingManagerTatara();

    public static CraftingManagerTatara instance() {
        return instance;
    }

    private final ArrayList<SmeltingRecipe> recipes = new ArrayList<>();

    private CraftingManagerTatara() {
    }


    public BlockCherryBox.CherryBoxType getType() {
        return BlockCherryBox.CherryBoxType.NONE;
    }

    public void addRecipe(Object input, ItemStack output) {
        this.recipes.add(createRecipe(input, output));
    }

    protected SmeltingRecipe createRecipe(Object input, ItemStack output)
    {
        return new CherryBoxRecipe(getType(),input,output);
    }

    public List<SmeltingRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }


    @Nullable
    public ItemStack getSmeltingResult(ItemStack input) {
        Iterator<SmeltingRecipe> var2 = this.recipes.iterator();

        SmeltingRecipe entry;
        do {
            if(!var2.hasNext()) {
                return ItemStack.EMPTY;
            }

            entry = var2.next();
        } while(!entry.matchesInput(input));

        return entry.getOutput();
    }

    public List<SmeltingRecipe> getRecipes() {
        return this.recipes;
    }
}
