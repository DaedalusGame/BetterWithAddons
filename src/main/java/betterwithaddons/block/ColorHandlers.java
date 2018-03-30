package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.block.EriottoMod.BlockCropTea;
import betterwithaddons.item.ItemTea;
import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityTea;
import betterwithaddons.util.TeaType;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;

public class ColorHandlers {
    public static final IBlockColor GRASS_COLORING = new IBlockColor()
    {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex)
        {
            return world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    };

    public static final IBlockColor REDSTONE_COLORING = new IBlockColor()
    {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex)
        {
            if(state != null && state.getBlock() instanceof BlockWirePCB)
            {
                return BlockWirePCB.colorMultiplier(state.getValue(BlockRedstoneWire.POWER));
            }

            return 0;
        }
    };

    public static final IItemColor ARMOR_COLORING = (stack, tintIndex) -> {
        ItemArmor armor = (ItemArmor)stack.getItem();
        return tintIndex == 1 ? 0xFFFFFF : armor.getColor(stack);
    };

    public static final IItemColor BLOCK_ITEM_COLORING = (stack, tintIndex) -> {
        IBlockState state = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
        IBlockColor blockColor = ((IColorable)state.getBlock()).getBlockColor();
        return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
    };

    public static final IItemColor ECKSIE_COLORING = (stack, tintIndex) -> {
        BlockEcksieSapling block = (BlockEcksieSapling) ((ItemBlock)stack.getItem()).getBlock();
        IBlockState state = block.getLeafBlock(stack.getMetadata());
        return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state,null,null,tintIndex);
    };

    public static final IItemColor MIMIC_COLORING = (stack,tintindex) -> {
        ItemStack innerstack = ModItems.brokenArtifact.getInnerStack(stack);
        if (!innerstack.isEmpty())
            return Minecraft.getMinecraft().getItemColors().colorMultiplier(innerstack, tintindex);
        return 0xFFFFFF;
    };

    public static final IBlockColor TEA_COLORING = (state, worldIn, pos, tintIndex) -> {
        if(tintIndex == 0) {
            int age = state.getValue(BlockCropTea.AGE);
            int j = (int)MathHelper.clampedLerp(123,181,age/7.0);
            int k = (int)MathHelper.clampedLerp(108,219,1.0 - age/7.0);
            int l = (int)MathHelper.clampedLerp(36,60,age/7.0);
            return j << 16 | k << 8 | l;
        }
        if(worldIn != null && pos != null) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TileEntityTea)
                return ((TileEntityTea) tile).getType().getLeafColor();
        }
        return TeaType.WHITE.getLeafColor();
    };

    public static final IItemColor TEA_ITEM_COLORING = (stack, tintIndex) -> {
        Item item = stack.getItem();
        if(item instanceof ItemTea)
            return ((ItemTea) item).getColor(stack);
        return TeaType.WHITE.getLeafColor();
    };
}
