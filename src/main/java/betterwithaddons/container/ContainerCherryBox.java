package betterwithaddons.container;

import betterwithaddons.tileentity.TileEntityCherryBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Iterator;

public class ContainerCherryBox extends Container {
    private BlockPos pos;
    private final TileEntityCherryBox tileCherryBox;
    private int workTime;
    private int totalWorkTime;

    public ContainerCherryBox(EntityPlayer player, World world, int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        this.tileCherryBox = (TileEntityCherryBox) world.getTileEntity(pos);

        addSlotToContainer(new SlotItemHandler(tileCherryBox.inventory, 0, 56, 36));
        addSlotToContainer(new SlotItemHandler(tileCherryBox.inventory, 1, 116, 35));

        bindPlayerInventory(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null));
    }

    protected void bindPlayerInventory(IItemHandler inventoryPlayer) {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                addSlotToContainer(new SlotItemHandler(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotItemHandler(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        Iterator<IContainerListener> it = this.listeners.iterator();
        while(it.hasNext())
        {
            IContainerListener craft = it.next();

            if (this.workTime != tileCherryBox.workTime) {
                craft.sendProgressBarUpdate(this, 0, tileCherryBox.workTime);
            }

            if (this.totalWorkTime != tileCherryBox.totalWorkTime) {
                craft.sendProgressBarUpdate(this, 1, tileCherryBox.totalWorkTime);
            }
        }

        this.workTime = tileCherryBox.workTime;
        this.totalWorkTime = tileCherryBox.totalWorkTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int fieldId, int fieldValue) {
        switch(fieldId)
        {
            case 0: tileCherryBox.workTime = fieldValue; break;
            case 1: tileCherryBox.totalWorkTime = fieldValue; break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack stack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index < 3)
            {
                if(!mergeItemStack(stack1, 3, this.inventorySlots.size(), true))
                    return null;
            }
            else if(!mergeItemStack(stack1, 0, 3, false))
                return null;
            if(stack1.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();
        }
        return stack;
    }
}
