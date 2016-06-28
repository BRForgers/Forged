package cf.brforgers.forged.proxy;

import cpw.mods.fml.relauncher.Side;

public abstract class Proxy {
	public void conditionalRun(Runnable runAtServer, Runnable runAtClient) {
		runIfNotNull((getSide() == Side.SERVER) ? runAtServer : runAtClient);
	}
	
	public void runIf(Runnable toExecute, Side equalsSide) {
		if(getSide() == equalsSide) runIfNotNull(toExecute);
	}
	public void runIfNotNull(Runnable toExecute) {
		if (toExecute != null) toExecute.run();
	}
	
	public abstract Side getSide();
}
