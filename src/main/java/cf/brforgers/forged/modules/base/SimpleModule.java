package cf.brforgers.forged.modules.base;

public abstract class SimpleModule extends Module {
	public abstract void preInit(ForgedEvent event);
	public abstract void init(ForgedEvent event);
	public abstract void postInit(ForgedEvent event);
	public abstract void loadComplete(ForgedEvent event);
	public void event(ForgedEvent event) {
		switch (event.state) {
		case PREINITIALIZATION:
			preInit(event);
			break;
		case INITIALIZATION:
			init(event);
			break;
		case POSTINITIALIZATION:
			postInit(event);
			break;
		case LOADCOMPLETE:
			loadComplete(event);
			break;
		default:
			super.event(event);
			break;
		}
	}
}
