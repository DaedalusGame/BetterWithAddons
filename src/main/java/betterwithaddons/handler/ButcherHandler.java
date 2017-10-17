package betterwithaddons.handler;

import betterwithaddons.util.EntityUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ButcherHandler {
    @SubscribeEvent
    public void playerTick(LivingAttackEvent attackEvent)
    {
        Entity target = attackEvent.getEntity();
        Entity source = attackEvent.getSource().getImmediateSource();
        if(source instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) source;
            if (target == null)
                return;
            World world = attacker.getEntityWorld();
            BlockPos pos = EntityUtil.getEntityFloor(target, 2);

            if (!world.isRemote) {
                IBlockState state = world.getBlockState(pos);
                if (isChopBlock(state) && isSuitableWeapon(attacker.getHeldItemMainhand())) {
                    attacker.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 200));
                    attacker.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 200));
                    splatter(world, pos, 1);
                }
            }
        }
    }

    private boolean isSuitableWeapon(ItemStack stack)
    {
        if(stack.isEmpty()) return false;

        Item item = stack.getItem();
        return item.getToolClasses(stack).contains("axe") || item instanceof ItemSword || item instanceof ItemAxe;
    }

    private boolean isChopBlock(IBlockState state)
    {
        return state.getBlock() == BWMBlocks.AESTHETIC && (state.getValue(BlockAesthetic.TYPE) == BlockAesthetic.EnumType.CHOPBLOCK || state.getValue(BlockAesthetic.TYPE) == BlockAesthetic.EnumType.CHOPBLOCKBLOOD);
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
                world.setBlockState(splatterpos,BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.TYPE, BlockAesthetic.EnumType.CHOPBLOCKBLOOD));
        }
    }
}
