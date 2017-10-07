package betterwithaddons.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelKarateZombie extends ModelBiped {
    public ModelRenderer headband_front;
    public ModelRenderer headband_back;
    public ModelRenderer headband_L;
    public ModelRenderer headband_R;
    public ModelRenderer headband_centerpiece;
    public ModelRenderer headband_tongueL;
    public ModelRenderer headband_tongueR;
    public ModelRenderer headband_knot;
    public ModelRenderer belt_front;
    public ModelRenderer belt_back;
    public ModelRenderer belt_sideR;
    public ModelRenderer belt_centerfront;
    public ModelRenderer belt_centerback;
    public ModelRenderer belt_sideL;
    public ModelRenderer belt_tongueL;
    public ModelRenderer belt_tongueR;

    public ModelKarateZombie()
    {
        super(0.0F, 0.0F, 64, 64);
        this.belt_back = new ModelRenderer(this, 0, 32);
        this.belt_back.setRotationPoint(0.0F, 10.5F, 2.3F);
        this.belt_back.addBox(-5.0F, -1.0F, -0.5F, 10, 2, 1, 0.0F);
        this.belt_front = new ModelRenderer(this, 0, 32);
        this.belt_front.setRotationPoint(-5.0F, 9.5F, -2.8F);
        this.belt_front.addBox(0.0F, 0.0F, 0.0F, 10, 2, 1, 0.0F);
        this.belt_sideR = new ModelRenderer(this, 0, 35);
        this.belt_sideR.setRotationPoint(4.2F, 10.5F, 0.0F);
        this.belt_sideR.addBox(-2.5F, -1.0F, -1.0F, 5, 2, 2, 0.0F);
        this.setRotateAngle(belt_sideR, 0.0F, -1.5707963267948966F, 0.0F);
        this.belt_sideL = new ModelRenderer(this, 0, 35);
        this.belt_sideL.setRotationPoint(-4.2F, 10.5F, 0.0F);
        this.belt_sideL.addBox(-2.5F, -1.0F, -1.0F, 5, 2, 2, 0.0F);
        this.setRotateAngle(belt_sideL, 0.0F, 1.5707963267948966F, 0.0F);
        this.belt_centerfront = new ModelRenderer(this, 10, 39);
        this.belt_centerfront.setRotationPoint(-1.0F, 9.5F, -3.6F);
        this.belt_centerfront.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2, 0.0F);
        this.belt_centerback = new ModelRenderer(this, 0, 39);
        this.belt_centerback.setRotationPoint(-1.5F, 9.5F, -3.2F);
        this.belt_centerback.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2, 0.0F);
        this.belt_tongueR = new ModelRenderer(this, 18, 35);
        this.belt_tongueR.setRotationPoint(1.7F, 0.8F, 0.4F);
        this.belt_tongueR.addBox(-1.0F, 0.0F, -0.5F, 2, 7, 1, 0.0F);
        this.setRotateAngle(belt_tongueR, 0.0F, 0.0F, -0.7285004297824331F);
        this.belt_tongueL = new ModelRenderer(this, 18, 35);
        this.belt_tongueL.setRotationPoint(1.3F, 0.8F, 0.4F);
        this.belt_tongueL.addBox(-1.0F, 0.0F, -0.5F, 2, 7, 1, 0.0F);
        this.setRotateAngle(belt_tongueL, 0.0F, 0.0F, 0.7285004297824331F);

        this.headband_front = new ModelRenderer(this, 0, 44);
        this.headband_front.setRotationPoint(0.0F, -6.5F, -4.0F);
        this.headband_front.addBox(-4.5F, -1.0F, -0.5F, 9, 2, 1, 0.0F);
        this.headband_back = new ModelRenderer(this, 0, 44);
        this.headband_back.setRotationPoint(0.0F, -6.5F, 4.0F);
        this.headband_back.addBox(-4.5F, -1.0F, -0.5F, 9, 2, 1, 0.0F);
        this.headband_R = new ModelRenderer(this, 0, 44);
        this.headband_R.setRotationPoint(4.0F, -6.5F, 0.0F);
        this.headband_R.addBox(-4.5F, -1.0F, -0.5F, 9, 2, 1, 0.0F);
        this.setRotateAngle(headband_R, 0.0F, 1.5707963267948966F, 0.0F);
        this.headband_L = new ModelRenderer(this, 0, 44);
        this.headband_L.setRotationPoint(-4.0F, -6.5F, 0.0F);
        this.headband_L.addBox(-4.5F, -1.0F, -0.5F, 9, 2, 1, 0.0F);
        this.setRotateAngle(headband_L, 0.0F, 1.5707963267948966F, 0.0F);
        this.headband_knot = new ModelRenderer(this, 4, 50);
        this.headband_knot.setRotationPoint(0.0F, 0.0F, 0.8F);
        this.headband_knot.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, 0.0F);
        this.headband_centerpiece = new ModelRenderer(this, 4, 47);
        this.headband_centerpiece.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.headband_centerpiece.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 1, 0.0F);
        this.headband_tongueR = new ModelRenderer(this, 0, 47);
        this.headband_tongueR.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.headband_tongueR.addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1, 0.0F);
        this.setRotateAngle(headband_tongueR, 0.36425021489121656F, 0.0F, -0.3490658503988659F);
        this.headband_tongueL = new ModelRenderer(this, 0, 47);
        this.headband_tongueL.setRotationPoint(0.0F, 0.0F, 0.5F);
        this.headband_tongueL.addBox(-0.5F, 0.0F, -0.5F, 1, 7, 1, 0.0F);
        this.setRotateAngle(headband_tongueL, 0.36425021489121656F, 0.0F, 0.3490658503988659F);

        this.headband_centerpiece.addChild(this.headband_tongueL);
        this.bipedHead.addChild(this.headband_front);
        this.bipedHead.addChild(this.headband_R);
        this.belt_centerback.addChild(this.belt_tongueL);
        this.headband_centerpiece.addChild(this.headband_tongueR);
        this.bipedBody.addChild(this.belt_centerfront);
        this.bipedBody.addChild(this.belt_sideL);
        this.bipedBody.addChild(this.belt_back);
        this.bipedBody.addChild(this.belt_front);
        this.bipedBody.addChild(this.belt_sideR);
        this.bipedBody.addChild(this.belt_centerback);
        this.headband_centerpiece.addChild(this.headband_knot);
        this.bipedHead.addChild(this.headband_L);
        this.belt_centerback.addChild(this.belt_tongueR);
        this.headband_back.addChild(this.headband_centerpiece);
        this.bipedHead.addChild(this.headband_back);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
