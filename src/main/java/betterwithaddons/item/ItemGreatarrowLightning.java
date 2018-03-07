package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
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
        NBTTagCompound fireworksTagEntity = new NBTTagCompound();
        NBTTagCompound explosionEntity = new NBTTagCompound();
        explosionEntity.setBoolean("Flicker", true);
        explosionEntity.setByte("Type", (byte) 0);
        explosionEntity.setIntArray("Colors", new int[]{16383998});
        explosionEntity.setIntArray("FadeColors", new int[]{16701501}); //THESE HAVE TO BE MAGIC NUMBERS BECAUSE THE METHOD FOR GETTING COLOR VALUE FROM DYE IS CLIENTSIDE
        NBTTagList explosionsEntityList = new NBTTagList();
        explosionsEntityList.appendTag(explosionEntity);
        fireworksTagEntity.setTag("Explosions", explosionsEntityList);
        fireworksTagEntity.setByte("Flight", (byte)-100);
        fireworksEntity.setTag("Fireworks", fireworksTagEntity);

        fireworksGround = new NBTTagCompound();
        NBTTagCompound fireworksTagGround = new NBTTagCompound();
        NBTTagCompound explosionGround = explosionEntity.copy();
        explosionGround.setByte("Type", (byte) 1);
        NBTTagList explosionsGroundList = new NBTTagList();
        explosionsGroundList.appendTag(explosionGround);
        fireworksTagGround.setTag("Explosions", explosionsGroundList);
        fireworksTagGround.setByte("Flight", (byte)-100);
        fireworksGround.setTag("Fireworks", fireworksTagGround);
    }

    private void makeFireworks(World world, Vec3d pos, NBTTagCompound parameters)
    {
        if(!world.isRemote)
        {
            ItemStack rocket = new ItemStack(Items.FIREWORKS);
            rocket.setTagCompound(parameters);
            EntityFireworkRocket explosion = new EntityFireworkRocket(world,pos.x,pos.y,pos.z,rocket);
            explosion.setInvisible(true);
            world.spawnEntity(explosion);
        }
    }

    @Override
    public void hitBlockFinal(EntityGreatarrow arrow) {
        super.hitBlockFinal(arrow);
        if (!arrow.isDead && !arrow.world.isRemote) {
            makeLightningField(arrow.world,arrow.getPositionVector(),4.0f);
            makeFireworks(arrow.world,arrow.getPositionVector(),fireworksGround);
            arrow.setDead();
        }
    }

    @Override
    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        if (!(entity instanceof EntityLivingBase))
            return;

        if (!entity.world.isRemote) {
            EntityLivingBase living = (EntityLivingBase) entity;

            if (isDragon(living)) {
                DamageSource damagesource = EntityUtil.causeLightningArrowDamage(arrow.shootingEntity);

                int hurtSave = entity.hurtResistantTime;
                entity.hurtResistantTime = 0;
                if (!entity.attackEntityFrom(damagesource, EXTRA_LIGHTNING_DAMAGE)) {
                    entity.hurtResistantTime = hurtSave;
                }
            }

            living.addPotionEffect(new PotionEffect(ModPotions.electrified, 20 * 5, 2, false, false)); //electrify for a bit
            makeLightningField(arrow.world,arrow.getPositionVector(),1.0f);
            makeFireworks(arrow.world,arrow.getPositionVector(),fireworksEntity);
            arrow.setDead();
        }
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
