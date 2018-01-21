package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.InventoryUtil;
import betterwithaddons.util.ItemUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class RotHandler {
    public static final String CREATION_TIME_TAG = "TimeOfCreation";
    private LinkedList<EntityItem> TrackedItems = new LinkedList<>();
    private LinkedList<EntityItem> TrackedItemsAdd = new LinkedList<>();
    private Iterator<EntityItem> TrackedItemsIterator;

    public static final ResourceLocation ROT = new ResourceLocation(Reference.MOD_ID, "rot");
    //@CapabilityInject(Rot.class)
    //public static Capability<Rot> ROT_CAP;

    public static final long ONE_DAY = 24000;
    public static long MINECRAFT_DATE = -1;

    static Multimap<Item,RotInfo> rottingItems = HashMultimap.create();

    public static class RotInfo
    {
        ItemStack itemStack;
        ItemStack rottedStack;
        String baseName;
        long spoilTime;

        public RotInfo(ItemStack matchStack)
        {
            this(matchStack, InteractionBWA.MISC_ROT_TIME, "food", new ItemStack(ModItems.rottenFood));
        }

        public RotInfo(ItemStack matchStack, long spoilTime, String baseName, ItemStack rotStack)
        {
            this.itemStack = matchStack;
            this.spoilTime = spoilTime;
            this.baseName = baseName;
            this.rottedStack = rotStack;
        }

        public boolean matches(ItemStack stack)
        {
            return stack.getItem() == itemStack.getItem() && (stack.getMetadata() == itemStack.getMetadata() || itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE);
        }

        public boolean shouldSpoil(ItemStack stack, long creationDate)
        {
            return matches(stack) && MINECRAFT_DATE >= creationDate + spoilTime;
        }

        public ItemStack getRottenStack(ItemStack stack)
        {
            ItemStack returnStack = rottedStack.copy();
            returnStack.setCount(stack.getCount());
            return returnStack;
        }

        public String getUnlocalizedName(ItemStack stack, long creationDate)
        {
            double rotPercent = (MINECRAFT_DATE - creationDate) / (double)spoilTime;
            int rotPercentInt = Math.min(100,(int)(rotPercent * 100.0));

            String returnKey = getLocalizationKey(stack.getUnlocalizedName(),rotPercentInt);
            if(returnKey == null)
                returnKey = getLocalizationKey(baseName,rotPercentInt);

            return returnKey;
        }

        protected String getLocalizationKey(String baseName, int percent)
        {
            percent = (percent / 25) * 25;
            String testkey = baseName + ".rot." + percent;

            if(I18n.hasKey(testkey))
                return testkey;
            return null;
        }
    }

    public static void addRottingItem(ItemStack matchItem)
    {
        addRottingItem(matchItem, InteractionBWA.MISC_ROT_TIME, "food", new ItemStack(ModItems.rottenFood));
    }

    public static void addRottingItem(ItemStack matchItem, long timeToRot)
    {
        addRottingItem(matchItem, timeToRot, "food", new ItemStack(ModItems.rottenFood));
    }

    public static void addRottingItem(ItemStack matchItem, long timeToRot, String baseName, ItemStack rottedItem)
    {
        rottingItems.put(matchItem.getItem(),new RotInfo(matchItem,timeToRot,baseName,rottedItem));
    }

    //For adding custom filtering and behavior
    public static void addRottingItem(Item item, RotInfo rotInfo)
    {
        rottingItems.put(item,rotInfo);
    }

    public static void removeRottingItem(ItemStack matchItem)
    {
        ArrayList<RotInfo> toRemove = rottingItems.get(matchItem.getItem()).stream().filter(rotInfo -> rotInfo.matches(matchItem)).collect(Collectors.toCollection(ArrayList::new));
        for (RotInfo info: toRemove) {
            rottingItems.remove(matchItem.getItem(),info);
        }
    }

    public static boolean isRottingItem(ItemStack stack)
    {
        return rottingItems.containsKey(stack.getItem());
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        World world = event.getWorld();

        if(entity instanceof EntityItem)
        {
            ItemStack stack = ((EntityItem) entity).getItem();
            if(!stack.isEmpty() && isRottingItem(stack))
            {
                if(!entity.isDead)
                    TrackedItemsAdd.add((EntityItem)entity);
            }
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent tickEvent)
    {
        World world = tickEvent.world;

        if(!world.isRemote && tickEvent.phase == TickEvent.Phase.END)
        {
            if(world.provider.getDimension() == 0)
                MINECRAFT_DATE = (tickEvent.world.getTotalWorldTime() / ONE_DAY) * ONE_DAY;
            handleRottingWorldItems();
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.world;
        if(!world.isRemote && world.getTotalWorldTime() % 20 == 0 && event.phase == TickEvent.Phase.END)
        {
            for(int slot = 0; slot < player.inventory.getSizeInventory(); slot++)
            {
                ItemStack stack = player.inventory.getStackInSlot(slot);
                if(!stack.isEmpty() && isRottingItem(stack))
                {
                    long timeOfCreation = getCreationDate(stack);
                    if(timeOfCreation == -1) {
                        timeOfCreation = MINECRAFT_DATE;
                        setCreationDate(stack,timeOfCreation);
                    }

                    for(RotInfo info : rottingItems.get(stack.getItem()))
                    {
                        if(info.shouldSpoil(stack,timeOfCreation))
                        {
                            ItemStack containerItem = stack.getItem().getContainerItem(stack);
                            containerItem.setCount(stack.getCount());
                            ItemStack rottenItem = info.getRottenStack(stack);

                            if(containerItem.isEmpty())
                                player.inventory.setInventorySlotContents(slot,rottenItem);
                            else
                            {
                                player.inventory.setInventorySlotContents(slot,containerItem);
                                InventoryUtil.addItemToPlayer(player,rottenItem);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @SideOnly(Side.CLIENT)
    public void onToolTip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();

        if(isRottingItem(stack))
        {
            long timeOfCreation = getCreationDate(stack);
            if(timeOfCreation == -1)
                return;

            for(RotInfo info : rottingItems.get(stack.getItem())) {
                if(info.matches(stack)) {
                    String prefix =  I18n.format(info.getUnlocalizedName(stack, timeOfCreation));
                    if(prefix.length() > 0)
                        prefix = prefix + " ";

                    event.getToolTip().set(0,prefix + event.getToolTip().get(0));
                    break;
                }
            }
        }
    }

    private void handleRottingWorldItems()
    {
        if(TrackedItemsIterator == null || !TrackedItemsIterator.hasNext())
        {
            for (EntityItem entity : TrackedItemsAdd) {
                rotOneItem(entity);
                TrackedItems.add(entity);
            }
            TrackedItemsAdd.clear();
            TrackedItemsIterator = TrackedItems.iterator();
        }
        else
        {
            EntityItem entity = TrackedItemsIterator.next();
            World world = entity.world;
            ItemStack stack = entity.getItem();
            boolean remove = false;
            if(entity.isDead || stack.isEmpty() || !isRottingItem(stack))
                remove = true;
            else
            {
                rotOneItem(entity);
            }

            if(remove)
                TrackedItemsIterator.remove();
        }
    }

    private void rotOneItem(EntityItem entity) {
        World world = entity.world;
        ItemStack stack = entity.getItem();

        if(!isRottingItem(stack))
            return;

        //Rot rot = stack.getCapability(ROT_CAP,null);
        long timeOfCreation = getCreationDate(stack);
        if(timeOfCreation == -1) {
            timeOfCreation = MINECRAFT_DATE;
            setCreationDate(stack,timeOfCreation);
        }

        for(RotInfo info : rottingItems.get(stack.getItem()))
        {
            if(info.shouldSpoil(stack,timeOfCreation))
            {
                ItemStack containerItem = stack.getItem().getContainerItem(stack);
                containerItem.setCount(stack.getCount());
                ItemStack rottenItem = info.getRottenStack(stack);

                if(containerItem.isEmpty()) {
                    entity.setItem(rottenItem);
                    if(rottenItem.isEmpty())
                        entity.setDead();
                }
                else
                {
                    entity.setItem(containerItem);
                    if(!rottenItem.isEmpty())
                    {
                        EntityItem result = new EntityItem(world, entity.posX, entity.posY, entity.posZ, rottenItem);
                        result.setDefaultPickupDelay();
                        world.spawnEntity(result);
                    }
                }
                break;
            }
        }
    }

    public static long getCreationDate(ItemStack stack)
    {
        if(!isRottingItem(stack))
            return -1;
        NBTTagCompound compound = stack.getTagCompound();
        return compound != null && compound.hasKey(CREATION_TIME_TAG) ? compound.getLong(CREATION_TIME_TAG) : -1;
    }

    public static void setCreationDate(ItemStack stack, long value)
    {
        if(!isRottingItem(stack))
            return;
        stack.setTagInfo(CREATION_TIME_TAG,new NBTTagLong(value));
    }

    /*public static void registerCapability()
    {
        CapabilityManager.INSTANCE.register(Rot.class, new Capability.IStorage<Rot>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<Rot> capability, Rot instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<Rot> capability, Rot instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound)nbt);
            }
        }, Rot::new);
    }

    public static class Rot implements ICapabilitySerializable<NBTTagCompound>
    {
        public long timeOfCreation = -1;

        public Rot()
        {
            timeOfCreation = MINECRAFT_DATE;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == ROT_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) this : null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            if(timeOfCreation != -1)
                nbt.setLong("TimeOfCreation", timeOfCreation);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            if(nbt.hasKey("TimeOfCreation"))
                timeOfCreation = nbt.getLong("TimeOfCreation");
        }
    }*/

    @SubscribeEvent
    public void rotAttachCapability(AttachCapabilitiesEvent<ItemStack> event)
    {
        ItemStack stack = event.getObject();

        if(isRottingItem(stack) && MINECRAFT_DATE != -1) //All items instead?
        {
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null || !compound.hasKey(CREATION_TIME_TAG))
                stack.setTagInfo(CREATION_TIME_TAG,new NBTTagLong(MINECRAFT_DATE));
            //event.addCapability(ROT,new Rot());
        }
    }
}
