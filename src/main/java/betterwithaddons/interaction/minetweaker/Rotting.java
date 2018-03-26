package betterwithaddons.interaction.minetweaker;

import betterwithaddons.handler.RotHandler;
import betterwithaddons.handler.RotHandler.RotInfo;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
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
        CraftTweaker.LATE_ACTIONS.add(new Add(CraftTweakerMC.getItemStack(input),CraftTweakerMC.getItemStack(output),time,baseName));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(input)));
    }

    public static class Add implements IAction
    {
        ItemStack stack;
        RotInfo rotInfo;

        public Add(ItemStack input, ItemStack output, long time, String baseName) {
            stack = input;
            rotInfo = new RotInfo(input,time,baseName,output);
        }

        @Override
        public void apply() {
            RotHandler.addRottingItem(stack.getItem(),rotInfo);
        }

        @Override
        public String describe() {
            return "Adding rotting item: "+stack.toString();
        }
    }

    public static class Remove implements IAction {
        ItemStack stack;

        protected Remove(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public void apply() {
            RotHandler.removeRottingItem(stack);
        }

        @Override
        public String describe() {
            return "Removing rotting item: "+stack.toString();
        }
    }
}
