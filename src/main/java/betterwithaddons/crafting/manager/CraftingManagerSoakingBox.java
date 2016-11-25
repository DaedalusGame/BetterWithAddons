package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;

public class CraftingManagerSoakingBox extends CraftingManagerCherryBox {
    private static final CraftingManagerSoakingBox instance = new CraftingManagerSoakingBox();

    public static CraftingManagerSoakingBox instance() {
        return instance;
    }

    @Override
    public CherryBoxType getType() {
        return CherryBoxType.SOAKING;
    }
}
