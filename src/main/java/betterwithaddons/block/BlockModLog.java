package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockModLog extends BlockLog
{
    // Each instance has a reference to its own variant property
    public ModWoods woodVariant;

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { LOG_AXIS });
    }

    public BlockModLog(ModWoods variant)
    {
        super();
        woodVariant = variant;

        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHarvestLevel("axe", 0);

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "log_"+woodVariant.getName()));
        this.setUnlocalizedName("log_"+woodVariant.getName());
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    // map from state to meta and vice verca - use high 2 bits for LOG_AXIS, low 2 bits for VARIANT
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.values()[meta >> 2]);
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LOG_AXIS).ordinal() * 4;
    }

    // discard the axis information - otherwise logs facing different directions would not stack together
    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.LOG.getFlammability(world, pos, face);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.LOG.getFireSpreadSpeed(world, pos, face);
    }


}
