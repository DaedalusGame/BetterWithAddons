package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Iterator;
import java.util.List;

public class InteractionQuark extends Interaction {
    final String modid = "quark";
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
        ConditionModule.MODULES.put("MidoriNeedsChunks", () -> MIDORI_BLOCKS_NEED_CHUNKS);
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) event.getRegistry();
        Block midoriBlock = Block.REGISTRY.getObject(new ResourceLocation(modid,"midori_block"));

        if(MIDORI_BLOCKS_NEED_CHUNKS)
            removeRecipeByOutput(reg, new ItemStack(midoriBlock,4),modid);
    }
}
