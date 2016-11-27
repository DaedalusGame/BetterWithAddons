package betterwithaddons.tileentity;

import betterwithaddons.block.BlockLureTree;
import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityLureTree extends TileEntityBase implements ITickable {
    int currentFood = 0;
    int chargeTicks = 0;
    private static ArrayList<Class> BLACKLIST = new ArrayList<>();

    public static void addBlacklistEntry(Class entity)
    {
        BLACKLIST.add(entity);
    }

    @Override
    public void update() {
        if(!isDead() && currentFood > 0) {
            if(chargeTicks < InteractionBWA.MAXCHARGE)
            {
                chargeTicks++;
                currentFood--;
            }
            else
            {
                if(!worldObj.isRemote) {
                    if(attractAnimals(worldObj.rand.nextInt(2) + 1))
                        chargeTicks = 0;
                }
                else
                {
                    chargeTicks = 0;
                }
            }
        }
    }

    public boolean attractAnimals(int amt)
    {
        Random random = worldObj.rand;
        Biome biome = worldObj.getBiome(pos);
        int spawned = 0;
        if(biome != null)
        {
            List<Biome.SpawnListEntry> spawnable = biome.getSpawnableList(EnumCreatureType.CREATURE);

            if(!spawnable.isEmpty())
                for (int i = 0; i < amt; i++)
                {
                    Biome.SpawnListEntry entry = WeightedRandom.getRandomItem(random,spawnable);

                    int x = pos.getX() + random.nextInt(InteractionBWA.RADIUS*2+1) - InteractionBWA.RADIUS;
                    int z = pos.getZ() + random.nextInt(InteractionBWA.RADIUS*2+1) - InteractionBWA.RADIUS;

                    if(!BLACKLIST.contains(entry.entityClass))
                    {
                        for (int y = pos.getY(); y > pos.getY() - 4; y--)
                        {
                            BlockPos pos = new BlockPos(x,y,z);
                            if(worldObj.isAirBlock(pos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND,worldObj,pos))
                            {
                                EntityLiving entityliving;

                                try
                                {
                                    entityliving = entry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldObj});
                                }
                                catch (Exception exception)
                                {
                                    exception.printStackTrace();
                                    continue;
                                }

                                entityliving.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
                                worldObj.spawnEntityInWorld(entityliving);

                                if (entityliving != null)
                                {
                                    entityliving.spawnExplosionParticle();
                                    spawned++;
                                }

                                break;
                            }
                            else if(worldObj.isSideSolid(pos, EnumFacing.UP)) {
                                break;
                            }
                        }
                    }
                }
        }

        return spawned > 0;
    }

    public boolean isDead()
    {
        if(worldObj == null) return true;
        IBlockState state = worldObj.getBlockState(pos);
        if(state.getBlock() instanceof BlockLureTree && !state.getValue(BlockLureTree.ACTIVE))
            return true;

        return false;
    }

    public boolean feed(ItemStack stack) {
        if(stack.getItem() == Items.GLOWSTONE_DUST && currentFood < InteractionBWA.MAXFOOD - InteractionBWA.FOODGLOWSTONE)
        {
            currentFood = Math.min(InteractionBWA.MAXFOOD,currentFood+ InteractionBWA.FOODGLOWSTONE);
            return true;
        }

        return false;
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing facing) {
        if(isDead())
            return false;

        ItemStack insertstack = heldItem.copy();
        insertstack.stackSize = 1;
        boolean flag = feed(insertstack);
        IBlockState state = worldObj.getBlockState(pos);

        if (flag && state.getValue(BlockLureTree.FACING) == facing) {
            if (!playerIn.isCreative()) {
                heldItem.stackSize -= 1;
                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.stackSize == 0 ? null : heldItem);
            }
            this.worldObj.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
                    ((worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 0.5F);
            return true;
        }

        return false;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("chargeticks",chargeTicks);
        compound.setInteger("food",currentFood);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        currentFood = compound.getInteger("food");
        chargeTicks = compound.getInteger("chargeticks");
    }
}
