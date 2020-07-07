package betterwithaddons.entity;

import betterwithaddons.interaction.InteractionEriottoMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityAncestryBottle extends EntityExpBottle {
    public EntityAncestryBottle(World worldIn) {
        super(worldIn);
    }

    public EntityAncestryBottle(World worldIn, double x, double y, double z) {
        super(worldIn);
        setPosition(x,y,z);
    }

    public EntityAncestryBottle(World world, EntityLivingBase thrower) {
        super(world,thrower);
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (!this.world.isRemote)
        {
            this.world.playEvent(2002, new BlockPos(this), InteractionEriottoMod.SPIRIT_COLOR_HIGH.getRGB());
            int i = InteractionEriottoMod.BOTTLE_MAX_SPIRITS;

            while (i > 0)
            {
                int j = EntitySpirit.getSpiritSplit(i);
                i -= j;
                this.world.spawnEntity(new EntitySpirit(this.world, this.posX, this.posY, this.posZ, j));
            }

            this.setDead();
        }
    }
}
