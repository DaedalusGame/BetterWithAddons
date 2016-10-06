package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;

/**
 * Created by Christian on 28.09.2016.
 */
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
