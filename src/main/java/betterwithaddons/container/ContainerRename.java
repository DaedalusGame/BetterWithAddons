package betterwithaddons.container;

import betterwithaddons.block.BlockWritingTable;
import betterwithaddons.interaction.InteractionBTWTweak;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IngredientSpecial;
import betterwithaddons.util.ItemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.StringUtils;

public class ContainerRename extends Container {
    private World world;
    private BlockPos pos;
    private IInventory outputSlot;
    private IInventory inputSlots;
    private String repairedItemName;
    public int cost;

    private Ingredient paperMatch;
    private Ingredient inkMatch;

    public ContainerRename(EntityPlayer player, World world, int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
        this.world = world;
        this.outputSlot = new InventoryCraftResult();
        this.inputSlots = new InventoryBasic("Rename", true, 3)
        {
            public void markDirty()
            {
                super.markDirty();
                ContainerRename.this.onCraftMatrixChanged(this);
            }
        };

        addSlotToContainer(new Slot(inputSlots,0,38,46));
        addSlotToContainer(new Slot(inputSlots,1,60,37));
        addSlotToContainer(new Slot(inputSlots,2,60,55));
        addSlotToContainer(new Slot(outputSlot,3,118,46){
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }

            @Override
            public boolean canTakeStack(EntityPlayer playerIn)
            {
                return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= cost) && this.getHasStack();
            }

            @Override
            public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
            {
                if (!thePlayer.capabilities.isCreativeMode)
                {
                    thePlayer.addExperienceLevel(-cost);
                }

                ItemStack quill = ContainerRename.this.inputSlots.getStackInSlot(1);
                quill.damageItem(1,thePlayer);
                ContainerRename.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
                ContainerRename.this.inputSlots.setInventorySlotContents(1, quill);
                ContainerRename.this.inputSlots.decrStackSize(2, 1);

                return stack;
            }
        });

        bindPlayerInventory(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,null));

        paperMatch = new IngredientSpecial(stack -> stack.getItem() instanceof ItemNameTag || ItemUtil.matchesOreDict(stack,"paper"));
        inkMatch = Ingredient.fromItem(ModItems.inkAndQuill);
    }

    protected void bindPlayerInventory(IItemHandler inventoryPlayer) {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                addSlotToContainer(new SlotItemHandler(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            addSlotToContainer(new SlotItemHandler(inventoryPlayer, i, 8 + i * 18, 144));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
        if (this.world.getBlockState(pos).getBlock() instanceof BlockWritingTable) {
            return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) <= 64.0D;
        } else {
            return false;
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        super.onCraftMatrixChanged(inventoryIn);

        if (inventoryIn == this.inputSlots)
        {
            this.updateRepairOutput();
        }
    }

    public void updateRepairOutput()
    {
        ItemStack inputStack = this.inputSlots.getStackInSlot(0);
        ItemStack inkStack = this.inputSlots.getStackInSlot(1);
        ItemStack paperStack = this.inputSlots.getStackInSlot(2);

        if (!paperMatch.apply(paperStack) || !inkMatch.apply(inkStack) || inputStack.getItem() instanceof ItemNameTag)
        {
            this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
            cost = 0;
        }
        else
        {
            ItemStack outputStack = inputStack.copy();

            cost = InteractionBTWTweak.WRITING_TABLE_COST;

            boolean blank = StringUtils.isBlank(this.repairedItemName);
            if (inputStack.isEmpty() && !blank)
            {
                if(paperStack.getItem() instanceof ItemNameTag)
                    outputStack = paperStack.copy();
                else
                    outputStack = new ItemStack(Items.NAME_TAG);
                outputStack.setStackDisplayName(this.repairedItemName);
            }
            else if (blank && inputStack.hasDisplayName())
            {
                outputStack.clearCustomName();
            }
            else if (!blank && !this.repairedItemName.equals(inputStack.getDisplayName()))
            {
                outputStack.setStackDisplayName(this.repairedItemName);
            }
            else
            {
                outputStack = ItemStack.EMPTY;
                cost = 0;
            }

            this.outputSlot.setInventorySlotContents(0, outputStack);
        }
        this.detectAndSendChanges();
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote)
        {
            this.clearContainer(playerIn, this.world, this.inputSlots);
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 3)
            {
                if (!this.mergeItemStack(itemstack1, 4, 40, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index < 0 || index > 3)
            {
                if (index >= 4 && index < 40 && !this.mergeItemStack(itemstack1, 0, 3, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 4, 40, false))
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

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public void updateItemName(String newName)
    {
        this.repairedItemName = newName;

        if (this.getSlot(3).getHasStack())
        {
            ItemStack itemstack = this.getSlot(3).getStack();

            if (StringUtils.isBlank(newName))
            {
                itemstack.clearCustomName();
            }
            else
            {
                itemstack.setStackDisplayName(this.repairedItemName);
            }
        }

        this.updateRepairOutput();
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.cost);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == 0)
        {
            this.cost = data;
        }
    }
}
