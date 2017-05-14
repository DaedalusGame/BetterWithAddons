package betterwithaddons;

import betterwithaddons.block.BlockThorns;
import betterwithaddons.block.IColorable;
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
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
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
        MinecraftForge.EVENT_BUS.register(ParticleHandler.class);
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

        RenderingRegistry.registerEntityRenderingHandler(EntityGreatarrow.class, RenderGreatarrow.GREATARROW_RENDER);
        RenderingRegistry.registerEntityRenderingHandler(EntityYa.class, RenderYa.YA_RENDER);
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
