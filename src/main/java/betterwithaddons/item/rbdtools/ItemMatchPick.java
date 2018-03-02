package betterwithaddons.item.rbdtools;

import betterwithaddons.util.ItemUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMatchPick extends ItemPickaxeConvenient {
    public ItemMatchPick(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return ItemUtil.matchesOreDict(stack, "torch");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumActionResult result = EnumActionResult.PASS;
        boolean sneaking = player.isSneaking();
        if(!sneaking)
            result = super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        if(sneaking || result != EnumActionResult.SUCCESS)
            return igniteBlock(player,world,pos,facing,player.getHeldItem(hand));
        return result;
    }

    private EnumActionResult igniteBlock(EntityPlayer player, World world, BlockPos pos, EnumFacing facing, ItemStack itemstack)
    {
        pos = pos.offset(facing);
        if (!player.canPlayerEdit(pos, facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            if (world.isAirBlock(pos))
            {
                world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
            }

            if (player instanceof EntityPlayerMP)
            {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
            }

            itemstack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
    }
}
