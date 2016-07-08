package cofh.thermaldynamics.core;

import cofh.lib.util.helpers.ServerHelper;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class TickHandler
{
  public static final TickHandler instance = new TickHandler();
  public static final WeakHashMap<World, WorldGridList> handlers = new WeakHashMap();
  public static final LinkedHashSet<WeakReference<IMultiBlock>> multiBlocksToCalculate = new LinkedHashSet();
  
  public static void addMultiBlockToCalculate(IMultiBlock paramIMultiBlock)
  {
    if (paramIMultiBlock.world() != null)
    {
      if (ServerHelper.isServerWorld(paramIMultiBlock.world())) {
        getTickHandlerworldtickingBlocks.add(paramIMultiBlock);
      }
    }
    else {
      synchronized (multiBlocksToCalculate)
      {
        multiBlocksToCalculate.add(new WeakReference(paramIMultiBlock));
      }
    }
  }
  
  public static WorldGridList getTickHandler(World paramWorld)
  {
    if (ServerHelper.isClientWorld(paramWorld)) {
      throw new IllegalStateException("World Grid called client-side");
    }
    synchronized (handlers)
    {
      WorldGridList localWorldGridList = (WorldGridList)handlers.get(paramWorld);
      if (localWorldGridList != null) {
        return localWorldGridList;
      }
      localWorldGridList = new WorldGridList(paramWorld);
      handlers.put(paramWorld, localWorldGridList);
      return localWorldGridList;
    }
  }
  
  @SubscribeEvent
  public void onServerTick(TickEvent.ServerTickEvent paramServerTickEvent)
  {
    if (phase != TickEvent.Phase.END) {
      return;
    }
    synchronized (multiBlocksToCalculate)
    {
      if (!multiBlocksToCalculate.isEmpty())
      {
        Iterator localIterator = multiBlocksToCalculate.iterator();
        while (localIterator.hasNext())
        {
          IMultiBlock localIMultiBlock = (IMultiBlock)((WeakReference)localIterator.next()).get();
          if (localIMultiBlock == null)
          {
            localIterator.remove();
          }
          else if (localIMultiBlock.world() != null)
          {
            if (ServerHelper.isServerWorld(localIMultiBlock.world())) {
              getTickHandlerworldtickingBlocks.add(localIMultiBlock);
            }
            localIterator.remove();
          }
        }
      }
    }
  }
  
  @SubscribeEvent
  public void tick(TickEvent.WorldTickEvent paramWorldTickEvent)
  {
    synchronized (handlers)
    {
      WorldGridList localWorldGridList = (WorldGridList)handlers.get(world);
      if (localWorldGridList == null) {
        return;
      }
      if (phase == TickEvent.Phase.START) {
        localWorldGridList.tickStart();
      } else {
        localWorldGridList.tickEnd();
      }
    }
  }
  
  @SubscribeEvent
  public void worldUnload(WorldEvent.Unload paramUnload)
  {
    World localWorld = world;
    if (isRemote) {
      return;
    }
    synchronized (handlers)
    {
      handlers.remove(localWorld);
      handlers.isEmpty();
    }
    synchronized (multiBlocksToCalculate)
    {
      if (!multiBlocksToCalculate.isEmpty())
      {
        Iterator localIterator = multiBlocksToCalculate.iterator();
        while (localIterator.hasNext())
        {
          IMultiBlock localIMultiBlock = (IMultiBlock)((WeakReference)localIterator.next()).get();
          if ((localIMultiBlock == null) || (localIMultiBlock.world() == localWorld)) {
            localIterator.remove();
          }
        }
      }
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\core\TickHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */