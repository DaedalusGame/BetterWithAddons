package betterwithaddons.block;

import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockAqueduct extends BlockBase implements IHasVariants {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", BlockAqueduct.EnumType.class);

    public BlockAqueduct() {
        super("aqueduct", Material.ROCK);

        this.setHardness(2.1F).setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 0);
        this.setTickRandomly(true);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(fromPos.equals(pos.up()))
        {
            worldIn.scheduleUpdate(pos, this, 25);
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        IBlockState waterState = worldIn.getBlockState(pos.up());
        Block block = waterState.getBlock();

        if(waterState.getMaterial() == Material.WATER && waterState.getValue(BlockLiquid.LEVEL) != 0) //Specifically almost full water... maybe needs to check falling water too...
        {
            int dist = TileEntityAqueductWater.getMinDistance(worldIn,pos.up())+1;
            if(dist <= InteractionBWA.AQUEDUCT_MAX_LENGTH) {
                worldIn.setBlockState(pos.up(), ModBlocks.aqueductWater.getDefaultState().withProperty(BlockLiquid.LEVEL,8));
                TileEntity te = worldIn.getTileEntity(pos.up());
                if(te instanceof TileEntityAqueductWater)
                    ((TileEntityAqueductWater) te).setDistanceFromSource(dist);
            }
        }
        else if(waterState.getBlock() instanceof BlockAqueductWater)
        {
            ((BlockAqueductWater) waterState.getBlock()).checkAndDry(worldIn,pos.up(),waterState);
        }
    }

    public int damageDropped(IBlockState state)
    {
        return (state.getValue(VARIANT)).getMetadata();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if(!disabled)
            for (EnumType type : EnumType.values())
            {
                list.add(new ItemStack(itemIn, 1, type.getMetadata()));
            }
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(VARIANT)).getMetadata();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT});
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        EnumType[] values = EnumType.values();
        ModelResourceLocation[] rlist = new ModelResourceLocation[values.length];

        for(EnumType type : values)
            rlist[type.getMetadata()] = new ModelResourceLocation(getRegistryName(), "variant="+type.getName());

        return Arrays.asList(rlist);
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    public enum EnumType implements IStringSerializable
    {
        STONE_BRICKS(0, "stone_bricks"),
        BRICKS(1, "bricks"),
        QUARTZ(2, "quartz"),
        WHITESTONE_BRICKS(3, "whitestone_bricks"),
        SANDSTONE(4,"sandstone"),
        RED_SANDSTONE(5,"red_sandstone"),
        ANDESITE(6,"andesite"),
        GRANITE(7,"granite"),
        DIORITE(8,"diorite"),
        PRISMARINE(9,"prismarine"),
        DARK_PRISMARINE(10,"dark_prismarine");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String name;

        EnumType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (EnumType type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }
}
