package betterwithaddons.handler;

import betterwithaddons.util.EntityUtil;
import betterwithmods.BWMBlocks;
import betterwithmods.blocks.BlockAesthetic;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ButcherHandler {
    @SubscribeEvent
    public void playerTick(AttackEntityEvent attackEvent)
    {
        Entity target = attackEvent.getTarget();
        EntityLivingBase attacker = attackEvent.getEntityLiving();
        if(attacker == null || target == null)
            return;
        World world = attacker.getEntityWorld();
        BlockPos pos = EntityUtil.getEntityFloor(attacker,2);

        if(!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            if(isChopBlock(state))
            {
                attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH,10));
                attacker.addPotionEffect(new PotionEffect(MobEffects.HUNGER,10));
                splatter(world,pos,1);
            }
        }
    }

    private boolean isSuitableWeapon(ItemStack stack)
    {
        return true;
    }

    private boolean isChopBlock(IBlockState state)
    {
        return state.getBlock() == BWMBlocks.AESTHETIC && (state.getValue(BlockAesthetic.blockType) == BlockAesthetic.EnumType.CHOPBLOCK || state.getValue(BlockAesthetic.blockType) == BlockAesthetic.EnumType.CHOPBLOCKBLOOD);
    }

    private void splatter(World world, BlockPos pos, int radius)
    {
        Random rand = world.rand;
        for (int x = -radius; x <= radius; x++)
        for (int y = -radius; y <= radius; y++)
        for (int z = -radius; z <= radius; z++)
        {
            BlockPos splatterpos = pos.add(x,y,z);
            if(rand.nextInt(3) == 0 && isChopBlock(world.getBlockState(splatterpos)))
                world.setBlockState(splatterpos,BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.blockType, BlockAesthetic.EnumType.CHOPBLOCKBLOOD));
        }
    }
}
