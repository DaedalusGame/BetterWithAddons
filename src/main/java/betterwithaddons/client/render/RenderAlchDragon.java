package betterwithaddons.client.render;

import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAlchDragon;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderAlchDragon extends TileEntitySpecialRenderer<TileEntityAlchDragon> {
    private static final ResourceLocation DRAGON_TEXTURES = new ResourceLocation(Reference.MOD_ID,"textures/entity/golden_dragon.png");
    private final ModelDragonHead dragonHead = new ModelDragonHead(0.0F);
    public static RenderAlchDragon instance;

    public RenderAlchDragon() {
    }

    @Override
    public void renderTileEntityAt(TileEntityAlchDragon te, double x, double y, double z, float p_renderTileEntityAt_8_, int destroystage) {
        EnumFacing facing = EnumFacing.getFront(te.getBlockMetadata() & 7);
        float animation = te.getAnimationProgress(p_renderTileEntityAt_8_);
        this.renderSkull((float)x, (float)y, (float)z, facing, (float)(te.getSkullRotation() * 360) / 16.0F, destroystage, animation);
    }

    @Override
    public void setRendererDispatcher(TileEntityRendererDispatcher dispatcher) {
        super.setRendererDispatcher(dispatcher);
        instance = this;
    }

    public void renderSkull(float x, float y, float z, EnumFacing facing, float rotation, int destroystage, float animation) {
        Object modeltorender = this.dragonHead;
        if(destroystage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroystage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(DRAGON_TEXTURES);
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        if(facing == EnumFacing.UP) {
            GlStateManager.translate(x + 0.5F, y, z + 0.5F);
        } else {
            switch(facing) {
                case NORTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.74F);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 0.5F, y + 0.25F, z + 0.26F);
                    rotation = 180.0F;
                    break;
                case WEST:
                    GlStateManager.translate(x + 0.74F, y + 0.25F, z + 0.5F);
                    rotation = 270.0F;
                    break;
                case EAST:
                default:
                    GlStateManager.translate(x + 0.26F, y + 0.25F, z + 0.5F);
                    rotation = 90.0F;
            }
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();

        ((ModelBase)modeltorender).render(null, animation, 0.0F, 0.0F, rotation, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
        if(destroystage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}