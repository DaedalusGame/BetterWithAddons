package betterwithaddons.entity;

import betterwithaddons.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGreatarrow extends EntityArrow {
    protected float blockBreakPower = 3.0f;
    public boolean impactSoundPlayed = false;

    public EntityGreatarrow(World worldIn) {
        super(worldIn);
    }

    public EntityGreatarrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityGreatarrow(World worldIn, IPosition pos) {
        super(worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.greatarrow);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(world.isRemote)
            spawnPotionParticles(4);
    }

    public void spawnPotionParticles(int particleCount) {
        if(particleCount > 0)
            for(int i = 0; i < particleCount; i++)
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        boolean isnormalhit = true;

        if(raytraceResultIn.entityHit == null)
        {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            IBlockState blockstate = world.getBlockState(blockpos);
            Block block = blockstate.getBlock();
            float hardness = block.getExplosionResistance(world,blockpos,this,null);

            if(!world.isRemote && breakBlockWithParticles(blockpos,blockBreakPower))
            {
                blockBreakPower -= hardness;
                isnormalhit = false;

                for(int i = 0; i < 2; i++) {
                    BlockPos sideblock = blockpos.offset(EnumFacing.random(rand), 1);
                    float sidehardness = world.getBlockState(sideblock).getBlock().getExplosionResistance(world,blockpos,this,null);
                    if (breakBlockWithParticles(sideblock, blockBreakPower))
                        blockBreakPower -= sidehardness * 0.7f;
                }
            }

            this.posX -= (float)((this.posX + this.motionX) - raytraceResultIn.hitVec.xCoord);
            this.posY -= (float)((this.posY + this.motionY) - raytraceResultIn.hitVec.yCoord);
            this.posZ -= (float)((this.posZ + this.motionZ) - raytraceResultIn.hitVec.zCoord);
            if(!impactSoundPlayed) {
                this.playSound(SoundEvents.ENTITY_IRONGOLEM_DEATH, 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                impactSoundPlayed = true;
            }
        }

        if(isnormalhit)
        {
            super.onHit(raytraceResultIn);
        }

        //if(!worldObj.isRemote)
        //    worldObj.createExplosion(this, posX, posY, posZ, 2F, true);
        //setDead();
    }

    public void setBlockBreakPower(float power)
    {
        blockBreakPower = power;
    }

    public float getBlockBreakPower()
    {
        return blockBreakPower;
    }

    protected boolean breakBlockWithParticles(BlockPos blockpos, float power)
    {
        IBlockState blockstate = world.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        float hardness = block.getExplosionResistance(world,blockpos,this,null);

        if(hardness <= power)
        {
            if(!world.isRemote && !blockstate.getBlock().isAir(blockstate, world, blockpos)) {
                world.playEvent(2001, blockpos, Block.getStateId(blockstate));
                world.destroyBlock(blockpos, true);
            }
            return true;
        }

        return false;
    }
}