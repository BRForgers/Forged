package cofh.thermaldynamics.gui.slot;

import cofh.api.item.ISpecialFilterFluid;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermaldynamics.duct.attachments.filter.IFilterConfig;
import net.minecraft.item.ItemStack;

public class SlotFilterFluid
  extends SlotFilter
{
  public SlotFilterFluid(IFilterConfig paramIFilterConfig, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramIFilterConfig, paramInt1, paramInt2, paramInt3);
  }
  
  public void func_75215_d(ItemStack paramItemStack)
  {
    if ((paramItemStack == null) || (func_75214_a(paramItemStack))) {
      super.func_75215_d(paramItemStack);
    }
  }
  
  public boolean func_75214_a(ItemStack paramItemStack)
  {
    return (paramItemStack != null) && ((FluidHelper.getFluidForFilledItem(paramItemStack) != null) || ((paramItemStack.getItem() instanceof ISpecialFilterFluid)));
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\slot\SlotFilterFluid.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */