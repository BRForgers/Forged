package cofh.thermaldynamics.duct.entity;

import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockGridWithRoutes;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class TransportGrid
  extends MultiBlockGridWithRoutes
{
  public TransportGrid(World paramWorld)
  {
    super(paramWorld);
  }
  
  public TransportGrid()
  {
    this(DimensionManager.getWorld(0));
  }
  
  public void tickGrid()
  {
    super.tickGrid();
  }
  
  public boolean canAddBlock(IMultiBlock paramIMultiBlock)
  {
    return paramIMultiBlock instanceof TileTransportDuctBaseRoute;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TransportGrid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */