package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;

/**
 * Created by Christian on 19.09.2016.
 */
public class CraftingManagerWaterNet extends CraftingManagerNet {
    private static final CraftingManagerWaterNet instance = new CraftingManagerWaterNet();

    public CraftingManagerWaterNet()
    {
        super(SifterType.WATER);
    }

    public static final CraftingManagerWaterNet getInstance()
    {
        return instance;
    }
}
