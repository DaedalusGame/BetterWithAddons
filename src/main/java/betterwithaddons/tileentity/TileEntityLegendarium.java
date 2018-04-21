package betterwithaddons.tileentity;

import betterwithaddons.entity.EntityArtifactFrame;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.ItemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.storage.WorldSavedData;

import java.util.List;

public class TileEntityLegendarium extends TileEntityBase {


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

    public ItemStack insertItem(EntityPlayer player, ItemStack stack)
    {
        if(stack.getItem() == ModItems.ARTIFACT_FRAME)
        {
            if(cleanItemFrames() == 0) {
                populateItemFrames();
                player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.frames_populated"),true);
            }
            else
                player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.frames_cleared"),true);

            return stack;
        }

        String analysis = analyzeItem(stack);

        if(analysis != null)
        {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium."+analysis),true);

            return stack;
        }

        long timeUntilNextTurnIn = lastTurnIn + InteractionBWA.LEGENDARIUM_TURN_IN_DELAY - world.getTotalWorldTime();

        if(timeUntilNextTurnIn > 0)
        {
            long days = timeUntilNextTurnIn / 24000;
            long hours = (timeUntilNextTurnIn % 24000) / 1000;
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.not_now",days,hours),true);

            return stack;
        }

        ItemStack retain = queue.insertItem(0, ModItems.BROKEN_ARTIFACT.makeFrom(stack),false);
        if(retain.isEmpty())
        {
            lastTurnIn = world.getTotalWorldTime();
            populateItemFrames();
        }


        if(queue.getSlots() >= InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE)
        {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.ready"),true);
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
        double maxDamage = stack.getMaxDamage() * InteractionBWA.LEGENDARIUM_MIN_DAMAGE + InteractionBWA.LEGENDARIUM_DAMAGE_PAD;
        if(actualDamage > maxDamage) return "not_broken";
        if(stack.getRepairCost() <= 30) return "not_at_limit";
        return null;
    }

    public ItemStack retrieveItem(EntityPlayer player)
    {
        long time = world.getTotalWorldTime();

        if(queue.getSlots() < InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE)
        {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.not_enough_artifacts"),true);
            return ItemStack.EMPTY;
        }

        if(time - lastClick > 3 && queue.getSlots() >= InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE)
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
        int posterRange = InteractionBWA.LEGENDARIUM_POSTER_RANGE;
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-posterRange,-posterRange,-posterRange),pos.add(posterRange,posterRange,posterRange));
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

        return !displayStack.isEmpty() && displayStack.getItem() == ModItems.BROKEN_ARTIFACT;
    }

    private int cleanItemFrames()
    {
        int posterRange = InteractionBWA.LEGENDARIUM_POSTER_RANGE;
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-posterRange,-posterRange,-posterRange),pos.add(posterRange,posterRange,posterRange));
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
