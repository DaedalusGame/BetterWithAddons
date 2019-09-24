package betterwithaddons.tileentity;

import betterwithaddons.entity.EntityArtifactFrame;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IDirtyHandler;
import betterwithaddons.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

public class TileEntityLegendarium extends TileEntityBase implements ITickable {
    public enum CanvasType {
        Normal,
        Flat,
        Invisible,
    }

    private long lastClick = 0;
    private long lastTurnIn = 0;

    private CanvasType canvasType = CanvasType.Normal;

    QueueItemStackHandler migrationQueue;
    IItemHandler readonlyHandler = new IItemHandler() {
        @Override
        public int getSlots() {
            IItemHandler queue = getInventory();
            return queue.getSlots();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            IItemHandler queue = getInventory();
            return queue.getStackInSlot(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            IItemHandler queue = getInventory();
            return queue.getSlotLimit(slot);
        }
    };

    public TileEntityLegendarium() {
        super();
    }

    public CanvasType getCanvasType() {
        return canvasType;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        if(migrationQueue != null)
            compound.setTag("Queue", migrationQueue.serializeNBT());
        compound.setLong("LastTurnIn",lastTurnIn);
        compound.setInteger("CanvasType",canvasType.ordinal());
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("Queue")) {
            migrationQueue = new QueueItemStackHandler(null);
            migrationQueue.deserializeNBT(compound.getCompoundTag("Queue"));
        }
        lastTurnIn = compound.getLong("LastTurnIn");
        canvasType = CanvasType.values()[compound.getInteger("CanvasType")];
    }

    private void migrate() {
        LegendariumData data = LegendariumData.getInstance(world);
        while(migrationQueue.getSlots() > 0) {
            ItemStack stack = migrationQueue.extractItem(0,1,false);
            data.queue.insertItem(0,stack,false);
        }
        migrationQueue = null;
    }

    public ItemStack insertItem(EntityPlayer player, ItemStack stack)
    {
        if(stack.getItem() instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) stack.getItem();
            Block block = itemBlock.getBlock();
            IBlockState state = block.getDefaultState();
            boolean changed = false;
            Material material = state.getMaterial();
            if(block instanceof BlockPane && material == Material.GLASS) {
                canvasType = canvasType != CanvasType.Invisible ? CanvasType.Invisible : CanvasType.Normal;
                changed = true;
            } else if(block instanceof BlockCarpet && (material == Material.CARPET || material == Material.CLOTH)) {
                canvasType = canvasType != CanvasType.Flat ? CanvasType.Flat : CanvasType.Normal;
                changed = true;
            }
            if(changed) {
                markDirty();
                syncTE();
                player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.canvas_changed." + canvasType.name().toLowerCase()), true);
            }

            return stack;
        } else if(stack.getItem() == ModItems.ARTIFACT_FRAME) {
            if(cleanItemFrames() == 0) {
                populateItemFrames();
                player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.frames_populated"),true);
            } else
                player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.frames_cleared"),true);

            return stack;
        }

        String analysis = analyzeItem(stack);

        if(analysis != null) {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium."+analysis),true);

            return stack;
        }

        long timeUntilNextTurnIn = lastTurnIn + InteractionBWA.LEGENDARIUM_TURN_IN_DELAY - world.getTotalWorldTime();

        if(timeUntilNextTurnIn > 0) {
            long days = timeUntilNextTurnIn / 24000;
            long hours = (timeUntilNextTurnIn % 24000) / 1000;
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.not_now",days,hours),true);

            return stack;
        }

        IItemHandler queue = getInventory();
        ItemStack retain = queue.insertItem(0, ModItems.BROKEN_ARTIFACT.makeFrom(stack),false);
        if(retain.isEmpty()) {
            lastTurnIn = world.getTotalWorldTime();
        }

