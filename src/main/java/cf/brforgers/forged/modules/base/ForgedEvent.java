package cf.brforgers.forged.modules.base;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ForgedEvent {
    public final FMLStateEvent event;
    public final Side side = FMLCommonHandler.instance().getSide();
    public final ForgedEventState state;

    private ForgedEvent(FMLStateEvent event) {
		this.event = event;
		this.state = ForgedEventState.fromFML(event);
	}

    public static ForgedEvent Create(FMLStateEvent event) {
        return new ForgedEvent(event);
    }
}
