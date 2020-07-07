package betterwithaddons.crafting;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ResultBlock implements ICraftingResult {
    ItemStack stack;
    IBlockState state;

    public ResultBlock(IBlockState state, ItemStack stack) {
        this.state = state;
        this.stack = stack;
    }

    @Override
    public List<ItemStack> getJEIItems() {
        return Lists.newArrayList(stack);
    }

    @Override
    public void addTooltip(ItemStack stack, List<String> tooltip) {

    }

    @Override
    public void addTooltip(FluidStack fluid, List<String> tooltip) {

    }

    @Override
    public List<ItemStack> getItems() {
        return Lists.newArrayList();
    }

    @Override
    public List<FluidStack> getFluids() {
        return Lists.newArrayList();
    }

    @Override
    public void apply(World world, BlockPos pos) {
        world.setBlockState(pos, state);
    }

    @Override
    public ICraftingResult copy() {
        return this;
    }
}
