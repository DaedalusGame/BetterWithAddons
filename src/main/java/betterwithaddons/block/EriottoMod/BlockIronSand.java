package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockIronSand extends BlockBase {
    public BlockIronSand() {
        super("iron_sand", Material.SAND);

        this.setHardness(0.7F).setResistance(5.0F);
        this.setSoundType(SoundType.SAND);
        this.setHarvestLevel("shovel", 0);
    }
}
