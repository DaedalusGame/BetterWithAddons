package betterwithaddons.network;

import betterwithaddons.BetterWithAddons;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFireBlastFX implements IMessage {
    double x, y, z;
    float size;
    int time;

    public MessageFireBlastFX() {
    }

    public MessageFireBlastFX(double x, double y, double z, float size, int time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.time = time;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        size = buf.readFloat();
        time = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(size);
        buf.writeInt(time);
    }

    public static class Handler implements IMessageHandler<MessageFireBlastFX,IMessage>
    {
        @Override
        public IMessage onMessage(MessageFireBlastFX message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                BetterWithAddons.proxy.makeFireBlastFX(world, message.x, message.y, message.z, message.size, message.time);
            });

            return null;
        }
    }
}
