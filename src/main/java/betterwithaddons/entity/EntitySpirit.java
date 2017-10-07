package betterwithaddons.entity;

import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;

public class EntitySpirit extends EntityXPOrb {
    private EntityPlayer closestPlayer;
    private float currentAngle;
    private int nextCheck;

    public EntitySpirit(World worldIn) {
        super(worldIn);
        this.currentAngle = (float)Math.toRadians(rand.nextInt(360));
    }

    public EntitySpirit(World worldIn, double x, double y, double z, int val) {
        super(worldIn,x,y,z,val);
        this.currentAngle = (float)Math.toRadians(rand.nextInt(360));
    }

    private void applyGravity()
    {
        RayTraceResult result = world.rayTraceBlocks(new Vec3d(posX,posY,posZ),new Vec3d(posX,posY - 1.5,posZ));

        if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
            this.motionY += 0.02D;
        else
            this.motionY -= 0.02D;
    }

    public void onUpdate() //Duplicate because why the hell not
    {
        //super.super.onUpdate violates encapsulations but this doesn't; thank you dark souls
        if (!this.world.isRemote)
        {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();

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
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > maxdist * maxdist)
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
        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
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
                    totalSpirit += spirit.xpValue;

                if (totalSpirit >= InteractionEriottoMod.SPIRIT_PER_BOTTLE)
                {
                    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                        ItemStack itemstack = player.inventory.getStackInSlot(i);
                        if (itemstack.getItem() != Items.GLASS_BOTTLE) continue;
                        itemstack.shrink(1);
                        player.inventory.setInventorySlotContents(i,itemstack);
                        InventoryUtil.addItemToPlayer(player,new ItemStack(ModItems.ancestryBottle));
                        int consumed = InteractionEriottoMod.SPIRIT_PER_BOTTLE;
                        for(EntitySpirit spirit : spirits)
                        {
                            int c = Math.min(consumed,spirit.xpValue);
                            consumed -= c;
                            spirit.xpValue -= c;
                            if(spirit.xpValue <= 0)
                                spirit.setDead();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getTextureByXP() {
        if(xpValue >= 128)
            return 10;
        else if(xpValue >= 64)
            return 8;
        else if(xpValue >= 3)
            return 6;
        else if(xpValue >= 2)
            return 4;
        else if(xpValue >= 1)
            return 2;
        else
            return 0;
    }

    public static int getSpiritSplit(int i) {
        return (i >= 128 ? 128 : (i >= 64 ? 64 : (i >= 3 ? 3 : (i >= 2 ? 2 : 1))));
    }
}
