package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBTWTweak;
import betterwithaddons.util.MiscUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class EggIncubationHandler {
    private LinkedList<EntityItem> TrackedItems = new LinkedList<>();
    private LinkedList<EntityItem> TrackedItemsAdd = new LinkedList<>();
    private Iterator<EntityItem> TrackedItemsIterator;

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();

        if(entity instanceof EntityItem)
        {
            ItemStack stack = ((EntityItem) entity).getItem();
            if(!stack.isEmpty() && stack.getItem() == Items.EGG)
            {
                TrackedItemsAdd.add((EntityItem)entity);
            }
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent tickEvent)
    {
        World world = tickEvent.world;
        if(!world.isRemote && tickEvent.phase == TickEvent.Phase.START) {
            handleEggs();
        }
    }

    private void handleEggs()
    {
        if(TrackedItemsIterator == null || !TrackedItemsIterator.hasNext())
        {
            TrackedItems.addAll(TrackedItemsAdd);
            TrackedItemsAdd.clear();
            TrackedItemsIterator = TrackedItems.iterator();
        }
        else
        {
            EntityItem entity = TrackedItemsIterator.next();
            World world = entity.world;
            ItemStack stack = entity.getItem();
            BlockPos pos = entity.getPosition();
            boolean remove = false;
            if(!world.isBlockLoaded(pos) || entity.isDead || stack.isEmpty() || stack.getItem() != Items.EGG || stack.getCount() > 1)
                remove = true;
            else {
                if((int) ObfuscationReflectionHelper.getPrivateValue(EntityItem.class, entity, "d", "field_70292_b", "age") > InteractionBTWTweak.EGG_INCUBATION_TIME && MiscUtil.hasPadding(world,pos.down()) && MiscUtil.hasLitLight(world,pos.up()))
                {
                    world.playSound(null,entity.posX,entity.posY,entity.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL,  0.25F, world.rand.nextFloat() * 1.5F + 1.0F);
                    EntityChicken chick = new EntityChicken(world);
                    chick.setGrowingAge(-24000);
                    chick.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0F, 0F);
                    world.spawnEntity(chick);
                    stack.shrink(1);
                    if (stack.isEmpty()) entity.setDead();
                }
            }

            if(remove)
                TrackedItemsIterator.remove();
        }
    }
}
