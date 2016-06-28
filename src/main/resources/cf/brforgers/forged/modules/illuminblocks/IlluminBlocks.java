package cf.brforgers.forged.modules.illuminblocks;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import net.minecraft.init.Blocks;

public class IlluminBlocks extends SimplerModule
{
	@Override
	public void load(ForgedEvent event) {
		Blocks.diamond_ore.setLightLevel(0.25f);
		Blocks.emerald_ore.setLightLevel(0.25f);
		Blocks.quartz_ore.setLightLevel(0.25f);
		Blocks.dragon_egg.setLightLevel(0.5f);
		Blocks.iron_ore.setLightLevel(0.125f);
		Blocks.command_block.setLightLevel(0.25f);
		Blocks.redstone_block.setLightLevel(0.25f);
		Blocks.end_portal.setLightLevel(1f);
		Blocks.end_portal_frame.setLightLevel(0.5F);
	}

	@Override
	public ForgedEventState loadAt() {
		return ForgedEventState.PREINITIALIZATION;
	}

	@Override
	public String name() {
		return "IlluminBlocks";
	}
}
