package cofh.thermaldynamics.duct;

import cofh.thermaldynamics.render.TextureOverlay;
import cofh.thermaldynamics.render.TextureTransparent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class Duct
{  
  public static enum Type
  {
    TRANSPORT,  STRUCTURAL,  CRAFTING;
    
    private Type() {}
  }
  
  public ItemStack itemStack = null;
  public IIcon iconBaseTexture;
  public IIcon iconConnectionTexture;
  public IIcon iconFluidTexture;
  public IIcon iconFrameTexture;
  public IIcon iconFrameBandTexture;
  public IIcon iconFrameFluidTexture;
  public byte frameType = 0;
  public final int id;
  public final String unlocalizedName;
  public final int pathWeight;
  public final Type ductType;
  public final DuctFactory factory;
  public final String baseTexture;
  public final String connectionTexture;
  public final String fluidTexture;
  public final byte fluidTransparency;
  public final String frameTexture;
  public final String frameFluidTexture;
  public final byte frameFluidTransparency;
  public final boolean opaque;
  public final int type;
  public EnumRarity rarity = EnumRarity.common;
  
  public Duct(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, String paramString1, Type paramType, DuctFactory paramDuctFactory, String paramString2, String paramString3, String paramString4, int paramInt4, String paramString5, String paramString6, int paramInt5)
  {
    id = paramInt1;
    pathWeight = paramInt2;
    ductType = paramType;
    opaque = paramBoolean;
    type = paramInt3;
    unlocalizedName = paramString1;
    factory = paramDuctFactory;
    baseTexture = paramString2;
    connectionTexture = paramString3;
    fluidTexture = paramString4;
    fluidTransparency = ((byte)paramInt4);
    frameTexture = paramString5;
    frameFluidTexture = paramString6;
    frameFluidTransparency = ((byte)paramInt5);
  }
  
  public Duct setRarity(int paramInt)
  {
    rarity = EnumRarity.values()[(paramInt %= EnumRarity.values().length)];
    return this;
  }
  
  public boolean isLargeTube()
  {
    return (frameType == 2) || (frameType == 4);
  }
  
  public void registerIcons(IIconRegister paramIIconRegister)
  {
    if (baseTexture != null) {
      iconBaseTexture = TextureOverlay.generateBaseTexture(paramIIconRegister, baseTexture, new String[] { opaque ? null : "trans", null });
    }
    if (connectionTexture != null) {
      iconConnectionTexture = TextureOverlay.generateConnectionTexture(paramIIconRegister, connectionTexture);
    }
    if (fluidTexture != null) {
      iconFluidTexture = TextureTransparent.registerTransparentIcon(paramIIconRegister, fluidTexture, fluidTransparency);
    }
    if (frameTexture != null) {
      if (frameTexture.endsWith("_large"))
      {
        frameType = 3;
        iconFrameTexture = paramIIconRegister.registerIcon("thermaldynamics:duct/base/" + frameTexture);
      }
      else if ("sideDucts".equals(frameTexture))
      {
        frameType = 1;
      }
      else
      {
        iconFrameTexture = TextureOverlay.generateFrameTexture(paramIIconRegister, frameTexture);
        iconFrameBandTexture = TextureOverlay.generateFrameBandTexture(paramIIconRegister, frameTexture);
        frameType = 2;
      }
    }
    if (frameFluidTexture != null)
    {
      if (frameType == 0) {
        frameType = 2;
      }
      iconFrameFluidTexture = TextureTransparent.registerTransparentIcon(paramIIconRegister, frameFluidTexture, frameFluidTransparency);
    }
  }
  
  public int compareTo(Duct paramDuct)
  {
    return id < id ? -1 : id > id ? 1 : 0;
  }
  
  public IIcon getBaseTexture(ItemStack paramItemStack)
  {
    return iconBaseTexture;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\Duct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */