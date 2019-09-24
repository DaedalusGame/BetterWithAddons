package betterwithaddons.entity;

import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityLegendarium;
import com.google.common.base.Optional;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.items.IItemHandler;

public class EntityArtifactFrame extends EntityItemFrame implements IEntityAdditionalSpawnData {
    private static final String TAG_FACINGDIRECTION = "Facing";
    private static final DataParameter<Optional<BlockPos>> LINKPOS = EntityDataManager.createKey(EntityArtifactFrame.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Integer> SLOT = EntityDataManager.createKey(EntityArtifactFrame.class,DataSerializers.VARINT);

    public EntityArtifactFrame(World worldIn, BlockPos pos, EnumFacing facing) {
        super(worldIn, pos, facing);
    }

    public EntityArtifactFrame(World worldIn)
    {
        super(worldIn);
    }

    public TileEntityLegendarium getLegendarium() {
        BlockPos linkPos = getLinkPos();
        if(linkPos != null) {
            TileEntity tile = world.getTileEntity(linkPos);
            if (tile instanceof TileEntityLegendarium)
                return (TileEntityLegendarium) tile;
        }
        return null;
    }

    public BlockPos getLinkPos() {
        return this.getDataManager().get(LINKPOS).orNull();
    }

    public int getSlot() {
        return this.getDataManager().get(SLOT);
    }

    public TileEntityLegendarium.CanvasType getCanvasType() {
        TileEntityLegendarium legendarium = getLegendarium();
        if(legendarium != null)
            return legendarium.getCanvasType();
        else
            return TileEntityLegendarium.CanvasType.Normal;
    }

    public void link(TileEntityLegendarium tile, int slot) {
        setLinkPos(tile.getPos());
        setSlot(slot);
    }

    public void unlink() {
        setLinkPos(null);
        setSlot(0);
    }

    public void setSlot(int slot) {
        this.getDataManager().set(SLOT,slot);
    }

    public void setLinkPos(BlockPos pos) {
        this.getDataManager().set(LINKPOS, Optional.fromNullable(pos));
    }

    @Override
    public boolean getIsInvulnerable() {
        return isLinked() || super.getIsInvulnerable();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(LINKPOS, Optional.absent());
        this.getDataManager().register(SLOT, 0);
    }

    public ItemStack getArtifact() {
        TileEntityLegendarium legendarium = getLegendarium();
        if(legendarium != null) {
            IItemHandler inventory = legendarium.getInventory();
            if(inventory.getSlots() > getSlot()) {
                ItemStack stack = inventory.getStackInSlot(getSlot()).copy();
                //stack.setItemFrame(this);
                return stack;
            } else
                return ItemStack.EMPTY;
        } else
            return ItemStack.EMPTY;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(!world.isRemote) {
            ItemStack stack = getArtifact();
            ItemStack displayStack = getDisplayedItem();
            if (!ItemStack.areItemStacksEqual(stack, displayStack))
                setDisplayedItem(stack);
        }
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
                this.markVelocityChanged();
                this.onBroken(source.getImmediateSource());
            }

            return true;
        }
    }

    @Override
    public void dropItemOrSelf(Entity entityIn, boolean dropFrame) {
        if(!dropFrame) {
            super.dropItemOrSelf(entityIn, false);
            return;
        }

        if(getEntityWorld().getGameRules().getBoolean("doEntityDrops")) {
            dropFrame();
        }
    }

    protected void dropFrame() {
        entityDropItem(new ItemStack(ModItems.ARTIFACT_FRAME, 1), 0.0F);
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
        if(getLinkPos() != null) {
            compound.setInteger("LinkX", getLinkPos().getX());
            compound.setInteger("LinkY", getLinkPos().getY());
            compound.setInteger("LinkZ", getLinkPos().getZ());
        }
        compound.setInteger("Slot",getSlot());
        super.writeEntityToNBT(compound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(compound.getByte(TAG_FACINGDIRECTION)));
        if(compound.hasKey("LinkX")) {
            setLinkPos(new BlockPos(compound.getInteger("LinkX"),compound.getInteger("LinkY"),compound.getInteger("LinkZ")));
        }
        setSlot(compound.getInteger("Slot"));
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

    public boolean isLinked() {
        TileEntityLegendarium legendarium = getLegendarium();
        return legendarium != null;
    }
}
