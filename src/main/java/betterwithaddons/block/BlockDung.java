package betterwithaddons.block;

import betterwithaddons.interaction.InteractionBWR;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDung extends BlockBase {
    public BlockDung() {
        super("dung", Material.GROUND);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(1.0F);
        this.setResistance(0.5F);
        this.setTickRandomly(true);
    }
}
