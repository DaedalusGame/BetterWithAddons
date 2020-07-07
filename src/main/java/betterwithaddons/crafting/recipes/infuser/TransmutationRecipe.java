package betterwithaddons.crafting.recipes.infuser;

import betterwithaddons.crafting.recipes.SmeltingRecipe;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class TransmutationRecipe extends SmeltingRecipe {
    private int requiredSpirit;
    private Random random = new Random();
    private ItemStack[] possibleOutputs;

    public TransmutationRecipe(Ingredient input, int requiredSpirit, ItemStack output) {
        this(input, requiredSpirit, new ItemStack[]{output});
    }
    
    public TransmutationRecipe(Ingredient input, int requiredSpirit, ItemStack[] outputs) {
        super(input, outputs.length == 1 ? outputs[0] : ItemStack.EMPTY);
        this.requiredSpirit = requiredSpirit;
        this.possibleOutputs = outputs;
    }
    
    @Override
    public ItemStack getOutput(ItemStack input) {
        int i = random.nextInt(possibleOutputs.length);
        return possibleOutputs[i];
    }
    
    @Override
    public List<ItemStack> getRecipeOutputs() {
        return Arrays.asList(possibleOutputs);
    }

    public int getRequiredSpirit(ItemStack input) {
        return requiredSpirit;
    }

    public int getRecipeRequiredSpirit() { return requiredSpirit; }

    public boolean matchesInput(ItemStack item, int spirits) {
        if(spirits < requiredSpirit)
            return false;
        
        if (Arrays.asList(possibleOutputs).stream().map(ItemStack::getItem).anyMatch((itemStack) -> itemStack == item.getItem()))
            return false;

        return matchesInput(item);
    }
}
