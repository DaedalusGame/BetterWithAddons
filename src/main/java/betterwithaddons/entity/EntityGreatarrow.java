package betterwithaddons.entity;

import betterwithaddons.item.ItemGreatarrow;
import betterwithaddons.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGreatarrow extends EntityArrow {
    protected float blockBreakPower = 3.0f;
    public boolean impactSoundPlayed = false;

    private static final DataParameter<ItemStack> ARROW_TYPE = EntityDataManager.createKey(EntityGreatarrow.class, DataSerializers.ITEM_STACK);

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
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ARROW_TYPE, new ItemStack(ModItems.greatarrow));
    }

    public void setArrowStack(ItemStack stack)
    {
        stack = stack.copy();
        stack.setCount(1); //Bows tend to only fire one arrow.
        dataManager.set(ARROW_TYPE, stack);
    }

    @Override
    protected ItemStack getArrowStack() {
        return dataManager.get(ARROW_TYPE);
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

        ItemStack arrowstack = getArrowStack();
        ItemGreatarrow arrowtype = ModItems.greatarrow;
        if(!arrowstack.isEmpty() && arrowstack.getItem() instanceof ItemGreatarrow) //I don't trust people like you.
            arrowtype = (ItemGreatarrow) arrowstack.getItem();
        Entity entity = raytraceResultIn.entityHit;

        if(entity == null)
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

            this.posX -= (float)((this.posX + this.motionX) - raytraceResultIn.hitVec.x);
            this.posY -= (float)((this.posY + this.motionY) - raytraceResultIn.hitVec.y);
            this.posZ -= (float)((this.posZ + this.motionZ) - raytraceResultIn.hitVec.z);
            if(!impactSoundPlayed) {
                this.playSound(SoundEvents.ENTITY_IRONGOLEM_DEATH, 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                impactSoundPlayed = true;
            }

            arrowtype.hitBlock(this,blockpos,blockstate,!isnormalhit);
        }
        else
        {
            arrowtype.hitEntity(this,entity);
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