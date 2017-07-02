package betterwithaddons.crafting.recipes;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import net.minecraft.item.ItemStack;

public class CherryBoxRecipe extends SmeltingRecipe {
    CherryBoxType type;

    public CherryBoxRecipe(CherryBoxType type, Object input, ItemStack output) {
        super(input, output);
        this.type = type;
    }

    public CherryBoxType getType() {
        return type;
    }
}
