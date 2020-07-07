package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.entity.EntityLightningArc;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.lib.Reference;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class ItemGreatarrowLightning extends ItemGreatarrow {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/greatarrow_lightning.png");
    public static final ResourceLocation TEXTURE_CHARGED = new ResourceLocation(Reference.MOD_ID, "textures/entity/greatarrow_lightning_aspect.png");

    NBTTagCompound fireworksEntity;
    NBTTagCompound fireworksGround;

    public ItemGreatarrowLightning() {
        super();
        fireworksEntity = new NBTTagCompound();
        NBTTagCompound fireworksTagEntity = new NBTTagCompound();
        NBTTagCompound explosionEntity = new NBTTagCompound();
        explosionEntity.setBoolean("Flicker", true);
        explosionEntity.setByte("Type", (byte) 0);
        explosionEntity.setIntArray("Colors", new int[]{InteractionBWA.GREATARROW_LIGHTNING_COLOR.getRGB()});
        explosionEntity.setIntArray("FadeColors", new int[]{InteractionBWA.GREATARROW_LIGHTNING_FADE.getRGB()}); //THESE HAVE TO BE MAGIC NUMBERS BECAUSE THE METHOD FOR GETTING COLOR VALUE FROM DYE IS CLIENTSIDE
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

    @Override
    public boolean canChargeFire() {
        return true;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGreatarrow arrow) {
        if(arrow.isCharged())
            return TEXTURE_CHARGED;
        return TEXTURE;
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
    public float getBlockBreakPowerBase() {
        return 0.0f;
    }

    @Override
    public double getDamageBase() {
        return InteractionBWA.GREATARROW_LIGHTNING_DAMAGE;
    }

    @Override
    public void hitBlockFinal(EntityGreatarrow arrow, RayTraceResult rayTraceResult) {
        super.hitBlockFinal(arrow, rayTraceResult);
        if (!arrow.isDead && !arrow.world.isRemote) {
            makeLightningField(arrow.world, rayTraceResult.hitVec,4.0f);
            if(arrow.isCharged())
                makeLightningArc(arrow.world, rayTraceResult.hitVec, new Vec3d(arrow.motionX, arrow.motionY, arrow.motionZ));
            makeFireworks(arrow.world, rayTraceResult.hitVec, fireworksGround);
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
                DamageSource damagesource = EntityUtil.causeLightningArrowDamage(arrow.shootingEntity,arrow);

                int hurtSave = entity.hurtResistantTime;
                entity.hurtResistantTime = 0;
                if (!entity.attackEntityFrom(damagesource, (float) InteractionBWA.GREATARROW_LIGHTNING_DAMAGE_VS_DRAGON)) {
                    entity.hurtResistantTime = hurtSave;
                }
            }

            living.addPotionEffect(new PotionEffect(ModPotions.electrified, 20 * 5, 2, false, false)); //electrify for a bit
            makeLightningField(arrow.world,arrow.getPositionVector(),1.0f);
            if(arrow.isCharged())
                makeLightningArc(arrow.world, new Vec3d(entity.posX,entity.posY + entity.height/2,entity.posZ), new Vec3d(arrow.motionX, arrow.motionY, arrow.motionZ));
            makeFireworks(arrow.world,arrow.getPositionVector(),fireworksEntity);
            arrow.setDead();
        }
    }

    private static void makeLightningField(World world, Vec3d pos, double range) {
        AxisAlignedBB aabb = new AxisAlignedBB(pos.x-range, pos.y-range, pos.z-range, pos.x+range, pos.y+range, pos.z+range);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

        for (EntityLivingBase living : list) {
            double distSq = living.getDistanceSq(pos.x, pos.y, pos.z);

            if (distSq < range * range) {
                living.addPotionEffect(new PotionEffect(ModPotions.electrified, 20 * 7, 2, false, false));
            }
        }
    }

    private static void makeLightningArc(World world, Vec3d pos, Vec3d velocity) {
        if(world.canSeeSky(new BlockPos(pos).up())) {
            double angle = Math.atan2(velocity.x, velocity.z);
            double width = Math.PI * 0.2;
            EntityLightningArc arc = new EntityLightningArc(world, pos.x, pos.y, pos.z);
            arc.setup(angle - width, angle + width, 40, 30);
            world.spawnEntity(arc);
        }
    }

    private static boolean isDragon(Entity target) {
        ResourceLocation location = EntityList.getKey(target);
        if (location == null) return false;
        String lowercase = location.getResourcePath().toLowerCase();
        return lowercase.contains("dragon") && !lowercase.contains("dragonfly");
    }
}
