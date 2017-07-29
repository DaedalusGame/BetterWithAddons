package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class EffectTarred extends PotionBase
{
	static final HashSet<EntityLivingBase> tarredBurningEntities = new HashSet<>();

	public EffectTarred()
	{
		super("tarred", true, new Color(64,45,55).getRGB());
		
		this.setPotionName("Tarred");
		this.setIconIndex(1,0);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration % 10 == 0;
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_) {
		if(entityLivingBaseIn.isBurning())
			tarredBurningEntities.add(entityLivingBaseIn);
		else if(tarredBurningEntities.contains(entityLivingBaseIn)) {
			entityLivingBaseIn.setFire(10);
			entityLivingBaseIn.attackEntityFrom(DamageSource.ON_FIRE,1.0f);
			if(entityLivingBaseIn.isInWater())
			{
				spawnBubbles(entityLivingBaseIn,16);
			}
		}
	}

	public void spawnBubbles(EntityLivingBase living, int amt)
	{
		World world = living.world;
		if(world != null) {
			Random random = world.rand;
			for (int i = 0; i < amt; i++) {
				AxisAlignedBB aabb = living.getRenderBoundingBox();
				double partX = aabb.minX + random.nextDouble() * (aabb.maxX - aabb.minX);
				double partY = aabb.minY + random.nextDouble() * (aabb.maxY - aabb.minY);
				double partZ = aabb.minZ + random.nextDouble() * (aabb.maxZ - aabb.minZ);
				BlockPos blockPos = new BlockPos(partX,partY,partZ);
				if(world.getBlockState(blockPos).getMaterial() == Material.WATER) {
					world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, partX, partY, partZ, 0.0, 0.0, 0.0);
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, partX, partY, partZ, 0.0, 0.0, 0.0);
				}
			}
		}
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		if(tarredBurningEntities.contains(entityLivingBaseIn))
			tarredBurningEntities.remove(entityLivingBaseIn);
	}
}
