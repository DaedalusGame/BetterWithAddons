package betterwithaddons.util;

import net.minecraft.item.ItemStack;

public class StackResult {
    ItemStack originalStack;
    ItemStack returnStack;
    boolean success;

    public StackResult(boolean success, ItemStack originalStack, ItemStack returnStack) {
        this.originalStack = originalStack;
        this.returnStack = returnStack;
        this.success = success;
        if(originalStack.isEmpty()) {
            this.originalStack = returnStack;
            this.returnStack = ItemStack.EMPTY;
        }
    }

    public StackResult(boolean success, ItemStack originalStack) {
        this(success,originalStack,ItemStack.EMPTY);
    }

    public ItemStack getOriginalStack() {
        return originalStack;
    }

    public void setOriginalStack(ItemStack originalStack) {
        this.originalStack = originalStack;
    }

    public ItemStack getReturnStack() {
        return returnStack;
    }

    public void setReturnStack(ItemStack returnStack) {
        this.returnStack = returnStack;
    }


    public boolean isSuccess() {
        return success;
    }
}
