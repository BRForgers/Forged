package cf.brforgers.forged.proxy;

import cpw.mods.fml.relauncher.Side;

public class ServerProxy extends Proxy {
	public Side getSide() {
		return Side.SERVER;
	}
}
