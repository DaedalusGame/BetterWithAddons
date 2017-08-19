package betterwithaddons.tileentity;

import betterwithaddons.lib.Reference;
import net.minecraft.tileentity.TileEntity;
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
        registerTE(TileEntityLureTree.class, "luretree");
        registerTE(TileEntityChute.class, "chute");
        registerTE(TileEntityAqueductWater.class, "aqueductWater");
        registerTE(TileEntityLegendarium.class, "legendarium");
        registerTE(TileEntityBrine.class, "brine");
        registerTE(TileEntityAncestrySand.class, "ancestrySand");
        registerTE(TileEntityInfuser.class, "infuser");
        registerTE(TileEntityLoom.class, "loom");
        registerTE(TileEntityRopeSideways.class, "rope_sideways");
        registerTE(TileEntityRopePost.class, "rope_post");
    }

    private static void registerTE(Class<? extends TileEntity> clazz, String name)
    {
        GameRegistry.registerTileEntity(clazz, Reference.MOD_ID + ":" + name);
    }
}
