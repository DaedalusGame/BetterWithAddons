package betterwithaddons.client.models;

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
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ModelToolShard implements IModel {
    public static final IModel MODEL = new ModelToolShard();

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedToolShard();
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    public enum LoaderToolShard implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public boolean accepts(ResourceLocation modelLocation)
        {
            return (modelLocation.getResourceDomain().equals(Reference.MOD_ID) && (modelLocation.getResourcePath().contains("tool_shard")));
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation)
        {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {

        }
    }

    private static final class BakedToolShard implements IBakedModel
    {
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        //private ImmutableList<BakedQuad> quads;

        public BakedToolShard()
        {
            ImmutableMap.Builder<TransformType, TRSRTransformation> builder = ImmutableMap.builder();
            builder.put(TransformType.GROUND, new TRSRTransformation(new Vector3f(0.25f, 0.375f, 0.25f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f), new Vector3f(0.5f, 0.5f, 0.5f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.HEAD, new TRSRTransformation(new Vector3f(1.0f, 0.8125f, 1.4375f), new Quat4f(0.0f, 1.0f, 0.0f, -4.371139E-8f), new Vector3f(1.0f, 1.0f, 1.0f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.FIRST_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.FIRST_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.910625f, 0.24816513f, 0.40617055f), new Quat4f(-0.15304594f, -0.6903456f, 0.15304594f, 0.6903456f), new Vector3f(0.68000007f, 0.68000007f, 0.68f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.THIRD_PERSON_RIGHT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            builder.put(TransformType.THIRD_PERSON_LEFT_HAND, new TRSRTransformation(new Vector3f(0.225f, 0.4125f, 0.2875f), new Quat4f(0.0f, 0.0f, 0.0f, 0.99999994f), new Vector3f(0.55f, 0.55f, 0.55f), new Quat4f(0.0f, 0.0f, 0.0f, 1.0f)));
            ImmutableMap<TransformType, TRSRTransformation> transformMap = builder.build();
            this.transforms = Maps.immutableEnumMap(transformMap);
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return ToolShardOverrideHandler.INSTANCE;
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
        public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
    }
}
