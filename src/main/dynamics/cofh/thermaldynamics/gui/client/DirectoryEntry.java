package cofh.thermaldynamics.gui.client;

import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketTileInfo;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuct.OutputData;
import com.google.common.base.Strings;
import net.minecraft.item.ItemStack;

public final class DirectoryEntry
{
  public final String name;
  public final int x;
  public final int y;
  public final int z;
  public final ItemStack icon;
  
  public static void addDirectoryEntry(PacketTileInfo paramPacketTileInfo, TileTransportDuct paramTileTransportDuct)
  {
    paramPacketTileInfo.addString(data.name);
    paramPacketTileInfo.addInt(paramTileTransportDuct.x());
    paramPacketTileInfo.addInt(paramTileTransportDuct.y());
    paramPacketTileInfo.addInt(paramTileTransportDuct.z());
    paramPacketTileInfo.addItemStack(data.item);
  }
  
  public DirectoryEntry(PacketCoFHBase paramPacketCoFHBase)
  {
    this(paramPacketCoFHBase.getString(), paramPacketCoFHBase.getInt(), paramPacketCoFHBase.getInt(), paramPacketCoFHBase.getInt(), paramPacketCoFHBase.getItemStack());
  }
  
  public DirectoryEntry(String paramString, int paramInt1, int paramInt2, int paramInt3, ItemStack paramItemStack)
  {
    name = paramString;
    x = paramInt1;
    y = paramInt2;
    z = paramInt3;
    icon = paramItemStack;
  }
  
  public String toString()
  {
    return "DirectoryEntry{name='" + name + '\'' + ", x=" + x + ", y=" + y + ", z=" + z + ", icon=" + icon + '}';
  }
  
  public String getName()
  {
    if (Strings.isNullOrEmpty(name)) {
      return StringHelper.localize("info.thermaldynamics.transport.unnamed");
    }
    return name;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\client\DirectoryEntry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */