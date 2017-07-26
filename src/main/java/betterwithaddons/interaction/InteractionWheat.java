package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.registry.bulk.manager.MillManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class InteractionWheat extends Interaction {
    public static boolean ENABLED = false;

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public void preInit() {
        Items.WHEAT_SEEDS.setUnlocalizedName("seed_grain"); //Keep compatible with Botania's stupid way of checking for seeds >_>
        ModItems.materialBag.subItemUnlocalizedNames[0] = "grain"; //Very ugly but y'know
    }

    @Override
    void init() {
        MillManager.getInstance().removeRecipe(new ItemStack(BWMBlocks.RAW_PASTRY,1, BlockRawPastry.EnumType.BREAD.getMetadata()),ItemStack.EMPTY);
        MillManager.getInstance().addRecipe(0,new ItemStack(BWMBlocks.RAW_PASTRY,1, BlockRawPastry.EnumType.BREAD.getMetadata()),ItemStack.EMPTY,new Object[]{new ItemStack(Items.WHEAT_SEEDS)});
    }

    @Override
    void preInitEnd() {
        BetterWithAddons.proxy.overrideItemModel(Items.WHEAT_SEEDS,0,new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID,"override/seed_wheat"),"inventory"));
        BetterWithAddons.proxy.overrideItemModel(Items.WHEAT,0,new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID,"override/wheat"),"inventory"));
        BetterWithAddons.proxy.overrideItemModel(ModItems.materialBag,0,new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID,"override/bag_seed"),"inventory"));
    }
}
