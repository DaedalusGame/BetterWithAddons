package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWM;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

import java.util.HashSet;

public class HardcoreWoolHandler {
    public static HashSet<String> EXTRA_SHEARS = new HashSet<>();

    @SubscribeEvent
    public void onShearEventTry1(PlayerInteractEvent.EntityInteract event)
    {
        shearSheep(event, event.getTarget());
    }

    @SubscribeEvent
    public void onShearEventTry2(PlayerInteractEvent.EntityInteractSpecific event)
    {
        shearSheep(event, event.getTarget());
    }

    private void shearSheep(PlayerInteractEvent event, Entity target) {
        if(!InteractionBWM.HARDCORE_SHEARING)
            return;

        World world = event.getWorld();
        EntityPlayer player = event.getEntityPlayer();
        BlockPos pos = player.getPosition();
        ItemStack tool = event.getItemStack();

        if(!world.isRemote && !target.isDead && target instanceof IShearable && isShears(tool))
        {
            event.setCanceled(true);
            event.setCancellationResult(EnumActionResult.PASS);
            IShearable sheep = (IShearable) target;
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
            event.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }

    private boolean isShears(ItemStack stack)
    {
        Item item = stack.getItem();
        String regname = item.getRegistryName().toString(); //no it may not produce a nullpointer exception. If it does, we shouldn't catch it.
        return item instanceof ItemShears || EXTRA_SHEARS.contains(regname);
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
