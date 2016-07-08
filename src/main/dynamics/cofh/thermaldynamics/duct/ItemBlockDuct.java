package cofh.thermaldynamics.duct;

import cofh.core.item.ItemBlockBase;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermaldynamics.duct.energy.subgrid.SubTileEnergyRedstone;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public class ItemBlockDuct
  extends ItemBlockBase
{
  int offset;
  
  public ItemBlockDuct(Block paramBlock)
  {
    super(paramBlock);
    offset = offset;
  }
  
  public String func_77667_c(ItemStack paramItemStack)
  {
    return TDDucts.isValid(id(paramItemStack)) ? "tile.thermaldynamics.duct." + getTypeidunlocalizedName : super.func_77667_c(paramItemStack);
  }
  
  public String func_77653_i(ItemStack paramItemStack)
  {
    if (TDDucts.isValid(id(paramItemStack)))
    {
      Duct localDuct = TDDucts.getType(id(paramItemStack));
      
      String str1 = null;String str2 = null;
      String str3 = func_77657_g(paramItemStack);
      if (opaque) {
        if (StatCollector.canTranslate(str3 + ".opaque.name")) {
          str3 = str3 + ".opaque";
        } else {
          str1 = "tile.thermaldynamics.duct.opaque.name";
        }
      }
      if (((localDuct instanceof DuctItem)) && (stackTagCompound != null)) {
        if (stackTagCompound.getByte("DenseType") == 1)
        {
          if (StatCollector.canTranslate(str3 + ".dense.name")) {
            str3 = str3 + ".dense";
          } else {
            str2 = "tile.thermaldynamics.duct.dense.name";
          }
        }
        else if (stackTagCompound.getByte("DenseType") == 2) {
          if (StatCollector.canTranslate(str3 + ".vacuum.name")) {
            str3 = str3 + ".vacuum";
          } else {
            str2 = "tile.thermaldynamics.duct.vacuum.name";
          }
        }
      }
      String str4 = StringHelper.localize(str3 + ".name");
      if (str1 != null) {
        str4 = StatCollector.translateToLocalFormatted(str1, new Object[] { str4 });
      }
      if (str2 != null) {
        str4 = StatCollector.translateToLocalFormatted(str2, new Object[] { str4 });
      }
      return str4;
    }
    return super.func_77653_i(paramItemStack);
  }
  
  public int id(ItemStack paramItemStack)
  {
    return offset + paramItemStack.getItemDamage();
  }
  
  public EnumRarity func_77613_e(ItemStack paramItemStack)
  {
    int i = id(paramItemStack);
    if (TDDucts.isValid(i)) {
      return getTyperarity;
    }
    return EnumRarity.uncommon;
  }
  
  public void func_77624_a(ItemStack paramItemStack, EntityPlayer paramEntityPlayer, List paramList, boolean paramBoolean)
  {
    super.func_77624_a(paramItemStack, paramEntityPlayer, paramList, paramBoolean);
    if ((StringHelper.displayShiftForDetail) && (!StringHelper.isShiftKeyDown())) {
      paramList.add(StringHelper.shiftForDetails());
    }
    if (!StringHelper.isShiftKeyDown())
    {
      if (ItemHelper.itemsIdentical(paramItemStack, structureitemStack)) {
        paramList.add(StringHelper.getInfoText("info.thermaldynamics.duct.cover"));
      }
      return;
    }
    int i = id(paramItemStack);
    if (!TDDucts.isValid(i)) {
      return;
    }
    Duct localDuct = TDDucts.getType(i);
    switch (localDuct.ductType)
    {
    case TRANSPORT: 
      paramList.add(StringHelper.localize("info.thermaldynamics.duct.transport"));
      if (localDuct == TDDucts.transportLongRange) {
        paramList.add(StringHelper.getInfoText("info.thermaldynamics.duct.transportLongRange"));
      } else if (localDuct == TDDucts.transportCrossover) {
        paramList.add(StringHelper.getInfoText("info.thermaldynamics.duct.transportCrossover"));
      }
      break;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\ItemBlockDuct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */