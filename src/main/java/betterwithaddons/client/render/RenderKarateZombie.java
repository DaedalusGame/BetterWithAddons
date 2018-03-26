package betterwithaddons.client.render;

import betterwithaddons.client.models.ModelKarateZombie;
import betterwithaddons.entity.EntityKarateZombie;
import betterwithaddons.lib.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderKarateZombie extends RenderBiped<EntityKarateZombie> {
    private static final ResourceLocation WHITEBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate0.png");
    private static final ResourceLocation YELLOWBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate1.png");
    private static final ResourceLocation GREENBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate2.png");
    private static final ResourceLocation BLUEBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate3.png");
    private static final ResourceLocation BROWNBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate4.png");
    private static final ResourceLocation BLACKBELT_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/karate5.png");
    private static final ResourceLocation WRESTLEMANIA_BROTHER_TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/entity/wrestlemania.png");

    public RenderKarateZombie(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelKarateZombie(), 0.5F);
    }

    @Override
    public void doRender(EntityKarateZombie entity, double x, double y, double z, float entityYaw, float partialTicks) {
        ModelBiped biped = (ModelBiped) getMainModel();

        boolean grabbing = entity.getPassengers().size() > 0;

        if(grabbing) {
            biped.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
            biped.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
        }
        else if(entity.getAttackTarget() != null) {
            switch (entity.getCurrentMove()) {
                case Attack:
                    biped.rightArmPose = ModelBiped.ArmPose.ITEM;
                    biped.leftArmPose = ModelBiped.ArmPose.ITEM;
                    break;
                case Suplex:
                    biped.rightArmPose = ModelBiped.ArmPose.EMPTY;
                    biped.leftArmPose = ModelBiped.ArmPose.ITEM;
                    break;
                case Throw:
                    biped.rightArmPose = ModelBiped.ArmPose.BLOCK;
                    biped.leftArmPose = ModelBiped.ArmPose.BLOCK;
                    break;
                case Disarm:
                    biped.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    biped.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    break;
            }
        }
        else
        {
            biped.rightArmPose = ModelBiped.ArmPose.EMPTY;
            biped.leftArmPose = ModelBiped.ArmPose.EMPTY;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityKarateZombie entity) {
        if (entity != null) {
            int level = entity.getLevel();
            if(entity.isARealAmerican())
                return WRESTLEMANIA_BROTHER_TEXTURES;

            switch (level) {
                case (1):
                    return YELLOWBELT_TEXTURES;
                case (2):
                    return GREENBELT_TEXTURES;
                case (3):
                    return BLUEBELT_TEXTURES;
                case (4):
                    return BROWNBELT_TEXTURES;
                default:
                    if (level > 0)
                        return BLACKBELT_TEXTURES;
                    else
                        return WHITEBELT_TEXTURES;
            }
        }

        return null;
    }
}
