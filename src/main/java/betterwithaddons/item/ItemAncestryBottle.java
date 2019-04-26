package betterwithaddons.item;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.entity.EntityAncestryBottle;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.tileentity.TileEntityAncestrySand;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAncestryBottle extends Item {
    public ItemAncestryBottle() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
                return new EntityAncestryBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }

            @Override
            protected float getProjectileInaccuracy()
            {
                return super.getProjectileInaccuracy() * 0.5F;
            }

            @Override
            protected float getProjectileVelocity()
            {
                return super.getProjectileVelocity() * 1.25F;
            }
        });
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        ItemStack stack = player.getHeldItem(hand);

        if(state.getBlock() == Blocks.SOUL_SAND)
        {
            worldIn.setBlockState(pos, ModBlocks.ANCESTRY_SAND.getDefaultState());
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityAncestrySand)
                ((TileEntityAncestrySand) te).addSpirits(InteractionEriottoMod.BOTTLE_MAX_SPIRITS);
            stack.shrink(1);
            if (stack.isEmpty())
                player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)))
                player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
            return EnumActionResult.SUCCESS;
        }
        else if(state.getBlock() == ModBlocks.ANCESTRY_SAND)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityAncestrySand)
                ((TileEntityAncestrySand) te).addSpirits(InteractionEriottoMod.BOTTLE_MAX_SPIRITS);
            stack.shrink(1);
            if (stack.isEmpty())
                player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
            else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE)))
                player.dropItem(new ItemStack(Items.GLASS_BOTTLE), false);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntityAncestryBottle entityexpbottle = new EntityAncestryBottle(worldIn, playerIn);
            entityexpbottle.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.7F, 1.0F);
            worldIn.spawnEntity(entityexpbottle);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
}
