package cofh.thermaldynamics.duct;

import net.minecraft.client.renderer.texture.IIconRegister;

public class DuctTransport
  extends Duct
{
  public DuctTransport(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, String paramString1, Duct.Type paramType, DuctFactory paramDuctFactory, String paramString2, String paramString3, String paramString4, int paramInt4, String paramString5, String paramString6, int paramInt5)
  {
    super(paramInt1, paramBoolean, paramInt2, paramInt3, paramString1, paramType, paramDuctFactory, paramString2, paramString3, paramString4, paramInt4, paramString5, paramString6, paramInt5);
  }
  
  public void registerIcons(IIconRegister paramIIconRegister)
  {
    super.registerIcons(paramIIconRegister);
    frameType = 4;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\DuctTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */