package cf.brforgers.forged.modules.climi;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cf.brforgers.core.lib.ModHelper;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import cf.brforgers.forged.modules.climi.wailawrecker.WailaWrecker;
import cf.brforgers.forged.modules.moarrecipes.MoarRecipes;
import cpw.mods.fml.common.Loader;
//import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;

//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.ModContainer;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.registry.GameData;

/**
 * A Modified version of LiMI, from Vazkii.
 * @author AdrianTodt (Original code by Vazkii)
 *
 */
//@Mod(modid = "CLiMI")
public class CLiMI extends SimplerModule {
	public String name() {
		return "CLiMI";
	}
	public ForgedEventState loadAt() {
		return ForgedEventState.POSTINITIALIZATION;
	}
	@Override
	public void load(ForgedEvent event) {
		if (WailaWrecker.wailaIn()) { // Waila is in, Let's wreck it.
			if (Loader.instance().getModState(WailaWrecker.getWailaContainer()) == ModState.POSTINITIALIZED) {
				WailaWrecker.Wreck();
			} else {
				WailaWrecker.wrapProxy();
			}
		} else {
			ModHelper.addEventsToBus(new CLiMI());
		}
	}
	
	/// /// - CONSTANTS - /// ///
	/** Private HashMap. Don't touch. */
	private static ConcurrentHashMap<String, String> modnames = new ConcurrentHashMap<String, String>();
	
	/** Default Formatting */
	private static String defaultFormatting = "";
	
	
	/// /// - CONFIGS FOR MODS - /// ///
	/**
	 * Add a Custom Display Name to your Mod
	 * @param modid The MODID of the Mod
	 * @param name The custom appearance of your mod
	 */
	public static void addCustomFormatting(String modid, String name) { modnames.put(modid, name); }
	
	/**
	 * Set the Default Formatting
	 * @param formatting Default Formatting String
	 */
	public static void setDefaultFormatting(String formatting) { defaultFormatting = formatting.replaceAll("&", "\u00a7"); }
	
	/// /// - WHAT MATTERS - /// ///
	
	/**
	 * Get Formatted Name
	 * <br> (Debug purposes)
	 * @param modid The Mod MODID
	 * @param modname The Mod Name
	 * @return Formatted Name
	 */
	public static String getFormattedName(String modid, String modname) { return (modnames.containsKey(modid)) ? modnames.get(modid) : defaultFormatting + modname; }
	
	/**
	 * Event Called on Tooltip Event
	 * @param event Forge Event
	 */
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		String itemName = GameData.getItemRegistry().getNameForObject(event.itemStack.getItem());
		ModContainer mod = Loader.instance().getIndexedModList().get(itemName.split(":")[0]);
		String modname = (mod == null) ? "Minecraft" : mod.getName();
		String modid = (mod == null) ? "Minecraft" : mod.getModId();
		
		event.toolTip.add(getFormattedName(modid, modname));
	}
//	/**
//	 * Event Called on Tooltip Event
//	 * @param event Forge Event
//	 */
//	@SubscribeEvent
//	public void onTooltip(ItemTooltipEvent event) {
//		ModContainer mod = findModContainer(event.itemStack.getItem());
//		String modname = (mod == null) ? "Minecraft" : mod.getName();
//		String modid = (mod == null) ? "Minecraft" : mod.getModId();
//		
//		event.toolTip.add(getFormattedName(modid, modname));
//	}
//	
//	/**
//	 * Find the Mod Container of the Item
//	 * @param item The Item
//	 * @return The Mod Container (Null if not Found/Is Minecraft)
//	 */
//    public static ModContainer findModContainer(Item item) {
//        ResourceLocation resource = (ResourceLocation) GameData.getItemRegistry().getNameForObject(item);
//        for (ModContainer container : Loader.instance().getModList())
//            if (resource.getResourceDomain().toLowerCase(Locale.US).equals(container.getModId().toLowerCase(Locale.US)))
//                return container;
//        return null;
//    }
}
