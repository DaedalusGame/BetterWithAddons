package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;

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
