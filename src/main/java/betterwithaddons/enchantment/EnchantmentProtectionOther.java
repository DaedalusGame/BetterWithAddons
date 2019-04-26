package betterwithaddons.enchantment;

import com.google.common.collect.Sets;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Predicate;

public class EnchantmentProtectionOther extends EnchantmentProtection {
    static HashSet<String> protectedSources = new HashSet<>();
    static ArrayList<Predicate<DamageSource>> sourceMatchers = new ArrayList<>();

    public static void reset() {
        protectedSources.clear();
        sourceMatchers.clear();
    }

    public static void register(String damageType) {
        protectedSources.add(damageType);
    }

    public static void register(Predicate<DamageSource> sourceMatcher) {
        sourceMatchers.add(sourceMatcher);
    }

    public EnchantmentProtectionOther(Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, Type.ALL, slots);
    }

    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        if (!source.canHarmInCreative() && isMiscDamage(source))
            return level;
        else
            return 0;
    }

    private boolean isMiscDamage(DamageSource source) {
        if(protectedSources.contains(source.damageType))
            return false;
        for (Predicate<DamageSource> sourceMatcher : sourceMatchers) {
            if(sourceMatcher.test(source))
                return false;
        }
        return true;
    }
}
