package betterwithaddons.crafting.recipes;

import betterwithaddons.block.ModBlocks;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.registry.HopperFilter;
import betterwithmods.common.registry.HopperInteractions.HopperRecipe;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class HopperCratingRecipe extends HopperRecipe {

    public HopperCratingRecipe(Ingredient input, ItemStack output) {
        super(HopperFilter.NONE.getName(), StackIngredient.fromIngredient(8, input), output);
    }

    @Override
    public List<ItemStack> getInputContainer() {
        return Lists.newArrayList(new ItemStack(ModBlocks.BOX));
    }

    public boolean isCrate(IBlockState state) {
        return state.getBlock() == ModBlocks.BOX;
    }

    @Override
    public boolean canCraft(World world, BlockPos pos) {
        IBlockState crateState = world.getBlockState(pos.down());
        if (!isCrate(crateState)) {
            return false;
        }
        return super.canCraft(world, pos);
    }

    @Override
    public void onCraft(World world, BlockPos pos, EntityItem item, TileEntityFilteredHopper tile) {
        world.setBlockToAir(pos.down());
        super.onCraft(world, pos, item, tile);
    }

}
