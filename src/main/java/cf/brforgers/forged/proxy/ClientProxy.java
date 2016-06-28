package cf.brforgers.forged.proxy;

import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends Proxy {
	public Side getSide() {
		return Side.CLIENT;
	}
}
