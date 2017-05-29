package betterwithaddons.block.BetterRedstone;

import betterwithaddons.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPCB extends BlockBase {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockPCB() {
        super("pcb_block", Material.ROCK);

        this.setHardness(3.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(NORTH, (meta & 1) > 0).withProperty(EAST, (meta & 2) > 0).withProperty(SOUTH, (meta & 4) > 0).withProperty(WEST, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i |= (state.getValue(NORTH) ? 1 : 0);
        i |= (state.getValue(EAST) ? 2 : 0);
        i |= (state.getValue(SOUTH) ? 4 : 0);
        i |= (state.getValue(WEST) ? 8 : 0);

        return i;
    }

    public static boolean allowsFacing(IBlockState state, EnumFacing testfacing)
    {
        if(testfacing == EnumFacing.NORTH) return state.getValue(NORTH);
        if(testfacing == EnumFacing.EAST) return state.getValue(EAST);
        if(testfacing == EnumFacing.SOUTH) return state.getValue(SOUTH);
        if(testfacing == EnumFacing.WEST) return state.getValue(WEST);
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        x -= 0.5f;
        z -= 0.5f;

        ItemStack item = player.getHeldItem(hand);

        if(facing != EnumFacing.UP || !item.isEmpty())
            return false;

        boolean isEast = x > 0 && Math.abs(z) < Math.abs(x);
        boolean isWest = x < 0 && Math.abs(z) < Math.abs(x);
        boolean isSouth = z > 0 && Math.abs(x) < Math.abs(z);
        boolean isNorth = z < 0 && Math.abs(x) < Math.abs(z);

        IBlockState newstate = state;

        if(isNorth) newstate = newstate.withProperty(NORTH,!state.getValue(NORTH));
        if(isSouth) newstate = newstate.withProperty(SOUTH,!state.getValue(SOUTH));
        if(isEast) newstate = newstate.withProperty(EAST,!state.getValue(EAST));
        if(isWest) newstate = newstate.withProperty(WEST,!state.getValue(WEST));

        world.setBlockState(pos,newstate);
        world.notifyNeighborsOfStateChange(pos.up(), this, false);

        return true;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { NORTH, EAST, SOUTH, WEST });
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        boolean[] connections = new boolean[] {state.getValue(NORTH),state.getValue(EAST),state.getValue(SOUTH),state.getValue(WEST)};
        int offset = 0;
        switch(rot)
        {
            case CLOCKWISE_90:
                offset = 1; break;
            case CLOCKWISE_180:
                offset = 2; break;
            case COUNTERCLOCKWISE_90:
                offset = 3; break;
        }

        return state.withProperty(NORTH,connections[(0+offset)%4])
                .withProperty(EAST,connections[(1+offset)%4])
                .withProperty(SOUTH,connections[(2+offset)%4])
                .withProperty(WEST,connections[(3+offset)%4]);
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        boolean north = state.getValue(NORTH);
        boolean east = state.getValue(EAST);
        boolean south = state.getValue(SOUTH);
        boolean west = state.getValue(WEST);

        return state.withProperty(NORTH,mirrorIn == Mirror.LEFT_RIGHT ? south : north)
                .withProperty(EAST,mirrorIn == Mirror.LEFT_RIGHT ? west : east)
                .withProperty(SOUTH,mirrorIn == Mirror.LEFT_RIGHT ? north : south)
                .withProperty(WEST,mirrorIn == Mirror.LEFT_RIGHT ? east : west);
    }
}
