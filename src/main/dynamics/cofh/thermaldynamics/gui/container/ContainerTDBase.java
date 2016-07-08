package cofh.thermaldynamics.gui.container;

import cofh.lib.gui.slot.SlotFalseCopy;
import cofh.lib.util.helpers.ItemHelper;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ContainerTDBase
  extends Container
{
  protected void addPlayerInventory(InventoryPlayer paramInventoryPlayer)
  {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(paramInventoryPlayer, j + i * 9 + 9, 8 + j * 18, 123 + i * 18));
      }
    }
    for (i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(paramInventoryPlayer, i, 8 + i * 18, 181));
    }
  }
  
  public ItemStack slotClick(int paramInt1, int paramInt2, int paramInt3, EntityPlayer paramEntityPlayer)
  {
    Slot localSlot = paramInt1 < 0 ? null : (Slot)inventorySlots.get(paramInt1);
    if ((localSlot instanceof SlotFalseCopy))
    {
      if (paramInt2 == 2)
      {
        localSlot.putStack(null);
        localSlot.onSlotChanged();
      }
      else
      {
        localSlot.putStack(inventory.getItemStack() == null ? null : inventory.getItemStack().copy());
      }
      return inventory.getItemStack();
    }
    return super.slotClick(paramInt1, paramInt2, paramInt3, paramEntityPlayer);
  }
  
  protected boolean mergeItemStack(ItemStack paramItemStack, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    boolean bool = false;
    int i = paramBoolean ? paramInt2 - 1 : paramInt1;
    Slot localSlot;
    ItemStack localItemStack;
    if (paramItemStack.isStackable()) {
      while ((stackSize > 0) && (((!paramBoolean) && (i < paramInt2)) || ((paramBoolean) && (i >= paramInt1))))
      {
        localSlot = (Slot)inventorySlots.get(i);
        localItemStack = localSlot.getStack();
        if ((localSlot.isItemValid(paramItemStack)) && (ItemHelper.itemsEqualWithMetadata(paramItemStack, localItemStack, true)))
        {
          int j = stackSize + stackSize;
          int k = Math.min(paramItemStack.getMaxStackSize(), localSlot.getSlotStackLimit());
          if (j <= k)
          {
            stackSize = 0;
            stackSize = j;
            localSlot.onSlotChanged();
            bool = true;
          }
          else if (stackSize < k)
          {
            stackSize -= k - stackSize;
            stackSize = k;
            localSlot.onSlotChanged();
            bool = true;
          }
        }
        i += (paramBoolean ? -1 : 1);
      }
    }
    if (stackSize > 0)
    {
      i = paramBoolean ? paramInt2 - 1 : paramInt1;
      while (((!paramBoolean) && (i < paramInt2)) || ((paramBoolean) && (i >= paramInt1)))
      {
        localSlot = (Slot)inventorySlots.get(i);
        localItemStack = localSlot.getStack();
        if ((localSlot.isItemValid(paramItemStack)) && (localItemStack == null))
        {
          localSlot.putStack(ItemHelper.cloneStack(paramItemStack, Math.min(stackSize, localSlot.getSlotStackLimit())));
          localSlot.onSlotChanged();
          if (localSlot.getStack() == null) {
            break;
          }
          stackSize -= getStackstackSize;
          bool = true; break;
        }
        i += (paramBoolean ? -1 : 1);
      }
    }
    return bool;
  }
  
  public ItemStack transferStackInSlot(EntityPlayer paramEntityPlayer, int paramInt)
  {
    ItemStack localItemStack1 = null;
    Slot localSlot = (Slot)inventorySlots.get(paramInt);
    
    int i = 27;
    int j = i + 9;
    int k = j + numTileSlots();
    if ((localSlot != null) && (localSlot.getHasStack()))
    {
      ItemStack localItemStack2 = localSlot.getStack();
      localItemStack1 = localItemStack2.copy();
      if (paramInt < 0)
      {
        if (!mergeItemStack(localItemStack2, 0, j, true)) {
          return null;
        }
      }
      else if (paramInt < j)
      {
        if (!mergeItemStack(localItemStack2, j, k, false)) {
          return null;
        }
      }
      else if (!mergeItemStack(localItemStack2, 0, j, true)) {
        return null;
      }
      if (stackSize <= 0) {
        localSlot.putStack((ItemStack)null);
      } else {
        localSlot.onSlotChanged();
      }
      if (stackSize == stackSize) {
        return null;
      }
    }
    return localItemStack1;
  }
  
  protected abstract int numTileSlots();
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\container\ContainerTDBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */