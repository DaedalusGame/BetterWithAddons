package betterwithaddons;

import betterwithaddons.block.IColorableBlock;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.fx.FXLeafParticle;
import betterwithaddons.client.handler.ParticleHandler;
import betterwithaddons.client.models.ItemModels;
import betterwithaddons.client.render.RenderAlchDragon;
import betterwithaddons.client.render.RenderGreatarrow;
import betterwithaddons.client.render.RenderYa;
import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.entity.EntityYa;
import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityAlchDragon;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.world.ColorizerGrass;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy implements IProxy
{
    @Override
    public void preInit() {
        registerModels();
    }

    @Override
    public void init() {
        registerColoredBlock(ModBlocks.grass);
        registerColoredBlock(ModBlocks.pcbwire);
        MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
    }

    @Override
    public void postInit() {
        registerRenderers();
    }

    @Override
    public void makeLeafFX(double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
        FXLeafParticle wisp = new FXLeafParticle(Minecraft.getMinecraft().theWorld, x, y, z, size, r, g, b, true, maxAgeMul);
        wisp.setSpeed(motionx, motiony, motionz);
        Minecraft.getMinecraft().effectRenderer.addEffect(wisp);
    }

    public void registerModels()
    {
        ItemModels.register();
        //BlockModels.register();

        RenderingRegistry.registerEntityRenderingHandler(EntityGreatarrow.class, RenderGreatarrow.GREATARROW_RENDER);
        RenderingRegistry.registerEntityRenderingHandler(EntityYa.class, RenderYa.YA_RENDER);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAlchDragon.class, new RenderAlchDragon());


    }

    private void registerColoredBlock(Block block)
    {
        if(block instanceof IColorableBlock) {
            IColorableBlock colorblock = (IColorableBlock)block;
            if (colorblock.getBlockColor() != null)
                Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(colorblock.getBlockColor(), block);
            if (colorblock.getItemColor() != null)
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler(colorblock.getItemColor(), block);
        }
    }

    public void registerRenderers() {
        //ModelLoader.setCustomModelResourceLocation(ModItems.greatbow, 0, new ModelResourceLocation(ModItems.greatbow.getRegistryName(), "inventory"));
    }
}
