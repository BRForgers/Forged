package cf.brforgers.forged.proxy;

import net.minecraftforge.fml.relauncher.Side;

public class ServerProxy extends Proxy {
	public Side getSide() {
		return Side.SERVER;
	}
}
