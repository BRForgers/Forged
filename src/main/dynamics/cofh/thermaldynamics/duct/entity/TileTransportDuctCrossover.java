package cofh.thermaldynamics.duct.entity;

import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.debughelper.DebugHelper;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.Route;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTransportDuctCrossover
  extends TileTransportDuctBaseRoute
{
  final BlockPosition[] rangePos = new BlockPosition[6];
  static final BlockPosition clientValue = new BlockPosition(0, 0, 0, ForgeDirection.DOWN);
  public static byte CHARGE_TIME = 120;
  
  public void handleTileSideUpdate(int paramInt)
  {
    super.handleTileSideUpdate(paramInt);
    if ((rangePos[paramInt] == null) || (rangePos[paramInt].orientation == ForgeDirection.UNKNOWN))
    {
      rangePos[paramInt] = null;
      return;
    }
    if (neighborTypes[paramInt] != TileTDBase.NeighborTypes.OUTPUT)
    {
      if ((paramInt < 2) || (worldObj.blockExists(xCoord + net.minecraft.util.Facing.offsetsXForSide[paramInt], yCoord, zCoord + net.minecraft.util.Facing.offsetsZForSide[paramInt]))) {
        rangePos[paramInt] = null;
      }
      return;
    }
    if (rangePos[paramInt] == clientValue) {
      return;
    }
    int i = rangePos[paramInt].orientation.ordinal();
    if (worldObj.blockExists(rangePos[paramInt].x, rangePos[paramInt].y, rangePos[paramInt].z))
    {
      TileEntity localTileEntity = worldObj.getTileEntity(rangePos[paramInt].x, rangePos[paramInt].y, rangePos[paramInt].z);
      if (((localTileEntity instanceof TileTransportDuctCrossover)) && (!isBlockedSide(paramInt)) && (!((TileTDBase)localTileEntity).isBlockedSide(i ^ 0x1)))
      {
        neighborMultiBlocks[paramInt] = ((IMultiBlock)localTileEntity);
        neighborTypes[paramInt] = TileTDBase.NeighborTypes.MULTIBLOCK;
      }
      else
      {
        rangePos[paramInt] = null;
        super.handleTileSideUpdate(paramInt);
      }
    }
    else
    {
      neighborMultiBlocks[paramInt] = null;
      neighborTypes[paramInt] = TileTDBase.NeighborTypes.OUTPUT;
    }
  }
  
  public boolean isOutput()
  {
    return false;
  }
  
  public Route getRoute(Entity paramEntity, int paramInt, byte paramByte)
  {
    return null;
  }
  
  public boolean openGui(EntityPlayer paramEntityPlayer)
  {
    if (worldObj.isRemote) {
      return true;
    }
    DebugHelper.startTimer();
    for (byte b1 = 0; b1 < 6; b1 = (byte)(b1 + 1))
    {
      rangePos[b1] = null;
      
      int i = 1;
      
      TileEntity localTileEntity1 = getAdjTileEntitySafe(b1);
      if ((localTileEntity1 instanceof TileTransportDuctLongRange))
      {
        paramEntityPlayer.addChatComponentMessage(new ChatComponentText("Searching on side - " + ForgeDirection.getOrientation(b1)));
        
        TileTransportDuctLongRange localTileTransportDuctLongRange = (TileTransportDuctLongRange)localTileEntity1;
        
        TileTransportDuctCrossover localTileTransportDuctCrossover = null;
        
        byte b2 = localTileTransportDuctLongRange.nextDirection(b1);
        
        BlockPosition localBlockPosition = new BlockPosition(localTileTransportDuctLongRange);
        while (b2 != -1)
        {
          i++;
          localBlockPosition.step(b2);
          for (int j = 2; j < 6; j++) {
            worldObj.getChunkFromBlockCoords(x + net.minecraft.util.Facing.offsetsXForSide[j], z + net.minecraft.util.Facing.offsetsZForSide[j]);
          }
          TileEntity localTileEntity2 = worldObj.getTileEntity(x, y, z);
          if ((localTileEntity2 instanceof TileTransportDuctCrossover))
          {
            localTileTransportDuctCrossover = (TileTransportDuctCrossover)localTileEntity2;
            break;
          }
          if (!(localTileEntity2 instanceof TileTransportDuctLongRange)) {
            break;
          }
          localTileTransportDuctLongRange = (TileTransportDuctLongRange)localTileEntity2;
          
          localTileTransportDuctLongRange.onNeighborBlockChange();
          
          b2 = localTileTransportDuctLongRange.nextDirection(b2);
        }
        if (localTileTransportDuctCrossover != null)
        {
          paramEntityPlayer.addChatComponentMessage(new ChatComponentText("Linked to -  (" + localTileTransportDuctCrossover.x() + ", " + localTileTransportDuctCrossover.y() + ", " + localTileTransportDuctCrossover.z() + ")"));
          rangePos[(b2 ^ 0x1)] = new BlockPosition(this).setOrientation(ForgeDirection.getOrientation(b1 ^ 0x1));
          rangePos[b1] = new BlockPosition(localTileTransportDuctCrossover).setOrientation(ForgeDirection.getOrientation(b2));
          if (internalGrid != null) {
            internalGrid.destroyAndRecreate();
          }
          if (internalGrid != null) {
            internalGrid.destroyAndRecreate();
          }
        }
        else
        {
          paramEntityPlayer.addChatComponentMessage(new ChatComponentText("Failed at - (" + x + ", " + y + ", " + z + ")"));
        }
      }
    }
    DebugHelper.stopTimer("Timer: ");
    return true;
  }
  
  public IMultiBlock getPhysicalConnectedSide(byte paramByte)
  {
    if (rangePos[paramByte] != null)
    {
      TileEntity localTileEntity = BlockHelper.getAdjacentTileEntity(this, paramByte);
      if ((localTileEntity instanceof TileTransportDuctLongRange)) {
        return (TileTransportDuctLongRange)localTileEntity;
      }
      return null;
    }
    return super.getPhysicalConnectedSide(paramByte);
  }
  
  public void advanceToNextTile(EntityTransport paramEntityTransport)
  {
    if (rangePos[direction] == null)
    {
      super.advanceToNextTile(paramEntityTransport);
    }
    else if ((neighborTypes[direction] == TileTDBase.NeighborTypes.MULTIBLOCK) && (connectionTypes[direction].allowTransfer))
    {
      TileTransportDuctBase localTileTransportDuctBase = (TileTransportDuctBase)getPhysicalConnectedSide(direction);
      if (!(localTileTransportDuctBase instanceof TileTransportDuctLongRange))
      {
        paramEntityTransport.bouncePassenger(this);
        return;
      }
      if (neighborTypes[(direction ^ 0x1)] == TileTDBase.NeighborTypes.MULTIBLOCK)
      {
        pos = new BlockPosition(localTileTransportDuctBase);
        
        oldDirection = direction;
        direction = ((TileTransportDuctLongRange)localTileTransportDuctBase).nextDirection(direction);
        if (direction == -1) {
          paramEntityTransport.dropPassenger();
        }
      }
      else
      {
        reRoute = true;
      }
    }
    else if ((neighborTypes[direction] == TileTDBase.NeighborTypes.OUTPUT) && (connectionTypes[direction].allowTransfer))
    {
      paramEntityTransport.dropPassenger();
    }
    else
    {
      paramEntityTransport.bouncePassenger(this);
    }
  }
  
  public boolean advanceEntity(EntityTransport paramEntityTransport)
  {
    if ((progress < 50) && (progress + step >= 50) && 
      (neighborTypes[direction] == TileTDBase.NeighborTypes.MULTIBLOCK) && (rangePos[direction] != null))
    {
      progress = 50;
      pause = CHARGE_TIME;
      return true;
    }
    return super.advanceEntity(paramEntityTransport);
  }
  
  public boolean advanceEntityClient(EntityTransport paramEntityTransport)
  {
    if ((progress < 50) && (progress + step >= 50) && 
      (neighborTypes[direction] == TileTDBase.NeighborTypes.MULTIBLOCK) && (rangePos[direction] != null))
    {
      progress = 50;
      pause = CHARGE_TIME;
      return true;
    }
    return super.advanceEntityClient(paramEntityTransport);
  }
  
  public void func_145839_a(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145839_a(paramNBTTagCompound);
    for (int i = 0; i < 6; i = (byte)(i + 1)) {
      if (paramNBTTagCompound.hasKey("crossover" + i, 10))
      {
        NBTTagCompound localNBTTagCompound = paramNBTTagCompound.getCompoundTag("crossover" + i);
        rangePos[i] = new BlockPosition(localNBTTagCompound);
      }
    }
  }
  
  public void func_145841_b(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145841_b(paramNBTTagCompound);
    for (int i = 0; i < 6; i++) {
      if (rangePos[i] != null)
      {
        NBTTagCompound localNBTTagCompound = new NBTTagCompound();
        rangePos[i].writeToNBT(localNBTTagCompound);
        paramNBTTagCompound.setTag("crossover" + i, localNBTTagCompound);
      }
    }
  }
  
  public boolean isConnectable(TileEntity paramTileEntity, int paramInt)
  {
    return ((paramTileEntity instanceof TileTransportDuctBaseRoute)) && (!(paramTileEntity instanceof TileTransportDuctCrossover));
  }
  
  public boolean isSignificantTile(TileEntity paramTileEntity, int paramInt)
  {
    return paramTileEntity instanceof TileTransportDuctLongRange;
  }
  
  public PacketCoFHBase getPacket()
  {
    PacketCoFHBase localPacketCoFHBase = super.getPacket();
    
    int i = 0;
    for (int j = 0; j < 6; j = (byte)(j + 1)) {
      if (rangePos[j] != null) {
        i |= 1 << j;
      }
    }
    localPacketCoFHBase.addInt(i);
    
    return localPacketCoFHBase;
  }
  
  public void handleTilePacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean)
  {
    super.handleTilePacket(paramPacketCoFHBase, paramBoolean);
    if (!paramBoolean)
    {
      int i = paramPacketCoFHBase.getInt();
      for (int j = 0; j < rangePos.length; j++) {
        if ((i & 1 << j) != 0) {
          rangePos[j] = clientValue;
        }
      }
    }
  }
  
  public int getWeight()
  {
    return super.getWeight() * 100;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TileTransportDuctCrossover.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */