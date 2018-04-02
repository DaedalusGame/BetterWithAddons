package betterwithaddons.util;

import com.google.common.collect.HashMultimap;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class TeaType {
    public static HashMap<String,TeaType> TYPES = new HashMap<>();

    public static final TeaType WHITE = new TeaType("white",new Color(239,229,153)).setHasLeaf().setHasWilted();
    public static final TeaType ASSAM = new TeaType("assam",new Color(73,26,19)) {
        @Override public int getWeight(World world, BlockPos pos) {
            return pos.getY() > 128 ? 100 : 0;
        }
    }.setHasLeaf().setHasWilted();
    public static final TeaType CEYLON = new TeaType("ceylon",new Color(119,40,16)){
        @Override public int getWeight(World world, BlockPos pos) {
            return pos.getY() < world.getSeaLevel() ? 100 : 0;
        }
    }.setHasLeaf().setHasWilted();
    public static final TeaType GYOKURO = new TeaType("gyokuro",new Color(176,255,81)){
        @Override public int getWeight(World world, BlockPos pos) {
            return inGoodBiome(world.getBiome(pos)) && !world.canSeeSky(pos) && world.getLight(pos) > 10 ? 23 : 0;
        }
    }.setHasLeaf().setHasSoaked();
    public static final TeaType SENCHA = new TeaType("sencha",new Color(124,182,24)){
        @Override public int getWeight(World world, BlockPos pos) {
            return inGoodBiome(world.getBiome(pos)) || inTropical(world.getBiome(pos)) ? 30 : 0;
        }
    }.setHasLeaf().setHasSoaked();
    public static final TeaType BANCHA = new TeaType("bancha",new Color(124,143,24)){
        @Override public int getWeight(World world, BlockPos pos) {
            return inGoodBiome(world.getBiome(pos)) || inTropical(world.getBiome(pos)) ? 30 : 0;
        }
    }.setHasLeaf().setHasSoaked();
    public static final TeaType TENCHA = new TeaType("tencha",new Color(36,194,20)){
        @Override public int getWeight(World world, BlockPos pos) {
            return inGoodBiome(world.getBiome(pos)) && !world.canSeeSky(pos) && world.getLight(pos) > 10 ? 7 : 0;
        }
    }.setHasLeaf().setHasSoaked();
    public static final TeaType MATCHA = new TeaType("matcha",new Color(36,194,20));
    public static final TeaType HOUJICHA = new TeaType("houjicha",new Color(55,93,31));

    public static final TeaType NETHER = new TeaType("nether",new Color(190,25,25)).setHasLeaf().setHasWilted();
    public static final TeaType END = new TeaType("end",new Color(85,56,160)).setHasLeaf().setHasWilted();

    static
    {
        WHITE.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.REGENERATION,20*10));
        WHITE.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.HUNGER,20*10));
        WHITE.addPositive(ItemType.Wilted,new PotionEffect(MobEffects.SPEED,20*10));
        WHITE.addNegative(ItemType.Wilted,new PotionEffect(MobEffects.HUNGER,20*10));
        WHITE.addPositive(ItemType.Powder,new PotionEffect(MobEffects.SPEED,20*20));
        WHITE.addNegative(ItemType.Powder,new PotionEffect(MobEffects.HUNGER,20*10));

        ASSAM.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.STRENGTH,20*30));
        ASSAM.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.BLINDNESS,20*20));
        ASSAM.addPositive(ItemType.Wilted,new PotionEffect(MobEffects.STRENGTH,20*60));
        ASSAM.addNegative(ItemType.Wilted,new PotionEffect(MobEffects.POISON,20*30));
        ASSAM.addPositive(ItemType.Powder,new PotionEffect(MobEffects.STRENGTH,20*30,1));
        ASSAM.addNegative(ItemType.Powder,new PotionEffect(MobEffects.HUNGER,20*20));

        CEYLON.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.WATER_BREATHING,20*30));
        CEYLON.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.WEAKNESS,20*60));
        CEYLON.addPositive(ItemType.Wilted,new PotionEffect(MobEffects.WATER_BREATHING,20*60));
        CEYLON.addNegative(ItemType.Wilted,new PotionEffect(MobEffects.WEAKNESS,20*60*2));
        CEYLON.addPositive(ItemType.Powder,new PotionEffect(MobEffects.WATER_BREATHING,20*60*2));
        CEYLON.addNegative(ItemType.Powder,new PotionEffect(MobEffects.WEAKNESS,20*60*3));

        GYOKURO.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.NIGHT_VISION,20*30));
        GYOKURO.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.WEAKNESS,20*60));
        GYOKURO.addPositive(ItemType.Soaked,new PotionEffect(MobEffects.JUMP_BOOST,20*30,1));
        GYOKURO.addNegative(ItemType.Soaked,new PotionEffect(MobEffects.SLOWNESS,20*60*2));
        GYOKURO.addPositive(ItemType.Powder,new PotionEffect(MobEffects.JUMP_BOOST,20*60*2));
        GYOKURO.addNegative(ItemType.Powder,new PotionEffect(MobEffects.POISON,20*60));

        SENCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.SPEED,20*30));
        SENCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.MINING_FATIGUE,20*60));
        SENCHA.addPositive(ItemType.Soaked,new PotionEffect(MobEffects.SPEED,20*30,1));
        SENCHA.addNegative(ItemType.Soaked,new PotionEffect(MobEffects.MINING_FATIGUE,20*60*2));
        SENCHA.addPositive(ItemType.Powder,new PotionEffect(MobEffects.SPEED,20*60*2));
        SENCHA.addNegative(ItemType.Powder,new PotionEffect(MobEffects.MINING_FATIGUE,20*60));

        TENCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.SATURATION,20*20));
        TENCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.SLOWNESS,20*60));
        TENCHA.addPositive(ItemType.Soaked,new PotionEffect(MobEffects.SATURATION,20*20,1));
        TENCHA.addNegative(ItemType.Soaked,new PotionEffect(MobEffects.BLINDNESS,20*60*2));
        TENCHA.addPositive(ItemType.Powder,new PotionEffect(MobEffects.INSTANT_HEALTH,0));
        TENCHA.addNegative(ItemType.Powder,new PotionEffect(MobEffects.WEAKNESS,20*60,2));

        BANCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.REGENERATION,20*20));
        BANCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.BLINDNESS,20*60));
        BANCHA.addPositive(ItemType.Soaked,new PotionEffect(MobEffects.REGENERATION,20*20,1));
        BANCHA.addNegative(ItemType.Soaked,new PotionEffect(MobEffects.WEAKNESS,20*60*2));
        BANCHA.addPositive(ItemType.Powder,new PotionEffect(MobEffects.RESISTANCE,20*60*2,1));
        BANCHA.addNegative(ItemType.Powder,new PotionEffect(MobEffects.NAUSEA,20*20));

        HOUJICHA.addPositive(ItemType.Powder,new PotionEffect(MobEffects.LUCK,20*60));
        HOUJICHA.addNegative(ItemType.Powder,new PotionEffect(MobEffects.BLINDNESS,20*60));

        //Ceremonial Tea. Matcha doesn't have leaves, but these will be added to the pool of effects to pull from
        MATCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.HUNGER,20*30));
        MATCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.NAUSEA,20*30));
        MATCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.MINING_FATIGUE,20*30,2));
        MATCHA.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.WEAKNESS,20*30,1));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.REGENERATION,20*60,1));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.HASTE,20*30));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.SPEED,20*60));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.SATURATION,20*30));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.ABSORPTION,20*60*10));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.INVISIBILITY,20*60*3));
        MATCHA.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.RESISTANCE,20*60));

        MATCHA.addPositive(ItemType.Powder,new PotionEffect(MobEffects.ABSORPTION,20*60,1));
        MATCHA.addNegative(ItemType.Powder,new PotionEffect(MobEffects.HUNGER,20*60));

        NETHER.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.FIRE_RESISTANCE,20*30));
        NETHER.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.WITHER,20*30));
        NETHER.addPositive(ItemType.Wilted,new PotionEffect(MobEffects.FIRE_RESISTANCE,20*30));
        NETHER.addNegative(ItemType.Wilted,new PotionEffect(MobEffects.WITHER,20*10));
        NETHER.addPositive(ItemType.Powder,new PotionEffect(MobEffects.FIRE_RESISTANCE,20*60));
        NETHER.addNegative(ItemType.Powder,new PotionEffect(MobEffects.UNLUCK,20*60,2));

        END.addPositive(ItemType.Leaves,new PotionEffect(MobEffects.INSTANT_HEALTH,20*30));
        END.addNegative(ItemType.Leaves,new PotionEffect(MobEffects.LEVITATION,20*10));
        END.addPositive(ItemType.Wilted,new PotionEffect(MobEffects.REGENERATION,20*30));
        END.addNegative(ItemType.Wilted,new PotionEffect(MobEffects.LEVITATION,20*10));
        END.addPositive(ItemType.Powder,new PotionEffect(MobEffects.INVISIBILITY,20*60*5));
        END.addNegative(ItemType.Powder,new PotionEffect(MobEffects.LEVITATION,20*10));
    }

    String name;
    boolean hasLeaf;
    int leafColor;
    boolean hasSoaked;
    int soakedColor;
    boolean hasWilted;
    int wiltedColor;
    int powderColor;

    //HashMap<ItemType,Integer> Strengths = new HashMap<>();
    HashMultimap<ItemType,PotionEffect> PositiveEffects = HashMultimap.create();
    HashMultimap<ItemType,PotionEffect> NegativeEffects = HashMultimap.create();

    public TeaType(String name, Color color)
    {
        this.name = name;
        powderColor = color.getRGB();
        leafColor = color.getRGB();
        soakedColor = color.getRGB();
        wiltedColor = color.getRGB();
        /*Strengths.put(ItemType.Leaves,0);
        Strengths.put(ItemType.Soaked,1);
        Strengths.put(ItemType.Wilted,2);
        Strengths.put(ItemType.Powder,3);*/
        TYPES.put(name,this);
    }

    public static TeaType getType(String name)
    {
        return TYPES.getOrDefault(name,WHITE);
    }

    public static List<TeaType> getTypesByItem(ItemType type)
    {
        return TYPES.values().stream().filter(tea -> tea.isItemType(type)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static TeaType getByLocation(World world, BlockPos pos, Random rand)
    {
        ArrayList<WeightedTea> weightedTeas = TYPES.values().stream().filter(tea -> tea.getWeight(world,pos) > 0).map(tea -> new WeightedTea(tea, tea.getWeight(world, pos))).collect(Collectors.toCollection(ArrayList::new));
        return weightedTeas.size() > 0 ? WeightedRandom.getRandomItem(rand,weightedTeas).getType() : null;
    }

    public boolean isItemType(ItemType type)
    {
        switch(type)
        {
            case Leaves: return hasLeaf();
            case Soaked: return hasSoaked();
            case Wilted: return hasWilted();
            case Powder: return true;
            default: return false;
        }
    }

    public TeaType setHasLeaf()
    {
        hasLeaf = true;
        return this;
    }

    public TeaType setHasLeaf(Color color)
    {
        leafColor = color.getRGB();
        return setHasLeaf();
    }

    public TeaType setHasSoaked()
    {
        hasSoaked = true;
        return this;
    }

    public TeaType setHasSoaked(Color color)
    {
        soakedColor = color.getRGB();
        return setHasLeaf();
    }

    public TeaType setHasWilted()
    {
        hasWilted = true;
        return this;
    }

    public TeaType setHasWilted(Color color)
    {
        wiltedColor = color.getRGB();
        return setHasLeaf();
    }

    /*public TeaType setStrength(ItemType type,int strength)
    {
        Strengths.put(type,strength);
        return this;
    }*/

    public TeaType addPositive(ItemType type,PotionEffect effect)
    {
        PositiveEffects.put(type,effect);
        return this;
    }

    public TeaType addNegative(ItemType type,PotionEffect effect)
    {
        NegativeEffects.put(type,effect);
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean hasLeaf() {
        return hasLeaf;
    }

    public boolean hasSoaked() {
        return hasSoaked;
    }

    public boolean hasWilted() {
        return hasWilted;
    }

    public int getLeafColor() {
        return leafColor;
    }

    public int getSoakedColor() {
        return soakedColor;
    }

    public int getWiltedColor() {
        return wiltedColor;
    }

    public int getPowderColor() {
        return powderColor;
    }

    /*public int getStrength(ItemType type) {
        return Strengths.getOrDefault(type,0);
    }*/

    public Collection<PotionEffect> getPositiveEffects() {
        return PositiveEffects.values();
    }

    public Collection<PotionEffect> getPositiveEffects(ItemType type) {
        return PositiveEffects.get(type);
    }

    public Collection<PotionEffect> getNegativeEffects() {
        return NegativeEffects.values();
    }

    public Collection<PotionEffect> getNegativeEffects(ItemType type) {
        return NegativeEffects.get(type);
    }

    public int getWeight(World world, BlockPos pos)
    {
        return 0;
    }

    static boolean inGoodBiome(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.SWAMP) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.PLAINS) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MOUNTAIN);
    }

    static boolean inTropical(Biome biome)
    {
        return biome.getRainfall() >= 0.75f || !biome.isSnowyBiome();
    }

    public enum ItemType
    {
        Leaves,
        Soaked,
        Wilted,
        Powder,
    }

    public static class WeightedTea extends WeightedRandom.Item
    {
        TeaType type;

        public WeightedTea(TeaType type, int itemWeightIn) {
            super(itemWeightIn);
            this.type = type;
        }

        public TeaType getType()
        {
            return type;
        }
    }
}