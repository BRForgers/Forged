package cf.brforgers.forged.modules.climi.wailawrecker;

import cf.brforgers.forged.modules.ManagerHelper;
import cf.brforgers.forged.modules.climi.CLiMI;
import mcp.mobius.waila.server.ProxyServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is to cause some damage to WAILA
 * @author AdrianTodt
 */
public class WailaWrecker {
	public static void Wreck() {
		try {
			Set<Entry<Object, ArrayList<IEventListener>>> entries = 
					((ConcurrentHashMap<Object, ArrayList<IEventListener>>)
						ReflectionHelper.getPrivateValue(EventBus.class, MinecraftForge.EVENT_BUS, "listeners"))
						.entrySet();
			
			for (Entry<Object, ArrayList<IEventListener>> entry : entries)
				if (entry.getKey() instanceof mcp.mobius.waila.handlers.VanillaTooltipHandler)
					MinecraftForge.EVENT_BUS.unregister(entry.getKey());
		} catch (Exception e) {
			ManagerHelper.getLogger("WailaWrecker").error("Error while Waila Wrecking: ", e);
		}
		MinecraftForge.EVENT_BUS.register(new CLiMI());
		unwrapProxy();
	}
	
	public static boolean wailaIn() {
		return Loader.isModLoaded("Waila");
	}
	
	public static boolean isProxyWrapped() {
		return (mcp.mobius.waila.Waila.proxy instanceof WrappeableProxy);
	}
	
	public static void wrapProxy() {
		mcp.mobius.waila.Waila.proxy = new WrappeableProxy(getInternalProxy());
	}
	
	public static void unwrapProxy() {
		mcp.mobius.waila.Waila.proxy = getInternalProxy();
	}
	
	public static WrappeableProxy getWrappedProxy() {
		wrapProxy(); return (WrappeableProxy) mcp.mobius.waila.Waila.proxy;
	}
	
	public static ProxyServer getInternalProxy() {
		return (isProxyWrapped() ? getWrappedProxy().wrappedProxy : mcp.mobius.waila.Waila.proxy);
	}
	
	public static ModContainer getWailaContainer() {
		return Loader.instance().getIndexedModList().get("Waila");
	}
}
