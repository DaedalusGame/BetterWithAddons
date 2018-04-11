package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class HorseFoodHandler {
    @SubscribeEvent
    public void onHorseEat(LivingEvent.LivingUpdateEvent event) {
        if(!InteractionBWA.HORSES_BREED_HAYBALE_PLACED)
            return;
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;
        if(!world.isRemote && entity instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) entity;
            if(!canHorseBreed(horse))
                return;
            Random random = horse.getRNG();
            BlockPos randPos = horse.getPosition().add(random.nextInt(3)-1,random.nextInt(2)-1,random.nextInt(3)-1);
            IBlockState state = world.getBlockState(randPos);
            double distance = 16.0;
            boolean hasPotentialMate = !world.getEntitiesInAABBexcluding(horse, horse.getEntityBoundingBox().expand(distance, distance, distance), otherEntity -> {
                if(otherEntity instanceof AbstractHorse)
                {
                    AbstractHorse otherHorse = (AbstractHorse) otherEntity;
                    if(otherHorse.isInLove() && !otherHorse.isBreeding())
                        return true;
                }
                return false;
            }).isEmpty();
            if(state.getBlock() == Blocks.HAY_BLOCK) {
                if(horse.isEatingHaystack() && (random.nextDouble() < 0.1 || hasPotentialMate)) {
                    horse.setInLove(null);
                    world.playEvent(2001, randPos, Block.getStateId(state));
                    world.setBlockToAir(randPos);
                }
                else if(!horse.isEatingHaystack() && !horse.isBeingRidden() && (random.nextDouble() < 0.01 || hasPotentialMate)) {
                    horse.setEatingHaystack(true);
                    horse.getLookHelper().setLookPosition(randPos.getX()+0.5,randPos.getY()+0.5,randPos.getZ()+0.5,30.0F,30.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public void onHorseUnmount(EntityMountEvent event) {
        Entity ridden = event.getEntityBeingMounted();
        if(!ridden.world.isRemote && ridden instanceof AbstractHorse) {
            AbstractHorse horse = (AbstractHorse) ridden;
            Entity rider = event.getEntityMounting();
            if(InteractionBWA.HORSES_SET_HOME && event.isDismounting() && horse.isTame() && isHorseSafe(horse, rider)) {
                horse.setHomePosAndDistance(horse.getPosition(),5);
                if(rider instanceof EntityPlayer) {
                    ((EntityPlayer) rider).sendStatusMessage(!horse.hasCustomName() ?
                            new TextComponentTranslation("info.horse.will_wait") :
                            new TextComponentTranslation("info.horse.your_horse_will_wait",horse.getCustomNameTag()),true);
                }
            }
        }
    }

    public static boolean isHorseSafe(AbstractHorse horse, Entity rider) {
        return !horse.isBurning() && !horse.isWet() && rider.hurtResistantTime <= 0 && horse.getRevengeTarget() == null;
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
            AbstractHorse horse = (AbstractHorse) entity;
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = player.getHeldItem(event.getHand());

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
