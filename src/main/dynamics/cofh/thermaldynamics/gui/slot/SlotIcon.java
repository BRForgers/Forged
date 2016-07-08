package cofh.thermaldynamics.gui.slot;

import cofh.lib.gui.slot.SlotFalseCopy;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuct.OutputData;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class SlotIcon
  extends SlotFalseCopy
{
  static final IInventory inv = new InventoryBasic("[FALSE]", false, 0);
  private final TileTransportDuct duct;
  
  public SlotIcon(int paramInt1, int paramInt2, TileTransportDuct paramTileTransportDuct)
  {
    super(inv, 0, paramInt1, paramInt2);
    duct = paramTileTransportDuct;
  }
  
  public ItemStack func_75211_c()
  {
    return ItemStack.copyItemStack(duct.data.item);
  }
  
  public void func_75215_d(ItemStack paramItemStack)
  {
    if (paramItemStack != null) {
      stackSize = 1;
    }
    duct.setIcon(paramItemStack);
  }
  
  public void func_75218_e() {}
  
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


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\slot\SlotIcon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */