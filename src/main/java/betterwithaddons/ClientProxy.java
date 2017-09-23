package betterwithaddons;

import betterwithaddons.block.*;
import betterwithaddons.client.ToolShardModelHandler;
import betterwithaddons.client.fx.FXLeafParticle;
import betterwithaddons.client.handler.ParticleHandler;
import betterwithaddons.client.models.ItemModels;
import betterwithaddons.client.render.*;
import betterwithaddons.entity.*;
import betterwithaddons.interaction.ModInteractions;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAlchDragon;
import betterwithmods.manual.api.ManualAPI;
import betterwithmods.manual.api.prefab.manual.ItemStackTabIconRenderer;
import betterwithmods.manual.api.prefab.manual.ResourceContentProvider;
import betterwithmods.manual.client.manual.provider.*;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class ClientProxy implements IProxy
{
    public static ModelResourceLocation aqueductWaterLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "aqueduct_water"), "normal");
    public static ModelResourceLocation ropeBridgeLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "rope_bridge"), "normal");
    public static ModelResourceLocation ropePostLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "rope_post_knot"), "normal");

    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(this);
        ModInteractions.preInitClient();
    }

    @Override
    public void init() {
        registerColorable(ModBlocks.ecksieSapling);
        registerColorable(ModBlocks.grass);
        registerColorable(ModBlocks.pcbwire);
        registerColorable(ModItems.samuraiBoots);
        registerColorable(ModItems.samuraiChestplate);
        registerColorable(ModItems.samuraiLeggings);
        registerColorable(ModItems.brokenArtifact);
        MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
        MinecraftForge.EVENT_BUS.register(new ToolShardModelHandler());
        //TODO definition provider
        ManualAPI.addProvider(new ResourceContentProvider(Reference.MOD_ID, "docs/"));
        ManualAPI.addProvider("", new TextureImageProvider());
        ManualAPI.addProvider("item", new ItemImageProvider());
        ManualAPI.addProvider("block", new BlockImageProvider());
        ManualAPI.addProvider("oredict", new OreDictImageProvider());
        ManualAPI.addTab(new ItemStackTabIconRenderer(new ItemStack(ModBlocks.chute)), "bwm.manual.bwa", "%LANGUAGE%/bwa/index.md");
    }

    @Override
    public void postInit() {

    }

    @Override
    public void makeLeafFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        FXLeafParticle wisp = new FXLeafParticle(Minecraft.getMinecraft().world, x, y, z, size, r, g, b, true, maxAgeMul);
        wisp.setSpeed(motionx, motiony, motionz);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }

    @Override
    public void overrideItemModel(Item item, int meta, ModelResourceLocation location) {
        ModelLoader.setCustomModelResourceLocation(item,meta,location);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event)
    {
        ItemModels.register();

        ModelLoader.setCustomStateMapper(ModBlocks.thorns,new StateMap.Builder().ignore(BlockThorns.FACING).build());
        //ModelLoader.setCustomStateMapper(ModBlocks.brine, new BrineStateMapper());
        ModelLoader.setCustomStateMapper(ModBlocks.ropeSideways, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                if(state.getValue(BlockRopeSideways.HAS_PLANKS))
                    return ropeBridgeLocation;

                Map <IProperty<?>,Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
                map.remove(BlockRopeSideways.HAS_PLANKS);

                return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()),this.getPropertyString(map));
            }
        });
        ModelLoader.setCustomStateMapper(ModBlocks.ropePost, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                if(state.getValue(BlockRopePost.HAS_POST))
                    return ropePostLocation;

                Map <IProperty<?>,Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
                map.remove(BlockRopePost.HAS_PLANKS);
                map.remove(BlockRopePost.HAS_POST);

                return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()),this.getPropertyString(map));
            }
        });
        ModelLoader.setCustomStateMapper(ModBlocks.aqueductWater, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return aqueductWaterLocation;
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityGreatarrow.class, RenderGreatarrow.GREATARROW_RENDER);
        RenderingRegistry.registerEntityRenderingHandler(EntityYa.class, RenderYa.YA_RENDER);
        RenderingRegistry.registerEntityRenderingHandler(EntitySpirit.class, manager -> new RenderSpirit(manager));
        RenderingRegistry.registerEntityRenderingHandler(EntityAncestryBottle.class, manager -> new RenderSnowball<>(manager,ModItems.ancestryBottle,Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityArtifactFrame.class, RenderArtifactFrame.ARTIFACEFRAME_RENDER);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlchDragon.class, new RenderAlchDragon());
    }

    private void registerColorable(IColorable colorable)
    {
        if(colorable instanceof Item && colorable.getItemColor() != null)
        {
            Minecraft.getMinecraft().getItemColors().registerItemColorHandler(colorable.getItemColor(), (Item)colorable );
        }
        else
        {
            Block block = (Block) colorable;
            if (colorable.getBlockColor() != null && block != null)
                Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(colorable.getBlockColor(), block);
            if (colorable.getItemColor() != null)
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler(colorable.getItemColor(), block);
        }
    }
}
