package betterwithaddons.interaction.minetweaker;

import betterwithaddons.util.IngredientCraftTweaker;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.BlockIngredientSpecial;
import betterwithmods.module.hardcore.beacons.BeaconEffect;
import betterwithmods.module.hardcore.beacons.HCBeacons;
import betterwithmods.module.hardcore.beacons.PotionBeaconEffect;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenClass(HCBeacon.clazz)
public class HCBeacon {
    public static final String clazz = "mods.betterwithaddons.HCBeacon";

    @ZenMethod
    public static void add(String name, IIngredient block, int tickrate, IBeaconCreateFunction create, IBeaconEffectFunction effect, IBeaconInteractFunction interact, IBeaconRemoveFunction remove) {
        BlockIngredient blockIngredient = new BlockIngredient(new IngredientCraftTweaker(block));
        HCBeacons.BEACON_EFFECTS.add(new BeaconEffect(name, blockIngredient, EntityLivingBase.class) {
            @Override
            public void onBeaconCreate(@Nonnull World world, @Nonnull BlockPos pos, int level) {
                if(create != null)
                    create.create(IBeaconInfo.create(world,pos,level));
            }

            @Override
            public void apply(NonNullList<EntityLivingBase> entitiesInRange, @Nonnull World world, @Nonnull BlockPos pos, int level) {
                if(effect != null)
                    effect.apply(IBeaconInfo.create(world,pos,level), getIEntities(entitiesInRange));
            }

            @Override
            public boolean onPlayerInteracted(World world, BlockPos pos, int level, EntityPlayer player, EnumHand hand, ItemStack stack) {
                if(interact != null)
                    return interact.interact(IBeaconInfo.create(world,pos,level), CraftTweakerMC.getIPlayer(player), CraftTweakerMC.getIItemStack(stack));
                return false;
            }

            @Override
            public void onBeaconBreak(World world, BlockPos pos, int level) {
                if(remove != null)
                    remove.remove(IBeaconInfo.create(world,pos,level));
            }
        }.setTickRate(tickrate));
    }

    @ZenMethod
    public static void setSounds(String name, String activation, String deactivation) {
        BeaconEffect effect = getEffect(name);
        if(effect == null) {
            CraftTweakerAPI.logError("beacon with name " + name + " doesn't exist");
            return;
        }
        if(activation != null)
            effect.setActivationSound(SoundEvent.REGISTRY.getObject(new ResourceLocation(activation)));
        if(deactivation != null)
            effect.setDeactivationSound(SoundEvent.REGISTRY.getObject(new ResourceLocation(deactivation)));
    }

    @ZenMethod
    public static void setColor(String name, int red, int green, int blue, int alpha) {
        BeaconEffect effect = getEffect(name);
        if(effect == null) {
            CraftTweakerAPI.logError("beacon with name " + name + " doesn't exist");
            return;
        }
        effect.setBaseBeamColor(new Color(red,green,blue,alpha));
    }

    @ZenMethod
    public static void setRanges(String name, int[] ranges) {
        BeaconEffect effect = getEffect(name);
        if(effect == null) {
            CraftTweakerAPI.logError("beacon with name " + name + " doesn't exist");
            return;
        }
        effect.setEffectRanges(ranges);
    }

    private static BeaconEffect getEffect(String name) {
        for (BeaconEffect effect : HCBeacons.BEACON_EFFECTS) {
            if(effect.getResourceLocation().toString().equals(name))
                return effect;
        }
        return null;
    }

    @ZenMethod
    public static void remove(String name) {
        HCBeacons.BEACON_EFFECTS.removeIf(beaconEffect -> beaconEffect.getResourceLocation().toString().equals(name));
    }

    private static List<IEntityLivingBase> getIEntities(NonNullList<EntityLivingBase> entitiesInRange) {
        List<IEntityLivingBase> entities = new ArrayList<>();
        for (EntityLivingBase entity : entitiesInRange) {
            entities.add(CraftTweakerMC.getIEntityLivingBase(entity));
        }
        return entities;
    }
}
