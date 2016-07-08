package cofh.thermaldynamics.duct.entity;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SoundWoosh
  extends MovingSound
{
  private final EntityTransport transport;
  
  protected SoundWoosh(EntityTransport paramEntityTransport)
  {
    super(new ResourceLocation("thermaldynamics", "ductsTransportWoosh"));
    transport = paramEntityTransport;
    repeat = true;
    field_147665_h = 0;
    volume = 1.0E-4F;
  }
  
  public void update()
  {
    if ((transport == null) || (transport.isDead))
    {
      if (volume > 0.0F) {
        volume -= 0.25F;
      } else {
        donePlaying = true;
      }
    }
    else
    {
      if (transport.pause > 0)
      {
        if (volume > 0.0F) {
          volume -= 0.25F;
        } else {
          volume = 0.0F;
        }
      }
      else if (volume < 0.5D) {
        volume += 0.0625F;
      } else {
        volume = 0.5F;
      }
      xPosF = ((float)transport.posX);
      yPosF = ((float)transport.posY);
      zPosF = ((float)transport.posZ);
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\SoundWoosh.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */