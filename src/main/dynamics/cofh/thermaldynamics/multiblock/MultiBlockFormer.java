package cofh.thermaldynamics.multiblock;

import java.util.LinkedList;
import java.util.Queue;
import net.minecraftforge.common.util.ForgeDirection;

public class MultiBlockFormer
{
  Queue<IMultiBlock> blocksToCheck = new LinkedList();
  MultiBlockGrid theGrid;
  
  public void formGrid(IMultiBlock paramIMultiBlock)
  {
    theGrid = paramIMultiBlock.getNewGrid();
    paramIMultiBlock.setGrid(theGrid);
    theGrid.addBlock(paramIMultiBlock);
    
    blocksToCheck.add(paramIMultiBlock);
    while (!blocksToCheck.isEmpty()) {
      checkMultiBlock((IMultiBlock)blocksToCheck.remove());
    }
    paramIMultiBlock.getGrid().resetMultiBlocks();
  }
  
  private void checkMultiBlock(IMultiBlock paramIMultiBlock)
  {
    if (!paramIMultiBlock.isValidForForming()) {
      return;
    }
    paramIMultiBlock.onNeighborBlockChange();
    paramIMultiBlock.setInvalidForForming();
    int j;
    for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; j = (byte)(i + 1)) {
      if (paramIMultiBlock.isSideConnected(i))
      {
        IMultiBlock localIMultiBlock = paramIMultiBlock.getConnectedSide(i);
        if ((localIMultiBlock != null) && (localIMultiBlock.isValidForForming())) {
          if ((localIMultiBlock.getGrid() == null) && (theGrid.canAddBlock(localIMultiBlock)))
          {
            localIMultiBlock.setGrid(theGrid);
            theGrid.addBlock(localIMultiBlock);
            blocksToCheck.add(localIMultiBlock);
          }
          else if ((theGrid.canAddBlock(localIMultiBlock)) && (localIMultiBlock.getGrid() != null) && (theGrid.canGridsMerge(localIMultiBlock.getGrid())))
          {
            if (theGrid != localIMultiBlock.getGrid()) {
              if (theGrid.size() >= localIMultiBlock.getGrid().size())
              {
                theGrid.mergeGrids(localIMultiBlock.getGrid());
              }
              else
              {
                localIMultiBlock.getGrid().mergeGrids(theGrid);
                theGrid = localIMultiBlock.getGrid();
              }
            }
          }
          else
          {
            paramIMultiBlock.setNotConnected(i);
            localIMultiBlock.setNotConnected((byte)(i ^ 0x1));
          }
        }
      }
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\MultiBlockFormer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */