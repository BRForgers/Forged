package cofh.thermaldynamics.gui.client;

import cofh.core.gui.GuiBaseAdv;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementTextField;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuct.OutputData;
import cofh.thermaldynamics.gui.container.ContainerTransportConfig;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiTransportConfig
  extends GuiBaseAdv
{
  static final String TEX_PATH = "thermaldynamics:textures/gui/TransportConfig.png";
  static final ResourceLocation TEXTURE = new ResourceLocation("thermaldynamics:textures/gui/TransportConfig.png");
  private final InventoryPlayer inventory;
  private final TileTransportDuct transportDuct;
  
  public GuiTransportConfig(InventoryPlayer paramInventoryPlayer, TileTransportDuct paramTileTransportDuct)
  {
    super(new ContainerTransportConfig(paramInventoryPlayer, paramTileTransportDuct), new ResourceLocation("thermaldynamics:textures/gui/TransportConfig.png"));
    inventory = paramInventoryPlayer;
    transportDuct = paramTileTransportDuct;
    
    ySize = 134;
  }
  
  public void func_73866_w_()
  {
    super.func_73866_w_();
    
    final boolean bool = inventory != null;
    
    addElement(new ElementTextField(this, 32, 18, 135, 10)
    {
      protected void onCharacterEntered(boolean paramAnonymousBoolean)
      {
        super.onCharacterEntered(paramAnonymousBoolean);
        transportDuct.setName(getText());
      }
      
      public void drawBackground(int paramAnonymousInt1, int paramAnonymousInt2, float paramAnonymousFloat)
      {
        if (bool) {
          super.drawBackground(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousFloat);
        }
      }
    }
    
      .setText(transportDuct.data.name).setBackgroundColor(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0)));
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\client\GuiTransportConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */