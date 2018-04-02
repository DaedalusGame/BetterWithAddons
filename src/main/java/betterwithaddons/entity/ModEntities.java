package betterwithaddons.entity;

import betterwithaddons.BetterWithAddons;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.awt.*;

public class ModEntities
{
    public static void init()
    {
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","greatarrow"),EntityGreatarrow.class, "greatarrow", 0, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","ya"),EntityYa.class, "ya", 1, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","artifactframe"),EntityArtifactFrame.class, "artifactframe", 2, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","ancestrybottle"),EntityAncestryBottle.class, "ancestrybottle", 3, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","spirit"),EntitySpirit.class, "spirit", 4, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","karate_zombie"),EntityKarateZombie.class, "karate_zombie", 5, BetterWithAddons.instance, 80, 1, true);

        EntityRegistry.registerEgg(new ResourceLocation("betterwithaddons","karate_zombie"),new Color(230,230,230).getRGB(),new Color(240,50,50).getRGB());
    }
}

