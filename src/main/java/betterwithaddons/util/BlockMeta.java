package betterwithaddons.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.oredict.OreDictionary;

public class BlockMeta {
    private Block block;
    private int meta;

    public Block getBlock() {
        return block;
    }

    public int getMeta() {
        return meta;
    }

    public BlockMeta(Block block, int meta)
    {
        this.block = block;
        this.meta = meta;
    }

    public BlockMeta(Block block)
    {
        this(block,OreDictionary.WILDCARD_VALUE);
    }

    public boolean matches(Block block, int meta)
    {
        return block.equals(this.block) && (meta == this.meta || this.meta == OreDictionary.WILDCARD_VALUE);
    }

    public boolean matches(IBlockState state)
    {
        Block block = state.getBlock();
        int meta = block.damageDropped(state);

        return matches(block,meta);
    }
}
