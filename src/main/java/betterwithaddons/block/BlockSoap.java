package betterwithaddons.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockSoap extends BlockBase {
    public BlockSoap() {
        super("wet_soap", Material.GROUND);
        this.setSoundType(SoundType.SLIME);
        this.slipperiness = 1.0F;
    }
}
