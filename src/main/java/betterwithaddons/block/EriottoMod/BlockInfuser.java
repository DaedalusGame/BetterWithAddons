package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.lib.GuiIds;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockInfuser extends BlockContainerBase {
    public static final PropertyBool ISACTIVE = PropertyBool.create("active");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockInfuser() {
        super("ancestry_infuser", Material.ROCK);
        this.setHardness(5.0F);
        this.setResistance(2000.0F);
        this.setTickRandomly(true);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ISACTIVE,(meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ISACTIVE) ? 1 : 0;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return AABB;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityInfuser();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(BetterWithAddons.instance, GuiIds.INFUSER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos,this,5);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean isOn = state.getValue(ISACTIVE);
        boolean isValid = isValid(worldIn,pos);

        if(isOn != isValid) {
            worldIn.setBlockState(pos,state.cycleProperty(ISACTIVE));
        }
    }

    public boolean isValid(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        IBlockState bottomState = world.getBlockState(pos.down());

        boolean validSand = bottomState.getBlock() == ModBlocks.ANCESTRY_SAND || bottomState.getBlock() == Blocks.SOUL_SAND;

        if(state.getBlock() != ModBlocks.INFUSER || !validSand)
            return false;

        int light = world.getLightFor(EnumSkyBlock.BLOCK, pos);
        int skylight = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted();
        float celestialAngle = world.getCelestialAngleRadians(1.0F);

        if (skylight > 0)
        {
            float celestialAngleClamped = celestialAngle < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
            celestialAngle = celestialAngle + (celestialAngleClamped - celestialAngle) * 0.2F;
            skylight = Math.round((float)skylight * MathHelper.cos(celestialAngle));
        }

        skylight = MathHelper.clamp(skylight, 0, 15);

        if(Math.max(light,skylight) > 7)
            return false;

        for(int x=-1; x<=1; x++)
            for(int z=-1; z<=1; z++)
                for(int y=-2; y<=1; y++)
                    if((x != 0 || z != 0 || y > 0 || y < -1) && !world.isAirBlock(pos.add(x,y,z)))
                        return false;

        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
