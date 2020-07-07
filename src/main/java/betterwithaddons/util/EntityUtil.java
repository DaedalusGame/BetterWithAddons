package betterwithaddons.util;

import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityUtil {
    public static BlockPos getEntityPos(Entity ent)
    {
        int x = MathHelper.floor(ent.posX);
        int y = MathHelper.floor(ent.posY);
        int z = MathHelper.floor(ent.posZ);

        return new BlockPos(x,y,z);
    }

    public static BlockPos getEntityFloor(Entity ent,int limit)
    {
        World world = ent.world;
        BlockPos pos = getEntityPos(ent);
        if(world != null)
            for (int i = 0; i <= limit; i++)
            {
                if(!world.isAirBlock(pos.down(i)))
                {
                    return pos.down(i);
                }
            }

        return pos;
    }

    public static DamageSource causeLightningArrowDamage(Entity shooter, Entity arrow)
    {
        return (new EntityDamageSourceIndirect(InteractionBWA.GREATARROW_LIGHTNING_DAMAGE_TYPE,arrow,shooter)).setProjectile().setDamageBypassesArmor();
    }

    public static DamageSource causeFireArrowDamage(Entity shooter, Entity arrow)
    {
        return (new EntityDamageSourceIndirect(InteractionBWA.GREATARROW_FIRE_DAMAGE_TYPE,arrow,shooter)).setProjectile().setFireDamage();
    }
}
