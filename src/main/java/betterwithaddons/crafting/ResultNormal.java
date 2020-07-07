package betterwithaddons.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Collectors;

public class ResultNormal implements ICraftingResult {
    List<ItemStack> items;
    List<FluidStack> fluids;

    public ResultNormal(List<ItemStack> items, List<FluidStack> fluids) {
        this.items = items;
        this.fluids = fluids;
    }

    public void addRemains(List<ItemStack> inputs) {
        for (ItemStack input : inputs) {
            items.add(input.getItem().getContainerItem(input));
        }
    }

    @Override
    public void addTooltip(ItemStack stack, List<String> tooltip) {

    }

    @Override
    public void addTooltip(FluidStack fluid, List<String> tooltip) {

    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public List<FluidStack> getFluids() {
        return fluids;
    }

    @Override
    public ICraftingResult copy() {
        return new ResultNormal(items.stream().map(ItemStack::copy).collect(Collectors.toList()), fluids.stream().map(FluidStack::copy).collect(Collectors.toList()));
    }
}
