package betterwithaddons.interaction.minetweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenClass("mods.betterwithaddons.IBeaconInfo")
@ZenRegister
public interface IBeaconInfo {
    static IBeaconInfo create(World world, BlockPos pos, int level) {
        return new IBeaconInfo() {
            @Override
            public IWorld getWorld() {
                return CraftTweakerMC.getIWorld(world);
            }

            @Override
            public IBlockPos getPos() {
                return CraftTweakerMC.getIBlockPos(pos);
            }

            @Override
            public int getLevel() {
                return level;
            }
        };
    }

    @ZenGetter("world")
    IWorld getWorld();

    @ZenGetter("pos")
    IBlockPos getPos();

    @ZenGetter("level")
    int getLevel();
}
