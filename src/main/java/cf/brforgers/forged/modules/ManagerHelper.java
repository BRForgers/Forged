package cf.brforgers.forged.modules;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cf.brforgers.forged.modules.base.Module;
import net.minecraftforge.common.config.Configuration;

public class ManagerHelper {
	private static File MAIN_CONFIG_FILE, CONFIG_FOLDER, FORGED_FOLDER;
	
	static void setConfig(File cfg) {
		MAIN_CONFIG_FILE = cfg;
		CONFIG_FOLDER = cfg.getParentFile();
		FORGED_FOLDER = new File(CONFIG_FOLDER, "ForgedModular");
	}
	
	public static File getConfig(String module) {
		return new File(new File(FORGED_FOLDER,module),module+".cfg");
	}
	
	
	public static Logger getLogger(String module) {
		return LogManager.getLogger("ForgedModular> " + module);
	}
	public static Logger getLogger(Module module) {
		return getLogger(module.name);
	}
	
	public static void setLogger(Module module) {
		module.logger = getLogger(module);
	}
}
