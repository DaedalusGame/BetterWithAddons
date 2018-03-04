package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemGreatarrowLightning extends ItemGreatarrow {
    static final float EXTRA_LIGHTNING_DAMAGE = 20f;
    NBTTagCompound fireworksEntity;
    NBTTagCompound fireworksGround;

    public ItemGreatarrowLightning() {
        super();
        fireworksEntity = new NBTTagCompound();
        NBTTagCompound explosionEntity = new NBTTagCompound();
        explosionEntity.setBoolean("Flicker", true);
        explosionEntity.setByte("Type", (byte) 0);
        explosionEntity.setIntArray("Colors", new int[]{EnumDyeColor.WHITE.getColorValue()});
        explosionEntity.setIntArray("FadeColors", new int[]{EnumDyeColor.YELLOW.getColorValue()});
        NBTTagList explosionsEntityList = new NBTTagList();
        explosionsEntityList.appendTag(explosionEntity);
        fireworksEntity.setTag("Explosions", explosionsEntityList);

        fireworksGround = new NBTTagCompound();
        NBTTagCompound explosionGround = explosionEntity.copy();
        explosionGround.setByte("Type", (byte) 1);
        NBTTagList explosionsGroundList = new NBTTagList();
        explosionsGroundList.appendTag(explosionGround);
        fireworksGround.setTag("Explosions", explosionsGroundList);
    }

    @Override
    public void hitBlock(EntityGreatarrow arrow, BlockPos pos, IBlockState state, boolean destroyed) {
        super.hitBlock(arrow, pos, state, destroyed);
        if (!destroyed && !arrow.isDead) {
            arrow.world.makeFireworks(arrow.posX, arrow.posY, arrow.posZ, 0, 0, 0, fireworksGround);
            //Lightning field
            arrow.setDead();
        }
    }

    @Override
    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        if (!(entity instanceof EntityLivingBase))
            return;

        if (!entity.world.isRemote) {
            EntityLivingBase living = (EntityLivingBase) entity;
            living.addPotionEffect(new PotionEffect(ModPotions.electrified, 20 * 5, 2, false, false)); //electrify for a bit

            if (isDragon(living)) {
                DamageSource damagesource;
                if (arrow.shootingEntity == null)
                    damagesource = EntityUtil.causeLightningArrowDamage(arrow, arrow);
                else
                    damagesource = EntityUtil.causeLightningArrowDamage(arrow, arrow.shootingEntity);

                if (entity.attackEntityFrom(damagesource, EXTRA_LIGHTNING_DAMAGE)) {
                    entity.hurtResistantTime = 0;
                }
            }
        }

        arrow.world.makeFireworks(arrow.posX, arrow.posY, arrow.posZ, 0, 0, 0, fireworksEntity);
    }

    private static void makeLightningField(World world, Vec3d pos, double range) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos.addVector(-range, -range, -range), pos.addVector(range, range, range));
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

        for (EntityLivingBase living : list) {
            double distSq = living.getDistanceSq(pos.x, pos.y, pos.z);

            if (distSq < range * range) {
                living.addPotionEffect(new PotionEffect(ModPotions.electrified, 20 * 7, 2, false, false));
            }
        }
    }

    private static boolean isDragon(Entity target) {
        ResourceLocation location = EntityList.getKey(target);
        if (location == null) return false;
        String lowercase = location.getResourcePath().toLowerCase();
        return lowercase.contains("dragon") && !lowercase.contains("dragonfly");
    }
}
