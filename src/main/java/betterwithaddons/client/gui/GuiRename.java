package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerChute;
import betterwithaddons.container.ContainerRename;
import betterwithaddons.lib.Reference;
import betterwithaddons.network.BWANetworkHandler;
import betterwithaddons.network.MessageRenameItem;
import betterwithaddons.tileentity.TileEntityChute;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiRename extends GuiContainer implements IContainerListener {
    private static final ResourceLocation boxGuiLocation = new ResourceLocation(Reference.MOD_ID,"textures/gui/rename.png");
    private ContainerRename writingTable;
    private GuiTextField nameField;
    private EntityPlayer player;

    public GuiRename(EntityPlayer player, World world, int x, int y, int z) {
        super(new ContainerRename(player, world, x, y, z));
        this.player = player;
        this.ySize = 168;
        this.writingTable = (ContainerRename) inventorySlots;
    }

    public void initGui()
    {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.nameField = new GuiTextField(0, this.fontRenderer, i + 12, j + 20, 154, 12);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.inventorySlots.removeListener(this);
        this.inventorySlots.addListener(this);
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        this.inventorySlots.removeListener(this);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (this.nameField.textboxKeyTyped(typedChar, keyCode))
        {
            this.renameItem();
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    private void renameItem()
    {
        String s = this.nameField.getText();
        Slot slot = this.writingTable.getSlot(0);

        if (slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
        {
            s = "";
        }

        this.writingTable.updateItemName(s);
        BWANetworkHandler.INSTANCE.sendToServer(new MessageRenameItem(s));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.fontRenderer.drawString(I18n.format("tile.writing_table.name"), 8, 6, 4210752);

        if (this.writingTable.cost > 0)
        {
            int i = 8453920;
            boolean flag = true;
            String string = I18n.format("inv.writing_table.cost.name", this.writingTable.cost);

            if (!this.writingTable.getSlot(3).getHasStack())
            {
                flag = false;
            }
            else if (!this.writingTable.getSlot(3).canTakeStack(this.player))
            {
                i = 16736352;
            }

            if (flag)
            {
                int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
                int k = this.xSize - 8 - this.fontRenderer.getStringWidth(string);
                int l = 74;

                if (this.fontRenderer.getUnicodeFlag())
                {
                    drawRect(k - 3, l - 2, this.xSize - 7, 77, -16777216);
                    drawRect(k - 2, l - 1, this.xSize - 8, 76, -12895429);
                }
                else
                {
                    this.fontRenderer.drawString(string, k, l + 1, j);
                    this.fontRenderer.drawString(string, k + 1, l, j);
                    this.fontRenderer.drawString(string, k + 1, l + 1, j);
                }

                this.fontRenderer.drawString(string, k, l, i);
            }
        }

        GlStateManager.enableLighting();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(boxGuiLocation);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        boolean hasWritingUtils = this.writingTable.getSlot(1).getHasStack() && this.writingTable.getSlot(2).getHasStack();
        this.drawTexturedModalRect(i + 7, j + 16, 0, this.ySize + (hasWritingUtils ? 0 : 16), 162, 16);
        this.nameField.setEnabled(hasWritingUtils);

        /*if ((this.writingTable.getSlot(0).getHasStack() || this.writingTable.getSlot(1).getHasStack()) && !this.writingTable.getSlot(2).getHasStack())
        {
            this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
        }*/
    }

    @Override
    public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
        this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
        if (slotInd == 0)
        {
            this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());

            if (!stack.isEmpty())
            {
                this.renameItem();
            }
        }
    }

    @Override
    public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

    }

    @Override
    public void sendAllWindowProperties(Container containerIn, IInventory inventory) {

    }
}
