package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerCherryBox;
import betterwithaddons.container.ContainerTatara;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityCherryBox;
import betterwithaddons.tileentity.TileEntityTatara;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Christian on 19.09.2016.
 */
public abstract class GuiCherryBox extends GuiContainer {
    private final ResourceLocation boxGuiLocation;
    private TileEntityCherryBox tileBox;

    public GuiCherryBox(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerCherryBox(player, world, x, y, z));
        this.tileBox = (TileEntityCherryBox) world.getTileEntity(new BlockPos(x,y,z));
        boxGuiLocation = getGuiLocation();
    }

    public ResourceLocation getGuiLocation()
    {
        return null;
    }

    public String getDisplayName()
    {
        return null;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_) {
        fontRendererObj.drawString(I18n.format(getDisplayName(), new Object[0]), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_drawGuiContainerBackgroundLayer_1_, int p_drawGuiContainerBackgroundLayer_2_, int p_drawGuiContainerBackgroundLayer_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(boxGuiLocation);
        int offsetLeft = (this.width - this.xSize) / 2;
        int offsetTop = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, this.xSize, this.ySize);
        int val;

        val = tileBox.getCookProgressScaled(24);
        this.drawTexturedModalRect(offsetLeft + 79, offsetTop + 34, 176, 14, val + 1, 16);

        if(tileBox.isWorking())
        {
            this.drawTexturedModalRect(offsetLeft + 84, offsetTop + 18, 176, 0, 13, 13);
        }
    }
}
