package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class InteractionQuark extends Interaction {
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
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
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
