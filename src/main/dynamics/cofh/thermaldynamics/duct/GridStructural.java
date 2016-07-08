package cofh.thermaldynamics.duct;

import cofh.thermaldynamics.core.WorldGridList;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import net.minecraft.world.World;

public class GridStructural
  extends MultiBlockGrid
{
  public GridStructural(WorldGridList paramWorldGridList)
  {
    super(paramWorldGridList);
  }
  
  public GridStructural(World paramWorld)
  {
    super(paramWorld);
  }
  
  public boolean canAddBlock(IMultiBlock paramIMultiBlock)
  {
    return true;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\GridStructural.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */