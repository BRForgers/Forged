package cf.brforgers.forged.modules.base;

public abstract class ModuleManager extends Module {
	public abstract void register(Class<? extends Module> module);
	public abstract void unregister(Class<? extends Module> module);
}
