package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class InteractionQuark implements IInteraction {
    final String modid = "Quark";
    public static boolean ENABLED = true;
    public static boolean MIDORI_BLOCKS_NEED_CHUNKS = true;

    @Override
    public boolean isActive() {
        return ENABLED && Loader.isModLoaded(modid);
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
    }

    @Override
    public List<IInteraction> getDependencies() {
        return null;
    }

    @Override
    public List<IInteraction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        if(!isActive())
            return;

        ItemStack midori = InteractionHelper.findBlock(modid,"midori_block",4,0);

        if(MIDORI_BLOCKS_NEED_CHUNKS) {
            BetterWithAddons.removeCraftingRecipe(midori);
            GameRegistry.addShapedRecipe(midori, "mm", "mm", 'm', ModItems.material.getMaterial("midori_popped"));
        }
    }

    @Override
    public void postInit() {
    }
}
