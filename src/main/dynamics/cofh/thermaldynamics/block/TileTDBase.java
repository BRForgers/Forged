package cofh.thermaldynamics.block;

import cofh.api.tileentity.IPortableData;
import cofh.api.tileentity.ITileInfo;
import cofh.core.block.TileCoFHBase;
import cofh.core.network.ITileInfoPacketHandler;
import cofh.core.network.ITilePacketHandler;
import cofh.core.network.PacketCoFHBase;
import cofh.core.render.hitbox.CustomHitBox;
import cofh.core.render.hitbox.ICustomHitBox;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.repack.codechicken.lib.raytracer.IndexedCuboid6;
import cofh.repack.codechicken.lib.raytracer.RayTracer;
import cofh.repack.codechicken.lib.vec.Cuboid6;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.core.TickHandler;
import cofh.thermaldynamics.duct.BlockDuct;
import cofh.thermaldynamics.duct.BlockDuct.ConnectionTypes;
import cofh.thermaldynamics.duct.Duct;
import cofh.thermaldynamics.duct.TDDucts;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockFormer;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import cofh.thermaldynamics.util.Utils;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TileTDBase
  extends TileCoFHBase
  implements IMultiBlock, ITilePacketHandler, ICustomHitBox, ITileInfoPacketHandler, IPortableData, ITileInfo
{
  public static Cuboid6[] subSelection;
  public static Cuboid6 selection;
  public static Cuboid6[] subSelection_large;
  
  static
  {
    GameRegistry.registerTileEntityWithAlternatives(TileTDBase.class, "thermaldynamics.Duct", new String[] { "thermaldynamics.multiblock" });
    
    subSelection = new Cuboid6[12];
    
    subSelection_large = new Cuboid6[12];
    
    genSelectionBoxes(subSelection, 0, 0.25D, 0.2D, 0.8D);
    genSelectionBoxes(subSelection, 6, 0.3D, 0.3D, 0.7D);
    selection = new Cuboid6(0.3D, 0.3D, 0.3D, 0.7D, 0.7D, 0.7D);
    
    genSelectionBoxes(subSelection_large, 0, 0.1D, 0.1D, 0.9D);
    genSelectionBoxes(subSelection_large, 6, 0.1D, 0.1D, 0.9D);
  }
  
  public static Cuboid6 selectionlarge = new Cuboid6(0.1D, 0.1D, 0.1D, 0.9D, 0.9D, 0.9D);
  public int facadeMask;
  public boolean isValid;
  public boolean isNode;
  public MultiBlockGrid myGrid;
  public IMultiBlock[] neighborMultiBlocks;
  public NeighborTypes[] neighborTypes;
  public ConnectionTypes[] connectionTypes;
  public byte internalSideCounter;
  
  private static void genSelectionBoxes(Cuboid6[] paramArrayOfCuboid6, int paramInt, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    paramArrayOfCuboid6[paramInt] = new Cuboid6(paramDouble2, 0.0D, paramDouble2, paramDouble3, paramDouble1, paramDouble3);
    paramArrayOfCuboid6[(paramInt + 1)] = new Cuboid6(paramDouble2, 1.0D - paramDouble1, paramDouble2, paramDouble3, 1.0D, paramDouble3);
    paramArrayOfCuboid6[(paramInt + 2)] = new Cuboid6(paramDouble2, paramDouble2, 0.0D, paramDouble3, paramDouble3, paramDouble1);
    paramArrayOfCuboid6[(paramInt + 3)] = new Cuboid6(paramDouble2, paramDouble2, 1.0D - paramDouble1, paramDouble3, paramDouble3, 1.0D);
    paramArrayOfCuboid6[(paramInt + 4)] = new Cuboid6(0.0D, paramDouble2, paramDouble2, paramDouble1, paramDouble3, paramDouble3);
    paramArrayOfCuboid6[(paramInt + 5)] = new Cuboid6(1.0D - paramDouble1, paramDouble2, paramDouble2, 1.0D, paramDouble3, paramDouble3);
  }
  
  public static final SubTileMultiBlock[] blankSubTiles = new SubTileMultiBlock[0];
  public SubTileMultiBlock[] subTiles;
  public long lastUpdateTime;
  public int hashCode;
  public LinkedList<WeakReference<Chunk>> neighbourChunks;
  Duct duct;
  public boolean isOutput;
  public boolean isInput;
  
  public void onChunkUnload()
  {
    if (ServerHelper.isServerWorld(worldObj))
    {
      for (SubTileMultiBlock localSubTileMultiBlock : subTiles) {
        localSubTileMultiBlock.onChunkUnload();
      }
      if (myGrid != null)
      {
        tileUnloading();
        myGrid.removeBlock(this);
      }
    }
    super.func_145843_s();
  }
  
  public World world()
  {
    return func_145831_w();
  }
  
  public int x()
  {
    return xCoord;
  }
  
  public int y()
  {
    return yCoord;
  }
  
  public int z()
  {
    return zCoord;
  }
  
  public void func_145843_s()
  {
    super.func_145843_s();
    if (ServerHelper.isServerWorld(worldObj))
    {
      for (SubTileMultiBlock localSubTileMultiBlock : subTiles) {
        localSubTileMultiBlock.invalidate();
      }
      if (myGrid != null) {
        myGrid.removeBlock(this);
      }
    }
  }
  
  public void setInvalidForForming()
  {
    isValid = false;
  }
  
  public void setValidForForming()
  {
    isValid = true;
  }
  
  public boolean isValidForForming()
  {
    return isValid;
  }
  
  public MultiBlockGrid getGrid()
  {
    return myGrid;
  }
  
  public void setGrid(MultiBlockGrid paramMultiBlockGrid)
  {
    myGrid = paramMultiBlockGrid;
  }
  
  public IMultiBlock getConnectedSide(byte paramByte)
  {
    if (paramByte >= neighborMultiBlocks.length) {
      return null;
    }
    return neighborMultiBlocks[paramByte];
  }
  
  public boolean isBlockedSide(int paramInt)
  {
    return (connectionTypes[paramInt] == ConnectionTypes.BLOCKED) || ((attachments[paramInt] != null) && (!attachments[paramInt].allowPipeConnection()));
  }
  
  public boolean isSideConnected(byte paramByte)
  {
    if (paramByte >= neighborMultiBlocks.length) {
      return false;
    }
    IMultiBlock localIMultiBlock = neighborMultiBlocks[paramByte];
    return (localIMultiBlock != null) && (!isBlockedSide(paramByte)) && (!localIMultiBlock.isBlockedSide(paramByte ^ 0x1));
  }
  
  public void setNotConnected(byte paramByte)
  {
    TileEntity localTileEntity = BlockPosition.getAdjacentTileEntity(this, ForgeDirection.getOrientation(paramByte));
    if (isSignificantTile(localTileEntity, paramByte))
    {
      neighborMultiBlocks[paramByte] = null;
      neighborTypes[paramByte] = NeighborTypes.OUTPUT;
      if (!isNode)
      {
        isNode = true;
        if (myGrid != null) {
          myGrid.addBlock(this);
        }
      }
    }
    else if (isStructureTile(localTileEntity, paramByte))
    {
      neighborMultiBlocks[paramByte] = null;
      neighborTypes[paramByte] = NeighborTypes.STRUCTURE;
    }
    else
    {
      neighborTypes[paramByte] = NeighborTypes.NONE;
      neighborMultiBlocks[paramByte] = null;
      connectionTypes[paramByte] = ConnectionTypes.BLOCKED;
    }
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    for (SubTileMultiBlock localSubTileMultiBlock : subTiles) {
      localSubTileMultiBlock.onNeighbourChange();
    }
  }
  
  public boolean isStructureTile(TileEntity paramTileEntity, int paramInt)
  {
    return false;
  }
  
  public void blockPlaced()
  {
    if (ServerHelper.isServerWorld(worldObj)) {
      TickHandler.addMultiBlockToCalculate(this);
    }
  }
  
  public void onNeighborBlockChange()
  {
    if ((ServerHelper.isClientWorld(worldObj)) && (lastUpdateTime == worldObj.getTotalWorldTime())) {
      return;
    }
    if (func_145837_r()) {
      return;
    }
    boolean bool1 = isNode;
    isNode = false;
    boolean bool2 = isInput;
    isInput = false;
    boolean bool3 = isOutput;
    isOutput = false;
    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i = (byte)(i + 1)) {
      handleSideUpdate(i);
    }
    if (myGrid != null) {
      if (bool1 != isNode) {
        myGrid.addBlock(this);
      } else if ((isOutput != bool3) || (isInput != bool2)) {
        myGrid.onMajorGridChange();
      }
    }
    for (Object localObject2 : subTiles) {
      ((SubTileMultiBlock)localObject2).onNeighbourChange();
    }
    for (??? = tickingAttachments.iterator(); ((Iterator)???).hasNext();)
    {
      Attachment localAttachment = (Attachment)((Iterator)???).next();
      localAttachment.postNeighbourChange();
    }
    if (ServerHelper.isServerWorld(worldObj)) {
      rebuildChunkCache();
    }
    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
  }
  
  public void handleSideUpdate(int paramInt)
  {
    if (cachesExist()) {
      clearCache(paramInt);
    }
    handleAttachmentUpdate(paramInt);
    handleTileSideUpdate(paramInt);
  }
  
  public void handleAttachmentUpdate(int paramInt)
  {
    neighborTypes[paramInt] = null;
    if (attachments[paramInt] != null)
    {
      attachments[paramInt].onNeighborChange();
      neighborMultiBlocks[paramInt] = null;
      
      neighborTypes[paramInt] = attachments[paramInt].getNeighborType();
      TileEntity localTileEntity;
      if (neighborTypes[paramInt] == NeighborTypes.MULTIBLOCK)
      {
        localTileEntity = getAdjTileEntitySafe(paramInt);
        if ((isConnectable(localTileEntity, paramInt)) && (isUnblocked(localTileEntity, paramInt))) {
          neighborMultiBlocks[paramInt] = ((IMultiBlock)localTileEntity);
        } else {
          neighborTypes[paramInt] = NeighborTypes.NONE;
        }
      }
      else if (neighborTypes[paramInt] == NeighborTypes.OUTPUT)
      {
        localTileEntity = getAdjTileEntitySafe(paramInt);
        if (isSignificantTile(localTileEntity, paramInt))
        {
          if (!cachesExist()) {
            createCaches();
          }
          cacheImportant(localTileEntity, paramInt);
        }
        isOutput = true;
      }
      else if (neighborTypes[paramInt] == NeighborTypes.INPUT)
      {
        localTileEntity = getAdjTileEntitySafe(paramInt);
        if (localTileEntity != null)
        {
          if (!cachesExist()) {
            createCaches();
          }
          cacheInputTile(localTileEntity, paramInt);
        }
        isInput = true;
      }
      else
      {
        neighborMultiBlocks[paramInt] = null;
      }
      connectionTypes[paramInt] = ConnectionTypes.NORMAL;
      isNode = attachments[paramInt].isNode();
    }
  }
  
  public void handleTileSideUpdate(int paramInt)
  {
    if (neighborTypes[paramInt] == null)
    {
      TileEntity localTileEntity = getAdjTileEntitySafe(paramInt);
      if (localTileEntity == null)
      {
        neighborMultiBlocks[paramInt] = null;
        neighborTypes[paramInt] = NeighborTypes.NONE;
        if (connectionTypes[paramInt] != ConnectionTypes.FORCED) {
          connectionTypes[paramInt] = ConnectionTypes.NORMAL;
        }
      }
      else if ((isConnectable(localTileEntity, paramInt)) && (isUnblocked(localTileEntity, paramInt)))
      {
        neighborMultiBlocks[paramInt] = ((IMultiBlock)localTileEntity);
        neighborTypes[paramInt] = NeighborTypes.MULTIBLOCK;
      }
      else if ((connectionTypes[paramInt].allowTransfer) && (isSignificantTile(localTileEntity, paramInt)))
      {
        neighborMultiBlocks[paramInt] = null;
        neighborTypes[paramInt] = NeighborTypes.OUTPUT;
        if (!cachesExist()) {
          createCaches();
        }
        cacheImportant(localTileEntity, paramInt);
        isNode = true;
        isOutput = true;
      }
      else if ((connectionTypes[paramInt].allowTransfer) && (isStructureTile(localTileEntity, paramInt)))
      {
        neighborMultiBlocks[paramInt] = null;
        neighborTypes[paramInt] = NeighborTypes.STRUCTURE;
        if (!cachesExist()) {
          createCaches();
        }
        cacheStructural(localTileEntity, paramInt);
        isNode = true;
      }
      else
      {
        neighborMultiBlocks[paramInt] = null;
        neighborTypes[paramInt] = NeighborTypes.NONE;
      }
    }
  }
  
  public TileEntity getAdjTileEntitySafe(int paramInt)
  {
    return BlockHelper.getAdjacentTileEntity(this, paramInt);
  }
  
  public boolean checkForChunkUnload()
  {
    if (neighbourChunks.isEmpty()) {
      return false;
    }
    for (WeakReference localWeakReference : neighbourChunks)
    {
      Object localObject = localWeakReference.get();
      if ((localObject != null) && (!isChunkLoaded))
      {
        neighbourChunks.clear();
        onNeighborBlockChange();
        return true;
      }
    }
    return false;
  }
  
  public void rebuildChunkCache()
  {
    if (!neighbourChunks.isEmpty()) {
      neighbourChunks.clear();
    }
    if (!isNode) {
      return;
    }
    Chunk localChunk1 = worldObj.getChunkFromBlockCoords(x(), y());
    for (int i = 0; i < 6; i = (byte)(i + 1)) {
      if ((neighborTypes[i] == NeighborTypes.INPUT) || (neighborTypes[i] == NeighborTypes.OUTPUT))
      {
        Chunk localChunk2 = worldObj.getChunkFromBlockCoords(x() + net.minecraft.util.Facing.offsetsXForSide[i], z() + net.minecraft.util.Facing.offsetsZForSide[i]);
        if (localChunk2 != localChunk1) {
          neighbourChunks.add(new WeakReference(localChunk2));
        }
      }
    }
  }
  
  public void onNeighborTileChange(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((ServerHelper.isClientWorld(worldObj)) && (lastUpdateTime == worldObj.getTotalWorldTime())) {
      return;
    }
    if (func_145837_r()) {
      return;
    }
    int i = BlockHelper.determineAdjacentSide(this, paramInt1, paramInt2, paramInt3);
    
    boolean bool1 = isNode;
    boolean bool2 = isInput;
    boolean bool3 = isOutput;
    
    handleSideUpdate(i);
    for (Object localObject2 : subTiles) {
      ((SubTileMultiBlock)localObject2).onNeighbourChange();
    }
    checkIsNode();
    if ((bool1 != isNode) && (myGrid != null)) {
      myGrid.addBlock(this);
    } else if ((myGrid != null) && ((isOutput != bool3) || (isInput != bool2))) {
      myGrid.onMajorGridChange();
    }
    for (??? = tickingAttachments.iterator(); ((Iterator)???).hasNext();)
    {
      Attachment localAttachment = (Attachment)((Iterator)???).next();
      localAttachment.postNeighbourChange();
    }
    if (ServerHelper.isServerWorld(worldObj)) {
      rebuildChunkCache();
    }
  }
  
  public void checkIsNode()
  {
    isNode = false;
    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i = (byte)(i + 1))
    {
      if ((neighborTypes[i] == NeighborTypes.OUTPUT) || (neighborTypes[i] == NeighborTypes.STRUCTURE) || ((attachments[i] != null) && (attachments[i].isNode()))) {
        isNode = true;
      }
      if (neighborTypes[i] == NeighborTypes.OUTPUT) {
        isOutput = true;
      }
      if (neighborTypes[i] == NeighborTypes.INPUT) {
        isInput = true;
      }
    }
  }
  
  public void tickInternalSideCounter(int paramInt)
  {
    for (int i = paramInt; i < neighborTypes.length; i++) {
      if ((neighborTypes[i] == NeighborTypes.OUTPUT) && (connectionTypes[i] == ConnectionTypes.NORMAL))
      {
        internalSideCounter = ((byte)i);
        return;
      }
    }
    for (i = 0; i < paramInt; i++) {
      if ((neighborTypes[i] == NeighborTypes.OUTPUT) && (connectionTypes[i] == ConnectionTypes.NORMAL))
      {
        internalSideCounter = ((byte)i);
        return;
      }
    }
  }
  
  public boolean isConnectable(TileEntity paramTileEntity, int paramInt)
  {
    return paramTileEntity instanceof TileTDBase;
  }
  
  public boolean isUnblocked(TileEntity paramTileEntity, int paramInt)
  {
    return (!isBlockedSide(paramInt)) && (!((TileTDBase)paramTileEntity).isBlockedSide(paramInt ^ 0x1));
  }
  
  public boolean isSignificantTile(TileEntity paramTileEntity, int paramInt)
  {
    return false;
  }
  
  public String getName()
  {
    return "tile.thermaldynamics.multiblock.name";
  }
  
  public int getType()
  {
    return 0;
  }
  
  public void tickMultiBlock()
  {
    if (func_145837_r()) {
      return;
    }
    onNeighborBlockChange();
    formGrid();
    for (SubTileMultiBlock localSubTileMultiBlock : subTiles)
    {
      localSubTileMultiBlock.onNeighbourChange();
      localSubTileMultiBlock.formGrid();
    }
  }
  
  public void formGrid()
  {
    if ((myGrid == null) && (ServerHelper.isServerWorld(worldObj))) {
      new MultiBlockFormer().formGrid(this);
    }
  }
  
  public boolean tickPass(int paramInt)
  {
    if (checkForChunkUnload()) {
      return false;
    }
    if (!tickingAttachments.isEmpty()) {
      for (Attachment localAttachment : tickingAttachments) {
        localAttachment.tick(paramInt);
      }
    }
    return true;
  }
  
  public boolean isNode()
  {
    return isNode;
  }
  
  public boolean existsYet()
  {
    return (worldObj != null) && (worldObj.blockExists(xCoord, yCoord, zCoord)) && ((worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockDuct));
  }
  
  public IMultiBlock[] getSubTiles()
  {
    return subTiles;
  }
  
  public void func_145839_a(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145839_a(paramNBTTagCompound);
    for (int i = 0; i < 6; i = (byte)(i + 1))
    {
      NBTTagCompound localNBTTagCompound;
      if (paramNBTTagCompound.hasKey("attachment" + i, 10))
      {
        localNBTTagCompound = paramNBTTagCompound.getCompoundTag("attachment" + i);
        int j = localNBTTagCompound.getShort("id");
        attachments[i] = AttachmentRegistry.createAttachment(this, i, j);
        if (attachments[i] != null)
        {
          attachments[i].readFromNBT(localNBTTagCompound);
          if (attachments[i].doesTick()) {
            tickingAttachments.add(attachments[i]);
          }
        }
      }
      else
      {
        attachments[i] = null;
      }
      if (paramNBTTagCompound.hasKey("facade" + i, 10))
      {
        localNBTTagCompound = paramNBTTagCompound.getCompoundTag("facade" + i);
        covers[i] = new Cover(this, i);
        covers[i].readFromNBT(localNBTTagCompound);
      }
      else
      {
        covers[i] = null;
      }
      connectionTypes[i] = ConnectionTypes.values()[paramNBTTagCompound.getByte("conTypes" + i)];
    }
    recalcFacadeMask();
    for (i = 0; i < subTiles.length; i++) {
      subTiles[i].readFromNBT(paramNBTTagCompound.getCompoundTag("subTile" + i));
    }
    TickHandler.addMultiBlockToCalculate(this);
  }
  
  public void func_145841_b(NBTTagCompound paramNBTTagCompound)
  {
    super.func_145841_b(paramNBTTagCompound);
    Object localObject;
    for (int i = 0; i < 6; i++)
    {
      if (attachments[i] != null)
      {
        localObject = new NBTTagCompound();
        ((NBTTagCompound)localObject).setShort("id", (short)attachments[i].getId());
        attachments[i].writeToNBT((NBTTagCompound)localObject);
        paramNBTTagCompound.setTag("attachment" + i, (NBTBase)localObject);
      }
      if (covers[i] != null)
      {
        localObject = new NBTTagCompound();
        covers[i].writeToNBT((NBTTagCompound)localObject);
        paramNBTTagCompound.setTag("facade" + i, (NBTBase)localObject);
      }
      paramNBTTagCompound.setByte("conTypes" + i, (byte)connectionTypes[i].ordinal());
    }
    for (i = 0; i < subTiles.length; i++)
    {
      localObject = subTiles[i];
      NBTTagCompound localNBTTagCompound = new NBTTagCompound();
      ((SubTileMultiBlock)localObject).writeToNBT(localNBTTagCompound);
      paramNBTTagCompound.setTag("subTile" + i, localNBTTagCompound);
    }
  }
  
  public boolean openGui(EntityPlayer paramEntityPlayer)
  {
    MovingObjectPosition localMovingObjectPosition = RayTracer.retraceBlock(worldObj, paramEntityPlayer, xCoord, yCoord, zCoord);
    if (localMovingObjectPosition == null) {
      return false;
    }
    int i = subHit;
    if ((i > 13) && (i < 20)) {
      return attachments[(i - 14)].openGui(paramEntityPlayer);
    }
    return super.openGui(paramEntityPlayer);
  }
  
  public Duct getDuctType()
  {
    if (duct == null) {
      duct = TDDucts.getDuct(func_145838_qoffset + func_145832_p());
    }
    return duct;
  }
  
  public void addTraceableCuboids(List<IndexedCuboid6> paramList)
  {
    if (!getDuctType().isLargeTube()) {
      addTraceableCuboids(paramList, selection, subSelection);
    } else {
      addTraceableCuboids(paramList, selectionlarge, subSelection_large);
    }
  }
  
  public void addTraceableCuboids(List<IndexedCuboid6> paramList, Cuboid6 paramCuboid6, Cuboid6[] paramArrayOfCuboid6)
  {
    Vector3 localVector3 = new Vector3(xCoord, yCoord, zCoord);
    for (int i = 0; i < 6; i++)
    {
      if (attachments[i] != null)
      {
        paramList.add(new IndexedCuboid6(Integer.valueOf(i + 14), attachments[i].getCuboid().add(localVector3)));
        if (neighborTypes[i] != NeighborTypes.NONE) {
          paramList.add(new IndexedCuboid6(Integer.valueOf(i + 14), paramArrayOfCuboid6[(i + 6)].copy().add(localVector3)));
        }
      }
      if (covers[i] != null) {
        paramList.add(new IndexedCuboid6(Integer.valueOf(i + 20), covers[i].getCuboid().add(localVector3)));
      }
      if (neighborTypes[i] == NeighborTypes.OUTPUT) {
        paramList.add(new IndexedCuboid6(Integer.valueOf(i), paramArrayOfCuboid6[i].copy().add(localVector3)));
      } else if (neighborTypes[i] == NeighborTypes.MULTIBLOCK) {
        paramList.add(new IndexedCuboid6(Integer.valueOf(i + 6), paramArrayOfCuboid6[(i + 6)].copy().add(localVector3)));
      } else if (neighborTypes[i] == NeighborTypes.STRUCTURE) {
        paramList.add(new IndexedCuboid6(Integer.valueOf(i), paramArrayOfCuboid6[(i + 6)].copy().add(localVector3)));
      }
    }
    paramList.add(new IndexedCuboid6(Integer.valueOf(13), paramCuboid6.copy().add(localVector3)));
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
        
        connectionTypes[j] = connectionTypes[j].next();
        
        TileEntity localTileEntity = BlockHelper.getAdjacentTileEntity(this, j);
        if (isConnectable(localTileEntity, j)) {
          connectionTypes[(j ^ 0x1)] = connectionTypes[j];
        }
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
  
  public boolean addFacade(Cover paramCover)
  {
    if (covers[side] != null) {
      return false;
    }
    covers[side] = paramCover;
    recalcFacadeMask();
    func_145831_w().notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, func_145838_q());
    onNeighborBlockChange();
    func_145831_w().markBlockForUpdate(xCoord, yCoord, zCoord);
    return true;
  }
  
  public void removeFacade(Cover paramCover)
  {
    covers[side] = null;
    recalcFacadeMask();
    func_145831_w().notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, func_145838_q());
    onNeighborBlockChange();
    func_145831_w().markBlockForUpdate(xCoord, yCoord, zCoord);
  }
  
  public void recalcFacadeMask()
  {
    facadeMask = 0;
    for (int i = 0; i < 6; i = (byte)(i + 1)) {
      if (covers[i] != null) {
        facadeMask |= 1 << i;
      }
    }
  }
  
  public PacketCoFHBase getPacket()
  {
    PacketCoFHBase localPacketCoFHBase = super.getPacket();
    
    int i = 0;
    recalcFacadeMask();
    for (int j = 0; j < neighborTypes.length; j = (byte)(j + 1))
    {
      localPacketCoFHBase.addByte(neighborTypes[j].ordinal());
      localPacketCoFHBase.addByte(connectionTypes[j].ordinal());
      if (attachments[j] != null) {
        i |= 1 << j;
      }
    }
    localPacketCoFHBase.addBool(isNode);
    
    localPacketCoFHBase.addByte(i);
    for (j = 0; j < 6; j = (byte)(j + 1)) {
      if (attachments[j] != null)
      {
        localPacketCoFHBase.addByte(attachments[j].getId());
        attachments[j].addDescriptionToPacket(localPacketCoFHBase);
      }
    }
    localPacketCoFHBase.addByte(facadeMask);
    for (j = 0; j < 6; j = (byte)(j + 1)) {
      if (covers[j] != null) {
        covers[j].addDescriptionToPacket(localPacketCoFHBase);
      }
    }
    localPacketCoFHBase.addInt(myGrid == null ? 0 : myGrid.hashCode());
    
    return localPacketCoFHBase;
  }
  
  public void handleTileInfoPacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean, EntityPlayer paramEntityPlayer)
  {
    int i = paramPacketCoFHBase.getByte();
    if (i == 0) {
      handleInfoPacket(paramPacketCoFHBase, paramBoolean, paramEntityPlayer);
    } else if ((i >= 1) && (i <= 6)) {
      attachments[(i - 1)].handleInfoPacket(paramPacketCoFHBase, paramBoolean, paramEntityPlayer);
    }
  }
  
  public void handleTilePacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      for (int i = 0; i < neighborTypes.length; i = (byte)(i + 1))
      {
        neighborTypes[i] = NeighborTypes.values()[paramPacketCoFHBase.getByte()];
        connectionTypes[i] = ConnectionTypes.values()[paramPacketCoFHBase.getByte()];
      }
      isNode = paramPacketCoFHBase.getBool();
      
      i = paramPacketCoFHBase.getByte();
      for (byte b = 0; b < 6; b = (byte)(b + 1)) {
        if ((i & 1 << b) != 0)
        {
          attachments[b] = AttachmentRegistry.createAttachment(this, b, paramPacketCoFHBase.getByte());
          attachments[b].getDescriptionFromPacket(paramPacketCoFHBase);
        }
        else
        {
          attachments[b] = null;
        }
      }
      facadeMask = paramPacketCoFHBase.getByte();
      for (b = 0; b < 6; b = (byte)(b + 1)) {
        if ((facadeMask & 1 << b) != 0)
        {
          covers[b] = new Cover(this, b);
          covers[b].getDescriptionFromPacket(paramPacketCoFHBase);
        }
        else
        {
          covers[b] = null;
        }
      }
      recalcFacadeMask();
      
      hashCode = paramPacketCoFHBase.getInt();
      
      worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
      
      lastUpdateTime = worldObj.getTotalWorldTime();
    }
  }
  
  public TileTDBase()
  {
    isValid = true;
    isNode = false;
    
    neighborMultiBlocks = new IMultiBlock[ForgeDirection.VALID_DIRECTIONS.length];
    neighborTypes = new NeighborTypes[] { NeighborTypes.NONE, NeighborTypes.NONE, NeighborTypes.NONE, NeighborTypes.NONE, NeighborTypes.NONE, NeighborTypes.NONE };
    
    connectionTypes = new ConnectionTypes[] { ConnectionTypes.NORMAL, ConnectionTypes.NORMAL, ConnectionTypes.NORMAL, ConnectionTypes.NORMAL, ConnectionTypes.NORMAL, ConnectionTypes.NORMAL, ConnectionTypes.BLOCKED };
    
    internalSideCounter = 0;
    
    subTiles = blankSubTiles;
    lastUpdateTime = -1L;
    hashCode = 0;
    
    neighbourChunks = new LinkedList();
    
    duct = null;
    
    isOutput = false;
    isInput = false;
  }
  
  public BlockDuct.ConnectionTypes getRenderConnectionType(int paramInt)
  {
    return getDefaultConnectionType(neighborTypes[paramInt], connectionTypes[paramInt]);
  }
  
  public static BlockDuct.ConnectionTypes getDefaultConnectionType(NeighborTypes paramNeighborTypes, ConnectionTypes paramConnectionTypes)
  {
    if (paramNeighborTypes == NeighborTypes.STRUCTURE) {
      return BlockDuct.ConnectionTypes.STRUCTURE;
    }
    if (paramNeighborTypes == NeighborTypes.INPUT) {
      return BlockDuct.ConnectionTypes.DUCT;
    }
    if (paramNeighborTypes == NeighborTypes.NONE)
    {
      if (paramConnectionTypes == ConnectionTypes.FORCED) {
        return BlockDuct.ConnectionTypes.DUCT;
      }
      return BlockDuct.ConnectionTypes.NONE;
    }
    if ((paramConnectionTypes == ConnectionTypes.BLOCKED) || (paramConnectionTypes == ConnectionTypes.REJECTED)) {
      return BlockDuct.ConnectionTypes.NONE;
    }
    if (paramNeighborTypes == NeighborTypes.OUTPUT) {
      return BlockDuct.ConnectionTypes.TILECONNECTION;
    }
    return BlockDuct.ConnectionTypes.DUCT;
  }
  
  public boolean canUpdate()
  {
    return false;
  }
  
  public boolean renderAdditional(int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    return false;
  }
  
  public boolean isSubNode()
  {
    return false;
  }
  
  public IIcon getBaseIcon()
  {
    return getDuctTypeiconBaseTexture;
  }
  
  public ItemStack getDrop()
  {
    return new ItemStack(func_145838_q(), 1, func_145832_p());
  }
  
  @SideOnly(Side.CLIENT)
  public CoverHoleRender.ITransformer[] getHollowMask(byte paramByte)
  {
    BlockDuct.ConnectionTypes localConnectionTypes = getRenderConnectionType(paramByte);
    if (localConnectionTypes == BlockDuct.ConnectionTypes.TILECONNECTION) {
      return CoverHoleRender.hollowDuctTile;
    }
    if (localConnectionTypes == BlockDuct.ConnectionTypes.NONE) {
      return null;
    }
    return CoverHoleRender.hollowDuct;
  }
  
  public static enum NeighborTypes
  {
    NONE,  MULTIBLOCK,  OUTPUT(true),  INPUT(true),  STRUCTURE(true),  DUCT_ATTACHMENT;
    
    public final boolean attachedToNeightbour;
    
    private NeighborTypes()
    {
      this(false);
    }
    
    private NeighborTypes(boolean paramBoolean)
    {
      attachedToNeightbour = paramBoolean;
    }
  }
  
  public static enum ConnectionTypes
  {
    NORMAL(true),  ONEWAY(true),  REJECTED(false),  BLOCKED(false),  FORCED(true);
    
    public final boolean allowTransfer;
    
    private ConnectionTypes(boolean paramBoolean)
    {
      allowTransfer = paramBoolean;
    }
    
    public ConnectionTypes next()
    {
      if (this == NORMAL) {
        return BLOCKED;
      }
      return NORMAL;
    }
  }
  
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox()
  {
    return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
  }
  
  public String getDataType()
  {
    return "tile.thermaldynamics.duct";
  }
  
  public void cofh_invalidate()
  {
    markChunkDirty();
  }
  
  public boolean shouldRenderCustomHitBox(int paramInt, EntityPlayer paramEntityPlayer)
  {
    return (paramInt == 13) || ((paramInt > 5) && (paramInt < 13) && (!Utils.isHoldingUsableWrench(paramEntityPlayer, xCoord, yCoord, zCoord)));
  }
  
  public CustomHitBox getCustomHitBox(int paramInt, EntityPlayer paramEntityPlayer)
  {
    double d1 = getDuctType().isLargeTube() ? 0.075D : 0.3D;
    double d2 = 1.0D - d1 * 2.0D;
    
    CustomHitBox localCustomHitBox = new CustomHitBox(d2, d2, d2, xCoord + d1, yCoord + d1, zCoord + d1);
    for (int i = 0; i < neighborTypes.length; i++) {
      if (neighborTypes[i] == NeighborTypes.MULTIBLOCK)
      {
        localCustomHitBox.drawSide(i, true);
        localCustomHitBox.setSideLength(i, d1);
      }
      else if (neighborTypes[i] != NeighborTypes.NONE)
      {
        localCustomHitBox.drawSide(i, true);
        localCustomHitBox.setSideLength(i, 0.04D);
      }
    }
    return localCustomHitBox;
  }
  
  public void readPortableData(EntityPlayer paramEntityPlayer, NBTTagCompound paramNBTTagCompound)
  {
    if (!paramNBTTagCompound.hasKey("AttachmentType", 8)) {
      return;
    }
    MovingObjectPosition localMovingObjectPosition = RayTracer.retraceBlock(worldObj, paramEntityPlayer, xCoord, yCoord, zCoord);
    if (localMovingObjectPosition == null) {
      return;
    }
    int i = subHit;
    if ((i <= 13) || (i >= 20)) {
      return;
    }
    if (!(attachments[(i - 14)] instanceof IPortableData)) {
      return;
    }
    IPortableData localIPortableData = (IPortableData)attachments[(i - 14)];
    if (paramNBTTagCompound.getString("AttachmentType").equals(localIPortableData.getDataType())) {
      localIPortableData.readPortableData(paramEntityPlayer, paramNBTTagCompound);
    }
  }
  
  public void writePortableData(EntityPlayer paramEntityPlayer, NBTTagCompound paramNBTTagCompound)
  {
    MovingObjectPosition localMovingObjectPosition = RayTracer.retraceBlock(worldObj, paramEntityPlayer, xCoord, yCoord, zCoord);
    if (localMovingObjectPosition == null) {
      return;
    }
    int i = subHit;
    if ((i <= 13) || (i >= 20)) {
      return;
    }
    if (!(attachments[(i - 14)] instanceof IPortableData)) {
      return;
    }
    IPortableData localIPortableData = (IPortableData)attachments[(i - 14)];
    localIPortableData.writePortableData(paramEntityPlayer, paramNBTTagCompound);
    if (!paramNBTTagCompound.hasNoTags()) {
      paramNBTTagCompound.setString("AttachmentType", localIPortableData.getDataType());
    }
  }
  
  public void getTileInfo(List<IChatComponent> paramList, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer, boolean paramBoolean)
  {
    MultiBlockGrid localMultiBlockGrid = getGrid();
    if (localMultiBlockGrid != null)
    {
      paramList.add(new ChatComponentTranslation("info.thermaldynamics.info.duct", new Object[0]));
      localMultiBlockGrid.addInfo(paramList, paramEntityPlayer, paramBoolean);
      if (subTiles.length != 0) {
        for (Object localObject2 : subTiles) {
          if (grid != null) {
            grid.addInfo(paramList, paramEntityPlayer, paramBoolean);
          }
        }
      }
    }
  }
  
  public Object getConfigGuiServer(InventoryPlayer paramInventoryPlayer)
  {
    return null;
  }
  
  public Object getConfigGuiClient(InventoryPlayer paramInventoryPlayer)
  {
    return null;
  }
  
  public void tileUnloading() {}
  
  public abstract MultiBlockGrid getNewGrid();
  
  public void cacheInputTile(TileEntity paramTileEntity, int paramInt) {}
  
  public void cacheStructural(TileEntity paramTileEntity, int paramInt) {}
  
  public void doDebug(EntityPlayer paramEntityPlayer) {}
  
  public void handleInfoPacket(PacketCoFHBase paramPacketCoFHBase, boolean paramBoolean, EntityPlayer paramEntityPlayer) {}
  
  public abstract boolean cachesExist();
  
  public abstract void createCaches();
  
  public abstract void cacheImportant(TileEntity paramTileEntity, int paramInt);
  
  public abstract void clearCache(int paramInt);
  
  public void randomDisplayTick() {}
  
  public void onPlacedBy(EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack) {}
  
  public void dropAdditional(ArrayList<ItemStack> paramArrayList) {}
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\block\TileTDBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */