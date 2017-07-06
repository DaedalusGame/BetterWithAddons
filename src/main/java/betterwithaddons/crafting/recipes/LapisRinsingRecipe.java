package betterwithaddons.crafting.recipes;

import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import betterwithmods.util.InvUtils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class LapisRinsingRecipe extends StokedCauldronRecipe {
    public LapisRinsingRecipe(ItemStack output, ItemStack secondaryOutput, Object... inputs) {
        super(output, secondaryOutput, inputs);
    }

    @Override
    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
        NonNullList<ItemStack> result = NonNullList.create();
        consumeInvIngredients(inv);
        result.add(getOutput());
        result.add(getSecondary());

        //rinse the mineral dye into clay for lapis
        ItemStack clay = new ItemStack(Items.CLAY_BALL);
        if(InvUtils.consumeItemsInInventory(inv,clay,clay.getCount(),false))
            result.add(new ItemStack(Items.DYE,1, EnumDyeColor.BLUE.getDyeDamage()));

        return result;
    }
}
