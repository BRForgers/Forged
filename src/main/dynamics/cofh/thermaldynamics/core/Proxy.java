package cofh.thermaldynamics.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;

public class Proxy
{
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void registerIcons(TextureStitchEvent.Pre paramPre) {}
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void initializeIcons(TextureStitchEvent.Post paramPost) {}
  
  public void registerRenderInformation() {}
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\core\Proxy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */