package betterwithaddons.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;

public class MultiOreIngredient extends Ingredient {
    public ArrayList<OreIngredient> internal = new ArrayList<>();
    public ItemStack[] matchingStacks;

    public MultiOreIngredient(String... ores) {
        for (String ore : ores) {
            internal.add(new OreIngredient(ore));
        }
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        if(matchingStacks == null) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            for (OreIngredient ingredient : internal) {
                for (ItemStack oreStack : ingredient.getMatchingStacks()) {
                    if(!stacks.stream().anyMatch(existing -> existing.isItemEqual(oreStack)))
                        stacks.add(oreStack);
                }
            }
            matchingStacks = stacks.toArray(new ItemStack[stacks.size()]);
        }

        return matchingStacks;
    }

    @Override
    protected void invalidate() {
        matchingStacks = null;
        super.invalidate();
    }
}
