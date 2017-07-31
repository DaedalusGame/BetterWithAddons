package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerInfuser;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;

public class GuiInfuser extends GuiContainer {
    private final ResourceLocation guiLocation = new ResourceLocation(Reference.MOD_ID,"textures/gui/infuser.png");
    private TileEntityInfuser tileInfuser;
    private ContainerInfuser container;
    private float currSouls, souls;

    public GuiInfuser(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerInfuser(player, world, x, y, z));
        tileInfuser = (TileEntityInfuser) world.getTileEntity(new BlockPos(x,y,z));
        container = (ContainerInfuser) inventorySlots;
        souls = currSouls = tileInfuser.getSpirits();
    }

    public float quartEase(float currentTime, float start, float delta, float duration)
    {
        currentTime = currentTime / duration - 1;
        return -delta * (currentTime*currentTime*currentTime*currentTime - 1) + start;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_) {
        int foregroundcolor = new Color(255,0,0).getRGB();
        int backgroundcolor = new Color(128,0,0).getRGB();

        String costString = I18n.format("inv.infuser.cost.name",container.requiredSpirit);

        if(container.requiredSpirit > 0) {
            int drawoffsetX = 102 - this.fontRenderer.getStringWidth(costString) / 2;
            int drawoffsetY = 38;

            fontRenderer.drawString(costString, drawoffsetX, drawoffsetY+1, backgroundcolor);
            fontRenderer.drawString(costString, drawoffsetX+1, drawoffsetY+1, backgroundcolor);
            fontRenderer.drawString(costString, drawoffsetX+1, drawoffsetY+1, backgroundcolor);
            fontRenderer.drawString(costString, drawoffsetX, drawoffsetY, foregroundcolor);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_drawGuiContainerBackgroundLayer_1_, int p_drawGuiContainerBackgroundLayer_2_, int p_drawGuiContainerBackgroundLayer_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiLocation);
        int offsetLeft = (this.width - this.xSize) / 2;
        int offsetTop = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, this.xSize, this.ySize);

        souls = tileInfuser != null ? tileInfuser.getSpirits() : 0;
        if(currSouls < souls)
            currSouls = Math.min(souls,currSouls+0.1f);
        else if(currSouls > souls)
            currSouls = Math.max(souls,currSouls-0.1f);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0f-quartEase(currSouls,0f,1f,InteractionEriottoMod.MAX_SPIRITS));
        this.drawTexturedModalRect(offsetLeft+21, offsetTop+8, 176, 0, 70, 70);
    }
}
