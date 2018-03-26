package betterwithaddons.crafting.recipes;

import betterwithaddons.block.ModBlocks;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.registry.HopperFilters;
import betterwithmods.common.registry.HopperInteractions.HopperRecipe;
import betterwithmods.util.InvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HopperCratingRecipe extends HopperRecipe {
    public HopperCratingRecipe(ItemStack input, ItemStack output) {
        super(-1, input, output);
    }

    public boolean isCrate(IBlockState state)
    {
        return state.getBlock() == ModBlocks.box;
    }

    @Override
    public boolean canCraft(World world, BlockPos pos) {
        IBlockState crateState = world.getBlockState(pos.down());
        TileEntityFilteredHopper tile = (TileEntityFilteredHopper)world.getTileEntity(pos);
        SimpleStackHandler inventory = tile.inventory;
        if(!isCrate(crateState)) {
            return false;
        }

        return true;
    }

    @Override
    public void craft(EntityItem inputStack, World world, BlockPos pos) {
        TileEntityFilteredHopper tile = (TileEntityFilteredHopper)world.getTileEntity(pos);
        SimpleStackHandler inventory = tile.inventory;
        if(!InvUtils.consumeItemsInInventory(inventory,input,7,false)) {
            return;
        }

        InvUtils.ejectStackWithOffset(world, pos.down(1), this.output.copy());

        this.onCraft(world, pos, inputStack);
    }

    @Override
    public void onCraft(World world, BlockPos pos, EntityItem item) {
        world.setBlockToAir(pos.down());

        super.onCraft(world, pos, item);
    }
}
