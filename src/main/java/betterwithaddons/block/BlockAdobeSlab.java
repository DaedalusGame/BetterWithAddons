package betterwithaddons.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.ItemStack;

public class BlockAdobeSlab extends BlockSlab {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 4);
    public static final PropertyBool DRY_STATE = PropertyBool.create("dry");

    public BlockAdobeSlab(Material materialIn) {
        super(materialIn);
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return null;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return null;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return null;
    }
}
