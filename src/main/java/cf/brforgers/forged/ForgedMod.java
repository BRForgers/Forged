package cf.brforgers.forged;

import cf.brforgers.forged.modules.ForgedModuleManager;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ModuleManager;
import cf.brforgers.forged.proxy.Proxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;
//import net.minecraftforge.fml.common.FMLLog;
//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.Mod.Instance;
//import net.minecraftforge.fml.common.event.*;

@Mod(modid = Lib.MODID , version = Lib.VERSION , name = Lib.MODNAME)
public class ForgedMod
{
	@Instance(Lib.MODID)
	/** Instance of the Mod */
	public static ForgedMod instance;
	
	@SidedProxy(clientSide="cf.brforgers.forged.proxy.ClientProxy",serverSide="cf.brforgers.forged.proxy.ClientProxy")
	public Proxy proxy;
	
	/** */
	public ModuleManager moduleManager = new ForgedModuleManager();
	
	/** The Mod Logger. */
	public Logger logger;
	
	//All Events redirected to the Manager
	public ForgedMod() {event(null);}
	@EventHandler public void preInit(FMLPreInitializationEvent e) {event(e);}
	@EventHandler public void init(FMLInitializationEvent e) {event(e);}
	@EventHandler public void postInit(FMLPostInitializationEvent e) {event(e);}
	@EventHandler public void loadComplete(FMLLoadCompleteEvent e) {event(e);}
	
	private void event(FMLStateEvent e) {
		if(e==null) moduleManager.event(ForgedEvent.Create(e));
	}
}
