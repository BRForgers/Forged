package cf.brforgers.forged.modules.base;

import cpw.mods.fml.common.event.FMLStateEvent;

public enum ForgedEventState {
	REGISTER, PREINITIALIZATION, INITIALIZATION, POSTINITIALIZATION, LOADCOMPLETE, EXTRA;
	
	public static ForgedEventState fromFML(FMLStateEvent e) {
		if (e==null) return REGISTER;
		switch (e.getModState()) {
			case PREINITIALIZED: return PREINITIALIZATION;
			case INITIALIZED: return INITIALIZATION;
			case POSTINITIALIZED: return POSTINITIALIZATION;
			case AVAILABLE: return LOADCOMPLETE;
			default: return EXTRA;
		}
	}
}
