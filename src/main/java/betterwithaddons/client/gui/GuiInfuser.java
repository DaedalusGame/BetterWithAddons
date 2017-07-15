package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerCherryBox;
import betterwithaddons.container.ContainerInfuser;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GuiInfuser extends GuiContainer {
    private final ResourceLocation guiLocation = new ResourceLocation(Reference.MOD_ID,"textures/gui/infuser.png");
    private TileEntityInfuser tileInfuser;

    public GuiInfuser(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerInfuser(player, world, x, y, z));
        tileInfuser = (TileEntityInfuser) world.getTileEntity(new BlockPos(x,y,z));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_drawGuiContainerForegroundLayer_1_, int p_drawGuiContainerForegroundLayer_2_) {
        //fontRendererObj.drawString(I18n.format("tile.ancestry_infuser.name", new Object[0]), 8, 6, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_drawGuiContainerBackgroundLayer_1_, int p_drawGuiContainerBackgroundLayer_2_, int p_drawGuiContainerBackgroundLayer_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiLocation);
        int offsetLeft = (this.width - this.xSize) / 2;
        int offsetTop = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetLeft, offsetTop, 0, 0, this.xSize, this.ySize);

        int spirits = tileInfuser != null ? tileInfuser.getSpirits() : 0;
        if(spirits < 8) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, spirits / 8.0F);
            this.drawTexturedModalRect(offsetLeft+21, offsetTop+8, 176, 0, 70, 70);
        }
    }
}
