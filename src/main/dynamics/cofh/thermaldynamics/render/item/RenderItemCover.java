package cofh.thermaldynamics.render.item;

import cofh.core.render.RenderUtils;
import cofh.repack.codechicken.lib.render.CCRenderState;
import cofh.thermaldynamics.duct.attachments.cover.CoverHelper;
import cofh.thermaldynamics.duct.attachments.cover.CoverRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderItemCover
  implements IItemRenderer
{
  public static IItemRenderer instance = new RenderItemCover();
  
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
    NBTTagCompound localNBTTagCompound = paramItemStack.getTagCompound();
    if ((localNBTTagCompound == null) || (!localNBTTagCompound.hasKey("Meta", 1)) || (!localNBTTagCompound.hasKey("Block", 8))) {
      return;
    }
    int i = localNBTTagCompound.getByte("Meta");
    Block localBlock = Block.getBlockFromName(localNBTTagCompound.getString("Block"));
    if ((localBlock == Blocks.air) || (i < 0) || (i >= 16) || (!CoverHelper.isValid(localBlock, i)))
    {
      localNBTTagCompound.removeTag("Meta");
      localNBTTagCompound.removeTag("Block");
      if (localNBTTagCompound.hasNoTags()) {
        paramItemStack.setTagCompound(null);
      }
    }
    GL11.glPushMatrix();
    double d = -0.5D;
    if ((paramItemRenderType == IItemRenderer.ItemRenderType.EQUIPPED) || (paramItemRenderType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) {
      d = 0.0D;
    } else if (paramItemRenderType == IItemRenderer.ItemRenderType.ENTITY) {
      GL11.glScaled(0.5D, 0.5D, 0.5D);
    }
    cofh.lib.render.RenderHelper.setBlockTextureSheet();
    RenderUtils.preItemRender();
    
    cofh.lib.render.RenderHelper.enableGUIStandardItemLighting();
    
    CCRenderState.startDrawing();
    GL11.glTranslated(d, d - 128.0D, d);
    
    instanceblock = localBlock;
    instancemeta = i;
    renderBlocksblockAccess = SingleBlockAccess.instance;
    Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
    
    ForgeDirection localForgeDirection = paramItemRenderType == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON ? ForgeDirection.WEST : ForgeDirection.SOUTH;
    GL11.glTranslated(-offsetX * 0.5D, -offsetY * 0.5D, -offsetZ * 0.5D);
    for (int j = 0; j < 2; j++) {
      if (localBlock.canRenderInPass(j)) {
        CoverRenderer.renderCover(CoverRenderer.renderBlocks, 0, 128, 0, localForgeDirection.ordinal(), localBlock, i, cofh.thermaldynamics.duct.attachments.cover.Cover.bounds[localForgeDirection.ordinal()], true, false, null);
      }
    }
    CCRenderState.draw();
    CCRenderState.useNormals = false;
    
    cofh.lib.render.RenderHelper.setItemTextureSheet();
    RenderUtils.postItemRender();
    
    net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    
    GL11.glPopMatrix();
  }
  
  public static class SingleBlockAccess
    implements IBlockAccess
  {
    public static SingleBlockAccess instance = new SingleBlockAccess();
    public Block block;
    public int meta;
    
    public boolean isLoc(int paramInt1, int paramInt2, int paramInt3)
    {
      return (paramInt1 == 0) && (paramInt2 == 128) && (paramInt3 == 0);
    }
    
    public SingleBlockAccess() {}
    
    public SingleBlockAccess(Block paramBlock, int paramInt)
    {
      block = paramBlock;
      meta = paramInt;
    }
    
    public Block getBlock(int paramInt1, int paramInt2, int paramInt3)
    {
      return isLoc(paramInt1, paramInt2, paramInt3) ? block : Blocks.air;
    }
    
    public TileEntity getTileEntity(int paramInt1, int paramInt2, int paramInt3)
    {
      return null;
    }
    
    public int getLightBrightnessForSkyBlocks(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      return 15728880;
    }
    
    public int getBlockMetadata(int paramInt1, int paramInt2, int paramInt3)
    {
      return isLoc(paramInt1, paramInt2, paramInt3) ? meta : 0;
    }
    
    public int isBlockProvidingPowerTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      return 0;
    }
    
    public boolean isAirBlock(int paramInt1, int paramInt2, int paramInt3)
    {
      return isLoc(paramInt1, paramInt2, paramInt3);
    }
    
    public BiomeGenBase getBiomeGenForCoords(int paramInt1, int paramInt2)
    {
      return BiomeGenBase.plains;
    }
    
    public int getHeight()
    {
      return 140;
    }
    
    public boolean extendedLevelsInChunkCache()
    {
      return false;
    }
    
    public boolean isSideSolid(int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, boolean paramBoolean)
    {
      return (isLoc(paramInt1, paramInt2, paramInt3)) && (block.isSideSolid(this, paramInt1, paramInt2, paramInt3, paramForgeDirection));
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\item\RenderItemCover.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */