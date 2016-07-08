package cofh.thermaldynamics.gui.container;

import cofh.lib.util.helpers.ItemHelper;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.gui.slot.SlotIcon;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTransportConfig
  extends ContainerTDBase
{
  private final InventoryPlayer inventory;
  private final TileTransportDuct transportDuct;
  
  public ContainerTransportConfig(InventoryPlayer paramInventoryPlayer, TileTransportDuct paramTileTransportDuct)
  {
    inventory = paramInventoryPlayer;
    transportDuct = paramTileTransportDuct;
    
    addPlayerInventory(paramInventoryPlayer);
    
    addSlotToContainer(new SlotIcon(8, 15, paramTileTransportDuct));
  }
  
  protected void addPlayerInventory(InventoryPlayer paramInventoryPlayer)
  {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(paramInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 53 + i * 18));
      }
    }
    for (i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(paramInventoryPlayer, i, 8 + i * 18, 111));
    }
  }
  
  public boolean canInteractWith(EntityPlayer paramEntityPlayer)
  {
    return true;
  }
  
  public ItemStack transferStackInSlot(EntityPlayer paramEntityPlayer, int paramInt)
  {
    Slot localSlot1 = (Slot)inventorySlots.get(paramInt);
    
    int i = 27;
    int j = i + 9;
    int k = j + 1;
    if ((localSlot1 != null) && (localSlot1.getHasStack()))
    {
      ItemStack localItemStack = localSlot1.getStack();
      if (paramInt < 0) {
        return null;
      }
      if (paramInt < j)
      {
        Object localObject = null;
        for (int m = j; m < k; m++)
        {
          Slot localSlot2 = (Slot)inventorySlots.get(m);
          if (!localSlot2.getHasStack())
          {
            if (localObject == null) {
              localObject = localSlot2;
            }
          }
          else if (ItemHelper.itemsEqualWithMetadata(localSlot2.getStack(), localItemStack)) {
            return null;
          }
        }
        if (localObject != null) {
          ((Slot)localObject).putStack(localItemStack.copy());
        }
        return null;
      }
      localSlot1.putStack(null);
      localSlot1.onSlotChanged();
    }
    return null;
  }
  
  protected int numTileSlots()
  {
    return 1;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\container\ContainerTransportConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */