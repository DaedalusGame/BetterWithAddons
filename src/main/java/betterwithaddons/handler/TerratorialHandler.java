package betterwithaddons.handler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class TerratorialHandler {
    @SubscribeEvent
    public void spawnLiving(LivingSpawnEvent.CheckSpawn spawnEvent)
    {
        World world = spawnEvent.getWorld();
        EntityLivingBase entity = spawnEvent.getEntityLiving();
        Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(spawnEvent.getX(),spawnEvent.getY(),spawnEvent.getZ()));

        if(!chunk.isLoaded())
            return;

        if(entity instanceof EntityZombie || entity instanceof EntitySkeleton || entity instanceof EntityCreeper)
            spawnEvent.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void chunkLoaded(ChunkDataEvent.Load event)
    {
        Chunk chunk = event.getChunk();
        NBTTagCompound compound = event.getData();

        if(!compound.hasKey("terratorial_tag"))
        {
            Random rand = chunk.getRandomWithSeed(chunk.getWorld().getSeed());
            if(rand.nextInt(100) < 4)
            {
                
            }

            chunk.markDirty();
        }
    }

    @SubscribeEvent
    public void chunkSaved(ChunkDataEvent.Save event)
    {
        Chunk chunk = event.getChunk();
        NBTTagCompound compound = event.getData();

        compound.setBoolean("terratorial_tag", true);
    }
}
