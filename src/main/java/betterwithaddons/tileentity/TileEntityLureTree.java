package betterwithaddons.tileentity;

import betterwithaddons.block.BlockLureTree;
import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
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
    private static ArrayList<TreeFood> TREE_FOODS = new ArrayList<>();

    public static ArrayList<TreeFood> getTreeFoods()
    {
        return TREE_FOODS;
    }

    public static class TreeFood
    {
        public ItemStack stack;
        public int amount;

        public TreeFood(ItemStack stack, int amount) {
            this.stack = stack;
            this.amount = amount;
        }

        public boolean matches(ItemStack stack)
        {
            return ItemStack.areItemsEqual(this.stack,stack);
        }
    }

    public static void addTreeFood(TreeFood recipe) {
        TREE_FOODS.add(recipe);
    }

    public static void addTreeFood(ItemStack stack, int amount)
    {
        TREE_FOODS.add(new TreeFood(stack,amount));
    }

    public static TreeFood getTreeFood(ItemStack stack)
    {
        for (TreeFood food : TREE_FOODS) {
            if (food.matches(stack)) {
                return food;
            }
        }

        return null;
    }

    int currentFood = 0;
    int chargeTicks = 0;
    private static ArrayList<Class<? extends Entity>> BLACKLIST = new ArrayList<>();

    public static void addBlacklistEntry(Class<? extends Entity> entity)
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
                if(!world.isRemote) {
                    if(attractAnimals(world.rand.nextInt(2) + 1))
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
        Random random = world.rand;
        Biome biome = world.getBiome(pos);
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
                            if(world.isAirBlock(pos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND,world,pos))
                            {
                                EntityLiving entityliving;

                                try
                                {
                                    entityliving = entry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
                                }
                                catch (Exception exception)
                                {
                                    exception.printStackTrace();
                                    continue;
                                }

                                entityliving.setLocationAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
                                world.spawnEntity(entityliving);

                                if (entityliving != null)
                                {
                                    entityliving.spawnExplosionParticle();
                                    spawned++;
                                }

                                break;
                            }
                            else if(world.isSideSolid(pos, EnumFacing.UP)) {
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
        if(world == null) return true;
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockLureTree && !state.getValue(BlockLureTree.ACTIVE))
            return true;

        return false;
    }

    public boolean feed(ItemStack stack) {
        TreeFood food = getTreeFood(stack);

        if(food != null && currentFood < InteractionBWA.MAXFOOD - food.amount)
        {
            currentFood = Math.min(InteractionBWA.MAXFOOD,currentFood+ food.amount);
            return true;
        }

        return false;
    }

    public boolean onActivated(EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing facing) {
        if(isDead())
            return false;

        ItemStack insertstack = heldItem.copy();
        insertstack.setCount(1);
        boolean flag = feed(insertstack);
        IBlockState state = world.getBlockState(pos);

        if (flag && state.getValue(BlockLureTree.FACING) == facing) {
            if (!playerIn.isCreative()) {
                heldItem.shrink(1);
                playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem.getCount() == 0 ? null : heldItem);
            }
            this.world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F,
                    ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 0.5F);
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
