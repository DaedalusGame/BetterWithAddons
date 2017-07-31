package betterwithaddons.client.render;

import betterwithaddons.entity.EntityArtifactFrame;
import betterwithaddons.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderArtifactFrame extends RenderItemFrame {
    public static final IRenderFactory ARTIFACEFRAME_RENDER = renderManager1 -> new RenderArtifactFrame(renderManager1);

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation mapModel = new ModelResourceLocation(ModItems.artifactFrame.getRegistryName(), "map");
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation(ModItems.artifactFrame.getRegistryName(), "normal");
    private final RenderItem itemRenderer;

    public RenderArtifactFrame(RenderManager renderManagerIn) {
        super(renderManagerIn, Minecraft.getMinecraft().getRenderItem());
        this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityArtifactFrame frame = (EntityArtifactFrame) entity;
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if(renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(getTeamColor(entity));
        }

        renderModel(frame);

        if(renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);

        this.renderItem(frame);
        GlStateManager.popMatrix();
        this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
    }

    private void renderItem(EntityArtifactFrame itemFrame)
    {
        ItemStack itemstack = itemFrame.getDisplayedItem();

        if (!itemstack.isEmpty())
        {
            EntityItem entityitem = new EntityItem(itemFrame.world, 0.0D, 0.0D, 0.0D, itemstack);
            entityitem.getItem().setCount(1);
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int i = itemFrame.getRotation();

            GlStateManager.rotate((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            net.minecraftforge.client.event.RenderItemInFrameEvent event = new net.minecraftforge.client.event.RenderItemInFrameEvent(itemFrame, this);
            if (!net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
            {
                //TODO:Render bigger if some flag is set
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                GlStateManager.pushAttrib();
                RenderHelper.enableStandardItemLighting();
                this.itemRenderer.renderItem(entityitem.getItem(), ItemCameraTransforms.TransformType.FIXED);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    private void renderModel(EntityArtifactFrame entity)
    {
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (!entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().getItem() == Items.FILLED_MAP)
        {
            //ModelResourceLocation mapModel = new ModelResourceLocation(new ResourceLocation("betterwithaddons",this.mapModel.getResourcePath()),this.mapModel.getVariant());
            ibakedmodel = modelmanager.getModel(mapModel);
        }
        else
        {
            //ModelResourceLocation itemFrameModel = new ModelResourceLocation(new ResourceLocation("betterwithaddons",this.itemFrameModel.getResourcePath()),this.itemFrameModel.getVariant());
            ibakedmodel = modelmanager.getModel(itemFrameModel);
        }

        //GlStateManager.pushMatrix();
        //GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
