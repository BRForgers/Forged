package cofh.thermaldynamics.render;

import cofh.repack.codechicken.lib.render.TextureDataHolder;
import cofh.repack.codechicken.lib.render.TextureSpecial;
import cofh.repack.codechicken.lib.render.TextureUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class TextureOverlay
{
  public static final String PATH_BASE = "thermaldynamics:textures/blocks/duct/base/";
  public static final String PATH_CONNECTION = "thermaldynamics:textures/blocks/duct/connection/";
  public static final String PATH_FRAME = "thermaldynamics:textures/blocks/duct/base/";
  
  public static ResourceLocation toLoc(String paramString1, String paramString2)
  {
    return new ResourceLocation(paramString1 + paramString2 + ".png");
  }
  
  public static TextureDataHolder incSize(TextureDataHolder paramTextureDataHolder, int paramInt)
  {
    int i = paramInt / width;
    TextureDataHolder localTextureDataHolder = new TextureDataHolder(paramInt, height * i);
    for (int j = 0; j < data.length; j++) {
      data[j] = -1737058186;
    }
    for (j = 0; j < width; j++) {
      for (int k = 0; k < height; k++)
      {
        int m = data[(j + k * width)];
        for (int n = 0; n < i; n++) {
          for (int i1 = 0; i1 < i; i1++) {
            data[(j * i + n + (k * i + i1) * width)] = m;
          }
        }
      }
    }
    return localTextureDataHolder;
  }
  
  public static IIcon generateBaseTexture(IIconRegister paramIIconRegister, String paramString, String... paramVarArgs)
  {
    TextureDataHolder localTextureDataHolder1 = TextureUtils.loadTexture(toLoc("thermaldynamics:textures/blocks/duct/base/", paramString));
    
    StringBuilder localStringBuilder = new StringBuilder("thermaldynamics:Duct_").append(paramString);
    for (String str1 : paramVarArgs) {
      if (str1 != null) {
        localStringBuilder.append('_').append(str1);
      }
    }
    ??? = localStringBuilder.toString();
    
    TextureAtlasSprite localTextureAtlasSprite = ((TextureMap)paramIIconRegister).getTextureExtry((String)???);
    if (localTextureAtlasSprite != null) {
      return localTextureAtlasSprite;
    }
    for (String str2 : paramVarArgs) {
      if (str2 != null)
      {
        TextureDataHolder localTextureDataHolder2 = TextureUtils.loadTexture(toLoc("thermaldynamics:textures/blocks/duct/base/", str2));
        if (width != width) {
          if (width < width) {
            localTextureDataHolder1 = incSize(localTextureDataHolder1, width);
          } else {
            localTextureDataHolder2 = incSize(localTextureDataHolder2, width);
          }
        }
        int n;
        if ("trans".equals(str2)) {
          for (n = 0; n < data.length; n++) {
            if ((data[n] >> 24 & 0xFF) != 0) {
              data[n] = 0;
            }
          }
        } else {
          for (n = 0; n < data.length; n++)
          {
            int i1 = data[n];
            if ((i1 >> 24 & 0xFF) != 0) {
              data[n] = i1;
            }
          }
        }
      }
    }
    ??? = TextureUtils.getTextureSpecial(paramIIconRegister, (String)???);
    ((TextureSpecial)???).addTexture(localTextureDataHolder1);
    
    return (IIcon)???;
  }
  
  public static IIcon generateConnectionTexture(IIconRegister paramIIconRegister, String paramString)
  {
    TextureDataHolder localTextureDataHolder = TextureUtils.loadTexture(toLoc("thermaldynamics:textures/blocks/duct/connection/", paramString));
    
    String str = "thermaldynamics:Conn_" + paramString;
    
    TextureAtlasSprite localTextureAtlasSprite = ((TextureMap)paramIIconRegister).getTextureExtry(str);
    if (localTextureAtlasSprite != null) {
      return localTextureAtlasSprite;
    }
    TextureSpecial localTextureSpecial = TextureUtils.getTextureSpecial(paramIIconRegister, str);
    localTextureSpecial.addTexture(localTextureDataHolder);
    
    return localTextureSpecial;
  }
  
  public static IIcon generateFrameTexture(IIconRegister paramIIconRegister, String paramString)
  {
    TextureDataHolder localTextureDataHolder = TextureUtils.loadTexture(toLoc("thermaldynamics:textures/blocks/duct/base/", paramString + "_trans"));
    
    String str = "thermaldynamics:Frame_" + paramString;
    
    TextureAtlasSprite localTextureAtlasSprite = ((TextureMap)paramIIconRegister).getTextureExtry(str);
    if (localTextureAtlasSprite != null) {
      return localTextureAtlasSprite;
    }
    TextureSpecial localTextureSpecial = TextureUtils.getTextureSpecial(paramIIconRegister, str);
    localTextureSpecial.addTexture(localTextureDataHolder);
    
    return localTextureSpecial;
  }
  
  public static IIcon generateFrameBandTexture(IIconRegister paramIIconRegister, String paramString)
  {
    TextureDataHolder localTextureDataHolder = TextureUtils.loadTexture(toLoc("thermaldynamics:textures/blocks/duct/base/", paramString + "_band"));
    
    String str = "thermaldynamics:Band_" + paramString;
    
    TextureAtlasSprite localTextureAtlasSprite = ((TextureMap)paramIIconRegister).getTextureExtry(str);
    if (localTextureAtlasSprite != null) {
      return localTextureAtlasSprite;
    }
    TextureSpecial localTextureSpecial = TextureUtils.getTextureSpecial(paramIIconRegister, str);
    localTextureSpecial.addTexture(localTextureDataHolder);
    
    return localTextureSpecial;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\TextureOverlay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */