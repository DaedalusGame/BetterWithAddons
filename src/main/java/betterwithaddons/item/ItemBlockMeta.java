package betterwithaddons.item;

import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMeta extends ItemBlock
{
    IHasVariants variantBlock;

    public ItemBlockMeta(Block block)
    {
        super(block);
        if(block instanceof IHasVariants)
            variantBlock = (IHasVariants)block;
        this.setMaxDamage(0).setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String variantName = (variantBlock != null && variantBlock.getVariantName(stack.getItemDamage()) != null) ? "."+variantBlock.getVariantName(stack.getItemDamage()) : "";
        return super.getUnlocalizedName() + variantName;
    }
}