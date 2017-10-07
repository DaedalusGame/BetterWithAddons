package betterwithaddons.util;

import net.minecraft.util.WeightedRandom;

public class SimpleWeightItem<T> extends WeightedRandom.Item {
    T item;

    public SimpleWeightItem(T item,int itemWeightIn) {
        super(itemWeightIn);
        this.item = item;
    }

    public T getItem()
    {
        return item;
    }
}
