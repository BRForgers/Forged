package cofh.thermaldynamics.core;

import cofh.thermaldynamics.duct.item.TileItemDuct;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class TickHandlerClient
{
  public static final TickHandlerClient instance = new TickHandlerClient();
  public static HashSet<TileItemDuct> tickBlocks = new HashSet();
  public static HashSet<TileItemDuct> tickBlocksToAdd = new HashSet();
  public static HashSet<TileItemDuct> tickBlocksToRemove = new HashSet();
  boolean needsMenu = false;
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void tick(TickEvent.ClientTickEvent paramClientTickEvent)
  {
    Minecraft localMinecraft = Minecraft.getMinecraft();
    if (phase == TickEvent.Phase.END)
    {
      if ((currentScreen instanceof GuiMainMenu))
      {
        if (needsMenu)
        {
          onMainMenu();
          needsMenu = false;
        }
      }
      else if (inGameHasFocus) {
        needsMenu = true;
      }
      if (!tickBlocksToAdd.isEmpty())
      {
        tickBlocks.addAll(tickBlocksToAdd);
        tickBlocksToAdd.clear();
      }
      if ((!localMinecraft.isGamePaused()) && (!tickBlocks.isEmpty()))
      {
        for (Iterator localIterator = tickBlocks.iterator(); localIterator.hasNext();)
        {
          TileItemDuct localTileItemDuct = (TileItemDuct)localIterator.next();
          if (localTileItemDuct.func_145837_r()) {
            localIterator.remove();
          } else {
            localTileItemDuct.tickItemsClient();
          }
        }
        tickBlocks.removeAll(tickBlocksToRemove);
        tickBlocksToRemove.clear();
      }
    }
  }
  
  public void onMainMenu()
  {
    synchronized (TickHandler.handlers)
    {
      TickHandler.handlers.clear();
    }
    tickBlocks.clear();
    tickBlocksToAdd.clear();
    tickBlocksToRemove.clear();
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\core\TickHandlerClient.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */