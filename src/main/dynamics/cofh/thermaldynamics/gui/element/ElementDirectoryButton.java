package cofh.thermaldynamics.gui.element;

import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementButton;
import cofh.thermaldynamics.gui.client.DirectoryEntry;
import cofh.thermaldynamics.gui.client.GuiTransport;
import java.util.List;
import net.minecraft.client.gui.FontRenderer;

public class ElementDirectoryButton
  extends ElementButton
{
  final int index;
  final GuiTransport gui;
  DirectoryEntry entry;
  
  public ElementDirectoryButton(int paramInt1, GuiTransport paramGuiTransport, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString)
  {
    super(paramGuiTransport, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramString);
    index = paramInt1;
    gui = paramGuiTransport;
  }
  
  public ElementDirectoryButton(int paramInt1, GuiTransport paramGuiTransport, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramGuiTransport, paramInt2, paramInt3 + paramInt1 * 23, 155, 22, 0, 204, 0, 226, "thermaldynamics:textures/gui/Transport.png");
  }
  
  public void setEntry(DirectoryEntry paramDirectoryEntry)
  {
    entry = paramDirectoryEntry;
  }
  
  public boolean isVisible()
  {
    return (super.isVisible()) && (entry != null);
  }
  
  public void onClick()
  {
    if (entry != null) {
      gui.goToDest(entry);
    }
  }
  
  public void drawBackground(int paramInt1, int paramInt2, float paramFloat)
  {
    super.drawBackground(paramInt1, paramInt2, paramFloat);
  }
  
  public void drawForeground(int paramInt1, int paramInt2)
  {
    super.drawForeground(paramInt1, paramInt2);
    if (entry == null) {
      return;
    }
    String str = getFontRenderer().trimStringToWidth(entry.getName(), sizeX - sizeY - 4);
    
    getFontRenderer().drawStringWithShadow(str, posX + sizeY + 4, posY + (sizeY - 8) / 2, getTextColor(paramInt1, paramInt2));
    if (entry.icon != null) {
      gui.drawItemStack(entry.icon, posX + 3, posY + 3, false, null);
    }
  }
  
  protected int getTextColor(int paramInt1, int paramInt2)
  {
    if (!isEnabled()) {
      return -6250336;
    }
    if (intersectsWith(paramInt1, paramInt2)) {
      return 16777120;
    }
    return 14737632;
  }
  
  public void setPosX(int paramInt)
  {
    posX = paramInt;
  }
  
  public void addTooltip(List<String> paramList)
  {
    paramList.add(entry.getName());
    
    paramList.add(String.format("x: %d", new Object[] { Integer.valueOf(entry.x) }));
    paramList.add(String.format("y: %d", new Object[] { Integer.valueOf(entry.y) }));
    paramList.add(String.format("z: %d", new Object[] { Integer.valueOf(entry.z) }));
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\element\ElementDirectoryButton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */