package betterwithaddons.entity;

import betterwithaddons.BetterWithAddons;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static void init()
    {
        EntityRegistry.registerModEntity(EntityGreatarrow.class, "greatarrow", 0, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(EntityYa.class, "ya", 1, BetterWithAddons.instance, 80, 1, true);
        EntityRegistry.registerModEntity(EntityFallingGourd.class, "fallinggourd", 2, BetterWithAddons.instance, 80, 1, true);
    }
}

