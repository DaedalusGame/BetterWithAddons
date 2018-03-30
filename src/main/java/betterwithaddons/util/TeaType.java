package betterwithaddons.util;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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

    String name;
    boolean hasLeaf;
    int leafColor;
    boolean hasSoaked;
    int soakedColor;
    boolean hasWilted;
    int wiltedColor;
    int powderColor;

    List<PotionEffect> PositiveEffects = new ArrayList<>();
    List<PotionEffect> NegativeEffects = new ArrayList<>();

    public TeaType(String name, Color color)
    {
        this.name = name;
        powderColor = color.getRGB();
        leafColor = color.getRGB();
        soakedColor = color.getRGB();
        wiltedColor = color.getRGB();
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

    public TeaType addPositive(PotionEffect effect)
    {
        PositiveEffects.add(effect);
        return this;
    }

    public TeaType addNegative(PotionEffect effect)
    {
        NegativeEffects.add(effect);
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

    public List<PotionEffect> getPositiveEffects() {
        return PositiveEffects;
    }

    public List<PotionEffect> getNegativeEffects() {
        return NegativeEffects;
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