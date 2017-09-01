package betterwithaddons.container;

import betterwithaddons.tileentity.TileEntityChute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerChute extends Container {
    private BlockPos pos;
    private final TileEntityChute tileChute;
    private byte lastMechPower;

    public ContainerChute(EntityPlayer player, World world, int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        this.tileChute = (TileEntityChute) world.getTileEntity(pos);
        this.lastMechPower = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(tileChute.inventory, j + i * 9, 8 + j * 18, 60 + i * 18));
            }
        }

        bindPlayerInventory(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null));
    }

    protected void bindPlayerInventory(IItemHandler inventoryPlayer) {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                addSlotToContainer(new SlotItemHandler(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotItemHandler(inventoryPlayer, i, 8 + i * 18, 169));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        return tileChute.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        int slots = this.inventorySlots.size();

        if(slot != null && slot.getHasStack())
        {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if(index < slots)
            {
                if(!mergeItemStack(stack1, slots, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if(!mergeItemStack(stack1, 0, slots, false))
                return ItemStack.EMPTY;
            if(stack1.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return stack;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.tileChute.power);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener craft : this.listeners) {
            if (this.lastMechPower != this.tileChute.power)
                craft.sendWindowProperty(this, 0, this.tileChute.power);
        }
        this.lastMechPower = this.tileChute.power;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0)
            this.tileChute.power = (byte) value;
    }
}
