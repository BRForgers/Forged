package cofh.thermaldynamics.gui.container;

import cofh.core.network.PacketHandler;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuctBaseRoute;
import cofh.thermaldynamics.gui.client.DirectoryEntry;
import cofh.thermaldynamics.multiblock.RouteCache;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerTransport
  extends Container
{
  public final TileTransportDuct transportDuct;
  public DirectoryEntry directoryEntry;
  RouteCache cache;
  public ArrayList<DirectoryEntry> directory;
  
  public ContainerTransport(TileTransportDuct paramTileTransportDuct)
  {
    transportDuct = paramTileTransportDuct;
  }
  
  public boolean canInteractWith(EntityPlayer paramEntityPlayer)
  {
    return (!transportDuct.func_145837_r()) && ((transportDuct.isOutput()) || (transportDuct.world().isRemote));
  }
  
  public void addCraftingToCrafters(ICrafting paramICrafting)
  {
    super.addCraftingToCrafters(paramICrafting);
    if ((paramICrafting instanceof EntityPlayerMP))
    {
      PacketHandler.sendTo(transportDuct.getDirectoryPacket(), (EntityPlayerMP)paramICrafting);
      cache = transportDuct.getCache();
    }
  }
  
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();
    if ((!crafters.isEmpty()) && (
      (cache == null) || (cache.invalid))) {
      if (transportDuct.internalGrid == null)
      {
        cache = null;
      }
      else
      {
        cache = transportDuct.getCache();
        for (Object localObject : crafters) {
          if ((localObject instanceof EntityPlayerMP)) {
            PacketHandler.sendTo(transportDuct.getDirectoryPacket(), (EntityPlayerMP)localObject);
          }
        }
      }
    }
  }
  
  public Comparator<DirectoryEntry> blockDist = new Comparator()
  {
    public int compare(DirectoryEntry paramAnonymousDirectoryEntry1, DirectoryEntry paramAnonymousDirectoryEntry2)
    {
      return compareStrings(name, name);
    }
  };
  
  public int compareDists(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = compareInts(paramInt1 * paramInt1 + paramInt2 * paramInt2 + paramInt3 * paramInt3, paramInt4 * paramInt4 + paramInt5 * paramInt5 + paramInt6 * paramInt6);
    if (i != 0) {
      return i;
    }
    i = compareInts(paramInt2, paramInt5);
    if (i != 0) {
      return i;
    }
    i = compareInts(paramInt1, paramInt4);
    if (i != 0) {
      return i;
    }
    return compareInts(paramInt3, paramInt6);
  }
  
  public int compareStrings(String paramString1, String paramString2)
  {
    return paramString2 == null ? 1 : paramString1 == null ? -1 : paramString2 == null ? 0 : paramString1.compareTo(paramString2);
  }
  
  public static int compareInts(int paramInt1, int paramInt2)
  {
    return paramInt1 == paramInt2 ? 0 : paramInt1 < paramInt2 ? -1 : 1;
  }
  
  boolean needsResort = false;
  
  public void setDirectory(ArrayList<DirectoryEntry> paramArrayList)
  {
    directory = paramArrayList;
    Collections.sort(directory, blockDist);
    needsResort = true;
  }
  
  public ItemStack transferStackInSlot(EntityPlayer paramEntityPlayer, int paramInt)
  {
    return null;
  }
  
  public void setEntry(DirectoryEntry paramDirectoryEntry)
  {
    directoryEntry = paramDirectoryEntry;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\container\ContainerTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */