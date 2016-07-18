package cf.brforgers.forged;

//import static net.minecraft.util.EnumChatFormatting.*;

import static net.minecraft.util.text.TextFormatting.BOLD;
import static net.minecraft.util.text.TextFormatting.GOLD;

/**
 * BRForgersCore's Strings Lib
 * @author TheFreeHigh
 */
public class Lib
{
	//Mod Identity
	/**
	 * MODID of the Core
	 */
	public static final String MODID = "Forged";
	
	/**
	 * VERSION of the Core
	 */
	public static final String VERSION = "1.0";
	
	/**
	 * MODNAME of the Core
	 */
	public static final String MODNAME = "Forged";
	
	/**
	 * Mod's FANCYNAME
	 * <br> Used in the Update Manager
	 */
	public static final String FANCYNAME = GOLD.toString() + BOLD.toString() + "Forged";
	///**
	// * Mod's FANCYNAME
	// * <br> Used in the Update Manager
	// */
	//public static final String FANCYNAME = TextFormatting.DARK_GREEN + "" + TextFormatting.BOLD + "BR" + TextFormatting.GOLD + "" + TextFormatting.BOLD + "Foundation";
	
	/**
	 * URL to Check for Updates
	 */
	public static final String UPDATEURL = "https://raw.githubusercontent.com/TheBrazillianForgersTeam/BRCore/master/latest.txt";
	
	/* Add dependences="required-after:BRForgersCore@[2.1,)" in @Mod(...) to make the mod dependent on BRForgersCore */
}
