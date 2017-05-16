package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

public class ModItems
{
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
            new int[] { 2, 6, 5, 2 }, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

    public static ItemGreatbow greatbow;
    public static ItemGreatarrow greatarrow;
    public static ItemMonument monument;
    public static ItemFood bakedMushroom;
    public static ItemFood bakedAmanita;
    public static ItemFood cookedBeetroot;
    public static ItemFood bakedBeetroot;
    public static ItemFood cookedCarrot;
    public static ItemFood bakedCarrot;
    public static ItemFood cookedPotato;
    public static ItemFood pieMelon;
    public static ItemFood pieMeat;
    public static ItemFood pieMushroom;
    public static ItemFood pieAmanita;
    public static ItemFood meatballs;
    public static ItemFood cookedEgg;
    public static ItemFood groundMeat;

    public static ItemFood cookedClownfish;
    public static ItemFood cookedPuffer;
    public static ItemFood preparedPuffer;
    public static ItemFood preparedCookedPuffer;
    public static ItemFood fuguSac;

    public static ItemFood sashimi;
    public static ItemFood rice;
    public static ItemFood riceBowl;
    public static ItemStainedBrick stainedBrick;
    public static ItemMaterial material;
    public static ItemMaterial materialJapan;
    public static ItemMaterial materialBolt;
    public static ItemMaterial materialCongealed;
    public static ItemMaterial materialCrate;
    public static ItemMaterial materialBag;
    public static ItemMaterial materialBundle;
    public static ItemMaterial materialDeco;
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

    public static void load(FMLPreInitializationEvent event)
    {
        /*arrowhead = registerItem("arrowhead",new Item());
        midori = registerItem("midori",new Item());
        midori_popped = registerItem("midori_popped",new Item());
        stone_brick = registerItem("stone_brick",new Item());
        bone_ingot = registerItem("bone_ingot",new Item());
        ender_cream = registerItem("ender_cream",new Item());
        thornrose = registerItem("thornrose",new Item());*/

        material = (ItemMaterial)registerItem("material",new ItemMaterial(
                new String[]{"arrowhead","midori","midori_popped","thornrose","stone_brick","bone_ingot","ender_cream"}
        ));

        greatbow = (ItemGreatbow)registerItem("greatbow",new ItemGreatbow());
        greatarrow = (ItemGreatarrow)registerItem("greatarrow",new ItemGreatarrow());
        monument = (ItemMonument)registerItem("monument",new ItemMonument());
        //Food
        bakedMushroom = (ItemFood)registerItem("food_mushroom_baked",new ItemFood(3, 0.2F, false));
        bakedAmanita = (ItemFood)registerItem("food_amanita_baked",new ItemFood(3, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1), 1.0F));
        cookedBeetroot = (ItemFood)registerItem("food_beetroot_cooked",new ItemFood(4, 0.5F, false));
        bakedBeetroot = (ItemFood)registerItem("food_beetroot_baked",new ItemFood(5, 0.4F, false));
        cookedCarrot = (ItemFood)registerItem("food_carrot_cooked",new ItemFood(3, 0.6F, false));
        bakedCarrot = (ItemFood)registerItem("food_carrot_baked",new ItemFood(4, 0.5F, false));
        cookedPotato = (ItemFood)registerItem("food_potato_cooked",new ItemFood(4, 0.7F, false));
        cookedEgg = (ItemFood)registerItem("food_egg_cooked",new ItemFood(4, 0.3F, false));
        meatballs = (ItemFood)registerItem("food_meatballs",new ItemFood(6, 0.6F, true));

        groundMeat = (ItemFood)registerItem("food_ground_meat",new ItemFood(2, 0.1F, true));

        pieMushroom = (ItemFood)registerItem("food_pie_mushroom",new ItemFood(8, 0.3F, false));
        pieAmanita = (ItemFood)registerItem("food_pie_amanita",new ItemFood(8, 0.3F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1), 1.0F));
        pieMeat = (ItemFood)registerItem("food_pie_meat",new ItemFood(9, 0.5F, true));
        pieMelon = (ItemFood)registerItem("food_pie_melon",new ItemFood(8, 0.4F, false));

        cookedClownfish = (ItemFood)registerItem("food_clownfish_cooked",new ItemFood(6, 0.5F, false));
        cookedPuffer = (ItemFood)registerItem("food_pufferfish_baked",new ItemFood(6, 0.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 500, 1), 0.1F));
        preparedPuffer = (ItemFood)registerItem("food_pufferfish_prepared",new ItemFood(2, 0.1F, false));
        preparedCookedPuffer = (ItemFood)registerItem("food_pufferfish_cooked",new ItemFood(4, 0.5F, false));
        fuguSac = (ItemFood)registerItem("food_fugu_sac",new ItemFood(2, 0.1F, false).setPotionEffect(new PotionEffect(MobEffects.WITHER, 2000, 1), 1.0F));

        sashimi = (ItemFood)registerItem("food_sashimi",new ItemFood(2, 0.1F, false));
        rice = (ItemFood)registerItem("food_cooked_rice",new ItemFood(2, 0.3F, false));
        riceBowl = (ItemFood)registerItem("food_bowl_rice",new ItemFood(9, 0.6F, false));

        stainedBrick = (ItemStainedBrick)registerItem("brick_stained",new ItemStainedBrick());

        shinai = (ItemShinai) registerItem("shinai",new ItemShinai());
        katana = (ItemKatana) registerItem("katana",new ItemKatana());
        wakizashi = (ItemWakizashi) registerItem("wakizashi",new ItemWakizashi());
        tanto = (ItemTanto) registerItem("tanto",new ItemTanto());
        yumi = (ItemYumi) registerItem("yumi",new ItemYumi());
        ya = (ItemYa) registerItem("ya",new ItemYa());

        materialJapan = (ItemMaterial)registerItem("japanmat",new ItemMaterial(
                new String[]{"rice","soaked_rice","rice_stalk","rice_hay","rice_ash","rush",
                        "soaked_bamboo","bamboo_slats","soaked_mulberry","mulberry_paste","mulberry_sheet","washi",
                        "iron_scales","lamellar","paper_lamellar","tsuka","half_katana_blade","ya_head","yumi_top","yumi_bottom",
                        "tamahagane","tamahagane_heated", "tamahagane_folded","tamahagane_reheated","tamahagane_finished","tamahagane_wrapped",
                        "hocho_tetsu","hocho_tetsu_heated","hocho_tetsu_fold_1","hocho_tetsu_fold_2","hocho_tetsu_finished",
                        "helmet_undecorated","chest_undecorated","legs_undecorated","boots_undecorated","bark_sakura","bark_mulberry"}
        ));

        samuraiHelm = (ItemSamuraiArmor)registerItem("helmet_samurai",new ItemSamuraiArmor(EntityEquipmentSlot.HEAD));
        samuraiChestplate = (ItemSamuraiArmor)registerItem("chest_samurai",new ItemSamuraiArmor(EntityEquipmentSlot.CHEST));
        samuraiLeggings = (ItemSamuraiArmor)registerItem("legs_samurai",new ItemSamuraiArmor(EntityEquipmentSlot.LEGS));
        samuraiBoots = (ItemSamuraiArmor)registerItem("boots_samurai",new ItemSamuraiArmor(EntityEquipmentSlot.FEET));

        materialBag = (ItemMaterial)registerItem("bag",new ItemMaterial(
                new String[]{"seed","seed_hemp","seed_melon","seed_pumpkin","seed_beets","cocoa","redstone","glowstone","sugar","gunpowder","flour","sulfur","nitre","sawdust","sawdust_soul","potash","hellfire"}
        ));
        materialCrate = (ItemMaterial)registerItem("crate",new ItemMaterial(
                new String[]{"pork","pork_raw","chicken","chicken_raw","steak","steak_raw","mutton","mutton_raw","rabbit","rabbit_raw","egg","slime","enderpearl"}
        ));
        materialCongealed = (ItemMaterial)registerItem("congealed",new ItemMaterial(
                new String[]{"bone","flesh","eye","amanita","mushroom","wart"}
        ));
        materialBolt = (ItemMaterial)registerItem("bolt",new ItemMaterial(
                new String[]{"fabric","vine","paper","leather","scoured_leather","tanned_leather","string"}
        ));
        materialBundle = (ItemMaterial)registerItem("bundle",new ItemMaterial(
                new String[]{"feather","arrows","blazerods","oak","birch","spruce","jungle","acacia","darkoak"}
        ));

        materialDeco = (ItemMaterial)registerItem("decomat",new ItemMaterial(
                new String[]{"hemp_oil","wood_bleach","wood_stain","glass_chunk"}
        ));
    }

    private static Item registerItem(String name,Item item)
    {
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        GameRegistry.register(item, new ResourceLocation(Reference.MOD_ID,name));
        LIST.add(item);

        return item;
    }
}
