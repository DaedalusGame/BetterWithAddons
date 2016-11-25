package betterwithaddons.container;

import betterwithaddons.tileentity.TileEntityTatara;
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

public class ContainerTatara extends Container {
    private BlockPos pos;
    private final TileEntityTatara tileTatara;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerTatara(EntityPlayer player, World world, int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        this.tileTatara = (TileEntityTatara) world.getTileEntity(pos);

        addSlotToContainer(new SlotItemHandler(tileTatara.inventory, 0, 56, 17));
        addSlotToContainer(new SlotItemHandler(tileTatara.inventory, 1, 56, 53));
        addSlotToContainer(new SlotItemHandler(tileTatara.inventory, 2, 116, 35));

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
            if (this.furnaceBurnTime != tileTatara.furnaceBurnTime) {
                craft.sendProgressBarUpdate(this, 0, tileTatara.furnaceBurnTime);
            }

            if (this.currentItemBurnTime != tileTatara.currentItemBurnTime) {
                craft.sendProgressBarUpdate(this, 1, tileTatara.currentItemBurnTime);
            }

            if (this.cookTime != tileTatara.cookTime) {
                craft.sendProgressBarUpdate(this, 2, tileTatara.cookTime);
            }

            if (this.totalCookTime != tileTatara.totalCookTime) {
                craft.sendProgressBarUpdate(this, 3, tileTatara.totalCookTime);
            }
        }
        this.furnaceBurnTime = tileTatara.furnaceBurnTime;
        this.currentItemBurnTime = tileTatara.currentItemBurnTime;
        this.cookTime = tileTatara.cookTime;
        this.totalCookTime = tileTatara.totalCookTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int fieldId, int fieldValue) {
        switch(fieldId)
        {
            case 0: tileTatara.furnaceBurnTime = fieldValue; break;
            case 1: tileTatara.currentItemBurnTime = fieldValue; break;
            case 2: tileTatara.cookTime = fieldValue; break;
            case 3: tileTatara.totalCookTime = fieldValue; break;
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
