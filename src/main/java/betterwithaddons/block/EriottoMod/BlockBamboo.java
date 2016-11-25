package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockBamboo extends BlockReed implements IHasVariants {
    public BlockBamboo()
    {
        super();

        this.setUnlocalizedName("bamboo");
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "bamboo"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.getBlockState(pos.down()).getBlock() == this || this.checkForDrop(worldIn, pos, state))
        {
            if (worldIn.isAirBlock(pos.up()))
            {
                int i;

                for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i);

                if (i < 5)
                {
                    int j = ((Integer)state.getValue(AGE)).intValue();

                    if (j == 15)
                    {
                        worldIn.setBlockState(pos.up(), this.getDefaultState());
                        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
                    }
                }
            }
        }

        if(isProperSoil(worldIn,pos.down()))
        {
            spread(worldIn,pos,rand);
        }
    }

    public void spread(World worldIn, BlockPos pos, Random rand)
    {
        int x = rand.nextInt(3)-1;
        int z = rand.nextInt(3)-1;
        BlockPos checkpos = pos.add(x,0,z);

        if(worldIn.isAirBlock(checkpos)) {
            if(isProperSoil(worldIn,checkpos.down()))
                worldIn.setBlockState(checkpos,this.getDefaultState());
            else if(worldIn.isAirBlock(checkpos.down()) && isProperSoil(worldIn,checkpos.down(2)))
                worldIn.setBlockState(checkpos.down(),this.getDefaultState());
        }
        else if(worldIn.isAirBlock(checkpos.up()) && isProperSoil(worldIn,checkpos))
        {
            worldIn.setBlockState(checkpos.up(),this.getDefaultState());
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState state = worldIn.getBlockState(pos.down());
        Block block = state.getBlock();
        if (block == this)
        {
            return true;
        }

        return isProperSoil(worldIn,pos.down());
    }

    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    public boolean isProperSoil(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.canSustainPlant(state, world, pos.down(), EnumFacing.UP, this)) return true;
        else if(block != Blocks.GRASS && block != Blocks.DIRT) return false;
        return true;
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(Reference.MOD_ID+":bamboo", "inventory"));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
