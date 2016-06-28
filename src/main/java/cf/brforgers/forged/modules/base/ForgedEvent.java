package cf.brforgers.forged.modules.base;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.relauncher.Side;

public class ForgedEvent {
	public static ForgedEvent Create(FMLStateEvent event) {
		return new ForgedEvent(event);
	}
	private ForgedEvent(FMLStateEvent event) {
		this.event = event;
		this.state = ForgedEventState.fromFML(event);
	}
	public final FMLStateEvent event;
	public final Side side = FMLCommonHandler.instance().getSide();
	public final ForgedEventState state;
}
