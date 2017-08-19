package betterwithaddons.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMeta {
    private Item item;
    private int metadata;

    public ItemMeta(Item item, int metadata)
    {
        this.item = item;
        this.metadata = metadata;
    }

    public ItemMeta(ItemStack stack)
    {
        this(stack.getItem(),stack.getMetadata());
    }

    @Override
    public int hashCode() {
        return item.hashCode() ^ metadata;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemMeta)
        {
            return item == ((ItemMeta) obj).item && metadata == ((ItemMeta) obj).metadata;
        }

        return super.equals(obj);
    }
}
