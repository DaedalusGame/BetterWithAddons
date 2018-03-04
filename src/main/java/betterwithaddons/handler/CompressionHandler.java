package betterwithaddons.handler;

import betterwithaddons.crafting.manager.CraftingManagerCrate;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.util.InvUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;

public class CompressionHandler {
    HashSet<TileEntityFilteredHopper> activeHoppers = new HashSet<>();

    @SubscribeEvent
    public void hardcorePackingInit(AttachCapabilitiesEvent<TileEntity> event)
    {
        TileEntity te = event.getObject();
        if(te instanceof TileEntityFilteredHopper)
        {
            activeHoppers.add((TileEntityFilteredHopper) te);
        }
    }

    @SubscribeEvent
    public void crateCompress(TickEvent.WorldTickEvent event) {
        HashSet<TileEntityFilteredHopper> toIterate = new HashSet<>(activeHoppers);
        HashSet<TileEntityFilteredHopper> toRemove = new HashSet<>();
        for (TileEntityFilteredHopper hopper: toIterate) {
            World world = hopper.getWorld();

            if(hopper.isInvalid())
                toRemove.add(hopper);

            if(world != null && !world.isRemote && !hopper.isPowered())
            {
                BlockPos pos = hopper.getPos();
                BlockPos cratePos = pos.down();
                IBlockState crateState = world.getBlockState(cratePos);

                if(isCrate(crateState) && hopper.getFilterStack().isEmpty())
                {
                    if(CraftingManagerCrate.getInstance().getCraftingResult(hopper.inventory) != null)
                    {
                        NonNullList<ItemStack> output = CraftingManagerCrate.getInstance().craftItem(world, hopper, hopper.inventory);
                        world.setBlockToAir(cratePos);
                        output.stream().filter(stack -> !stack.isEmpty()).forEach(stack -> InvUtils.ejectStack(world, cratePos, stack.copy()));
                    }
                }
            }
        }

        activeHoppers.removeAll(toRemove);
    }

    public boolean isCrate(IBlockState state)
    {
        return state.getBlock() == Blocks.PLANKS && state.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.OAK;
    }
}
