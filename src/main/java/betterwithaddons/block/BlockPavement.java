package betterwithaddons.block;

import net.minecraft.block.material.Material;

public class BlockPavement extends BlockBase {
    protected BlockPavement() {
        super("pavement", Material.ROCK);

        setHardness(3.0f);
    }
}
