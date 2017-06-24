package betterwithaddons.client;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.client.models.ModelToolShardInner;
import betterwithaddons.interaction.InteractionBWA;
import betterwithaddons.interaction.InteractionBWM;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.ItemUtil;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ToolShardModelHandler {
    Method getVariantNames;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void textureStitch(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(Reference.MOD_ID, "items/breakmask"));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onModelBake(ModelBakeEvent event)
    {
        if(!InteractionBWA.ARMOR_SHARD_RENDER)
            return;

        for (Item item : Item.REGISTRY) {
            if (!ItemUtil.isTool(item)) continue;
            for (String s : getVariantNames(event.getModelLoader(), item)) {
                ResourceLocation file = getItemLocation(s);
                ModelResourceLocation memory = ModelLoader.getInventoryVariant(s);
                IModel model = null;
                try {
                    model = ModelLoaderRegistry.getModel(file);
                } catch (Exception e) {
                    try {
                        model = ModelLoaderRegistry.getModel(memory);
                    } catch (Exception e1) {
                        //e1.printStackTrace();
                    }
                }
                if(model == null) continue;
                IModel brokenmodel = new ModelToolShardInner(ImmutableList.copyOf(model.getTextures()));
                IBakedModel bakedbrokenmodel = brokenmodel.bake(brokenmodel.getDefaultState(), DefaultVertexFormats.ITEM, location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
                ToolShardOverrideHandler.INSTANCE.addModel(item,bakedbrokenmodel);
            }
        }
    }



    protected List<String> getVariantNames(ModelLoader loader, Item item)
    {
        if(getVariantNames == null)
            getVariantNames = ReflectionHelper.findMethod(ModelBakery.class, "getVariantNames", "func_177596_a", Item.class);

        try {
            return (List<String>) getVariantNames.invoke(loader,item);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    protected ResourceLocation getItemLocation(String location)
    {
        ResourceLocation resourcelocation = new ResourceLocation(location.replaceAll("#.*", ""));
        return new ResourceLocation(resourcelocation.getResourceDomain(), "item/" + resourcelocation.getResourcePath());
    }
}
