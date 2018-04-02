package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.item.ModItems;
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
            innercopy.setRepairCost((int) (innercopy.getRepairCost() * InteractionBWA.LEGENDARIUM_REPAIR_COST_MULTIPLIER));
            event.setOutput(innercopy);
            event.setCost(InteractionBWA.LEGENDARIUM_SHARD_COST);
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
