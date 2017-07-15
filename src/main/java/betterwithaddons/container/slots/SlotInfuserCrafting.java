package betterwithaddons.container.slots;

import betterwithaddons.container.ContainerInfuser;
import betterwithaddons.crafting.manager.CraftingManagerInfuser;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SlotInfuserCrafting extends SlotCrafting {
    private final InventoryCrafting craftMatrix;
    private final EntityPlayer player;
    private final ContainerInfuser container;

    public SlotInfuserCrafting(EntityPlayer player, ContainerInfuser container, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, container.craftMatrix, inventoryIn, slotIndex, xPosition, yPosition);
        this.container = container;
        this.player = player;
        this.craftMatrix = container.craftMatrix;
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        super.onCrafting(stack);

        container.onCrafting(stack);
    }

    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
    {
        if(!container.canCraft())
            return ItemStack.EMPTY;
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> remaining = CraftingManagerInfuser.getInstance().getRemainingItems(this.craftMatrix, thePlayer.world);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < remaining.size(); ++i)
        {
            ItemStack currentStack = this.craftMatrix.getStackInSlot(i);
            ItemStack remainStack = remaining.get(i);

            if (!currentStack.isEmpty())
            {
                this.craftMatrix.decrStackSize(i, 1);
                currentStack = this.craftMatrix.getStackInSlot(i);
            }

            if (!remainStack.isEmpty())
            {
                if (currentStack.isEmpty())
                {
                    this.craftMatrix.setInventorySlotContents(i, remainStack);
                }
                else if (ItemStack.areItemsEqual(currentStack, remainStack) && ItemStack.areItemStackTagsEqual(currentStack, remainStack))
                {
                    remainStack.grow(currentStack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, remainStack);
                }
                else if (!this.player.inventory.addItemStackToInventory(remainStack))
                {
                    this.player.dropItem(remainStack, false);
                }
            }
        }

        return stack;
    }
}
