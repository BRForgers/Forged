package cf.brforgers.forged;

import org.apache.logging.log4j.Logger;

import cf.brforgers.core.UpdateManager;
import cf.brforgers.core.lib.ModHelper;
import cf.brforgers.forged.modules.ForgedModuleManager;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.Module;
import cf.brforgers.forged.modules.capes.CapeLoader;
import cf.brforgers.forged.modules.climi.CLiMI;
import cf.brforgers.forged.modules.illuminblocks.IlluminBlocks;
import cf.brforgers.forged.modules.moarrecipes.MoarRecipes;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.config.Configuration;
//import net.minecraftforge.fml.common.FMLLog;
//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.Mod.Instance;
//import net.minecraftforge.fml.common.event.*;

@Mod(modid = Lib.MODID , version = Lib.VERSION , name = Lib.MODNAME)
public class Forged
{
	@Instance(Lib.MODID)
	/** Instance of the Mod */
	public static Forged instance;
	
	/** */
	public Module moduleManager = ForgedModuleManager.instance;
	
	/** The Mod Logger. */
	public Logger logger;
	
	//All Events redirected to the Manager
	public Forged() {event(null);}
	@EventHandler public void preInit(FMLPreInitializationEvent e) {event(e);}
	@EventHandler public void init(FMLInitializationEvent e) {event(e);}
	@EventHandler public void postInit(FMLPostInitializationEvent e) {event(e);}
	@EventHandler public void loadComplete(FMLLoadCompleteEvent e) {event(e);}
	
	private void event(FMLStateEvent e) {
		if(e==null) moduleManager.event(ForgedEvent.Create(e));
	}
}
