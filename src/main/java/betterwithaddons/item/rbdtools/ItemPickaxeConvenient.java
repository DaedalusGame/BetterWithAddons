package betterwithaddons.item.rbdtools;

import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

import java.util.List;

import static net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel;
import static net.minecraft.stats.StatList.getBlockStats;

public class ItemPickaxeConvenient extends ItemPickaxe implements IConvenientTool {
    public ItemPickaxeConvenient(ToolMaterial material) {
        super(material);
        this.setMaxDamage((int)(material.getMaxUses() * 1.1f));
    }

    @Override
    public boolean canShear(ItemStack stack, IBlockState state) {return false;}

    @Override
    public boolean canCollect(ItemStack stack, IBlockState state) {return false;}

    @Override
    public  boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {return false;}

    @Override
    public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
        return false;
    }

    @Override
    public float getEfficiency(ItemStack stack, IBlockState state) {return  1.0f;}

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        if(canHarvestEfficiently(stack,state))
            return true;

        return super.canHarvestBlock(state, stack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        return super.getStrVsBlock(stack, state) * getEfficiency(stack,state);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack handstack = player.getHeldItem(hand);
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(canPlace(stack,player,world,pos,hand,facing,hitX,hitY,hitZ)) {
                player.inventory.setInventorySlotContents(i,handstack);
                player.setHeldItem(hand,stack);
                EnumActionResult result = stack.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
                player.inventory.setInventorySlotContents(i,player.getHeldItem(hand));
                player.setHeldItem(hand,handstack);
                return result;
            }
        }

        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player)
    {
        if (player.world.isRemote || player.capabilities.isCreativeMode)
        {
            return false;
        }
        IBlockState state = player.world.getBlockState(pos);
        Block block = state.getBlock();
        if (canShear(itemstack,state) && block instanceof IShearable)
        {
            IShearable target = (IShearable)block;
            if (target.isShearable(itemstack, player.world, pos))
            {
                List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
                        getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));

                for (ItemStack stack : drops)
                {
                    InventoryUtil.addItemToPlayer(player,stack);
                }

                itemstack.damageItem(1, player);
                player.addStat(getBlockStats(block));
                player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11); //TODO: Move to IShearable implementors in 1.12+
                return true;
            }
        }
        return false;
    }
}
