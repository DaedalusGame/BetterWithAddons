package betterwithaddons.block;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Christian on 05.08.2016.
 */
public interface IColorableBlock {
    @SideOnly(Side.CLIENT)
    IBlockColor getBlockColor();
    @SideOnly(Side.CLIENT)
    IItemColor getItemColor();
}
