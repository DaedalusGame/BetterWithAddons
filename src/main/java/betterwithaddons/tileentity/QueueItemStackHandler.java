package betterwithaddons.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class QueueItemStackHandler extends ItemStackHandler {
    private final TileEntity tile;

    public QueueItemStackHandler(TileEntity inv) {
        super(NonNullList.create());
        this.tile = inv;
    }

    @Override
    public void setSize(int size) {
        NonNullList<ItemStack> oldstacks = stacks;
        stacks = NonNullList.create();
        for(int i = 0; i < size; i++)
        {
            if(i < oldstacks.size())
                stacks.add(oldstacks.get(i));
            else
                stacks.add(ItemStack.EMPTY);
        }
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        int limit = getStackLimit(slot, stack);

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate)
        {
            this.stacks.add(reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            onContentsChanged(stacks.size()-1);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0 || stacks.size() == 0)
            return ItemStack.EMPTY;

        ItemStack existing = stacks.get(0);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract)
        {
            if (!simulate)
            {
                this.stacks.remove(0);
                onContentsChanged(0);
            }
            return existing;
        }
        else
        {
            if (!simulate)
            {
                this.stacks.set(0, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                onContentsChanged(0);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    protected void onContentsChanged(int slot) {
        tile.markDirty();
    }
}
