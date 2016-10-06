package betterwithaddons.tileentity;

import betterwithaddons.lib.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities
{
    public static void register()
    {
        registerTE(TileEntityBannerDetector.class, "bannerDetector");
        registerTE(TileEntityWorldScaleActive.class, "worldScaleActive");
        registerTE(TileEntityAlchDragon.class, "alchDragon");
        registerTE(TileEntityNettedScreen.class, "nettedScreen");
        registerTE(TileEntityTatara.class, "tatara");
        registerTE(TileEntitySoakingBox.class, "soakingbox");
        registerTE(TileEntityDryingBox.class, "dryingbox");
    }

    private static void registerTE(Class<? extends TileEntity> clazz, String name)
    {
        GameRegistry.registerTileEntity(clazz, Reference.MOD_ID + ":" + name);
    }
}
