package betterwithaddons.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class PatientiaHandler {
    private static HashSet<Block> CUSTOM_BLOCKS = new HashSet<>();

    public static void addCustomBlock(Block block)
    {
        CUSTOM_BLOCKS.add(block);
    }

    public static boolean shouldRegister()
    {
        return CUSTOM_BLOCKS.size() > 0;
    }

    protected int updateLCG = (new Random()).nextInt();

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent tickEvent)
    {
        World world = tickEvent.world;
        if(!world.isRemote && world instanceof WorldServer) {
            customRandomTick((WorldServer)world);
        }
    }

    private void customRandomTick(WorldServer world) {
        if (world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return;
        }

        int i = world.getGameRules().getInt("randomTickSpeed");

        if (i > 0) {
            Iterator<Chunk> iterator = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator());

            while (iterator.hasNext()) {
                Chunk chunk = iterator.next();
                int j = chunk.x * 16;
                int k = chunk.z * 16;

                for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
                    if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE) {
                        for (int i1 = 0; i1 < i; ++i1) {
                            this.updateLCG = this.updateLCG * 3 + 1013904223;
                            int j1 = this.updateLCG >> 2;
                            int k1 = j1 & 15;
                            int l1 = j1 >> 8 & 15;
                            int i2 = j1 >> 16 & 15;
                            IBlockState iblockstate = extendedblockstorage.get(k1, i2, l1);
                            Block block = iblockstate.getBlock();

                            if (getTickRandomly(block)) {
                                MinecraftForge.EVENT_BUS.post(new RandomBlockTickEvent(world, new BlockPos(k1 + j, i2 + extendedblockstorage.getYLocation(), l1 + k), iblockstate, world.rand));
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean getTickRandomly(Block block) {
        return CUSTOM_BLOCKS.contains(block);
    }
}
