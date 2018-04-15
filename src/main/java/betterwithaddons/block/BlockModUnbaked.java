package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockModUnbaked extends BlockBase implements IHasVariants {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    public BlockModUnbaked() {
        super("unbaked", Material.GROUND);
        this.setDefaultState(getDefaultState().withProperty(TYPE,EnumType.MELON));
    }

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(ModBlocks.unbaked, 1, type.getMetadata());
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float flX, float flY, float flZ, int meta, EntityLivingBase entity, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, hand);
        return state.withProperty(TYPE, EnumType.byMetadata(meta));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(tab.equals(BetterWithAddons.instance.creativeTab))
        for(EnumType type : EnumType.values())
            if(type.hasItem())
                items.add(new ItemStack(this,1,type.getMetadata()));
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return state.getValue(TYPE).getMaterial();
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(TYPE).getSoundType();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(TYPE).getAABB();
    }

    @Override
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
        return state.getValue(TYPE).getHardness();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getMetadata();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMetadata();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<>();

        for (EnumType type : EnumType.values())
        {
            rlist.add(new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID,type.getName()),"inventory"));
        }

        return rlist;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public String getVariantName(int meta) {
        return EnumType.byMetadata(meta).getName();
    }

    public enum EnumType implements IStringSerializable {
        MELON(0, "raw_melon_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), Material.CAKE, SoundType.CLOTH, 0.1f),
        MEAT(1, "raw_meat_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), Material.CAKE, SoundType.CLOTH, 0.1f),
        MUSHROOM(2, "raw_mushroom_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), Material.CAKE, SoundType.CLOTH, 0.1f),
        AMANITA(3, "raw_amanita_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D), Material.CAKE, SoundType.CLOTH, 0.1f);

        private static final EnumType[] VALUES = values();

        private final int meta;
        private final String name;
        private final AxisAlignedBB aabb;
        private final float hardness;
        private final boolean hasItem;
        private final SoundType soundType;
        private final Material material;

        @Override
        public String getName() {
            return this.name;
        }

        public int getMetadata() {
            return this.meta;
        }

        public AxisAlignedBB getAABB() {
            return this.aabb;
        }

        public SoundType getSoundType() {
            return soundType;
        }

        public Material getMaterial() {
            return material;
        }

        public float getHardness() {
            return hardness;
        }

        public boolean hasItem() {
            return hasItem;
        }

        EnumType(int metaIn, String nameIn, AxisAlignedBB aabbIn, Material material, SoundType soundType, float hardness) {
            this(metaIn, nameIn, aabbIn, material, soundType, hardness, true);
        }

        EnumType(int metaIn, String nameIn, AxisAlignedBB aabbIn, Material material, SoundType soundType, float hardness, boolean hasItem) {
            this.meta = metaIn;
            this.name = nameIn;
            this.aabb = aabbIn;
            this.hardness = hardness;
            this.soundType = soundType;
            this.material = material;
            this.hasItem = hasItem;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= VALUES.length) {
                meta = 0;
            }

            return VALUES[meta];
        }

        public String toString() {
            return this.name;
        }
    }
}
