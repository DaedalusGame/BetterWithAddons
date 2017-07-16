package betterwithaddons.item.rbdtools;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemToolConvenient extends ItemTool {
    IEffectiveBlock effectiveBlockMatcher;
    ICanPlace itemUseMatcher;
    boolean instantCollect;
    int damagePerAttack = 2;

    public ItemToolConvenient(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
        effectiveBlockMatcher = (stack, state) -> false;
        itemUseMatcher = (stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ) -> false;
        this.setMaxDamage((int)(materialIn.getMaxUses() * 1.1));
    }

    public ItemToolConvenient setEffectiveBlocks(IEffectiveBlock effectiveBlockMatcher) {
        this.effectiveBlockMatcher = effectiveBlockMatcher;
        return this;
    }

    public ItemToolConvenient setItemUse(ICanPlace itemUseMatcher) {
        this.itemUseMatcher = itemUseMatcher;
        return this;
    }

    public ItemToolConvenient setInstantCollect(boolean instantCollect) {
        this.instantCollect = instantCollect;
        return this;
    }

    public ItemToolConvenient setDamagePerAttack(int damagePerAttack) {
        this.damagePerAttack = damagePerAttack;
        return this;
    }

    public boolean isEffectiveAgainst(ItemStack stack, IBlockState state)
    {
        return effectiveBlockMatcher.isEffective(stack,state);
    }

    public boolean canInstantlyCollect()
    {
        return instantCollect;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(damagePerAttack, attacker);
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if(isEffectiveAgainst(stack,state))
            return efficiencyOnProperMaterial * 2.5f;

        return super.getStrVsBlock(stack, state);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handstack = player.getHeldItem(hand);
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(itemUseMatcher.canPlace(stack,player,worldIn,pos,hand,facing,hitX,hitY,hitZ))
                return stack.onItemUse(player,worldIn,pos,hand,facing,hitX,hitY,hitZ);
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
