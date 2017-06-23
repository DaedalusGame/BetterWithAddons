package betterwithaddons.handler;

import betterwithaddons.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolShardRepairHandler {
    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        if(event.getLeft().isEmpty() || event.getRight().isEmpty())
            return;

        ItemStack shard = event.getLeft();
        ItemStack mat = event.getRight();
        if(shard.hasCapability(ModItems.brokenArtifact.DATA_CAP,null))
        {
            ItemStack inner = shard.getCapability(ModItems.brokenArtifact.DATA_CAP,null).inner;
            if(!inner.isEmpty() && mat.getItem() == inner.getItem() && !mat.isItemDamaged())
            {
                ItemStack innercopy = inner.copy();
                innercopy.setItemDamage(0);
                innercopy.setRepairCost(innercopy.getRepairCost() / 2); //TODO: make this configurable??
                event.setOutput(innercopy);
                event.setCost(35);
            }
        }
    }
}
