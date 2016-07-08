package cofh.thermaldynamics.multiblock;

import java.util.HashMap;
import java.util.LinkedList;
import net.minecraft.world.World;

public abstract class MultiBlockGridWithRoutes
  extends MultiBlockGrid
{
  public MultiBlockGridWithRoutes(World paramWorld)
  {
    super(paramWorld);
  }
  
  public void doTickProcessing(long paramLong)
  {
    for (int i = 0; !calculatingRoutes.isEmpty(); i++)
    {
      RouteCache localRouteCache = (RouteCache)calculatingRoutes.peek();
      if ((localRouteCache != null) && (!localRouteCache.processStep())) {
        calculatingRoutes.remove(localRouteCache);
      }
      if (i == 15)
      {
        if (System.nanoTime() > paramLong) {
          return;
        }
        i = 0;
      }
    }
  }
  
  public boolean isTickProcessing()
  {
    return !calculatingRoutes.isEmpty();
  }
  
  public HashMap<IMultiBlockRoute, RouteCache> routeCacheMap = new HashMap();
  public final LinkedList<RouteCache> calculatingRoutes = new LinkedList();
  
  public void onMinorGridChange()
  {
    super.onMinorGridChange();
    onMajorGridChange();
  }
  
  public void onMajorGridChange()
  {
    super.onMajorGridChange();
    if (!routeCacheMap.isEmpty())
    {
      for (RouteCache localRouteCache : routeCacheMap.values()) {
        localRouteCache.invalidate();
      }
      routeCacheMap.clear();
    }
    if (!calculatingRoutes.isEmpty()) {
      calculatingRoutes.clear();
    }
  }
  
  public RouteCache getRoutesFromOutputNonUrgent(IMultiBlockRoute paramIMultiBlockRoute)
  {
    RouteCache localRouteCache = (RouteCache)routeCacheMap.get(paramIMultiBlockRoute);
    if (localRouteCache != null) {
      return localRouteCache;
    }
    localRouteCache = new RouteCache(paramIMultiBlockRoute);
    calculatingRoutes.add(localRouteCache);
    
    routeCacheMap.put(paramIMultiBlockRoute, localRouteCache);
    return localRouteCache;
  }
  
  public RouteCache getRoutesFromOutputRange(IMultiBlockRoute paramIMultiBlockRoute, int paramInt)
  {
    RouteCache localRouteCache = (RouteCache)routeCacheMap.get(paramIMultiBlockRoute);
    if (localRouteCache == null)
    {
      localRouteCache = new RouteCache(paramIMultiBlockRoute, paramInt);
      localRouteCache.generateCache();
      routeCacheMap.put(paramIMultiBlockRoute, localRouteCache);
    }
    else if (maxPathLength < paramInt)
    {
      maxPathLength = paramInt;
      localRouteCache.generateCache();
    }
    return localRouteCache;
  }
  
  public RouteCache getRoutesFromOutput(IMultiBlockRoute paramIMultiBlockRoute)
  {
    RouteCache localRouteCache = (RouteCache)routeCacheMap.get(paramIMultiBlockRoute);
    if (localRouteCache == null)
    {
      localRouteCache = new RouteCache(paramIMultiBlockRoute);
      localRouteCache.generateCache();
      routeCacheMap.put(paramIMultiBlockRoute, localRouteCache);
    }
    else if ((!localRouteCache.isFinishedGenerating()) || (maxPathLength < Integer.MAX_VALUE))
    {
      maxPathLength = Integer.MAX_VALUE;
      localRouteCache.generateCache();
    }
    return localRouteCache;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\MultiBlockGridWithRoutes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */