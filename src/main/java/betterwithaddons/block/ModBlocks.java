package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.block.EriottoMod.*;
import betterwithaddons.block.Factorization.*;
import betterwithaddons.item.ItemBlockMeta;
import betterwithaddons.item.ItemBlockSeed;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModBlocks {
    public static ArrayList<Block> LIST = new ArrayList<Block>();

    @GameRegistry.ObjectHolder("betterwithaddons:banner_detector")
    public static BlockBannerDetector bannerDetector;
    @GameRegistry.ObjectHolder("betterwithaddons:world_scale")
    public static BlockWorldScale worldScale;
    @GameRegistry.ObjectHolder("betterwithaddons:world_scale_active")
    public static BlockWorldScaleActive worldScaleActive;
    @GameRegistry.ObjectHolder("betterwithaddons:elytra_magma")
    public static BlockElytraMagma elytraMagma;
    @GameRegistry.ObjectHolder("betterwithaddons:extra_grass")
    public static BlockExtraGrass grass;
    @GameRegistry.ObjectHolder("betterwithaddons:pcb_wire")
    public static BlockWirePCB pcbwire;
    @GameRegistry.ObjectHolder("betterwithaddons:pcb_block")
    public static BlockPCB pcbblock;
    @GameRegistry.ObjectHolder("betterwithaddons:lattice")
    public static BlockLattice lattice;
    @GameRegistry.ObjectHolder("betterwithaddons:alchemical_dragon")
    public static BlockAlchDragon alchDragon;
    @GameRegistry.ObjectHolder("betterwithaddons:log_mulberry")
    public static BlockModLog mulberryLog;
    @GameRegistry.ObjectHolder("betterwithaddons:log_sakura")
    public static BlockModLog sakuraLog;
    @GameRegistry.ObjectHolder("betterwithaddons:planks_mulberry")
    public static BlockModPlanks mulberryPlanks;
    @GameRegistry.ObjectHolder("betterwithaddons:planks_sakura")
    public static BlockModPlanks sakuraPlanks;
    @GameRegistry.ObjectHolder("betterwithaddons:crop_rush")
    public static BlockCropRush rush;
    @GameRegistry.ObjectHolder("betterwithaddons:crop_rice")
    public static BlockCropRice rice;
    @GameRegistry.ObjectHolder("betterwithaddons:bricks_stained")
    public static BlockColoredBrick coloredBrick;
    @GameRegistry.ObjectHolder("betterwithaddons:slat")
    public static BlockSlat bambooSlats;
    @GameRegistry.ObjectHolder("betterwithaddons:iron_sand")
    public static BlockIronSand ironSand;
    @GameRegistry.ObjectHolder("betterwithaddons:kera")
    public static BlockKera kera;
    @GameRegistry.ObjectHolder("betterwithaddons:netted_screen")
    public static BlockNettedScreen nettedScreen;
    @GameRegistry.ObjectHolder("betterwithaddons:tatara")
    public static BlockTatara tatara;
    @GameRegistry.ObjectHolder("betterwithaddons:cherrybox")
    public static BlockCherryBox cherrybox;
    @GameRegistry.ObjectHolder("betterwithaddons:world_scale_ore")
    public static BlockWorldScaleOre worldScaleOre;
    @GameRegistry.ObjectHolder("betterwithaddons:bamboo")
    public static BlockBamboo bamboo;
    @GameRegistry.ObjectHolder("betterwithaddons:leaves_sakura")
    public static BlockModLeaves sakuraLeaves;
    @GameRegistry.ObjectHolder("betterwithaddons:sapling_sakura")
    public static BlockModSapling sakuraSapling;
    @GameRegistry.ObjectHolder("betterwithaddons:leaves_mulberry")
    public static BlockModLeaves mulberryLeaves;
    @GameRegistry.ObjectHolder("betterwithaddons:sapling_mulberry")
    public static BlockModSapling mulberrySapling;
    @GameRegistry.ObjectHolder("betterwithaddons:leafpile_sakura")
    public static BlockCherryLeafPile sakuraLeafPile;
    @GameRegistry.ObjectHolder("betterwithaddons:thorn_rose")
    public static BlockThornRose thornrose;
    @GameRegistry.ObjectHolder("betterwithaddons:thorns")
    public static BlockThorns thorns;
    @GameRegistry.ObjectHolder("betterwithaddons:leaves_luretree")
    public static BlockModLeaves luretreeLeaves;
    @GameRegistry.ObjectHolder("betterwithaddons:sapling_luretree")
    public static BlockLureTreeSapling luretreeSapling;
    @GameRegistry.ObjectHolder("betterwithaddons:log_luretree")
    public static BlockModLog luretreeLog;
    @GameRegistry.ObjectHolder("betterwithaddons:log_luretree_face")
    public static BlockLureTree luretreeFace;
    @GameRegistry.ObjectHolder("betterwithaddons:paper_wall")
    public static BlockModPane paperWall;
    @GameRegistry.ObjectHolder("betterwithaddons:wrought_bars")
    public static BlockModPane wroughtBars;
    @GameRegistry.ObjectHolder("betterwithaddons:shoji")
    public static BlockModPane shoji;
    @GameRegistry.ObjectHolder("betterwithaddons:fusuma")
    public static BlockFusumaPainted fusuma;
    @GameRegistry.ObjectHolder("betterwithaddons:chandelier")
    public static BlockChandelier chandelier;
    @GameRegistry.ObjectHolder("betterwithaddons:wood_lamp")
    public static BlockLantern paperLantern;
    @GameRegistry.ObjectHolder("betterwithaddons:wrought_lamp")
    public static BlockLantern wroughtLantern;
    @GameRegistry.ObjectHolder("betterwithaddons:tatami")
    public static BlockTatami tatami;
    @GameRegistry.ObjectHolder("betterwithaddons:pavement")
    public static BlockPavement pavement;
    @GameRegistry.ObjectHolder("betterwithaddons:wet_soap")
    public static BlockSoap wetSoap;
    @GameRegistry.ObjectHolder("betterwithaddons:whitebrick")
    public static BlockWhiteBrick whiteBrick;
    @GameRegistry.ObjectHolder("betterwithaddons:chute")
    public static BlockChute chute;
    @GameRegistry.ObjectHolder("betterwithaddons:ecksie_sapling")
    public static BlockEcksieSapling ecksieSapling;
    @GameRegistry.ObjectHolder("betterwithaddons:aqueduct_water")
    public static BlockAqueductWater aqueductWater;
    @GameRegistry.ObjectHolder("betterwithaddons:aqueduct")
    public static BlockAqueduct aqueduct;
    @GameRegistry.ObjectHolder("betterwithaddons:greatbow")
    public static BlockLegendarium legendarium;
    @GameRegistry.ObjectHolder("betterwithaddons:pond_base")
    public static BlockPondBase pondBase;
    @GameRegistry.ObjectHolder("betterwithaddons:brine")
    public static BlockBrine brine;
    @GameRegistry.ObjectHolder("betterwithaddons:salt_layer")
    public static BlockSaltLayer saltLayer;
    @GameRegistry.ObjectHolder("betterwithaddons:spindle")
    public static BlockSpindle spindle;
    @GameRegistry.ObjectHolder("betterwithaddons:loom")
    public static BlockLoom loom;
    @GameRegistry.ObjectHolder("betterwithaddons:ancestry_sand")
    public static BlockAncestrySand ancestrySand;
    @GameRegistry.ObjectHolder("betterwithaddons:ancestry_infuser")
    public static BlockInfuser infuser;
    @GameRegistry.ObjectHolder("betterwithaddons:unbaked")
    public static BlockModUnbaked unbaked;

    public static void load(FMLPreInitializationEvent event) {
        FluidRegistry.registerFluid(new Fluid("brine", new ResourceLocation(Reference.MOD_ID, "blocks/brine_still"), new ResourceLocation(Reference.MOD_ID, "blocks/brine_flow")));
        FluidRegistry.registerFluid(new Fluid("salinated_brine", new ResourceLocation(Reference.MOD_ID, "blocks/salinated_brine_still"), new ResourceLocation(Reference.MOD_ID, "blocks/salinated_brine_flow")));

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

        registerBlock(new BlockMatcher());
        registerBlock(new BlockLegendarium());
        registerBlock(new BlockPondBase());
        registerBlock(new BlockBrine(), null, false);
        registerBlock(new BlockSaltLayer());

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
        registerBlock(new BlockBamboo());
        registerBlock(new BlockSlat());
        registerBlock(new BlockNettedScreen());
        registerBlock(new BlockIronSand());
        registerBlock(new BlockKera());
        registerBlock(new BlockTatara());
        registerBlock(new BlockCherryBox());
        registerBlock(new BlockModPane("shoji", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockFusumaPainted("fusuma").setHardness(1.0f));
        registerBlock(new BlockTatami().setHardness(1.0f));

        registerBlock(new BlockSoap());

        registerBlock(new BlockChandelier().setLightLevel(0.9375F));
        registerBlock(new BlockModPane("paper_wall", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockModPane("wrought_bars", Material.IRON).setHardness(5.0f));
        registerBlock(new BlockLantern("wood_lamp", Material.WOOD).setHardness(1.0f));
        registerBlock(new BlockLantern("wrought_lamp", Material.IRON).setHardness(5.0f));
        registerBlock(new BlockPavement());
        registerBlock(new BlockWhiteBrick());

        registerBlock(new BlockColoredBrick(), ItemCloth.class, true);
        registerBlock(new BlockModUnbaked());

        ecksieSapling = (BlockEcksieSapling) registerBlock(new BlockEcksieSapling("ecksie_sapling") {
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
                        return ModBlocks.sakuraLeaves.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    case (7):
                        return ModBlocks.mulberryLeaves.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    case (8):
                        return ModBlocks.luretreeLeaves.getDefaultState().withProperty(BlockLeaves.DECAYABLE, false);
                    default:
                        return null;
                }
            }

            @Override
            public int getTypes() {
                return 9;
            }
        });
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

    private static Block registerBlock(Block block) {
        LIST.add(block);
        ModItems.registerItem(block.getRegistryName().toString(), new ItemBlockMeta(block).setRegistryName(block.getRegistryName()));

        return block;
    }

    private static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock, boolean hasSubtypes) {
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