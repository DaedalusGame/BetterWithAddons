package betterwithaddons.item.rbdtools;

import betterwithaddons.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpade extends ItemSpadeConvenient {
    public ItemSpade(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {
        return isDirt(stack, state);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return isItemDirt(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
        return isDirt(stack, state);
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {
        return isDirt(stack, state) ? 1.5f : 1.0f;
    }

    public static boolean isDirt(ItemStack tool, IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.GROUND || material == Material.SAND || material == Material.GRASS || material == Material.CLAY;
    }

    public static boolean isItemDirt(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isDirt(stack, state);
        }
        return false;
    }
}
