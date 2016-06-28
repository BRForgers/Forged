package cf.brforgers.forged.modules.capes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cf.brforgers.core.lib.CustomCapes;
import cf.brforgers.core.lib.IOHelper;
import cf.brforgers.forged.Forged;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import cf.brforgers.forged.CFG;
import cf.brforgers.forged.CFG.FOUNDATION;
import cf.brforgers.forged.CFG.FOUNDATION.CAPES;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class CapeLoader extends SimplerModule {
	public static final String DEFAULT_SERVER = "http://brazillianforgers.esy.es/servers.pointer";
	
	@Override
	public ForgedEventState loadAt() {
		return ForgedEventState.INITIALIZATION;
	}

	@Override
	public String name() {
		return "Capes";
	}
	
	@Override
	public void load(ForgedEvent event) {
		try {
			loadServer(DEFAULT_SERVER);
			for (String SERVER : CFG.FOUNDATION.CAPES.USER_SERVERS_LIST) {
				loadServer(SERVER);
			}
			CustomCapes.parse(CFG.FOUNDATION.CAPES.USER_CAPES_FILE, true, false);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loadServer(String str) {
		String[] result = CapeHelper.follow(CapeHelper.newURL(str), 10);
		logger.info("Loading " + result.length + " Cape Definition"+ (result.length == 1 ? "" : "s") +"... (Origin: \""+ str +"\")...");
		List<Boolean> returnList = new ArrayList<Boolean>();
		for (String server : result) {
			returnList.add(CustomCapes.parse(server, true, false));
		}
		
		logger.info("Loaded " + Collections.frequency(returnList, Boolean.TRUE) + " out of " + result.length + " Cape Definiton" + (result.length == 1 ? "" : "s"));
	}
}
