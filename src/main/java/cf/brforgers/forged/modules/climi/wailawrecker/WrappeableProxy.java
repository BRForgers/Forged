package cf.brforgers.forged.modules.climi.wailawrecker;

public class WrappeableProxy extends mcp.mobius.waila.server.ProxyServer {
	public mcp.mobius.waila.server.ProxyServer wrappedProxy;
	public WrappeableProxy(mcp.mobius.waila.server.ProxyServer wrapped) { wrappedProxy = wrapped; }
	public void registerHandlers() { wrappedProxy.registerHandlers(); WailaWrecker.Wreck(); }
	public void registerMods() { wrappedProxy.registerMods(); }
	public void registerIMCs() { wrappedProxy.registerIMCs(); }
	public void callbackRegistration(String a, String b) { wrappedProxy.callbackRegistration(a, b); }	
	public Object getFont() { return wrappedProxy.getFont(); }
}
