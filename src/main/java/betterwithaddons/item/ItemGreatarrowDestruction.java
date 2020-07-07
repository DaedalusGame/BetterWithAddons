package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemGreatarrowDestruction extends ItemGreatarrow {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/greatarrow_destruction.png");

    @Override
    public ResourceLocation getEntityTexture(EntityGreatarrow arrow) {
        return TEXTURE;
    }

    @Override
    public double getDamageBase() {
        return InteractionBWA.GREATARROW_DESTRUCTION_DAMAGE;
    }

    @Override
    public float getBlockBreakPowerBase() {
        return 5.0f;
    }

    @Override
    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        if(!(entity instanceof EntityLivingBase))
            return;

        if(!entity.world.isRemote) {
            EntityLivingBase living = (EntityLivingBase) entity;
            for (ItemStack armor : living.getArmorInventoryList()) {
                int durability = armor.getMaxDamage();
                if(!armor.isEmpty()) {
                    armor.damageItem((int)(InteractionBWA.GREATARROW_DESTRUCTION_DAMAGE_VS_ARMOR + durability * InteractionBWA.GREATARROW_DESTRUCTION_DAMAGE_VS_ARMOR_PERCENT), living);
                }
            }
        }
    }
}
