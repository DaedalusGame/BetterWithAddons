package betterwithaddons.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityUtil {
    public static BlockPos getEntityPos(Entity ent)
    {
        int x = MathHelper.floor_double(ent.posX);
        int y = MathHelper.floor_double(ent.posY);
        int z = MathHelper.floor_double(ent.posZ);

        return new BlockPos(x,y,z);
    }

    public static BlockPos getEntityFloor(Entity ent,int limit)
    {
        World world = ent.worldObj;
        BlockPos pos = getEntityPos(ent);
        if(world != null)
            for (int i = 0; i <= limit; i++)
            {
                if(!world.isAirBlock(pos.down(i)))
                {
                    return pos;
                }
            }

        return pos;
    }
}
