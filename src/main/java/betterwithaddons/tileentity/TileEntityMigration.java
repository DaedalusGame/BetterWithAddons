package betterwithaddons.tileentity;

import betterwithaddons.util.migration.Migration;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityMigration extends TileEntityBase {
    Migration migrationData;

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        if(migrationData != null)
            migrationData.writeToNBT(compound);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        migrationData = Migration.deserialize(compound);
    }
}
