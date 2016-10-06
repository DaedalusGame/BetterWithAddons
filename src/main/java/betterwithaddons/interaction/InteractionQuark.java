package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Christian on 13.08.2016.
 */
public class InteractionQuark implements IInteraction {
    final String modid = "Quark";

    @Override
    public boolean isActive() {
        return Loader.isModLoaded(modid);
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        if(!isActive())
            return;

        ItemStack midori = InteractionHelper.findBlock(modid,"midori_block",4,0);

        BetterWithAddons.removeCraftingRecipe(midori);
        GameRegistry.addShapedRecipe(midori,"mm","mm",'m',ModItems.material.getMaterial("midori_popped"));
    }

    @Override
    public void postInit() {
    }
}
