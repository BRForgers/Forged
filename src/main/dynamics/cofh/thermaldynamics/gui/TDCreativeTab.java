package cofh.thermaldynamics.gui;

import cofh.CoFHCore;
import cofh.core.Proxy;
import cofh.core.util.CoreUtils;
import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cofh.thermaldynamics.duct.Duct;
import cofh.thermaldynamics.duct.DuctItem;
import cofh.thermaldynamics.duct.ItemBlockDuct;
import cofh.thermaldynamics.duct.TDDucts;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TDCreativeTab
  extends CreativeTabs
{
  int iconIndex = 0;
  TimeTracker iconTracker = new TimeTracker();
  
  public TDCreativeTab()
  {
    super("ThermalDynamics");
  }
  
  @SideOnly(Side.CLIENT)
  public ItemStack getIconItemStack()
  {
    updateIcon();
    return getDucticonIndex).itemStack;
  }
  
  @SideOnly(Side.CLIENT)
  public Item getTabIconItem()
  {
    return getIconItemStack().getItem();
  }
  
  @SideOnly(Side.CLIENT)
  public String getTabLabel()
  {
    return "thermaldynamics.creativeTab";
  }
  
  private void updateIcon()
  {
    World localWorld = CoFHCore.proxy.getClientWorld();
    if ((CoreUtils.isClient()) && (iconTracker.hasDelayPassed(localWorld, 80)))
    {
      int i = MathHelper.RANDOM.nextInt(TDDucts.ductList.size() - 1);
      iconIndex = (i >= iconIndex ? i + 1 : i);
      iconTracker.markTime(localWorld);
    }
  }
  
  public void displayAllReleventItems(List paramList)
  {
    LinkedList localLinkedList = new LinkedList();
    super.displayAllReleventItems(localLinkedList);
    for (Iterator localIterator = TDDucts.getSortedDucts().iterator(); localIterator.hasNext();)
    {
      localObject = (Duct)localIterator.next();
      paramList.add(itemStack.copy());
      if ((localObject instanceof DuctItem))
      {
        paramList.add(((DuctItem)localObject).getDenseItemStack());
        paramList.add(((DuctItem)localObject).getVacuumItemStack());
      }
    }
    Object localObject;
    for (localIterator = localLinkedList.iterator(); localIterator.hasNext();)
    {
      localObject = (ItemStack)localIterator.next();
      if (!(((ItemStack)localObject).getItem() instanceof ItemBlockDuct)) {
        paramList.add(localObject);
      }
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\TDCreativeTab.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */