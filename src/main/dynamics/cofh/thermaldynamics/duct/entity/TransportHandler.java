package cofh.thermaldynamics.duct.entity;

import cofh.core.render.ShaderHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import org.lwjgl.opengl.GL11;

public class TransportHandler
{
  public static final TransportHandler INSTANCE = new TransportHandler();
  @SideOnly(Side.CLIENT)
  Camera camera;
  
  @SubscribeEvent(priority=EventPriority.HIGHEST)
  @SideOnly(Side.CLIENT)
  public void renderTravellers(RenderLivingEvent.Pre paramPre)
  {
    EntityLivingBase localEntityLivingBase = entity;
    Entity localEntity = ridingEntity;
    if ((localEntity != null) && (localEntity.getClass() == EntityTransport.class))
    {
      paramPre.setCanceled(true);
      if ((localEntityLivingBase instanceof EntityPlayer)) {
        return;
      }
      float f1 = ShaderHelper.midGameTick;
      EntityTransport localEntityTransport = (EntityTransport)localEntity;
      localEntityTransport.setPosition(0.0D);
      localEntity.updateRiderPosition();
      float f2 = prevRotationYaw + (rotationYaw - prevRotationYaw) * f1;
      
      GL11.glPushMatrix();
      float f3 = Math.max(Math.max(Math.max(height, width), originalWidth), originalHeight);
      
      GL11.glTranslated(x, y, z);
      if (f3 > 0.4D)
      {
        double d = 0.4D / f3;
        GL11.glTranslated(0.0D, -d / 2.0D, 0.0D);
        GL11.glScaled(d, d, d);
      }
      else
      {
        GL11.glTranslated(0.0D, 0.0D, 0.0D);
      }
      try
      {
        ridingEntity = null;
        renderer.doRender(localEntityLivingBase, 0.0D, 0.0D, 0.0D, f2, f1);
      }
      finally
      {
        ridingEntity = localEntityTransport;
      }
      GL11.glPopMatrix();
    }
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void controlPlayer(TickEvent.ClientTickEvent paramClientTickEvent)
  {
    if (phase == TickEvent.Phase.END) {
      return;
    }
    Minecraft localMinecraft = Minecraft.getMinecraft();
    
    EntityClientPlayerMP localEntityClientPlayerMP = thePlayer;
    if (localEntityClientPlayerMP == null) {
      return;
    }
    Entity localEntity = ridingEntity;
    if ((localEntity != null) && (localEntity.getClass() == EntityTransport.class))
    {
      EntityTransport localEntityTransport = (EntityTransport)localEntity;
      localEntityTransport.updateRider(localEntityClientPlayerMP);
      if (gameSettings.thirdPersonView != 0) {
        return;
      }
      double d1 = 0.0D;double d2 = 0.0D;
      
      int i = direction;
      switch (i)
      {
      case 0: 
        d2 = 90.0D;
        break;
      case 1: 
        d2 = -90.0D;
        break;
      case 2: 
        d1 = 180.0D;
        break;
      case 3: 
        d1 = 0.0D;
        break;
      case 4: 
        d1 = 90.0D;
        break;
      case 5: 
        d1 = 270.0D;
        break;
      default: 
        return;
      }
      EntityClientPlayerMP tmp172_171 = localEntityClientPlayerMP;172171rotationPitch = ((float)(172171rotationPitch + Math.sin((d2 - rotationPitch) / 180.0D * 3.141592653589793D) * 30.0D));
      if (d2 == 0.0D)
      {
        EntityClientPlayerMP tmp213_212 = localEntityClientPlayerMP;213212rotationYaw = ((float)(213212rotationYaw + Math.sin((d1 - rotationYaw) / 180.0D * 3.141592653589793D) * 30.0D));
      }
    }
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void controlCamera(TickEvent.RenderTickEvent paramRenderTickEvent)
  {
    Minecraft localMinecraft = Minecraft.getMinecraft();
    EntityClientPlayerMP localEntityClientPlayerMP = thePlayer;
    if (localEntityClientPlayerMP == null) {
      return;
    }
    Entity localEntity = ridingEntity;
    if (localEntity == null)
    {
      if ((renderViewEntity != null) && (renderViewEntity == camera))
      {
        renderViewEntity = localEntityClientPlayerMP;
        camera.worldObj = null;
      }
    }
    else if (localEntity.getClass() == EntityTransport.class)
    {
      EntityTransport localEntityTransport = (EntityTransport)localEntity;
      if (camera == null) {
        camera = new Camera();
      }
      camera.copyFromEntityTransport(localEntityTransport, localEntityClientPlayerMP);
      renderViewEntity = camera;
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TransportHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */