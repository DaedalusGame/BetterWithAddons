package betterwithaddons;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.GuiHandler;
import betterwithaddons.config.ModConfiguration;
import betterwithaddons.entity.ModEntities;
import betterwithaddons.handler.AssortedHandler;
import betterwithaddons.handler.ElytraUpdriftHandler;
import betterwithaddons.handler.HarvestHandler;
import betterwithaddons.interaction.ModInteractions;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = "after:betterwithmods")
public class BetterWithAddons
{
	@Instance(Reference.MOD_ID)
	public static BetterWithAddons instance;

	@SidedProxy(clientSide = "betterwithaddons.ClientProxy",serverSide = "betterwithaddons.ServerProxy")
	public static IProxy proxy;

	public BWACreativeTab creativeTab;
	public ModConfiguration config;

	public Logger logger;

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
		config = new ModConfiguration();

		logger = event.getModLog();
		config.preInit(event);

		ModBlocks.load(event);
		ModItems.load(event);
		ModTileEntities.register();
		ModEntities.init();
		ModPotions.preInit(event);
		ModInteractions.preInit(event);
		proxy.preInit();

		//AssortedHandler eventHandler = ;
		MinecraftForge.EVENT_BUS.register(new AssortedHandler());
		//MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
		MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestHandler());
		//FMLCommonHandler.instance().bus().register(eventHandler);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModInteractions.init(event);
		proxy.init();

		if(!ModInteractions.bwm.isActive()) //add bow and arrow recipes even if better with mods isn't installed.
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
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		ModInteractions.postInit(event);
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
