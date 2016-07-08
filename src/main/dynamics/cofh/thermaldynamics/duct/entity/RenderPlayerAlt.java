package cofh.thermaldynamics.duct.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class RenderPlayerAlt
  extends RendererLivingEntity
{
  public ModelBiped modelBipedMain;
  public ModelBiped modelArmorChestplate;
  public ModelBiped modelArmor;
  
  public RenderPlayerAlt()
  {
    super(new ModelBiped(0.0F), 0.5F);
    modelBipedMain = ((ModelBiped)mainModel);
    modelArmorChestplate = new ModelBiped(1.0F);
    modelArmor = new ModelBiped(0.5F);
  }
  
  public void func_76986_a(AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
  {
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
    ItemStack localItemStack = inventory.getCurrentItem();
    modelArmorChestplate.heldItemRight = (modelArmor.heldItemRight = modelBipedMain.heldItemRight = localItemStack != null ? 1 : 0);
    if ((localItemStack != null) && (paramAbstractClientPlayer.getItemInUseCount() > 0))
    {
      EnumAction localEnumAction = localItemStack.getItemUseAction();
      if (localEnumAction == EnumAction.block) {
        modelArmorChestplate.heldItemRight = (modelArmor.heldItemRight = modelBipedMain.heldItemRight = 3);
      } else if (localEnumAction == EnumAction.bow) {
        modelArmorChestplate.aimedBow = (modelArmor.aimedBow = modelBipedMain.aimedBow = 1);
      }
    }
    modelArmorChestplate.isSneak = (modelArmor.isSneak = modelBipedMain.isSneak = paramAbstractClientPlayer.isSneaking());
    double d = paramDouble2 - yOffset;
    if ((paramAbstractClientPlayer.isSneaking()) && (!(paramAbstractClientPlayer instanceof EntityPlayerSP))) {
      d -= 0.125D;
    }
    super.doRender(paramAbstractClientPlayer, paramDouble1, d, paramDouble3, paramFloat1, paramFloat2);
    modelArmorChestplate.aimedBow = (modelArmor.aimedBow = modelBipedMain.aimedBow = 0);
    modelArmorChestplate.isSneak = (modelArmor.isSneak = modelBipedMain.isSneak = 0);
    modelArmorChestplate.heldItemRight = (modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0);
  }
  
  protected void func_96449_a(EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3, String paramString, float paramFloat, double paramDouble4)
  {
    if (paramDouble4 < 100.0D)
    {
      Scoreboard localScoreboard = ((AbstractClientPlayer)paramEntityLivingBase).getWorldScoreboard();
      ScoreObjective localScoreObjective = localScoreboard.func_96539_a(2);
      if (localScoreObjective != null)
      {
        Score localScore = localScoreboard.func_96529_a(paramEntityLivingBase.getCommandSenderName(), localScoreObjective);
        if (paramEntityLivingBase.isPlayerSleeping()) {
          func_147906_a(paramEntityLivingBase, localScore.getScorePoints() + " " + localScoreObjective.getDisplayName(), paramDouble1, paramDouble2 - 1.5D, paramDouble3, 64);
        } else {
          func_147906_a(paramEntityLivingBase, localScore.getScorePoints() + " " + localScoreObjective.getDisplayName(), paramDouble1, paramDouble2, paramDouble3, 64);
        }
        paramDouble2 += getFontRendererFromRenderManagerFONT_HEIGHT * 1.15F * paramFloat;
      }
    }
    super.func_96449_a(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3, paramString, paramFloat, paramDouble4);
  }
  
  protected void preRenderCallback(EntityLivingBase paramEntityLivingBase, float paramFloat)
  {
    float f = 0.9375F;
    GL11.glScalef(f, f, f);
  }
  
  protected void func_82408_c(EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
  {
    ItemStack localItemStack = inventory.armorItemInSlot(3 - paramInt);
    if (localItemStack != null)
    {
      Item localItem = localItemStack.getItem();
      if ((localItem instanceof ItemArmor))
      {
        bindTexture(RenderBiped.getArmorResource(paramEntityLivingBase, localItemStack, paramInt, "overlay"));
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
      }
    }
  }
  
  protected int shouldRenderPass(EntityLivingBase paramEntityLivingBase, int paramInt, float paramFloat)
  {
    ItemStack localItemStack = inventory.armorItemInSlot(3 - paramInt);
    if (localItemStack != null)
    {
      Item localItem = localItemStack.getItem();
      if ((localItem instanceof ItemArmor))
      {
        ItemArmor localItemArmor = (ItemArmor)localItem;
        bindTexture(RenderBiped.getArmorResource(paramEntityLivingBase, localItemStack, paramInt, null));
        ModelBiped localModelBiped = paramInt == 2 ? modelArmor : modelArmorChestplate;
        bipedHead.showModel = (paramInt == 0);
        bipedHeadwear.showModel = (paramInt == 0);
        bipedBody.showModel = ((paramInt == 1) || (paramInt == 2));
        bipedRightArm.showModel = (paramInt == 1);
        bipedLeftArm.showModel = (paramInt == 1);
        bipedRightLeg.showModel = ((paramInt == 2) || (paramInt == 3));
        bipedLeftLeg.showModel = ((paramInt == 2) || (paramInt == 3));
        localModelBiped = ForgeHooksClient.getArmorModel(paramEntityLivingBase, localItemStack, paramInt, localModelBiped);
        setRenderPassModel(localModelBiped);
        onGround = mainModel.onGround;
        isRiding = mainModel.isRiding;
        isChild = mainModel.isChild;
        
        int i = localItemArmor.getColor(localItemStack);
        if (i != -1)
        {
          float f1 = (i >> 16 & 0xFF) / 255.0F;
          float f2 = (i >> 8 & 0xFF) / 255.0F;
          float f3 = (i & 0xFF) / 255.0F;
          GL11.glColor3f(f1, f2, f3);
          if (localItemStack.isItemEnchanted()) {
            return 31;
          }
          return 16;
        }
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        if (localItemStack.isItemEnchanted()) {
          return 15;
        }
        return 1;
      }
    }
    return -1;
  }
  
  protected void renderEquippedItems(EntityLivingBase paramEntityLivingBase, float paramFloat)
  {
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
    super.renderArrowsStuckInEntity(paramEntityLivingBase, paramFloat);
    AbstractClientPlayer localAbstractClientPlayer = (AbstractClientPlayer)paramEntityLivingBase;
    ItemStack localItemStack1 = inventory.armorItemInSlot(3);
    float f1;
    if (localItemStack1 != null)
    {
      GL11.glPushMatrix();
      modelBipedMain.bipedHead.postRender(0.0625F);
      Object localObject;
      if ((localItemStack1.getItem() instanceof ItemBlock))
      {
        localObject = MinecraftForgeClient.getItemRenderer(localItemStack1, IItemRenderer.ItemRenderType.EQUIPPED);
        
        int i = (localObject != null) && (((IItemRenderer)localObject).shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, localItemStack1, IItemRenderer.ItemRendererHelper.BLOCK_3D)) ? 1 : 0;
        if ((i != 0) || (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(localItemStack1.getItem()).getRenderType())))
        {
          f1 = 0.625F;
          GL11.glTranslatef(0.0F, -0.25F, 0.0F);
          GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
          GL11.glScalef(f1, -f1, -f1);
        }
        renderManager.itemRenderer.renderItem(paramEntityLivingBase, localItemStack1, 0);
      }
      else if (localItemStack1.getItem() == Items.skull)
      {
        f1 = 1.0625F;
        GL11.glScalef(f1, -f1, -f1);
        localObject = null;
        if (localItemStack1.hasTagCompound())
        {
          NBTTagCompound localNBTTagCompound = localItemStack1.getTagCompound();
          if (localNBTTagCompound.hasKey("SkullOwner", 10)) {
            localObject = NBTUtil.func_152459_a(localNBTTagCompound.getCompoundTag("SkullOwner"));
          } else if ((localNBTTagCompound.hasKey("SkullOwner", 8)) && (!StringUtils.isNullOrEmpty(localNBTTagCompound.getString("SkullOwner")))) {
            localObject = new GameProfile(null, localNBTTagCompound.getString("SkullOwner"));
          }
        }
        TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, localItemStack1.getItemDamage(), (GameProfile)localObject);
      }
      GL11.glPopMatrix();
    }
    float f2;
    if ((paramEntityLivingBase.getCommandSenderName().equals("deadmau5")) && (localAbstractClientPlayer.func_152123_o()))
    {
      bindTexture(localAbstractClientPlayer.getLocationSkin());
      for (bool = false; bool < true; bool++)
      {
        f2 = prevRotationYaw + (rotationYaw - prevRotationYaw) * paramFloat - (prevRenderYawOffset + (renderYawOffset - prevRenderYawOffset) * paramFloat);
        
        float f3 = prevRotationPitch + (rotationPitch - prevRotationPitch) * paramFloat;
        GL11.glPushMatrix();
        GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.375F * (bool * true - 1), 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -0.375F, 0.0F);
        GL11.glRotatef(-f3, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
        f1 = 1.3333334F;
        GL11.glScalef(f1, f1, f1);
        modelBipedMain.renderEars(0.0625F);
        GL11.glPopMatrix();
      }
    }
    boolean bool = localAbstractClientPlayer.func_152122_n();
    if ((bool) && (!paramEntityLivingBase.isInvisible()) && (!localAbstractClientPlayer.getHideCape()))
    {
      bindTexture(localAbstractClientPlayer.getLocationCape());
      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, 0.125F);
      double d1 = field_71091_bM + (field_71094_bP - field_71091_bM) * paramFloat - (prevPosX + (posX - prevPosX) * paramFloat);
      
      double d2 = field_71096_bN + (field_71095_bQ - field_71096_bN) * paramFloat - (prevPosY + (posY - prevPosY) * paramFloat);
      
      double d3 = field_71097_bO + (field_71085_bR - field_71097_bO) * paramFloat - (prevPosZ + (posZ - prevPosZ) * paramFloat);
      
      f2 = prevRenderYawOffset + (renderYawOffset - prevRenderYawOffset) * paramFloat;
      double d4 = MathHelper.sin(f2 * 3.1415927F / 180.0F);
      double d5 = -MathHelper.cos(f2 * 3.1415927F / 180.0F);
      float f7 = (float)d2 * 10.0F;
      if (f7 < -6.0F) {
        f7 = -6.0F;
      }
      if (f7 > 32.0F) {
        f7 = 32.0F;
      }
      float f8 = (float)(d1 * d4 + d3 * d5) * 100.0F;
      float f9 = (float)(d1 * d5 - d3 * d4) * 100.0F;
      if (f8 < 0.0F) {
        f8 = 0.0F;
      }
      float f10 = prevCameraYaw + (cameraYaw - prevCameraYaw) * paramFloat;
      f7 += MathHelper.sin((prevDistanceWalkedModified + (distanceWalkedModified - prevDistanceWalkedModified) * paramFloat) * 6.0F) * 32.0F * f10;
      if (paramEntityLivingBase.isSneaking()) {
        f7 += 25.0F;
      }
      GL11.glRotatef(6.0F + f8 / 2.0F + f7, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(f9 / 2.0F, 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(-f9 / 2.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
      modelBipedMain.renderCloak(0.0625F);
      GL11.glPopMatrix();
    }
    ItemStack localItemStack2 = inventory.getCurrentItem();
    if (localItemStack2 != null)
    {
      GL11.glPushMatrix();
      modelBipedMain.bipedRightArm.postRender(0.0625F);
      GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
      if (fishEntity != null) {
        localItemStack2 = new ItemStack(Items.stick);
      }
      EnumAction localEnumAction = null;
      if (localAbstractClientPlayer.getItemInUseCount() > 0) {
        localEnumAction = localItemStack2.getItemUseAction();
      }
      IItemRenderer localIItemRenderer = MinecraftForgeClient.getItemRenderer(localItemStack2, IItemRenderer.ItemRenderType.EQUIPPED);
      
      int j = (localIItemRenderer != null) && (localIItemRenderer.shouldUseRenderHelper(IItemRenderer.ItemRenderType.EQUIPPED, localItemStack2, IItemRenderer.ItemRendererHelper.BLOCK_3D)) ? 1 : 0;
      if ((j != 0) || (((localItemStack2.getItem() instanceof ItemBlock)) && (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(localItemStack2.getItem()).getRenderType()))))
      {
        f1 = 0.5F;
        GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
        f1 *= 0.75F;
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
      }
      else if (localItemStack2.getItem() == Items.bow)
      {
        f1 = 0.625F;
        GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
        GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(f1, -f1, f1);
        GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      }
      else if (localItemStack2.getItem().isFull3D())
      {
        f1 = 0.625F;
        if (localItemStack2.getItem().shouldRotateAroundWhenRendering())
        {
          GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
          GL11.glTranslatef(0.0F, -0.125F, 0.0F);
        }
        if ((localAbstractClientPlayer.getItemInUseCount() > 0) && (localEnumAction == EnumAction.block))
        {
          GL11.glTranslatef(0.05F, 0.0F, -0.1F);
          GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
          GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
          GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
        }
        GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
        GL11.glScalef(f1, -f1, f1);
        GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      }
      else
      {
        f1 = 0.375F;
        GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
        GL11.glScalef(f1, f1, f1);
        GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
      }
      if (localItemStack2.getItem().requiresMultipleRenderPasses()) {
        for (k = 0; k < localItemStack2.getItem().getRenderPasses(localItemStack2.getItemDamage()); k++)
        {
          int m = localItemStack2.getItem().getColorFromItemStack(localItemStack2, k);
          f5 = (m >> 16 & 0xFF) / 255.0F;
          f4 = (m >> 8 & 0xFF) / 255.0F;
          f2 = (m & 0xFF) / 255.0F;
          GL11.glColor4f(f5, f4, f2, 1.0F);
          renderManager.itemRenderer.renderItem(paramEntityLivingBase, localItemStack2, k);
        }
      }
      int k = localItemStack2.getItem().getColorFromItemStack(localItemStack2, 0);
      float f6 = (k >> 16 & 0xFF) / 255.0F;
      float f5 = (k >> 8 & 0xFF) / 255.0F;
      float f4 = (k & 0xFF) / 255.0F;
      GL11.glColor4f(f6, f5, f4, 1.0F);
      renderManager.itemRenderer.renderItem(paramEntityLivingBase, localItemStack2, 0);
      
      GL11.glPopMatrix();
    }
  }
  
  protected void rotateCorpse(EntityLivingBase paramEntityLivingBase, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if ((paramEntityLivingBase.isEntityAlive()) && (paramEntityLivingBase.isPlayerSleeping()))
    {
      GL11.glRotatef(((AbstractClientPlayer)paramEntityLivingBase).getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(getDeathMaxRotation(paramEntityLivingBase), 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
    }
    else
    {
      super.rotateCorpse(paramEntityLivingBase, paramFloat1, paramFloat2, paramFloat3);
    }
  }
  
  protected void renderLivingAt(EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramEntityLivingBase.isEntityAlive()) && (paramEntityLivingBase.isPlayerSleeping())) {
      super.renderLivingAt(paramEntityLivingBase, paramDouble1 + field_71079_bU, paramDouble2 + field_71082_cx, paramDouble3 + field_71089_bV);
    } else {
      super.renderLivingAt(paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3);
    }
  }
  
  public void doRender(EntityLivingBase paramEntityLivingBase, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
  {
    func_76986_a((AbstractClientPlayer)paramEntityLivingBase, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
  }
  
  protected ResourceLocation getEntityTexture(Entity paramEntity)
  {
    return ((AbstractClientPlayer)paramEntity).getLocationSkin();
  }
  
  public void doRender(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2)
  {
    func_76986_a((AbstractClientPlayer)paramEntity, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\RenderPlayerAlt.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */