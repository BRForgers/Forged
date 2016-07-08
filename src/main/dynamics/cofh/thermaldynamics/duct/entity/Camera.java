package cofh.thermaldynamics.duct.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Camera
  extends EntityLivingBase
{
  public static final ItemStack[] emptyItemStacks = new ItemStack[5];
  
  public Camera()
  {
    super(null);
    width = 0.0F;
    height = 0.0F;
    invulnerable = true;
  }
  
  public boolean canTriggerWalking()
  {
    return false;
  }
  
  public ItemStack getHeldItem()
  {
    return null;
  }
  
  public ItemStack getEquipmentInSlot(int paramInt)
  {
    return null;
  }
  
  public void setCurrentItemOrArmor(int paramInt, ItemStack paramItemStack) {}
  
  public ItemStack[] getLastActiveItems()
  {
    return emptyItemStacks;
  }
  
  public void copyFromEntityTransport(EntityTransport paramEntityTransport, EntityPlayer paramEntityPlayer)
  {
    if (pos != null) {
      paramEntityTransport.setPosition(0.0D);
    }
    if (worldObj == null) {
      worldObj = getMinecrafttheWorld;
    }
    double d1 = 0.0D;double d2 = yOffset - 1.62F;double d3 = 0.0D;
    
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
  
  public float getEyeHeight()
  {
    return 0.0F;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\Camera.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */