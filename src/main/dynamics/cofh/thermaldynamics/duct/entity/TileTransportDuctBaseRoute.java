package cofh.thermaldynamics.duct.entity;

import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.duct.item.TileItemDuct;
import cofh.thermaldynamics.duct.item.TileItemDuct.RouteInfo;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.IMultiBlockRoute;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import cofh.thermaldynamics.multiblock.Route;
import cofh.thermaldynamics.multiblock.RouteCache;
import gnu.trove.list.linked.TByteLinkedList;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileTransportDuctBaseRoute
  extends TileTransportDuctBase
  implements IMultiBlockRoute
{
  public TransportGrid internalGrid;
  
  public void setGrid(MultiBlockGrid paramMultiBlockGrid)
  {
    super.setGrid(paramMultiBlockGrid);
    internalGrid = ((TransportGrid)paramMultiBlockGrid);
  }
  
  public MultiBlockGrid getNewGrid()
  {
    return new TransportGrid(worldObj);
  }
  
  public RouteCache getCache()
  {
    return getCache(true);
  }
  
  public RouteCache getCache(boolean paramBoolean)
  {
    return paramBoolean ? internalGrid.getRoutesFromOutput(this) : internalGrid.getRoutesFromOutputNonUrgent(this);
  }
  
  public Route getRoute(Entity paramEntity, int paramInt, byte paramByte)
  {
    if ((paramEntity == null) || (isDead)) {
      return null;
    }
    for (Route localRoute1 : getCacheoutputRoutes) {
      if ((endPoint != this) && (endPoint.isOutput()))
      {
        Route localRoute2 = localRoute1.copy();
        byte b = endPoint.getStuffedSide();
        pathDirections.add(b);
        return localRoute2;
      }
    }
    return null;
  }
  
  public EntityTransport findRoute(Entity paramEntity, int paramInt, byte paramByte)
  {
    Route localRoute = getRoute(paramEntity, paramInt, paramByte);
    return localRoute != null ? new EntityTransport(this, localRoute, (byte)paramInt, paramByte) : null;
  }
  
  public int getWeight()
  {
    return 1;
  }
  
  public boolean canStuffItem()
  {
    return false;
  }
  
  public boolean isOutput()
  {
    return false;
  }
  
  public int getMaxRange()
  {
    return Integer.MAX_VALUE;
  }
  
  public TileTDBase.NeighborTypes getCachedSideType(byte paramByte)
  {
    return neighborTypes[paramByte];
  }
  
  public TileTDBase.ConnectionTypes getConnectionType(byte paramByte)
  {
    return connectionTypes[paramByte];
  }
  
  public IMultiBlock getCachedTile(byte paramByte)
  {
    return neighborMultiBlocks[paramByte];
  }
  
  public TileItemDuct.RouteInfo canRouteItem(ItemStack paramItemStack)
  {
    return TileItemDuct.noRoute;
  }
  
  public byte getStuffedSide()
  {
    for (byte b = 0; b < 6; b = (byte)(b + 1)) {
      if (neighborTypes[b] == TileTDBase.NeighborTypes.OUTPUT) {
        return b;
      }
    }
    return 0;
  }
  
  public boolean acceptingStuff()
  {
    return false;
  }
  
  public boolean advanceEntity(EntityTransport paramEntityTransport)
  {
    EntityTransport tmp1_0 = paramEntityTransport;10progress = ((byte)(10progress + step));
    if (myPath == null)
    {
      paramEntityTransport.bouncePassenger(this);
    }
    else if (progress >= 100)
    {
      EntityTransport tmp39_38 = paramEntityTransport;3938progress = ((byte)(3938progress % 100));
      advanceToNextTile(paramEntityTransport);
    }
    else if ((progress >= 50) && (progress - step < 50) && (
      (reRoute) || (neighborTypes[direction] == TileTDBase.NeighborTypes.NONE)))
    {
      paramEntityTransport.bouncePassenger(this);
    }
    return false;
  }
  
  public void advanceToNextTile(EntityTransport paramEntityTransport)
  {
    paramEntityTransport.advanceTile(this);
  }
  
  public boolean isConnectable(TileEntity paramTileEntity, int paramInt)
  {
    return paramTileEntity instanceof TileTransportDuctBaseRoute;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TileTransportDuctBaseRoute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */