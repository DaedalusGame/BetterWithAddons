package betterwithaddons.network;

import betterwithaddons.BetterWithAddons;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFireExplosionFX implements IMessage {
    double x, y, z;
    float size;
    int time;
    float radius;
    int count;

    public MessageFireExplosionFX() {
    }

    public MessageFireExplosionFX(double x, double y, double z, float size, float radius, int time, int count) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.radius = radius;
        this.time = time;
        this.count = count;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        size = buf.readFloat();
        radius = buf.readFloat();
        time = buf.readInt();
        count = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(size);
        buf.writeFloat(radius);
        buf.writeInt(time);
        buf.writeInt(count);
    }

    public static class Handler implements IMessageHandler<MessageFireExplosionFX,IMessage>
    {
        @Override
        public IMessage onMessage(MessageFireExplosionFX message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().world;
            Minecraft.getMinecraft().addScheduledTask(() -> {
                BetterWithAddons.proxy.makeFireExplosionFX(world, message.x, message.y, message.z, message.size, message.radius, message.time, message.count);
            });

            return null;
        }
    }
}
