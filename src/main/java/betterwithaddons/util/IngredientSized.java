package betterwithaddons.util;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;

public class IngredientSized extends Ingredient implements IHasSize {
    Ingredient internal;
    int size;
    ItemStack[] cachedStacks;

    public IngredientSized(Ingredient internal, int size) {
        super(0);
        this.internal = internal;
        this.size = size;
    }

    @Override
    public ItemStack[] getMatchingStacks() {
        if(cachedStacks == null) {
            ItemStack[] stacks = internal.getMatchingStacks();
            cachedStacks = new ItemStack[stacks.length];

            for (int i = 0; i < stacks.length; i++) {
                cachedStacks[i] = stacks[i].copy();
                cachedStacks[i].setCount(size);
            }
        }

        return cachedStacks;
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        return internal.apply(stack) && stack != null && stack.getCount() >= size;
    }

    @Override
    public IntList getValidItemStacksPacked() {
        return internal.getValidItemStacksPacked();
    }

    public int getSize() {
        return size;
    }

    public static Ingredient fromItem(Item item, int size)
    {
        return new IngredientSized(Ingredient.fromItem(item),size);
    }

    public static Ingredient fromBlock(Block block, int size)
    {
        return fromBlock(block, OreDictionary.WILDCARD_VALUE, size);
    }

    public static Ingredient fromBlock(Block block, int meta, int size)
    {
        return fromStacks(new ItemStack(block,size,meta));
    }

    public static Ingredient fromStacks(ItemStack... stacks)
    {
        if(stacks.length > 0)
            return new IngredientSized(Ingredient.fromStacks(stacks),stacks[0].getCount());
        else
            return Ingredient.EMPTY;
    }

    public static Ingredient fromOredict(String string, int size)
    {
        return new IngredientSized(new OreIngredient(string),size);
    }
}
