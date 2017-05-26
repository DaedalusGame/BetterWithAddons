package betterwithaddons.tileentity;

import betterwithaddons.block.BlockChute;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityChute extends TileEntityBase implements ITickable {
    public SimpleItemStackHandler inventory = createItemStackHandler();


    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 18);
    }

    boolean[] blockedOutputs = new boolean[4];
    public byte power;
    int ejectCounter = 0;
    EnumFacing ejectDir = EnumFacing.NORTH;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (facing == EnumFacing.UP && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (facing == EnumFacing.UP && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) inventory;
        return super.getCapability(capability, facing);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.merge(inventory.serializeNBT());
        compound.setInteger("EjectDirection", this.ejectDir.getHorizontalIndex());
        compound.setInteger("EjectCounter", this.ejectCounter);
        compound.setBoolean("IsPowered", power > 1);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        inventory = createItemStackHandler();
        inventory.deserializeNBT(compound);
        if (compound.hasKey("EjectDirection"))
            this.ejectDir = EnumFacing.getHorizontal(compound.getInteger("EjectDirection"));
        if (compound.hasKey("EjectCounter"))
            this.ejectCounter = compound.getInteger("EjectCounter");
        if (compound.hasKey("IsPowered"))
            this.power = compound.getBoolean("IsPowered") ? (byte) 1 : 0;
    }

    public static boolean putDropInInventoryAllSlots(IItemHandler inv, EntityItem entityItem) {
        boolean putAll = false;
        if (entityItem == null) {
            return false;
        } else {
            ItemStack itemstack = entityItem.getEntityItem().copy();
            ItemStack leftovers = attemptToInsert(inv, itemstack);
            if (!leftovers.isEmpty() && leftovers.getCount() != 0) {
                entityItem.setEntityItemStack(leftovers);
            } else {
                putAll = true;
                entityItem.setDead();
            }
            return putAll;
        }
    }

    public static ItemStack attemptToInsert(IItemHandler inv, ItemStack stack) {
        ItemStack leftover = ItemStack.EMPTY;
        for (int slot = 0; slot < inv.getSlots() - 1; slot++) {
            leftover = inv.insertItem(slot, stack, false);
            if (leftover.isEmpty())
                break;
        }
        return leftover;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update() {
        if (world == null || world.isRemote)
            return;

        IBlockState state = world.getBlockState(pos);

        if (!(state.getBlock() instanceof BlockChute))
            return;

        entityCollision();
        boolean isOn = false;
        if (state.getBlock() instanceof BlockChute) {
            isOn = ((BlockChute) state.getBlock()).isMechanicalOn(world, pos);
        }
        power = (byte) (isOn ? 1 : 0);

        if (!areAllOutputsBlocked()) {
            this.ejectCounter += 1;
            if (this.ejectCounter > 2) {
                attemptToEjectStackFromInv(isOn);
                this.ejectCounter = 0;
            }
        } else
            this.ejectCounter = 0;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.getWorld() != null) {
            //resetOutputBlocked();
        }
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return player.getDistanceSq(x + 0.5D, y + 0.5D, z + 0.5D) <= 64.0D;
    }

    public List<EntityItem> getCaptureItems(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    private void rotateToNextOutput(boolean powered, Random rand) {
        ArrayList<EnumFacing> validfacings = null;
        if(!powered)
            validfacings = new ArrayList<>(4);
        for (int i = 0; i < blockedOutputs.length; i++) {
            ejectDir = ejectDir.rotateAround(EnumFacing.Axis.Y);
            if (!getOutputBlocked(ejectDir)) {
                if(powered)
                    return;
                else
                    validfacings.add(ejectDir);
            }
        }
        if(!powered && validfacings.size() > 0)
            ejectDir = validfacings.get(rand.nextInt(validfacings.size()));
    }

    private boolean isFull() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    private void entityCollision() {
        boolean flag = false;
        if (!isFull()) {
            flag = captureDroppedItems();
        }
        if (flag) {
            this.markDirty();
        }
    }

    private boolean captureDroppedItems() {
        List<EntityItem> items = this.getCaptureItems(getWorld(), getPos());
        if (items.size() > 0) {
            boolean flag = false;
            for (EntityItem item : items) {
                flag = putDropInInventoryAllSlots(inventory, item) || flag;
                if (flag)
                    this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

            }
            if (flag) {
                this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return true;
            }
        }
        return false;
    }

    private void attemptToEjectStackFromInv(boolean powered) {
        int stackIndex = InventoryUtil.getFirstOccupiedStackInRange(inventory, 0, 17);

        if (stackIndex > -1 && stackIndex < 18) {
            ItemStack invStack = inventory.getStackInSlot(stackIndex);
            int ejectStackSize = 1;

            ItemStack ejectStack = new ItemStack(invStack.getItem(), ejectStackSize, invStack.getItemDamage());

            InventoryUtil.copyTags(ejectStack, invStack);

            BlockPos ejectpos = pos.offset(ejectDir);

            boolean ejectIntoWorld = false;

            if (!getOutputBlocked(ejectDir))
                if (this.getWorld().isAirBlock(ejectpos))
                    ejectIntoWorld = true;
                else if (this.getWorld().getBlockState(ejectpos).getBlock().isReplaceable(this.getWorld(), ejectpos))
                    ejectIntoWorld = true;
                else {
                    Block block = this.getWorld().getBlockState(ejectpos).getBlock();

                    if (block == null || (!block.isBlockSolid(this.getWorld(), ejectpos, ejectDir.getOpposite()) && (this.getWorld().getTileEntity(ejectpos) == null || !(this.getWorld().getTileEntity(ejectpos).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ejectDir.getOpposite())))))
                        ejectIntoWorld = true;
                    else if (powered) {
                        TileEntity tile = this.getWorld().getTileEntity(ejectpos);
                        if (tile != null && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ejectDir.getOpposite())) {
                            IItemHandler below = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ejectDir.getOpposite());
                            ItemStack leftover;
                            for (int slot = 0; slot < below.getSlots(); slot++) {
                                leftover = below.insertItem(slot, ejectStack, false);
                                if (leftover.isEmpty()) {
                                    inventory.extractItem(stackIndex, ejectStackSize, false);
                                    break;
                                }
                            }
                        } else
                            setOutputBlocked(ejectDir, true);
                    } else {
                        setOutputBlocked(ejectDir, true);
                    }

                }

            if (ejectIntoWorld) {
                ejectStack(ejectStack, ejectDir);
                inventory.extractItem(stackIndex, ejectStackSize, false);
            }

            rotateToNextOutput(powered,world.rand);
        }
    }

    private void ejectStack(ItemStack stack, EnumFacing facing) {
        float xEject = facing.getFrontOffsetX();
        float zEject = facing.getFrontOffsetZ();

        float xOff = this.getWorld().rand.nextFloat() * 0.05F + 0.475F + xEject * 0.7F;
        float yOff = 0.1F;
        float zOff = this.getWorld().rand.nextFloat() * 0.05F + 0.475F + zEject * 0.7F;

        EntityItem item = new EntityItem(this.getWorld(), pos.getX() + xOff, pos.getY() + yOff, pos.getZ() + zOff, stack);

        item.motionX = xEject * 0.1f;
        item.motionY = 0.0D;
        item.motionZ = zEject * 0.1f;
        item.setDefaultPickupDelay();
        this.getWorld().spawnEntity(item);
    }

    public void resetOutputBlocked() {
        for (int i = 0; i < blockedOutputs.length; i++)
            blockedOutputs[i] = false;
    }

    public void setOutputBlocked(EnumFacing facing, boolean blocked) {
        if (facing.getHorizontalIndex() != -1)
            blockedOutputs[facing.getHorizontalIndex()] = blocked;
    }

    public boolean getOutputBlocked(EnumFacing facing) {
        if (facing.getHorizontalIndex() != -1)
            return blockedOutputs[facing.getHorizontalIndex()];

        return true;
    }

    public boolean areAllOutputsBlocked() {
        for (boolean blocked : blockedOutputs) {
            if (!blocked) return false;
        }

        return true;
    }
}
