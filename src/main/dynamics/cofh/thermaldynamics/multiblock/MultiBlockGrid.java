package cofh.thermaldynamics.multiblock;

import cofh.core.chat.ChatHelper;
import cofh.thermaldynamics.block.Attachment;
import cofh.thermaldynamics.core.TickHandler;
import cofh.thermaldynamics.core.WorldGridList;
import cofh.thermaldynamics.debughelper.NoComodSet;
import cofh.thermaldynamics.duct.attachments.relay.Relay;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class MultiBlockGrid
{
  public NoComodSet<IMultiBlock> nodeSet = new NoComodSet();
  public NoComodSet<IMultiBlock> idleSet = new NoComodSet();
  public WorldGridList worldGrid;
  public boolean signalsUpToDate;
  public RedstoneControl rs;
  
  public MultiBlockGrid(WorldGridList paramWorldGridList)
  {
    worldGrid = paramWorldGridList;
    newGrids.add(this);
  }
  
  public MultiBlockGrid(World paramWorld)
  {
    this(TickHandler.getTickHandler(paramWorld));
  }
  
  public void addIdle(IMultiBlock paramIMultiBlock)
  {
    idleSet.add(paramIMultiBlock);
    if (nodeSet.contains(paramIMultiBlock))
    {
      nodeSet.remove(paramIMultiBlock);
      onMajorGridChange();
    }
    else
    {
      int i = 0;
      int k;
      for (int j = 0; j < ForgeDirection.VALID_DIRECTIONS.length; k = (byte)(j + 1)) {
        if (paramIMultiBlock.isSideConnected(j))
        {
          if (i != 0)
          {
            onMajorGridChange();
            break;
          }
          i = 1;
        }
      }
    }
    balanceGrid();
  }
  
  public void addNode(IMultiBlock paramIMultiBlock)
  {
    nodeSet.add(paramIMultiBlock);
    if (idleSet.contains(paramIMultiBlock)) {
      idleSet.remove(paramIMultiBlock);
    }
    onMajorGridChange();
    balanceGrid();
  }
  
  public void mergeGrids(MultiBlockGrid paramMultiBlockGrid)
  {
    Iterator localIterator;
    IMultiBlock localIMultiBlock;
    if (!nodeSet.isEmpty())
    {
      for (localIterator = nodeSet.iterator(); localIterator.hasNext();)
      {
        localIMultiBlock = (IMultiBlock)localIterator.next();
        localIMultiBlock.setGrid(this);
        addBlock(localIMultiBlock);
      }
      onMajorGridChange();
    }
    if (!idleSet.isEmpty())
    {
      for (localIterator = idleSet.iterator(); localIterator.hasNext();)
      {
        localIMultiBlock = (IMultiBlock)localIterator.next();
        localIMultiBlock.setGrid(this);
        addBlock(localIMultiBlock);
      }
      onMajorGridChange();
    }
    onMinorGridChange();
    paramMultiBlockGrid.destroy();
  }
  
  public void destroy()
  {
    nodeSet.clear();
    idleSet.clear();
    
    worldGrid.oldGrids.add(this);
  }
  
  public boolean canGridsMerge(MultiBlockGrid paramMultiBlockGrid)
  {
    return paramMultiBlockGrid.getClass() == getClass();
  }
  
  public void resetMultiBlocks()
  {
    for (Iterator localIterator = nodeSet.iterator(); localIterator.hasNext();)
    {
      localIMultiBlock = (IMultiBlock)localIterator.next();
      localIMultiBlock.setValidForForming();
    }
    IMultiBlock localIMultiBlock;
    for (localIterator = idleSet.iterator(); localIterator.hasNext();)
    {
      localIMultiBlock = (IMultiBlock)localIterator.next();
      localIMultiBlock.setValidForForming();
    }
  }
  
  public void tickGrid()
  {
    Object localObject1;
    if ((rs != null) && (rs.nextRedstoneLevel != Byte.MIN_VALUE))
    {
      rs.redstoneLevel = rs.nextRedstoneLevel;
      rs.nextRedstoneLevel = Byte.MIN_VALUE;
      
      localObject1 = rs.relaysOut;
      if (localObject1 != null) {
        for (localObject2 = ((ArrayList)localObject1).iterator(); ((Iterator)localObject2).hasNext();)
        {
          localObject3 = (Attachment)((Iterator)localObject2).next();
          ((Attachment)localObject3).checkSignal();
        }
      }
    }
    Object localObject3;
    if (signalsUpToDate) {
      return;
    }
    signalsUpToDate = true;
    if ((rs == null) || (rs.relaysIn == null))
    {
      if (rs != null) {
        rs.relaysOut = null;
      }
      for (localObject1 = nodeSet.iterator(); ((Iterator)localObject1).hasNext();)
      {
        localObject2 = (IMultiBlock)((Iterator)localObject1).next();
        ((IMultiBlock)localObject2).addRelays();
      }
    }
    if (rs == null) {
      return;
    }
    if (rs.relaysIn == null)
    {
      if (rs.relaysOut == null)
      {
        rs = null;
        return;
      }
      rs.nextRedstoneLevel = 0;
      
      return;
    }
    int i = 0;
    for (Object localObject2 = rs.relaysIn.iterator(); ((Iterator)localObject2).hasNext();)
    {
      localObject3 = (Relay)((Iterator)localObject2).next();
      i = Math.max(i, ((Relay)localObject3).getPowerLevel());
      if (i == 15) {
        break;
      }
    }
    rs.nextRedstoneLevel = ((byte)i);
  }
  
  public void balanceGrid() {}
  
  public void addBlock(IMultiBlock paramIMultiBlock)
  {
    if (paramIMultiBlock.isNode()) {
      addNode(paramIMultiBlock);
    } else {
      addIdle(paramIMultiBlock);
    }
  }
  
  public void destroyAndRecreate()
  {
    worldGrid.gridsToRecreate.add(this);
  }
  
  public void removeBlock(IMultiBlock paramIMultiBlock)
  {
    destroyNode(paramIMultiBlock);
    if (paramIMultiBlock.isNode())
    {
      nodeSet.remove(paramIMultiBlock);
      onMajorGridChange();
    }
    else
    {
      idleSet.remove(paramIMultiBlock);
    }
    if ((nodeSet.isEmpty()) && (idleSet.isEmpty()))
    {
      worldGrid.oldGrids.add(this);
      return;
    }
    int i = 0;
    for (byte b = 0; b < 6; b = (byte)(b + 1)) {
      if (paramIMultiBlock.isSideConnected(b)) {
        i = (byte)(i + 1);
      }
    }
    if (i <= 1)
    {
      balanceGrid();
      onMinorGridChange();
      return;
    }
    onMajorGridChange();
    
    worldGrid.gridsToRecreate.add(this);
  }
  
  public void onMinorGridChange()
  {
    resetRelays();
  }
  
  public void onMajorGridChange()
  {
    resetRelays();
  }
  
  public int size()
  {
    return nodeSet.size() + idleSet.size();
  }
  
  public void doTickProcessing(long paramLong) {}
  
  public boolean isTickProcessing()
  {
    return false;
  }
  
  public void destroyNode(IMultiBlock paramIMultiBlock)
  {
    paramIMultiBlock.setGrid(null);
  }
  
  public boolean isFirstMultiblock(IMultiBlock paramIMultiBlock)
  {
    return nodeSet.iterator().next() == paramIMultiBlock;
  }
  
  public abstract boolean canAddBlock(IMultiBlock paramIMultiBlock);
  
  public void addInfo(List<IChatComponent> paramList, EntityPlayer paramEntityPlayer, boolean paramBoolean)
  {
    if (paramBoolean) {
      addInfo(paramList, "size", Integer.valueOf(size()));
    }
    if (rs != null)
    {
      int i = rs.redstoneLevel;
      if (rs.nextRedstoneLevel != Byte.MIN_VALUE) {
        i = rs.nextRedstoneLevel;
      }
      addInfo(paramList, "redstone", Integer.valueOf(i));
    }
  }
  
  protected final void addInfo(List<IChatComponent> paramList, String paramString, Object paramObject)
  {
    paramList.add(new ChatComponentTranslation("info.thermaldynamics.info." + paramString, new Object[0]).appendText(": ").appendSibling(ChatHelper.getChatComponent(paramObject)));
  }
  
  public static class RedstoneControl
  {
    public byte nextRedstoneLevel = Byte.MIN_VALUE;
    public int redstoneLevel;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\MultiBlockGrid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */