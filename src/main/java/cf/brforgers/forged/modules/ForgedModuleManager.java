package cf.brforgers.forged.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.Module;
import cf.brforgers.forged.modules.base.ModuleManager;
import net.minecraftforge.common.config.Configuration;

public class ForgedModuleManager extends ModuleManager {
	public ForgedModuleManager() {
		this.logger = ManagerHelper.getLogger(this);
		moduleInstances.put(this.getClass(),this);
		
		this.register(ForgedModuleRegister.class);
	}
	
	public static Map<Class<? extends Module>, Module> moduleInstances = new HashMap<Class<? extends Module>, Module>();
	public static List<Module> enabledModules;
	
	public Module getModule(Class<? extends Module> module) {
		return moduleInstances.get(module);
	}

	@Override
	public void event(ForgedEvent event) {
		for (Iterator iterator = moduleInstances.values().iterator(); iterator.hasNext();) {
			Module module = (Module) iterator.next();
			if(module!=this) module.event(event);
		}
	}

	@Override
	public String name() {
		return "ModuleManager";
	}

	@Override
	public void register(Class<? extends Module> module) {
		if(!moduleInstances.containsKey(module))
			try {
				moduleInstances.put(module,module.newInstance());
			} catch (Exception e) {
				logger.error("Error while registering module \"" + module.getSimpleName()+"", e);
			}
	}

	@Override
	public void unregister(Class<? extends Module> module) {
		if(moduleInstances.containsKey(module))
			try {
				moduleInstances.remove(module);
			} catch (Exception e) {
				logger.error("Error while unregistering module \"" + module.getSimpleName()+"", e);
			}
	}
}
