package betterwithaddons.interaction.minetweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.betterwithaddons.IBeaconInteractFunction")
@ZenRegister
public interface IBeaconInteractFunction {
    boolean interact(IWorld world, IBlockPos pos, int beaconlevel, IPlayer player, IItemStack stack);
}
