package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockBase;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 19.09.2016.
 */
public class BlockIronSand extends BlockBase {
    public BlockIronSand() {
        super("iron_sand", Material.SAND);

        this.setHardness(0.7F).setResistance(5.0F);
        this.setSoundType(SoundType.SAND);
        this.setHarvestLevel("shovel", 0);
    }
}
