package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.rbdtools.*;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.ItemUtil;
import betterwithaddons.util.TeaType;
import betterwithmods.common.BWMItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModItems {
    public static ArrayList<Item> LIST = new ArrayList<Item>();

    public static Item.ToolMaterial bambooToolMaterial = EnumHelper.addToolMaterial("bamboo", 0, 51, 0.5f, -4.0f, 1);

    {
        bambooToolMaterial.setRepairItem(materialJapan.getMaterial("bamboo_slats"));
    }

    public static Item.ToolMaterial tamahaganeToolMaterial = EnumHelper.addToolMaterial("tamahagane", 3, 1210, 8.0f, 2.5f, 9);

    {
        tamahaganeToolMaterial.setRepairItem(materialJapan.getMaterial("tamahagane_finished"));
    }

    public static Item.ToolMaterial japansteelToolMaterial = EnumHelper.addToolMaterial("japansteel", 3, 1710, 8.0f, 4.0f, 10);

    {
        japansteelToolMaterial.setRepairItem(materialJapan.getMaterial("tamahagane_finished"));
    }

    public static ItemArmor.ArmorMaterial samuraiArmorMaterial = EnumHelper.addArmorMaterial("SAMURAI", "samurai", 16,
            new int[]{2, 6, 5, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

    @GameRegistry.ObjectHolder("betterwithaddons:greatbow")
    public static ItemGreatbow greatbow;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow")
    public static ItemGreatarrow greatarrow;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow_lightning")
    public static ItemGreatarrow greatarrowLightning;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow_destruction")
    public static ItemGreatarrow greatarrowDestruction;
    @GameRegistry.ObjectHolder("betterwithaddons:monument")
    public static ItemMonument monument;
    @GameRegistry.ObjectHolder("betterwithaddons:artifact_frame")
    public static ItemArtifactFrame artifactFrame;
    @GameRegistry.ObjectHolder("betterwithaddons:food_mushroom_baked")
    public static ItemFood bakedMushroom;
    @GameRegistry.ObjectHolder("betterwithaddons:food_amanita_baked")
    public static ItemFood bakedAmanita;
    @GameRegistry.ObjectHolder("betterwithaddons:food_beetroot_cooked")
    public static ItemFood cookedBeetroot;
    @GameRegistry.ObjectHolder("betterwithaddons:food_beetroot_baked")
    public static ItemFood bakedBeetroot;
    @GameRegistry.ObjectHolder("betterwithaddons:food_carrot_cooked")
    public static ItemFood cookedCarrot;
    @GameRegistry.ObjectHolder("betterwithaddons:food_carrot_baked")
    public static ItemFood bakedCarrot;
    @GameRegistry.ObjectHolder("betterwithaddons:food_potato_cooked")
    public static ItemFood cookedPotato;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_melon")
    public static ItemFood pieMelon;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_meat")
    public static ItemFood pieMeat;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_mushroom")
    public static ItemFood pieMushroom;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_amanita")
    public static ItemFood pieAmanita;
    @GameRegistry.ObjectHolder("betterwithaddons:food_meatballs")
    public static ItemFood meatballs;
    @GameRegistry.ObjectHolder("betterwithaddons:food_egg_cooked")
    public static ItemFood cookedEgg;
    @GameRegistry.ObjectHolder("betterwithaddons:food_ground_meat")
    public static ItemFood groundMeat;

    @GameRegistry.ObjectHolder("betterwithaddons:soulsand_pile")
    public static Item soulSandPile;
    @GameRegistry.ObjectHolder("betterwithaddons:wool")
    public static ItemColored wool;
    @GameRegistry.ObjectHolder("betterwithaddons:rotten_food")
    public static ItemFood rottenFood;

    @GameRegistry.ObjectHolder("betterwithaddons:salt")
    public static ItemMaterial salts;

    @GameRegistry.ObjectHolder("betterwithaddons:food_clownfish_cooked")
    public static ItemFood cookedClownfish;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_baked")
    public static ItemFood cookedPuffer;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_prepared")
    public static ItemFood preparedPuffer;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_cooked")
    public static ItemFood preparedCookedPuffer;
    @GameRegistry.ObjectHolder("betterwithaddons:food_fugu_sac")
    public static ItemFood fuguSac;
    @GameRegistry.ObjectHolder("betterwithaddons:worldshard")
    public static ItemWorldScale worldShard;

    @GameRegistry.ObjectHolder("betterwithaddons:ancestry_bottle")
    public static ItemAncestryBottle ancestryBottle;
    @GameRegistry.ObjectHolder("betterwithaddons:food_sashimi")
    public static ItemFood sashimi;
    @GameRegistry.ObjectHolder("betterwithaddons:food_mulberry")
    public static ItemFood mulberry;
    @GameRegistry.ObjectHolder("betterwithaddons:food_cooked_rice")
    public static ItemFood rice;
    @GameRegistry.ObjectHolder("betterwithaddons:food_bowl_rice")
    public static ItemFood riceBowl;
    @GameRegistry.ObjectHolder("betterwithaddons:laxative")
    public static Item laxative;
    @GameRegistry.ObjectHolder("betterwithaddons:japanmat")
    public static ItemMaterial materialJapan;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_leaves")
    public static ItemTea teaLeaves;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_soaked")
    public static ItemTea teaSoaked;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_wilted")
    public static ItemTea teaWilted;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_powder")
    public static ItemTea teaPowder;
    @GameRegistry.ObjectHolder("betterwithaddons:teacup")
    public static ItemTeaCup teaCup;

    @GameRegistry.ObjectHolder("betterwithaddons:brick_stained")
    public static ItemStainedBrick stainedBrick;
    @GameRegistry.ObjectHolder("betterwithaddons:material")
    public static ItemMaterial material;
    @GameRegistry.ObjectHolder("betterwithaddons:bolt")
    public static ItemMaterial materialBolt;
    @GameRegistry.ObjectHolder("betterwithaddons:congealed")
    public static ItemMaterial materialCongealed;
    @GameRegistry.ObjectHolder("betterwithaddons:crate")
    public static ItemMaterial materialCrate;
    @GameRegistry.ObjectHolder("betterwithaddons:bag")
    public static ItemMaterial materialBag;
    @GameRegistry.ObjectHolder("betterwithaddons:bundle")
    public static ItemMaterial materialBundle;
    @GameRegistry.ObjectHolder("betterwithaddons:decomat")
    public static ItemMaterial materialDeco;
    @GameRegistry.ObjectHolder("betterwithaddons:wheatmat")
    public static ItemMaterial materialWheat;

    @GameRegistry.ObjectHolder("betterwithaddons:tweakmat")
    public static ItemMaterial materialTweak;
    @GameRegistry.ObjectHolder("betterwithaddons:ink_and_quill")
    public static ItemInkAndQuill inkAndQuill;

    @GameRegistry.ObjectHolder("betterwithaddons:katana")
    public static ItemKatana katana;
    @GameRegistry.ObjectHolder("betterwithaddons:wakizashi")
    public static ItemWakizashi wakizashi;
    @GameRegistry.ObjectHolder("betterwithaddons:tanto")
    public static ItemTanto tanto;
    @GameRegistry.ObjectHolder("betterwithaddons:shinai")
    public static ItemShinai shinai;
    @GameRegistry.ObjectHolder("betterwithaddons:yumi")
    public static ItemYumi yumi;
    @GameRegistry.ObjectHolder("betterwithaddons:ya")
    public static ItemYa ya;
    @GameRegistry.ObjectHolder("betterwithaddons:poisoned_ya")
    public static ItemPoisonedYa yaPoisoned;
    @GameRegistry.ObjectHolder("betterwithaddons:helmet_samurai")
    public static ItemSamuraiArmor samuraiHelm;
    @GameRegistry.ObjectHolder("betterwithaddons:chest_samurai")
    public static ItemSamuraiArmor samuraiChestplate;
    @GameRegistry.ObjectHolder("betterwithaddons:legs_samurai")
    public static ItemSamuraiArmor samuraiLeggings;
    @GameRegistry.ObjectHolder("betterwithaddons:boots_samurai")
    public static ItemSamuraiArmor samuraiBoots;

    @GameRegistry.ObjectHolder("betterwithaddons:tool_shard")
    public static ItemToolShard brokenArtifact;

    @GameRegistry.ObjectHolder("betterwithaddons:iron_spade")
    public static ItemSpade ironSpade;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_machete")
    public static ItemMachete ironMachete;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_matchpick")
    public static ItemMatchPick ironMatchPick;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_kukri")
    public static ItemKukri ironKukri;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_carpentersaw")
    public static ItemCarpenterSaw ironCarpenterSaw;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_masonpick")
    public static ItemMasonPick ironMasonPick;

    @GameRegistry.ObjectHolder("betterwithaddons:gold_spade")
    public static ItemSpade goldSpade;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_machete")
    public static ItemMachete goldMachete;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_matchpick")
    public static ItemMatchPick goldMatchPick;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_kukri")
    public static ItemKukri goldKukri;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_carpentersaw")
    public static ItemCarpenterSaw goldCarpenterSaw;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_masonpick")
    public static ItemMasonPick goldMasonPick;

    @GameRegistry.ObjectHolder("betterwithaddons:diamond_spade")
    public static ItemSpade diamondSpade;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_machete")
    public static ItemMachete diamondMachete;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_matchpick")
    public static ItemMatchPick diamondMatchPick;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_kukri")
    public static ItemKukri diamondKukri;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_carpentersaw")
    public static ItemCarpenterSaw diamondCarpenterSaw;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_masonpick")
    public static ItemMasonPick diamondMasonPick;

    @GameRegistry.ObjectHolder("betterwithaddons:steel_spade")
    public static ItemSpade steelSpade;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_machete")
    public static ItemMachete steelMachete;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_matchpick")
    public static ItemMatchPick steelMatchPick;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_kukri")
    public static ItemKukri steelKukri;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_carpentersaw")
    public static ItemCarpenterSaw steelCarpenterSaw;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_masonpick")
    public static ItemMasonPick steelMasonPick;

    @GameRegistry.ObjectHolder("betterwithaddons:explosion")
    public static Item explosion;

    public static void load(FMLPreInitializationEvent event) {
        ironSpade = (ItemSpade) registerItem("iron_spade", new ItemSpade(Item.ToolMaterial.IRON));
        ironMatchPick = (ItemMatchPick) registerItem("iron_matchpick", new ItemMatchPick(Item.ToolMaterial.IRON));
        ironMachete = (ItemMachete) registerItem("iron_machete", new ItemMachete(Item.ToolMaterial.IRON));
        ironKukri = (ItemKukri) registerItem("iron_kukri", new ItemKukri(Item.ToolMaterial.IRON, 8.0f, -3.1f));
        ironCarpenterSaw = (ItemCarpenterSaw) registerItem("iron_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.IRON, 8.0f, -3.1f));
        ironMasonPick = (ItemMasonPick) registerItem("iron_masonpick", new ItemMasonPick(Item.ToolMaterial.IRON));

        goldSpade = (ItemSpade) registerItem("gold_spade", new ItemSpade(Item.ToolMaterial.GOLD));
        goldMatchPick = (ItemMatchPick) registerItem("gold_matchpick", new ItemMatchPick(Item.ToolMaterial.GOLD));
        goldMachete = (ItemMachete) registerItem("gold_machete", new ItemMachete(Item.ToolMaterial.GOLD));
        goldKukri = (ItemKukri) registerItem("gold_kukri", new ItemKukri(Item.ToolMaterial.GOLD, 6.0f, -3.0f));
        goldCarpenterSaw = (ItemCarpenterSaw) registerItem("gold_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.GOLD, 6.0f, -3.0f));
        goldMasonPick = (ItemMasonPick) registerItem("gold_masonpick", new ItemMasonPick(Item.ToolMaterial.GOLD));

        diamondSpade = (ItemSpade) registerItem("diamond_spade", new ItemSpade(Item.ToolMaterial.DIAMOND));
        diamondMatchPick = (ItemMatchPick) registerItem("diamond_matchpick", new ItemMatchPick(Item.ToolMaterial.DIAMOND));
        diamondMachete = (ItemMachete) registerItem("diamond_machete", new ItemMachete(Item.ToolMaterial.DIAMOND));
        diamondKukri = (ItemKukri) registerItem("diamond_kukri", new ItemKukri(Item.ToolMaterial.DIAMOND, 8.0f, -3.0f));
        diamondCarpenterSaw = (ItemCarpenterSaw) registerItem("diamond_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.DIAMOND, 8.0f, -3.0f));
        diamondMasonPick = (ItemMasonPick) registerItem("diamond_masonpick", new ItemMasonPick(Item.ToolMaterial.DIAMOND));

        steelSpade = (ItemSpade) registerItem("steel_spade", new ItemSpade(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMatchPick = (ItemMatchPick) registerItem("steel_matchpick", new ItemMatchPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMachete = (ItemMachete) registerItem("steel_machete", new ItemMachete(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelKukri = (ItemKukri) registerItem("steel_kukri", new ItemKukri(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelCarpenterSaw = (ItemCarpenterSaw) registerItem("steel_carpentersaw", new ItemCarpenterSaw(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMasonPick = (ItemMasonPick) registerItem("steel_masonpick", new ItemMasonPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });

        material = (ItemMaterial) registerItem("material", new ItemMaterial(
                new String[]{"arrowhead", "midori", "midori_popped", "thornrose", "stone_brick", "bone_ingot", "ender_cream"}
        ));

        worldShard = (ItemWorldScale) registerItem("worldshard", new ItemWorldScale());
        greatbow = (ItemGreatbow) registerItem("greatbow", new ItemGreatbow());
        greatarrow = (ItemGreatarrow) registerItem("greatarrow", new ItemGreatarrow());
        greatarrowLightning = (ItemGreatarrow) registerItem("greatarrow_lightning", new ItemGreatarrowLightning());
        greatarrowDestruction = (ItemGreatarrow) registerItem("greatarrow_destruction", new ItemGreatarrowDestruction());
        monument = (ItemMonument) registerItem("monument", new ItemMonument());
        //Food
        bakedMushroom = (ItemFood) registerItem("food_mushroom_baked", new ItemFood(3, 0.2F, false));
        bakedAmanita = (ItemFood) registerItem("food_amanita_baked", new ItemFood(3, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        cookedBeetroot = (ItemFood) registerItem("food_beetroot_cooked", new ItemFood(4, 0.5F, false));
        bakedBeetroot = (ItemFood) registerItem("food_beetroot_baked", new ItemFood(5, 0.4F, false));
        cookedCarrot = (ItemFood) registerItem("food_carrot_cooked", new ItemFood(3, 0.6F, false));
        bakedCarrot = (ItemFood) registerItem("food_carrot_baked", new ItemFood(4, 0.5F, false));
        cookedPotato = (ItemFood) registerItem("food_potato_cooked", new ItemFood(4, 0.7F, false));
        cookedEgg = (ItemFood) registerItem("food_egg_cooked", new ItemFood(4, 0.3F, false));
        meatballs = (ItemFood) registerItem("food_meatballs", new ItemFood(3, 0.6F, true));

        groundMeat = (ItemFood) registerItem("food_ground_meat", new ItemFood(1, 0.1F, true));

        pieMushroom = (ItemFood) registerItem("food_pie_mushroom", new ItemFood(8, 0.3F, false));
        pieAmanita = (ItemFood) registerItem("food_pie_amanita", new ItemFood(8, 0.3F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        pieMeat = (ItemFood) registerItem("food_pie_meat", new ItemFood(9, 0.5F, true));
        pieMelon = (ItemFood) registerItem("food_pie_melon", new ItemFood(8, 0.4F, false));

        cookedClownfish = (ItemFood) registerItem("food_clownfish_cooked", new ItemFood(6, 0.5F, false));
        cookedPuffer = (ItemFood) registerItem("food_pufferfish_baked", new ItemFood(6, 0.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 500, 1), 0.1F));
        preparedPuffer = (ItemFood) registerItem("food_pufferfish_prepared", new ItemFood(2, 0.1F, false));
        preparedCookedPuffer = (ItemFood) registerItem("food_pufferfish_cooked", new ItemFood(4, 0.5F, false));
        fuguSac = (ItemFood) registerItem("food_fugu_sac", new ItemFood(2, 0.1F, false).setPotionEffect(new PotionEffect(MobEffects.WITHER, 2000, 1), 1.0F));

        sashimi = (ItemFood) registerItem("food_sashimi", new ItemFood(2, 0.1F, false));
        mulberry = (ItemFood) registerItem("food_mulberry", new ItemFood(1, 0.2F, false));
        rice = (ItemFood) registerItem("food_cooked_rice", new ItemFood(2, 0.3F, false));
        riceBowl = (ItemFood) registerItem("food_bowl_rice", new ItemFood(9, 0.6F, false).setMaxStackSize(1));
        laxative = registerItem("laxative", new ItemLaxative(0,0,true));
        teaLeaves = (ItemTea) registerItem("tea_leaves", new ItemTea(TeaType.ItemType.Leaves));
        teaSoaked = (ItemTea) registerItem("tea_soaked", new ItemTea(TeaType.ItemType.Soaked));
        teaWilted = (ItemTea) registerItem("tea_wilted", new ItemTea(TeaType.ItemType.Wilted));
        teaPowder = (ItemTea) registerItem("tea_powder", new ItemTea(TeaType.ItemType.Powder));

        soulSandPile = registerItem("soulsand_pile", new Item());
        rottenFood = (ItemFood)registerItem("rotten_food", new ItemFood(1,0.1f,false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 2000, 1), 1.0F));
        wool = (ItemColored)registerItem("wool",new ItemColored());

        /*bowls = (ItemMaterial) registerItem("bowl", new ItemMaterial(
                new String[]{"salt"}
        ));*/
        salts = (ItemMaterial) registerItem("salt", new ItemMaterial(
                new String[]{"crystal"}
        ));

        artifactFrame = (ItemArtifactFrame) registerItem("artifact_frame", new ItemArtifactFrame());
        brokenArtifact = (ItemToolShard) registerItem("tool_shard", new ItemToolShard().setMaxStackSize(1));

        stainedBrick = (ItemStainedBrick) registerItem("brick_stained", new ItemStainedBrick());

        shinai = (ItemShinai) registerItem("shinai", new ItemShinai());
        katana = (ItemKatana) registerItem("katana", new ItemKatana());
        wakizashi = (ItemWakizashi) registerItem("wakizashi", new ItemWakizashi());
        tanto = (ItemTanto) registerItem("tanto", new ItemTanto());
        yumi = (ItemYumi) registerItem("yumi", new ItemYumi());
        ya = (ItemYa) registerItem("ya", new ItemYa());
        yaPoisoned = (ItemPoisonedYa) registerItem("poisoned_ya", new ItemPoisonedYa());
        ancestryBottle = (ItemAncestryBottle) registerItem("ancestry_bottle", new ItemAncestryBottle());
        teaCup = (ItemTeaCup) registerItem("teacup", new ItemTeaCup());

        materialJapan = (ItemMaterial) registerItem("japanmat", new ItemMaterial(
                new String[]{"rice", "soaked_rice", "rice_stalk", "rice_hay", "rice_ash", "rush",
                        "soaked_bamboo", "bamboo_slats", "soaked_mulberry", "mulberry_paste", "mulberry_sheet", "washi",
                        "iron_scales", "lamellar", "paper_lamellar", "tsuka", "half_katana_blade", "ya_head", "yumi_top", "yumi_bottom",
                        "tamahagane", "tamahagane_heated", "tamahagane_folded", "tamahagane_reheated", "tamahagane_finished", "tamahagane_wrapped",
                        "hocho_tetsu", "hocho_tetsu_heated", "hocho_tetsu_fold_1", "hocho_tetsu_fold_2", "hocho_tetsu_finished",
                        "helmet_undecorated", "chest_undecorated", "legs_undecorated", "boots_undecorated", "bark_sakura", "bark_mulberry"}
        ));

        samuraiHelm = (ItemSamuraiArmor) registerItem("helmet_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.HEAD));
        samuraiChestplate = (ItemSamuraiArmor) registerItem("chest_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.CHEST));
        samuraiLeggings = (ItemSamuraiArmor) registerItem("legs_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.LEGS));
        samuraiBoots = (ItemSamuraiArmor) registerItem("boots_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.FEET));

        materialBag = (ItemMaterial) registerItem("bag", new ItemMaterial(
                new String[]{"seed", "seed_hemp", "seed_melon", "seed_pumpkin", "seed_beets", "cocoa", "redstone", "glowstone", "sugar", "gunpowder", "flour", "sulfur", "nitre", "sawdust", "sawdust_soul", "potash", "hellfire", "kibble"}
        ));
        materialCrate = (ItemMaterial) registerItem("crate", new ItemMaterial(
                new String[]{"pork", "pork_raw", "chicken", "chicken_raw", "steak", "steak_raw", "mutton", "mutton_raw", "rabbit", "rabbit_raw", "egg", "slime", "enderpearl", "cactus", "inksac"}
        ));
        materialCongealed = (ItemMaterial) registerItem("congealed", new ItemMaterial(
                new String[]{"bone", "flesh", "eye", "amanita", "mushroom", "wart"}
        ));
        materialBolt = (ItemMaterial) registerItem("bolt", new ItemMaterial(
                new String[]{"fabric", "vine", "paper", "leather", "scoured_leather", "tanned_leather", "string"}
        ));
        materialBundle = (ItemMaterial) registerItem("bundle", new ItemMaterial(
                new String[]{"feather", "arrows", "blazerods", "oak", "birch", "spruce", "jungle", "acacia", "darkoak"}
        ));

        materialDeco = (ItemMaterial) registerItem("decomat", new ItemMaterial(
                new String[]{"hemp_oil", "wood_bleach", "wood_stain", "glass_chunk"}
        ));

        materialTweak = (ItemMaterial) registerItem("tweakmat", new ItemMaterial(
                new String[]{"ash", "ink_and_quill"}
        ));
        inkAndQuill = (ItemInkAndQuill) registerItem("ink_and_quill", new ItemInkAndQuill());

        materialWheat = (ItemMaterial) registerItem("wheatmat", new ItemMaterial(
                new String[]{"hay"}
        ));

        explosion = registerItem("explosion", new Item() {
            @Override
            public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
                return;
            }
        });
    }

    @SubscribeEvent
    public static void registryEvent(RegistryEvent.Register<Item> event) {
        for (Item item : LIST) {
            event.getRegistry().register(item);
        }
    }

    public static Item registerItem(String name, Item item) {
        if(item.getRegistryName() == null)
            item.setRegistryName(Reference.MOD_ID,name);
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        LIST.add(item);

        return item;
    }
}
