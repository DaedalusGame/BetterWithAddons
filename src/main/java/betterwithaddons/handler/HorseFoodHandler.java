package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class HorseFoodHandler {
    @SubscribeEvent
    public void onHorseEat(LivingEvent.LivingUpdateEvent event) {
        if(!InteractionBWA.HORSES_BREED_HAYBALES)
            return;
        EntityLivingBase entity = event.getEntityLiving();
        if(!entity.world.isRemote && entity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) entity;
            Random random = horse.getRNG();
            if(canHorseBreed(horse) && horse.isEatingHaystack() && random.nextDouble() < 0.01) {
                horse.setInLove(null);
            }
        }
    }

    @SubscribeEvent
    public void onHorseUnmount(EntityMountEvent event) {
        if(!InteractionBWA.HORSES_SET_HOME)
            return;
        Entity ridden = event.getEntityBeingMounted();
        if(!ridden.world.isRemote && ridden instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) ridden;
            Entity rider = event.getEntityMounting();
            if(event.isDismounting() && horse.isTame() && isHorseSafe(horse, rider)) {
                horse.setHomePosAndDistance(horse.getPosition(),3);
                if(rider instanceof EntityPlayer) {
                    ((EntityPlayer) rider).sendStatusMessage(new TextComponentTranslation("info.horse.will_wait",horse.hasCustomName() ? horse.getCustomNameTag() : "info.horse.your_horse"),true);
                }
            }
        }
    }

    public static boolean isHorseSafe(AbstractHorse horse, Entity rider) {
        return !horse.isBurning() && !horse.isWet() && rider.hurtResistantTime <= 0 && horse.getRevengeTarget() != null;
    }

    @SubscribeEvent
    public void onFeedHorse(PlayerInteractEvent.EntityInteract event) {
        onFeed(event.getTarget(),event);
    }

    @SubscribeEvent
    public void onFeedHorseSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        onFeed(event.getTarget(),event);
    }

    public void onFeed(Entity entity, PlayerInteractEvent event) {
        if(entity instanceof AbstractHorse) {
            ItemStack stack = event.getEntityPlayer().getHeldItem(event.getHand());

            if(InteractionBWA.HORSES_IGNORE_GOLD && (stack.getItem() == Items.GOLDEN_APPLE || stack.getItem() == Items.GOLDEN_CARROT)) {
                event.setCancellationResult(EnumActionResult.FAIL);
                event.setCanceled(true);
            }
        }
    }

    public static boolean canHorseBreed(AbstractHorse horse) {
        return horse.isTame() && horse.getGrowingAge() == 0 && !horse.isInLove();
    }
}
