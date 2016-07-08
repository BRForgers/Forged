package cofh.thermaldynamics.duct.entity;

import cofh.core.render.ShaderHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderPlayerRiding
  extends RenderPlayerAlt
{
  static EntityTransport transport;
  
  public void setRenderPassModel(ModelBase paramModelBase)
  {
    if ((paramModelBase instanceof ModelBiped)) {
      renderPassModel = new ModelWrapper((ModelBiped)paramModelBase);
    } else {
      renderPassModel = null;
    }
  }
  
  protected void renderModel(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    GL11.glPushMatrix();
    bindEntityTexture(paramEntityLivingBase);
    renderBiped(modelBipedMain);
    GL11.glPopMatrix();
  }
  
  protected boolean func_110813_b(EntityLivingBase paramEntityLivingBase)
  {
    return (transport != null) && (transportriddenByEntity != getMinecraftthePlayer);
  }
  
  public static void handleAnimations(ModelBiped paramModelBiped)
  {
    if (transport == null) {
      return;
    }
    double d = 0.85D;
    GL11.glScaled(d, d, d);
    
    int i = transportdirection;
    int j = transportoldDirection;
    
    float f1 = (transportprogress + (transportpause > 0 ? 0 : transportstep) * ShaderHelper.midGameTick) / 100.0F;
    
    float f2 = 0.0F;
    float f3;
    switch (i)
    {
    case 0: 
      f3 = 180.0F;
      break;
    case 1: 
      f3 = 0.0F;
      break;
    case 2: 
      f2 = 180.0F;
      f3 = 90.0F;
      break;
    case 3: 
      f2 = 0.0F;
      f3 = 90.0F;
      break;
    case 4: 
      f2 = 90.0F;
      f3 = 90.0F;
      break;
    case 5: 
      f2 = 270.0F;
      f3 = 90.0F;
      break;
    default: 
      return;
    }
    paramModelBiped.bipedLeftLeg.rotateAngleX = (paramModelBiped.bipedLeftLeg.rotateAngleZ = paramModelBiped.bipedRightLeg.rotateAngleX = paramModelBiped.bipedRightLeg.rotateAngleZ = 0.0F);
    if ((i != j) && (i != (j ^ 0x1)))
    {
      float f5 = 0.0F;
      switch (j)
      {
      case 0: 
        f4 = 180.0F;
        break;
      case 1: 
        f4 = 0.0F;
        break;
      case 2: 
        f5 = 180.0F;
        f4 = 90.0F;
        break;
      case 3: 
        f5 = 0.0F;
        f4 = 90.0F;
        break;
      case 4: 
        f5 = 90.0F;
        f4 = 90.0F;
        break;
      case 5: 
        f5 = 270.0F;
        f4 = 90.0F;
        break;
      default: 
        return;
      }
      if (i < 2) {
        f2 = f5;
      } else if (j < 2) {
        f5 = f2;
      }
      float f6 = MathHelper.clamp((f1 - 0.25F) / 0.75F, 0.0F, 1.0F);
      if (Math.abs(f5 - f2) > Math.abs(f5 - f2 - 360.0F)) {
        f2 += 360.0F;
      }
      if (Math.abs(f5 - f2) > Math.abs(f5 - f2 + 360.0F)) {
        f2 -= 360.0F;
      }
      f2 = f2 * f6 + f5 * (1.0F - f6);
      f3 = f3 * f6 + f4 * (1.0F - f6);
      
      f6 = MathHelper.clamp(f6, 0.0F, 1.0F);
      float f7 = f6 * (1.0F - f6) * 4.0F * 60.0F / 180.0F * 3.1415927F;
      if ((i == 0) || (j == 1))
      {
    	  paramModelBiped.bipedLeftLeg.rotateAngleX = (paramModelBiped.bipedRightLeg.rotateAngleX = -f7);
      }
      else if ((i == 1) || (j == 0))
      {
    	  paramModelBiped.bipedLeftLeg.rotateAngleX = (paramModelBiped.bipedRightLeg.rotateAngleX = f7);
      }
      else
      {
        int k;
        if ((i == 2) || (i == 3)) {
          k = (j == 4 ? 1 : 0) == (i == 2 ? 1 : 0) ? 1 : -1;
        } else {
          k = (j == 2 ? 1 : 0) == (i == 4 ? 1 : 0) ? -1 : 1;
        }
        f7 *= k;
        
        paramModelBiped.bipedLeftLeg.rotateAngleZ = (paramModelBiped.bipedRightLeg.rotateAngleZ = f7);
        if (f7 < 0.0F) {
        	paramModelBiped.bipedRightLeg.rotationPointZ *= 0.7F;
        } else {
        	paramModelBiped.bipedLeftLeg.rotationPointZ *= 0.7F;
        }
      }
    }
    GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
    
    paramModelBiped.bipedHead.rotateAngleY = 0.0F;
    paramModelBiped.bipedHead.rotateAngleX = -1.5707964F;
    paramModelBiped.bipedHeadwear.rotateAngleY = paramModelBiped.bipedHead.rotateAngleY;
    paramModelBiped.bipedHeadwear.rotateAngleX = paramModelBiped.bipedHead.rotateAngleX;
    
    paramModelBiped.bipedRightArm.rotateAngleZ = 0.0F;
    paramModelBiped.bipedLeftArm.rotateAngleZ = 0.0F;
    paramModelBiped.bipedRightLeg.rotateAngleY = 0.0F;
    paramModelBiped.bipedLeftLeg.rotateAngleY = 0.0F;
    paramModelBiped.bipedRightArm.rotateAngleX = 0.0F;
    paramModelBiped.bipedLeftArm.rotateAngleX = 0.0F;
    
    paramModelBiped.bipedBody.rotateAngleX = 0.0F;
    
    paramModelBiped.bipedRightLeg.rotationPointZ = 0.1F;
    paramModelBiped.bipedLeftLeg.rotationPointZ = 0.1F;
    paramModelBiped.bipedRightLeg.rotationPointY = 12.0F;
    paramModelBiped.bipedLeftLeg.rotationPointY = 12.0F;
    
    float f4 = 0.0625F;
    
    paramModelBiped.bipedHead.render(f4);
    paramModelBiped.bipedBody.render(f4);
    paramModelBiped.bipedRightArm.render(f4);
    paramModelBiped.bipedLeftArm.render(f4);
    paramModelBiped.bipedHeadwear.render(f4);
    if (j != i)
    {
      GL11.glTranslatef(0.0F, -0.3F, 0.0F);
    }
    else if ((f1 < 0.5F) && 
      (transportpos != null))
    {
      TileEntity localTileEntity = transportworldObj.getTileEntity(transportpos.x + net.minecraft.util.Facing.offsetsXForSide[(i ^ 0x1)], transportpos.y + net.minecraft.util.Facing.offsetsYForSide[(i ^ 0x1)], transportpos.z + net.minecraft.util.Facing.offsetsZForSide[(i ^ 0x1)]);
      if ((localTileEntity instanceof TileTransportDuctBase))
      {
        if (neighborTypes[(i ^ 0x1)] == TileTDBase.NeighborTypes.NONE) {
          GL11.glTranslatef(0.0F, -0.3F * (1.0F - f1 * 2.0F), 0.0F);
        }
      }
      else {
        GL11.glTranslatef(0.0F, -0.3F * (1.0F - f1 * 2.0F), 0.0F);
      }
    }
    paramModelBiped.bipedRightLeg.render(f4);
    paramModelBiped.bipedLeftLeg.render(f4);
  }
  
  private void renderBiped(ModelBiped paramModelBiped)
  {
    handleAnimations(paramModelBiped);
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\RenderPlayerRiding.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */