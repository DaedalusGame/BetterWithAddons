package betterwithaddons.client.models;

import betterwithaddons.lib.Reference;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;

public class ModelRopeSideways implements IModel {
    public static final ModelRopeSideways INSTANCE = new ModelRopeSideways();
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of(new ResourceLocation(Reference.MOD_ID,"block/rope_bridge"));
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections.emptySet();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedModelRopeSideways();
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public static class Loader implements ICustomModelLoader {
        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.getResourcePath().equals("rope_bridge");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws Exception {
            return INSTANCE;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {

        }
    }
}
