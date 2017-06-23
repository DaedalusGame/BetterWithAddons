package betterwithaddons.tileentity;

import betterwithaddons.entity.EntityArtifactFrame;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.InventoryUtil;
import betterwithaddons.util.ItemUtil;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.ArrayList;
import java.util.List;

public class TileEntityLegendarium extends TileEntityBase {
    public final float MIN_DAMAGE = 0.1f;
    public final int DAMAGE_PAD = 24;
    public final int POSTER_RANGE = 16;

    private long lastClick = 0;
    private long lastTurnIn = 0;
    QueueItemStackHandler queue;

    public TileEntityLegendarium()
    {
        super();
        queue = new QueueItemStackHandler(this);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setTag("Queue", queue.serializeNBT());
        compound.setLong("LastTurnIn",lastTurnIn);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        queue.deserializeNBT(compound.getCompoundTag("Queue"));
        lastTurnIn = compound.getLong("LastTurnIn");
    }

    public ItemStack insertItem(ItemStack stack)
    {
        if(stack.getItem() == ModItems.artifactFrame)
        {
            if(cleanItemFrames() == 0)
                populateItemFrames();
            return stack;
        }

        String analysis = analyzeItem(stack);

        if(analysis != null)
        {
            //if(analysis.equals("not_broken"))
            //    stack.setItemDamage(stack.getMaxDamage()-1);
            return stack;
        }

        ItemStack retain = queue.insertItem(0, ModItems.brokenArtifact.makeFrom(stack),false);
        if(retain.isEmpty())
        {
            populateItemFrames();
        }
        return retain;
    }

    public String analyzeItem(ItemStack stack)
    {
        if(stack.isEmpty()) return "not_item";
        if(!ItemUtil.isTool(stack.getItem())) return "not_tool";
        if(!stack.hasDisplayName()) return "not_artifact";
        if(!stack.isItemEnchanted()) return "not_artifact";
        int actualDamage = (stack.getMaxDamage() - stack.getItemDamage());
        float maxDamage = stack.getMaxDamage() * MIN_DAMAGE + DAMAGE_PAD;
        if(actualDamage > maxDamage) return "not_broken";
        return null;
    }

    public ItemStack retrieveItem()
    {
        long time = world.getTotalWorldTime();
        if(time - lastClick > 3)
        {
            lastClick = time;
            ItemStack retrieved = queue.extractItem(0,1,false);
            populateItemFrames();
            return retrieved;
        }

        return ItemStack.EMPTY;
    }

    private void populateItemFrames()
    {
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-POSTER_RANGE,-POSTER_RANGE,-POSTER_RANGE),pos.add(POSTER_RANGE,POSTER_RANGE,POSTER_RANGE));
        List<EntityArtifactFrame> frames = world.getEntitiesWithinAABB(EntityArtifactFrame.class,posterArea);
        cleanItemFrames();

        int e = 0;
        for (int i = 0; i < queue.getSlots(); i++) {
            ItemStack artifact = queue.getStackInSlot(i);
            while(e < frames.size())
            {
                EntityArtifactFrame frame = frames.get(e);
                e++;
                if(!isFrameClean(frame)) continue;
                frame.setDisplayedItem(artifact);
                frame.setCustomNameTag(artifact.getDisplayName());
                frame.setEntityInvulnerable(true);
                break;
            }
        }
    }

    private boolean isFrameClean(EntityArtifactFrame frame)
    {
        return frame.getDisplayedItem().isEmpty();
    }

    private boolean isFrameArtifact(EntityArtifactFrame frame)
    {
        ItemStack displayStack = frame.getDisplayedItem();

        return !displayStack.isEmpty() && displayStack.getItem() == ModItems.brokenArtifact;
    }

    private int cleanItemFrames()
    {
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-POSTER_RANGE,-POSTER_RANGE,-POSTER_RANGE),pos.add(POSTER_RANGE,POSTER_RANGE,POSTER_RANGE));
        List<EntityArtifactFrame> frames = world.getEntitiesWithinAABB(EntityArtifactFrame.class,posterArea);

        int ret = 0;

        for (EntityArtifactFrame frame : frames) {
            if(!isFrameArtifact(frame)) continue;
            frame.setEntityInvulnerable(false);
            frame.setDisplayedItem(ItemStack.EMPTY);
            frame.setCustomNameTag("");
            ret++;
        }

        return ret;
    }

    public static class LegendariumData extends WorldSavedData
    {
        public static final String ID = "LegendariumInfo";
        private World worldObj;

        NBTTagCompound data = new NBTTagCompound();

        public LegendariumData() {
            super(ID);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            data = compound;
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            for (String key : data.getKeySet()) {
                compound.setInteger(key, data.getInteger(key));
            }
            return compound;
        }

        private static String getName(World world) {
            final IChunkGenerator chunkGenerator = world.provider.createChunkGenerator();
            return chunkGenerator.getClass().getName();
        }

        public static LegendariumData getInstance(World world)
        {
            if (world != null)
            {
                WorldSavedData handler = world.getPerWorldStorage().getOrLoadData(LegendariumData.class, ID);
                if (handler == null)
                {
                    handler = new LegendariumData();
                    world.getPerWorldStorage().setData(ID, handler);
                }
                ((LegendariumData)handler).worldObj = world;

                return (LegendariumData) handler;
            }
            return null;
        }
    }
}
