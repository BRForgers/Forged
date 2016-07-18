package cofh.thermaldynamics.gui;

import cofh.core.block.TileCoFHBase;
import cofh.thermaldynamics.block.Attachment;
import cofh.thermaldynamics.block.TileTDBase;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler
  implements IGuiHandler
{
  
  public Object getServerGuiElement(int paramInt1, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt2, int paramInt3, int paramInt4)
  {
    TileEntity localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
    if ((paramInt1 >= 1) && (paramInt1 <= 6) && 
      ((localTileEntity instanceof TileTDBase)))
    {
      Attachment localAttachment = attachments[(paramInt1 - 1)];
      if (localAttachment != null) {
        return localAttachment.getGuiServer(inventory);
      }
    }
    switch (paramInt1)
    {
    case 0: 
      localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
      if ((localTileEntity instanceof TileCoFHBase)) {
        return ((TileCoFHBase)localTileEntity).getGuiServer(inventory);
      }
    case 7: 
      localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
      if ((localTileEntity instanceof TileTDBase)) {
        return ((TileTDBase)localTileEntity).getConfigGuiServer(inventory);
      }
      return null;
    }
    return null;
  }
  
  public Object getClientGuiElement(int paramInt1, EntityPlayer paramEntityPlayer, World paramWorld, int paramInt2, int paramInt3, int paramInt4)
  {
    TileEntity localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
    if ((paramInt1 >= 1) && (paramInt1 <= 6) && 
      ((localTileEntity instanceof TileTDBase)))
    {
      Attachment localAttachment = attachments[(paramInt1 - 1)];
      if (localAttachment != null) {
        return localAttachment.getGuiClient(inventory);
      }
    }
    switch (paramInt1)
    {
    case 0: 
      localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
      if ((localTileEntity instanceof TileCoFHBase)) {
        return ((TileCoFHBase)localTileEntity).getGuiClient(inventory);
      }
      return null;
    case 7: 
      localTileEntity = paramWorld.getTileEntity(paramInt2, paramInt3, paramInt4);
      if ((localTileEntity instanceof TileTDBase)) {
        return ((TileTDBase)localTileEntity).getConfigGuiClient(inventory);
      }
      return null;
    }
    return null;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\gui\GuiHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */