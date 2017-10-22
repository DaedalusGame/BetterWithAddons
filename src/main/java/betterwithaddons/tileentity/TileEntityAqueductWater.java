package betterwithaddons.tileentity;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.interaction.InteractionBWA;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockPump;
import betterwithmods.util.DirUtils;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import java.util.HashSet;

public class TileEntityAqueductWater extends TileEntityBase {
    private int distanceFromSource;
    private static final HashSet<Block> WATER_SOURCES = new HashSet<>();
    private static final HashSet<Biome> BIOMES = new HashSet<>();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T)new FluidBlockWrapper(ModBlocks.aqueductWater, world, pos);

        return super.getCapability(capability, facing);
    }

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

        HashSet<BlockPos> corners = new HashSet<>();

        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos neighborpos = checkpos.offset(facing);
            IBlockState state = world.getBlockState(neighborpos);
            boolean isWater = false;
            int dist = getBlockDistanceFromSource(world,neighborpos,state,false);

            if(dist > -1)
            {
                minDistance = Math.min(minDistance,dist);
                isWater = true;
            }

            if(isWater)
            {
                corners.add(neighborpos.offset(facing.rotateY(),1));
                corners.add(neighborpos.offset(facing.rotateY(),-1));
            }
        }

        for(BlockPos neighborpos : corners)
        {
            IBlockState state = world.getBlockState(neighborpos);

            minDistance = Math.min(minDistance,getBlockDistanceFromSource(world,neighborpos,state,false));
        }

        return minDistance;
    }

    public static int getBlockDistanceFromSource(World world, BlockPos pos, IBlockState state, boolean recursed) {
        boolean isValidBiome = true;

        if(!BIOMES.isEmpty())
            isValidBiome = BIOMES.contains(world.getBiome(pos)) == InteractionBWA.AQUEDUCT_BIOMES_IS_WHITELIST;

        if(isValidBiome && ((state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0) || WATER_SOURCES.contains(state.getBlock())))
        {
            return 0;
        }
        else if(state.getBlock() == BWMBlocks.TEMP_LIQUID_SOURCE)
        {
            BlockPos pumppos = pos.down();
            IBlockState pump = world.getBlockState(pumppos);
            if(pump.getBlock() == BWMBlocks.PUMP)
            {
                EnumFacing facing = pump.getValue(DirUtils.HORIZONTAL);
                BlockPos pumpInput = pumppos.offset(facing);
                return getBlockDistanceFromSource(world,pumpInput,world.getBlockState(pumpInput),true);
            }
        }
        else if(state.getBlock() == ModBlocks.aqueduct)
        {
            return getBlockDistanceFromSource(world,pos.up(),world.getBlockState(pos.up()),true);
        }
        else
        {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof TileEntityAqueductWater) {
                return ((TileEntityAqueductWater) te).getDistanceFromSource();
            }
        }

        return -1;
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
