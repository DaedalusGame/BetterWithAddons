package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;

public class CraftingManagerDryingBox extends CraftingManagerCherryBox {
    private static final CraftingManagerDryingBox instance = new CraftingManagerDryingBox();

    public static CraftingManagerDryingBox instance() {
        return instance;
    }

    @Override
    public CherryBoxType getType() {
        return CherryBoxType.DRYING;
    }
}
