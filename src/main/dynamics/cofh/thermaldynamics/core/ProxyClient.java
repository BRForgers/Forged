package cofh.thermaldynamics.core;

import cofh.thermaldynamics.ThermalDynamics;
import cofh.thermaldynamics.debughelper.CommandServerDebug;
import cofh.thermaldynamics.duct.BlockDuct;
import cofh.thermaldynamics.duct.entity.EntityTransport;
import cofh.thermaldynamics.duct.entity.RenderTransport;
import cofh.thermaldynamics.render.RenderDuct;
import cofh.thermaldynamics.render.RenderDuctItems;
import cofh.thermaldynamics.render.item.RenderItemCover;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;

public class ProxyClient // NO_UCD (unused code)
  extends Proxy
{
  public void registerRenderInformation()
  {
    FMLCommonHandler.instance().bus().register(TickHandlerClient.instance);
    for (BlockDuct localBlockDuct : ThermalDynamics.blockDuct) {
      MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(localBlockDuct), RenderDuct.instance);
    }
    
    ClientCommandHandler.instance.registerCommand(new CommandServerDebug());
    
    RenderingRegistry.registerEntityRenderingHandler(EntityTransport.class, new RenderTransport());
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\core\ProxyClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */