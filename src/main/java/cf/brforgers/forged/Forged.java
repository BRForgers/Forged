package cf.brforgers.forged;

import cf.brforgers.forged.modules.base.Module;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.Side;

/**
 * 
 * @author AdrianTodt
 */
public class Forged {
	/**
	 * Run based at the {@link Side} of the game
	 * @param runIfAtServer will run if we're at {@link Side}.SERVER
	 * @param runIfAtClient will run if we're at {@link Side}.CLIENT
	 */
	public static void ConditionalRun(Runnable runIfAtServer, Runnable runIfAtClient) {
		ForgedMod.instance.proxy.conditionalRun(runIfAtServer, runIfAtClient);
	}
	
	/**
	 * Run if the {@link Side} of the game is as specified
	 * @param side that will make the Code run
	 * @param run will be executed if the side is correct
	 */
	public static void RunIfAtSide(Side side, Runnable run) {
		ForgedMod.instance.proxy.runIf(run, side);
	}
	
	/**
	 * Give the {@link Side} detected via {@link SidedProxy}
	 * @return
	 */
	public static Side GetProxySide() {
		return ForgedMod.instance.proxy.getSide();
	}
	
	/**
	 * Register the Module, especified by the Class, in the ModuleManager
	 * @param module
	 */
	public static void RegisterModule(Class<? extends Module> module) {
		ForgedMod.instance.moduleManager.register(module);
	}
}
