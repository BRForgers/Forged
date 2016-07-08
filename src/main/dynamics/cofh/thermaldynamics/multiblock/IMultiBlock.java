package cofh.thermaldynamics.multiblock;

import net.minecraft.world.World;

public abstract interface IMultiBlock
{
  public abstract World world();
  
  public abstract int x();
  
  public abstract int y();
  
  public abstract int z();
  
  public abstract void setInvalidForForming();
  
  public abstract void setValidForForming();
  
  public abstract boolean isValidForForming();
  
  public abstract MultiBlockGrid getNewGrid();
  
  public abstract void setGrid(MultiBlockGrid paramMultiBlockGrid);
  
  public abstract MultiBlockGrid getGrid();
  
  public abstract IMultiBlock getConnectedSide(byte paramByte);
  
  public abstract boolean isBlockedSide(int paramInt);
  
  public abstract boolean isSideConnected(byte paramByte);
  
  public abstract void setNotConnected(byte paramByte);
  
  public abstract void tickMultiBlock();
  
  public abstract boolean tickPass(int paramInt);
  
  public abstract boolean isNode();
  
  public abstract boolean existsYet();
  
  public abstract IMultiBlock[] getSubTiles();
  
  public abstract void onNeighborBlockChange();
  
  public abstract void addRelays();
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\IMultiBlock.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */