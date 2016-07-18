package cf.brforgers.forged.modules.drops;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Drops extends SimplerModule {
	public static EntityItem CreateDrop(EntityPlayer p, ItemStack i) {
		return new EntityItem(p.worldObj, p.posX, p.posY, p.posZ, i);
	}

	@Override
	public void load(ForgedEvent event) {
		MinecraftForge.EVENT_BUS.register(new Drops());
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
		switch (e.getEntityPlayer().getDisplayNameString()) {
		case "AdrianTodt":
			e.getDrops().add(CreateDrop(e.getEntityPlayer(), new ItemStack(Items.DYE, 1, 14)));
			break;
		case "armelin1":

			break;
		case "RamonIgo":

			break;
		}
	}
}
