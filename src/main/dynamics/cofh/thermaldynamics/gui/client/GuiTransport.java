package cofh.thermaldynamics.gui.client;

import cofh.core.gui.GuiBaseAdv;
import cofh.core.network.PacketHandler;
import cofh.core.network.PacketTileInfo;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButtonManaged;
import cofh.lib.gui.element.listbox.SliderVertical;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.gui.container.ContainerTransport;
import cofh.thermaldynamics.gui.element.ElementDirectoryButton;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class GuiTransport
  extends GuiBaseAdv
{
  final ContainerTransport container;
  public static final String TEX_PATH = "thermaldynamics:textures/gui/Transport.png";
  static final ResourceLocation TEXTURE = new ResourceLocation("thermaldynamics:textures/gui/Transport.png");
  ElementDirectoryButton[] directoryButtons;
  SliderVertical vertical;
  public ElementButtonManaged buttonConfig;
  public int x0;
  public int y0;
  public static final int NUM_ENTRIES = 7;
  public static final int BUTTON_WIDTH = 155;
  public static final int BUTTON_HEIGHT = 22;
  public static final int BUTTON_OFFSET = 1;
  public static final int GUI_BUTTON_X0_BASE = 0;
  public static final int GUI_BUTTON_Y0_BASE = 204;
  public static final int GUI_BUTTON_X0_HOVER = 0;
  public static final int GUI_BUTTON_Y0_HOVER = 226;
  static final int SLIDER_WIDTH = 6;
  
  public GuiTransport(ContainerTransport paramContainerTransport)
  {
    super(paramContainerTransport, TEXTURE);
    container = paramContainerTransport;
    ySize = 204;
    drawInventory = false;
    drawTitle = true;
    name = "info.thermaldynamics.transport.name";
  }
  
  public GuiTransport(TileTransportDuct paramTileTransportDuct)
  {
    this(new ContainerTransport(paramTileTransportDuct));
  }
  
  public void func_73866_w_()
  {
    super.func_73866_w_();
    
    x0 = ((xSize - 155) / 2 - 6);
    y0 = (getFontRendererFONT_HEIGHT + 28);
    
    vertical = new SliderVertical(this, xSize - 6 - 6, y0, 6, 160, 10);
    vertical.setVisible(false);
    addElement(vertical);
    
    directoryButtons = new ElementDirectoryButton[7];
    for (int i = 0; i < 7; i++)
    {
      directoryButtons[i] = new ElementDirectoryButton(i, this, x0, y0);
      addElement(directoryButtons[i]);
    }
    Mouse.setCursorPosition((directoryButtons[0].getPosX() + (directoryButtons[0].getWidth() >> 1) + guiLeft) * mc.displayWidth / width, 
      (height - (1 + directoryButtons[0].getPosY() + (directoryButtons[0].getHeight() >> 1) + guiTop + 1)) * mc.displayHeight / height);
    
    String str = StringHelper.localize("info.thermaldynamics.transport.config");
    int j = getFontRenderer().getStringWidth(str);
    buttonConfig = new ElementButtonManaged(this, xSize - 12 - j, 16, j + 8, 16, str)
    {
      public void onClick()
      {
        PacketTileInfo localPacketTileInfo = PacketTileInfo.newPacket(container.transportDuct);
        localPacketTileInfo.addByte(0);
        localPacketTileInfo.addByte(3);
        PacketHandler.sendToServer(localPacketTileInfo);
      }
    };
    addElement(buttonConfig);
  }
  
  protected void func_146979_b(int paramInt1, int paramInt2)
  {
    super.func_146979_b(paramInt1, paramInt2);
    
    DirectoryEntry localDirectoryEntry = container.directoryEntry;
    if (localDirectoryEntry != null)
    {
      int i = 15;
      
      int j = icon != null ? 22 : 0;
      String str = getFontRenderer().trimStringToWidth(localDirectoryEntry.getName(), xSize - buttonConfig.getWidth() - 16 - j);
      getFontRenderer().drawString(str, x0 + j + 4, i + 7, 4210752);
      if (icon != null) {
        drawItemStack(icon, x0 + 3, i + 3, false, null);
      }
    }
    ArrayList localArrayList = container.directory;
    if (localArrayList == null) {
      fontRendererObj.drawString(StringHelper.localize("info.thermaldynamics.transport.waiting"), 
        getCenteredOffset(StringHelper.localize("info.thermaldynamics.transport.waiting")), ySize / 2, 4210752);
    } else if (localArrayList.isEmpty()) {
      fontRendererObj.drawString(StringHelper.localize("info.thermaldynamics.transport.nodest"), 
        getCenteredOffset(StringHelper.localize("info.thermaldynamics.transport.nodest")), ySize / 2, 4210752);
    }
  }
  
  public void goToDest(DirectoryEntry paramDirectoryEntry)
  {
    container.transportDuct.sendRequest(x, y, z);
  }
  
  protected void updateElementInformation()
  {
    ArrayList localArrayList = container.directory;
    if (localArrayList == null) {
      return;
    }
    boolean bool = localArrayList.size() > 7;
    
    int i = localArrayList.size() - 7;
    
    vertical.setVisible(bool);
    vertical.setLimits(0, bool ? i : 0);
    
    x0 = ((xSize - 155) / 2 - (bool ? 6 : 0));
    
    int j = vertical.getValue();
    for (int k = 0; k < directoryButtons.length; k++)
    {
      int m = j + k;
      directoryButtons[k].setPosX(x0);
      directoryButtons[k].setEntry(m >= localArrayList.size() ? null : (DirectoryEntry)localArrayList.get(m));
    }
  }
  
  protected boolean onMouseWheel(int paramInt1, int paramInt2, int paramInt3)
  {
    return (vertical.isVisible()) && (vertical.onMouseWheel(paramInt1, paramInt2, paramInt3));
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\client\GuiTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */