package cofh.thermaldynamics.render;

import cofh.core.block.BlockCoFHBase;
import cofh.core.render.IconRegistry;
import cofh.core.render.RenderUtils;
import cofh.core.render.RenderUtils.ScaledIconTransformation;
import cofh.repack.codechicken.lib.lighting.LightModel;
import cofh.repack.codechicken.lib.render.CCModel;
import cofh.repack.codechicken.lib.render.CCRenderState;
import cofh.repack.codechicken.lib.render.CCRenderState.IVertexOperation;
import cofh.repack.codechicken.lib.vec.Cuboid6;
import cofh.repack.codechicken.lib.vec.Scale;
import cofh.repack.codechicken.lib.vec.Translation;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.block.Attachment;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.core.TDProps;
import cofh.thermaldynamics.duct.BlockDuct;
import cofh.thermaldynamics.duct.BlockDuct.ConnectionTypes;
import cofh.thermaldynamics.duct.Duct;
import cofh.thermaldynamics.duct.TDDucts;
import cofh.thermaldynamics.duct.attachments.cover.Cover;
import cofh.thermalfoundation.fluid.TFFluids;
import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderDuct
  implements ISimpleBlockRenderingHandler, IItemRenderer
{
  public static final RenderDuct instance = new RenderDuct();
  static final int[] INV_CONNECTIONS = { BlockDuct.ConnectionTypes.DUCT.ordinal(), BlockDuct.ConnectionTypes.DUCT.ordinal(), 0, 0, 0, 0 };
  static int[] connections = new int[6];
  static IIcon textureCenterLine;
  public static IIcon coverBase;
  public static IIcon signalTexture;
  public static IIcon[] servoTexture = new IIcon[10];
  public static IIcon[] retrieverTexture = new IIcon[10];
  public static IIcon[] filterTexture = new IIcon[5];
  public static IIcon sideDucts;
  static CCModel[][] modelFluid = new CCModel[6][7];
  public static CCModel[][] modelConnection = new CCModel[3][6];
  static CCModel modelCenter;
  static CCModel[] modelLine = new CCModel[6];
  static CCModel modelLineCenter;
  static CCModel[] modelFrameConnection = new CCModel[64];
  static CCModel[] modelFrame = new CCModel[64];
  static CCModel[] modelTransportConnection = new CCModel[64];
  static CCModel[] modelTransport = new CCModel[64];
  public static CCModel[] modelOpaqueTubes;
  public static CCModel[] modelTransTubes;
  private static CCModel[] modelFluidTubes;
  private static CCModel[] modelLargeTubes;
  
  static
  {
    TDProps.renderDuctId = RenderingRegistry.getNextAvailableRenderId();
    RenderingRegistry.registerBlockHandler(instance);
    
    generateModels();
    generateFluidModels();
  }
  
  public static void initialize()
  {
    generateFluidModels();
    generateModels();
    for (int i = 0; i < 10; i++)
    {
      servoTexture[i] = IconRegistry.getIcon("ServoBase" + i);
      retrieverTexture[i] = IconRegistry.getIcon("RetrieverBase" + i);
    }
    for (i = 0; i < 5; i++) {
      filterTexture[i] = IconRegistry.getIcon("FilterBase" + i);
    }
    coverBase = IconRegistry.getIcon("CoverBase");
    sideDucts = IconRegistry.getIcon("SideDucts");
    signalTexture = IconRegistry.getIcon("Signaller");
    
    textureCenterLine = TFFluids.fluidSteam.getIcon();
  }
  
  private static void generateFluidModels()
  {
    for (int i = 1; i < 7; i++)
    {
      double d1 = 0.47D - 0.025D * i;
      double d2 = 0.53D + 0.025D * i;
      double d3 = 0.32D + 0.06D * i;
      double d4 = 0.32D;
      double d5 = 0.68D;
      double[][] arrayOfDouble = { { d1, 0.0D, d1, d2, d4, d2 }, { d1, d3, d1, d2, 1.0D, d2 }, { d4, d4, 0.0D, d5, d3, d4 }, { d4, d4, d5, d5, d3, 1.0D }, { 0.0D, d4, d4, d4, d3, d5 }, { d5, d4, d4, 1.0D, d3, d5 }, { d4, d4, d4, d5, d3, d5 } };
      for (int j = 0; j < 7; j++) {
        modelFluid[(i - 1)][j] = CCModel.quadModel(24).generateBlock(0, arrayOfDouble[j][0], arrayOfDouble[j][1], arrayOfDouble[j][2], arrayOfDouble[j][3], arrayOfDouble[j][4], arrayOfDouble[j][5]).computeNormals();
      }
    }
  }
  
  private static void generateModels()
  {
    modelCenter = CCModel.quadModel(48).generateBox(0, -3.0D, -3.0D, -3.0D, 6.0D, 6.0D, 6.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D);
    
    modelConnection[0][1] = CCModel.quadModel(48).generateBlock(0, new Cuboid6(0.3125D, 0.6875D, 0.3125D, 0.6875D, 1.0D, 0.6875D).expand(-9.765625E-4D));
    modelConnection[1][1] = CCModel.quadModel(24).generateBox(0, -4.0D, 4.0D, -4.0D, 8.0D, 3.9990234375D, 8.0D, 0.0D, 0.0D, 32.0D, 32.0D, 16.0D).computeNormals();
    modelConnection[2][1] = CCModel.quadModel(24).generateBox(0, -4.0D, 4.0D, -4.0D, 8.0D, 3.9990234375D, 8.0D, 0.0D, 16.0D, 32.0D, 32.0D, 16.0D).computeNormals();
    
    double d = 0.4D;
    modelLineCenter = CCModel.quadModel(24).generateBlock(0, d, d, d, 1.0D - d, 1.0D - d, 1.0D - d).computeNormals();
    modelLine[1] = CCModel.quadModel(16).generateBlock(0, d, 1.0D - d, d, 1.0D - d, 1.0D, 1.0D - d, 3).computeNormals();
    CCModel.generateSidedModels(modelLine, 1, Vector3.center);
    
    modelOpaqueTubes = ModelHelper.StandardTubes.genModels(0.1875F, true);
    modelTransTubes = ModelHelper.StandardTubes.genModels(0.1875F, false);
    modelFluidTubes = ModelHelper.StandardTubes.genModels(0.1875F * TDProps.smallInnerModelScaling, false, false);
    modelLargeTubes = ModelHelper.StandardTubes.genModels(0.21875F, true);
    
    modelFrameConnection = new ModelHelper.OctagonalTubeGen(0.375D, 0.1812D, true).generateModels();
    modelFrame = new ModelHelper.OctagonalTubeGen(0.375D * TDProps.largeInnerModelScaling, 0.1812D, false).generateModels();
    
    modelTransportConnection = new ModelHelper.OctagonalTubeGen(0.5D * TDProps.largeInnerModelScaling, 0.1812D, true).generateModels();
    
    modelTransport = new ModelHelper.OctagonalTubeGen(0.5D * TDProps.largeInnerModelScaling * TDProps.largeInnerModelScaling, 0.1812D, false).generateModels();
    
    CCModel.generateBackface(modelCenter, 0, modelCenter, 24, 24);
    CCModel.generateBackface(modelConnection[0][1], 0, modelConnection[0][1], 24, 24);
    modelConnection[0][1].apply(RenderUtils.getRenderVector(-0.5D, -0.5D, -0.5D).translation());
    for (int i = 0; i < modelConnection.length; i++) {
      CCModel.generateSidedModels(modelConnection[i], 1, Vector3.zero);
    }
    Scale[] arrayOfScale = { new Scale(1.0D, -1.0D, 1.0D), new Scale(1.0D, 1.0D, -1.0D), new Scale(-1.0D, 1.0D, 1.0D) };
    int m;
    for (int j = 0; j < modelConnection.length; j++)
    {
      CCModel[] arrayOfCCModel1 = modelConnection[j];
      for (m = 2; m < 6; m += 2) {
        arrayOfCCModel1[m] = arrayOfCCModel1[0].sidedCopy(0, m, Vector3.zero);
      }
      for (m = 1; m < 6; m += 2) {
        arrayOfCCModel1[m] = arrayOfCCModel1[(m - 1)].backfacedCopy().apply(arrayOfScale[(m / 2)]);
      }
    }
    modelCenter.computeNormals().computeLighting(LightModel.standardLightModel).shrinkUVs(9.765625E-4D);
    for (CCModel[] arrayOfCCModel2 : modelConnection) {
      for (CCModel localCCModel : arrayOfCCModel2) {
        localCCModel.computeNormals().computeLighting(LightModel.standardLightModel).shrinkUVs(9.765625E-4D);
      }
    }
  }
  
  public boolean renderBase(boolean paramBoolean, int paramInt, int[] paramArrayOfInt, double paramDouble1, double paramDouble2, double paramDouble3, IIcon paramIIcon)
  {
    paramDouble1 += 0.5D;
    paramDouble2 += 0.5D;
    paramDouble3 += 0.5D;
    
    Translation localTranslation = RenderUtils.getRenderVector(paramDouble1, paramDouble2, paramDouble3).translation();
    
    int i = 0;
    Duct localDuct = (Duct)TDDucts.ductList.get(paramInt);
    for (int j = 0; j < 6; j++) {
      if (BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct())
      {
        RenderUtils.ScaledIconTransformation localScaledIconTransformation2;
        if (BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]] == BlockDuct.ConnectionTypes.STRUCTURE)
        {
          localScaledIconTransformation2 = RenderUtils.getIconTransformation(structureiconBaseTexture);
          modelConnection[0][j].render(8, 24, new CCRenderState.IVertexOperation[] { localTranslation, localScaledIconTransformation2 });
          modelConnection[0][j].render(32, 48, new CCRenderState.IVertexOperation[] { localTranslation, localScaledIconTransformation2 });
          if (iconConnectionTexture != null) {
            modelConnection[1][j].render(new CCRenderState.IVertexOperation[] { localTranslation, RenderUtils.getIconTransformation(iconConnectionTexture) });
          }
        }
        else
        {
          i |= 1 << j;
          if ((paramBoolean) && (paramIIcon != null))
          {
            localScaledIconTransformation2 = RenderUtils.getIconTransformation(structureInvisiconBaseTexture);
            modelConnection[0][j].render(4, 8, new CCRenderState.IVertexOperation[] { localTranslation, localScaledIconTransformation2 });
          }
          if ((paramArrayOfInt[j] == BlockDuct.ConnectionTypes.TILECONNECTION.ordinal()) && (iconConnectionTexture != null)) {
            modelConnection[1][j].render(new CCRenderState.IVertexOperation[] { localTranslation, RenderUtils.getIconTransformation(iconConnectionTexture) });
          }
        }
      }
    }
    if (paramIIcon != null)
    {
      RenderUtils.ScaledIconTransformation localScaledIconTransformation1 = RenderUtils.getIconTransformation(paramIIcon);
      (opaque ? modelOpaqueTubes[i] : modelTransTubes[i]).render(new CCRenderState.IVertexOperation[] { localTranslation, localScaledIconTransformation1 });
    }
    if ((iconFluidTexture != null) && (fluidTransparency == -1)) {
      modelFluidTubes[i].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFluidTexture));
    }
    if (frameType == 1)
    {
      renderSideTubes(0, paramArrayOfInt, paramDouble1 - 0.5D, paramDouble2 - 0.5D, paramDouble3 - 0.5D, sideDucts);
    }
    else
    {
      int k;
      if ((frameType == 2) && (iconFrameTexture != null))
      {
        i = 0;
        for (k = 0; k < 6; k++) {
          if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[k]].renderDuct()) && (paramArrayOfInt[k] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal()))
          {
            i |= 1 << k;
            if ((paramBoolean) || (paramArrayOfInt[k] != BlockDuct.ConnectionTypes.DUCT.ordinal()))
            {
              modelFrameConnection[(64 + k)].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameBandTexture));
              modelFrame[(70 + k)].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameTexture));
            }
          }
        }
        if (modelFrameConnectionverts.length != 0) {
          modelFrameConnection[i].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameTexture));
        }
      }
      else if ((frameType == 3) && (iconFrameTexture != null))
      {
        modelLargeTubes[i].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameTexture));
      }
      else if ((frameType == 4) && (iconFrameTexture != null))
      {
        i = 0;
        for (k = 0; k < 6; k++) {
          if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[k]].renderDuct()) && (paramArrayOfInt[k] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal()))
          {
            i |= 1 << k;
            if ((paramBoolean) || (paramArrayOfInt[k] != BlockDuct.ConnectionTypes.DUCT.ordinal())) {
              modelTransportConnection[(64 + k)].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameBandTexture));
            }
          }
        }
        if (modelTransportConnectionverts.length != 0) {
          modelTransportConnection[i].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(iconFrameTexture));
        }
      }
    }
    return true;
  }
  
  public boolean renderSideTubes(int paramInt, int[] paramArrayOfInt, double paramDouble1, double paramDouble2, double paramDouble3, IIcon paramIIcon)
  {
    CCModel[] arrayOfCCModel = paramInt == 0 ? ModelHelper.SideTubeGen.standardTubes : ModelHelper.SideTubeGen.standardTubesInner;
    int i = 0;
    for (int j = 0; j < 6; j++) {
      if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.CLEANDUCT.ordinal()))
      {
        i |= 1 << j;
        if (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.DUCT.ordinal()) {
          arrayOfCCModel[(64 + j)].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(paramIIcon));
        }
      }
    }
    if (verts.length == 0) {
      return false;
    }
    arrayOfCCModel[i].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(paramIIcon));
    return true;
  }
  
  public boolean renderWorldExtra(boolean paramBoolean, TileTDBase paramTileTDBase, int paramInt, int[] paramArrayOfInt, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
    Duct localDuct = (Duct)TDDucts.ductList.get(paramInt);
    IIcon localIIcon = iconFluidTexture;
    
    boolean bool = false;
    int i;
    int j;
    if ((localIIcon != null) && (fluidTransparency != -1))
    {
      i = 0;
      for (j = 0; j < 6; j++) {
        if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal())) {
          i |= 1 << j;
        }
      }
      modelFluidTubes[i].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(localIIcon));
      
      bool = true;
    }
    if ((frameType == 1) && (iconFrameFluidTexture != null)) {
      bool = (renderSideTubes(1, paramArrayOfInt, paramDouble1, paramDouble2, paramDouble3, iconFrameFluidTexture)) || (bool);
    }
    if ((frameType == 2) && (iconFrameFluidTexture != null))
    {
      i = 0;
      for (j = 0; j < 6; j++) {
        if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal()))
        {
          i |= 1 << j;
          if ((paramBoolean) || (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.DUCT.ordinal())) {
            modelFrame[(70 + j)].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(iconFrameFluidTexture));
          }
        }
      }
      if (modelFrameverts.length != 0)
      {
        modelFrame[i].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(iconFrameFluidTexture));
        bool = true;
      }
    }
    if ((frameType == 4) && (iconFrameFluidTexture != null))
    {
      i = 0;
      for (j = 0; j < 6; j++) {
        if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal())) {
          i |= 1 << j;
        }
      }
      if (modelTransportverts.length != 0)
      {
        modelTransport[i].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(iconFrameFluidTexture));
        bool = true;
      }
    }
    return bool;
  }
  
  public void renderFluid(FluidStack paramFluidStack, int[] paramArrayOfInt, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramFluidStack == null) || (amount <= 0) || (paramInt <= 0)) {
      return;
    }
    GL11.glBlendFunc(770, 771);
    
    CCRenderState.startDrawing();
    Fluid localFluid = paramFluidStack.getFluid();
    
    RenderUtils.setFluidRenderColor(paramFluidStack);
    cofh.lib.render.RenderHelper.bindTexture(cofh.lib.render.RenderHelper.MC_BLOCK_SHEET);
    IIcon localIIcon = cofh.lib.render.RenderHelper.getFluidTexture(paramFluidStack);
    if (localFluid.isGaseous(paramFluidStack))
    {
      CCRenderState.alphaOverride = 32 + 32 * paramInt;
      paramInt = 6;
    }
    int j;
    if (paramInt < 6)
    {
      CCModel[] arrayOfCCModel = modelFluid[(paramInt - 1)];
      for (j = 0; j < 6; j++) {
        if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal())) {
          arrayOfCCModel[j].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(localIIcon));
        }
      }
      arrayOfCCModel[6].render(paramDouble1, paramDouble2, paramDouble3, RenderUtils.getIconTransformation(localIIcon));
    }
    else
    {
      int i = 0;
      for (j = 0; j < 6; j++) {
        if ((BlockDuct.ConnectionTypes.values()[paramArrayOfInt[j]].renderDuct()) && (paramArrayOfInt[j] != BlockDuct.ConnectionTypes.STRUCTURE.ordinal())) {
          i |= 1 << j;
        }
      }
      modelFluidTubes[i].render(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D, RenderUtils.getIconTransformation(localIIcon));
    }
    CCRenderState.draw();
  }
  
  public void getDuctConnections(TileTDBase paramTileTDBase)
  {
    for (int i = 0; i < 6; i++) {
      connections[i] = paramTileTDBase.getRenderConnectionType(i).ordinal();
    }
  }
  
  public boolean renderWorldBlock(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, Block paramBlock, int paramInt4, RenderBlocks paramRenderBlocks)
  {
    TileEntity localTileEntity = paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    if (!(localTileEntity instanceof TileTDBase)) {
      return false;
    }
    TileTDBase localTileTDBase = (TileTDBase)localTileEntity;
    
    RenderUtils.preWorldRender(paramIBlockAccess, paramInt1, paramInt2, paramInt3);
    getDuctConnections(localTileTDBase);
    
    boolean bool = false;
    Object localObject2;
    for (localObject2 : attachments) {
      if (localObject2 != null) {
        bool = (((Attachment)localObject2).render(BlockCoFHBase.renderPass, paramRenderBlocks)) || (bool);
      }
    }
    for (localObject2 : covers) {
      if (localObject2 != null) {
        bool = (((Cover)localObject2).render(BlockCoFHBase.renderPass, paramRenderBlocks)) || (bool);
      }
    }
    int i = getDuctoffset + paramIBlockAccess.getBlockMetadata(paramInt1, paramInt2, paramInt3)).id;
    if (BlockCoFHBase.renderPass == 0)
    {
      renderBase(false, i, connections, paramInt1, paramInt2, paramInt3, localTileTDBase.getBaseIcon());
      bool = true;
    }
    else
    {
      bool = (renderWorldExtra(false, localTileTDBase, i, connections, paramInt1, paramInt2, paramInt3)) || (bool);
    }
    bool = (localTileTDBase.renderAdditional(i, connections, BlockCoFHBase.renderPass)) || (bool);
    
    return bool;
  }
  
  public boolean shouldRender3DInInventory(int paramInt)
  {
    return true;
  }
  
  public int getRenderId()
  {
    return TDProps.renderDuctId;
  }
  
  public boolean handleRenderType(ItemStack paramItemStack, IItemRenderer.ItemRenderType paramItemRenderType)
  {
    return true;
  }
  
  public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, IItemRenderer.ItemRendererHelper paramItemRendererHelper)
  {
    return true;
  }
  
  public void renderItem(IItemRenderer.ItemRenderType paramItemRenderType, ItemStack paramItemStack, Object... paramVarArgs)
  {
    Block localBlock = Block.getBlockFromItem(paramItemStack.getItem());
    
    Duct localDuct = TDDucts.getDuct(offset + paramItemStack.getItemDamage());
    int i = id;
    
    GL11.glPushMatrix();
    double d = -0.5D;
    if ((paramItemRenderType == IItemRenderer.ItemRenderType.EQUIPPED) || (paramItemRenderType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) {
      d = 0.0D;
    }
    cofh.lib.render.RenderHelper.setBlockTextureSheet();
    RenderUtils.preItemRender();
    
    cofh.lib.render.RenderHelper.enableGUIStandardItemLighting();
    
    CCRenderState.startDrawing();
    renderBase(true, i, INV_CONNECTIONS, d, d, d, localDuct.getBaseTexture(paramItemStack));
    CCRenderState.draw();
    
    CCRenderState.startDrawing();
    renderWorldExtra(true, null, i, INV_CONNECTIONS, d, d - 9.765625E-4D, d);
    CCRenderState.draw();
    
    CCRenderState.useNormals = false;
    cofh.lib.render.RenderHelper.setItemTextureSheet();
    
    RenderUtils.postItemRender();
    
    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    GL11.glPopMatrix();
  }
  
  public void renderInventoryBlock(Block paramBlock, int paramInt1, int paramInt2, RenderBlocks paramRenderBlocks) {}
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\RenderDuct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */