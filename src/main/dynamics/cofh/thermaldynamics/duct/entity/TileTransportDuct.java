package cofh.thermaldynamics.duct.entity;

import cofh.api.block.IBlockConfigGui;
import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;
import cofh.core.network.PacketTileInfo;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.repack.codechicken.lib.raytracer.RayTracer;
import cofh.thermaldynamics.ThermalDynamics;
import cofh.thermaldynamics.block.Attachment;
import cofh.thermaldynamics.block.SubTileMultiBlock;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.duct.BlockDuct.ConnectionTypes;
import cofh.thermaldynamics.duct.attachments.cover.Cover;
import cofh.thermaldynamics.duct.attachments.cover.CoverHoleRender;
import cofh.thermaldynamics.duct.attachments.cover.CoverHoleRender.ITransformer;
import cofh.thermaldynamics.gui.client.DirectoryEntry;
import cofh.thermaldynamics.gui.client.GuiTransport;
import cofh.thermaldynamics.gui.client.GuiTransportConfig;
import cofh.thermaldynamics.gui.container.ContainerTransport;
import cofh.thermaldynamics.gui.container.ContainerTransportConfig;
import cofh.thermaldynamics.multiblock.IMultiBlockRoute;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import cofh.thermaldynamics.multiblock.Route;
import cofh.thermaldynamics.multiblock.RouteCache;
import cofh.thermaldynamics.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.list.linked.TByteLinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileTransportDuct
  extends TileTransportDuctBaseRoute
  implements IBlockConfigGui
{
  public boolean isOutput()
  {
    return isOutput;
  }
  
  public void handleTileSideUpdate(int paramInt)
  {
    super.handleTileSideUpdate(paramInt);
    if (connectionTypes[paramInt] == TileTDBase.ConnectionTypes.FORCED)
    {
      neighborMultiBlocks[paramInt] = null;
      neighborTypes[paramInt] = TileTDBase.NeighborTypes.OUTPUT;
      isNode = true;
      isOutput = true;
    }
  }
  
  public boolean isBlockedSide(int paramInt)
  {
    return (super.isBlockedSide(paramInt)) || (connectionTypes[paramInt] == TileTDBase.ConnectionTypes.FORCED);
  }
  
  public boolean onWrench(EntityPlayer paramEntityPlayer, int paramInt)
  {
    if (Utils.isHoldingUsableWrench(paramEntityPlayer, xCoord, yCoord, zCoord))
    {
      MovingObjectPosition localMovingObjectPosition = RayTracer.retraceBlock(worldObj, paramEntityPlayer, xCoord, yCoord, zCoord);
      if (localMovingObjectPosition == null) {
        return false;
      }
      int i = subHit;
      if ((i >= 0) && (i <= 13))
      {
        int j = i < 6 ? i : i == 13 ? paramInt : i - 6;
        
        onNeighborBlockChange();
        
        TileEntity localTileEntity = BlockHelper.getAdjacentTileEntity(this, j);
        if (isConnectable(localTileEntity, j))
        {
          connectionTypes[j] = connectionTypes[j].next();
          connectionTypes[(j ^ 0x1)] = connectionTypes[j];
        }
        else if (connectionTypes[j] == TileTDBase.ConnectionTypes.FORCED)
        {
          connectionTypes[j] = TileTDBase.ConnectionTypes.NORMAL;
        }
        else
        {
          connectionTypes[j] = TileTDBase.ConnectionTypes.FORCED;
          for (int k = 0; k < 6; k++) {
            if ((j != k) && (connectionTypes[k] == TileTDBase.ConnectionTypes.FORCED)) {
              connectionTypes[k] = TileTDBase.ConnectionTypes.NORMAL;
            }
          }
        }
        onNeighborBlockChange();
        
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, func_145838_q());
        if (myGrid != null) {
          myGrid.destroyAndRecreate();
        }
        for (SubTileMultiBlock localSubTileMultiBlock : subTiles) {
          localSubTileMultiBlock.destroyAndRecreate();
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return true;
      }
      if ((i > 13) && (i < 20)) {
        return attachments[(i - 14)].onWrenched();
      }
      if ((i >= 20) && (i < 26)) {
        return covers[(i - 20)].onWrenched();
      }
    }
    return false;
  }
  
  public boolean openGui(EntityPlayer paramEntityPlayer)
  {
    if ((super.openGui(paramEntityPlayer)) || (ServerHelper.isClientWorld(worldObj))) {
      return true;
    }
    if (internalGrid == null) {
      return false;
    }
    onNeighborBlockChange();
    for (int i = 0; i < 6; i++) {
      if ((connectionTypes[i] == TileTDBase.ConnectionTypes.FORCED) && 
        (neighborMultiBlocks[i] == null))
      {
        PacketHandler.sendTo(getPacket(), paramEntityPlayer);
        paramEntityPlayer.openGui(ThermalDynamics.instance, 0, worldObj, xCoord, yCoord, zCoord);
        return true;
      }
    }
    return false;
  }
  
  private static final OutputData BLANK_NAME = new OutputData();
  public OutputData data;
  public static final int NETWORK_REQUEST = 0;
  public static final int NETWORK_SETOUTPUTDATA = 1;
  public static final int NETWORK_LIST = 2;
  public static final int NETWORK_CONFIG = 3;
  
  public TileTransportDuct()
  {
    data = BLANK_NAME;
  }
  
  public void func_145841_b(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145841_b(paramNBTTagCompound);
    if (data != BLANK_NAME) {
      data.write(paramNBTTagCompound, this);
    }
  }
  
  public void func_145839_a(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145839_a(paramNBTTagCompound);
    data = OutputData.read(paramNBTTagCompound);
  }
  
  public PacketCoFHBase getPacket()
  {
    PacketCoFHBase localPacketCoFHBase = super.getPacket();
    if (data != BLANK_NAME)
    {
      localPacketCoFHBase.addBool(true);
      data.addToPacket(localPacketCoFHBase);
    }
    else
    {
      localPacketCoFHBase.addBool(false);
    }
    return localPacketCoFHBase;
  }
  
  public void handleTilePacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean)
  {
    super.handleTilePacket(paramPacketCoFHBase, paramBoolean);
    if (paramPacketCoFHBase.getBool())
    {
      if (data == BLANK_NAME) {
        data = new OutputData();
      }
      data.readPacket(paramPacketCoFHBase);
    }
    else
    {
      data = BLANK_NAME;
    }
  }
  
  public void handleInfoPacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean, EntityPlayer paramEntityPlayer)
  {
    int i = paramPacketCoFHBase.getByte();
    if ((i == 0) && (paramBoolean))
    {
      sendPlayerToDest(paramEntityPlayer, paramPacketCoFHBase.getInt(), paramPacketCoFHBase.getInt(), paramPacketCoFHBase.getInt());
    }
    else if ((i == 1) && (paramBoolean))
    {
      if (data == BLANK_NAME) {
        data = new OutputData();
      }
      data.loadConfigData(paramPacketCoFHBase);
      if (internalGrid != null) {
        internalGrid.onMajorGridChange();
      }
    }
    else if ((i == 2) && (!paramBoolean))
    {
      Container localContainer = openContainer;
      if (!(localContainer instanceof ContainerTransport)) {
        return;
      }
      ContainerTransport localContainerTransport = (ContainerTransport)localContainer;
      
      localContainerTransport.setEntry(new DirectoryEntry(paramPacketCoFHBase));
      
      ArrayList localArrayList = new ArrayList();
      
      int j = paramPacketCoFHBase.getShort();
      for (int k = 0; k < j; k++) {
        localArrayList.add(new DirectoryEntry(paramPacketCoFHBase));
      }
      localContainerTransport.setDirectory(localArrayList);
    }
    else if ((i == 3) && (paramBoolean))
    {
      PacketHandler.sendTo(getPacket(), paramEntityPlayer);
      paramEntityPlayer.openGui(ThermalDynamics.instance, 7, worldObj, xCoord, yCoord, zCoord);
    }
  }
  
  public void setName(String paramString)
  {
    if (!paramString.equals(data.name))
    {
      if (data == BLANK_NAME) {
        data = new OutputData();
      }
      data.name = paramString;
      sendOutputDataConfigPacket();
    }
  }
  
  public void setIcon(ItemStack paramItemStack)
  {
    if (data == BLANK_NAME) {
      data = new OutputData();
    }
    data.item = paramItemStack;
    sendOutputDataConfigPacket();
  }
  
  public void sendOutputDataConfigPacket()
  {
    if (worldObj.isRemote)
    {
      PacketTileInfo localPacketTileInfo = PacketTileInfo.newPacket(this);
      localPacketTileInfo.addByte(0);
      localPacketTileInfo.addByte(1);
      data.saveConfigData(localPacketTileInfo);
      PacketHandler.sendToServer(localPacketTileInfo);
    }
  }
  
  public void sendRequest(int paramInt1, int paramInt2, int paramInt3)
  {
    PacketTileInfo localPacketTileInfo = PacketTileInfo.newPacket(this);
    localPacketTileInfo.addByte(0);
    localPacketTileInfo.addByte(0);
    localPacketTileInfo.addInt(paramInt1);
    localPacketTileInfo.addInt(paramInt2);
    localPacketTileInfo.addInt(paramInt3);
    PacketHandler.sendToServer(localPacketTileInfo);
  }
  
  public PacketCoFHBase getDirectoryPacket()
  {
    PacketTileInfo localPacketTileInfo = PacketTileInfo.newPacket(this);
    localPacketTileInfo.addByte(0);
    localPacketTileInfo.addByte(2);
    
    LinkedList localLinkedList = getCacheoutputRoutes;
    
    ArrayList localArrayList = new ArrayList(localLinkedList.size());
    for (Iterator localIterator = localLinkedList.iterator(); localIterator.hasNext();)
    {
      localObject = (Route)localIterator.next();
      if ((endPoint.isOutput()) && (endPoint != this) && ((endPoint instanceof TileTransportDuct))) {
        localArrayList.add((TileTransportDuct)endPoint);
      }
    }
    Object localObject;
    DirectoryEntry.addDirectoryEntry(localPacketTileInfo, this);
    
    localPacketTileInfo.addShort(localArrayList.size());
    for (localIterator = localArrayList.iterator(); localIterator.hasNext();)
    {
      localObject = (TileTransportDuct)localIterator.next();
      DirectoryEntry.addDirectoryEntry(localPacketTileInfo, (TileTransportDuct)localObject);
    }
    return localPacketTileInfo;
  }
  
  public boolean sendPlayerToDest(EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3)
  {
    for (Route localRoute1 : getCacheoutputRoutes)
    {
      IMultiBlockRoute localIMultiBlockRoute = endPoint;
      if ((localIMultiBlockRoute.x() == paramInt1) && (localIMultiBlockRoute.y() == paramInt2) && (localIMultiBlockRoute.z() == paramInt3))
      {
        Route localRoute2 = localRoute1.copy();
        pathDirections.add(localIMultiBlockRoute.getStuffedSide());
        
        EntityTransport localEntityTransport = new EntityTransport(this, localRoute2, (byte)(getStuffedSide() ^ 0x1), (byte)50);
        localEntityTransport.start(paramEntityPlayer);
        if ((openContainer instanceof ContainerTransport)) {
          paramEntityPlayer.closeScreen();
        }
        return true;
      }
    }
    return false;
  }
  
  public Object getGuiServer(InventoryPlayer paramInventoryPlayer)
  {
    return new ContainerTransport(this);
  }
  
  @SideOnly(Side.CLIENT)
  public Object getGuiClient(InventoryPlayer paramInventoryPlayer)
  {
    return new GuiTransport(this);
  }
  
  public Object getConfigGuiServer(InventoryPlayer paramInventoryPlayer)
  {
    return new ContainerTransportConfig(paramInventoryPlayer, this);
  }
  
  public Object getConfigGuiClient(InventoryPlayer paramInventoryPlayer)
  {
    return new GuiTransportConfig(paramInventoryPlayer, this);
  }
  
  public boolean openConfigGui(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer)
  {
    if (ServerHelper.isClientWorld(worldObj)) {
      return true;
    }
    PacketHandler.sendTo(getPacket(), paramEntityPlayer);
    paramEntityPlayer.openGui(ThermalDynamics.instance, 7, worldObj, xCoord, yCoord, zCoord);
    return true;
  }
  
  public static class OutputData
  {
    public String name = "";
    public ItemStack item = null;
    
    public void write(NBTTagCompound paramNBTTagCompound, TileTransportDuct paramTileTransportDuct)
    {
      if (!"".equals(name)) {
        paramNBTTagCompound.setString("DestinationName", name);
      }
      if (item != null) {
        paramNBTTagCompound.setTag("DestinationIcon", item.writeToNBT(new NBTTagCompound()));
      }
    }
    
    public static OutputData read(NBTTagCompound paramNBTTagCompound)
    {
      if ((!paramNBTTagCompound.hasKey("DestinationName")) && (!paramNBTTagCompound.hasKey("DestinationIcon"))) {
        return TileTransportDuct.BLANK_NAME;
      }
      OutputData localOutputData = new OutputData();
      name = paramNBTTagCompound.getString("DestinationName");
      item = ItemStack.loadItemStackFromNBT(paramNBTTagCompound.getCompoundTag("DestinationIcon"));
      return localOutputData;
    }
    
    public void addToPacket(PacketCoFHBase paramPacketCoFHBase)
    {
      paramPacketCoFHBase.addString(name);
      paramPacketCoFHBase.addItemStack(item);
    }
    
    public void readPacket(PacketCoFHBase paramPacketCoFHBase)
    {
      name = paramPacketCoFHBase.getString();
      item = paramPacketCoFHBase.getItemStack();
    }
    
    public void loadConfigData(PacketCoFHBase paramPacketCoFHBase)
    {
      String str = name;
      ItemStack localItemStack = ItemStack.copyItemStack(item);
      try
      {
        name = paramPacketCoFHBase.getString();
        item = paramPacketCoFHBase.getItemStack();
      }
      catch (RuntimeException localRuntimeException)
      {
        name = str;
        item = localItemStack;
      }
    }
    
    public void saveConfigData(PacketTileInfo paramPacketTileInfo)
    {
      paramPacketTileInfo.addString(name);
      paramPacketTileInfo.addItemStack(item);
    }
  }
  
  @SideOnly(Side.CLIENT)
  public CoverHoleRender.ITransformer[] getHollowMask(byte paramByte)
  {
    BlockDuct.ConnectionTypes localConnectionTypes = getRenderConnectionType(paramByte);
    return localConnectionTypes == BlockDuct.ConnectionTypes.NONE ? null : CoverHoleRender.hollowDuctTransport;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TileTransportDuct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */