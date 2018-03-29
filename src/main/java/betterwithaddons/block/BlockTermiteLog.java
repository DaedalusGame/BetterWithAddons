package betterwithaddons.block;

import betterwithmods.common.items.ItemMaterial;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BlockTermiteLog extends BlockModLog {
    public BlockTermiteLog() {
        super(ModWoods.TERMITE);
        this.setUnlocalizedName("log.oak");
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return Lists.newArrayList(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
    }
}
