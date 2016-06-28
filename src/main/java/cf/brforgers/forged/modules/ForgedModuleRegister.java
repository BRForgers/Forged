package cf.brforgers.forged.modules;

import cf.brforgers.forged.Forged;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.Module;
import cf.brforgers.forged.modules.base.SimpleModule;
import cf.brforgers.forged.modules.capes.CapeLoader;
import cf.brforgers.forged.modules.climi.CLiMI;
import cf.brforgers.forged.modules.drops.Drops;
import cf.brforgers.forged.modules.illuminblocks.IlluminBlocks;
import cf.brforgers.forged.modules.moarrecipes.MoarRecipes;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ForgedModuleRegister extends SimpleModule {
	public ForgedModuleRegister() {
		ForgedModuleManager manager = (ForgedModuleManager) Forged.instance.moduleManager;
		manager.registerModule(CapeLoader.class);
		manager.registerModule(CLiMI.class);
		manager.registerModule(Drops.class);
		manager.registerModule(IlluminBlocks.class);
		manager.registerModule(MoarRecipes.class);
	}
	
	public String name() {
		return "ModuleRegister";
	}

	@Override
	public void preInit(ForgedEvent event) {
		ManagerHelper.setConfig(((FMLPreInitializationEvent)event.event).getSuggestedConfigurationFile());
	}

	@Override
	public void init(ForgedEvent event) {}

	@Override
	public void postInit(ForgedEvent event) {}

	@Override
	public void loadComplete(ForgedEvent event) {}
}
