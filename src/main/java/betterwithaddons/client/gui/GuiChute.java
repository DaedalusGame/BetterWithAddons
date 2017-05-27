package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerChute;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityChute;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiChute extends GuiContainer {
    private static final ResourceLocation boxGuiLocation = new ResourceLocation(Reference.MOD_ID,"textures/gui/chute.png");
    private TileEntityChute tileChute;


    public GuiChute(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerChute(player, world, x, y, z));
        this.ySize = 193;
        this.tileChute = (TileEntityChute) world.getTileEntity(new BlockPos(x,y,z));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_) {
        fontRendererObj.drawString(I18n.format("tile.chute.name", new Object[0]), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_drawGuiContainerBackgroundLayer_1_, int p_drawGuiContainerBackgroundLayer_2_, int p_drawGuiContainerBackgroundLayer_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(boxGuiLocation);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);

        if (this.tileChute.power > 0) {
            drawTexturedModalRect(xPos + 80, yPos + 18, 176, 0, 14, 14);
        }
    }
}
