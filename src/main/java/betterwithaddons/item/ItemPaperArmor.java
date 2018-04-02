package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class ItemPaperArmor extends ItemArmor implements ISpecialArmor {
    public static final ArmorMaterial PAPER = EnumHelper.addArmorMaterial("paper", "betterwithaddons:paper", 10, new int[]{1, 4, 5, 2}, 5, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
    public static final EntityEquipmentSlot[] ARMOR_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    public ItemPaperArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(PAPER, 2, equipmentSlotIn);
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        MinecraftForge.EVENT_BUS.register(getClass());
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MOD_ID + ":textures/entity/paper" + (this.armorType.getSlotIndex() == 2 ? "2" : "1") + ".png";
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return true;
    }

    @SubscribeEvent
    public static void onWear(LivingEvent.LivingUpdateEvent event)
    {
        int equippedPieces = 0;
        EntityLivingBase entity = event.getEntityLiving();
        for (EntityEquipmentSlot slot : ARMOR_SLOTS)
        {
            ItemStack armor = entity.getItemStackFromSlot(slot);
            if(armor.getItem() instanceof ItemPaperArmor)
            {
                equippedPieces++;
                if(entity.isBurning())
                    armor.damageItem(1, entity);
            }
        }

        if(entity.isWet()) {
            PotionEffect wetness = new PotionEffect(MobEffects.SLOWNESS,20*60,Math.max(0,equippedPieces-1),true,false);
            entity.addPotionEffect(wetness);
        }
    }

    private int getWornPieces(EntityLivingBase player)
    {
        int equippedPieces = 0;
        for (EntityEquipmentSlot slot : ARMOR_SLOTS)
        {
            ItemStack armor = player.getItemStackFromSlot(slot);
            if(armor.getItem() instanceof ItemPaperArmor)
                equippedPieces++;
        }
        return equippedPieces;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties armorProperties = new ArmorProperties(0, 0, Integer.MAX_VALUE);
        armorProperties.Armor = damageReduceAmount + getWetnessBonus(entity);
        armorProperties.Toughness = toughness;
        return armorProperties;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return getWetnessBonus(player);
    }

    private int getWetnessBonus(EntityLivingBase entity) {
        return entity.isWet() && getWornPieces(entity) > 3 ? 1 : 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        //NOOP
    }
}
