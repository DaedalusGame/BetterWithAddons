package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.item.ModItems;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        IBlockState state = block.treeLeaves[stack.getMetadata()];
        return Minecraft.getMinecraft().getBlockColors().colorMultiplier(state,null,null,tintIndex);
    };

    public static final IItemColor MIMIC_COLORING = (stack,tintindex) -> {
        if(stack.hasCapability(ModItems.brokenArtifact.DATA_CAP,null)) {
            ItemStack innerstack = stack.getCapability(ModItems.brokenArtifact.DATA_CAP,null).inner;
            if (!innerstack.isEmpty())
                return Minecraft.getMinecraft().getItemColors().getColorFromItemstack(innerstack, tintindex);
        }
        return 0xFFFFFF;
    };
}
