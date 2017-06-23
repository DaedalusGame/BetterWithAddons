package betterwithaddons.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class InventoryUtil
{
    public static void copyTags(ItemStack destStack, ItemStack sourceStack) {
        if (sourceStack.hasTagCompound()) {
            destStack.setTagCompound(sourceStack.getTagCompound().copy());
        }

    }

    public static int getFirstOccupiedStackInRange(IItemHandler inv, int minSlot, int maxSlot) {
        for (int slot = minSlot; slot <= maxSlot; ++slot) {
            if (!inv.getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    public static void insert(IItemHandler inv, NonNullList<ItemStack> stacks, boolean simulate) {
        stacks.forEach(stack -> insert(inv, stack, 0, inv.getSlots(), simulate));
    }

    public static boolean insert(IItemHandler inv, ItemStack stack, boolean simulate) {
        return insert(inv, stack, 0, inv.getSlots(), simulate);
    }

    public static boolean insert(IItemHandler inv, ItemStack stack, int minSlot, int maxSlot, boolean simulate) {
        if (isFull(inv))
            return false;
        boolean insert = false;
        for (int slot = minSlot; slot < maxSlot; slot++) {
            if (inv.insertItem(slot, stack, simulate).isEmpty()) {
                insert = true;
                break;
            }
        }
        return insert;
    }

    public static boolean isFull(IItemHandler inv) {
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (stack.isEmpty() || stack.getCount() != stack.getMaxStackSize())
                return false;
        }
        return true;
    }

    public static boolean isEmpty(IItemHandler inv) {
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            ItemStack stack = inv.getStackInSlot(slot);
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    public static void shuffleInventory(IInventory inventory)
    {
        Random rgen = new Random();

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            int randomPosition = rgen.nextInt(inventory.getSizeInventory());
            ItemStack temp = inventory.getStackInSlot(i);
            inventory.setInventorySlotContents(i, inventory.getStackInSlot(randomPosition));
            inventory.setInventorySlotContents(randomPosition, temp);
        }
    }

    public static int getInventorySlotContainItem(IInventory inventory, Item i)
    {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
        {
            ItemStack itemInSlot = inventory.getStackInSlot(slot);
            if (!itemInSlot.isEmpty() && itemInSlot.getItem() == i)
            {
                return slot;
            }
        }
        return -1;
    }

    public static void writeInventoryToCompound(NBTTagCompound compound, IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack is = inventory.getStackInSlot(i);
            NBTTagCompound stackNBT = new NBTTagCompound();

            if (!is.isEmpty())
            {
                is.writeToNBT(stackNBT);
            }
            else
            {
                stackNBT.setBoolean("empty", true);
            }

            compound.setTag("slot" + i, stackNBT);
        }
    }

    public static void readInventoryFromCompound(NBTTagCompound compound, IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            NBTTagCompound stackNBT = compound.getCompoundTag("slot" + i);

            if (stackNBT != null)
            {
                if (stackNBT.hasKey("empty"))
                {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
                else
                {
                    inventory.setInventorySlotContents(i, new ItemStack(stackNBT));
                }
            }
        }
    }

    public static ItemStack getPlayerInventoryItem(Item item, EntityPlayer player)
    {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (!is.isEmpty() && is.getItem() == item)
            {
                return is;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean isInventoryEmpty(IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (!inventory.getStackInSlot(i).isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public static boolean listContains(ItemStack check, List<ItemStack> list) {
        if (list != null) {
            if (list.isEmpty()) return false;
            for (ItemStack item : list) {
                if (ItemStack.areItemsEqual(check, item))
                    return true;
            }
        }
        return false;
    }
}