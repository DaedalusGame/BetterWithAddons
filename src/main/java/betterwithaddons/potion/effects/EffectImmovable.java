package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

public class EffectImmovable extends PotionBase {
    public EffectImmovable()
    {
        super("immovable", false, Color.BLACK.getRGB());

        this.setPotionName("Immovable");
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        super.renderInventoryEffect(x, y, effect, mc);
    }
}
