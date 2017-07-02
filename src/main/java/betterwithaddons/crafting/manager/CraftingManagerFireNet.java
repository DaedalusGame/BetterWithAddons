package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.recipes.NetRecipe;
import net.minecraft.item.ItemStack;

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
