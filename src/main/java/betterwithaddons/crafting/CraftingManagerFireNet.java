package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;

/**
 * Created by Christian on 19.09.2016.
 */
public class CraftingManagerFireNet extends CraftingManagerNet {
    private static final CraftingManagerFireNet instance = new CraftingManagerFireNet();

    public CraftingManagerFireNet()
    {
        super(SifterType.FIRE);
    }

    public static final CraftingManagerFireNet getInstance()
    {
        return instance;
    }
}
