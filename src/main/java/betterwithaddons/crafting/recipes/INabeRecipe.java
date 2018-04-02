package betterwithaddons.crafting.recipes;

import betterwithaddons.tileentity.TileEntityNabe;
import betterwithaddons.util.NabeResult;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface INabeRecipe extends Comparable<INabeRecipe> {
    int getBoilingTime(TileEntityNabe tile);

    boolean isValidItem(ItemStack stack);

    boolean matches(TileEntityNabe tile, List<ItemStack> stacks);

    NabeResult craft(TileEntityNabe tile, List<ItemStack> stacks);

    String getName();

    int getPriority();

    @Override
    default int compareTo(INabeRecipe o)
    {
        return Integer.compare(getPriority(),o.getPriority());
    }
}
