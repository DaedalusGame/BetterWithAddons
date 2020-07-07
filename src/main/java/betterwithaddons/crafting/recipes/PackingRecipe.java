package betterwithaddons.crafting.recipes;

import betterwithaddons.crafting.ICraftingResult;
import betterwithaddons.util.ItemUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PackingRecipe
{
    public ICraftingResult output;
    public List<Ingredient> inputs;

    public PackingRecipe(List<Ingredient> inputs, ICraftingResult output) {
        this.output = output;
        this.inputs = inputs;
    }

    public ICraftingResult getOutput(List<ItemStack> inputs, IBlockState compressState) {
        return this.output.copy();
    }

    public boolean consume(List<ItemStack> inputs, IBlockState compressState, boolean simulate)
    {
        inputs = new ArrayList<>(inputs);
        for (Ingredient ingredient : this.inputs) {
            boolean matches = false;
            Iterator<ItemStack> iterator = inputs.iterator();
            while(iterator.hasNext()) {
                ItemStack checkStack = iterator.next();
                if(ingredient.apply(checkStack)) {
                    if(!simulate)
                        checkStack.shrink(ItemUtil.getSize(ingredient));
                    iterator.remove();
                    matches = true;
                }
            }
            if(!matches)
                return false;
        }
        return true;
    }
}