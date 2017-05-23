package betterwithaddons.block;

import net.minecraft.block.material.Material;

public class BlockSoap extends BlockBase {
    public BlockSoap() {
        super("wetsoap", Material.GROUND);
        this.slipperiness = 0.98F;
    }


}
