package betterwithaddons.network;

import betterwithaddons.lib.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BWANetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    static
    {
        INSTANCE.registerMessage(MessageRenameItem.HandlerRenameItem.class, MessageRenameItem.class, 0, Side.SERVER);
    }
}
