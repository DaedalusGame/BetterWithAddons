package betterwithaddons.container;

import betterwithaddons.container.slots.SlotInfuserCrafting;
import betterwithaddons.crafting.manager.CraftingManagerInfuser;
import betterwithaddons.crafting.recipes.infuser.InfuserRecipe;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerInfuser extends Container {
    private World world;
    private TileEntityInfuser tileInfuser;
    private BlockPos pos;
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    public int requiredSpirit = 0;
    private boolean spiritMet;

    public ContainerInfuser(EntityPlayer player, World world, int x, int y, int z) {
        this.world = world;
        this.pos = new BlockPos(x, y, z);
        this.tileInfuser = (TileEntityInfuser) world.getTileEntity(pos);


        this.addSlotToContainer(new SlotInfuserCrafting(player, this, this.craftResult, 0, 124, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 3; ++j)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileInfuser != null && pos.distanceSq(playerIn.getPosition()) < 64 && world.getTileEntity(pos) == tileInfuser;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!tileInfuser.getWorld().isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);

                if (!itemstack.isEmpty())
                {
                    playerIn.dropItem(itemstack, false);
                }
            }
        }
    }

    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        InfuserRecipe recipe = CraftingManagerInfuser.getInstance().findMatchingRecipe(this.craftMatrix, this.tileInfuser.getWorld());

        int required = recipe != null ? recipe.getRequiredSpirit(craftMatrix) : 0;
        spiritMet = recipe != null && tileInfuser.getSpirits() >= required;
        if(canCraft()) {
            requiredSpirit = required;
            this.craftResult.setInventorySlotContents(0, recipe.internal.getCraftingResult(this.craftMatrix));
        }
        else {
            requiredSpirit = 0;
            this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
        }
    }

    public boolean canCraft() {
        return spiritMet && tileInfuser.isValid();
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                itemstack1.getItem().onCreated(itemstack1, this.tileInfuser.getWorld(), playerIn);

                    if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
                        return ItemStack.EMPTY;
                    }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 10 && index < 37)
            {
                if (!this.mergeItemStack(itemstack1, 37, 46, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 37 && index < 46)
            {
                if (!this.mergeItemStack(itemstack1, 10, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 10, 46, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);

            if (index == 0)
            {
                playerIn.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

    public void onCrafting(ItemStack stack) {
        InfuserRecipe recipe = CraftingManagerInfuser.getInstance().findMatchingRecipe(craftMatrix,world);

        tileInfuser.consumeSpirits(recipe.getRequiredSpirit(craftMatrix));
    }
}
