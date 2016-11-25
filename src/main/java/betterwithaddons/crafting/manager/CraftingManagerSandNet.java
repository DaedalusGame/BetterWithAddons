package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;

public class CraftingManagerSandNet extends CraftingManagerNet {
    private static final CraftingManagerSandNet instance = new CraftingManagerSandNet();

    public CraftingManagerSandNet()
    {
        super(SifterType.SAND);
    }

    public static final CraftingManagerSandNet getInstance()
    {
        return instance;
    }
}
