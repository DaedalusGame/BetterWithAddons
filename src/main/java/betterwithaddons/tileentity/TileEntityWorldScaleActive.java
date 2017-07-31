package betterwithaddons.tileentity;

import betterwithaddons.block.BlockWorldScale;
import betterwithaddons.handler.WorldScaleData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;

public class TileEntityWorldScaleActive extends TileEntityBase implements ITickable
{
    ArrayList<ChunkPos> claimedChunks;
    BlockPos oldPos;
    int updateTick = 0;
    final int UPDATE_TICKRATE = 50;
    final int MAX_RANGE = 5;

    public TileEntityWorldScaleActive() {
        super();
        claimedChunks = new ArrayList<ChunkPos>();
        setOldPos();
    }

    @Override
    public void update()
    {
        //if (!this.worldObj.isRemote)
        //{
            BlockPos pos = this.getPos();

            if(!oldPos.equals(pos))
            {
                unclaimAllChunks();
                claimAllChunks();
                setOldPos();
            }

            if(updateTick++ > UPDATE_TICKRATE) {
                ChunkPos chunkpos = new ChunkPos(pos);
                claimChunk(chunkpos);

                for (int z = -MAX_RANGE; z <= MAX_RANGE; z++)
                for (int x = -MAX_RANGE; x <= MAX_RANGE; x++) {
                    if(z == x && x == 0)
                        continue;
                    BlockPos newpos = pos.add(x,0,z);
                    if(!world.isBlockLoaded(newpos))
                        continue;
                    IBlockState blockstate = world.getBlockState(newpos);
                    ChunkPos newchunk = new ChunkPos(chunkpos.x+x,chunkpos.z+z);
                    Block block = blockstate.getBlock();
                    if(block instanceof BlockWorldScale)
                    {
                        boolean claimed = claimChunk(newchunk);
                        if(!world.isRemote)
                            ((BlockWorldScale)block).setCracked(world,newpos,blockstate,!claimed);
                    }
                    else
                    {
                        unclaimChunk(newchunk);
                    }
                }
                updateTick = 0;
            }
        //}
    }

    public void setOldPos()
    {
        BlockPos pos = this.getPos();
        oldPos = pos;
    }

    public boolean claimChunk(ChunkPos chunkpos)
    {
        if(claimedChunks.contains(chunkpos))
            return true;

        WorldScaleData scaledata = WorldScaleData.getInstance(world);
        if(!scaledata.isClaimed(chunkpos))
        {
            claimedChunks.add(chunkpos);
            scaledata.claimChunk(chunkpos,this.getPos());
            return true;
        }
        return false;
    }

    public void unclaimChunk(ChunkPos chunkpos)
    {
        WorldScaleData scaledata = WorldScaleData.getInstance(world);
        if(claimedChunks.contains(chunkpos))
        {
            claimedChunks.remove(chunkpos);
            scaledata.unclaimChunk(chunkpos);
        }
    }

    public void claimAllChunks()
    {
        BlockPos pos = this.getPos();
        for (ChunkPos chunk: claimedChunks) {
            WorldScaleData.getInstance(world).claimChunk(chunk,pos);
        }
    }

    public void unclaimAllChunks()
    {
        for (ChunkPos chunk: claimedChunks) {
            WorldScaleData.getInstance(world).unclaimChunk(chunk);
        }
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound)
    {
        NBTTagList shardlist = new NBTTagList();

        for(ChunkPos chunkpos : claimedChunks) {
            NBTTagCompound shardcompound = new NBTTagCompound();
            shardcompound.setInteger("chunkX",chunkpos.x);
            shardcompound.setInteger("chunkZ",chunkpos.z);
            shardlist.appendTag(shardcompound);
        }

        compound.setTag("claimedChunks",shardlist);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound)
    {
        NBTTagList shardlist = compound.getTagList("claimedChunks",10);
        claimedChunks.clear();

        for (int i = 0; i < shardlist.tagCount(); i++) {
            NBTTagCompound shardcompound = shardlist.getCompoundTagAt(i);
            ChunkPos chunkpos = new ChunkPos(shardcompound.getInteger("chunkX"),shardcompound.getInteger("chunkZ"));
            claimedChunks.add(chunkpos);
        }
    }
}
