package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockBase;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ModItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockKera extends BlockBase {
    public BlockKera() {
        super("kera", Material.ROCK);

        this.setHardness(2.1F).setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 0);
    }

    private static ItemStack getRandomMetal(Random random, boolean canEmpty) {
        double tamaThreshold = InteractionEriottoMod.KERA_TAMAHAGANE_CHANCE;
        double hochoThreshold = tamaThreshold + InteractionEriottoMod.KERA_HOCHOTETSU_CHANCE;
        double ironThreshold = hochoThreshold + InteractionEriottoMod.KERA_IRON_CHANCE;
        double totalWeight = InteractionEriottoMod.KERA_TAMAHAGANE_CHANCE + InteractionEriottoMod.KERA_HOCHOTETSU_CHANCE + InteractionEriottoMod.KERA_IRON_CHANCE;
        if(totalWeight < 1.0 && canEmpty)
            totalWeight = 1.0;
        double pick = random.nextDouble() * totalWeight;
        if(pick < tamaThreshold)
            return ModItems.MATERIAL_JAPAN.getMaterial("tamahagane");
        else if(pick < hochoThreshold)
            return ModItems.MATERIAL_JAPAN.getMaterial("hocho_tetsu");
        else if(pick < ironThreshold)
            return new ItemStack(Items.IRON_INGOT);
        else
            return ItemStack.EMPTY;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        Random rand = world instanceof World ?((World)world).rand:new Random();

        ret.add(getRandomMetal(rand,false));
        for(int i = 1; i < InteractionEriottoMod.IRON_PER_IRONSAND; i++)
            ret.add(getRandomMetal(rand,true));

        return ret;
    }
}
