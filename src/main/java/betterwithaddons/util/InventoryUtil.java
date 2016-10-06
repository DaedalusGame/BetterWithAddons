package betterwithaddons.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;

public class InventoryUtil
{
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
            if (itemInSlot != null && itemInSlot.getItem() == i)
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

            if (is != null)
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
                    inventory.setInventorySlotContents(i, null);
                }
                else
                {
                    inventory.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(stackNBT));
                }
            }
        }
    }

    public static ItemStack getPlayerInventoryItem(Item item, EntityPlayer player)
    {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack is = player.inventory.getStackInSlot(i);
            if (is != null && is.getItem() == item)
            {
                return is;
            }
        }
        return null;
    }

    public static boolean isInventoryEmpty(IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (inventory.getStackInSlot(i) != null)
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