package betterwithaddons.interaction;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistry;

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

    void preInitClient() {}

    void preInit() {}

    void preInitEnd() {}

    void init() {}

    void postInit() {}

    void loadComplete() {}

    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {}

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
