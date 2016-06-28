package cf.brforgers.forged.modules.climi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.Logger;

import cf.brforgers.forged.CFG;
import cf.brforgers.forged.Forged;
import cf.brforgers.forged.modules.climi.wailawrecker.WailaWrecker;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.Loader;
//import net.minecraftforge.fml.common.LoaderState.ModState;
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Helper {
	public static void pre() {
		File configFile = CFG.CLIMI_CFG_FILE;
		try {
			if (!configFile.exists()) configFile.createNewFile();
			String in = IOUtils.toString(new FileReader(configFile));
			if (in == null || in.equals("")) {
				Files.write(
						configFile.toPath(), Arrays.asList(
								"# CLiMI Custom Modnames", 
								"# Syntax: \"MODID\" \"CUSTOMMODNAME\"",
								"# Example: \"BRFoundation\" \"&eBR&fFoundation\"",
								"# You can use both & and § for colors!"
						), Charset.forName("UTF-8")
				);
			} else {
				String[] lines = in.split(SystemUtils.LINE_SEPARATOR);
				for (String line : lines) {
					for (int i = 0; i < line.length(); i++) {
						if (line.charAt(i) == '#' || (i+1 < line.length() && line.charAt(i) == '/' && line.charAt(i+1) == '/')) {
							break;
						} else if (i-1 != 0 && line.charAt(i) == '"' && line.charAt(i-1) != '\\') {
							
						}
					}
				}
			}
		} catch(Exception e) {}
	}
}
