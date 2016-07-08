package cofh.thermaldynamics.debughelper;

import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;
import java.util.HashSet;
import net.minecraft.entity.player.EntityPlayer;

public class PacketDebug
  extends PacketCoFHBase
{
  public static void initialize()
  {
    PacketHandler.instance.registerPacket(PacketDebug.class);
  }
  
  public PacketDebug() {}
  
  public PacketDebug(int[] paramArrayOfInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      addInt(paramArrayOfInt[i]);
    }
  }
  
  public void handleClientSide(EntityPlayer paramEntityPlayer)
  {
    int[] arrayOfInt = new int[DebugTickHandler.DebugEvent.n];
    for (int i = 0; i < DebugTickHandler.DebugEvent.n; i++) {
      arrayOfInt[i] = getInt();
    }
  }
  
  public void handlePacket(EntityPlayer paramEntityPlayer, boolean paramBoolean)
  {
    if ((paramBoolean) && 
      (!DebugTickHandler.debugPlayers.remove(paramEntityPlayer))) {
      DebugTickHandler.debugPlayers.add(paramEntityPlayer);
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\debughelper\PacketDebug.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */