package cf.brforgers.forged.modules.base;

import org.apache.logging.log4j.Logger;

public abstract class Module {
	public final String name = name();
	public Logger logger;

	public void event(ForgedEvent event) {}

	public abstract String name();
}
