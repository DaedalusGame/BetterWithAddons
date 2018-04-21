package betterwithaddons.handler;

import betterwithaddons.item.ModItems;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

//Copyright (c) 2016,2017 Ferreus Veritas
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.

public class BurnHandler implements IWorldEventListener {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event)
    {
        event.getWorld().addEventListener(new BurnHandler());
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        if(!worldIn.isRemote && newState.getBlock() instanceof BlockFire) {
            if(oldState.getBlock() instanceof BlockLog && oldState.getMaterial() == Material.WOOD) {
                if(worldIn.getGameRules().getBoolean("doTileDrops")&& !worldIn.restoringBlockSnapshots) {
                    double xOffset = worldIn.rand.nextDouble() * 0.5 + 0.25D;
                    double yOffset = worldIn.rand.nextDouble() * 0.5 + 0.25D;
                    double zOffset = worldIn.rand.nextDouble() * 0.5 + 0.25D;
                    EntityItem entityitem = new EntityItem(worldIn, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, ModItems.MATERIAL_TWEAK.getMaterial("ash"));
                    entityitem.setDefaultPickupDelay();
                    entityitem.attackEntityFrom(DamageSource.IN_FIRE, -10000); //Make it immune to fire so it doesn't instantly burn
                    worldIn.spawnEntity(entityitem);
                }
            }
        }
    }

    @Override
    public void notifyLightSet(BlockPos pos) {

    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {

    }

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {

    }

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {

    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {

    }

    @Override
    public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) {

    }

    @Override
    public void onEntityAdded(Entity entityIn) {

    }

    @Override
    public void onEntityRemoved(Entity entityIn) {

    }

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {

    }

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {

    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }
}
