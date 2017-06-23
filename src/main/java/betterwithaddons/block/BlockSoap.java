package betterwithaddons.block;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockSoap extends BlockBase {
    public BlockSoap() {
        super("wet_soap", Material.GROUND);
        this.setSoundType(SoundType.SLIME);
        this.slipperiness = 1.0F;
        this.setHardness(1.0F);
        this.setResistance(5.0F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BWMBlocks.AESTHETIC);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return BlockAesthetic.EnumType.SOAP.getMeta();
    }
}
