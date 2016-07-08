package cofh.thermaldynamics.duct.entity;

import cofh.CoFHCore;
import cofh.core.Proxy;
import cofh.repack.codechicken.lib.raytracer.IndexedCuboid6;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public abstract class TileTransportDuctBase
  extends TileTDBase
{
  public boolean cachesExist()
  {
    return true;
  }
  
  public void createCaches() {}
  
  public void cacheImportant(TileEntity paramTileEntity, int paramInt) {}
  
  public void clearCache(int paramInt) {}
  
  public void addTraceableCuboids(List<IndexedCuboid6> paramList)
  {
    EntityPlayer localEntityPlayer = CoFHCore.proxy.getClientPlayer();
    if ((localEntityPlayer != null) && (ridingEntity != null) && (ridingEntity.getClass() == EntityTransport.class)) {
      return;
    }
    super.addTraceableCuboids(paramList);
  }
  
  public abstract boolean advanceEntity(EntityTransport paramEntityTransport);
  
  public IMultiBlock getPhysicalConnectedSide(byte paramByte)
  {
    return super.getConnectedSide(paramByte);
  }
  
  public boolean advanceEntityClient(EntityTransport paramEntityTransport)
  {
    EntityTransport tmp1_0 = paramEntityTransport;10progress = ((byte)(10progress + step));
    if ((progress >= 100) && 
      (!paramEntityTransport.trySimpleAdvance())) {
      return true;
    }
    return false;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\TileTransportDuctBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */