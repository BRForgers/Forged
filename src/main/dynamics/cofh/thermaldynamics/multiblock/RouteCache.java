package cofh.thermaldynamics.multiblock;

import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import gnu.trove.list.linked.TByteLinkedList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import net.minecraftforge.common.util.ForgeDirection;

public class RouteCache
{
  public IMultiBlockRoute origin;
  public LinkedList<Route> outputRoutes;
  public LinkedList<Route> stuffableRoutes;
  public HashSet<IMultiBlockRoute> visited;
  public HashSet<IMultiBlockRoute> outputvisited;
  private LinkedList<Route> validRoutes;
  public int maxPathLength;
  private boolean isFinishedGenerating;
  public boolean invalid = false;
  
  public RouteCache(IMultiBlockRoute paramIMultiBlockRoute)
  {
    this(paramIMultiBlockRoute, paramIMultiBlockRoute.getMaxRange());
  }
  
  public RouteCache(IMultiBlockRoute paramIMultiBlockRoute, int paramInt)
  {
    origin = paramIMultiBlockRoute;
    maxPathLength = paramInt;
    init();
  }
  
  public void init()
  {
    outputRoutes = new LinkedList();
    if (origin.isOutput())
    {
      Route localRoute = new Route(origin);
      
      routeFinished = true;
      outputRoutes.add(localRoute);
    }
    stuffableRoutes = new LinkedList();
    validRoutes = new LinkedList();
    validRoutes.add(new Route(origin));
    visited = new HashSet();
    visited.add(origin);
    outputvisited = new HashSet();
    if (origin.isOutput()) {
      outputvisited.add(origin);
    }
  }
  
  public synchronized void generateCache()
  {
    while (processStep()) {}
  }
  
  public boolean processStep()
  {
    if ((isFinishedGenerating) || (invalid)) {
      return false;
    }
    boolean bool = false;
    
    LinkedList localLinkedList = new LinkedList();
    for (Route localRoute : validRoutes)
    {
      moveForwards(localRoute, localLinkedList);
      if (!routeFinished) {
        bool = true;
      }
    }
    validRoutes.addAll(localLinkedList);
    if (!bool) {
      finished();
    }
    return bool;
  }
  
  private void finished()
  {
    visited.clear();
    outputvisited.clear();
    validRoutes.clear();
    isFinishedGenerating = true;
    Collections.sort(outputRoutes);
  }
  
  public void moveForwards(Route paramRoute, LinkedList<Route> paramLinkedList)
  {
    int i = 0;
    Object localObject = null;
    if (routeFinished) {
      return;
    }
    if (pathDirections.size() > maxPathLength)
    {
      routeFinished = true;
      return;
    }
    byte b = -1;
    int k;
    for (int j = 0; j < ForgeDirection.VALID_DIRECTIONS.length; k = (byte)(j + 1)) {
      if ((endPoint.getCachedSideType(j) == TileTDBase.NeighborTypes.MULTIBLOCK) && (endPoint.getConnectionType(j).allowTransfer))
      {
        IMultiBlockRoute localIMultiBlockRoute = (IMultiBlockRoute)endPoint.getCachedTile(j);
        if (localIMultiBlockRoute != null)
        {
          if (!visited.contains(localIMultiBlockRoute))
          {
            visited.add(localIMultiBlockRoute);
            
            localIMultiBlockRoute.onNeighborBlockChange();
            if (localIMultiBlockRoute.canStuffItem()) {
              stuffableRoutes.add(new Route(paramRoute, localIMultiBlockRoute, j, true));
            }
            if (i == 0)
            {
              localObject = localIMultiBlockRoute;
              b = j;
              i = 1;
            }
            else
            {
              paramLinkedList.add(new Route(paramRoute, localIMultiBlockRoute, j, false));
            }
          }
          if ((localIMultiBlockRoute.isOutput()) && (!outputvisited.contains(localIMultiBlockRoute)))
          {
            outputRoutes.add(new Route(paramRoute, localIMultiBlockRoute, j, true));
            outputvisited.add(localIMultiBlockRoute);
          }
        }
      }
    }
    if (i == 0)
    {
      routeFinished = true;
    }
    else
    {
      pathDirections.add(b);
      pathWeight += ((IMultiBlockRoute)localObject).getWeight();
      endPoint = ((IMultiBlockRoute)localObject);
    }
  }
  
  public synchronized boolean isFinishedGenerating()
  {
    return isFinishedGenerating;
  }
  
  public void reset()
  {
    isFinishedGenerating = false;
    init();
  }
  
  public void invalidate()
  {
    invalid = true;
    outputRoutes.clear();
    stuffableRoutes.clear();
    origin = null;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\RouteCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */