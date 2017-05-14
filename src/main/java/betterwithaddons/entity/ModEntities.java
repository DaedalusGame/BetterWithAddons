package betterwithaddons.entity;

import betterwithaddons.BetterWithAddons;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static void init()
    {
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","greatarrow"),EntityGreatarrow.class, "greatarrow", 0, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("betterwithaddons","ya"),EntityYa.class, "ya", 1, BetterWithAddons.instance, 80, 1, true);
    }
}

