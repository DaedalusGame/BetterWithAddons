package betterwithaddons.network;

import betterwithaddons.lib.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class BWANetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

    public static void registerMessages()
    {
        int id = 0;
        INSTANCE.registerMessage(MessageRenameItem.Handler.class, MessageRenameItem.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageFireBlastFX.Handler.class, MessageFireBlastFX.class, id++, Side.CLIENT);
    }
}
