package betterwithaddons.tileentity;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityCherryBox extends TileEntityBase implements ITickable {
    public int workTime;
    public int totalWorkTime;

    public SimpleItemStackHandler inventory = createItemStackHandler();

    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 2);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) inventory;
        return super.getCapability(capability, facing);
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.merge(inventory.serializeNBT());

        compound.setInteger("WorkTime", this.workTime);
        compound.setInteger("WorkTimeTotal", this.totalWorkTime);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        inventory = createItemStackHandler();
        inventory.deserializeNBT(compound);

        this.workTime = compound.getInteger("WorkTime");
        this.totalWorkTime = compound.getInteger("WorkTimeTotal");
    }

    public boolean isValidStructure()
    {
        return false;
    }

    public CraftingManagerCherryBox getManager()
    {
        return null;
    }

    public int getCookProgressScaled(int width) {
        return totalWorkTime != 0 && workTime != 0?workTime * width / totalWorkTime:0;
    }

    public int getWorkTime(@Nullable ItemStack stack) {
        return 1000;
    }

    private boolean canWork() {
        ItemStack inputstack = inventory.getStackInSlot(0);
        ItemStack outputstack = inventory.getStackInSlot(1);
        if(inputstack == null) {
            return false;
        } else {
            ItemStack itemstack = getManager().getWorkResult(inputstack);
            if(itemstack == null) {
                return false;
            } else if(outputstack == null) {
                return true;
            } else if(!outputstack.isItemEqual(itemstack)) {
                return false;
            } else {
                int result = outputstack.stackSize + itemstack.stackSize;
                return result <= this.getInventoryStackLimit() && result <= outputstack.getMaxStackSize();
            }
        }
    }

    public void finishItem() {
        ItemStack inputstack = inventory.getStackInSlot(0);
        ItemStack outputstack = inventory.getStackInSlot(1);

        if(this.canWork()) {
            ItemStack itemstack = getManager().getWorkResult(inputstack);
            if(outputstack == null) {
                inventory.setStackInSlot(1,itemstack.copy());
            } else if(outputstack.getItem() == itemstack.getItem()) {
                outputstack.stackSize += itemstack.stackSize;
            }

            --inputstack.stackSize;
            if(inputstack.stackSize <= 0) {
                inventory.setStackInSlot(0,null);
            }
        }

    }

    public boolean isWorking()
    {
        return isValidStructure();
    }

    private int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void update() {
        boolean flag1 = false;

        if(!this.worldObj.isRemote) {
            if(!isValidStructure())
                return;

            ItemStack inputstack = inventory.getStackInSlot(0);
            if(this.isWorking() || inputstack != null) {
                if(this.isWorking() && this.canWork()) {
                    ++this.workTime;
                    this.totalWorkTime = this.getWorkTime(inputstack);
                    if(this.workTime >= this.totalWorkTime) {
                        this.workTime = 0;
                        this.totalWorkTime = this.getWorkTime(inputstack);
                        this.finishItem();
                        flag1 = true;
                    }
                } else {
                    this.workTime = 0;
                }
            } else if(!this.isWorking() && this.workTime > 0) {
                this.workTime = MathHelper.clamp_int(this.workTime - 2, 0, this.totalWorkTime);
            }
        }

        if(flag1) {
            this.markDirty();
        }
    }
}
