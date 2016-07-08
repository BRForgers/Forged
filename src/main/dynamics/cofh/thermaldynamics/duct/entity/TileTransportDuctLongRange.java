package cofh.thermaldynamics.duct.entity;

import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.duct.BlockDuct.ConnectionTypes;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import cofh.thermaldynamics.multiblock.Route;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileTransportDuctLongRange
  extends TileTransportDuctBase
{
  byte d1;
  byte d2;
  byte connections;
  
  public MultiBlockGrid getNewGrid()
  {
    return null;
  }
  
  public void onNeighborBlockChange()
  {
    d1 = 7;
    d2 = 7;
    super.onNeighborBlockChange();
    
    checkConnections();
  }
  
  public void onNeighborTileChange(int paramInt1, int paramInt2, int paramInt3)
  {
    d1 = 7;
    d2 = 7;
    
    super.onNeighborTileChange(paramInt1, paramInt2, paramInt3);
    
    checkConnections();
  }
  
  public void checkConnections()
  {
    connections = 0;
    for (byte b = 5; b > 0; b = (byte)(b - 1)) {
      if (neighborTypes[b] != TileTDBase.NeighborTypes.NONE) {
        if (connections == 0)
        {
          d1 = b;
          connections = 1;
        }
        else if (connections == 1)
        {
          d2 = b;
          connections = 2;
        }
        else
        {
          d1 = 7;
          d2 = 7;
          connections = 3;
          break;
        }
      }
    }
  }
  
  public void formGrid() {}
  
  public boolean isValidForForming()
  {
    return false;
  }
  
  public byte nextDirection(byte paramByte)
  {
    if (connections != 2) {
      return -1;
    }
    if ((paramByte ^ 0x1) == d1) {
      return d2;
    }
    if ((paramByte ^ 0x1) == d2) {
      return d1;
    }
    return -1;
  }
  
  public void handleTileSideUpdate(int paramInt)
  {
    super.handleTileSideUpdate(paramInt);
  }
  
  public boolean advanceEntity(EntityTransport paramEntityTransport)
  {
    int i = progress;
    i += step * 2;
    progress = ((byte)(i % 100));
    if (i >= 100)
    {
      if ((neighborTypes[direction] == TileTDBase.NeighborTypes.MULTIBLOCK) && (connectionTypes[direction] == TileTDBase.ConnectionTypes.NORMAL))
      {
        TileTransportDuctBase localTileTransportDuctBase = (TileTransportDuctBase)getConnectedSide(direction);
        localTileTransportDuctBase.onNeighborBlockChange();
        if (neighborTypes[(direction ^ 0x1)] == TileTDBase.NeighborTypes.MULTIBLOCK)
        {
          pos = new BlockPosition(localTileTransportDuctBase);
          
          oldDirection = direction;
          if ((localTileTransportDuctBase instanceof TileTransportDuctLongRange))
          {
            TileTransportDuctLongRange localTileTransportDuctLongRange = (TileTransportDuctLongRange)localTileTransportDuctBase;
            direction = localTileTransportDuctLongRange.nextDirection(direction);
            if (direction == -1)
            {
              paramEntityTransport.dropPassenger();
              return true;
            }
          }
          else if (myPath != null)
          {
            if (myPath.hasNextDirection()) {
              direction = myPath.getNextDirection();
            } else {
              reRoute = true;
            }
          }
        }
      }
      else
      {
        paramEntityTransport.dropPassenger();
        return true;
      }
    }
    else if ((progress >= 50) && (progress - step < 50) && 
      (neighborTypes[direction] == TileTDBase.NeighborTypes.NONE))
    {
      paramEntityTransport.dropPassenger();
      return true;
    }
    return false;
  }
  
  public boolean advanceEntityClient(EntityTransport paramEntityTransport)
  {
    int i = progress;
    i += step + step;
    progress = ((byte)(i % 100));
    if ((i >= 100) && 
      (!paramEntityTransport.trySimpleAdvance())) {
      return true;
    }
    return false;
  }
  
  public boolean isConnectable(TileEntity paramTileEntity, int paramInt)
  {
    return ((paramTileEntity instanceof TileTransportDuctLongRange)) || ((paramTileEntity instanceof TileTransportDuctCrossover));
  }
  
  public void func_145841_b(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145841_b(paramNBTTagCompound);
    paramNBTTagCompound.setByte("SimpleConnect", connections);
    paramNBTTagCompound.setByte("SimpleConnect1", d1);
    paramNBTTagCompound.setByte("SimpleConnect2", d2);
  }
  
  public void func_145839_a(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145839_a(paramNBTTagCompound);
    connections = paramNBTTagCompound.getByte("SimpleConnect");
    d1 = paramNBTTagCompound.getByte("SimpleConnect1");
    d2 = paramNBTTagCompound.getByte("SimpleConnect2");
  }
  
  public BlockDuct.ConnectionTypes getRenderConnectionType(int paramInt)
  {
    BlockDuct.ConnectionTypes localConnectionTypes = super.getRenderConnectionType(paramInt);
    if ((localConnectionTypes == BlockDuct.ConnectionTypes.NONE) || (connections == 0)) {
      return localConnectionTypes;
    }
    if ((paramInt != d1) && (paramInt != d2)) {
      return BlockDuct.ConnectionTypes.NONE;
    }
    TileEntity localTileEntity = BlockHelper.getAdjacentTileEntity(this, paramInt);
    if ((localTileEntity != null) && (localTileEntity.getClass() == TileTransportDuctLongRange.class))
    {
      TileTransportDuctLongRange localTileTransportDuctLongRange = (TileTransportDuctLongRange)localTileEntity;
      if (((d1 ^ 0x1) == paramInt) || ((d2 ^ 0x1) == paramInt)) {
        return localConnectionTypes;
      }
      return BlockDuct.ConnectionTypes.NONE;
    }
    return localConnectionTypes;
  }
  
  public PacketCoFHBase getPacket()
  {
    PacketCoFHBase localPacketCoFHBase = super.getPacket();
    localPacketCoFHBase.addShort(connections << 6 | d1 << 3 | d2);
    return localPacketCoFHBase;
  }
  
  public void handleTilePacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean)
  {
    super.handleTilePacket(paramPacketCoFHBase, paramBoolean);
    int i = paramPacketCoFHBase.getShort();
    connections = ((byte)(i >> 6 & 0x7));
    d1 = ((byte)(i >> 3 & 0x7));
    d2 = ((byte)(i & 0x7));
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TileTransportDuctLongRange.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */