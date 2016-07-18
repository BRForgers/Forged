package cf.brforgers.forged.proxy;

import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends Proxy {
	public Side getSide() {
		return Side.CLIENT;
	}
}
