package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.block.EriottoMod.*;
import betterwithaddons.block.Factorization.BlockLegendarium;
import betterwithaddons.block.Factorization.BlockMatcher;
import betterwithaddons.item.ItemBlockMeta;
import betterwithaddons.item.ItemBlockSeed;
import betterwithaddons.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModBlocks {
    public static ArrayList<Block> LIST = new ArrayList<Block>();

    @ObjectHolder("betterwithaddons:world_scale_ore")
    public static BlockWorldScaleOre WORLD_SCALE_ORE;
    @ObjectHolder("betterwithaddons:world_scale")
    public static BlockWorldScale WORLD_SCALE;
    @ObjectHolder("betterwithaddons:world_scale_active")
    public static BlockWorldScaleActive WORLD_SCALE_ACTIVE;
    @ObjectHolder("betterwithaddons:lattice")
    public static BlockLattice LATTICE;

    @ObjectHolder("betterwithaddons:bricks_stained")
    public static BlockColoredBrick COLORED_BRICKS;
    @ObjectHolder("betterwithaddons:adobe")
    public static BlockAdobe ADOBE;
    @ObjectHolder("betterwithaddons:scaffold")
    public static BlockScaffold SCAFFOLD;
    @ObjectHolder("betterwithaddons:rope_sideways")
    public static BlockRopeSideways ROPE_SIDEWAYS;
    @ObjectHolder("betterwithaddons:rope_post")
    public static BlockRopePost ROPE_POST;
    @ObjectHolder("betterwithaddons:elytra_magma")
    public static BlockElytraMagma ELYTRA_MAGMA;
    @ObjectHolder("betterwithaddons:extra_grass")
    public static BlockExtraGrass GRASS;

    @ObjectHolder("betterwithaddons:thorn_rose")
    public static BlockThornRose THORN_ROSE;
    @ObjectHolder("betterwithaddons:thorns")
    public static BlockThorns THORNS;
    @ObjectHolder("betterwithaddons:leaves_luretree")
    public static BlockModLeaves LURETREE_LEAVES;
    @ObjectHolder("betterwithaddons:sapling_luretree")
    public static BlockLureTreeSapling LURETREE_SAPLING;
    @ObjectHolder("betterwithaddons:log_luretree")
    public static BlockModLog LURETREE_LOG;
    @ObjectHolder("betterwithaddons:log_luretree_face")
    public static BlockLureTree LURETREE_FACE;

    @ObjectHolder("betterwithaddons:unbaked")
    public static BlockModUnbaked UNBAKED;

    @ObjectHolder("betterwithaddons:chute")
    public static BlockChute CHUTE;
    @ObjectHolder("betterwithaddons:spindle")
    public static BlockSpindle SPINDLE;
    @ObjectHolder("betterwithaddons:loom")
    public static BlockLoom LOOM;
    @ObjectHolder("betterwithaddons:box")
    public static BlockBox BOX;
    @ObjectHolder("betterwithaddons:redstone_emitter")
    public static BlockRedstoneEmitter REDSTONE_EMITTER;
    @ObjectHolder("betterwithaddons:alchemical_dragon")
    public static BlockAlchDragon ALCHEMICAL_DRAGON;
    @ObjectHolder("betterwithaddons:banner_detector")
    public static BlockBannerDetector BANNER_DETECTOR;

    @ObjectHolder("betterwithaddons:aqueduct_water")
    public static BlockAqueductWater AQUEDUCT_WATER;
    @ObjectHolder("betterwithaddons:aqueduct")
    public static BlockAqueduct AQUEDUCT;

    @ObjectHolder("betterwithaddons:pcb_wire")
    public static BlockWirePCB PCB_WIRE;
    @ObjectHolder("betterwithaddons:pcb_block")
    public static BlockPCB PCB_BLOCK;

    @ObjectHolder("betterwithaddons:log_mulberry")
    public static BlockModLog MULBERRY_LOG;
    @ObjectHolder("betterwithaddons:log_sakura")
    public static BlockModLog SAKURA_LOG;
    @ObjectHolder("betterwithaddons:planks_mulberry")
    public static BlockModPlanks MULBERRY_PLANKS;
    @ObjectHolder("betterwithaddons:planks_sakura")
    public static BlockModPlanks SAKURA_PLANKS;
    @ObjectHolder("betterwithaddons:sapling_mulberry")
    public static BlockModSapling MULBERRY_SAPLING;
    @ObjectHolder("betterwithaddons:sapling_sakura")
    public static BlockModSapling SAKURA_SAPLING;
    @ObjectHolder("betterwithaddons:leaves_mulberry")
    public static BlockModLeaves MULBERRY_LEAVES;
    @ObjectHolder("betterwithaddons:leaves_sakura")
    public static BlockModLeaves SAKURA_LEAVES;
    @ObjectHolder("betterwithaddons:leafpile_sakura")
    public static BlockCherryLeafPile SAKURA_LEAFPILE;
    @ObjectHolder("betterwithaddons:bamboo")
    public static BlockBamboo BAMBOO;
    @ObjectHolder("betterwithaddons:crop_rush")
    public static BlockCropRush RUSH;
    @ObjectHolder("betterwithaddons:crop_rice")
    public static BlockCropRice RICE;
    @ObjectHolder("betterwithaddons:crop_tea")
    public static BlockCropTea TEA;
    @ObjectHolder("betterwithaddons:slat")
    public static BlockSlat BAMBOO_SLATS;
    @ObjectHolder("betterwithaddons:shoji")
    public static BlockModPane SHOJI;
    @ObjectHolder("betterwithaddons:fusuma")
    public static BlockFusumaPainted FUSUMA;
    @ObjectHolder("betterwithaddons:tatami")
    public static BlockTatami TATAMI;
    @ObjectHolder("betterwithaddons:tatami_full")
    public static BlockTatami TATAMI_RECESSED;
    @ObjectHolder("betterwithaddons:zen_sand")
    public static BlockZenSand ZEN_SAND;
    @ObjectHolder("betterwithaddons:zen_redsand")
    public static BlockZenSand ZEN_RED_SAND;
    @ObjectHolder("betterwithaddons:zen_soulsand")
    public static BlockZenSand ZEN_SOUL_SAND;
    @ObjectHolder("betterwithaddons:zen_ironsand")
    public static BlockZenSand ZEN_IRON_SAND;
    @ObjectHolder("betterwithaddons:iron_sand")
    public static BlockIronSand IRON_SAND;
    @ObjectHolder("betterwithaddons:kera")
    public static BlockKera KERA;
    @ObjectHolder("betterwithaddons:ancestry_sand")
    public static BlockAncestrySand ANCESTRY_SAND;
    @ObjectHolder("betterwithaddons:ancestry_infuser")
    public static BlockInfuser INFUSER;
    @ObjectHolder("betterwithaddons:netted_screen")
    public static BlockNettedScreen NETTED_SCREEN;
    @ObjectHolder("betterwithaddons:tatara")
    public static BlockTatara TATARA;
    @ObjectHolder("betterwithaddons:cherrybox")
    public static BlockCherryBox CHERRY_BOX;
    @ObjectHolder("betterwithaddons:nabe")
    public static BlockNabe NABE;

    @ObjectHolder("betterwithaddons:whitebrick")
    public static BlockWhiteBrick WHITE_BRICK;
    @ObjectHolder("betterwithaddons:pavement")
    public static BlockPavement PAVEMENT;
    @ObjectHolder("betterwithaddons:paper_wall")
    public static BlockModPane PAPER_WALL;
    @ObjectHolder("betterwithaddons:wrought_bars")
    public static BlockModPane WROUGHT_BARS;
    @ObjectHolder("betterwithaddons:chandelier")
    public static BlockChandelier CHANDELIER;
    @ObjectHolder("betterwithaddons:wood_lamp")
    public static BlockLantern PAPER_LANTERN;
    @ObjectHolder("betterwithaddons:wrought_lamp")
    public static BlockLantern WROUGHT_LANTERN;

    @ObjectHolder("betterwithaddons:wet_soap")
    public static BlockSoap WET_SOAP;
    @ObjectHolder("betterwithaddons:rail_rusted")
    public static BlockRustyRail RUSTY_RAIL;
    @ObjectHolder("betterwithaddons:log_termite")
    public static BlockTermiteLog TERMITE_LOG;

    @ObjectHolder("betterwithaddons:ecksie_sapling")
    public static BlockEcksieSapling ECKSIE_SAPLING;

    @ObjectHolder("betterwithaddons:block_matcher")
    public static BlockMatcher BLOCK_MATCHER;
    @ObjectHolder("betterwithaddons:legendarium")
    public static BlockLegendarium LEGENDARIUM;

    @ObjectHolder("betterwithaddons:pond_replacement")
    public static BlockReplacement POND_REPLACEMENT;

    public static void load(FMLPreInitializationEvent event) {
        //FluidRegistry.registerFluid(new Fluid("brine", new ResourceLocation(Reference.MOD_ID, "blocks/brine_still"), new ResourceLocation(Reference.MOD_ID, "blocks/brine_flow")));
        //FluidRegistry.registerFluid(new Fluid("salinated_brine", new ResourceLocation(Reference.MOD_ID, "blocks/salinated_brine_still"), new ResourceLocation(Reference.MOD_ID, "blocks/salinated_brine_flow")));

        registerBlock(new BlockBannerDetector());
        registerBlock(new BlockWorldScale());
        registerBlock(new BlockWorldScaleOre());
        registerBlock(new BlockWorldScaleActive());
        registerBlock(new BlockElytraMagma());
        registerBlock(new BlockExtraGrass());
        registerBlock(new BlockWirePCB(), null, false);
        registerBlock(new BlockPCB());
        registerBlock(new BlockLattice());
        registerBlock(new BlockThornRose());
        registerBlock(new BlockThorns());
        registerBlock(new BlockChute());
        registerBlock(new BlockAqueduct());
        registerBlock(new BlockAqueductWater(), null, false);
        registerBlock(new BlockRedstoneEmitter());

        registerBlock(new BlockMatcher());
        registerBlock(new BlockLegendarium());
        //registerBlock(new BlockPondBase());
        //registerBlock(new BlockBrine(), null, false);
        //registerBlock(new BlockSaltLayer());

        registerBlock(new BlockWeight("weight_wood") {
            @Override
            public boolean decideActivity(boolean isEmpty, boolean isFull) {
                return !isEmpty;
            }
        });
        registerBlock(new BlockWeight("weight_stone") {
            @Override
            public boolean decideActivity(boolean isEmpty, boolean isFull) {
                return isFull;
            }
        });

        registerBlock(new BlockScaffold("scaffold"));
        registerBlock(new BlockRopeSideways("rope_sideways"), null, false);
        registerBlock(new BlockRopePost("rope_post"), null, false);

        registerBlock(new BlockSpindle());
        registerBlock(new BlockLoom());
        //alchDragon = (BlockAlchDragon) registerBlock(new BlockAlchDragon());

        registerBlock(new BlockModLeaves(ModWoods.LURETREE));
        registerBlock(new BlockLureTreeSapling());
        registerBlock(new BlockModLog(ModWoods.LURETREE));
        registerBlock(new BlockLureTree());

        registerBlock(new BlockModLeaves(ModWoods.MULBERRY));
        registerBlock(new BlockModSapling(ModWoods.MULBERRY));
        registerBlock(new BlockModLog(ModWoods.MULBERRY));
        registerBlock(new BlockModPlanks(ModWoods.MULBERRY));

        registerBlock(new BlockCherryLeaves(ModWoods.SAKURA));
        registerBlock(new BlockModSapling(ModWoods.SAKURA));
        registerBlock(new BlockModLog(ModWoods.SAKURA));
        registerBlock(new BlockModPlanks(ModWoods.SAKURA));

        registerBlock(new BlockAncestrySand());
        registerBlock(new BlockInfuser());
        registerBlock(new BlockCherryLeafPile());
        registerBlock(new BlockCropRush(), ItemBlockSeed.class, false);
        registerBlock(new BlockCropRice(), ItemBlockSeed.class, false);
        registerBlock(new BlockCropTea(), ItemBlockSeed.class, false);
        registerBlock(new BlockBamboo());
        registerBlock(new BlockSlat());
        registerBlock(new BlockNettedScreen());
        registerBlock(new BlockIronSand());
        registerBlock(new BlockKera());
        registerBlock(new BlockTatara());
        registerBlock(new BlockCherryBox());
        registerBlock(new BlockNabe());
        registerBlock(new BlockModPane("shoji", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockFusumaPainted("fusuma").setHardness(1.0f));
        registerBlock(new BlockTatami("tatami").setHardness(1.0f));
        registerBlock(new BlockTatamiRecessed("tatami_full").setHardness(1.0f));

        registerBlock(new BlockZenSand("zen_sand").setShouldFall().setHardness(0.5F));
        registerBlock(new BlockZenSand("zen_redsand").setShouldFall().setHardness(0.5F));
        registerBlock(new BlockZenSand("zen_soulsand").setShouldSlow().setHardness(0.5F));
        registerBlock(new BlockZenSand("zen_ironsand").setHardness(0.7F).setResistance(5.0F));

        registerBlock(new BlockSoap());

        registerBlock(new BlockChandelier().setLightLevel(0.9375F));
        registerBlock(new BlockModPane("paper_wall", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockModPane("wrought_bars", Material.IRON).setHardness(5.0f));
        registerBlock(new BlockLantern("wood_lamp", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockLantern("wrought_lamp", Material.IRON).setHardness(5.0f));
        registerBlock(new BlockPavement());
        registerBlock(new BlockWhiteBrick());

        registerBlock(new BlockBox());
        registerBlock(new BlockColoredBrick(), ItemCloth.class, true);
        registerBlock(new BlockModUnbaked());

        registerBlock(new BlockRustyRail());
        registerBlock(new BlockTermiteLog());
        registerBlock(new BlockWritingTable());

        registerBlock(new BlockAdobe("adobe", new AdobeType[]{
                AdobeType.MOSTLY_CLAY, AdobeType.CLAYSAND, AdobeType.SANDCLAY, AdobeType.MOSTLY_SAND,
                AdobeType.MOSTLY_STRAW, AdobeType.LIGHT, AdobeType.DARK, AdobeType.MOSTLY_DUNG
        }));

        registerBlock(new BlockReplacement("pond_replacement") {
            @Override
            public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
                switch(state.getValue(BlockReplacement.META))
                {
                    case(0):
                    case(1):
                        drops.add(new ItemStack(ADOBE, 1,1));
                        break;
                    case(2):
                        drops.add(new ItemStack(ADOBE, 1,9));
                        break;
                }
            }
        });

        registerBlock(new BlockEcksieSapling("ecksie_sapling") {
            @Override
            public IBlockState getLeafBlock(int type) {
                switch (type) {
                    case (0):
                        return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.DECAYABLE, false);
                    case (1):
                        return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.DECAYABLE, false);
                    case (2):
                        return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.DECAYABLE, false);
                    case (3):
                        return Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.DECAYABLE, false);
                    case (4):
                        return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.DECAYABLE, false);
                    case (5):
                        return Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.DECAYABLE, false);
                    case (6):
                        return ModBlocks.SAKURA_LEAVES.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    case (7):
                        return ModBlocks.MULBERRY_LEAVES.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    case (8):
                        return ModBlocks.LURETREE_LEAVES.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    default:
                        return null;
                }
            }

            @Override
            public int getTypes() {
                return 9;
            }
        });

        registerBlock(new BlockInvertedGearbox("inverted_gearbox"));
    }

    @SubscribeEvent
    public static void registryEvent(RegistryEvent.Register<Block> event) {
        for (Block block : LIST) {
            event.getRegistry().register(block);
        }
    }

    public static void connectPanes(BlockModPane pane1, BlockModPane pane2) {
        pane1.addCompatiblePane(pane2);
        pane2.addCompatiblePane(pane1);
    }

    public static Block registerBlock(Block block) {
        LIST.add(block);
        ModItems.registerItem(block.getRegistryName().toString(), new ItemBlockMeta(block).setRegistryName(block.getRegistryName()));

        return block;
    }

    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, boolean hasSubtypes) {
        LIST.add(block);
        if (itemBlock != null)
            try {
                ModItems.registerItem(block.getRegistryName().toString(), itemBlock.getConstructor(Block.class).newInstance(block).setRegistryName(block.getRegistryName()).setHasSubtypes(hasSubtypes));
            } catch (Exception e) {
                System.out.println("Error Registering ItemBlock for " + block.getRegistryName());
                e.printStackTrace();
            }

        return block;
    }
}