package betterwithaddons.entity;

import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ItemSamuraiArmor;
import betterwithaddons.util.SimpleWeightItem;
import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EntityKarateZombie extends EntityZombie implements IHasSpirits {
    private int nextCheck = 0;
    private List<EntitySpirit> attractedSpirits = new ArrayList<>();

    private static final String TAG_SPIRITS = "spirits";
    private static final String TAG_MOVE = "move";
    private static final String TAG_MOVETIMEOUT = "move_timeout";
    private static final String TAG_MOVETIME = "move_time";
    private static final String TAG_PERFORMING = "performing";

    private static final DataParameter<Integer> SPIRITS = EntityDataManager.createKey(EntityKarateZombie.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> MOVE = EntityDataManager.createKey(EntityKarateZombie.class, DataSerializers.VARINT);

    int moveTime = -1;
    int moveTimeout = 0;
    boolean performingMove = false;

    public EntityKarateZombie(World worldIn) {
        super(worldIn);
    }

    public int getSpirits() {
        return dataManager.get(SPIRITS);
    }

    public void setSpirits(int n) {
        dataManager.set(SPIRITS, Math.min(n, InteractionEriottoMod.MAX_SPIRITS));
    }

    public MartialArts getMove() {
        return MartialArts.values()[dataManager.get(MOVE)];
    }

    public void setMove(MartialArts n) {
        dataManager.set(MOVE, n.ordinal());
    }

    private int getMoveInternal() { return dataManager.get(MOVE); }

    private void setMoveInternal(int n) { dataManager.set(MOVE, n); }

    public void addSpirits(int n) {
        setSpirits(getSpirits() + n);
    }

    public MartialArts getCurrentMove()
    {
        return getMove();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0); //Yeah nah
    }

    @Override
    protected void applyEntityAI() {
        super.applyEntityAI();
        this.tasks.addTask(6, new EntityAIAvoidEntity<>(this,EntityPlayer.class, target -> isInFullSamuraiArmor(target) && this.getLevel() <= 1, 6.0F, 1.0D, 1.2D));
    }

    private boolean isInFullSamuraiArmor(EntityPlayer target) {
        ItemStack helmet = target.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = target.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack legs = target.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack boots = target.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        return helmet.getItem() instanceof ItemSamuraiArmor &&
                chest.getItem() instanceof ItemSamuraiArmor &&
                legs.getItem() instanceof ItemSamuraiArmor &&
                boots.getItem() instanceof ItemSamuraiArmor;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        int i = getSpirits();

        if(!isARealAmerican())
        while (i > 0)
        {
            int j = EntitySpirit.getSpiritSplit(i);
            i -= j;
            world.spawnEntity(new EntitySpirit(world, posX, posY, posZ, j));
        }
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        MartialArts currentMove = getCurrentMove();

        if(entityIn.getRidingEntity() == this || currentMove == MartialArts.Disarm)
            return false;

        boolean success = super.attackEntityAsMob(entityIn);

        if ((currentMove == MartialArts.Suplex || currentMove == MartialArts.Throw) && success && entityIn instanceof EntityLivingBase) {
            if (moveTime <= 0)
            {
                pickup(entityIn);
            }
        }

        return success;
    }

    private void pickup(Entity entityIn) {
        boolean success = entityIn.startRiding(this, true);
        if(success) {
            moveTime = getMoveTime(getCurrentMove());
            performingMove = true;
            if (entityIn instanceof EntityPlayer) {
                ((EntityPlayer) entityIn).sendStatusMessage(new TextComponentTranslation("hint.struggle"), true);
            }
        }
    }

    private int getMoveTime(MartialArts move) {
        switch(move)
        {
            case Throw:
                return 20;
            case Suplex:
                return (int)MathHelper.clampedLerp(20,10,getPower());
            case Disarm:
                return (int)MathHelper.clampedLerp(20,40,getPower());
            default:
                return 0;
        }
    }

    private int getMoveTimeout(MartialArts move) {
        switch(move)
        {
            case Disarm:
                return (int)MathHelper.clampedLerp(10,20,getPower());
            default:
                return (int)MathHelper.clampedLerp(60,20,getPower());
        }
    }

    private void randomizeMove()
    {
        int level = getLevel();
        List<SimpleWeightItem<MartialArts>> nextMoves = Lists.newArrayList();
        nextMoves.add(new SimpleWeightItem<>(MartialArts.Attack,100));
        if(level >= 2)
            nextMoves.add(new SimpleWeightItem<>(MartialArts.Throw,80));
        if(level >= 4)
            nextMoves.add(new SimpleWeightItem<>(MartialArts.Suplex,80));
        if(level >= 6)
            nextMoves.add(new SimpleWeightItem<>(MartialArts.Disarm,40));
        MartialArts currentMove = WeightedRandom.getRandomItem(rand,nextMoves).getItem();
        moveTimeout = getMoveTimeout(currentMove);
        performingMove = false;
        setMove(currentMove);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);

        int spiritsPerLevel = InteractionEriottoMod.SPIRIT_PER_LEVEL;
        int spirits = (spiritsPerLevel * 2) / 3;
        if(rand.nextInt(100) < 30)
            spirits += rand.nextInt(4) * spiritsPerLevel;
        else
            spirits += rand.nextInt(spiritsPerLevel);
        setSpirits(spirits);

        return data;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(SPIRITS, 0);
        dataManager.register(MOVE, 0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        EntityLivingBase entity = this.getAttackTarget();
        boolean success = super.attackEntityFrom(source, amount);

        if(success && getCurrentMove() == MartialArts.Disarm && !this.isDead)
        {
            if(entity == source.getImmediateSource() && entity != null)
            {
                disarm(entity);
            }

            randomizeMove();
        }

        return success;
    }

    private void disarm(EntityLivingBase entity) {
        ItemStack mainhand = entity.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offhand = entity.getHeldItem(EnumHand.OFF_HAND);

        if(!(mainhand.getItem() instanceof ItemShield) && !(offhand.getItem() instanceof ItemShield)) {
            if(!EnchantmentHelper.hasBindingCurse(mainhand)) {
                entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                entity.entityDropItem(mainhand,0);
            }
            if(!EnchantmentHelper.hasBindingCurse(offhand)) {
                entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                entity.entityDropItem(offhand, 0);
            }
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        float mult = damageMultiplier;
        List<Entity> passengers = getPassengers();
        if(getCurrentMove() == MartialArts.Suplex)
        {
            for (Entity passenger:passengers
                 ) {
                forceDismount(passenger);

            }
            float[] ret = net.minecraftforge.common.ForgeHooks.onLivingFall(this, distance, damageMultiplier);
            if (ret == null) return;
            distance = ret[0]; damageMultiplier = ret[1];
            PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
            float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);
            int i = MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);
            if (i > 0)
            {
                this.playSound(this.getFallSound(i), 1.0F, 1.0F);
                for (Entity passenger:passengers) {
                    passenger.attackEntityFrom(DamageSource.causeMobDamage(this), (float)i);
                }
            }
            mult = 0;
            randomizeMove();
        }
        super.fall(distance, mult);
    }

    public boolean isARealAmerican()
    {
        return hasCustomName() && getCustomNameTag().toLowerCase().equals("hulk hogan");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(isARealAmerican())
        {
            setSpirits(InteractionEriottoMod.MAX_SPIRITS);
        }

        if (!world.isRemote) {
            double power = getPower();
            getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(MathHelper.clampedLerp(3.0,6.0,power));
            getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(MathHelper.clampedLerp(0.23,0.5,power));

            moveTime--;
            moveTimeout--;

            List<Entity> passengers = getPassengers();

            float rad = 0.017453292F;
            switch(getCurrentMove())
            {
                case Suplex:
                    if (moveTime == 0 && passengers.size() > 0)
                    {
                        double jumppower = MathHelper.clampedLerp(1.5,2.0,power);
                        addVelocity(MathHelper.sin(this.rotationYaw * rad) * 0.1,jumppower,-MathHelper.cos(this.rotationYaw * rad) * 0.1);
                    }
                    break;
                case Throw:
                    if (moveTime == 0) {
                        double throwpower = MathHelper.clampedLerp(1.0,2.0,power);
                        for (Entity passenger : passengers)
                            if (passenger instanceof EntityLivingBase) {
                                forceDismount(passenger);
                                passenger.attackEntityFrom(DamageSource.causeMobDamage(this), (float)2);
                                passenger.addVelocity(throwpower * -MathHelper.sin(this.rotationYaw * rad), 0, throwpower * MathHelper.cos(this.rotationYaw * rad));
                            }
                        randomizeMove();
                    }
                    break;
            }

            if (moveTimeout <= 0 && !performingMove) {
                randomizeMove();
            }

            Vec3d pos = getPositionVector();
            float maxdist = 8.0f;
            AxisAlignedBB aabb = new AxisAlignedBB(pos.x,pos.y,pos.z, pos.x,pos.y,pos.z);

            if (nextCheck-- < 0) {
                attractedSpirits = world.getEntitiesWithinAABB(EntitySpirit.class, aabb.grow(maxdist - 0.5));
                nextCheck = 50;
            }

            int spirits = getSpirits();

            if (spirits < InteractionEriottoMod.MAX_SPIRITS)
                for (EntitySpirit spirit : attractedSpirits) {
                    double spiritdist = spirit.getDistanceSq(pos.x, pos.y, pos.z);

                    if (spiritdist < 1.2f) {
                        if (spirits < InteractionEriottoMod.MAX_SPIRITS) {
                            int consume = Math.min(InteractionEriottoMod.MAX_SPIRITS - spirits, spirit.xpValue);
                            addSpirits(consume);
                            spirit.xpValue -= consume;
                            if (spirit.xpValue <= 0)
                                spirit.setDead();
                        }
                        continue;
                    }

                    if (spiritdist > maxdist * maxdist)
                        continue;

                    double dx = (pos.x - spirit.posX) / 8.0D;
                    double dy = (pos.y - spirit.posY) / 8.0D;
                    double dz = (pos.z - spirit.posZ) / 8.0D;
                    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    double d5 = 1.0D - dist;

                    if (d5 > 0.0D) {
                        d5 = d5 * d5;
                        spirit.motionX += dx / dist * d5 * 0.1D;
                        spirit.motionY += dy / dist * d5 * 0.1D;
                        spirit.motionZ += dz / dist * d5 * 0.1D;
                    }
                }
        }
    }

    private void forceDismount(Entity passenger) {
        passenger.dismountRidingEntity();
    }

    private double getPower() {
        return getSpirits() / (double) InteractionEriottoMod.MAX_SPIRITS;
    }

    public int getLevel() {
        int level = getSpirits() / InteractionEriottoMod.SPIRIT_PER_LEVEL;

        return level;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        setSpirits(compound.getInteger(TAG_SPIRITS));
        setMoveInternal(compound.getInteger(TAG_MOVE));
        moveTime = compound.getInteger(TAG_MOVETIME);
        moveTimeout = compound.getInteger(TAG_MOVETIMEOUT);
        performingMove = compound.getBoolean(TAG_PERFORMING);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(TAG_SPIRITS, getSpirits());
        compound.setInteger(TAG_MOVE, getMoveInternal());
        compound.setInteger(TAG_MOVETIME, moveTime);
        compound.setInteger(TAG_MOVETIMEOUT, moveTimeout);
        compound.setBoolean(TAG_PERFORMING, performingMove);
    }

    public enum MartialArts
    {
        Attack,
        Suplex,
        Throw,
        Disarm
    }
}
