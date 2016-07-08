package cofh.thermaldynamics.render;

import cofh.thermaldynamics.ThermalDynamics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureTransparent
  extends TextureAtlasSprite
{
  byte transparency;
  ResourceLocation location;
  
  public static IIcon registerTransparentIcon(IIconRegister paramIIconRegister, String paramString, byte paramByte)
  {
    if (paramByte == -1) {
      return paramIIconRegister.registerIcon(paramString);
    }
    TextureMap localTextureMap = (TextureMap)paramIIconRegister;
    
    Object localObject = localTextureMap.getTextureExtry(transformedName(paramString, paramByte));
    if (localObject == null)
    {
      localObject = new TextureTransparent(paramString, paramByte);
      localTextureMap.setTextureEntry(((TextureAtlasSprite)localObject).getIconName(), (TextureAtlasSprite)localObject);
    }
    return (IIcon)localObject;
  }
  
  protected TextureTransparent(String paramString, byte paramByte)
  {
    super(transformedName(paramString, paramByte));
    transparency = paramByte;
    
    String str1 = "minecraft";
    String str2 = paramString;
    int i = paramString.indexOf(':');
    if (i >= 0)
    {
      str2 = paramString.substring(i + 1, paramString.length());
      if (i > 1) {
        str1 = paramString.substring(0, i);
      }
    }
    location = new ResourceLocation(str1, "textures/blocks/" + str2 + ".png");
  }
  
  private static String transformedName(String paramString, byte paramByte)
  {
    return paramString + "_trans_" + paramByte;
  }
  
  public boolean hasCustomLoader(IResourceManager paramIResourceManager, ResourceLocation paramResourceLocation)
  {
    return true;
  }
  
  public boolean load(IResourceManager paramIResourceManager, ResourceLocation paramResourceLocation)
  {
    GameSettings localGameSettings = getMinecraftgameSettings;
    try
    {
      IResource localIResource = paramIResourceManager.getResource(location);
      
      BufferedImage localBufferedImage = ImageIO.read(localIResource.getInputStream());
      
      int[] arrayOfInt = new int[localBufferedImage.getWidth() * localBufferedImage.getHeight()];
      localBufferedImage.getRGB(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), arrayOfInt, 0, localBufferedImage.getWidth());
      for (int i = 0; i < arrayOfInt.length; i++) {
        if ((arrayOfInt[i] & 0xFF000000) != 0) {
          arrayOfInt[i] = (arrayOfInt[i] & 0xFFFFFF | transparency << 24);
        }
      }
      localBufferedImage.setRGB(0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), arrayOfInt, 0, localBufferedImage.getWidth());
      
      BufferedImage[] arrayOfBufferedImage = new BufferedImage[1 + mipmapLevels];
      arrayOfBufferedImage[0] = localBufferedImage;
      
      AnimationMetadataSection localAnimationMetadataSection = (AnimationMetadataSection)localIResource.getMetadata("animation");
      super.loadSprite(arrayOfBufferedImage, localAnimationMetadataSection, anisotropicFiltering > 1.0F);
    }
    catch (IOException localIOException)
    {
      ThermalDynamics.log.error("Using missing texture, unable to load " + location, localIOException);
      return true;
    }
    return false;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\render\TextureTransparent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */