package betterwithaddons.crafting;

import betterwithmods.common.registry.blockmeta.recipe.SawRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DisplaySawRecipe extends SawRecipe {
    ItemStack display = ItemStack.EMPTY;

    public DisplaySawRecipe(Block block, int meta, List<ItemStack> outputs, ItemStack display) {
        super(block, meta, outputs);
        this.display = display;
    }

    @Override
    public ItemStack getStack() {
        return display;
    }
}
