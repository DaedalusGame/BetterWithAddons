package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;

/**
 * Created by Christian on 28.09.2016.
 */
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
