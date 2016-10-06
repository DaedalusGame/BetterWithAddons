package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockBase;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Christian on 19.09.2016.
 */
public class BlockKera extends BlockBase {
    public BlockKera() {
        super("kera", Material.ROCK);

        this.setHardness(2.1F).setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList ret = new ArrayList();
        Random rand = world instanceof World ?((World)world).rand:new Random();

        int hochodrop = rand.nextInt(2);
        int tamadrop = rand.nextInt(3);
        int irondrop = rand.nextInt(2);

        if(hochodrop == 0 && tamadrop == 0) tamadrop = hochodrop = 1;
        if(hochodrop > 0) ret.add(ModItems.japanMaterial.getMaterial("hocho_tetsu",hochodrop));
        if(tamadrop > 0) ret.add(ModItems.japanMaterial.getMaterial("tamahagane",tamadrop));
        if(irondrop > 0) ret.add(new ItemStack(Items.IRON_INGOT,irondrop));

        return ret;
    }
}
