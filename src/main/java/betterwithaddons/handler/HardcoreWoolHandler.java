package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWM;
import betterwithaddons.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HardcoreWoolHandler {
    @SubscribeEvent
    public void onShearEvent(PlayerInteractEvent.EntityInteractSpecific event)
    {
        if(!InteractionBWM.HARDCORE_SHEARING)
            return;

        World world = event.getWorld();
        BlockPos pos = event.getEntity().getPosition();
        ItemStack tool = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        if(!world.isRemote && event.getTarget() instanceof IShearable && tool.getItem() instanceof ItemShears)
        {
            IShearable sheep = (IShearable) event.getTarget();
            if(!sheep.isShearable(tool,world,pos))
                return;
            java.util.Random rand = new java.util.Random();
            for(ItemStack stack : InteractionBWM.convertShearedWool(sheep.onSheared(tool,world,pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,tool))))
            {
                EntityItem ent = target.entityDropItem(stack, 1.0F);
                ent.motionY += rand.nextFloat() * 0.05F;
                ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
                ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
            }
            tool.damageItem(1, player);
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public void sheepDropEvent(LivingDropsEvent event)
    {
        Entity entity = event.getEntity();
        World world = entity.world;
        if(entity instanceof IShearable && !world.isRemote)
        {
            InteractionBWM.convertShearedWoolEntities(event.getDrops());
        }
    }
}
