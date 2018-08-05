package betterwithaddons.interaction.minetweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.List;

@ZenClass("mods.betterwithaddons.IBeaconEffectFunction")
@ZenRegister
public interface IBeaconEffectFunction {
    boolean apply(IWorld world, IBlockPos pos, int beaconlevel, List<IEntityLivingBase> entities);
}
