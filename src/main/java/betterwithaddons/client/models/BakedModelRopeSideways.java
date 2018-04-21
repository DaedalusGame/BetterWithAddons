package betterwithaddons.client.models;

import betterwithaddons.block.BlockRopeSideways;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.lib.Reference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BakedModelRopeSideways implements IBakedModel {
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(state.getBlock() != ModBlocks.ROPE_SIDEWAYS)
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);

        Minecraft mc = Minecraft.getMinecraft();

        List<BakedQuad> quads = new ArrayList<>();

        IBlockState actual = state.getBlock().getDefaultState().withProperty(BlockRopeSideways.SHAPE,state.getValue(BlockRopeSideways.SHAPE)).withProperty(BlockRopeSideways.HAS_PLANKS,false);

        IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(actual);

        quads.addAll(model.getQuads(actual, side, rand));

        ModelRotation plankRotation = ModelRotation.X0_Y0;
        if(state.getValue(BlockRopeSideways.SHAPE) == BlockRopeSideways.EnumRopeShape.X)
            plankRotation = ModelRotation.X0_Y90;

        ItemStack heldPlanks = ((IExtendedBlockState) state).getValue(BlockRopeSideways.HELD_PLANKS);
        Block blockPlanks = Block.getBlockFromItem(heldPlanks.getItem());
        int meta = heldPlanks.getMetadata();
        if(blockPlanks == Blocks.AIR) //Catch items that are registered as plankWood
        {
            blockPlanks = Blocks.PLANKS;
            meta = 0;
        }
        IBlockState plankState = blockPlanks.getStateFromMeta(meta);
        String plankTexture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(plankState).getIconName();

        IModel plankModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(Reference.MOD_ID,"block/rope_bridge")).retexture(ImmutableMap.of("all",plankTexture));
        IBakedModel bakedPlankModel = plankModel.bake(new TRSRTransformation(plankRotation), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
        quads.addAll(bakedPlankModel.getQuads(state,side,rand));

        return ImmutableList.copyOf(quads);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("betterwithmods:blocks/kiln");
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }
}
