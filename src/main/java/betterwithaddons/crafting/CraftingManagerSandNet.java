package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;

/**
 * Created by Christian on 19.09.2016.
 */
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
