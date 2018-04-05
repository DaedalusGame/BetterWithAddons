package betterwithaddons.crafting.recipes;

import betterwithaddons.block.ModBlocks;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.common.blocks.tile.SimpleStackHandler;
import betterwithmods.common.registry.HopperFilter;
import betterwithmods.common.registry.HopperInteractions.HopperRecipe;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class HopperCratingRecipe extends HopperRecipe {
    StackIngredient testIngredient;

    public HopperCratingRecipe(Ingredient input, ItemStack output) {
        super(HopperFilter.NONE.getName(), input, output);
        testIngredient = StackIngredient.fromIngredient(7,input);
    }

    @Override
    public List<ItemStack> getContainers() {
        return Lists.newArrayList(new ItemStack(ModBlocks.box));
    }

    @Override
    public List<ItemStack> getInputs() {
        return super.getInputs().stream().map(stack -> {
            ItemStack newStack = stack.copy();
            newStack.setCount(8);
            return newStack;
        }).collect(Collectors.toList());
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
        return InvUtils.consumeItemsInInventory(inventory, testIngredient, true, NonNullList.create());
    }

    @Override
    public void craft(EntityItem entity, World world, BlockPos pos) {
        TileEntityFilteredHopper tile = (TileEntityFilteredHopper)world.getTileEntity(pos);
        SimpleStackHandler inventory = tile.inventory;
        if(!InvUtils.consumeItemsInInventory(inventory, entity.getItem(), 7, false)) {
            return;
        }

        InvUtils.ejectStackWithOffset(world, pos.down(1), secondaryOutputs);

        this.onCraft(world, pos, entity);
    }

    @Override
    public void onCraft(World world, BlockPos pos, EntityItem item) {
        world.setBlockToAir(pos.down());

        super.onCraft(world, pos, item);
    }
}
