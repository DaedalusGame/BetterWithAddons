package betterwithaddons.interaction.minetweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.item.WeightedItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ZenClass("mods.betterwithaddons.IRecipeFunction")
@ZenRegister
public interface IModRecipeFunction {
    Random random = new Random();

    @ZenMethod
    static IModRecipeFunction pickOne(WeightedItemStack[] stacks) {
        return (inputs, info) -> {
            List<IItemStack> outputs = new ArrayList<>();
            float total = 0;
            for (WeightedItemStack stack : stacks) {
                total += stack.getChance();
            }
            float selection = random.nextFloat() * total;
            for (WeightedItemStack stack : stacks) {
                selection -= stack.getChance();
                if(selection <= 0)
                    outputs.add(stack.getStack());
            }
            return outputs.toArray(new IItemStack[outputs.size()]);
        };
    }

    @ZenMethod
    static IModRecipeFunction randomDrops(WeightedItemStack[] stacks) {
        return (inputs, info) -> {
            List<IItemStack> outputs = new ArrayList<>();
            for (WeightedItemStack stack : stacks) {
                if(random.nextFloat() < stack.getChance())
                    outputs.add(stack.getStack());
            }
            return outputs.toArray(new IItemStack[outputs.size()]);
        };
    }

    IItemStack[] process(Map<String, IItemStack> inputs, IMachineInfo info);
}
