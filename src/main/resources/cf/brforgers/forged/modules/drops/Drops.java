package cf.brforgers.forged.modules.drops;

import cf.brforgers.core.lib.ModHelper;
import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class Drops extends SimplerModule {
	@Override
	public void load(ForgedEvent event) {
		ModHelper.addEventsToBus(new Drops());
	}

	@Override
	public ForgedEventState loadAt() {
		return ForgedEventState.POSTINITIALIZATION;
	}

	@Override
	public String name() {
		return "Drops";
	}
	
	@SubscribeEvent
	public void PlayerDrops(PlayerDropsEvent e) {
		switch (e.entityPlayer.getCommandSenderName()) {
		case "AdrianTodt":
			e.drops.add(CreateDrop(e.entityPlayer, new ItemStack(Items.dye,1,14)));
			break;
		case "armelin1":
			
			break;
		case "RamonIgo":
	
			break;
		}
	}

	public static EntityItem CreateDrop(EntityPlayer p, ItemStack i) {
		return new EntityItem(p.worldObj, p.posX, p.posY, p.posZ, i);
	}
}
