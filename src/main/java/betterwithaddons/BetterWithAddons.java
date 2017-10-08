package betterwithaddons;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.GuiHandler;
import betterwithaddons.config.ModConfiguration;
import betterwithaddons.entity.ModEntities;
import betterwithaddons.interaction.ModInteractions;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.tileentity.ModTileEntities;
import betterwithmods.common.BWMRecipes;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.api.item.IIngredient;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions = "1.12,1.12.1,1.12.2", dependencies = "required-after:betterwithmods")
public class BetterWithAddons
{
	@Instance(Reference.MOD_ID)
	public static BetterWithAddons instance;

	@SidedProxy(clientSide = "betterwithaddons.ClientProxy",serverSide = "betterwithaddons.ServerProxy")
	public static IProxy proxy;

	public BWACreativeTab creativeTab;
	public ModConfiguration config;

	public Logger logger;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
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
		ModInteractions.preInitEnd(event);

		MinecraftForge.EVENT_BUS.register(this);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}

	@EventHandler
	public void construct(FMLConstructionEvent event)
	{
		proxy.registerResourcePack();
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
	public void loadComplete(FMLLoadCompleteEvent event)
	{
		ModInteractions.loadComplete(event);
	}

	@SubscribeEvent
	public void remapBlocks(RegistryEvent.MissingMappings<Block> event)
	{
		for(RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings())
		{
			if(!mapping.key.getResourceDomain().equals(Reference.MOD_ID))
				continue;

			if(mapping.key.getResourcePath().equals("pond_base"))
				mapping.remap(ModBlocks.pondReplacement);
		}
	}

	@SubscribeEvent
	public void remapItems(RegistryEvent.MissingMappings<Item> event)
	{
		for(RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings())
		{
			if(!mapping.key.getResourceDomain().equals(Reference.MOD_ID))
				continue;

			if(mapping.key.getResourcePath().equals("pond_base"))
				mapping.remap(Item.getItemFromBlock(ModBlocks.pondReplacement));
			if(mapping.key.getResourcePath().equals("bowl"))
				mapping.remap(ModItems.salts);
		}
	}

	public static void removeSmeltingRecipe(ItemStack withoutput)
	{
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.instance().getSmeltingList();
		for(Iterator<Map.Entry<ItemStack, ItemStack>> furnaceIterator = smeltingList.entrySet().iterator(); furnaceIterator.hasNext(); ) {
			Map.Entry<ItemStack, ItemStack> recipe = furnaceIterator.next();
			if(withoutput.isEmpty() || !ItemStack.areItemStacksEqual(withoutput, recipe.getValue())) continue;
			furnaceIterator.remove();
		}
	}

	public static void removeCraftingRecipe(ItemStack withoutput)
	{
		BWMRecipes.removeRecipe(withoutput);

		/*List<IRecipe> craftingList = CraftingManager.getInstance().getRecipeList();
		for(Iterator<IRecipe> craftingIterator = craftingList.iterator(); craftingIterator.hasNext(); ) {
			IRecipe recipe = craftingIterator.next();
			if(withoutput.isEmpty() || !ItemStack.areItemStacksEqual(withoutput, recipe.getRecipeOutput())) continue;
				craftingIterator.remove();
		}*/
	}
}
