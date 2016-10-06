package betterwithaddons;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.GuiHandler;
import betterwithaddons.crafting.*;
import betterwithaddons.entity.ModEntities;
import betterwithaddons.handler.*;
import betterwithaddons.interaction.InteractionBWM;
import betterwithaddons.interaction.InteractionQuark;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.tileentity.ModTileEntities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class BetterWithAddons
{
	@Instance(Reference.MOD_ID)
	public static BetterWithAddons instance;

	@SidedProxy(clientSide = "betterwithaddons.ClientProxy",serverSide = "betterwithaddons.ServerProxy")
	public static IProxy proxy;

	public BWACreativeTab creativeTab;

	public Logger logger;

	public InteractionBWM bwmIntegration;
	public InteractionQuark quarkIntegration;

	ASMDataTable asmDataTable;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		asmDataTable = event.getAsmData();

		creativeTab = new BWACreativeTab();
		logger = event.getModLog();

		bwmIntegration = new InteractionBWM();
		quarkIntegration = new InteractionQuark();

		ModBlocks.load(event);
		ModItems.load(event);
		ModTileEntities.register();
		ModEntities.init();
		ModPotions.preInit(event);
		proxy.preInit();

		//RTEventHandler eventHandler = ;
		MinecraftForge.EVENT_BUS.register(new RTEventHandler());
		MinecraftForge.EVENT_BUS.register(new TerratorialHandler());
		MinecraftForge.EVENT_BUS.register(new GourdgoreHandler());
		MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestHandler());
		//FMLCommonHandler.instance().bus().register(eventHandler);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		bwmIntegration.init();
		quarkIntegration.init();
		proxy.init();

		if(!bwmIntegration.isActive()) //add bow and arrow recipes even if better with mods isn't installed.
		{
			String oreArrowhead = "ingotIron";
			if(OreDictionary.doesOreNameExist("ingotSteel"))
				oreArrowhead = "ingotSteel";
			String oreHaft = "stickWood";
			String oreGlue = "slimeball";
			String oreString = "string";
			ItemStack bow = new ItemStack(Items.BOW);
			ItemStack feather = new ItemStack(Items.FEATHER);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatarrow,1)," a "," b ","cbc",'a',oreArrowhead,'b',oreHaft,'c',feather));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1),"bac","ed ","bac",'a',oreArrowhead,'b',oreHaft,'c',oreString,'d',bow,'e',oreGlue));
			GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.pcbblock),new ItemStack(Blocks.STONEBRICK),new ItemStack(Items.FERMENTED_SPIDER_EYE));

			GameRegistry.addSmelting(ModItems.material.getMaterial("midori"),ModItems.material.getMaterial("midori_popped"),0.1f);

			GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScale,1)," i ","iai"," i ",'a',new ItemStack(ModBlocks.worldScaleOre,0,1),'i',new ItemStack(Items.IRON_INGOT));
		}

		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScaleActive,1)," d ","iae"," d ",'a',new ItemStack(ModBlocks.worldScale),'i',new ItemStack(Items.IRON_PICKAXE),'e',new ItemStack(Items.IRON_AXE),'d',new ItemStack(Items.DIAMOND));

		GameRegistry.addSmelting(Items.CARROT,new ItemStack(ModItems.bakedCarrot),0.35f);
		GameRegistry.addSmelting(Items.BEETROOT,new ItemStack(ModItems.bakedBeetroot),0.35f);
		GameRegistry.addSmelting(Blocks.BROWN_MUSHROOM,new ItemStack(ModItems.bakedMushroom),0.35f);
		GameRegistry.addSmelting(Blocks.RED_MUSHROOM,new ItemStack(ModItems.bakedAmanita),0.35f);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMelon),new ItemStack(Items.MELON),new ItemStack(Items.MELON),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMushroom),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieAmanita),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMeat),new ItemStack(ModItems.groundMeat),new ItemStack(ModItems.groundMeat),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));

		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lattice,2)," a ","aaa"," a ",'a',new ItemStack(Blocks.IRON_BARS));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elytraMagma,1),"aa","aa",'a',ModItems.material.getMaterial("ender_cream"));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bannerDetector,1),"aaa","o r","aaa",'a',new ItemStack(Blocks.COBBLESTONE),'o',new ItemStack(Items.ENDER_EYE),'r',new ItemStack(Items.REDSTONE));

		removeCraftingRecipe(new ItemStack(Blocks.STONEBRICK,4));
		GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONEBRICK,1),"aa","aa",'a',ModItems.material.getMaterial("stone_brick"));
		GameRegistry.addSmelting(Blocks.STONE,ModItems.material.getMaterial("stone_brick",4),0.1f);

		//Eriotto's Japanese Culture
		CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.ironSand,1)},new ItemStack(Blocks.IRON_BLOCK,1),8);
		CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(Blocks.IRON_BLOCK,1),new ItemStack(Blocks.SAND,8)},new ItemStack(ModBlocks.ironSand,1),0);
		CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{null,new ItemStack(ModItems.sashimi,3)},new ItemStack(Items.FISH,1),0);
		CraftingManagerFireNet.getInstance().addRecipe(new ItemStack[]{ModItems.japanMaterial.getMaterial("iron_scales",27)},new ItemStack(ModBlocks.ironSand,1),0);
		CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{null,ModItems.japanMaterial.getMaterial("washi",9)},ModItems.japanMaterial.getMaterial("mulberry_sheet"),0);

		OreDictionary.registerOre("logWood",ModBlocks.sakuraLog);
		OreDictionary.registerOre("logWood",ModBlocks.mulberryLog);
		OreDictionary.registerOre("plankWood",ModBlocks.sakuraPlanks);
		OreDictionary.registerOre("plankWood",ModBlocks.mulberryPlanks);
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraPlanks,4),new ItemStack(ModBlocks.sakuraLog));
		GameRegistry.addShapedRecipe(new ItemStack(Items.PAPER),"aaa",'a',new ItemStack(ModBlocks.mulberryLog));
		GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberryPlanks),new ItemStack(ModBlocks.mulberryLog));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("mulberry_sheet"),"aa","aa",'a',ModItems.japanMaterial.getMaterial("mulberry_paste"));

		addFoldingRecipe(ModItems.japanMaterial.getMaterial("hocho_tetsu_fold_1"),ModItems.japanMaterial.getMaterial("hocho_tetsu_heated"));
		addFoldingRecipe(ModItems.japanMaterial.getMaterial("hocho_tetsu_fold_2"),ModItems.japanMaterial.getMaterial("hocho_tetsu_fold_1"));
		addFoldingRecipe(ModItems.japanMaterial.getMaterial("hocho_tetsu_finished"),ModItems.japanMaterial.getMaterial("hocho_tetsu_fold_2"));

		addFoldingRecipe(ModItems.japanMaterial.getMaterial("tamahagane_folded"),ModItems.japanMaterial.getMaterial("tamahagane_heated"));
		addFoldingRecipe(ModItems.japanMaterial.getMaterial("tamahagane_finished"),ModItems.japanMaterial.getMaterial("tamahagane_reheated"));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("tamahagane_wrapped")," w ","wtw"," w ",'t',ModItems.japanMaterial.getMaterial("tamahagane_folded"),'w',ModItems.japanMaterial.getMaterial("washi"));

		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("tsuka"),"lll","ssp",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'p',new ItemStack(ModBlocks.sakuraPlanks));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("half_katana_blade"),"t","h","t",'t',ModItems.japanMaterial.getMaterial("tamahagane_finished"),'h',ModItems.japanMaterial.getMaterial("hocho_tetsu_finished"));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("ya_head",3),"t  ","hh ","ttt",'t',ModItems.japanMaterial.getMaterial("tamahagane_finished"),'h',ModItems.japanMaterial.getMaterial("hocho_tetsu_finished"));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("yumi_top")," b","bs","bl",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'b',ModItems.japanMaterial.getMaterial("bamboo_slats"));
		GameRegistry.addShapedRecipe(ModItems.japanMaterial.getMaterial("yumi_bottom"),"bl","bs"," b",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'b',ModItems.japanMaterial.getMaterial("bamboo_slats"));

		GameRegistry.addShapedRecipe(new ItemStack(ModItems.katana),"l","l","w",'l',ModItems.japanMaterial.getMaterial("half_katana_blade"),'w',ModItems.japanMaterial.getMaterial("tsuka"));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.wakizashi),"l","l","w",'l',ModItems.japanMaterial.getMaterial("tamahagane_finished"),'w',ModItems.japanMaterial.getMaterial("tsuka"));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.tanto),"l","w",'l',ModItems.japanMaterial.getMaterial("tamahagane_finished"),'w',ModItems.japanMaterial.getMaterial("tsuka"));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.shinai),"l","l","w",'l',ModItems.japanMaterial.getMaterial("bamboo_slats"),'w',ModItems.japanMaterial.getMaterial("tsuka"));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.ya,12),"h","l","f",'h',ModItems.japanMaterial.getMaterial("ya_head"),'l',ModItems.japanMaterial.getMaterial("bamboo_slats"),'f',new ItemStack(Items.FEATHER));
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.yumi)," ns","l s"," us",'n',ModItems.japanMaterial.getMaterial("yumi_top"),'u',ModItems.japanMaterial.getMaterial("yumi_bottom"),'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STRING));

		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.nettedScreen),"bsb","sss","bsb",'s',new ItemStack(Items.STRING),'b',ModItems.japanMaterial.getMaterial("bamboo_slats"));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bambooSlats),"bb","bb",'b',ModItems.japanMaterial.getMaterial("bamboo_slats"));
		GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.tatara),"idi","g g","ini",'i',new ItemStack(Items.IRON_INGOT),'g',new ItemStack(Items.GOLD_INGOT),'d',new ItemStack(Items.DIAMOND),'n',new ItemStack(Blocks.NETHERRACK));

		GameRegistry.addSmelting(ModItems.japanMaterial.getMaterial("rice_stalk"),ModItems.japanMaterial.getMaterial("rice_ash"),0.1f);

		CraftingManagerSoakingBox.instance().getWorkingRecipe(ModItems.japanMaterial.getMaterial("rice"),ModItems.japanMaterial.getMaterial("soaked_rice"));
		CraftingManagerSoakingBox.instance().getWorkingRecipe(new ItemStack(ModBlocks.mulberryLog),ModItems.japanMaterial.getMaterial("soaked_mulberry"));

		CraftingManagerDryingBox.instance().getWorkingRecipe(ModItems.japanMaterial.getMaterial("rice_stalk"),ModItems.japanMaterial.getMaterial("rice_hay"));
		CraftingManagerDryingBox.instance().getWorkingRecipe(ModItems.japanMaterial.getMaterial("soaked_mulberry"),ModItems.japanMaterial.getMaterial("mulberry_paste"));
		CraftingManagerDryingBox.instance().getWorkingRecipe(ModItems.japanMaterial.getMaterial("soaked_bamboo"),ModItems.japanMaterial.getMaterial("bamboo_slats"));

		CraftingManagerTatara.instance().addSmeltingRecipe(new ItemStack(ModBlocks.ironSand),new ItemStack(ModBlocks.kera));
		CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.japanMaterial.getMaterial("tamahagane"),ModItems.japanMaterial.getMaterial("tamahagane_heated"));
		CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.japanMaterial.getMaterial("tamahagane_wrapped"),ModItems.japanMaterial.getMaterial("tamahagane_reheated"));
		CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.japanMaterial.getMaterial("hocho_tetsu"),ModItems.japanMaterial.getMaterial("hocho_tetsu_heated"));
	}

	private void addFoldingRecipe(ItemStack out, ItemStack in)
	{
		GameRegistry.addShapedRecipe(out,"t","s",'t',in,'s',new ItemStack(Items.STICK));
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	public static void removeSmeltingRecipe(ItemStack withoutput)
	{
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		for(Iterator<Map.Entry<ItemStack, ItemStack>> furnaceIterator = smeltingList.entrySet().iterator(); furnaceIterator.hasNext(); ) {
			Map.Entry<ItemStack, ItemStack> recipe = furnaceIterator.next();
			if(withoutput == null || !ItemStack.areItemStacksEqual(withoutput, recipe.getValue())) continue;
			furnaceIterator.remove();
		}
	}

	public static void removeCraftingRecipe(ItemStack withoutput)
	{
		List<IRecipe> craftingList = CraftingManager.getInstance().getRecipeList();
		for(Iterator<IRecipe> craftingIterator = craftingList.iterator(); craftingIterator.hasNext(); ) {
			IRecipe recipe = craftingIterator.next();
			if(withoutput == null || !ItemStack.areItemStacksEqual(withoutput, recipe.getRecipeOutput())) continue;
			craftingIterator.remove();
		}
	}

	public ASMDataTable getASMData()
	{
		return asmDataTable;
	}
}
