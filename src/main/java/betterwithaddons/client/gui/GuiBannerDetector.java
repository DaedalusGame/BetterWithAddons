package betterwithaddons.client.gui;

import java.io.IOException;

import betterwithaddons.container.ContainerBannerDetector;
import betterwithaddons.lib.Reference;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiBannerDetector extends GuiContainer
{
    final ResourceLocation background = new ResourceLocation(Reference.MOD_ID,"textures/gui/banner_detector.png");

    public GuiBannerDetector(EntityPlayer player, World world, int x, int y, int z)
    {
        super(new ContainerBannerDetector(player, world, x, y, z));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRendererObj.drawString(I18n.format("tile.banner_detector.name", new Object[0]), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        this.mc.renderEngine.bindTexture(background);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
