package betterwithaddons.interaction;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.ArmorDecorateRecipe;
import betterwithaddons.crafting.recipes.TeaNabeRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.entity.EntityKarateZombie;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IngredientSized;
import betterwithaddons.util.NabeResultPoison;
import betterwithaddons.util.TeaType;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.registry.Wood;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.module.hardcore.crafting.HCLumber;
import com.google.common.collect.Lists;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteractionEriottoMod extends Interaction {
    public static boolean ENABLED = true;
    //public static boolean GRASS_DROPS_SEEDS = false;
    public static boolean INFUSER_REPAIRS = true;
    public static boolean ALTERNATE_INFUSER_RECIPE = false;
    public static int MAX_SPIRITS = 128;
    public static int SPIRIT_PER_BOTTLE = 8;
    public static boolean JAPANESE_RANDOM_SPAWN = true;
    public static int JAPANESE_RANDOM_SPAWN_WEIGHT = 40;
    public static int KARATE_ZOMBIE_MIN_SPIRITS = 5;
    public static int KARATE_ZOMBIE_MAX_SPIRITS = 29;
    public static int KARATE_ZOMBIE_SPIRIT_PER_LEVEL = 8;
    public static double KARATE_ZOMBIE_DROP_MULTIPLIER = 1.0f;
    public static int CHERRY_BOX_CRAFTING_TIME = 500;
    public ArrayList<Item> REPAIRABLE_TOOLS = new ArrayList<>();

    @Override
    protected String getName() {
        return "addons.EriottoMod";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled","Whether the Japanese Culture module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.",ENABLED);
        INFUSER_REPAIRS = loadPropBool("InfuserRepairs","Infusers can repair japanese weapons and armors.",INFUSER_REPAIRS);
        ALTERNATE_INFUSER_RECIPE = loadPropBool("AlternateInfuserRecipe","Hardcore Structures pushes the Enchanting Table behind some exploration. This enables an alternate recipe if you want to start japanese culture before finding a Desert Temple.",ALTERNATE_INFUSER_RECIPE);
        JAPANESE_RANDOM_SPAWN = loadPropBool("RandomJapaneseMobs","Karate Zombies infused with ancestral spirit spawn randomly.",JAPANESE_RANDOM_SPAWN);
        JAPANESE_RANDOM_SPAWN_WEIGHT = loadPropInt("RandomJapaneseMobsWeight","Weight for a karate zombie to spawn.",JAPANESE_RANDOM_SPAWN_WEIGHT);
        doesNotNeedRestart(() -> {
            MAX_SPIRITS = loadPropInt("MaxSpirits","Maximum amount of spirit to be stored in Infused Soul Sand.",MAX_SPIRITS);
            SPIRIT_PER_BOTTLE = loadPropInt("SpiritsPerBottle","How much spirit is contained in one bottle.",SPIRIT_PER_BOTTLE);
            KARATE_ZOMBIE_MIN_SPIRITS = loadPropInt("KarateZombieMinSpirits","How many spirits karate zombies at least spawn with.",KARATE_ZOMBIE_MIN_SPIRITS);
            KARATE_ZOMBIE_MAX_SPIRITS = loadPropInt("KarateZombieMaxSpirits","How many spirits karate zombies at most spawn with.",KARATE_ZOMBIE_MAX_SPIRITS);
            KARATE_ZOMBIE_SPIRIT_PER_LEVEL = loadPropInt("KarateZombiePerLevel","How much spirit is required for Karate Zombies to level up.",KARATE_ZOMBIE_SPIRIT_PER_LEVEL);
            KARATE_ZOMBIE_DROP_MULTIPLIER = loadPropDouble("KarateZombieDropMultiplier","How much spirit is dropped by Karate Zombies, as a ratio of how much they have.",KARATE_ZOMBIE_DROP_MULTIPLIER);
            CHERRY_BOX_CRAFTING_TIME = loadPropInt("CherryBoxCraftingTime","How long the drying and soaking units take to process one item, in ticks.",CHERRY_BOX_CRAFTING_TIME);
        });
    }

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        if(JAPANESE_RANDOM_SPAWN) {
            List<Biome> biomes = new ArrayList<>();
            for (Biome biome: Biome.REGISTRY) {
                if(biome.getSpawnableList(EnumCreatureType.MONSTER).stream().anyMatch(entry -> entry.entityClass.isAssignableFrom(EntityZombie.class)))
                    biomes.add(biome);
            }

            EntityRegistry.addSpawn(EntityKarateZombie.class, JAPANESE_RANDOM_SPAWN_WEIGHT, 1, 4, EnumCreatureType.MONSTER, biomes.toArray(new Biome[biomes.size()]));
        }

        ConditionModule.MODULES.put("EriottoMod", this::isActive);
        ConditionModule.MODULES.put("AlternateInfuser", () -> ALTERNATE_INFUSER_RECIPE);
    }

    @Override
    public void init() {
        REPAIRABLE_TOOLS = Lists.newArrayList(ModItems.katana, ModItems.wakizashi, ModItems.tanto, ModItems.shinai, ModItems.yumi, ModItems.samuraiBoots, ModItems.samuraiLeggings, ModItems.samuraiHelm, ModItems.samuraiChestplate);

        ModBlocks.mulberrySapling.setLeaves(ModBlocks.mulberryLeaves.getDefaultState()).setLog(ModBlocks.mulberryLog.getDefaultState());
        ModBlocks.mulberryLeaves.setSapling(new ItemStack(ModBlocks.mulberrySapling));

        ModBlocks.sakuraSapling.setLeaves(ModBlocks.sakuraLeaves.getDefaultState()).setLog(ModBlocks.sakuraLog.getDefaultState()).setBig(true);
        ModBlocks.sakuraLeaves.setSapling(new ItemStack(ModBlocks.sakuraSapling));

        ModBlocks.connectPanes(ModBlocks.shoji, ModBlocks.fusuma);

        CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.ironSand, 1)}, new OreIngredient("blockIron"), 8);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(Blocks.IRON_BLOCK, 1), new ItemStack(Blocks.SAND, 8)}, IngredientSized.fromBlock(ModBlocks.ironSand, 1), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, new ItemStack(ModItems.sashimi, 3)}, IngredientSized.fromStacks(new ItemStack(Items.FISH, 1)), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModItems.fuguSac, 1), new ItemStack(ModItems.preparedPuffer, 3)}, Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), 0);
        CraftingManagerFireNet.getInstance().addRecipe(new ItemStack[]{ModItems.materialJapan.getMaterial("iron_scales", 27)}, Ingredient.fromStacks(new ItemStack(ModBlocks.ironSand, 1)));
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.materialJapan.getMaterial("washi", 9)}, Ingredient.fromStacks(ModItems.materialJapan.getMaterial("mulberry_sheet")), 0);
        TeaType.getTypesByItem(TeaType.ItemType.Wilted).stream().forEach(tea -> CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.teaPowder.getStack(tea)}, Ingredient.fromStacks(ModItems.teaWilted.getStack(tea)), 0));
        TeaType.getTypesByItem(TeaType.ItemType.Soaked).stream().forEach(tea -> CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.teaPowder.getStack(tea)}, Ingredient.fromStacks(ModItems.teaSoaked.getStack(tea)), 0));
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.teaPowder.getStack(TeaType.MATCHA)}, Ingredient.fromStacks(ModItems.teaPowder.getStack(TeaType.TENCHA)),0);

        OreDictionary.registerOre("seed", new ItemStack(Items.WHEAT_SEEDS)); //I'll beat the shit out of HarvestCraft
        OreDictionary.registerOre("seed", new ItemStack(Items.PUMPKIN_SEEDS));
        OreDictionary.registerOre("seed", new ItemStack(Items.MELON_SEEDS));
        OreDictionary.registerOre("seed", new ItemStack(Items.BEETROOT_SEEDS));
        if (ModInteractions.bwm.isActive())
            OreDictionary.registerOre("seed", new ItemStack(BWMBlocks.HEMP));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.rice));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.rush));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.tea));

        OreDictionary.registerOre("cropRice", ModItems.materialJapan.getMaterial("rice"));
        OreDictionary.registerOre("seedRice", new ItemStack(ModBlocks.rice));
        OreDictionary.registerOre("cropRush", ModItems.materialJapan.getMaterial("rush"));
        OreDictionary.registerOre("seedRush", new ItemStack(ModBlocks.rush));
        TeaType.getTypesByItem(TeaType.ItemType.Leaves).stream().forEach(teaType -> OreDictionary.registerOre("cropTea", ModItems.teaLeaves.getStack(teaType)));
        OreDictionary.registerOre("seedTea", new ItemStack(ModBlocks.tea));
        OreDictionary.registerOre("materialBamboo", new ItemStack(ModBlocks.bamboo));

        OreDictionary.registerOre("treeSapling", ModBlocks.sakuraSapling);
        OreDictionary.registerOre("treeSapling", ModBlocks.mulberrySapling);
        OreDictionary.registerOre("logWood", ModBlocks.sakuraLog);
        OreDictionary.registerOre("logWood", ModBlocks.mulberryLog);
        OreDictionary.registerOre("plankWood", ModBlocks.sakuraPlanks);
        OreDictionary.registerOre("plankWood", ModBlocks.mulberryPlanks);

        OreDictionary.registerOre("barkWood", ModItems.materialJapan.getMaterial("bark_mulberry"));
        OreDictionary.registerOre("barkWood", ModItems.materialJapan.getMaterial("bark_sakura"));

        OreDictionary.registerOre("ingotTamahagane", ModItems.materialJapan.getMaterial("tamahagane_finished"));
        OreDictionary.registerOre("ingotHochoTetsu", ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));

        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.rice), new Object[]{ModItems.materialJapan.getMaterial("soaked_rice")});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.laxative), new Object[]{new ItemStack(ModItems.mulberry,3),new ItemStack(Items.SUGAR),BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD)});

        GameRegistry.addSmelting(ModItems.teaPowder, ModItems.teaPowder.getStack(TeaType.HOUJICHA), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.preparedPuffer), new ItemStack(ModItems.preparedCookedPuffer), 0.35f);
        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("soaked_rice"), new ItemStack(ModItems.rice), 0.35f);

        //if (GRASS_DROPS_SEEDS) {
        //    MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rice), 2);
        //    MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rush), 2);
        //}

        BWOreDictionary.woods.add(new Wood(new ItemStack(ModBlocks.sakuraLog),new ItemStack(ModBlocks.sakuraPlanks),ModItems.materialJapan.getMaterial("bark_sakura")));
        BWOreDictionary.woods.add(new Wood(new ItemStack(ModBlocks.mulberryLog),new ItemStack(ModBlocks.mulberryPlanks),ModItems.materialJapan.getMaterial("bark_mulberry")){
            @Override
            public ItemStack getPlank(int count) {
                ItemStack copy = new ItemStack(ModBlocks.mulberryPlanks);
                copy.setCount((int)Math.ceil(count / (float) HCLumber.axePlankAmount));
                return copy;
            }
        });

        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("rice_stalk"), ModItems.materialJapan.getMaterial("rice_ash"), 0.1f);

        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"katana"),new ItemStack(ModItems.katana), "l", "l", "w", 'l', ModItems.materialJapan.getMaterial("half_katana_blade"), 'w', ModItems.materialJapan.getMaterial("tsuka")),8);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"wakizashi"),new ItemStack(ModItems.wakizashi), "l", "l", "w", 'l', "ingotTamahagane", 'w', ModItems.materialJapan.getMaterial("tsuka")),6);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"tanto"),new ItemStack(ModItems.tanto), "l", "w", 'l', "ingotTamahagane", 'w', ModItems.materialJapan.getMaterial("tsuka")),4);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"shinai"),new ItemStack(ModItems.shinai), "l", "l", "w", 'l', ModItems.materialJapan.getMaterial("bamboo_slats"), 'w', ModItems.materialJapan.getMaterial("tsuka")),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"ya"),new ItemStack(ModItems.ya, 12), "h", "l", "f", 'h', ModItems.materialJapan.getMaterial("ya_head"), 'l', ModItems.materialJapan.getMaterial("bamboo_slats"), 'f', new ItemStack(Items.FEATHER)),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"yumi"),new ItemStack(ModItems.yumi), " ns", "l s", " us", 'n', ModItems.materialJapan.getMaterial("yumi_top"), 'u', ModItems.materialJapan.getMaterial("yumi_bottom"), 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STRING)),5);

        addArmorFinishRecipe("samurai_helmet",new ItemStack(ModItems.samuraiHelm), ModItems.materialJapan.getMaterial("helmet_undecorated"), 5);
        addArmorFinishRecipe("samurai_chestplate",new ItemStack(ModItems.samuraiChestplate), ModItems.materialJapan.getMaterial("chest_undecorated"), 8);
        addArmorFinishRecipe("samurai_leggings",new ItemStack(ModItems.samuraiLeggings), ModItems.materialJapan.getMaterial("legs_undecorated"), 7);
        addArmorFinishRecipe("samurai_boots",new ItemStack(ModItems.samuraiBoots), ModItems.materialJapan.getMaterial("boots_undecorated"), 4);

        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"netted_screen"),new ItemStack(ModBlocks.nettedScreen), "bsb", "sss", "bsb", 's', "string", 'b', ModItems.materialJapan.getMaterial("bamboo_slats")),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"tatara"),new ItemStack(ModBlocks.tatara), "idi", "g g", "ini", 'i', "ingotIron", 'g', "ingotGold", 'd', "gemDiamond", 'n', new ItemStack(Blocks.NETHERRACK)),4);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"soaking_unit"),new ItemStack(ModBlocks.cherrybox, 1, 0), "pxp", "x x", "pxp", 'p', new ItemStack(ModBlocks.sakuraPlanks), 'x', new ItemStack(Blocks.IRON_BARS)),1);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"drying_unit"),new ItemStack(ModBlocks.cherrybox, 1, 1), "pxp", "p p", "ppp", 'p', new ItemStack(ModBlocks.sakuraPlanks), 'x', new ItemStack(Blocks.GLASS_PANE)),1);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"nabe"),new ItemStack(ModBlocks.nabe, 1), "i i", "i i", "lhl", 'i', "ingotIron", 'l', "ingotTamahagane", 'h', "ingotHochoTetsu"),1);

        //Random seeds
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(new OreIngredient("seed"), 1, ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public ItemStack getOutput(ItemStack input) {
                int i = random.nextInt(2);
                switch(i)
                {
                    case(0):return new ItemStack(ModBlocks.rice);
                    case(1):return new ItemStack(ModBlocks.rush);
                    case(2):return new ItemStack(ModBlocks.tea);
                    default:return ItemStack.EMPTY;
                }
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.rice) || item.getItem() == Item.getItemFromBlock(ModBlocks.rush) || item.getItem() == Item.getItemFromBlock(ModBlocks.tea))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.rice), new ItemStack(ModBlocks.rush), new ItemStack(ModBlocks.tea));
            }
        });
        //Random saplings
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(new OreIngredient("treeSapling"), 1, ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public ItemStack getOutput(ItemStack input) {
                if (random.nextInt(2) == 0)
                    return new ItemStack(ModBlocks.sakuraSapling);
                else
                    return new ItemStack(ModBlocks.mulberrySapling);
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.sakuraSapling) || item.getItem() == Item.getItemFromBlock(ModBlocks.mulberrySapling))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(ModBlocks.mulberrySapling));
            }
        });
        //Bamboo
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new OreIngredient("sugarcane"), 1, new ItemStack(ModBlocks.bamboo));
        //Repair tools and armor
        if (INFUSER_REPAIRS)
            CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(Ingredient.fromItem(ModItems.katana), 2, ItemStack.EMPTY) {
                @Override
                public boolean matchesInput(ItemStack item) {
                    return isRepairableTool(item);
                }

                @Override
                public List<ItemStack> getRecipeInputs() {
                    ArrayList<ItemStack> inputs = new ArrayList<>();

                    for (Item tool : REPAIRABLE_TOOLS) {
                        ItemStack basestack = new ItemStack(tool);
                        if (tool.isDamageable()) {
                            int maxdamage = basestack.getMaxDamage();
                            int durability = maxdamage / 20;
                            for (int i = 20; i >= 1; i--) {
                                ItemStack damaged = new ItemStack(tool, 1, Math.min(durability * i, maxdamage - 1));
                                inputs.add(damaged);
                            }
                        }
                    }

                    return inputs;
                }

                @Override
                public ItemStack getOutput(ItemStack input) {
                    if (input.isItemStackDamageable())
                        input.setItemDamage(Math.max(0, input.getItemDamage() - input.getMaxDamage() / 20));
                    return input;
                }
            });

        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.bamboo)), ModItems.materialJapan.getMaterial("soaked_bamboo"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("rice")), ModItems.materialJapan.getMaterial("soaked_rice"));
        if (ModInteractions.bwm.isActive())
            CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("bark_mulberry")), ModItems.materialJapan.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.mulberryLog)), ModItems.materialJapan.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 0)), new ItemStack(Blocks.SPONGE, 1, 1));
        TeaType.getTypesByItem(TeaType.ItemType.Soaked).stream().filter(TeaType::hasLeaf).forEach(tea -> CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.teaLeaves.getStack(tea)), ModItems.teaSoaked.getStack(tea)));

        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("rice_stalk")), ModItems.materialJapan.getMaterial("rice_hay"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("soaked_mulberry")), ModItems.materialJapan.getMaterial("mulberry_paste"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("soaked_bamboo")), ModItems.materialJapan.getMaterial("bamboo_slats"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 1)), new ItemStack(Blocks.SPONGE, 1, 0));
        TeaType.getTypesByItem(TeaType.ItemType.Wilted).stream().filter(TeaType::hasLeaf).forEach(tea -> CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.teaLeaves.getStack(tea)), ModItems.teaWilted.getStack(tea)));

        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.ironSand)), new ItemStack(ModBlocks.kera));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("tamahagane")), ModItems.materialJapan.getMaterial("tamahagane_heated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("tamahagane_wrapped")), ModItems.materialJapan.getMaterial("tamahagane_reheated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.materialJapan.getMaterial("hocho_tetsu")), ModItems.materialJapan.getMaterial("hocho_tetsu_heated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromItem(Items.CLAY_BALL), ModItems.teaCup.getEmpty());

        CraftingManagerNabe.getInstance().addRecipe("poison",new NabeResultPoison(12,12),Lists.newArrayList(new OreIngredient("cropNetherWart"),new OreIngredient("gunpowder"),new OreIngredient("dustRedstone"),new OreIngredient("dustGlowstone"),Ingredient.fromItem(Items.SPIDER_EYE),new OreIngredient("cropRush")),1000);
        CraftingManagerNabe.getInstance().addRecipe(new TeaNabeRecipe());
    }

    private boolean isRepairableTool(ItemStack stack) {
        return REPAIRABLE_TOOLS.contains(stack.getItem()) && stack.isItemDamaged();
    }

    private void addArmorFinishRecipe(String group,ItemStack out, ItemStack in, int spirit) {
        ItemStack gold = new ItemStack(Items.GOLD_NUGGET);
        ItemStack dye = new ItemStack(Items.DYE,1,14);
        ItemStack washi = ModItems.materialJapan.getMaterial("washi");
        //GameRegistry.addShapelessRecipe(out,gold,gold,gold,dye,in,dye,washi,washi,washi);
        CraftingManagerInfuser.getInstance().addRecipe(new ArmorDecorateRecipe(new ResourceLocation(Reference.MOD_ID,group), out, "ggg", "dad", "www", 'g', "nuggetGold", 'd', "dye", 'a', in, 'w', ModItems.materialJapan.getMaterial("washi")),spirit);
    }

    private void addFoldingRecipe(ItemStack out, ItemStack in) {
        //GameRegistry.addShapedRecipe(out, "t", "s", 't', in, 's', new ItemStack(Items.STICK));
    }
}
