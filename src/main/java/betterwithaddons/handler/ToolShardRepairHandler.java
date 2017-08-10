package betterwithaddons.handler;

import betterwithaddons.item.ModItems;
import betterwithaddons.util.ItemUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolShardRepairHandler {
    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().isEmpty() || event.getRight().isEmpty())
            return;

        ItemStack shard = event.getLeft();
        ItemStack mat = event.getRight();
        ItemStack artifact = ModItems.brokenArtifact.getInnerStack(shard);
        if (!artifact.isEmpty() && mat.getItem() == artifact.getItem() && !mat.isItemDamaged()) {
            ItemStack innercopy = artifact.copy();
            innercopy.setItemDamage(0);
            innercopy.setRepairCost(innercopy.getRepairCost() / 2); //TODO: make this configurable??
            event.setOutput(innercopy);
            event.setCost(35);
        }
    }

    /*@SubscribeEvent
    public void onArtifactBreak(AnvilUpdateEvent event)
    {
        if(event.getLeft().isEmpty() || event.getRight().isEmpty())
            return;

        ItemStack tool = event.getLeft();
        ItemStack mat = event.getRight();
        if(ItemUtil.isTool(tool.getItem()) && mat.getItem() == Items.FLINT)
        {
            event.setOutput(ModItems.brokenArtifact.makeFrom(tool));
            event.setCost(1);
        }
    }*/
}
