package betterwithaddons.item.rbdtools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IConvenientTool {
    boolean canShear(ItemStack stack, IBlockState state);

    boolean canCollect(ItemStack stack, IBlockState state);

    boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);

    boolean canHarvestEfficiently(ItemStack stack, IBlockState state);

    float getEfficiency(ItemStack stack, IBlockState state);
}

