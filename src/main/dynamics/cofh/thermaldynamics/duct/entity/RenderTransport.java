package cofh.thermaldynamics.duct.entity;

import cofh.core.render.ShaderHelper;
import java.util.List;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTransport
  extends RenderEntity
{
  RenderPlayerRiding renderPlayer = new RenderPlayerRiding();
  WeakHashMap<EntityPlayer, EntityOtherPlayerMP> dolls = new WeakHashMap();
  
  public void doRender(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
  {
    if (riddenByEntity == null) {
      return;
    }
    EntityPlayer localEntityPlayer = null;
    if ((riddenByEntity instanceof EntityPlayer)) {
      localEntityPlayer = (EntityPlayer)riddenByEntity;
    }
    if ((localEntityPlayer == getMinecraftthePlayer) && 
      (getMinecraftgameSettings.thirdPersonView == 0)) {
      return;
    }
    if (localEntityPlayer == null) {
      return;
    }
    EntityTransport localEntityTransport = (EntityTransport)paramEntity;
    
    localEntityTransport.setPosition(ShaderHelper.midGameTick);
    
    localEntityTransport.updateRiderPosition();
    
    EntityOtherPlayerMP localEntityOtherPlayerMP = (EntityOtherPlayerMP)dolls.get(localEntityPlayer);
    if (localEntityOtherPlayerMP == null)
    {
      localEntityOtherPlayerMP = new EntityOtherPlayerMP(worldObj, localEntityPlayer.getGameProfile());
      dolls.put(localEntityPlayer, localEntityOtherPlayerMP);
    }
    List localList = localEntityPlayer.getDataWatcher().getAllWatched();
    if (localList != null) {
      localEntityOtherPlayerMP.getDataWatcher().updateWatchedObjectsFromList(localList);
    }
    for (int i = 1; i < 5; i++) {
      localEntityOtherPlayerMP.setCurrentItemOrArmor(i, localEntityPlayer.getEquipmentInSlot(i));
    }
    renderPlayer.setRenderManager(renderManager);
    
    localEntityTransport.setPosition(0.0D);
    
    GL11.glPushMatrix();
    RenderPlayerRiding.transport = localEntityTransport;
    
    double d = yOffset - 1.62F;
    renderPlayer.func_76986_a(localEntityOtherPlayerMP, paramDouble1, paramDouble2 + d, paramDouble3, paramFloat1, paramFloat2);
    RenderPlayerRiding.transport = null;
    GL11.glPopMatrix();
  }
  
  protected ResourceLocation getEntityTexture(Entity paramEntity)
  {
    return null;
  }
  
  public void copyFromEntityTransport(Entity paramEntity, EntityTransport paramEntityTransport, EntityPlayer paramEntityPlayer)
  {
    if (pos != null) {
      paramEntityTransport.setPosition(0.0D);
    }
    worldObj = getMinecrafttheWorld;
    
    double d1 = 0.0D;double d2 = -(posY - (boundingBox.maxY + boundingBox.minY));double d3 = 0.0D;
    
    posX += d1;
    posY += d2;
    posZ += d3;
    
    lastTickPosX += d1;
    lastTickPosY += d2;
    lastTickPosZ += d3;
    
    prevPosX += d1;
    prevPosY += d2;
    prevPosZ += d3;
    
    rotationYaw = rotationYaw;
    rotationPitch = rotationPitch;
    
    prevRotationYaw = prevRotationYaw;
    prevRotationPitch = prevRotationPitch;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\RenderTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */