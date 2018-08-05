package betterwithaddons.interaction.minetweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import stanhebben.zenscript.annotations.ZenClass;

@ZenClass("mods.betterwithaddons.IBeaconCreateFunction")
@ZenRegister
public interface IBeaconCreateFunction {
    void create(IWorld world, IBlockPos pos, int beaconlevel);
}
