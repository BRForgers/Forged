package cf.brforgers.forged.modules;

import cf.brforgers.forged.ForgedMod;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.Module;
import cf.brforgers.forged.modules.base.ModuleManager;
import cf.brforgers.forged.modules.base.SimpleModule;
import cf.brforgers.forged.modules.capes.CapeLoader;
import cf.brforgers.forged.modules.climi.CLiMI;
import cf.brforgers.forged.modules.drops.Drops;
import cf.brforgers.forged.modules.illuminblocks.IlluminBlocks;
import cf.brforgers.forged.modules.moarrecipes.MoarRecipes;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ForgedModuleRegister extends SimpleModule {
	public ForgedModuleRegister() {
		ModuleManager manager = (ModuleManager) ForgedMod.instance.moduleManager;
		manager.register(CapeLoader.class);
		manager.register(CLiMI.class);
		manager.register(Drops.class);
		manager.register(IlluminBlocks.class);
		manager.register(MoarRecipes.class);
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
