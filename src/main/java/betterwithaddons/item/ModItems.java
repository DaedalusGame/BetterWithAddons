package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.interaction.ModInteractions;
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
    public static ArrayList<Item> LIST = new ArrayList<>();

    public static Item.ToolMaterial bambooToolMaterial = EnumHelper.addToolMaterial("bamboo", 0, 51, 0.5f, -4.0f, 1);

    {
        bambooToolMaterial.setRepairItem(MATERIAL_JAPAN.getMaterial("bamboo_slats"));
    }

    public static Item.ToolMaterial tamahaganeToolMaterial = EnumHelper.addToolMaterial("tamahagane", 3, 1210, 8.0f, 2.5f, 9);

    {
        tamahaganeToolMaterial.setRepairItem(MATERIAL_JAPAN.getMaterial("tamahagane_finished"));
    }

    public static Item.ToolMaterial japansteelToolMaterial = EnumHelper.addToolMaterial("japansteel", 3, 1710, 8.0f, 4.0f, 10);

    {
        japansteelToolMaterial.setRepairItem(MATERIAL_JAPAN.getMaterial("tamahagane_finished"));
    }

    public static ItemArmor.ArmorMaterial samuraiArmorMaterial = EnumHelper.addArmorMaterial("SAMURAI", "samurai", 16,
            new int[]{3, 6, 7, 3}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f);

    @GameRegistry.ObjectHolder("betterwithaddons:greatbow")
    public static ItemGreatbow GREATBOW;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow")
    public static ItemGreatarrow GREATARROW;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow_lightning")
    public static ItemGreatarrow GREATARROW_LIGHTNING;
    @GameRegistry.ObjectHolder("betterwithaddons:greatarrow_destruction")
    public static ItemGreatarrow GREATARROW_DESTRUCTION;
    @GameRegistry.ObjectHolder("betterwithaddons:monument")
    public static ItemMonument MONUMENT;
    @GameRegistry.ObjectHolder("betterwithaddons:artifact_frame")
    public static ItemArtifactFrame ARTIFACT_FRAME;
    @GameRegistry.ObjectHolder("betterwithaddons:food_mushroom_baked")
    public static ItemFood BAKED_MUSHROOM;
    @GameRegistry.ObjectHolder("betterwithaddons:food_amanita_baked")
    public static ItemFood BAKED_AMANITA;
    @GameRegistry.ObjectHolder("betterwithaddons:food_beetroot_cooked")
    public static ItemFood COOKED_BEETROOT;
    @GameRegistry.ObjectHolder("betterwithaddons:food_beetroot_baked")
    public static ItemFood BAKED_BEETROOT;
    @GameRegistry.ObjectHolder("betterwithaddons:food_carrot_cooked")
    public static ItemFood COOKED_CARROT;
    @GameRegistry.ObjectHolder("betterwithaddons:food_carrot_baked")
    public static ItemFood BAKED_CARROT;
    @GameRegistry.ObjectHolder("betterwithaddons:food_potato_cooked")
    public static ItemFood COOKED_POTATO;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_melon")
    public static ItemFood PIE_MELON;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_meat")
    public static ItemFood PIE_MEAT;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_mushroom")
    public static ItemFood PIE_MUSHROOM;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pie_amanita")
    public static ItemFood PIE_AMANITA;
    @GameRegistry.ObjectHolder("betterwithaddons:food_meatballs")
    public static ItemFood MEATBALLS;
    @GameRegistry.ObjectHolder("betterwithaddons:food_egg_cooked")
    public static ItemFood COOKED_EGG;
    @GameRegistry.ObjectHolder("betterwithaddons:food_ground_meat")
    public static ItemFood GROUND_MEAT;

    @GameRegistry.ObjectHolder("betterwithaddons:soulsand_pile")
    public static Item SOUL_SAND_PILE;
    @GameRegistry.ObjectHolder("betterwithaddons:wool")
    public static ItemColored WOOL;
    @GameRegistry.ObjectHolder("betterwithaddons:rotten_food")
    public static ItemFood ROTTEN_FOOD;

    @GameRegistry.ObjectHolder("betterwithaddons:salt")
    public static ItemMaterial SALTS;

    @GameRegistry.ObjectHolder("betterwithaddons:food_clownfish_cooked")
    public static ItemFood COOKED_CLOWNFISH;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_baked")
    public static ItemFood COOKED_PUFFER;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_prepared")
    public static ItemFood PREPARED_PUFFER;
    @GameRegistry.ObjectHolder("betterwithaddons:food_pufferfish_cooked")
    public static ItemFood PREPARED_COOKED_PUFFER;
    @GameRegistry.ObjectHolder("betterwithaddons:food_fugu_sac")
    public static ItemFood FUGU_SAC;
    @GameRegistry.ObjectHolder("betterwithaddons:worldshard")
    public static ItemWorldScale WORLD_SHARD;

    @GameRegistry.ObjectHolder("betterwithaddons:ancestry_bottle")
    public static ItemAncestryBottle ANCESTRY_BOTTLE;
    @GameRegistry.ObjectHolder("betterwithaddons:food_sashimi")
    public static ItemFood SASHIMI;
    @GameRegistry.ObjectHolder("betterwithaddons:food_mulberry")
    public static ItemFood MULBERRY;
    @GameRegistry.ObjectHolder("betterwithaddons:food_cooked_rice")
    public static ItemFood RICE;
    @GameRegistry.ObjectHolder("betterwithaddons:food_bowl_rice")
    public static ItemFood RICE_BOWL;
    @GameRegistry.ObjectHolder("betterwithaddons:laxative")
    public static Item LAXATIVE;
    @GameRegistry.ObjectHolder("betterwithaddons:japanmat")
    public static ItemMaterial MATERIAL_JAPAN;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_leaves")
    public static ItemTea TEA_LEAVES;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_soaked")
    public static ItemTea TEA_SOAKED;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_wilted")
    public static ItemTea TEA_WILTED;
    @GameRegistry.ObjectHolder("betterwithaddons:tea_powder")
    public static ItemTea TEA_POWDER;
    @GameRegistry.ObjectHolder("betterwithaddons:teacup")
    public static ItemTeaCup TEA_CUP;

    @GameRegistry.ObjectHolder("betterwithaddons:brick_stained")
    public static ItemStainedBrick STAINED_BRICK;
    @GameRegistry.ObjectHolder("betterwithaddons:material")
    public static ItemMaterial MATERIAL;
    @GameRegistry.ObjectHolder("betterwithaddons:bolt")
    public static ItemMaterial MATERIAL_BOLT;
    @GameRegistry.ObjectHolder("betterwithaddons:congealed")
    public static ItemMaterial MATERIAL_CONGEALED;
    @GameRegistry.ObjectHolder("betterwithaddons:crate")
    public static ItemMaterial MATERIAL_CRATE;
    @GameRegistry.ObjectHolder("betterwithaddons:bag")
    public static ItemMaterial MATERIAL_BAG;
    @GameRegistry.ObjectHolder("betterwithaddons:bundle")
    public static ItemMaterial MATERIAL_BUNDLE;
    @GameRegistry.ObjectHolder("betterwithaddons:decomat")
    public static ItemMaterial MATERIAL_DECO;
    @GameRegistry.ObjectHolder("betterwithaddons:wheatmat")
    public static ItemMaterial MATERIAL_WHEAT;

    @GameRegistry.ObjectHolder("betterwithaddons:tweakmat")
    public static ItemMaterial MATERIAL_TWEAK;
    @GameRegistry.ObjectHolder("betterwithaddons:ink_and_quill")
    public static ItemInkAndQuill INK_AND_QUILL;

    @GameRegistry.ObjectHolder("betterwithaddons:katana")
    public static ItemKatana KATANA;
    @GameRegistry.ObjectHolder("betterwithaddons:wakizashi")
    public static ItemWakizashi WAKIZASHI;
    @GameRegistry.ObjectHolder("betterwithaddons:tanto")
    public static ItemTanto TANTO;
    @GameRegistry.ObjectHolder("betterwithaddons:shinai")
    public static ItemShinai SHINAI;
    @GameRegistry.ObjectHolder("betterwithaddons:yumi")
    public static ItemYumi YUMI;
    @GameRegistry.ObjectHolder("betterwithaddons:ya")
    public static ItemYa YA;
    @GameRegistry.ObjectHolder("betterwithaddons:poisoned_ya")
    public static ItemPoisonedYa YA_POISONED;
    @GameRegistry.ObjectHolder("betterwithaddons:helmet_samurai")
    public static ItemSamuraiArmor SAMURAI_HELMET;
    @GameRegistry.ObjectHolder("betterwithaddons:chest_samurai")
    public static ItemSamuraiArmor SAMURAI_CHESTPLATE;
    @GameRegistry.ObjectHolder("betterwithaddons:legs_samurai")
    public static ItemSamuraiArmor SAMURAI_LEGGINGS;
    @GameRegistry.ObjectHolder("betterwithaddons:boots_samurai")
    public static ItemSamuraiArmor SAMURAI_BOOTS;
    @GameRegistry.ObjectHolder("betterwithaddons:helmet_paper")
    public static ItemPaperArmor PAPER_HELMET;
    @GameRegistry.ObjectHolder("betterwithaddons:chest_paper")
    public static ItemPaperArmor PAPER_CHESTPLATE;
    @GameRegistry.ObjectHolder("betterwithaddons:legs_paper")
    public static ItemPaperArmor PAPER_LEGGINGS;
    @GameRegistry.ObjectHolder("betterwithaddons:boots_paper")
    public static ItemPaperArmor PAPER_BOOTS;

    @GameRegistry.ObjectHolder("betterwithaddons:tool_shard")
    public static ItemToolShard BROKEN_ARTIFACT;

    @GameRegistry.ObjectHolder("betterwithaddons:iron_spade")
    public static ItemSpade IRON_SPADE;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_machete")
    public static ItemMachete IRON_MACHETE;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_matchpick")
    public static ItemMatchPick IRON_MATCHPICK;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_kukri")
    public static ItemKukri IRON_KUKRI;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_carpentersaw")
    public static ItemCarpenterSaw IRON_CARPENTER_SAW;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_masonpick")
    public static ItemMasonPick IRON_MASON_PICK;

    @GameRegistry.ObjectHolder("betterwithaddons:gold_spade")
    public static ItemSpade GOLD_SPADE;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_machete")
    public static ItemMachete GOLD_MACHETE;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_matchpick")
    public static ItemMatchPick GOLD_MATCHPICK;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_kukri")
    public static ItemKukri GOLD_KUKRI;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_carpentersaw")
    public static ItemCarpenterSaw GOLD_CARPENTER_SAW;
    @GameRegistry.ObjectHolder("betterwithaddons:gold_masonpick")
    public static ItemMasonPick GOLD_MASON_PICK;

    @GameRegistry.ObjectHolder("betterwithaddons:diamond_spade")
    public static ItemSpade DIAMOND_SPADE;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_machete")
    public static ItemMachete DIAMOND_MACHETE;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_matchpick")
    public static ItemMatchPick DIAMOND_MATCHPICK;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_kukri")
    public static ItemKukri DIAMOND_KUKRI;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_carpentersaw")
    public static ItemCarpenterSaw DIAMOND_CARPENTER_SAW;
    @GameRegistry.ObjectHolder("betterwithaddons:diamond_masonpick")
    public static ItemMasonPick DIAMOND_MASON_PICK;

    @GameRegistry.ObjectHolder("betterwithaddons:steel_spade")
    public static ItemSpade STEEL_SPADE;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_machete")
    public static ItemMachete STEEL_MACHETE;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_matchpick")
    public static ItemMatchPick STEEL_MATCHPICK;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_kukri")
    public static ItemKukri STEEL_KUKRI;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_carpentersaw")
    public static ItemCarpenterSaw STEEL_CARPENTER_SAW;
    @GameRegistry.ObjectHolder("betterwithaddons:steel_masonpick")
    public static ItemMasonPick STEEL_MASON_PICK;

    @GameRegistry.ObjectHolder("betterwithaddons:explosion")
    public static Item EXPLOSION;

    public static void load(FMLPreInitializationEvent event) {
        registerItem("iron_spade", new ItemSpade(Item.ToolMaterial.IRON));
        registerItem("iron_matchpick", new ItemMatchPick(Item.ToolMaterial.IRON));
        registerItem("iron_machete", new ItemMachete(Item.ToolMaterial.IRON));
        registerItem("iron_kukri", new ItemKukri(Item.ToolMaterial.IRON, 8.0f, -3.1f));
        registerItem("iron_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.IRON, 8.0f, -3.1f));
        registerItem("iron_masonpick", new ItemMasonPick(Item.ToolMaterial.IRON));

        registerItem("gold_spade", new ItemSpade(Item.ToolMaterial.GOLD));
        registerItem("gold_matchpick", new ItemMatchPick(Item.ToolMaterial.GOLD));
        registerItem("gold_machete", new ItemMachete(Item.ToolMaterial.GOLD));
        registerItem("gold_kukri", new ItemKukri(Item.ToolMaterial.GOLD, 6.0f, -3.0f));
        registerItem("gold_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.GOLD, 6.0f, -3.0f));
        registerItem("gold_masonpick", new ItemMasonPick(Item.ToolMaterial.GOLD));

        registerItem("diamond_spade", new ItemSpade(Item.ToolMaterial.DIAMOND));
        registerItem("diamond_matchpick", new ItemMatchPick(Item.ToolMaterial.DIAMOND));
        registerItem("diamond_machete", new ItemMachete(Item.ToolMaterial.DIAMOND));
        registerItem("diamond_kukri", new ItemKukri(Item.ToolMaterial.DIAMOND, 8.0f, -3.0f));
        registerItem("diamond_carpentersaw", new ItemCarpenterSaw(Item.ToolMaterial.DIAMOND, 8.0f, -3.0f));
        registerItem("diamond_masonpick", new ItemMasonPick(Item.ToolMaterial.DIAMOND));

        registerItem("steel_spade", new ItemSpade(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        registerItem("steel_matchpick", new ItemMatchPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        registerItem("steel_machete", new ItemMachete(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        registerItem("steel_kukri", new ItemKukri(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        registerItem("steel_carpentersaw", new ItemCarpenterSaw(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });
        registerItem("steel_masonpick", new ItemMasonPick(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
                return ItemUtil.matchesOreDict(repair, "ingotSoulforgedSteel") || super.getIsRepairable(toRepair, repair);
            }
        });

        registerItem("material", new ItemMaterial(
                new String[]{"arrowhead", "midori", "midori_popped", "thornrose", "stone_brick", "bone_ingot", "ender_cream"}
        ));

        registerItem("worldshard", new ItemWorldScale());
        registerItem("greatbow", new ItemGreatbow());
        registerItem("greatarrow", new ItemGreatarrow());
        registerItem("greatarrow_lightning", new ItemGreatarrowLightning());
        registerItem("greatarrow_destruction", new ItemGreatarrowDestruction());
        registerItem("monument", new ItemMonument());
        //Food
        registerItem("food_mushroom_baked", new ItemFood(3, 0.2F, false));
        registerItem("food_amanita_baked", new ItemFood(3, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        registerItem("food_beetroot_cooked", new ItemFood(4, 0.5F, false));
        registerItem("food_beetroot_baked", new ItemFood(5, 0.4F, false));
        registerItem("food_carrot_cooked", new ItemFood(3, 0.6F, false));
        registerItem("food_carrot_baked", new ItemFood(4, 0.5F, false));
        registerItem("food_potato_cooked", new ItemFood(4, 0.7F, false));
        registerItem("food_egg_cooked", new ItemFood(4, 0.3F, false));
        registerItem("food_meatballs", new ItemFood(3, 0.6F, true));

        registerItem("food_ground_meat", new ItemFood(1, 0.1F, true));

        registerItem("food_pie_mushroom", new ItemFood(8, 0.3F, false));
        registerItem("food_pie_amanita", new ItemFood(8, 0.3F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        registerItem("food_pie_meat", new ItemFood(9, 0.5F, true));
        registerItem("food_pie_melon", new ItemFood(8, 0.4F, false));

        registerItem("food_clownfish_cooked", new ItemFood(6, 0.5F, false));
        registerItem("food_pufferfish_baked", new ItemFood(6, 0.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 500, 1), 0.1F));
        registerItem("food_pufferfish_prepared", new ItemFood(2, 0.1F, false));
        registerItem("food_pufferfish_cooked", new ItemFood(4, 0.5F, false));
        registerItem("food_fugu_sac", new ItemFood(2, 0.1F, false).setPotionEffect(new PotionEffect(MobEffects.WITHER, 2000, 1), 1.0F));

        registerItem("food_sashimi", new ItemFood(2, 0.1F, false));
        registerItem("food_mulberry", new ItemFood(1, 0.2F, false));
        registerItem("food_cooked_rice", new ItemFood(2, 0.3F, false));
        registerItem("food_bowl_rice", new ItemFood(9, 0.6F, false).setMaxStackSize(1));
        registerItem("laxative", new ItemLaxative(0, 0, false));
        registerItem("tea_leaves", new ItemTea(TeaType.ItemType.Leaves));
        registerItem("tea_soaked", new ItemTea(TeaType.ItemType.Soaked));
        registerItem("tea_wilted", new ItemTea(TeaType.ItemType.Wilted));
        registerItem("tea_powder", new ItemTea(TeaType.ItemType.Powder));

        registerItem("soulsand_pile", new Item());
        registerItem("rotten_food", new ItemFood(1, 0.1f, false).setPotionEffect(new PotionEffect(MobEffects.HUNGER, 2000, 1), 1.0F));
        registerItem("wool", new ItemColored());

        /*bowls = (ItemMaterial) registerItem("bowl", new ItemMaterial(
                new String[]{"salt"}
        ));*/
        registerItem("salt", new ItemMaterial(
                new String[]{"crystal"}
        ));

        registerItem("artifact_frame", new ItemArtifactFrame());
        registerItem("tool_shard", new ItemToolShard().setMaxStackSize(1));

        registerItem("brick_stained", new ItemStainedBrick());

        registerItem("shinai", new ItemShinai());
        registerItem("katana", new ItemKatana());
        registerItem("wakizashi", new ItemWakizashi());
        registerItem("tanto", new ItemTanto());
        registerItem("yumi", new ItemYumi());
        registerItem("ya", new ItemYa());
        registerItem("poisoned_ya", new ItemPoisonedYa());
        registerItem("ancestry_bottle", new ItemAncestryBottle());
        registerItem("teacup", new ItemTeaCup());

        registerItem("japanmat", new ItemMaterial(
                new String[]{"rice", "soaked_rice", "rice_stalk", "rice_hay", "rice_ash", "rush",
                        "soaked_bamboo", "bamboo_slats", "soaked_mulberry", "mulberry_paste", "mulberry_sheet", "washi",
                        "iron_scales", "lamellar", "paper_lamellar", "tsuka", "half_katana_blade", "ya_head", "yumi_top", "yumi_bottom",
                        "tamahagane", "tamahagane_heated", "tamahagane_folded", "tamahagane_reheated", "tamahagane_finished", "tamahagane_wrapped",
                        "hocho_tetsu", "hocho_tetsu_heated", "hocho_tetsu_fold_1", "hocho_tetsu_fold_2", "hocho_tetsu_finished",
                        "helmet_undecorated", "chest_undecorated", "legs_undecorated", "boots_undecorated", "bark_sakura", "bark_mulberry"}
        ));

        registerItem("helmet_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.HEAD));
        registerItem("chest_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.CHEST));
        registerItem("legs_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.LEGS));
        registerItem("boots_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.FEET));

        registerItem("helmet_paper", new ItemPaperArmor(EntityEquipmentSlot.HEAD));
        registerItem("chest_paper", new ItemPaperArmor(EntityEquipmentSlot.CHEST));
        registerItem("legs_paper", new ItemPaperArmor(EntityEquipmentSlot.LEGS));
        registerItem("boots_paper", new ItemPaperArmor(EntityEquipmentSlot.FEET));

        registerItem("bag", new ItemMaterial(
                new String[]{"seed", "seed_hemp", "seed_melon", "seed_pumpkin", "seed_beets", "cocoa", "redstone", "glowstone", "sugar", "gunpowder", "flour", "sulfur", "nitre", "sawdust", "sawdust_soul", "potash", "hellfire", "kibble"}
        ));
        registerItem("crate", new ItemMaterial(
                new String[]{"pork", "pork_raw", "chicken", "chicken_raw", "steak", "steak_raw", "mutton", "mutton_raw", "rabbit", "rabbit_raw", "egg", "slime", "enderpearl", "cactus", "inksac"}
        ));
        registerItem("congealed", new ItemMaterial(
                new String[]{"bone", "flesh", "eye", "amanita", "mushroom", "wart"}
        ));
        registerItem("bolt", new ItemMaterial(
                new String[]{"fabric", "vine", "paper", "leather", "scoured_leather", "tanned_leather", "string"}
        ));
        registerItem("bundle", new ItemMaterial(
                new String[]{"feather", "arrows", "blazerods", "oak", "birch", "spruce", "jungle", "acacia", "darkoak"}
        ));

        registerItem("decomat", new ItemMaterial(
                new String[]{"hemp_oil", "wood_bleach", "wood_stain", "glass_chunk"}
        ));

        registerItem("tweakmat", new ItemMaterial(
                new String[]{"ash", "ink_and_quill"}
        ));
        registerItem("ink_and_quill", new ItemInkAndQuill());

        registerItem("wheatmat", new ItemMaterial(
                new String[]{"hay"}
        ));

        registerItem("explosion", new Item() {
            @Override
            public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
            }
        });
    }

    @SubscribeEvent
    public static void registryEvent(RegistryEvent.Register<Item> event) {
        for (Item item : LIST) {
            event.getRegistry().register(item);
        }

        ModInteractions.oreDictRegistration();
    }

    public static Item registerItem(Item item) {
        LIST.add(item);

        return item;
    }

    public static Item registerItem(String name, Item item) {
        if (item.getRegistryName() == null)
            item.setRegistryName(Reference.MOD_ID, name);
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        return registerItem(item);
    }
}
