package betterwithaddons.tileentity;

import betterwithaddons.util.TeaType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTea extends TileEntityBase {
    public static final String TAG_TYPE = "teatype";
    TeaType type = TeaType.WHITE;

    public TeaType getType() {
        return type;
    }

    public void setType(TeaType type) {
        this.type = type;
        markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setString(TAG_TYPE,type.getName());
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        type = TeaType.getType(compound.getString(TAG_TYPE));
    }
}
