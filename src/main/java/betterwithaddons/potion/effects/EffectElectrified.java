package betterwithaddons.potion.effects;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

public class EffectElectrified extends PotionBase {
    public EffectElectrified() {
        super("electrified", true, new Color(126, 255, 170).getRGB());

        this.setPotionName("Electrified");
        this.setIconIndex(2, 0);
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % (Math.max(40 / Math.max(amplifier + 1, 1), 1)) == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int level) {
        if (entity instanceof EntityCreeper) {
            //spawn firefly particles
            return;
        }

        if (entity instanceof EntityPlayer) {
            entity.resetActiveHand();
        }
        entity.attackEntityFrom(DamageSource.LIGHTNING_BOLT, 0.1f);
        entity.heal(0.1f);
        entity.motionX = 0;
        entity.motionY = 0;
        entity.motionZ = 0;

        Random rand = entity.getRNG();
        for(int i = 0; i < 5; i++)
            BetterWithAddons.proxy.makeLightningFX(entity.posX + (rand.nextDouble() - 0.5D) * (double)entity.width, entity.posY + rand.nextDouble() * (double)entity.height, entity.posZ + (rand.nextDouble() - 0.5D) * (double)entity.width, 1.0f, 1.0f, 1.0f, 0.4f, 10f);
    }
}
