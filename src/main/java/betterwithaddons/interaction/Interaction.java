package betterwithaddons.interaction;

import betterwithaddons.config.ModConfiguration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class Interaction {
    boolean isActive() {
        return false;
    }

    void setEnabled(boolean active) {}

    List<Interaction> getDependencies() {
        return null;
    }

    List<Interaction> getIncompatibilities() {
        return null;
    }

    void setupConfig() {}

    void preInitClient() {}

    void preInit() {}

    void preInitEnd() {}

    void init() {}

    void postInit() {}

    void loadComplete() {}

    void oreDictRegistration() {}

    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {}

    protected abstract String getName();

    protected final int loadPropInt(String propName, String desc, int default_) {
        return ModConfiguration.loadPropInt(propName, getName(), desc, default_);
    }

    protected final int loadPropInt(String propName, String desc, int default_, int min, int max) {
        return ModConfiguration.loadPropInt(propName, getName(), desc, default_, min, max);
    }

    protected final double loadPropDouble(String propName, String desc, double default_, double min, double max) {
        return ModConfiguration.loadPropDouble(propName, getName(), desc, default_, min, max);
    }

    protected final double loadPropDouble(String propName, String desc, double default_) {
        return ModConfiguration.loadPropDouble(propName, getName(), desc, default_);
    }

    protected final boolean loadPropBool(String propName, String desc, boolean default_) {
        return ModConfiguration.loadPropBool(propName, getName(), desc, default_);
    }

    protected final String[] loadPropStringList(String propName, String desc, String[] default_) {
        return ModConfiguration.loadPropStringList(propName, getName(), desc, default_);
    }

    protected final HashSet<String> loadPropStringSet(String propName, String desc, String[] default_) {
        return ModConfiguration.loadPropStringSet(propName, getName(), desc, default_);
    }

    protected final void doesNotNeedRestart(Runnable op)
    {
        ModConfiguration.doesNotNeedRestart(op);
    }

    protected void removeRecipeByOutput(ForgeRegistry<IRecipe> reg, ItemStack outputToRemove) {
        removeRecipeByOutput(reg,outputToRemove,null);
    }

    protected void removeRecipeByOutput(ForgeRegistry<IRecipe> reg, ItemStack outputToRemove, String modid) {
        for (Iterator<IRecipe> iter = reg.iterator(); iter.hasNext(); ) {
            IRecipe recipe = iter.next();
            if ((modid == null || modid.equals(recipe.getRegistryName().getResourceDomain())) && ItemStack.areItemStacksEqual(recipe.getRecipeOutput(),outputToRemove)) {
                reg.remove(reg.getKey(recipe));
                break;
            }
        }
    }
}
