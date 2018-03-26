package betterwithaddons.crafting.recipes;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CherryBoxRecipe extends SmeltingRecipe {
    CherryBoxType type;

    public CherryBoxRecipe(CherryBoxType type, Ingredient input, ItemStack output) {
        super(input, output);
        this.type = type;
    }

    public CherryBoxType getType() {
        return type;
    }
}
