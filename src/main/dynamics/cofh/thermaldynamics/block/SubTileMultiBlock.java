package cofh.thermaldynamics.block;

import cofh.lib.util.helpers.ServerHelper;
import cofh.thermaldynamics.core.TickHandler;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockFormer;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class SubTileMultiBlock
  implements IMultiBlock
{
  public MultiBlockGrid grid;
  private boolean isValid = true;
  public TileTDBase parent;
  
  public SubTileMultiBlock(TileTDBase paramTileTDBase)
  {
    parent = paramTileTDBase;
  }
  
  public World world()
  {
    return parent.world();
  }
  
  public int x()
  {
    return parent.xCoord;
  }
  
  public int y()
  {
    return parent.yCoord + 1;
  }
  
  public int z()
  {
    return parent.zCoord;
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
  
  public abstract MultiBlockGrid getNewGrid();
  
  public void setGrid(MultiBlockGrid paramMultiBlockGrid)
  {
    grid = paramMultiBlockGrid;
  }
  
  public MultiBlockGrid getGrid()
  {
    return grid;
  }
  
  public IMultiBlock getConnectedSide(byte paramByte)
  {
    IMultiBlock localIMultiBlock1 = parent.getConnectedSide(paramByte);
    if (localIMultiBlock1.getClass() != parent.getClass()) {
      return null;
    }
    IMultiBlock[] arrayOfIMultiBlock1 = localIMultiBlock1.getSubTiles();
    if (arrayOfIMultiBlock1 != null) {
      for (IMultiBlock localIMultiBlock2 : arrayOfIMultiBlock1) {
        if (sameType(localIMultiBlock2)) {
          return localIMultiBlock2;
        }
      }
    }
    return null;
  }
  
  public boolean isBlockedSide(int paramInt)
  {
    return parent.isBlockedSide(paramInt);
  }
  
  public boolean sameType(IMultiBlock paramIMultiBlock)
  {
    return paramIMultiBlock.getClass() == getClass();
  }
  
  public boolean isSideConnected(byte paramByte)
  {
    return (parent.isSideConnected(paramByte)) && (getConnectedSide(paramByte) != null);
  }
  
  public void setNotConnected(byte paramByte) {}
  
  public void tickMultiBlock()
  {
    if ((!parent.func_145837_r()) && (grid == null) && (ServerHelper.isServerWorld(parent.world())))
    {
      onNeighbourChange();
      formGrid();
    }
  }
  
  public void formGrid()
  {
    if ((grid == null) && (ServerHelper.isServerWorld(parent.world()))) {
      new MultiBlockFormer().formGrid(this);
    }
  }
  
  public boolean tickPass(int paramInt)
  {
    return false;
  }
  
  public boolean isNode()
  {
    return false;
  }
  
  public void tileUnloading() {}
  
  public boolean existsYet()
  {
    return parent.existsYet();
  }
  
  public void readFromNBT(NBTTagCompound paramNBTTagCompound)
  {
    TickHandler.addMultiBlockToCalculate(this);
  }
  
  public static final IMultiBlock[] BLANK = new IMultiBlock[0];
  
  public void writeToNBT(NBTTagCompound paramNBTTagCompound) {}
  
  public final IMultiBlock[] getSubTiles()
  {
    return BLANK;
  }
  
  public void onChunkUnload()
  {
    if (grid != null)
    {
      tileUnloading();
      grid.removeBlock(this);
    }
  }
  
  public void invalidate()
  {
    if (grid != null) {
      grid.removeBlock(this);
    }
  }
  
  public void onNeighbourChange() {}
  
  public void destroyAndRecreate()
  {
    if (grid != null) {
      grid.destroyAndRecreate();
    }
  }
  
  public void addRelays() {}
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\block\SubTileMultiBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */