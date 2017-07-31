package betterwithaddons.entity;

import betterwithaddons.item.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityArtifactFrame extends EntityItemFrame implements IEntityAdditionalSpawnData {
    private static final String TAG_FACINGDIRECTION = "Facing";


    public EntityArtifactFrame(World worldIn, BlockPos pos, EnumFacing facing) {
        super(worldIn, pos, facing);
    }

    public EntityArtifactFrame(World worldIn)
    {
        super(worldIn);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (!this.getDisplayedItem().isEmpty())
        {
            if (!this.world.isRemote && !source.isExplosion())
            {
                this.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.2F, 1.5F);
            }

            return false;
        }
        else
        {
            if (!this.isDead && !this.world.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                this.onBroken(source.getImmediateSource());
            }

            return true;
        }
    }

    @Override
    public void dropItemOrSelf(Entity entityIn, boolean p_146065_2_) {
        if(!p_146065_2_) {
            super.dropItemOrSelf(entityIn, p_146065_2_);
            return;
        }

        if(getEntityWorld().getGameRules().getBoolean("doEntityDrops")) {
            dropFrame();
        }
    }

    protected void dropFrame() {
        entityDropItem(new ItemStack(ModItems.artifactFrame, 1), 0.0F);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        return true;
    }

    @Override
    public int getAnalogOutput() {
        return this.getDisplayedItem().isEmpty() ? 0 : 15;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setByte(TAG_FACINGDIRECTION, (byte)this.facingDirection.getHorizontalIndex());
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(compound.getByte(TAG_FACINGDIRECTION)));
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeShort(facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        updateFacingWithBoundingBox(EnumFacing.getHorizontal(additionalData.readShort()));
    }
}
