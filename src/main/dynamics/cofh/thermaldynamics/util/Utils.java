package cofh.thermaldynamics.util;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Utils
{
  public static boolean isHoldingUsableWrench(EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3)
  {
    Item localItem = paramEntityPlayer.getCurrentEquippedItem() != null ? paramEntityPlayer.getCurrentEquippedItem().getItem() : null;
    if ((localItem instanceof IToolHammer)) {
      return ((IToolHammer)localItem).isUsable(paramEntityPlayer.getCurrentEquippedItem(), paramEntityPlayer, paramInt1, paramInt2, paramInt3);
    }
    if (bcWrenchExists) {
      return canHandleBCWrench(localItem, paramEntityPlayer, paramInt1, paramInt2, paramInt3);
    }
    return false;
  }
  
  public static void usedWrench(EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3)
  {
    Item localItem = paramEntityPlayer.getCurrentEquippedItem() != null ? paramEntityPlayer.getCurrentEquippedItem().getItem() : null;
    if ((localItem instanceof IToolHammer)) {
      ((IToolHammer)localItem).toolUsed(paramEntityPlayer.getCurrentEquippedItem(), paramEntityPlayer, paramInt1, paramInt2, paramInt3);
    } else if (bcWrenchExists) {
      bcWrenchUsed(localItem, paramEntityPlayer, paramInt1, paramInt2, paramInt3);
    }
  }
  
  private static boolean bcWrenchExists = classExists("buildcraft.api.tools.IToolWrench");
  
  private static boolean classExists(String paramString)
  {
    try
    {
      Class.forName(paramString);
      return true;
    }
    catch (Throwable localThrowable) {}
    return false;
  }
  
  private static boolean canHandleBCWrench(Item paramItem, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3)
  {
    return ((paramItem instanceof IToolWrench)) && (((IToolWrench)paramItem).canWrench(paramEntityPlayer, paramInt1, paramInt2, paramInt3));
  }
  
  private static void bcWrenchUsed(Item paramItem, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramItem instanceof IToolWrench)) {
      ((IToolWrench)paramItem).wrenchUsed(paramEntityPlayer, paramInt1, paramInt2, paramInt3);
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\util\Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */