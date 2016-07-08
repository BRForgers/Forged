package cofh.thermaldynamics.gui.slot;

import cofh.lib.gui.slot.SlotFalseCopy;
import cofh.thermaldynamics.duct.attachments.filter.IFilterConfig;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class SlotFilter
  extends SlotFalseCopy
{
  IFilterConfig filter;
  public static final IInventory dummy = new InventoryBasic("Dummy", true, 0);
  
  public SlotFilter(IFilterConfig paramIFilterConfig, int paramInt1, int paramInt2, int paramInt3)
  {
    super(dummy, paramInt1, paramInt2, paramInt3);
    filter = paramIFilterConfig;
  }
  
  public boolean func_75214_a(ItemStack paramItemStack)
  {
    return paramItemStack != null;
  }
  
  public ItemStack func_75211_c()
  {
    return filter.getFilterStacks()[getSlotIndex()];
  }
  
  public void func_75215_d(ItemStack paramItemStack)
  {
    synchronized (filter.getFilterStacks())
    {
      if (paramItemStack != null) {
        stackSize = 1;
      }
      filter.getFilterStacks()[getSlotIndex()] = paramItemStack;
      func_75218_e();
    }
  }
  
  public void func_75218_e()
  {
    filter.onChange();
  }
  
  public int func_75219_a()
  {
    return 1;
  }
  
  public ItemStack func_75209_a(int paramInt)
  {
    return null;
  }
  
  public boolean func_75217_a(IInventory paramIInventory, int paramInt)
  {
    return false;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\slot\SlotFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */