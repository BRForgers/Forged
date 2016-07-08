package cofh.thermaldynamics.render;

import cofh.core.render.RenderUtils;
import cofh.lib.render.RenderHelper;
import cofh.repack.codechicken.lib.render.CCModel;
import cofh.repack.codechicken.lib.render.CCRenderState;
import cofh.repack.codechicken.lib.render.CCRenderState.IVertexOperation;
import cofh.repack.codechicken.lib.vec.Translation;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.duct.BlockDuct.ConnectionTypes;
import cofh.thermaldynamics.duct.item.TileItemDuct;
import cofh.thermaldynamics.duct.item.TravelingItem;
import com.google.common.collect.Iterators;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderDuctItems
  extends TileEntitySpecialRenderer
{
  public static final int ITEMS_TO_RENDER_PER_DUCT = 16;
  static RenderItem travelingItemRender;
  static EntityItem travelingEntityItem = new EntityItem(null);
  static float travelingItemSpin = 0.25F;
  public static final RenderDuctItems instance = new RenderDuctItems();
  static final float ITEM_RENDER_SCALE = 0.6F;
  
  static
  {
    travelingItemRender = new RenderItem()
    {
      public boolean shouldBob()
      {
        return false;
      }
      
      public boolean shouldSpreadItems()
      {
        return false;
      }
    };
    travelingItemRender.setRenderManager(RenderManager.instance);
    travelingEntityItemhoverStart = 0.0F;
    
    FMLCommonHandler.instance().bus().register(instance);
  }
  
  public static float spinStep = 0.026175F;
  
  @SubscribeEvent
  public void clientTick(TickEvent.ClientTickEvent paramClientTickEvent)
  {
    travelingItemSpin += spinStep;
    travelingItemSpin %= 180.0F;
  }
  
  public void renderTileEntityAt(TileEntity paramTileEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat)
  {
    TileItemDuct localTileItemDuct = (TileItemDuct)paramTileEntity;
    renderItemDuct(localTileItemDuct, paramDouble1, paramDouble2, paramDouble3, paramFloat);
  }
  
  public void renderItemDuct(TileItemDuct paramTileItemDuct, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat)
  {
    if ((!myItems.isEmpty()) || (!itemsToAdd.isEmpty()))
    {
      RenderUtils.preWorldRender(paramTileItemDuct.func_145831_w(), xCoord, yCoord, zCoord);
      CCRenderState.useNormals = true;
      
      renderTravelingItems(Iterators.concat(itemsToAdd.iterator(), myItems.iterator()), paramTileItemDuct, paramTileItemDuct.func_145831_w(), paramDouble1, paramDouble2, paramDouble3, paramFloat);
      CCRenderState.useNormals = false;
      CCRenderState.reset();
    }
    if (centerLine > 0)
    {
      GL11.glPushMatrix();
      
      Translation localTranslation = new Vector3(paramDouble1, paramDouble2, paramDouble3).translation();
      
      CCRenderState.reset();
      GL11.glEnable(3008);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      CCRenderState.useNormals = true;
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      
      GL11.glEnable(3042);
      GL11.glEnable(2884);
      GL11.glBlendFunc(770, 1);
      RenderHelper.bindTexture(RenderHelper.MC_BLOCK_SHEET);
      RenderUtils.preWorldRender(paramTileItemDuct.func_145831_w(), xCoord, yCoord, zCoord);
      CCRenderState.setColour(-1);
      CCRenderState.setBrightness(15728880);
      
      RenderDuct.instance.getDuctConnections(paramTileItemDuct);
      CCRenderState.startDrawing();
      for (int i = 0; i < 6; i++) {
        if ((BlockDuct.ConnectionTypes.values()[RenderDuct.connections[i]].renderDuct()) && (centerLineSub[i] != 0))
        {
          CCRenderState.alphaOverride = getAlphaLevel(centerLineSub[i], paramFloat);
          RenderDuct.modelLine[i].render(new CCRenderState.IVertexOperation[] { localTranslation, RenderUtils.getIconTransformation(RenderDuct.textureCenterLine) });
        }
        else
        {
          CCRenderState.alphaOverride = getAlphaLevel(centerLine, paramFloat);
          RenderDuct.modelLineCenter.render(i * 4, i * 4 + 4, new CCRenderState.IVertexOperation[] { localTranslation, RenderUtils.getIconTransformation(RenderDuct.textureCenterLine) });
        }
      }
      CCRenderState.draw();
      CCRenderState.alphaOverride = -1;
      CCRenderState.reset();
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3042);
      GL11.glEnable(2896);
      
      CCRenderState.useNormals = false;
      GL11.glPopMatrix();
    }
  }
  
  public static int getAlphaLevel(int paramInt, float paramFloat)
  {
    return (int)Math.min(80.0D, 0.7D * ((paramInt - paramFloat) * 255.0D) / 10.0D);
  }
  
  public void renderTravelingItems(Iterator<TravelingItem> paramIterator, TileItemDuct paramTileItemDuct, World paramWorld, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat)
  {
    if (!paramIterator.hasNext()) {
      return;
    }
    travelingEntityItemhoverStart = (travelingItemSpin + paramFloat * spinStep);
    
    GL11.glPushMatrix();
    
    GL11.glTranslated(paramDouble1 + 0.5D, paramDouble2 + 0.5D, paramDouble3 + 0.5D);
    for (int i = 0; (paramIterator.hasNext()) && (i < 16); i++)
    {
      TravelingItem localTravelingItem = (TravelingItem)paramIterator.next();
      if ((localTravelingItem != null) && (stack != null))
      {
        double d = (progress + paramFloat * step) / paramTileItemDuct.getPipeLength();
        
        d -= 0.5D;
        if ((!shouldDie) || (d <= 0.0D))
        {
          GL11.glPushMatrix();
          if (d < 0.0D) {
            translateItem(oldDirection, d);
          } else {
            translateItem(direction, d);
          }
          GL11.glScalef(0.6F, 0.6F, 0.6F);
          
          travelingEntityItem.setEntityItemStack(stack);
          travelingItemRender.doRender(travelingEntityItem, 0.0D, -0.10000000149011612D, 0.0D, 0.0F, 0.0F);
          
          GL11.glPopMatrix();
        }
      }
    }
    GL11.glPopMatrix();
  }
  
  private void translateItem(byte paramByte, double paramDouble)
  {
    GL11.glTranslated(net.minecraft.util.Facing.offsetsXForSide[paramByte] * paramDouble, net.minecraft.util.Facing.offsetsYForSide[paramByte] * paramDouble, net.minecraft.util.Facing.offsetsZForSide[paramByte] * paramDouble);
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\RenderDuctItems.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */