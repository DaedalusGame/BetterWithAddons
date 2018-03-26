package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.potion.PotionEffect;

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
}
