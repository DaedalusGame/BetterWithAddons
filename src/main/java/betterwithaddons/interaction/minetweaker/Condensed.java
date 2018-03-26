package betterwithaddons.interaction.minetweaker;

import betterwithaddons.item.ItemMaterial;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashSet;

@ZenRegister
@ZenClass(Condensed.clazz)
public class Condensed {
    public static final String clazz = "mods.betterwithaddons.Condensed";
    static HashSet<ItemMaterial> replacedContainers = new HashSet<>();

    @ZenMethod
    public static void setContainer(@NotNull IItemStack condensed, @NotNull IItemStack input) {
        ItemStack item = CraftTweakerMC.getItemStack(condensed);
        ItemStack container = CraftTweakerMC.getItemStack(input);
        if(item.getItem() instanceof ItemMaterial)
            CraftTweaker.LATE_ACTIONS.add(new SetContainer((ItemMaterial) item.getItem(),container));
    }

    public static class SetContainer implements IAction
    {
        ItemMaterial condensed;
        ItemStack container;
        ItemStack prevContainer;

        public SetContainer(ItemMaterial condensed, ItemStack container) {
            this.condensed = condensed;
            this.container = container;
            this.prevContainer = condensed.getContainer();
        }

        @Override
        public String describe() {
            return String.format("Replacing container of %s with %s", condensed, container);
        }

        @Override
        public void apply() {
            if(!replacedContainers.contains(condensed)) {
                condensed.setContainer(container);
                replacedContainers.add(condensed);
            }
        }
    }
}
