package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class EffectElectrified extends PotionBase {
    ResourceLocation icon = new ResourceLocation("bordthings:textures/gui/effects/electrified.png");

    public EffectElectrified() {
        super("electrified", true, new Color(126,255,170).getRGB());

        this.setPotionName("Electrified");
    }

    @Override
    public void performEffect(EntityLivingBase entity, int level) {
        if(entity instanceof EntityCreeper)
        {
            //spawn firefly particles
            return;
        }

        World world = entity.world;

        if(world != null && !world.isRemote && entity.getRNG().nextInt(100) < 15) {
            entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 0.0f);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        super.renderInventoryEffect(x, y, effect, mc);

        mc.renderEngine.bindTexture(icon);

        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }
}
