package betterwithaddons.crafting;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface ICraftingResult {
    default List<ItemStack> getJEIItems() {
        return getItems();
    }

    default List<FluidStack> getJEIFluids() {
        return getFluids();
    }

    void addTooltip(ItemStack stack, List<String> tooltip);

    void addTooltip(FluidStack fluid, List<String> tooltip);

    List<ItemStack> getItems();

    List<FluidStack> getFluids();

    default void apply(World world, BlockPos pos) {
        //NOOP
    }

    ICraftingResult copy();

    default void spawnItems(World world, Vec3d pos) {
        for (ItemStack stack : getItems()) {
            EntityItem entity = new EntityItem(world, pos.x, pos.y, pos.z);
            world.spawnEntity(entity);
        }
    }
}
