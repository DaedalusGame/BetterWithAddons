package betterwithaddons.enchantment;

import betterwithaddons.util.ItemUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

public class EnchantmentMendingGoldOnly extends EnchantmentMending {
    private static HashSet<String> goldItems = new HashSet<>();

    public static void reset() {
        goldItems.clear();
    }

    public static void register(String goldItem) {
        goldItems.add(goldItem);
    }

    public EnchantmentMendingGoldOnly(Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, slots);
        type = EnumHelper.addEnchantmentType("GOLD_ONLY", this::appliesToItem); //Fuck you
        MinecraftForge.EVENT_BUS.register(this);
    }

    private boolean appliesToItem(Item item)
    {
        Item.ToolMaterial toolMaterial = ItemUtil.getToolMaterial(item);
        ItemArmor.ArmorMaterial armorMaterial = ItemUtil.getArmorMaterial(item);

        if (toolMaterial != null && toolMaterial.name().equals("GOLD"))
            return true;
        if (armorMaterial != null && armorMaterial.name().equals("GOLD"))
            return true;
        if (goldItems.contains(item.getRegistryName().toString()))
            return true;
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return appliesToItem(stack.getItem());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onXP(PlayerPickupXpEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, player);
        EntityXPOrb orb = event.getOrb();

        if (!appliesToItem(stack.getItem()))
        {
            player.xpCooldown = 2;
            player.onItemPickup(orb, 1);

            if (orb.xpValue > 0)
            {
                player.addExperience(orb.xpValue);
            }

            orb.setDead();
        }
    }
}
