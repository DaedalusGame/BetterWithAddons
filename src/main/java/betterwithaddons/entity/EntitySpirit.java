package betterwithaddons.entity;

import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

public class EntitySpirit extends Entity {
    /** A constantly increasing value that RenderXPOrb uses to control the colour shifting (Green / yellow) */
    public int xpColor;
    /** The age of the XP orb in ticks. */
    public int orbAge;
    public int delayBeforeCanPickup;
    /** The health of this XP orb. */
    private int orbHealth = 5;
    /** The closest EntityPlayer to this orb. */
    private EntityPlayer closestPlayer;

    private static final DataParameter<Integer> SPIRITS = EntityDataManager.createKey(EntitySpirit.class, DataSerializers.VARINT);

    private float currentAngle;
    private int nextCheck;

    public EntitySpirit(World worldIn) {
        super(worldIn);
        this.currentAngle = (float)Math.toRadians(rand.nextInt(360));
    }

    public EntitySpirit(World worldIn, double x, double y, double z, int val) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.motionX = (double)((float)(Math.random() * 0.2D - 0.1D) * 2.0F);
        this.motionY = (double)((float)(Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double)((float)(Math.random() * 0.2D - 0.1D) * 2.0F);
        setSpiritValue(val);
        this.currentAngle = (float)Math.toRadians(rand.nextInt(360));
    }

    protected void entityInit() {
        dataManager.register(SPIRITS, 0);
    }

    public int getSpiritValue() {
        return dataManager.get(SPIRITS);
    }

    public void setSpiritValue(int value) {
        dataManager.set(SPIRITS,value);
    }

    private void applyGravity()
    {
        RayTraceResult result = world.rayTraceBlocks(new Vec3d(posX,posY,posZ),new Vec3d(posX,posY - 1.5,posZ));

        if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            this.motionY += 0.02D;
        else
            this.motionY -= 0.02D;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.delayBeforeCanPickup > 0)
        {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity())
        {
            applyGravity();
        }

        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA)
        {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
        double maxdist = 8.0D;

        if (this.xpColor - 20 + this.getEntityId() % 100 > nextCheck)
        {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > maxdist * maxdist)
            {
                this.closestPlayer = this.world.getClosestPlayer(posX, posY, posZ, maxdist, player -> InventoryUtil.getInventorySlotContainItem(((EntityPlayer)player).inventory,Items.GLASS_BOTTLE) >= 0);
            }

            this.nextCheck = this.xpColor;
            this.currentAngle = (float)Math.toRadians(rand.nextInt(360));
        }

        if (this.closestPlayer != null && this.closestPlayer.isSpectator())
        {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null)
        {
            double dx = (this.closestPlayer.posX - this.posX) / 8.0D;
            double dy = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() / 2.0D - this.posY) / 8.0D;
            double dz = (this.closestPlayer.posZ - this.posZ) / 8.0D;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double d5 = 1.0D - dist;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                this.motionX += dx / dist * d5 * 0.1D;
                this.motionY += dy / dist * d5 * 0.1D;
                this.motionZ += dz / dist * d5 * 0.1D;
            }
        }
        else
        {
            double dx = MathHelper.sin(currentAngle) * 0.01D;
            double dz = MathHelper.cos(currentAngle) * 0.01D;

            this.motionX += dx;
            this.motionZ += dz;
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = 0.98F;

        if (this.onGround)
        {
            f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
        }

        this.motionX *= (double)f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double)f;

        if (this.onGround)
        {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.orbAge;

        if (this.orbAge >= InteractionEriottoMod.MAX_SPIRIT_AGE)
        {
            this.setDead();
        }
    }

    public void onCollideWithPlayer(EntityPlayer player)
    {
        if (!this.world.isRemote)
        {
            if (this.delayBeforeCanPickup == 0 && player.xpCooldown == 0)
            {
                player.xpCooldown = 2;

                List<EntitySpirit> spirits = world.getEntitiesWithinAABB(EntitySpirit.class, new AxisAlignedBB(this.getPosition()).grow(0.5));
                int totalSpirit = 0;

                for(EntitySpirit spirit : spirits)
                    totalSpirit += spirit.getSpiritValue();

                if (totalSpirit >= InteractionEriottoMod.BOTTLE_MAX_SPIRITS)
                {
                    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                        ItemStack itemstack = player.inventory.getStackInSlot(i);
                        if (itemstack.getItem() != Items.GLASS_BOTTLE) continue;
                        itemstack.shrink(1);
                        player.inventory.setInventorySlotContents(i,itemstack);
                        InventoryUtil.addItemToPlayer(player,new ItemStack(ModItems.ANCESTRY_BOTTLE));
                        int consumed = InteractionEriottoMod.BOTTLE_MAX_SPIRITS;
                        for(EntitySpirit spirit : spirits)
                        {
                            int spiritsCached = spirit.getSpiritValue();
                            int c = Math.min(consumed,spiritsCached);
                            consumed -= c;
                            spiritsCached -= c;
                            if(spiritsCached <= 0)
                                spirit.setDead();
                            spirit.setSpiritValue(spiritsCached);
                        }
                        break;
                    }
                }
            }
        }
    }

    public int getTextureBySpirits() {
        if(getSpiritValue() >= 128)
            return 10;
        else if(getSpiritValue() >= 64)
            return 8;
        else if(getSpiritValue() >= 3)
            return 6;
        else if(getSpiritValue() >= 2)
            return 4;
        else if(getSpiritValue() >= 1)
            return 2;
        else
            return 0;
    }

    public static int getSpiritSplit(int value) {
        return (value >= 128 ? 128 : (value >= 64 ? 64 : (value >= 3 ? 3 : (value >= 2 ? 2 : 1))));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public int getBrightnessForRender() {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int)(f * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
    }

    protected void dealFireDamage(int amount) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) amount);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.world.isRemote || this.isDead) return false; //Forge: Fixes MC-53850
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            this.markVelocityChanged();
            this.orbHealth = (int)((float) this.orbHealth - amount);

            if (this.orbHealth <= 0)
            {
                this.setDead();
            }

            return false;
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Health", (short) this.orbHealth);
        compound.setShort("Age", (short) this.orbAge);
        compound.setShort("Value", (short) getSpiritValue());
    }

    public void readEntityFromNBT(NBTTagCompound compound) {
        this.orbHealth = compound.getShort("Health");
        this.orbAge = compound.getShort("Age");
        setSpiritValue(compound.getShort("Value"));
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }
}
