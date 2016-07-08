package cofh.thermaldynamics.core;

import cofh.thermaldynamics.debughelper.NoComodSet;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import net.minecraft.world.World;

public class WorldGridList
{
  public World worldObj;
  
  public WorldGridList(World paramWorld)
  {
    worldObj = paramWorld;
  }
  
  public LinkedHashSet<MultiBlockGrid> tickingGrids = new LinkedHashSet();
  public LinkedHashSet<IMultiBlock> tickingBlocks = new LinkedHashSet();
  public LinkedHashSet<MultiBlockGrid> gridsToRecreate = new LinkedHashSet();
  public LinkedHashSet<MultiBlockGrid> newGrids = new LinkedHashSet();
  public LinkedHashSet<MultiBlockGrid> oldGrids = new LinkedHashSet();
  
  public void tickStart()
  {
    if (!newGrids.isEmpty())
    {
      tickingGrids.addAll(newGrids);
      newGrids.clear();
    }
    if (!oldGrids.isEmpty())
    {
      tickingGrids.removeAll(oldGrids);
      oldGrids.clear();
    }
  }
  
  public void tickEnd()
  {
    Object localObject3;
    if (!gridsToRecreate.isEmpty())
    {
      tickingGrids.removeAll(gridsToRecreate);
      for (localObject1 = gridsToRecreate.iterator(); ((Iterator)localObject1).hasNext();)
      {
        localObject2 = (MultiBlockGrid)((Iterator)localObject1).next();
        for (localObject3 = idleSet.iterator(); ((Iterator)localObject3).hasNext();)
        {
          localIMultiBlock = (IMultiBlock)((Iterator)localObject3).next();
          tickingBlocks.add(localIMultiBlock);
          ((MultiBlockGrid)localObject2).destroyNode(localIMultiBlock);
        }
        for (localObject3 = nodeSet.iterator(); ((Iterator)localObject3).hasNext();)
        {
          localIMultiBlock = (IMultiBlock)((Iterator)localObject3).next();
          tickingBlocks.add(localIMultiBlock);
          ((MultiBlockGrid)localObject2).destroyNode(localIMultiBlock);
        }
      }
      IMultiBlock localIMultiBlock;
      gridsToRecreate.clear();
    }
    Object localObject1 = new ArrayList();
    for (Object localObject2 = tickingGrids.iterator(); ((Iterator)localObject2).hasNext();)
    {
      localObject3 = (MultiBlockGrid)((Iterator)localObject2).next();
      ((MultiBlockGrid)localObject3).tickGrid();
      if (((MultiBlockGrid)localObject3).isTickProcessing()) {
        ((ArrayList)localObject1).add(localObject3);
      }
    }
    if (!((ArrayList)localObject1).isEmpty())
    {
      long l = System.nanoTime() + 100000L;
      int i = 0;int j = ((ArrayList)localObject1).size();
      for (int k = 0; i < j; i++)
      {
        ((MultiBlockGrid)((ArrayList)localObject1).get(i)).doTickProcessing(l);
        if (k++ == 7)
        {
          if (System.nanoTime() > l) {
            break;
          }
          k = 0;
        }
      }
    }
    if (!tickingBlocks.isEmpty())
    {
      Iterator localIterator = tickingBlocks.iterator();
      while (localIterator.hasNext())
      {
        localObject3 = (IMultiBlock)localIterator.next();
        if (((IMultiBlock)localObject3).existsYet())
        {
          ((IMultiBlock)localObject3).tickMultiBlock();
          localIterator.remove();
        }
      }
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\core\WorldGridList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */