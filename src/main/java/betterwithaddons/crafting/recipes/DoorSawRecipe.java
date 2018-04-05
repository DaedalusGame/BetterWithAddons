package betterwithaddons.crafting.recipes;

import betterwithaddons.util.MappedBlockIngredient;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DoorSawRecipe extends SawRecipe {
    BlockDoor door;

    public DoorSawRecipe(BlockDoor door, List<ItemStack> outputs, ItemStack doorStack) {
        super(new MappedBlockIngredient((world, pos, state) -> state.getBlock() == door, doorStack), outputs);
        this.door = door;
    }

    @Override
    public boolean consumeIngredients(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        BlockPos lower = pos.down();
        BlockPos upper = pos.up();
        boolean success;

        if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER && world.getBlockState(lower).getBlock() == door) {
            success = world.setBlockToAir(lower);
            world.setBlockToAir(pos);
        }
        else if (state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER && world.getBlockState(upper).getBlock() == door) {
            success = world.setBlockToAir(pos);
            world.setBlockToAir(upper);
        }
        else
            success = false;

        return success;
    }
}
