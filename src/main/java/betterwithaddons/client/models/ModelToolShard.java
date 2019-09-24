package betterwithaddons.client.models;

import betterwithaddons.client.ToolShardModelHandler;
import betterwithaddons.client.ToolShardOverrideHandler;
import betterwithaddons.lib.Reference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModelToolShard implements IModel {
    public static ModelToolShard MODEL = new ModelToolShard();

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = PerspectiveMapWrapper.getTransforms(state);
        ToolShardModelHandler.STATE = state;
        return new BakedToolShard(map);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public enum LoaderToolShard implements ICustomModelLoader {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation) {
            return modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.getResourcePath().contains("tool_shard_custom");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager) {

        }
    }

    private static final class BakedToolShard implements IBakedModel {
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final ItemOverrideList overrideList;
        //private ImmutableList<BakedQuad> quads;

        public BakedToolShard(ImmutableMap<TransformType, TRSRTransformation> transforms) {
            this.transforms = Maps.immutableEnumMap(transforms);
            this.overrideList = new ToolShardOverrideHandler();
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return overrideList;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
        {
            return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType);
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
        {
            //if(side == null) return quads;
            return ImmutableList.of();
        }

        public boolean isAmbientOcclusion() { return true;  }
        public boolean isGui3d() { return false; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() { return null; }
    }
}
