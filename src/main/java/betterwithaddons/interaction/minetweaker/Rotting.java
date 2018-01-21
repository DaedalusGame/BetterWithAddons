package betterwithaddons.interaction.minetweaker;

import betterwithaddons.handler.RotHandler;
import betterwithaddons.handler.RotHandler.RotInfo;
import betterwithaddons.interaction.InteractionCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseUndoable;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(Rotting.clazz)
public class Rotting {
    public static final String clazz = "mods.betterwithaddons.Rotting";

    @ZenMethod
    public static void add(@NotNull IItemStack input, IItemStack output, long time, String baseName) {
        InteractionCraftTweaker.LATE_ADDITIONS.add(new Add(InputHelper.toStack(input),InputHelper.toStack(output),time,baseName));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        InteractionCraftTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(input)));
    }

    public static class Add extends BaseUndoable
    {
        ItemStack stack;
        RotInfo rotInfo;

        public Add(ItemStack input, ItemStack output, long time, String baseName) {
            super("Rotting");
            stack = input;
            rotInfo = new RotInfo(input,time,baseName,output);
        }

        @Override
        public void apply() {
            RotHandler.addRottingItem(stack.getItem(),rotInfo);
        }

        @Override
        protected String getRecipeInfo() {
            return stack.toString();
        }
    }

    public static class Remove extends BaseUndoable
    {
        ItemStack stack;

        protected Remove(ItemStack stack) {
            super("Rotting");
            this.stack = stack;
        }

        @Override
        public void apply() {
            RotHandler.removeRottingItem(stack);
        }

        @Override
        protected String getRecipeInfo() {
            return stack.toString();
        }
    }
}
