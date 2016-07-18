package cofh.thermaldynamics.gui;

import cofh.core.util.ConfigHandler;
import cofh.thermaldynamics.ThermalDynamics;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class GuiConfigTD
  extends GuiConfig
{
  public GuiConfigTD(GuiScreen paramGuiScreen)
  {
    super(paramGuiScreen, getConfigElements(paramGuiScreen), "ThermalDynamics", false, false, "Thermal Dynamics");
  }
  
  public static final String[] CATEGORIES = new String[0];
  
  private static List<IConfigElement> getConfigElements(GuiScreen paramGuiScreen)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < CATEGORIES.length; i++) {
      localArrayList.add(new ConfigElement(ThermalDynamics.config.getCategory(CATEGORIES[i])));
    }
    return localArrayList;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\GuiConfigTD.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */