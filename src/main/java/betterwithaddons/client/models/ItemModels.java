package betterwithaddons.client.models;

import betterwithaddons.util.IHasVariants;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.List;

public class ItemModels
{
    public static void register()
    {
        for (Block block: ModBlocks.LIST) {
            registerBlock(block);
        }

        for (Item item: ModItems.LIST) {
            registerItem(item);
        }
    }

    private static void registerBlock(Block block)
    {
        if(block instanceof IHasVariants)
        {
            List<ModelResourceLocation> variants = ((IHasVariants) block).getVariantModels();

            for(int i = 0; i < variants.size(); i++)
                if(variants.get(i) != null)
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, variants.get(i));
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
    }

    private static void registerItem(Item item)
    {
        if(item instanceof IHasVariants)
        {
            List<ModelResourceLocation> variants = ((IHasVariants) item).getVariantModels();

            for(int i = 0; i < variants.size(); i++)
                if(variants.get(i) != null)
                    ModelLoader.setCustomModelResourceLocation(item, i, variants.get(i));
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

    }
}