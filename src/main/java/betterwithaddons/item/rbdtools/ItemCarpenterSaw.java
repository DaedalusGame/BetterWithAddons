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

public class ItemCarpenterSaw extends ItemAxeConvenient {
    public ItemCarpenterSaw(ToolMaterial material, float damage, float speed) {
        super(material, damage, speed);
    }

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {
        return isCarpentry(stack, state);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return isItemCarpentry(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {
        return isCarpentry(stack, state) ? 1.5f : 0.25f;
    }

    public static boolean isItemCarpentry(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isCarpentry(stack, state);
        }
        return false;
    }

    public static boolean isCarpentry(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.WOOD && !ItemKukri.isTree(tool, state);
    }

}
