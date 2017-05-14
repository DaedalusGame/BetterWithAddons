package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCropRush extends BlockCrops implements IPlantable, IHasVariants {
    public BlockCropRush()
    {
        super();

        this.setUnlocalizedName("crop_rush");
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "crop_rush"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn)
    {
        return MathHelper.getInt(worldIn.rand, 1, 2);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        checkAndDropBlock(world, pos, state);
        BlockPos up = pos.up();

        int meta = state.getValue(AGE);

        double growthChance = 15D;

        if(world.getBlockState(pos.down()).getBlock().isFertile(world, pos.down()))
            growthChance /= 1.33D;
        else if(world.getBlockState(pos.down()).getBlock().canSustainPlant(world.getBlockState(pos.down()), world, pos.down(), EnumFacing.UP, this))
            growthChance /= 1.2D;
        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            IBlockState check = world.getBlockState(pos.offset(facing));
            if(check.getBlock() instanceof BlockCrops)
                growthChance /= 1.1D;
        }

        if(!isMaxAge(state))
        {
            if(world.getLightFromNeighbors(up) > 12)
            {
                if(rand.nextInt(MathHelper.floor(growthChance)) == 0)
                    world.setBlockState(pos, state.withProperty(AGE, meta + 1));
            }
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, 0);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos);
    }

    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
    {
        if(!canBlockStay(world, pos, state))
        {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return isMaxAge(state) ? 5 : 0;
    }

    @Override
    protected Item getSeed()
    {
        return Item.getItemFromBlock(ModBlocks.rush);
    }

    @Override
    protected Item getCrop()
    {
        return ModItems.materialJapan;
    }

    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new ArrayList<ItemStack>();//super.getDrops(world, pos, state, fortune);
        int age = getAge(state);
        Random rand = world instanceof World ? ((World)world).rand : new Random();

        ret.add(new ItemStack(this.getItemDropped(state, rand, fortune), 1, this.damageDropped(state)));

        if (isMaxAge(state))
        {
            for (int i = 0; i < 1 + fortune; ++i)
            {
                if (rand.nextInt(2 * getMaxAge()) <= age)
                {
                    ret.add(new ItemStack(this.getSeed(), 1, 0));
                }
            }
        }
        return ret;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return isMaxAge(state) ? this.getCrop() : this.getSeed();
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(Reference.MOD_ID+":seed_rush", "inventory"));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
