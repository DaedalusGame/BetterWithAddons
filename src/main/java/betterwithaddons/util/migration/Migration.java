package betterwithaddons.util.migration;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public abstract class Migration {
    private static final HashMap<Block, IMigrationProvider> needMigration = new HashMap<>();
    private static final HashMap<String, IMigrationStorage> migrationTypes = new HashMap<>();

    private final String type;

    public Migration(String type)
    {
        this.type = type;
    }

    public static void addMigrationProvider(Block block, IMigrationProvider provider)
    {
        needMigration.put(block,provider);
    }

    public static Migration migrateBlock(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if(needMigration.containsKey(block))
            return needMigration.get(block).getMigrationData(world,pos);

        return null;
    }

    public static Migration deserialize(NBTTagCompound compound)
    {
        String type = compound.getString("type");

        if(needMigration.containsKey(type)) {
            Migration migration = migrationTypes.get(type).deserialize(type);
            migration.readFromNBT(compound);
            return migration;
        }

        return null;
    }

    public abstract void placeBlock(World world, BlockPos pos);

    public abstract void writeToNBT(NBTTagCompound compound);

    public abstract void readFromNBT(NBTTagCompound compound);
}
