package cf.brforgers.forged.modules.illuminblocks;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import net.minecraft.init.Blocks;

public class IlluminBlocks extends SimplerModule
{
	@Override
	public void load(ForgedEvent event) {
        Blocks.DIAMOND_ORE.setLightLevel(0.25f);
        Blocks.EMERALD_ORE.setLightLevel(0.25f);
        Blocks.QUARTZ_ORE.setLightLevel(0.25f);
        Blocks.DRAGON_EGG.setLightLevel(0.5f);
        Blocks.IRON_ORE.setLightLevel(0.125f);
        Blocks.COMMAND_BLOCK.setLightLevel(0.25f);
        Blocks.REDSTONE_BLOCK.setLightLevel(0.25f);
        Blocks.END_PORTAL.setLightLevel(1f);
        Blocks.END_PORTAL_FRAME.setLightLevel(0.5F);
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
