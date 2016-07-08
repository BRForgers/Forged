package cofh.thermaldynamics.multiblock;

import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.duct.item.TileItemDuct.RouteInfo;
import net.minecraft.item.ItemStack;

public abstract interface IMultiBlockRoute
  extends IMultiBlock
{
  public abstract int getWeight();
  
  public abstract boolean canStuffItem();
  
  public abstract boolean isOutput();
  
  public abstract int getMaxRange();
  
  public abstract TileTDBase.NeighborTypes getCachedSideType(byte paramByte);
  
  public abstract TileTDBase.ConnectionTypes getConnectionType(byte paramByte);
  
  public abstract IMultiBlock getCachedTile(byte paramByte);
  
  public abstract TileItemDuct.RouteInfo canRouteItem(ItemStack paramItemStack);
  
  public abstract byte getStuffedSide();
  
  public abstract boolean acceptingStuff();
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\IMultiBlockRoute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */