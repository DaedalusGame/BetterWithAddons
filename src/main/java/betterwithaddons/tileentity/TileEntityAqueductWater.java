package betterwithaddons.tileentity;

import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.interaction.InteractionBWM;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.HashSet;

public class TileEntityAqueductWater extends TileEntityBase {
    private int distanceFromSource;
    private static final HashSet<Block> WATER_SOURCES = new HashSet<>();
    private static final HashSet<Biome> BIOMES = new HashSet<>();

    public static void addWaterSource(Block block)
    {
        WATER_SOURCES.add(block);
    }

    public int getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
        this.markDirty();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("Distance", this.distanceFromSource);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        this.distanceFromSource = compound.getInteger("Distance");
    }

    public static int getMinDistance(World world, BlockPos checkpos)
    {
        int minDistance = 1000000;

        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos neighborpos = checkpos.offset(facing);
            IBlockState state = world.getBlockState(neighborpos);

            if(isProperSource(world,neighborpos,state))
            {
                minDistance = Math.min(minDistance,0);
            }
            else
            {
                TileEntity te = world.getTileEntity(neighborpos);

                if (te instanceof TileEntityAqueductWater) {
                    minDistance = Math.min(minDistance,((TileEntityAqueductWater) te).getDistanceFromSource());
                }
            }
        }

        return minDistance;
    }

    public static boolean isProperSource(World world, BlockPos pos, IBlockState state) {
        boolean isValidBiome = true;

        if(!BIOMES.isEmpty())
            isValidBiome = BIOMES.contains(world.getBiome(pos)) == InteractionBWA.AQUEDUCT_BIOMES_IS_WHITELIST;

        return isValidBiome && ((state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0) || WATER_SOURCES.contains(state.getBlock()));
    }

    public static void reloadBiomeList()
    {
        BIOMES.clear();
        for (String str : InteractionBWA.AQUEDUCT_BIOME_STRINGS) {
            ResourceLocation location = new ResourceLocation(str);
            Biome biome = Biome.REGISTRY.getObject(location);
            if(biome != null && !BIOMES.contains(biome))
                BIOMES.add(biome);
        }
    }
}
