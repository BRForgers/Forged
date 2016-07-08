package cofh.thermaldynamics.duct.entity;

import java.util.Random;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public class ModelWrapper
  extends ModelBiped
{
  ModelBiped base;
  
  public ModelWrapper(ModelBiped paramModelBiped) //NO_UCD
  {
    base = paramModelBiped;
    
    textureWidth = base.textureWidth;
    textureHeight = base.textureHeight;
    if (bipedHead != null) {
      bipedHead = base.bipedHead;
    }
    if (bipedHeadwear != null) {
      bipedHeadwear = base.bipedHeadwear;
    }
    if (bipedBody != null) {
      bipedBody = base.bipedBody;
    }
    if (bipedRightArm != null) {
      bipedRightArm = base.bipedRightArm;
    }
    if (bipedLeftArm != null) {
      bipedLeftArm = base.bipedLeftArm;
    }
    if (bipedRightLeg != null) {
      bipedRightLeg = base.bipedRightLeg;
    }
    if (bipedLeftLeg != null) {
      bipedLeftLeg = base.bipedLeftLeg;
    }
    if (bipedEars != null) {
      bipedEars = base.bipedEars;
    }
    if (bipedCloak != null) {
      bipedCloak = base.bipedCloak;
    }
    heldItemLeft = base.heldItemLeft;
    
    heldItemRight = base.heldItemRight;
    isSneak = base.isSneak;
    isChild = base.isChild;
    isRiding = base.isRiding;
    onGround = base.onGround;
    
    aimedBow = base.aimedBow;
  }
  
  public void render(Entity paramEntity, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
  {
    GL11.glPushMatrix();
    
    RenderPlayerRiding.handleAnimations(this);
    
    GL11.glPopMatrix();
  }
  
  public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, Entity paramEntity) {}
  
  public void renderEars(float paramFloat)
  {
    base.renderEars(paramFloat);
  }
  
  public void renderCloak(float paramFloat)
  {
    base.renderCloak(paramFloat);
  }
  
  public void setLivingAnimations(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3) {}
  
  public ModelRenderer getRandomModelBox(Random paramRandom)
  {
    return base.getRandomModelBox(paramRandom);
  }
  
  public TextureOffset getTextureOffset(String paramString)
  {
    return base.getTextureOffset(paramString);
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\ModelWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */