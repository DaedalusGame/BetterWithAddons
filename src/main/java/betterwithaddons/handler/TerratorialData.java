package betterwithaddons.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TerratorialData extends WorldSavedData
{
    public static final String ID = "TerratorialData";

    private HashMap<Integer,MobTerritory> territories;
    private HashMap<ChunkPos,MobTerritory> territoryChunks;
    private int currentMaxID = 0;

    public TerratorialData(String name)
    {
        super(name);
        territories = new HashMap<Integer,MobTerritory>();
        territoryChunks = new HashMap<ChunkPos, MobTerritory>();
    }

    public TerratorialData()
    {
        this(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList territorylist = nbt.getTagList("territoryChunks",10);
        NBTTagList chunks = nbt.getTagList("territoryChunks",10);

        for (int i = 0; i < territorylist.tagCount(); i++) {
            NBTTagCompound territorycompound = territorylist.getCompoundTagAt(i);
            int territoryid = territorycompound.getInteger("id");
            String typeid = territorycompound.getString("type");
            ItemStack bannerstack = ItemStack.loadItemStackFromNBT(territorycompound.getCompoundTag("banner"));

            if(MobTerritoryType.typeList.containsKey(typeid)) {
                MobTerritory territory = new MobTerritory(territoryid,MobTerritoryType.typeList.get(typeid));
                territory.setBanner(bannerstack);
                territories.put(territoryid, territory);
            }

            currentMaxID = Math.max(currentMaxID,territoryid);
        }

        currentMaxID++;

        for (int i = 0; i < chunks.tagCount(); i++) {
            NBTTagCompound chunkcompound = chunks.getCompoundTagAt(i);
            ChunkPos chunkpos = new ChunkPos(chunkcompound.getInteger("chunkX"),chunkcompound.getInteger("chunkZ"));
            int territoryid = chunkcompound.getInteger("id");
            if(territories.containsKey(territoryid))
                territoryChunks.put(chunkpos,territories.get(territoryid));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList territorylist = new NBTTagList();
        NBTTagList chunklist = new NBTTagList();
        ArrayList<MobTerritory> hasNoTerritory = new ArrayList<MobTerritory>(territories.values());

        for(Map.Entry<ChunkPos, MobTerritory> entry : territoryChunks.entrySet()) {
            NBTTagCompound chunkcompound = new NBTTagCompound();
            ChunkPos chunkpos = entry.getKey();
            MobTerritory territory = entry.getValue();
            chunkcompound.setInteger("chunkX",chunkpos.chunkXPos);
            chunkcompound.setInteger("chunkZ",chunkpos.chunkZPos);
            chunkcompound.setInteger("territoryid",territory.getId());
            territorylist.appendTag(chunkcompound);
            if(hasNoTerritory.contains(territory))
                hasNoTerritory.remove(territory);
        }

        for(Map.Entry<Integer, MobTerritory> entry : territories.entrySet()) {

            MobTerritory territory = entry.getValue();
            if(hasNoTerritory.contains(territory)) continue;
            NBTTagCompound territorycompound = new NBTTagCompound();
            territorycompound.setInteger("id",territory.getId());
            territorycompound.setString("type",territory.getTerritoryType().getIdentifier());
            territorycompound.setTag("banner",territory.getBanner().serializeNBT());
            territorylist.appendTag(territorycompound);
        }

        nbt.setTag("territoryChunks",chunklist);
        nbt.setTag("territories",territorylist);

        return nbt;
    }

    public void addTerritory(MobTerritory territory)
    {
        territories.put(territory.getId(),territory);
        this.markDirty();
    }

    public void removeTerritory(MobTerritory territory)
    {
        for (Iterator<Map.Entry<ChunkPos, MobTerritory>> it = territoryChunks.entrySet().iterator(); it.hasNext();)
        {
            if(it.next().getValue() == territory)
            {
                it.remove();
            }
        }
        this.markDirty();
    }

    public void claimChunk(MobTerritory territory, ChunkPos chunkpos)
    {
        if(territories.containsValue(territory) && !territoryChunks.containsKey(chunkpos))
        {
            territoryChunks.put(chunkpos,territory);
            this.markDirty();
        }
    }

    public void unclaimChunk(ChunkPos chunkpos)
    {
        if(territoryChunks.containsKey(chunkpos))
        {
            territoryChunks.remove(chunkpos);
            this.markDirty();
        }
    }

    public static TerratorialData getInstance(World world)
    {
        if (world != null)
        {
            WorldSavedData handler = world.getPerWorldStorage().getOrLoadData(TerratorialData.class, ID);
            if (handler == null)
            {
                handler = new TerratorialData();
                world.getPerWorldStorage().setData(ID, handler);
            }

            return (TerratorialData) handler;
        }
        return null;
    }
}