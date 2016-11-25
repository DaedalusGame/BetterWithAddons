package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockModPlanks extends Block
{
    ModWoods woodVariant;

    public BlockModPlanks(ModWoods variant)
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState());
        this.setHardness(2.0F).setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);

        woodVariant = variant;

        this.setUnlocalizedName("planks_"+woodVariant.getName());
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "planks_"+woodVariant.getName()));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.PLANKS.getFlammability(world, pos, face);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.PLANKS.getFireSpreadSpeed(world, pos, face);
    }
}
