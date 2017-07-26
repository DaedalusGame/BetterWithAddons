package betterwithaddons.item.rbdtools;

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
import net.minecraftforge.oredict.OreDictionary;

import java.util.Random;

public class ItemMasonPick extends ItemPickaxeConvenient {
    public ItemMasonPick(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {
        return isMasonry(stack, state);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return isItemMasonry(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {
        return isMasonry(stack, state) ? 1.5f : 0.25f;
    }

    public static boolean isItemMasonry(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isMasonry(stack, state);
        }
        return false;
    }

    public static boolean isMasonry(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.ROCK && (!state.isFullCube() || !isOre(tool, state));
    }

    public static boolean isOre(ItemStack tool, IBlockState state) {
        Block block = state.getBlock();
        ItemStack oreItem = new ItemStack(block.getItemDropped(state, new Random(), 0), 1, block.damageDropped(state));
        return isItemOre(oreItem);
    }

    public static boolean isItemOre(ItemStack ore) {
        if (ore.isEmpty()) return false;
        if (ore.getItem() instanceof ItemBlock)
            for (int oreid : OreDictionary.getOreIDs(ore))
                if (OreDictionary.getOreName(oreid).startsWith("ore"))
                    return true;
        return false;
    }
}
