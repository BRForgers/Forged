package cf.brforgers.forged.modules.base;

import cpw.mods.fml.common.LoaderState.ModState;

public abstract class SimplerModule extends Module {
	public abstract void load(ForgedEvent event);
	public abstract ForgedEventState loadAt();
	public void event(ForgedEvent event) {
		if(event.state.equals(loadAt())) {
			load(event);
		}
	}
}
