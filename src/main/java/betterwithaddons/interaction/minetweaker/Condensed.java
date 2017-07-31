package betterwithaddons.interaction.minetweaker;

import betterwithaddons.item.ItemMaterial;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashSet;

@ZenClass(Condensed.clazz)
public class Condensed {
    public static final String clazz = "mods.betterwithaddons.Condensed";
    static HashSet<ItemMaterial> replacedContainers = new HashSet<>();

    @ZenMethod
    public static void setContainer(@NotNull IItemStack condensed, @NotNull IItemStack input) {

        ItemStack item = InputHelper.toStack(condensed);
        ItemStack container = InputHelper.toStack(input);
        if(item.getItem() instanceof ItemMaterial)
            CraftTweakerAPI.apply(new SetContainer((ItemMaterial) item.getItem(),container));
    }

    public static class SetContainer extends BaseUndoable
    {
        ItemMaterial condensed;
        ItemStack container;
        ItemStack prevContainer;

        public SetContainer(ItemMaterial condensed, ItemStack container) {
            super("Condensed");
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
