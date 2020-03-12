package betterwithaddons.handler;

// Original Copyright Notice provided as required by the License.
// ==========================================================================
// Copyright (C)2013 by Aaron Suen <warr1024@gmail.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// ---------------------------------------------------------------------------

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.*;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalCrossbreedHandler {
    private static ArrayList<AnimalMutation> mutationSet = new ArrayList<>();
    static Field inLove;

    public static void spawnEntityAt(World world, Vec3d pos, Entity entity)
    {
        entity.setPosition(pos.x,pos.y,pos.z);
        world.spawnEntity(entity);
    }

    public static void spawnBabyAt(World world, Vec3d pos, EntityAgeable entity)
    {
        entity.setPosition(pos.x,pos.y,pos.z);
        entity.setGrowingAge(-24000);
        world.spawnEntity(entity);
    }

    public static int getSquidWeight(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father)
    {
        BlockPos blockpos = new BlockPos(pos);
        IBlockState state = world.getBlockState(blockpos);

        return state.getMaterial() == Material.WATER ? 50 : 0; //TODO: habitat check
    }

    public static int getWolfWeight(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father)
    {
        BlockPos blockpos = new BlockPos(pos);
        Biome biome = world.getBiome(blockpos);

        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.CONIFEROUS) ? 50 : 0; //TODO: habitat check
    }

    public static int getOcelotWeight(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father)
    {
        BlockPos blockpos = new BlockPos(pos);
        Biome biome = world.getBiome(blockpos);

        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) ? 50 : 0; //TODO: habitat check
    }

    public static void initialize()
    {

        inLove = ObfuscationReflectionHelper.findField(EntityAnimal.class,"field_70881_d");

        //Standard
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityCow(world)),100));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntitySheep(world)),100));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityPig(world)),100));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityChicken(world)),50)); //Lower chance for egg hatching animal
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityRabbit(world)),75));

        //Parent species
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,mother.createChild(mother)),100));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,father.createChild(father)),100));

        //Abominations
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnEntityAt(world,pos,new EntitySilverfish(world)),200));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnEntityAt(world,pos,new EntityCaveSpider(world)),100));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnEntityAt(world,pos,new EntitySlime(world)),50));

        //Enviroment dependent
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnEntityAt(world,pos,new EntitySquid(world)),0).setCustomWeight(AnimalCrossbreedHandler::getSquidWeight));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityWolf(world)),0).setCustomWeight(AnimalCrossbreedHandler::getWolfWeight));
        mutationSet.add(new AnimalMutation((world, pos, mother, father) -> spawnBabyAt(world,pos,new EntityOcelot(world)),0).setCustomWeight(AnimalCrossbreedHandler::getOcelotWeight));
    }

    public interface IAnimalSpawner {
        void spawn(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father);
    }

    public interface IAnimalWeight {
        int getCustomWeight(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father);
    }

    public static class AnimalMutation extends WeightedRandom.Item {
        IAnimalSpawner spawner;
        IAnimalWeight weight;

        public AnimalMutation(IAnimalSpawner spawner,int itemWeightIn) {
            super(itemWeightIn);
            this.spawner = spawner;
        }

        public AnimalMutation copy()
        {
            return new AnimalMutation(spawner, itemWeight).setCustomWeight(weight);
        }

        public AnimalMutation setCustomWeight(IAnimalWeight weight)
        {
            this.weight = weight;
            return this;
        }

        public AnimalMutation updateWeight(World world, Vec3d pos, EntityAnimal mother, EntityAnimal father)
        {
            if(weight != null)
                itemWeight = weight.getCustomWeight(world,pos,mother,father);
            return this;
        }
    }

    @SubscribeEvent
    public void crossBreedAnimal(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;
        BlockPos pos = entity.getPosition();

        try {
            if (!world.isRemote && entity instanceof EntityAnimal) {
                EntityAnimal animal = (EntityAnimal) entity;

                if(!animal.isInLove())
                    return;

                if (world.getLight(pos) >= 8 || (int)inLove.get(animal) > 20)
                    return;

                AxisAlignedBB aabb = new AxisAlignedBB(pos).expand(8,8,8); //Instead of expanding entity bounding box, expand block bounding box because it makes more sense this way probably
                List<EntityAnimal> others = world.getEntitiesWithinAABB(EntityAnimal.class,aabb);
                EntityAnimal found = null;
                for (EntityAnimal other: others) {
                    if(other == animal || !other.isInLove())
                        continue;

                    if(isSameSpecies(animal,other))
                        return;

                    if(animal.getPositionVector().squareDistanceTo(other.getPositionVector()) > 3.5 * 3.5)
                        continue;

                    if(!isNotSitting(other) || world.getLight(other.getPosition()) >= 8)
                        continue;

                    found = other;
                    break;
                }

                if(found == null)
                    return;

                Vec3d spawnPos = new Vec3d((found.posX + animal.posX) / 2,(found.posY + animal.posY) / 2,(found.posZ + animal.posZ) / 2);
                BlockPos spawnBlockPos = new BlockPos(spawnPos);

                //TODO: Habitat check

                final EntityAnimal finalFound = found;
                ArrayList<AnimalMutation> mutations = mutationSet.stream().map(x -> x.copy().updateWeight(world,spawnPos,finalFound,animal)).collect(Collectors.toCollection(ArrayList::new));

                AnimalMutation mutation = WeightedRandom.getRandomItem(world.rand,mutations);
                if(mutation != null)
                    mutation.spawner.spawn(world,spawnPos,found,animal);

                animal.resetInLove();
                animal.setGrowingAge(6000);
                found.resetInLove();
                found.setGrowingAge(6000);

                animal.playLivingSound();
                found.playLivingSound();

                animal.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,6000));
                found.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,6000));
            }
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private boolean isSameSpecies(EntityAnimal animalA, EntityAnimal animalB)
    {
        return animalA.getClass().isInstance(animalB) || animalB.getClass().isInstance(animalA);
    }

    private boolean isNotSitting(EntityAnimal animal)
    {
        if(animal instanceof EntityTameable)
        {
            EntityTameable tameable = (EntityTameable) animal;
            return tameable.isTamed() && !tameable.isSitting();
        }

        return true;
    }
}
