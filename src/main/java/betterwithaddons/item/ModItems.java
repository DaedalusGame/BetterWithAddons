package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.rbdtools.*;
import betterwithaddons.item.rbdtools.ItemSpade;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

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

    public static ItemGreatbow greatbow;
    public static ItemGreatarrow greatarrow;
    public static ItemMonument monument;
    public static ItemArtifactFrame artifactFrame;
    public static ItemModFood bakedMushroom;
    public static ItemModFood bakedAmanita;
    public static ItemModFood cookedBeetroot;
    public static ItemModFood bakedBeetroot;
    public static ItemModFood cookedCarrot;
    public static ItemModFood bakedCarrot;
    public static ItemModFood cookedPotato;
    public static ItemModFood pieMelon;
    public static ItemModFood pieMeat;
    public static ItemModFood pieMushroom;
    public static ItemModFood pieAmanita;
    public static ItemModFood meatballs;
    public static ItemModFood cookedEgg;
    public static ItemModFood groundMeat;

    public static Item soulSandPile;
    public static ItemColored wool;
    public static ItemFood rottenFood;

    public static ItemMaterial bowls;

    public static ItemModFood cookedClownfish;
    public static ItemModFood cookedPuffer;
    public static ItemModFood preparedPuffer;
    public static ItemModFood preparedCookedPuffer;
    public static ItemModFood fuguSac;
    public static ItemWorldScale worldShard;

    public static ItemAncestryBottle ancestryBottle;
    public static ItemModFood sashimi;
    public static ItemModFood rice;
    public static ItemModFood riceBowl;
    public static ItemStainedBrick stainedBrick;
    public static ItemMaterial material;
    public static ItemMaterial materialJapan;
    public static ItemMaterial materialBolt;
    public static ItemMaterial materialCongealed;
    public static ItemMaterial materialCrate;
    public static ItemMaterial materialBag;
    public static ItemMaterial materialBundle;
    public static ItemMaterial materialDeco;
    public static ItemMaterial materialTweak;
    public static ItemKatana katana;
    public static ItemWakizashi wakizashi;
    public static ItemTanto tanto;
    public static ItemShinai shinai;
    public static ItemYumi yumi;
    public static ItemYa ya;
    public static ItemSamuraiArmor samuraiHelm;
    public static ItemSamuraiArmor samuraiChestplate;
    public static ItemSamuraiArmor samuraiLeggings;
    public static ItemSamuraiArmor samuraiBoots;

    public static ItemToolShard brokenArtifact;

    public static ItemSpade ironSpade;
    public static ItemMachete ironMachete;
    public static ItemMatchPick ironMatchPick;
    public static ItemKukri ironKukri;
    public static ItemCarpenterSaw ironCarpenterSaw;
    public static ItemMasonPick ironMasonPick;

    public static ItemSpade goldSpade;
    public static ItemMachete goldMachete;
    public static ItemMatchPick goldMatchPick;
    public static ItemKukri goldKukri;
    public static ItemCarpenterSaw goldCarpenterSaw;
    public static ItemMasonPick goldMasonPick;

    public static ItemSpade diamondSpade;
    public static ItemMachete diamondMachete;
    public static ItemMatchPick diamondMatchPick;
    public static ItemKukri diamondKukri;
    public static ItemCarpenterSaw diamondCarpenterSaw;
    public static ItemMasonPick diamondMasonPick;

    public static ItemSpade steelSpade;
    public static ItemMachete steelMachete;
    public static ItemMatchPick steelMatchPick;
    public static ItemKukri steelKukri;
    public static ItemCarpenterSaw steelCarpenterSaw;
    public static ItemMasonPick steelMasonPick;

    public static Item explosion;

    public static void load(FMLPreInitializationEvent event) {
        /*arrowhead = registerItem("arrowhead",new Item());
        midori = registerItem("midori",new Item());
        midori_popped = registerItem("midori_popped",new Item());
        stone_brick = registerItem("stone_brick",new Item());
        bone_ingot = registerItem("bone_ingot",new Item());
        ender_cream = registerItem("ender_cream",new Item());
        thornrose = registerItem("thornrose",new Item());*/

        ironSpade = (ItemSpade) registerItem("iron_spade", new ItemSpade(Item.ToolMaterial.IRON));
        ironMatchPick = (ItemMatchPick) registerItem("iron_matchpick", new ItemMatchPick(Item.ToolMaterial.IRON));
        ironMachete = (ItemMachete) registerItem("iron_machete", new ItemMachete(Item.ToolMaterial.IRON));
        ironKukri = (ItemKukri) registerItem("iron_kukri", new ItemKukri(Item.ToolMaterial.IRON,8.0f,-3.1f));
        ironCarpenterSaw = (ItemCarpenterSaw) registerItem("iron_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.IRON,8.0f,-3.1f));
        ironMasonPick = (ItemMasonPick) registerItem("iron_masonpick", new ItemMasonPick(Item.ToolMaterial.IRON));

        goldSpade = (ItemSpade) registerItem("gold_spade", new ItemSpade(Item.ToolMaterial.GOLD));
        goldMatchPick = (ItemMatchPick) registerItem("gold_matchpick", new ItemMatchPick(Item.ToolMaterial.GOLD));
        goldMachete = (ItemMachete) registerItem("gold_machete", new ItemMachete(Item.ToolMaterial.GOLD));
        goldKukri = (ItemKukri) registerItem("gold_kukri", new ItemKukri(Item.ToolMaterial.GOLD,6.0f,-3.0f));
        goldCarpenterSaw = (ItemCarpenterSaw) registerItem("gold_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.GOLD,6.0f,-3.0f));
        goldMasonPick = (ItemMasonPick) registerItem("gold_masonpick", new ItemMasonPick(Item.ToolMaterial.GOLD));

        diamondSpade = (ItemSpade) registerItem("diamond_spade", new ItemSpade(Item.ToolMaterial.DIAMOND));
        diamondMatchPick = (ItemMatchPick) registerItem("diamond_matchpick", new ItemMatchPick(Item.ToolMaterial.DIAMOND));
        diamondMachete = (ItemMachete) registerItem("diamond_machete", new ItemMachete(Item.ToolMaterial.DIAMOND));
        diamondKukri = (ItemKukri) registerItem("diamond_kukri", new ItemKukri(Item.ToolMaterial.DIAMOND,8.0f,-3.0f));
        diamondCarpenterSaw = (ItemCarpenterSaw) registerItem("diamond_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.DIAMOND,8.0f,-3.0f));
        diamondMasonPick = (ItemMasonPick) registerItem("diamond_masonpick", new ItemMasonPick(Item.ToolMaterial.DIAMOND));

        steelSpade = (ItemSpade) registerItem("steel_spade", new ItemSpade(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMatchPick = (ItemMatchPick) registerItem("steel_matchpick", new ItemMatchPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMachete = (ItemMachete) registerItem("steel_machete", new ItemMachete(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelKukri = (ItemKukri) registerItem("steel_kukri", new ItemKukri(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelCarpenterSaw = (ItemCarpenterSaw) registerItem("steel_carpentersaw", new ItemCarpenterSaw(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        steelMasonPick = (ItemMasonPick) registerItem("steel_masonpick", new ItemMasonPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair,"ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });

        material = (ItemMaterial) registerItem("material", new ItemMaterial(
                new String[]{"arrowhead", "midori", "midori_popped", "thornrose", "stone_brick", "bone_ingot", "ender_cream"}
        ));

        worldShard = (ItemWorldScale) registerItem("worldshard", new ItemWorldScale());
        greatbow = (ItemGreatbow) registerItem("greatbow", new ItemGreatbow());
        greatarrow = (ItemGreatarrow) registerItem("greatarrow", new ItemGreatarrow());
        monument = (ItemMonument) registerItem("monument", new ItemMonument());
        //Food
        bakedMushroom = (ItemModFood) registerItem("food_mushroom_baked", new ItemModFood(3, 0.2F, false));
        bakedAmanita = (ItemModFood) registerItem("food_amanita_baked", new ItemModFood(3, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        cookedBeetroot = (ItemModFood) registerItem("food_beetroot_cooked", new ItemModFood(4, 0.5F, false));
        bakedBeetroot = (ItemModFood) registerItem("food_beetroot_baked", new ItemModFood(5, 0.4F, false));
        cookedCarrot = (ItemModFood) registerItem("food_carrot_cooked", new ItemModFood(3, 0.6F, false));
        bakedCarrot = (ItemModFood) registerItem("food_carrot_baked", new ItemModFood(4, 0.5F, false));
        cookedPotato = (ItemModFood) registerItem("food_potato_cooked", new ItemModFood(4, 0.7F, false));
        cookedEgg = (ItemModFood) registerItem("food_egg_cooked", new ItemModFood(4, 0.3F, false));
        meatballs = (ItemModFood) registerItem("food_meatballs", new ItemModFood(3, 0.6F, true));

        groundMeat = (ItemModFood) registerItem("food_ground_meat", new ItemModFood(1, 0.1F, true));

        pieMushroom = (ItemModFood) registerItem("food_pie_mushroom", new ItemModFood(8, 0.3F, false));
        pieAmanita = (ItemModFood) registerItem("food_pie_amanita", new ItemModFood(8, 0.3F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        pieMeat = (ItemModFood) registerItem("food_pie_meat", new ItemModFood(9, 0.5F, true));
        pieMelon = (ItemModFood) registerItem("food_pie_melon", new ItemModFood(8, 0.4F, false));

        cookedClownfish = (ItemModFood) registerItem("food_clownfish_cooked", new ItemModFood(6, 0.5F, false));
        cookedPuffer = (ItemModFood) registerItem("food_pufferfish_baked", new ItemModFood(6, 0.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 500, 1), 0.1F));
        preparedPuffer = (ItemModFood) registerItem("food_pufferfish_prepared", new ItemModFood(2, 0.1F, false));
        preparedCookedPuffer = (ItemModFood) registerItem("food_pufferfish_cooked", new ItemModFood(4, 0.5F, false));
        fuguSac = (ItemModFood) registerItem("food_fugu_sac", new ItemModFood(2, 0.1F, false).setPotionEffect(new PotionEffect(MobEffects.WITHER, 2000, 1), 1.0F));

        sashimi = (ItemModFood) registerItem("food_sashimi", new ItemModFood(2, 0.1F, false));
        rice = (ItemModFood) registerItem("food_cooked_rice", new ItemModFood(2, 0.3F, false));
        riceBowl = (ItemModFood) registerItem("food_bowl_rice", new ItemModFood(9, 0.6F, false).setMaxStackSize(1));

        soulSandPile = registerItem("soulsand_pile", new Item());
        rottenFood = (ItemFood)registerItem("rotten_food", new ItemFood(1,0.1f,false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 2000, 1), 1.0F));
        wool = (ItemColored)registerItem("wool",new ItemColored());

        bowls = (ItemMaterial) registerItem("bowl", new ItemMaterial(
                new String[]{"salt"}
        ));
        bowls.setContainer(new ItemStack(Items.BOWL));
        OreDictionary.registerOre("foodSalt", bowls.getMaterial("salt"));

        artifactFrame = (ItemArtifactFrame) registerItem("artifact_frame", new ItemArtifactFrame());
        brokenArtifact = (ItemToolShard) registerItem("tool_shard", new ItemToolShard().setMaxStackSize(1));

        stainedBrick = (ItemStainedBrick) registerItem("brick_stained", new ItemStainedBrick());

        shinai = (ItemShinai) registerItem("shinai", new ItemShinai());
        katana = (ItemKatana) registerItem("katana", new ItemKatana());
        wakizashi = (ItemWakizashi) registerItem("wakizashi", new ItemWakizashi());
        tanto = (ItemTanto) registerItem("tanto", new ItemTanto());
        yumi = (ItemYumi) registerItem("yumi", new ItemYumi());
        ya = (ItemYa) registerItem("ya", new ItemYa());
        ancestryBottle = (ItemAncestryBottle) registerItem("ancestry_bottle", new ItemAncestryBottle());

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
                new String[]{"pork", "pork_raw", "chicken", "chicken_raw", "steak", "steak_raw", "mutton", "mutton_raw", "rabbit", "rabbit_raw", "egg", "slime", "enderpearl"}
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

        explosion = registerItem("explosion", new Item() {
            @Override
            public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
                //NOOP
            }
        });
    }

    private static Item registerItem(String name, Item item) {
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        GameRegistry.register(item, new ResourceLocation(Reference.MOD_ID, name));
        LIST.add(item);

        return item;
    }
}
