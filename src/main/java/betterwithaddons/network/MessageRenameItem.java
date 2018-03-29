package betterwithaddons.network;

import betterwithaddons.container.ContainerRename;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRenameItem implements IMessage {
    private String name;

    public MessageRenameItem() {
    }

    public MessageRenameItem(String name) {
        this.name = name;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,name);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        name = ByteBufUtils.readUTF8String(buf);
    }

    public static class HandlerRenameItem implements IMessageHandler<MessageRenameItem,IMessage>
    {
        @Override
        public IMessage onMessage(MessageRenameItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if(player.openContainer instanceof ContainerRename)
            {
                ContainerRename writingTable = (ContainerRename) player.openContainer;
                Minecraft.getMinecraft().addScheduledTask(() -> writingTable.updateItemName(ChatAllowedCharacters.filterAllowedCharacters(message.name)));
            }

            return null;
        }
    }
}