        if(queue.getSlots() >= InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE) {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.ready"),true);
        }
        return retain;
    }

    public String analyzeItem(ItemStack stack)
    {
        if(stack.isEmpty()) return "not_item";
        if(!ItemUtil.isTool(stack.getItem())) return "not_tool";
        if(!stack.hasDisplayName() && InteractionBWA.LEGENDARIUM_MUST_BE_NAMED) return "not_artifact";
        if(!stack.isItemEnchanted() && InteractionBWA.LEGENDARIUM_MUST_BE_ENCHANTED) return "not_artifact";
        int actualDamage = (stack.getMaxDamage() - stack.getItemDamage());
        double maxDamage = stack.getMaxDamage() * InteractionBWA.LEGENDARIUM_MIN_DAMAGE + InteractionBWA.LEGENDARIUM_DAMAGE_PAD;
        if(actualDamage > maxDamage) return "not_broken";
        if(stack.getRepairCost() <= 30 && InteractionBWA.LEGENDARIUM_MUST_BE_LIMIT) return "not_at_limit";
        return null;
    }

    public ItemStack retrieveItem(EntityPlayer player)
    {
        long time = world.getTotalWorldTime();
        IItemHandler queue = getInventory();

        if(queue.getSlots() < InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE) {
            player.sendStatusMessage(new TextComponentTranslation("tile.legendarium.not_enough_artifacts"),true);
            return ItemStack.EMPTY;
        }

        if(time - lastClick > 3 && queue.getSlots() >= InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE) {
            lastClick = time;
            ItemStack retrieved = queue.extractItem(0,1,false);
            populateItemFrames();
            return retrieved;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(readonlyHandler);
        return super.getCapability(capability, facing);
    }

    public IItemHandler getInventory() {
        LegendariumData data = LegendariumData.getInstance(world);
        return data.queue;
    }

    private void populateItemFrames()
    {
        int posterRange = InteractionBWA.LEGENDARIUM_POSTER_RANGE;
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-posterRange,-posterRange,-posterRange),pos.add(posterRange,posterRange,posterRange));
        List<EntityArtifactFrame> frames = world.getEntitiesWithinAABB(EntityArtifactFrame.class,posterArea,frame -> !frame.isLinked());
        //cleanItemFrames();

        frames.sort(Comparator.comparingDouble(o -> o.getDistanceSq(getPos())));
        int i = 0;
        for (EntityArtifactFrame frame : frames) {
            frame.link(this,i);
            i++;
        }

        /*IItemHandler queue = getInventory();
        int e = 0;
        for (int i = 0; i < queue.getSlots(); i++) {
            ItemStack artifact = queue.getStackInSlot(i);
            while(e < frames.size()) {
                EntityArtifactFrame frame = frames.get(e);
                e++;
                if(!isFrameClean(frame)) continue;
                frame.setDisplayedItem(artifact);
                frame.setCustomNameTag(artifact.getDisplayName());
                frame.setEntityInvulnerable(true);
                break;
            }
        }*/
    }

    private int cleanItemFrames()
    {
        int posterRange = InteractionBWA.LEGENDARIUM_POSTER_RANGE;
        AxisAlignedBB posterArea = new AxisAlignedBB(pos.add(-posterRange,-posterRange,-posterRange),pos.add(posterRange,posterRange,posterRange));
        List<EntityArtifactFrame> frames = world.getEntitiesWithinAABB(EntityArtifactFrame.class,posterArea,frame -> frame.isLinked());

        int ret = 0;
        for (EntityArtifactFrame frame : frames) {
            frame.unlink();
            ret++;
        }

        return ret;
    }

    @Override
    public void update() {
        if(migrationQueue != null) {
            migrate();
        }
    }

    public static class LegendariumData extends WorldSavedData implements IDirtyHandler
    {
        public static final String ID = "LegendariumInfo";

        QueueItemStackHandler queue;

        public LegendariumData(String id) {
            super(id);
            queue = new QueueItemStackHandler(this);
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            queue.deserializeNBT(compound.getCompoundTag("Queue"));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound compound) {
            compound.setTag("Queue",queue.serializeNBT());
            return compound;
        }

        public static LegendariumData getInstance(World world)
        {
            if (world != null)
            {
                WorldSavedData handler = world.getPerWorldStorage().getOrLoadData(LegendariumData.class, ID);
                if (handler == null) {
                    handler = new LegendariumData(ID);
                    world.getPerWorldStorage().setData(ID, handler);
                }

                return (LegendariumData) handler;
            }
            return null;
        }
    }
}
