package betterwithaddons.handler;

import betterwithaddons.block.ModBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoapHandler {
    IBlockState soapState;

    public SoapHandler(IBlockState soapState)
    {
        this.soapState = soapState;
    }

    @SubscribeEvent
    public void blockNeighborUpdate(BlockEvent.NeighborNotifyEvent notifyEvent) {
        World world = notifyEvent.getWorld();
        makeWetSoap(world, notifyEvent.getPos());
    }

    private void makeWetSoap(World world, BlockPos pos) {
        IBlockState blockstate = world.getBlockState(pos);
        IBlockState bottomblock = world.getBlockState(pos.down());
        if (!world.isRemote && blockstate.getMaterial() == Material.WATER && bottomblock.equals(soapState)) {
            world.setBlockState(pos.down(), ModBlocks.WET_SOAP.getDefaultState());
        }
    }
}
