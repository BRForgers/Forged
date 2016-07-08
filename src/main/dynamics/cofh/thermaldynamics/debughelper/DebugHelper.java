package cofh.thermaldynamics.debughelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import java.lang.reflect.Field;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class DebugHelper
{
  public static boolean debug;
  
  static
  {
    try
    {
      World.class.getMethod("getBlock", new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE });
      debug = true;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      debug = false;
    }
  }
  
  public static final Logger log = LogManager.getLogger("ThermalDebug");
  
  public static void initialize()
  {
    if (!debug) {
      return;
    }
    FMLCommonHandler.instance().bus().register(DebugTickHandler.INSTANCE);
    MinecraftForge.EVENT_BUS.register(DebugTickHandler.INSTANCE);
  }
  
  public static void info(Object paramObject)
  {
    if (debug) {
      log.info(paramObject);
    }
  }
  
  public static <T> T logObject(T paramT)
  {
    info(paramT);
    return paramT;
  }
  
  public static long time = 0L;
  
  public static void startTimer()
  {
    time = System.nanoTime();
  }
  
  public static void stopTimer(String paramString)
  {
    if (debug)
    {
      double d = (System.nanoTime() - time) * 1.0E-6D;
      log.info(paramString + ": " + d + " ms");
    }
  }
  
  private static Random rand = new Random();
  
  @SideOnly(Side.CLIENT)
  public static void showParticle(World paramWorld, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt)
  {
    rand.setSeed(paramInt);
    double d1 = rand.nextDouble();double d2 = rand.nextDouble();double d3 = rand.nextDouble();
    double d4 = 1.0D / (d3 > d2 ? d3 : d1 > d2 ? d1 : d3 > d1 ? d3 : d2);
    
    d1 *= d4;
    d2 *= d4;
    d3 *= d4;
    if (paramWorld == null) {
      paramWorld = getMinecrafttheWorld;
    }
    paramWorld.spawnParticle("reddust", paramDouble1, paramDouble2, paramDouble3, d1, d2, d3);
  }
  
  public static void quit()
  {
    FMLCommonHandler.instance().exitJava(0, true);
  }
  
  static String[] glStates = { "GL_ALPHA_TEST", "GL_AUTO_NORMAL", "GL_BLEND", "GL_CLIP_PLANE i", "GL_COLOR_ARRAY", "GL_COLOR_LOGIC_OP", "GL_COLOR_MATERIAL", "GL_CULL_FACE", "GL_DEPTH_TEST", "GL_DITHER", "GL_FOG", "GL_INDEX_ARRAY", "GL_INDEX_LOGIC_OP", "GL_LIGHT i", "GL_LIGHTING", "GL_LINE_SMOOTH", "GL_LINE_STIPPLE", "GL_MAP1_COLOR_4", "GL_MAP1_INDEX", "GL_MAP1_NORMAL", "GL_MAP1_TEXTURE_COORD_1", "GL_MAP1_TEXTURE_COORD_2", "GL_MAP1_TEXTURE_COORD_3", "GL_MAP1_TEXTURE_COORD_4", "GL_MAP1_VERTEX_3", "GL_MAP1_VERTEX_4", "GL_MAP2_COLOR_4", "GL_MAP2_INDEX", "GL_MAP2_NORMAL", "GL_MAP2_TEXTURE_COORD_1", "GL_MAP2_TEXTURE_COORD_2", "GL_MAP2_TEXTURE_COORD_3", "GL_MAP2_TEXTURE_COORD_4", "GL_MAP2_VERTEX_3", "GL_MAP2_VERTEX_4", "GL_NORMAL_ARRAY", "GL_NORMALIZE", "GL_POINT_SMOOTH", "GL_POLYGON_OFFSET_FILL", "GL_POLYGON_OFFSET_LINE", "GL_POLYGON_OFFSET_POINT", "GL_POLYGON_SMOOTH", "GL_POLYGON_STIPPLE", "GL_SCISSOR_TEST", "GL_STENCIL_TEST", "GL_TEXTURE_1D", "GL_TEXTURE_2D", "GL_TEXTURE_COORD_ARRAY", "GL_TEXTURE_GEN_Q", "GL_TEXTURE_GEN_R", "GL_TEXTURE_GEN_S", "GL_TEXTURE_GEN_T", "GL_VERTEX_ARRAY" };
  @SideOnly(Side.CLIENT)
  public static int[] glCaps;
  
  @SideOnly(Side.CLIENT)
  private static void initGLStates()
  {
    glCaps = new int[glStates.length];
    for (int i = 0; i < glStates.length; i++)
    {
      glCaps[i] = -1;
      try
      {
        glCaps[i] = GL11.class.getField(glStates[i]).getInt(null);
      }
      catch (NoSuchFieldException localNoSuchFieldException) {}catch (IllegalAccessException localIllegalAccessException) {}
    }
  }
  
  @SideOnly(Side.CLIENT)
  public static void logGLStates()
  {
    if (glCaps == null) {
      initGLStates();
    }
    for (int i = 0; i < glCaps.length; i++) {
      if ((glCaps[i] != -1) && (GL11.glIsEnabled(glCaps[i]))) {
        info(glStates[i]);
      }
    }
  }
  
  public static TObjectLongHashMap<String> subTicks = new TObjectLongHashMap();
  public static TObjectLongHashMap<String> markTicks = new TObjectLongHashMap();
  public static TObjectIntHashMap<String> subTickCalls = new TObjectIntHashMap();
  
  public static void startTimerTick(String paramString)
  {
    subTickCalls.adjustOrPutValue(paramString, 1, 1);
    long l = System.nanoTime();
    markTicks.put(paramString, l);
  }
  
  public static void stopTimerTick(String paramString)
  {
    long l1 = System.nanoTime();
    long l2 = l1 - markTicks.get(paramString);
    subTicks.adjustOrPutValue(paramString, l2, l2);
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\debughelper\DebugHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */