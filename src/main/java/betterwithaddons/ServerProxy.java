package betterwithaddons;

import betterwithaddons.network.BWANetworkHandler;
import betterwithaddons.network.MessageFireBlastFX;
import betterwithaddons.network.MessageFireExplosionFX;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void postInit() {

    }

    @Override
    public void makeLeafFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {

    }

    @Override
    public void makeLightningFX(double x, double y, double z, float r, float g, float b, float size, float maxAgeMul) {

    }

    @Override
    public void makeFireBlastFX(World world, double x, double y, double z, float size, int time) {
        BWANetworkHandler.INSTANCE.sendToDimension(new MessageFireBlastFX(x,y,z,size,time),world.provider.getDimension());
    }

    @Override
    public void makeFireExplosionFX(World world, double x, double y, double z, float size, float radius, int time, int count) {
        BWANetworkHandler.INSTANCE.sendToDimension(new MessageFireExplosionFX(x,y,z,size,radius,time,count),world.provider.getDimension());
    }

    @Override
    public void registerResourcePack() {

    }

    @Override
    public void addResourceOverride(String space, String dir, String file, String ext) {

    }

    @Override
    public void addResourceOverride(String modid, String space, String dir, String file, String ext) {

    }
}
