package betterwithaddons.handler;

import betterwithaddons.block.BlockWorldScaleActive;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldScaleData extends WorldSavedData
{
    public static final String ID = "WorldScaleInfo";

    private HashMap<ChunkPos,BlockPos> WorldScales;
    private long lastCleanup = 0;
    private World worldObj;

    public WorldScaleData(String name)
    {
        super(name);
        WorldScales = new HashMap<ChunkPos, BlockPos>();
    }

    public WorldScaleData()
    {
        this(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList shardlist = nbt.getTagList("worldShards",10);

        for (int i = 0; i < shardlist.tagCount(); i++) {
            NBTTagCompound shardcompound = shardlist.getCompoundTagAt(i);
            ChunkPos chunkpos = new ChunkPos(shardcompound.getInteger("chunkX"),shardcompound.getInteger("chunkZ"));
            BlockPos shardpos = new BlockPos(shardcompound.getInteger("blockX"),shardcompound.getInteger("blockY"),shardcompound.getInteger("blockZ"));
            WorldScales.put(chunkpos,shardpos);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList shardlist = new NBTTagList();

        for(Map.Entry<ChunkPos, BlockPos> entry : WorldScales.entrySet()) {
            NBTTagCompound shardcompound = new NBTTagCompound();
            ChunkPos chunkpos = entry.getKey();
            BlockPos shardpos = entry.getValue();
            shardcompound.setInteger("chunkX",chunkpos.chunkXPos);
            shardcompound.setInteger("chunkZ",chunkpos.chunkZPos);
            shardcompound.setInteger("blockX",shardpos.getX());
            shardcompound.setInteger("blockY",shardpos.getY());
            shardcompound.setInteger("blockZ",shardpos.getZ());
            shardlist.appendTag(shardcompound);
        }

        nbt.setTag("worldShards",shardlist);

        return nbt;
    }

    public BlockPos getNearbyScale(BlockPos pos)
    {
        ChunkPos chunkpos = new ChunkPos(pos);
        if(WorldScales.containsKey(chunkpos))
            return WorldScales.get(chunkpos);
        return null;
    }

    public boolean isClaimed(ChunkPos chunkpos)
    {
        return WorldScales.containsKey(chunkpos);
    }

    public void claimChunk(ChunkPos chunkpos, BlockPos shardpos)
    {
        if(!isClaimed(chunkpos))
        {
            WorldScales.put(chunkpos,shardpos);
            this.markDirty();
        }
    }

    public void unclaimChunk(ChunkPos chunkpos)
    {
        if(isClaimed(chunkpos))
        {
            WorldScales.remove(chunkpos);
            this.markDirty();
        }
    }

    public void cleanup()
    {
        long currenttick = worldObj.getTotalWorldTime(); //let's hope this never goes back in time.
        if(currenttick <= lastCleanup + 100)
            return;

        ArrayList<ChunkPos> releasedChunks = new ArrayList<ChunkPos>();

        for(Map.Entry<ChunkPos, BlockPos> entry : WorldScales.entrySet()) {
            BlockPos pos = entry.getValue();
            IBlockState blockstate = worldObj.getBlockState(pos);
            if(worldObj.isBlockLoaded(pos) && !(blockstate.getBlock() instanceof BlockWorldScaleActive))
                releasedChunks.add(entry.getKey());
        }

        for(ChunkPos chunk : releasedChunks)
            WorldScales.remove(chunk);

        if(releasedChunks.size() > 0)
            this.markDirty();

        lastCleanup = currenttick;
    }

    public static WorldScaleData getInstance(World world)
    {
        if (world != null)
        {
            WorldSavedData handler = world.getPerWorldStorage().getOrLoadData(WorldScaleData.class, ID);
            if (handler == null)
            {
                handler = new WorldScaleData();
                world.getPerWorldStorage().setData(ID, handler);
            }
            ((WorldScaleData)handler).worldObj = world;

            return (WorldScaleData) handler;
        }
        return null;
    }
}
