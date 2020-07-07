package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemGreatarrow extends Item {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/greatarrow.png");

    public ItemGreatarrow() {
        super();
    }

    public EntityGreatarrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityGreatarrow entityarrow = new EntityGreatarrow(worldIn, shooter);
        entityarrow.setArrowStack(stack);
        entityarrow.setDamage(getDamageBase());
        entityarrow.setBlockBreakPower(getBlockBreakPowerBase());
        return entityarrow;
    }

    public boolean canChargeFire() {
        return false;
    }

    public double getDamageBase() {
        return InteractionBWA.GREATARROW_DAMAGE;
    }

    public float getBlockBreakPowerBase() {
        return 3.0f;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
    }

    public ResourceLocation getEntityTexture(EntityGreatarrow arrow) {
        return TEXTURE;
    }

    public void hitBlock(EntityGreatarrow arrow, RayTraceResult rayTraceResult, boolean destroyed) {
        //NOOP
    }

    public void hitBlockFinal(EntityGreatarrow arrow, RayTraceResult rayTraceResult) {
        //NOOP
    }

    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        //NOOP
    }
}