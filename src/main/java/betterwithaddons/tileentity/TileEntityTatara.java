package betterwithaddons.tileentity;

import betterwithaddons.crafting.manager.CraftingManagerTatara;
import betterwithaddons.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityTatara extends TileEntityBase implements ITickable {
    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int cookTime;
    public int totalCookTime;

    public SimpleItemStackHandler inventory = createItemStackHandler();

    public SimpleItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 3);
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

        compound.setInteger("BurnTime", this.furnaceBurnTime);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        inventory = createItemStackHandler();
        inventory.deserializeNBT(compound);

        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.inventory.getStackInSlot(1));
    }

    public boolean isValidStructure()
    {
        //DON'T ASK QUESTIONS YOU'RE NOT READY
        for (int z = -1; z <= 1; z++)
            for (int x = -1; x <= 1; x++)
            {
                IBlockState upperstate = worldObj.getBlockState(pos.add(x,+1,z));
                if(!isTopping(upperstate))
                    return false;
                if((z != x || x != 0) && !isBedding(worldObj.getBlockState(pos.add(x,-1,z))))
                    return false;
                if(x == z && x != 0 && !isStoneBrick(worldObj.getBlockState(pos.add(x,0,z))))
                    return false;
            }

        boolean hasSiding = isSiding(worldObj.getBlockState(pos.north())) && isSiding(worldObj.getBlockState(pos.south()));
        hasSiding ^= isSiding(worldObj.getBlockState(pos.east())) && isSiding(worldObj.getBlockState(pos.west()));
        return isHeatSource(worldObj.getBlockState(pos.down())) && hasSiding;
    }

    public boolean isSiding(IBlockState state)
    {
        return state.getBlock() == Blocks.IRON_BLOCK;
    }

    public boolean isBedding(IBlockState state)
    {
        return state.getBlock() == Blocks.CLAY;
    }

    public boolean isTopping(IBlockState state)
    {
        return state.getBlock() == Blocks.NETHER_BRICK;
    }

    public boolean isStoneBrick(IBlockState state)
    {
        return state.getBlock() == Blocks.STONEBRICK;
    }

    public boolean isHeatSource(IBlockState state)
    {
        return state.getMaterial() == Material.LAVA || state.getMaterial() == Material.FIRE;
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    public static int getItemBurnTime(ItemStack stack) {
        if(stack != null && stack.isItemEqual(ModItems.materialJapan.getMaterial("rice_ash"))) {
            return 1600;
        }

        return 0;
    }

    public static boolean isItemFuel(ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    public int getCookProgressScaled(int width) {
        return totalCookTime != 0 && cookTime != 0?cookTime * width / totalCookTime:0;
    }

    public int getBurnLeftScaled(int height) {
        return currentItemBurnTime != 0 ? furnaceBurnTime * height / currentItemBurnTime : 0;
    }

    public int getCookTime(@Nullable ItemStack stack) {
        return 1000;
    }

    private boolean canSmelt() {
        ItemStack inputstack = inventory.getStackInSlot(0);
        ItemStack outputstack = inventory.getStackInSlot(2);
        if(inputstack == null) {
            return false;
        } else {
            ItemStack itemstack = CraftingManagerTatara.instance().getSmeltingResult(inputstack);
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

    public void smeltItem() {
        ItemStack inputstack = inventory.getStackInSlot(0);
        ItemStack outputstack = inventory.getStackInSlot(2);

        if(this.canSmelt()) {
            ItemStack itemstack = CraftingManagerTatara.instance().getSmeltingResult(inputstack);
            if(outputstack == null) {
                inventory.setStackInSlot(2,itemstack.copy());
            } else if(outputstack.getItem() == itemstack.getItem()) {
                outputstack.stackSize += itemstack.stackSize;
            }

            --inputstack.stackSize;
            if(inputstack.stackSize <= 0) {
                inventory.setStackInSlot(0,null);
            }
        }

    }

    private int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void update() {
        boolean burning = this.isBurning();
        boolean flag1 = false;
        if(this.isBurning()) {
            --this.furnaceBurnTime;
        }

        if(!this.worldObj.isRemote) {
            if(!isValidStructure())
                return;

            ItemStack inputstack = inventory.getStackInSlot(0);
            ItemStack fuelstack = inventory.getStackInSlot(1);
            if(this.isBurning() || fuelstack != null && inputstack != null) {
                if(!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = getItemBurnTime(fuelstack);
                    this.currentItemBurnTime = this.furnaceBurnTime;
                    if(this.isBurning()) {
                        flag1 = true;
                        if(fuelstack != null) {
                            --fuelstack.stackSize;
                            if(fuelstack.stackSize == 0) {
                                inventory.setStackInSlot(1,fuelstack.getItem().getContainerItem(fuelstack));
                            }
                        }
                    }
                }

                if(this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;
                    this.totalCookTime = this.getCookTime(inputstack);
                    if(this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(inputstack);
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if(!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }

            if(burning != this.isBurning()) {
                flag1 = true;
            }
        }

        if(flag1) {
            this.markDirty();
        }
    }
}
