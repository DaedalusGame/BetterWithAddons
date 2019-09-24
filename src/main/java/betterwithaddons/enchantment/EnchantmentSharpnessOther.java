package betterwithaddons.enchantment;

import net.minecraft.enchantment.EnchantmentDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;

public class EnchantmentSharpnessOther extends EnchantmentDamage {
    private static ThreadLocal<Entity> lastTarget = new ThreadLocal<>();
    private static HashSet<String> creatureTypes = new HashSet<>();
    private static HashSet<Class<? extends Entity>> creatureByClass = new HashSet<>();
    private static HashSet<String> creatureByResourceName = new HashSet<>();

    public static void reset() {
        creatureTypes.clear();
        creatureByClass.clear();
        creatureByResourceName.clear();
    }

    public static void register(String resourceName) {
        creatureByResourceName.add(resourceName);
    }

    public static void register(Class<? extends Entity> clazz) {
        creatureByClass.add(clazz);
    }

    public static void registerType(String type) {
        creatureTypes.add(type);
    }

    public static void registerType(EnumCreatureAttribute type) {
        registerType(type.name());
    }

    public static void registerByClass(ResourceLocation resourceName) {
        EntityEntry entry = ForgeRegistries.ENTITIES.getValue(resourceName);
        creatureByClass.add(entry.getEntityClass());
    }

    public EnchantmentSharpnessOther(Rarity rarityIn, EntityEquipmentSlot... slots) {
        super(rarityIn, 0, slots);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Entity target = event.getTarget();
        lastTarget.set(target);
    }

    private boolean appliesToEntity(Entity entity) {
        if(entity == null)
            return true;
        ResourceLocation name = EntityList.getKey(entity);
        if(name == null || creatureByResourceName.contains(name.toString()))
            return false;
        if(get(entity.getClass()))
            return false;
        return true;
    }

    private boolean appliesToCreatureType(EnumCreatureAttribute creatureType) {
        return !creatureTypes.contains(creatureType.name());
    }

    private static boolean get(Class<? extends Entity> clazz) {
        if(creatureByClass.contains(clazz))
            return true;
        else if(clazz != Entity.class)
            return get((Class<? extends Entity>) clazz.getSuperclass());
        else
            return false;
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        Entity target = lastTarget.get();
        if(target != null && appliesToCreatureType(creatureType) && appliesToEntity(target))
            return super.calcDamageByCreature(level, creatureType);
        else
            return 0;
    }
}
