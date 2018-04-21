package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionWheat;
import betterwithaddons.item.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class WheatHandler {
    static Field toolMaterialField;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void harvestBlock(BlockEvent.HarvestDropsEvent event)
    {
        if(!InteractionWheat.REPLACE_WHEAT_DROPS)
            return;

        IBlockState state = event.getState();
        if(state.getBlock() != Blocks.WHEAT)
            return;

        List<ItemStack> drops = event.getDrops();
        if(drops instanceof ImmutableList)
            return; //Give up immediately, there's a mod that blocks all the fun.

        ListIterator<ItemStack> iterator = drops.listIterator();
        while(iterator.hasNext())
        {
            ItemStack stack = iterator.next();
            if(stack.getItem() == Items.WHEAT)
                iterator.set(ModItems.MATERIAL_WHEAT.getMaterial("hay",stack.getCount()));
            if(stack.getItem() == Items.WHEAT_SEEDS)
                iterator.set(new ItemStack(Items.WHEAT,stack.getCount()));
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed event)
    {
        if(!InteractionWheat.DIG_UP_CROPS)
            return;

        if(toolMaterialField == null)
            toolMaterialField = ReflectionHelper.findField(ItemHoe.class,"field_77843_a","toolMaterial");

        EntityPlayer player = event.getEntityPlayer();

        if(player == null)
            return;

        IBlockState state = event.getState();
        Block block = state.getBlock();
        ItemStack tool = player.getHeldItemMainhand();

        if(tool.getItem() instanceof ItemHoe && "hoe".equals(block.getHarvestTool(state)))
        {
            try {
                Item.ToolMaterial material = (Item.ToolMaterial) toolMaterialField.get(tool.getItem());
                if(material != null)
                    event.setNewSpeed(event.getNewSpeed() * material.getEfficiency());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void villagerFix(LivingEvent.LivingUpdateEvent event)
    {
        if (!InteractionWheat.REPLACE_WHEAT_DROPS)
            return;
        if (event.getEntityLiving().getEntityWorld().isRemote)
            return;
        if (event.getEntityLiving() instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) event.getEntityLiving();
            InventoryBasic inventory = villager.getVillagerInventory();
            ArrayList<ItemStack> itemstacks_created = new ArrayList<>();

            for (int i = 0; i < inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = inventory.getStackInSlot(i);

                if (!itemstack.isEmpty())
                {
                    Item item = itemstack.getItem();

                    if (item == Items.WHEAT && itemstack.getCount() >= 6)
                    {
                        int wheat_consumed = itemstack.getCount() / 2 / 3 * 3;
                        int bread_produced = wheat_consumed / 2;
                        int seeds_produced = wheat_consumed / 2;
                        itemstack.shrink(wheat_consumed);
                        itemstacks_created.add(new ItemStack(Items.BREAD, bread_produced, 0));
                        itemstacks_created.add(new ItemStack(Items.WHEAT_SEEDS, seeds_produced, 0));
                        if(InteractionWheat.THRESH_WHEAT)
                            itemstacks_created.add(ModItems.MATERIAL_WHEAT.getMaterial("hay",wheat_consumed));
                    }

                    if (itemstack.isEmpty())
                    {
                        inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                }

                for(ItemStack stack : itemstacks_created)
                {
                    double y = villager.posY - 0.3 + (double)villager.getEyeHeight();
                    EntityItem entityitem = new EntityItem(villager.world, villager.posX, y, villager.posZ, stack);
                    entityitem.setDefaultPickupDelay();
                    villager.world.spawnEntity(entityitem);
                }
            }
        }
    }
}
