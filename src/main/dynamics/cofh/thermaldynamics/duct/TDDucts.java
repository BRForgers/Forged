package cofh.thermaldynamics.duct;

import cofh.thermaldynamics.ThermalDynamics;
import cofh.thermaldynamics.debughelper.DebugHelper;
import cofh.thermaldynamics.duct.light.DuctLight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class TDDucts
{
  public static ArrayList<Duct> ductList = new ArrayList();
  public static ArrayList<Duct> ductListSorted = null;
  
  static Duct addDuct(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, String paramString1, Duct.Type paramType, DuctFactory paramDuctFactory, String paramString2, String paramString3, String paramString4, int paramInt4, String paramString5, String paramString6, int paramInt5)
  {
    Duct localDuct = new Duct(paramInt1, paramBoolean, paramInt2, paramInt3, paramString1, paramType, paramDuctFactory, paramString2, paramString3, paramString4, paramInt4, paramString5, paramString6, paramInt5);
    
    return registerDuct(localDuct);
  }
  
  static DuctItem addDuctItem(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, String paramString1, Duct.Type paramType, DuctFactory paramDuctFactory, String paramString2, String paramString3, String paramString4, int paramInt4, String paramString5, String paramString6, int paramInt5)
  {
    DuctItem localDuctItem = new DuctItem(paramInt1, paramBoolean, paramInt2, paramInt3, paramString1, paramType, paramDuctFactory, paramString2, paramString3, paramString4, paramInt4, paramString5, paramString6, paramInt5);
    
    return (DuctItem)registerDuct(localDuctItem);
  }
  
  static <T extends Duct> T registerDuct(T paramT)
  {
    int i = id;
    while (i >= ductList.size()) {
      ductList.add(null);
    }
    Duct localDuct = (Duct)ductList.set(i, paramT);
    if (localDuct != null) {
      ThermalDynamics.log.info("Replacing " + unlocalizedName + " with " + unlocalizedName);
    }
    return paramT;
  }
  
  public static Duct getDuct(int paramInt)
  {
    if (isValid(paramInt)) {
      return (Duct)ductList.get(paramInt);
    }
    return structure;
  }
  
  public static List<Duct> getSortedDucts()
  {
    if (ductListSorted == null)
    {
      ductListSorted = new ArrayList();
      for (Duct localDuct : ductList) {
        if (localDuct != null) {
          ductListSorted.add(localDuct);
        }
      }
      Collections.sort(ductListSorted, new Comparator()
      {
        public int compare(Duct paramAnonymousDuct1, Duct paramAnonymousDuct2)
        {
          int i = ductType.compareTo(ductType);
          if (i == 0) {
            i = paramAnonymousDuct1.compareTo(paramAnonymousDuct2);
          }
          return i;
        }
      });
    }
    return ductListSorted;
  }
  
  public static boolean isValid(int paramInt)
  {
    return (paramInt < ductList.size()) && (ductList.get(paramInt) != null);
  }
  
  public static Duct getType(int paramInt)
  {
    return ductList.get(paramInt) != null ? (Duct)ductList.get(paramInt) : structure;
  }
  
  public static boolean addDucts()
  {
    addTransportDucts();
  }
  
  static void addTransportDucts()
  {
    transportBasic = (DuctTransport)registerDuct(new DuctTransport(OFFSET_TRANSPORT + 0, false, 1, 4, "transport", Duct.Type.TRANSPORT, DuctFactory.transport, null, null, null, 255, "electrum", "thermaldynamics:duct/base/greenGlass", 96));
    
    transportLongRange = (DuctTransport)registerDuct(new DuctTransport(OFFSET_TRANSPORT + 1, false, 1, 4, "transportLongRange", Duct.Type.TRANSPORT, DuctFactory.transport_longrange, null, null, null, 255, "copper", "thermaldynamics:duct/base/greenGlass", 80));
    
    transportCrossover = (DuctTransport)registerDuct(new DuctTransport(OFFSET_TRANSPORT + 2, false, 1, 4, "transportAcceleration", Duct.Type.TRANSPORT, DuctFactory.transport_crossover, null, null, null, 255, "enderium", "thermaldynamics:duct/base/greenGlass", 128));
    
    transportFrame = (DuctTransport)registerDuct(new DuctTransport(OFFSET_TRANSPORT + 3, false, 1, 4, "transportCrafting", Duct.Type.CRAFTING, DuctFactory.structural, null, null, null, 255, "electrum", null, 128));
    
    transportBasic.setRarity(1);
    transportLongRange.setRarity(1);
    transportCrossover.setRarity(2);
  }
  
  public static int OFFSET_TRANSPORT = 64;
  public static DuctTransport transportBasic;
  public static DuctTransport transportLongRange;
  public static DuctTransport transportCrossover;
  public static DuctTransport transportFrame;
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\TDDucts.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */