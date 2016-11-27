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

		MinecraftForge.EVENT_BUS.register(new AssortedHandler());
		//MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
		MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModInteractions.init(event);
		proxy.init();
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
