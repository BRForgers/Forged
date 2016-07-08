package cofh.thermaldynamics.duct;

import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import net.minecraft.tileentity.TileEntity;

public class TileStructuralDuct
  extends TileTDBase
{
  public boolean isConnectable(TileEntity paramTileEntity, int paramInt)
  {
    return (paramTileEntity != null) && (paramTileEntity.getClass() == getClass()) && (paramTileEntity.getBlockType() == func_145838_q()) && (paramTileEntity.getBlockMetadata() == func_145832_p());
  }
  
  public boolean cachesExist()
  {
    return true;
  }
  
  public void createCaches() {}
  
  public void cacheImportant(TileEntity paramTileEntity, int paramInt) {}
  
  public void clearCache(int paramInt) {}
  
  public MultiBlockGrid getNewGrid()
  {
    return new GridStructural(worldObj);
  }
  
  public boolean isStructureTile(TileEntity paramTileEntity, int paramInt)
  {
    return false;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\TileStructuralDuct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */