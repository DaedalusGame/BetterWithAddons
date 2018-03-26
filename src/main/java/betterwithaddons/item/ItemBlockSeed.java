package betterwithaddons.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemBlockSeed extends ItemBlockMeta implements IPlantable {
    EnumPlantType type = EnumPlantType.Crop;

    public ItemBlockSeed(Block block) {
        super(block);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return type;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return block.getDefaultState();
    }
}
