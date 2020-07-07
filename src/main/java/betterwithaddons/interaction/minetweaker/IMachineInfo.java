package betterwithaddons.interaction.minetweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import crafttweaker.api.world.IBlockPos;
import crafttweaker.api.world.IWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenClass("mods.betterwithaddons.IMachineInfo")
@ZenRegister
public interface IMachineInfo {
    static IMachineInfo create(World world, BlockPos pos, EntityPlayer player) {
        return new IMachineInfo() {
            @Override
            public IPlayer getPlayer() {
                return CraftTweakerMC.getIPlayer(player);
            }

            @Override
            public IWorld getWorld() {
                return CraftTweakerMC.getIWorld(world);
            }

            @Override
            public IBlockPos getPos() {
                return CraftTweakerMC.getIBlockPos(pos);
            }
        };
    }

    static IMachineInfo create(TileEntity tile) {
        return create(tile.getWorld(),tile.getPos(),null);
    }

    @ZenGetter("player")
    IPlayer getPlayer();

    @ZenGetter("world")
    IWorld getWorld();

    @ZenGetter("pos")
    IBlockPos getPos();
}
