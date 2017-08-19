package betterwithaddons.client.models;

import betterwithaddons.block.BlockRopePost;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityRopePost;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BakedModelRopePost implements IBakedModel {
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(state.getBlock() != ModBlocks.ropePost)
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel().getQuads(state, side, rand);

        Minecraft mc = Minecraft.getMinecraft();

        List<BakedQuad> quads = new ArrayList<>();

        IBlockState actualRope = state.getBlock().getDefaultState()
                .withProperty(BlockRopePost.NORTH,state.getValue(BlockRopePost.NORTH))
                .withProperty(BlockRopePost.SOUTH,state.getValue(BlockRopePost.SOUTH))
                .withProperty(BlockRopePost.EAST,state.getValue(BlockRopePost.EAST))
                .withProperty(BlockRopePost.WEST,state.getValue(BlockRopePost.WEST))
                .withProperty(BlockRopePost.HAS_PLANKS,false)
                .withProperty(BlockRopePost.HAS_POST,false);

        IBakedModel modelRope = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(actualRope);

        quads.addAll(modelRope.getQuads(actualRope, side, rand));

        BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
        IBlockState heldState = ((IExtendedBlockState) state).getValue(BlockRopePost.HELD_STATE);
        IBlockAccess heldWorld = ((IExtendedBlockState) state).getValue(BlockRopePost.HELD_WORLD);
        BlockPos heldPos = ((IExtendedBlockState) state).getValue(BlockRopePost.HELD_POS);

        if (heldWorld == null || heldPos == null) {
            return ImmutableList.of();
        }

        if(heldWorld instanceof FakeBlockAccess)
            return ImmutableList.of();

        if(heldState != null) {
            if(heldState.getBlock().canRenderInLayer(heldState, layer)) {
                IBlockState actual = heldState.getBlock().getActualState(heldState, new FakeBlockAccess(heldWorld), heldPos);

                // Steal camo's model
                IBakedModel model = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelForState(actual);

                // Their model can be smart too
                IBlockState extended = heldState.getBlock().getExtendedState(actual, new FakeBlockAccess(heldWorld), heldPos);
                quads.addAll(model.getQuads(extended, side, rand));
            }
        }

        if(state.getValue(BlockRopePost.HAS_PLANKS)) {
            ModelRotation plankRotation = ModelRotation.X0_Y0;

            boolean x_axis = state.getValue(BlockRopePost.EAST) || state.getValue(BlockRopePost.WEST);
            boolean z_axis = state.getValue(BlockRopePost.NORTH) || state.getValue(BlockRopePost.SOUTH);
            if(x_axis && !z_axis)
                plankRotation = ModelRotation.X0_Y90;

            ItemStack heldPlanks = ((IExtendedBlockState) state).getValue(BlockRopePost.HELD_PLANKS);
            Block blockPlanks = Block.getBlockFromItem(heldPlanks.getItem());
            int meta = heldPlanks.getMetadata();
            if (blockPlanks == Blocks.AIR) //Catch items that are registered as plankWood
            {
                blockPlanks = Blocks.PLANKS;
                meta = 0;
            }
            IBlockState plankState = blockPlanks.getStateFromMeta(meta);
            String plankTexture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(plankState).getIconName();

            IModel plankModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation(Reference.MOD_ID, "block/rope_bridge")).retexture(ImmutableMap.of("all", plankTexture));
            IBakedModel bakedPlankModel = plankModel.bake(new TRSRTransformation(plankRotation), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
            quads.addAll(bakedPlankModel.getQuads(state, side, rand));
        }

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

    private static class FakeBlockAccess implements IBlockAccess {

        private final IBlockAccess compose;

        private FakeBlockAccess(IBlockAccess compose) {
            this.compose = compose;
        }

        @Override
        public TileEntity getTileEntity(@Nonnull BlockPos pos) {
            return compose.getTileEntity(pos);
        }

        @Override
        public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
            return 15 << 20 | 15 << 4;
        }

        @Nonnull
        @Override
        public IBlockState getBlockState(@Nonnull BlockPos pos) {
            IBlockState state = compose.getBlockState(pos);
            if(state.getBlock() instanceof BlockRopePost) {
                TileEntity te = compose.getTileEntity(pos);
                if(te instanceof TileEntityRopePost)
                    state = ((TileEntityRopePost) te).getFenceState();
            }
            return state == null ? Blocks.AIR.getDefaultState() : state;
        }

        @Override
        public boolean isAirBlock(@Nonnull BlockPos pos) {
            return compose.isAirBlock(pos);
        }

        @Nonnull
        @Override
        public Biome getBiome(@Nonnull BlockPos pos) {
            return compose.getBiome(pos);
        }

        @Override
        public int getStrongPower(@Nonnull BlockPos pos, @Nonnull EnumFacing direction) {
            return compose.getStrongPower(pos, direction);
        }

        @Nonnull
        @Override
        public WorldType getWorldType() {
            return compose.getWorldType();
        }

        @Override
        public boolean isSideSolid(@Nonnull BlockPos pos, @Nonnull EnumFacing side, boolean _default) {
            return compose.isSideSolid(pos, side, _default);
        }
    }
}
