package betterwithaddons.interaction;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.ArmorDecorateRecipe;
import betterwithaddons.crafting.recipes.NabeRecipeVisual;
import betterwithaddons.crafting.recipes.TeaNabeRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.entity.EntityKarateZombie;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.*;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.Wood;
import betterwithmods.module.gameplay.miniblocks.MiniBlocks;
import betterwithmods.module.gameplay.miniblocks.MiniType;
import betterwithmods.module.gameplay.miniblocks.blocks.BlockMini;
import betterwithmods.module.hardcore.crafting.HCLumber;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class InteractionEriottoMod extends Interaction {
    public static boolean ENABLED = true;
    //public static boolean GRASS_DROPS_SEEDS = false;
    public static boolean INFUSER_REPAIRS = true;
    public static boolean ALTERNATE_INFUSER_RECIPE = false;
    public static int SOULSAND_MAX_SPIRITS = 128;
    public static int BOTTLE_MAX_SPIRITS = 8;
    public static boolean JAPANESE_RANDOM_SPAWN = true;
    public static int KARATE_ZOMBIE_SPAWN_WEIGHT = 40;
    public static int KARATE_ZOMBIE_SPAWN_MIN_SPIRITS = 5;
    public static int KARATE_ZOMBIE_SPAWN_MAX_SPIRITS = 29;
    public static int KARATE_ZOMBIE_SPIRIT_PER_LEVEL = 8;
    public static double KARATE_ZOMBIE_DROP_MULTIPLIER = 1.0f;
    public static int CHERRY_BOX_CRAFTING_TIME = 500;
    public static int MAX_SPIRIT_AGE = 1200;
    public static int IRON_PER_IRONSAND = 3;
    public static int SAND_PER_IRONSAND = 8;
    public static double KERA_TAMAHAGANE_CHANCE = 0.30;
    public static double KERA_HOCHOTETSU_CHANCE = 0.20;
    public static double KERA_IRON_CHANCE = 0.20;
    public static int KARATE_ZOMBIE_MAX_SPIRITS = 128;
    public static Color SPIRIT_COLOR_LOW = new Color(127, 0, 0, 128);
    public static Color SPIRIT_COLOR_HIGH = new Color(255, 0, 0, 128);
    public static Color SPIRIT_GUI_COLOR_LOW = new Color(127, 0, 0, 255);
    public static Color SPIRIT_GUI_COLOR_HIGH = new Color(255, 0, 0, 255);
    public static Color INFUSER_COLOR_LOW = new Color(100,100,100);
    public static Color INFUSER_COLOR_HIGH = new Color(255,0,0);
    public static boolean HARDCORE_BREWING = true;

    public static HashSet<String> TRANSFORM_ZOMBIES = new HashSet<>();
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
        KARATE_ZOMBIE_SPAWN_WEIGHT = loadPropInt("RandomJapaneseMobsWeight","Weight for a karate zombie to spawn.", KARATE_ZOMBIE_SPAWN_WEIGHT);
        IRON_PER_IRONSAND = loadPropInt("IronPerIronSand","How much iron should be required per block of iron sand", IRON_PER_IRONSAND);
        SAND_PER_IRONSAND = loadPropInt("SandPerIronSand","How much sand should be required per block of iron sand", SAND_PER_IRONSAND);
        HARDCORE_BREWING = loadPropBool("HardcoreBrewing", "Replaces certain Nabe brewing ingredients with their equivalent HC brewing equivalents.", HARDCORE_BREWING);
        doesNotNeedRestart(() -> {
            SOULSAND_MAX_SPIRITS = loadPropInt("MaxSpirits","Maximum amount of spirit to be stored in Infused Soul Sand.", SOULSAND_MAX_SPIRITS);
            BOTTLE_MAX_SPIRITS = loadPropInt("SpiritsPerBottle","How much spirit is contained in one bottle.", BOTTLE_MAX_SPIRITS);
            MAX_SPIRIT_AGE = loadPropInt("MaxSpiritAge","How long spirits can exist in world, in ticks.", MAX_SPIRIT_AGE);
            KARATE_ZOMBIE_MAX_SPIRITS = loadPropInt("KarateZombieMaxSpirits","How many spirits can be infused into karate zombies.", KARATE_ZOMBIE_MAX_SPIRITS);
            KARATE_ZOMBIE_SPAWN_MIN_SPIRITS = loadPropInt("KarateZombieSpawnMinSpirits","How many spirits karate zombies at least spawn with.", KARATE_ZOMBIE_SPAWN_MIN_SPIRITS);
            KARATE_ZOMBIE_SPAWN_MAX_SPIRITS = loadPropInt("KarateZombieSpawnMaxSpirits","How many spirits karate zombies at most spawn with.", KARATE_ZOMBIE_SPAWN_MAX_SPIRITS);
            KARATE_ZOMBIE_SPIRIT_PER_LEVEL = loadPropInt("KarateZombiePerLevel","How much spirit is required for Karate Zombies to level up.",KARATE_ZOMBIE_SPIRIT_PER_LEVEL);
            KARATE_ZOMBIE_DROP_MULTIPLIER = loadPropDouble("KarateZombieDropMultiplier","How much spirit is dropped by Karate Zombies, as a ratio of how much they have.",KARATE_ZOMBIE_DROP_MULTIPLIER);
            CHERRY_BOX_CRAFTING_TIME = loadPropInt("CherryBoxCraftingTime","How long the drying and soaking units take to process one item, in ticks.",CHERRY_BOX_CRAFTING_TIME);
            KERA_TAMAHAGANE_CHANCE = loadPropDouble("KeraTamahaganeChance","Chance to obtain Tamahagane from breaking Kera.",KERA_TAMAHAGANE_CHANCE);
            KERA_HOCHOTETSU_CHANCE = loadPropDouble("KeraHochoTetsuChance","Chance to obtain Hocho-Tetsu from breaking Kera.",KERA_HOCHOTETSU_CHANCE);
            KERA_IRON_CHANCE = loadPropDouble("KeraIronChance","Chance to obtain Iron from breaking Kera.",KERA_IRON_CHANCE);
            TRANSFORM_ZOMBIES = loadPropStringSet("TransformZombies","A list of entity ids that can become Karate Zombies on contact with spirits.",new String[]{"minecraft:zombie"});
            SPIRIT_COLOR_LOW = loadPropColor("SpiritColorLow", "The low color that spirit entities have.", SPIRIT_COLOR_LOW);
            SPIRIT_COLOR_HIGH = loadPropColor("SpiritColorHigh", "The high color that spirit entities have.", SPIRIT_COLOR_HIGH);
            SPIRIT_GUI_COLOR_LOW = loadPropColor("SpiritGuiColorLow", "The background color that spirit values have in a gui.", SPIRIT_GUI_COLOR_LOW);
            SPIRIT_GUI_COLOR_HIGH = loadPropColor("SpiritGuiColorHigh", "The foreground color that spirit values have in a gui.", SPIRIT_GUI_COLOR_HIGH);
            INFUSER_COLOR_LOW = loadPropColor("InfuserColorLow", "The color that an empty Infuser has.", INFUSER_COLOR_LOW);
            INFUSER_COLOR_HIGH = loadPropColor("InfuserColorHigh", "The color that a filled Infuser has.", INFUSER_COLOR_HIGH);
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

            EntityRegistry.addSpawn(EntityKarateZombie.class, KARATE_ZOMBIE_SPAWN_WEIGHT, 1, 4, EnumCreatureType.MONSTER, biomes.toArray(new Biome[biomes.size()]));
        }

        ConditionModule.MODULES.put("EriottoMod", this::isActive);
        ConditionModule.MODULES.put("AlternateInfuser", () -> ALTERNATE_INFUSER_RECIPE);

        VariableSegment.addVariableSupplier(new ResourceLocation(Reference.MOD_ID, "spirits_per_bottle"), () -> Integer.toString(BOTTLE_MAX_SPIRITS));
    }

    @Override
    void oreDictRegistration() {
        OreDictionary.registerOre("treeSapling", ModBlocks.SAKURA_SAPLING);
        OreDictionary.registerOre("treeSapling", ModBlocks.MULBERRY_SAPLING);
        OreDictionary.registerOre("logWood", ModBlocks.SAKURA_LOG);
        OreDictionary.registerOre("logWood", ModBlocks.MULBERRY_LOG);
        OreDictionary.registerOre("plankWood", ModBlocks.SAKURA_PLANKS);
        OreDictionary.registerOre("plankWood", ModBlocks.MULBERRY_PLANKS);
    }

    @Override
    public void init() {
        REPAIRABLE_TOOLS = Lists.newArrayList(ModItems.KATANA, ModItems.WAKIZASHI, ModItems.TANTO, ModItems.SHINAI, ModItems.YUMI, ModItems.SAMURAI_BOOTS, ModItems.SAMURAI_LEGGINGS, ModItems.SAMURAI_HELMET, ModItems.SAMURAI_CHESTPLATE);

        ModBlocks.MULBERRY_SAPLING.setLeaves(ModBlocks.MULBERRY_LEAVES.getDefaultState()).setLog(ModBlocks.MULBERRY_LOG.getDefaultState());
        ModBlocks.MULBERRY_LEAVES.setSapling(new ItemStack(ModBlocks.MULBERRY_SAPLING));

        ModBlocks.SAKURA_SAPLING.setLeaves(ModBlocks.SAKURA_LEAVES.getDefaultState()).setLog(ModBlocks.SAKURA_LOG.getDefaultState()).setBig(true);
        ModBlocks.SAKURA_LEAVES.setSapling(new ItemStack(ModBlocks.SAKURA_SAPLING));

        ModBlocks.connectPanes(ModBlocks.SHOJI, ModBlocks.FUSUMA);

        OreDictionary.registerOre("seed", new ItemStack(Items.WHEAT_SEEDS)); //I'll beat the shit out of HarvestCraft
        OreDictionary.registerOre("seed", new ItemStack(Items.PUMPKIN_SEEDS));
        OreDictionary.registerOre("seed", new ItemStack(Items.MELON_SEEDS));
        OreDictionary.registerOre("seed", new ItemStack(Items.BEETROOT_SEEDS));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.RICE));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.RUSH));
        OreDictionary.registerOre("seed", new ItemStack(ModBlocks.TEA));
        if (ModInteractions.bwm.isActive())
            OreDictionary.registerOre("seed", new ItemStack(BWMBlocks.HEMP));

        OreDictionary.registerOre("cropRice", ModItems.MATERIAL_JAPAN.getMaterial("rice"));
        OreDictionary.registerOre("seedRice", new ItemStack(ModBlocks.RICE));
        OreDictionary.registerOre("cropRush", ModItems.MATERIAL_JAPAN.getMaterial("rush"));
        OreDictionary.registerOre("seedRush", new ItemStack(ModBlocks.RUSH));
        TeaType.getTypesByItem(TeaType.ItemType.Leaves).stream().forEach(teaType -> OreDictionary.registerOre("cropTea", ModItems.TEA_LEAVES.getStack(teaType)));
        OreDictionary.registerOre("seedTea", new ItemStack(ModBlocks.TEA));
        OreDictionary.registerOre("materialBamboo", new ItemStack(ModBlocks.BAMBOO));


        OreDictionary.registerOre("barkWood", ModItems.MATERIAL_JAPAN.getMaterial("bark_mulberry"));
        OreDictionary.registerOre("barkWood", ModItems.MATERIAL_JAPAN.getMaterial("bark_sakura"));

        OreDictionary.registerOre("ingotTamahagane", ModItems.MATERIAL_JAPAN.getMaterial("tamahagane_finished"));
        OreDictionary.registerOre("ingotHochoTetsu", ModItems.MATERIAL_JAPAN.getMaterial("hocho_tetsu_finished"));

        Ingredient ironSandInput = IRON_PER_IRONSAND % 9 == 0 ? IngredientSized.fromOredict("blockIron",IRON_PER_IRONSAND/9) : IngredientSized.fromOredict("ingotIron",IRON_PER_IRONSAND);
        ItemStack ironSandOutput = IRON_PER_IRONSAND % 9 == 0 ? new ItemStack(Blocks.IRON_BLOCK,IRON_PER_IRONSAND/9) : new ItemStack(Items.IRON_INGOT,IRON_PER_IRONSAND);

        CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.IRON_SAND, 1)}, ironSandInput, SAND_PER_IRONSAND);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ironSandOutput, new ItemStack(Blocks.SAND, SAND_PER_IRONSAND)}, IngredientSized.fromBlock(ModBlocks.IRON_SAND, 1), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, new ItemStack(ModItems.SASHIMI, 3)}, IngredientSized.fromStacks(new ItemStack(Items.FISH, 1)), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModItems.FUGU_SAC, 1), new ItemStack(ModItems.PREPARED_PUFFER, 3)}, Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), 0);
        CraftingManagerFireNet.getInstance().addRecipe(new ItemStack[]{ModItems.MATERIAL_JAPAN.getMaterial("iron_scales", IRON_PER_IRONSAND * 3)}, Ingredient.fromStacks(new ItemStack(ModBlocks.IRON_SAND, 1)));
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.MATERIAL_JAPAN.getMaterial("washi", 9)}, Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("mulberry_sheet")), 0);
        TeaType.getTypesByItem(TeaType.ItemType.Wilted).stream().forEach(tea -> CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.TEA_POWDER.getStack(tea)}, new IngredientTea(tea, TeaType.ItemType.Wilted), 0));
        TeaType.getTypesByItem(TeaType.ItemType.Soaked).stream().forEach(tea -> CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.TEA_POWDER.getStack(tea)}, new IngredientTea(tea, TeaType.ItemType.Soaked), 0));
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.TEA_POWDER.getStack(TeaType.MATCHA)}, new IngredientTea(TeaType.TENCHA, TeaType.ItemType.Powder),0);

        BWRegistry.CAULDRON.addUnstokedRecipe(ModItems.MATERIAL_JAPAN.getMaterial("soaked_rice"),new ItemStack(ModItems.RICE));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(ModItems.MULBERRY,3)),Ingredient.fromItem(Items.SUGAR),new OreIngredient("foodFlour")),new ItemStack(ModItems.LAXATIVE,2));

        GameRegistry.addSmelting(ModItems.TEA_POWDER, ModItems.TEA_POWDER.getStack(TeaType.HOUJICHA), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.PREPARED_PUFFER), new ItemStack(ModItems.PREPARED_COOKED_PUFFER), 0.35f);
        GameRegistry.addSmelting(ModItems.MATERIAL_JAPAN.getMaterial("soaked_rice"), new ItemStack(ModItems.RICE), 0.35f);

        //if (GRASS_DROPS_SEEDS) {
        //    MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rice), 2);
        //    MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rush), 2);
        //}

        ItemStack sakuraPlanks = new ItemStack(ModBlocks.SAKURA_PLANKS);
        BWOreDictionary.woods.add(new Wood(new ItemStack(ModBlocks.SAKURA_LOG), sakuraPlanks,ModItems.MATERIAL_JAPAN.getMaterial("bark_sakura")));
        BWOreDictionary.woods.add(new Wood(new ItemStack(ModBlocks.MULBERRY_LOG),new ItemStack(ModBlocks.MULBERRY_PLANKS),ModItems.MATERIAL_JAPAN.getMaterial("bark_mulberry")){
            @Override
            public ItemStack getPlank(int count) {
                ItemStack copy = new ItemStack(ModBlocks.MULBERRY_PLANKS);
                copy.setCount((int)Math.ceil(count / (float) HCLumber.axePlankAmount));
                return copy;
            }
        });

        GameRegistry.addSmelting(ModItems.MATERIAL_JAPAN.getMaterial("rice_stalk"), ModItems.MATERIAL_JAPAN.getMaterial("rice_ash"), 0.1f);

        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"katana"),new ItemStack(ModItems.KATANA), "l", "l", "w", 'l', ModItems.MATERIAL_JAPAN.getMaterial("half_katana_blade"), 'w', ModItems.MATERIAL_JAPAN.getMaterial("tsuka")),8);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"wakizashi"),new ItemStack(ModItems.WAKIZASHI), "l", "l", "w", 'l', "ingotTamahagane", 'w', ModItems.MATERIAL_JAPAN.getMaterial("tsuka")),6);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"tanto"),new ItemStack(ModItems.TANTO), "l", "w", 'l', "ingotTamahagane", 'w', ModItems.MATERIAL_JAPAN.getMaterial("tsuka")),4);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"shinai"),new ItemStack(ModItems.SHINAI), "l", "l", "w", 'l', ModItems.MATERIAL_JAPAN.getMaterial("bamboo_slats"), 'w', ModItems.MATERIAL_JAPAN.getMaterial("tsuka")),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"ya"),new ItemStack(ModItems.YA, 12), "h", "l", "f", 'h', ModItems.MATERIAL_JAPAN.getMaterial("ya_head"), 'l', ModItems.MATERIAL_JAPAN.getMaterial("bamboo_slats"), 'f', new ItemStack(Items.FEATHER)),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"yumi"),new ItemStack(ModItems.YUMI), " ns", "l s", " us", 'n', ModItems.MATERIAL_JAPAN.getMaterial("yumi_top"), 'u', ModItems.MATERIAL_JAPAN.getMaterial("yumi_bottom"), 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STRING)),5);

        addArmorFinishRecipe("samurai_helmet",new ItemStack(ModItems.SAMURAI_HELMET), ModItems.MATERIAL_JAPAN.getMaterial("helmet_undecorated"), 5);
        addArmorFinishRecipe("samurai_chestplate",new ItemStack(ModItems.SAMURAI_CHESTPLATE), ModItems.MATERIAL_JAPAN.getMaterial("chest_undecorated"), 8);
        addArmorFinishRecipe("samurai_leggings",new ItemStack(ModItems.SAMURAI_LEGGINGS), ModItems.MATERIAL_JAPAN.getMaterial("legs_undecorated"), 7);
        addArmorFinishRecipe("samurai_boots",new ItemStack(ModItems.SAMURAI_BOOTS), ModItems.MATERIAL_JAPAN.getMaterial("boots_undecorated"), 4);

        HashMap<Material, BlockMini> sidingMaterials = MiniBlocks.MINI_MATERIAL_BLOCKS.get(MiniType.SIDING);

        ItemStack sakuraSiding = MiniBlocks.fromParent(sidingMaterials.get(Material.WOOD), ModBlocks.SAKURA_PLANKS.getDefaultState());
        Ingredient sakuraPlankOrSiding = Ingredient.fromStacks(sakuraPlanks,sakuraSiding);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"netted_screen"),new ItemStack(ModBlocks.NETTED_SCREEN), "bsb", "sss", "bsb", 's', "string", 'b', ModItems.MATERIAL_JAPAN.getMaterial("bamboo_slats")),2);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"tatara"),new ItemStack(ModBlocks.TATARA), "idi", "g g", "ini", 'i', "ingotIron", 'g', "ingotGold", 'd', "gemDiamond", 'n', new ItemStack(Blocks.NETHERRACK)),4);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"soaking_unit"),new ItemStack(ModBlocks.CHERRY_BOX, 1, 0), "pxp", "x x", "pxp", 'p', sakuraPlankOrSiding, 'x', new ItemStack(Blocks.IRON_BARS)),1);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"drying_unit"),new ItemStack(ModBlocks.CHERRY_BOX, 1, 1), "pxp", "p p", "ppp", 'p', sakuraPlankOrSiding, 'x', new ItemStack(Blocks.GLASS_PANE)),1);
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ResourceLocation(Reference.MOD_ID,"nabe"),new ItemStack(ModBlocks.NABE, 1), "i i", "i i", "lhl", 'i', "ingotIron", 'l', "ingotTamahagane", 'h', "ingotHochoTetsu"),1);

        //Random seeds
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(new OreIngredient("seed"), 1, ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public List<ItemStack> getOutput(List<ItemStack> inputs, TileEntity tile) {
                return Lists.newArrayList(getRandomSeed());
            }

            public ItemStack getRandomSeed() {
                int i = random.nextInt(3);
                switch(i)
                {
                    case(0):return new ItemStack(ModBlocks.RICE);
                    case(1):return new ItemStack(ModBlocks.RUSH);
                    case(2):return new ItemStack(ModBlocks.TEA);
                    default:return ItemStack.EMPTY;
                }
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.RICE) || item.getItem() == Item.getItemFromBlock(ModBlocks.RUSH) || item.getItem() == Item.getItemFromBlock(ModBlocks.TEA))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.RICE), new ItemStack(ModBlocks.RUSH), new ItemStack(ModBlocks.TEA));
            }
        });
        //Random saplings
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(new OreIngredient("treeSapling"), 1, ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public List<ItemStack> getOutput(List<ItemStack> inputs, TileEntity tile) {
                return Lists.newArrayList(getRandomSapling());
            }

            public ItemStack getRandomSapling() {
                if (random.nextInt(2) == 0)
                    return new ItemStack(ModBlocks.SAKURA_SAPLING);
                else
                    return new ItemStack(ModBlocks.MULBERRY_SAPLING);
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.SAKURA_SAPLING) || item.getItem() == Item.getItemFromBlock(ModBlocks.MULBERRY_SAPLING))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.SAKURA_SAPLING), new ItemStack(ModBlocks.MULBERRY_SAPLING));
            }
        });
        //Bamboo
        CraftingManagerInfuserTransmutation.getInstance().addRecipe(new OreIngredient("sugarcane"), 1, new ItemStack(ModBlocks.BAMBOO));
        //Repair tools and armor
        if (INFUSER_REPAIRS)
            CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(Ingredient.fromItem(ModItems.KATANA), 2, ItemStack.EMPTY) {
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
                public List<ItemStack> getOutput(List<ItemStack> inputs, TileEntity tile) {
                    return inputs.stream().map(ItemStack::copy).map(this::repair).collect(Collectors.toList());
                }

                private ItemStack repair(ItemStack stack) {
                    if (stack.isItemStackDamageable())
                        stack.setItemDamage(Math.max(0, stack.getItemDamage() - stack.getMaxDamage() / 20));
                    return stack;
                }
            });

        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.BAMBOO)), ModItems.MATERIAL_JAPAN.getMaterial("soaked_bamboo"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("rice")), ModItems.MATERIAL_JAPAN.getMaterial("soaked_rice"));
        if (ModInteractions.bwm.isActive())
            CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("bark_mulberry")), ModItems.MATERIAL_JAPAN.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.MULBERRY_LOG)), ModItems.MATERIAL_JAPAN.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 0)), new ItemStack(Blocks.SPONGE, 1, 1));
        TeaType.getTypesByItem(TeaType.ItemType.Soaked).stream().filter(this::hasLeaf).forEach(tea -> CraftingManagerSoakingBox.instance().addRecipe(new IngredientTea(tea, TeaType.ItemType.Leaves), ModItems.TEA_SOAKED.getStack(tea)));

        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("rice_stalk")), ModItems.MATERIAL_JAPAN.getMaterial("rice_hay"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("soaked_mulberry")), ModItems.MATERIAL_JAPAN.getMaterial("mulberry_paste"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("soaked_bamboo")), ModItems.MATERIAL_JAPAN.getMaterial("bamboo_slats"));
        CraftingManagerDryingBox.instance().addRecipe(Ingredient.fromStacks(new ItemStack(Blocks.SPONGE, 1, 1)), new ItemStack(Blocks.SPONGE, 1, 0));
        TeaType.getTypesByItem(TeaType.ItemType.Wilted).stream().filter(this::hasLeaf).forEach(tea -> CraftingManagerDryingBox.instance().addRecipe(new IngredientTea(tea, TeaType.ItemType.Leaves), ModItems.TEA_WILTED.getStack(tea)));

        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.IRON_SAND)), new ItemStack(ModBlocks.KERA));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("tamahagane")), ModItems.MATERIAL_JAPAN.getMaterial("tamahagane_heated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("tamahagane_wrapped")), ModItems.MATERIAL_JAPAN.getMaterial("tamahagane_reheated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromStacks(ModItems.MATERIAL_JAPAN.getMaterial("hocho_tetsu")), ModItems.MATERIAL_JAPAN.getMaterial("hocho_tetsu_heated"));
        CraftingManagerTatara.instance().addRecipe(Ingredient.fromItem(Items.CLAY_BALL), ModItems.TEA_CUP.getEmpty());

        Ingredient extender = new OreIngredient("dustRedstone");
        Ingredient amplifier = new OreIngredient("dustGlowstone");
        Ingredient projectile = new OreIngredient("gunpowder");
        Ingredient poison = Ingredient.fromItem(Items.SPIDER_EYE);
        if(HARDCORE_BREWING) {
            extender = ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.WITCH_WART);
            amplifier = ItemMaterial.getIngredient(ItemMaterial.EnumMaterial.BRIMSTONE);
            projectile = Ingredient.fromItem(BWMItems.CREEPER_OYSTER);
            poison = Ingredient.fromStacks(new ItemStack(Blocks.RED_MUSHROOM));
        }

        ArrayList<Ingredient> poisonIngredients = Lists.newArrayList(new OreIngredient("cropNetherWart"), projectile, extender, amplifier, poison, new OreIngredient("cropRush"));
        CraftingManagerNabe.getInstance().addRecipe(new ResourceLocation(Reference.MOD_ID,"poison"),new NabeResultPoison(12,12), poisonIngredients,1000);
        CraftingManagerNabe.getInstance().addRecipe(new TeaNabeRecipe());

        Ingredient teaA = Ingredient.fromStacks(ModItems.TEA_POWDER.getStack(TeaType.BANCHA));
        Ingredient teaB = Ingredient.fromStacks(ModItems.TEA_LEAVES.getStack(TeaType.WHITE));
        Ingredient teaGreen = Ingredient.fromStacks(ModItems.TEA_POWDER.getStack(TeaType.MATCHA));
        ArrayList<Ingredient> teaIngredients = Lists.newArrayList(teaA, teaA, teaB, TeaNabeRecipe.SUGAR, TeaNabeRecipe.MILK);
        ArrayList<Ingredient> ceremonialTeaIngredients = Lists.newArrayList(teaGreen, teaGreen, teaGreen, teaGreen, teaGreen, teaGreen);
        CraftingManagerNabe.getInstance().addVisualRecipe(new NabeRecipeVisual(teaIngredients) {
            @Override
            public String getInfo() {
                return "Mix different ingredients to make tea.";
            }
        });
        CraftingManagerNabe.getInstance().addVisualRecipe(new NabeRecipeVisual(ceremonialTeaIngredients) {
            @Override
            public String getInfo() {
                return "Ceremonial tea:\n3 random positive effects\n1 random negative effect.";
            }
        });
        CraftingManagerNabe.getInstance().addVisualRecipe(new NabeRecipeVisual(Lists.newArrayList(TeaNabeRecipe.MILK)) {
            @Override
            public String getInfo() {
                return "Tea additive:\n-1 negative effect";
            }
        });
        CraftingManagerNabe.getInstance().addVisualRecipe(new NabeRecipeVisual(Lists.newArrayList(TeaNabeRecipe.SUGAR)) {
            @Override
            public String getInfo() {
                return "Tea additive:\nimprove positive effect";
            }
        });
        CraftingManagerNabe.getInstance().addVisualRecipe(new NabeRecipeVisual(poisonIngredients,new IngredientSized(Ingredient.fromStacks(new ItemStack(ModItems.YA)),12),new ItemStack(ModItems.YA_POISONED,12)));
    }

    private boolean hasLeaf(TeaType type) {
        return type.hasType(TeaType.ItemType.Leaves);
    }

    private boolean isRepairableTool(ItemStack stack) {
        return REPAIRABLE_TOOLS.contains(stack.getItem()) && stack.isItemDamaged();
    }

    private void addArmorFinishRecipe(String group,ItemStack out, ItemStack in, int spirit) {
        ItemStack gold = new ItemStack(Items.GOLD_NUGGET);
        ItemStack dye = new ItemStack(Items.DYE,1,14);
        ItemStack washi = ModItems.MATERIAL_JAPAN.getMaterial("washi");
        //GameRegistry.addShapelessRecipe(out,gold,gold,gold,dye,in,dye,washi,washi,washi);
        CraftingManagerInfuser.getInstance().addRecipe(new ArmorDecorateRecipe(new ResourceLocation(Reference.MOD_ID,group), out, "ggg", "dad", "www", 'g', "nuggetGold", 'd', "dye", 'a', in, 'w', ModItems.MATERIAL_JAPAN.getMaterial("washi")),spirit);
    }

    private void addFoldingRecipe(ItemStack out, ItemStack in) {
        //GameRegistry.addShapedRecipe(out, "t", "s", 't', in, 's', new ItemStack(Items.STICK));
    }
}
