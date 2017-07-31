package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.tileentity.TileEntityAncestrySand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAncestrySand extends BlockContainerBase {
    public BlockAncestrySand() {
        super("ancestry_sand", Material.SAND);
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.SOUL_SAND);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking() || !playerIn.getHeldItem(hand).isEmpty())
            return super.onBlockActivated(worldIn,pos,state,playerIn,hand,facing,hitX,hitY,hitZ);

        TileEntity te = worldIn.getTileEntity(pos);
        if(!worldIn.isRemote && te instanceof TileEntityAncestrySand)
        {
            playerIn.sendStatusMessage(new TextComponentTranslation("tile.ancestry_sand.spirits",((TileEntityAncestrySand) te).getSpirits()),true);
        }

        return true;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.NETHERRACK;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAncestrySand();
    }

    public int tickRate(World world) {
        return 10;
    }
}
