package cofh.thermaldynamics.render;

import cofh.core.render.ShaderHelper;
import cofh.thermalfoundation.render.shader.ShaderStarfield;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;

public class RenderTest
{
  @SubscribeEvent
  public void renderStart(RenderLivingEvent.Pre paramPre)
  {
    ShaderHelper.useShader(ShaderStarfield.starfieldShader, null);
  }
  
  @SubscribeEvent
  public void renderStop(RenderLivingEvent.Post paramPost) {}
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\RenderTest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */