package cofh.thermaldynamics.duct;

import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuctCrossover;
import cofh.thermaldynamics.duct.entity.TileTransportDuctLongRange;
import net.minecraft.world.World;

public abstract class DuctFactory
{
  public static DuctFactory structural = new DuctFactory()
  {
    public TileTDBase createTileEntity(Duct paramAnonymousDuct, World paramAnonymousWorld)
    {
      return new TileStructuralDuct();
    }
  };
  public static DuctFactory transport = new DuctFactory()
  {
    public TileTDBase createTileEntity(Duct paramAnonymousDuct, World paramAnonymousWorld)
    {
      return new TileTransportDuct();
    }
  };
  public static DuctFactory transport_longrange = new DuctFactory()
  {
    public TileTDBase createTileEntity(Duct paramAnonymousDuct, World paramAnonymousWorld)
    {
      return new TileTransportDuctLongRange();
    }
  };
  public static DuctFactory transport_crossover = new DuctFactory()
  {
    public TileTDBase createTileEntity(Duct paramAnonymousDuct, World paramAnonymousWorld)
    {
      return new TileTransportDuctCrossover();
    }
  };
  
  public abstract TileTDBase createTileEntity(Duct paramDuct, World paramWorld);
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\DuctFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */