package cf.brforgers.forged;

import java.io.File;
import java.io.PrintWriter;

import org.apache.logging.log4j.Logger;

import cf.brforgers.core.lib.IOHelper;
import net.minecraftforge.common.config.Configuration;

public final class CFG {
	/** "Static" class */ private CFG() {}
	public static File CONFIG_FOLDER, CAPES_FOLDER, MAIN_CFG_FILE, USER_CAPES_FILE, USER_CAPESERVERS_FILE, CLIMI_CFG_FILE;
	public static Configuration MAIN_CFG;
	
	static void init(File configPath) {
		CONFIG_FOLDER = new File(configPath.getParentFile(),"BRFoundation");
		CAPES_FOLDER = new File(CONFIG_FOLDER, "Capes");
		MAIN_CFG_FILE = new File(CONFIG_FOLDER,"BRFoundation.cfg");
		USER_CAPES_FILE = new File(CAPES_FOLDER, "userCapes.txt");
		USER_CAPESERVERS_FILE = new File(CAPES_FOLDER, "userCapeServers.txt");
		CLIMI_CFG_FILE = new File(CONFIG_FOLDER, "CLiMI.cfg");
		
		MAIN_CFG = new Configuration(MAIN_CFG_FILE);
		
		MAIN_CFG.load();
		
		FOUNDATION.MODULE_CAPES = MAIN_CFG.getBoolean("Capes", "FOUNDATION", FOUNDATION.MODULE_CAPES, "Enable/Disable the Capes Module");
		if (FOUNDATION.MODULE_CAPES) {
			FOUNDATION.CAPES.DEFAULT_SERVER = MAIN_CFG.getBoolean("DefaultServer", "CAPES", FOUNDATION.CAPES.DEFAULT_SERVER, "Load the Default Capes Servers (Recommended, only disable if you know what you're doing!)");
			FOUNDATION.CAPES.USER_CAPES = MAIN_CFG.getBoolean("UserCapes", "CAPES", FOUNDATION.CAPES.USER_CAPES, "Load the User-Defined Capes (Recommended the use of other methods. Config Files will be generated in next load)");
			FOUNDATION.CAPES.USER_SERVERS = MAIN_CFG.getBoolean("UserServers", "CAPES", FOUNDATION.CAPES.USER_SERVERS, "Load the User-Defined Cape Servers (Recommended on Modpacks. Config Files will be generated in next load)");
			FOUNDATION.CAPES.SERVER_CAPES = MAIN_CFG.getBoolean("ServerCapes", "CAPES", FOUNDATION.CAPES.SERVER_CAPES, "Disables the ServerSync locally on Client, Globally on Servers (Setting FALSE on Server make this Mod useless Serverside.)");
			
			if(FOUNDATION.CAPES.USER_CAPES) {
				FOUNDATION.CAPES.USER_CAPES_FILE = automaticFileHandle(USER_CAPES_FILE, "userCapes.txt", "{\"ExampleDude1\":\"http://example.com/cape1.png\",\"ExampleDude2\":\"http://example.com/cape2.png\"}", Forged.instance.logger);
			}
			
			if(FOUNDATION.CAPES.USER_SERVERS) {
				FOUNDATION.CAPES.USER_SERVERS_LIST = automaticFileHandle(USER_CAPESERVERS_FILE, "userCapeServers.txt", "# This is the Servers List. Add a Cape Server URL here and it will be loaded", Forged.instance.logger)
						.split("/^(?!#).+/gm");
			}
		}
		
		FOUNDATION.MODULE_MOARRECIPES = MAIN_CFG.getBoolean("MoarRecipes", "FOUNDATION", FOUNDATION.MODULE_MOARRECIPES, "Enable/Disable the MoarRecipes Module");
		if (FOUNDATION.MODULE_MOARRECIPES) {
			FOUNDATION.MOARRECIPES.SHAPED_RECIPES = MAIN_CFG.getBoolean("ShapedRecipes", "MOARRECIPES", FOUNDATION.MOARRECIPES.SHAPED_RECIPES, "Enable the Shaped Recipes of the Module");
			FOUNDATION.MOARRECIPES.SHAPELESS_RECIPES = MAIN_CFG.getBoolean("ShapelessRecipes", "MOARRECIPES", FOUNDATION.MOARRECIPES.SHAPELESS_RECIPES, "Enable the Shapeless Recipes of the Module");
			FOUNDATION.MOARRECIPES.FURNACE_RECIPES = MAIN_CFG.getBoolean("FurnaceRecipes", "MOARRECIPES", FOUNDATION.MOARRECIPES.FURNACE_RECIPES, "Enable the Smelting Recipes of the Module");
			FOUNDATION.MOARRECIPES.DUNGEON_LOOT = MAIN_CFG.getBoolean("DungeonLoot", "MOARRECIPES", FOUNDATION.MOARRECIPES.DUNGEON_LOOT, "Enable the Extra Dungeon Loot of the Module");
		}
		
		FOUNDATION.MODULE_CLIMI = MAIN_CFG.getBoolean("LiMI", "FOUNDATION", FOUNDATION.MODULE_CLIMI, "Enable/Disable the Mod Indicator Module, based on Vazkii LiMI.");
		if (FOUNDATION.MODULE_CLIMI) {
			FOUNDATION.CLIMI.FORCE_CLIMI = MAIN_CFG.getBoolean("ForceLiMI", "LIMI", FOUNDATION.CLIMI.FORCE_CLIMI, "Force the Mod Indicator to be enabled even when WAILA/LiMI is in the Game.");
			FOUNDATION.CLIMI.DEFAULT_FORMATTING = MAIN_CFG.getString("DefaultFormatting", "LIMI", "&9&o", "The formatting codes to use in the tooltip. Use & to substitute for the control character.");
		}
		
		FOUNDATION.MODULE_ILLUMIN = MAIN_CFG.getBoolean("IlluminBlocks", "FOUNDATION", FOUNDATION.MODULE_ILLUMIN, "Enable/Disable the IlluminBlocks Module");
		
		MAIN_CFG.save();
	}
	
	private static String automaticFileHandle(File file, String fileName, String writeOnNew, Logger errorLogger)
	{
		if(file.exists())
		{
			try {
				return IOHelper.toString(file);
			} catch (Exception e) {
				errorLogger.error("Cannot read '" + fileName + "' file. Skipping...");
			}
		} else {
			PrintWriter writer;
			try {
				writer = new PrintWriter(file, "UTF-8");
				writer.print(writeOnNew);
				writer.close();
				return writeOnNew;
			} catch (Exception e) {
				errorLogger.error("Cannot create '" + fileName + "' file. Skipping...");
			}
		}
		return null;
	}
	
	/** Master Tree of Configs */
	public static final class FOUNDATION {
		private FOUNDATION() {}
		public static boolean MODULE_CAPES = true;
		public static final class CAPES {
			private CAPES() {}
			public static boolean DEFAULT_SERVER = true;
			public static boolean USER_CAPES = false;
			public static boolean USER_SERVERS = false;
			public static boolean SERVER_CAPES = true;
			public static String[] USER_SERVERS_LIST = {};
			public static String USER_CAPES_FILE = "null";
		}
		public static boolean MODULE_MOARRECIPES = true;
		public static final class MOARRECIPES {
			private MOARRECIPES() {}
			public static boolean SHAPED_RECIPES = true;
			public static boolean SHAPELESS_RECIPES = true;
			public static boolean FURNACE_RECIPES = true;
			public static boolean DUNGEON_LOOT = true;
		}
		public static boolean MODULE_CLIMI = true;
		public static final class CLIMI {
			private CLIMI() {}
			public static boolean FORCE_CLIMI = false;
			public static String DEFAULT_FORMATTING = "&9&o";
		}
		public static boolean MODULE_ILLUMIN = true;
	}
}
