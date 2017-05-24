package betterwithaddons.block;

import betterwithaddons.util.IHasVariants;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockWhiteBrick extends BlockBase implements IHasVariants {
    public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", BlockWhiteBrick.EnumType.class);

    public BlockWhiteBrick() {
        super("whitebrick", Material.ROCK);

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.DEFAULT));
        this.setHardness(3.0f);
    }

    public int damageDropped(IBlockState state)
    {
        return (state.getValue(VARIANT)).getMetadata();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list)
    {
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
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+EnumType.DEFAULT.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+EnumType.MOSSY.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+EnumType.CRACKED.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+EnumType.CHISELED.getName()));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return EnumType.values()[meta].getName();
    }

    public enum EnumType implements IStringSerializable
    {
        DEFAULT(0, "default"),
        MOSSY(1, "mossy"),
        CRACKED(2, "cracked"),
        CHISELED(3, "chiseled");

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
