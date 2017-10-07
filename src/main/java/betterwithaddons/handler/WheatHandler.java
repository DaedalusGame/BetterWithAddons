package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionWheat;
import betterwithaddons.item.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
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
                iterator.set(ModItems.materialWheat.getMaterial("hay",stack.getCount()));
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
                    event.setNewSpeed(event.getNewSpeed() * material.getEfficiencyOnProperMaterial());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
