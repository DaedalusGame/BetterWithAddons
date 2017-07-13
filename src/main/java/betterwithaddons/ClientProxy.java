package betterwithaddons;

import betterwithaddons.block.BlockThorns;
import betterwithaddons.block.IColorable;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.BrineStateMapper;
import betterwithaddons.client.ToolShardModelHandler;
import betterwithaddons.client.fx.FXLeafParticle;
import betterwithaddons.client.handler.ParticleHandler;
import betterwithaddons.client.models.ItemModels;
import betterwithaddons.client.render.*;
import betterwithaddons.entity.*;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAlchDragon;
import betterwithmods.BWMod;
import li.cil.manual.api.ManualAPI;
import li.cil.manual.api.prefab.manual.ItemStackTabIconRenderer;
import li.cil.manual.api.prefab.manual.ResourceContentProvider;
import li.cil.manual.api.prefab.manual.TextureTabIconRenderer;
import li.cil.manual.client.manual.provider.BlockImageProvider;
import li.cil.manual.client.manual.provider.ItemImageProvider;
import li.cil.manual.client.manual.provider.OreDictImageProvider;
import li.cil.manual.client.manual.provider.TextureImageProvider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
    @Override
    public void preInit() {
        registerModels();
    }

    @Override
    public void init() {
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
        registerRenderers();
    }

    @Override
    public void makeLeafFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        FXLeafParticle wisp = new FXLeafParticle(Minecraft.getMinecraft().world, x, y, z, size, r, g, b, true, maxAgeMul);
        wisp.setSpeed(motionx, motiony, motionz);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }

    public void registerModels()
    {
        ItemModels.register();
        //BlockModels.register();

        ModelLoader.setCustomStateMapper(ModBlocks.thorns,new StateMap.Builder().ignore(BlockThorns.FACING).build());
        ModelLoader.setCustomStateMapper(ModBlocks.brine, new BrineStateMapper());

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

    public void registerRenderers() {
        //ModelLoader.setCustomModelResourceLocation(ModItems.greatbow, 0, new ModelResourceLocation(ModItems.greatbow.getRegistryName(), "inventory"));
    }
}
