package betterwithaddons.crafting.recipes;

import betterwithaddons.item.ModItems;
import betterwithmods.common.registry.crafting.ToolDamageRecipe;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.tuple.Pair;

public class ThreshingRecipe extends ToolDamageRecipe {
    public ThreshingRecipe(ResourceLocation group, ItemStack result, Ingredient input) {
        super(group, result, input, ThreshingRecipe::isShovel);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.BLOCK_GRASS_STEP;
    }

    @Override
    public Pair<Float, Float> getSoundValues() { return Pair.of(0.25F, 2.5F); }

    @Override
    public ItemStack getExampleStack() {
        return new ItemStack(Items.IRON_SHOVEL);
    }

    private static boolean isShovel(ItemStack stack) {
        if (stack != null) {
            if (stack.getItem().getToolClasses(stack).contains("shovel")) {
                if (stack.getItem().getRegistryName().getResourceDomain().equals("tconstruct")) {
                    if (stack.getItemDamage() >= stack.getMaxDamage())
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void dropExtra(PlayerEvent.ItemCraftedEvent event) {
        if (event.player == null)
            return;
        if (isMatch(event.craftMatrix, event.player.world)) {
            if (!event.player.getEntityWorld().isRemote) {
                event.player.entityDropItem(ModItems.MATERIAL_WHEAT.getMaterial("hay"), 0);
            }
        }
    }
}
