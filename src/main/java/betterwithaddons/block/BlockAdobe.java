package betterwithaddons.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAdobe extends BlockBase {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 7);
    public static final PropertyBool DRY_STATE = PropertyBool.create("dry");

    public AdobeType[] types;

    protected BlockAdobe(String name, AdobeType[] types) {
        super(name, Material.CLAY);
        this.types = types;
        for(int meta = 0; meta < types.length; meta++)
        {
            types[meta].wetBlock = getDefaultState().withProperty(VARIANT,meta).withProperty(DRY_STATE,false);
            types[meta].dryBlock = getDefaultState().withProperty(VARIANT,meta).withProperty(DRY_STATE,true);
        }
    }

    @Override
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
        return types[state.getValue(VARIANT)].getHardness();
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        if(world == null)
            return 15.0f;

        IBlockState state = world.getBlockState(pos);

        return types[state.getValue(VARIANT)].getResistance();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if(!state.getValue(DRY_STATE) || types[state.getValue(VARIANT)].dropsBlock())
            super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return state.getValue(DRY_STATE) ? Material.ROCK : Material.CLAY;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(DRY_STATE) ? SoundType.STONE : SoundType.GROUND;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, DRY_STATE});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT) | (state.getValue(DRY_STATE) ? 8 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT,meta & 7).withProperty(DRY_STATE, (meta & 8) > 0);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for(int meta = 0; meta < 16; meta++)
            items.add(new ItemStack(this,1,meta));
    }
}
