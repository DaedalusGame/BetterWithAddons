package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.EntityUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ItemGreatarrowFire extends ItemGreatarrow {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/entity/greatarrow_fire.png");

    @Override
    public ResourceLocation getEntityTexture(EntityGreatarrow arrow) {
        return TEXTURE;
    }

    @Override
    public double getDamageBase() {
        return InteractionBWA.GREATARROW_FIRE_DAMAGE;
    }

    @Override
    public float getBlockBreakPowerBase() {
        return 0.0f;
    }

    @Override
    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        if(!arrow.isDead)
            explode(arrow, new Vec3d(entity.posX, entity.posY + entity.height / 2, entity.posZ), null);
    }

    @Override
    public void hitBlock(EntityGreatarrow arrow, RayTraceResult rayTraceResult, boolean destroyed) {
        World world = arrow.world;
        BlockPos pos = rayTraceResult.getBlockPos();
        IBlockState newState = world.getBlockState(pos);
        if(newState.getBlock().isReplaceable(world,pos)) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
    }

    @Override
    public void hitBlockFinal(EntityGreatarrow arrow, RayTraceResult rayTraceResult) {
        if(!arrow.isDead)
            explode(arrow, rayTraceResult.hitVec, rayTraceResult.sideHit);
    }

    public void explode(EntityGreatarrow arrow, Vec3d impact, EnumFacing normal) {
        World world = arrow.world;
        if(!world.isRemote) {
            BetterWithAddons.proxy.makeFireBlastFX(world, impact.x, impact.y, impact.z, 4,10);
            BetterWithAddons.proxy.makeFireExplosionFX(world, impact.x, impact.y, impact.z, 3, 2, 8, 4);
            BlockPos hitPos = new BlockPos(impact.x, impact.y, impact.z);
            if(normal != null)
                hitPos = hitPos.offset(normal);
            napalmFill(world, hitPos,3,5, 0);
            List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(impact.x, impact.y, impact.z, impact.x, impact.y, impact.z).grow(3), entity -> true);
            DamageSource damageSource = EntityUtil.causeFireArrowDamage(arrow.shootingEntity, arrow);
            for (Entity entity : entities) {
                entity.attackEntityFrom(damageSource, (float)InteractionBWA.GREATARROW_FIRE_EXTRA_DAMAGE);
                entity.setFire(InteractionBWA.GREATARROW_FIRE_BURN);
            }
            world.playSound(null, arrow.posX, arrow.posY, arrow.posZ, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 1.5f, 0.4f);
        }
        arrow.setDead();
    }

    class FloodPos {
        BlockPos pos;
        int distance;

        public FloodPos(BlockPos pos, int distance) {
            this.pos = pos;
            this.distance = distance;
        }
    }

    public void napalmFill(World world, BlockPos pos, float radius, int distance, int up) {
        Queue<FloodPos> toVisit = new LinkedList<>();
        HashSet<BlockPos> visited = new HashSet<>();

        toVisit.add(new FloodPos(pos,0));

        while(!toVisit.isEmpty()) {
            FloodPos visitPos = toVisit.remove();
            BlockPos checkPos = visitPos.pos;
            visited.add(checkPos);
            if(checkPos.distanceSq(pos) > radius * radius)
                continue;
            IBlockState state = world.getBlockState(checkPos);
            boolean replaceable = state.getBlock().isReplaceable(world, checkPos);
            if(replaceable && itemRand.nextInt(3) < 2) {
                world.setBlockState(checkPos, Blocks.FIRE.getDefaultState());
            } else if(visitPos.distance > 0 && !replaceable && !isFlammable(world,checkPos,state)) {
                continue;
            }
            for (EnumFacing facing : EnumFacing.VALUES) {
                int newDist = visitPos.distance + 1;
                BlockPos newPos = checkPos.offset(facing);
                if(visited.contains(newPos) || newDist > distance)
                    continue;
                toVisit.add(new FloodPos(newPos, newDist));
            }
        }
    }

    private boolean isFlammable(World world, BlockPos pos, IBlockState state) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if(state.getBlock().isFlammable(world,pos,facing))
                return true;
        }
        return false;
    }
}
