package cofh.thermaldynamics;

import cofh.api.core.IInitializer;
import cofh.core.CoFHProps;
import cofh.core.util.ConfigHandler;
import cofh.lib.util.helpers.MathHelper;
import cofh.mod.BaseMod;
import cofh.mod.updater.UpdateManager;
import cofh.thermaldynamics.core.Proxy;
import cofh.thermaldynamics.core.TickHandler;
import cofh.thermaldynamics.debughelper.CommandThermalDebug;
import cofh.thermaldynamics.debughelper.DebugHelper;
import cofh.thermaldynamics.debughelper.PacketDebug;
import cofh.thermaldynamics.duct.BlockDuct;
import cofh.thermaldynamics.duct.TDDucts;
import cofh.thermaldynamics.duct.entity.TileTransportDuctCrossover;
import cofh.thermaldynamics.gui.GuiHandler;
import cofh.thermaldynamics.gui.TDCreativeTab;
import cofh.thermaldynamics.gui.TDCreativeTabCovers;
import cofh.thermaldynamics.item.ItemCover;
import cofh.thermaldynamics.item.ItemFilter;
import cofh.thermaldynamics.item.ItemRelay;
import cofh.thermaldynamics.item.ItemRetriever;
import cofh.thermaldynamics.item.ItemServo;
import cofh.thermaldynamics.plugins.TDPlugins;
import cofh.thermaldynamics.util.crafting.RecipeCover;
import cofh.thermaldynamics.util.crafting.TDCrafting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid="ThermalDynamics", name="Thermal Dynamics", version="1.7.10R1.2.0", dependencies="required-after:CoFHCore@[1.7.10R3.1.2,1.7.10R3.2.0);required-after:ThermalFoundation@[1.7.10R1.2.4,1.7.10R1.3.0);", guiFactory="cofh.thermaldynamics.gui.GuiConfigTDFactory", customProperties={@net.minecraftforge.fml.common.Mod.CustomProperty(k="cofhversion", v="true")})
public class ThermalDynamics
  extends BaseMod
{
  @Mod.Instance("ThermalDynamics")
  public static ThermalDynamics instance;
  @SidedProxy(clientSide="cofh.thermaldynamics.core.ProxyClient", serverSide="cofh.thermaldynamics.core.Proxy")
  public static Proxy proxy;
  public static final Logger log = LogManager.getLogger("ThermalDynamics");
  public static final GuiHandler guiHandler = new GuiHandler();
  public static CreativeTabs tabCommon;
  
  public ThermalDynamics()
  {
    super(log);
  }
  
  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent paramFMLPreInitializationEvent) // NO_UCD (unused code)
  {    
    configOptions();
    
    TDDucts.addDucts();
    
    int i = (int)Math.ceil(TDDucts.ductList.size() / 16.0D);
    blockDuct = new BlockDuct[i];
    for (int j = 0; j < i; j++) {
      blockDuct[j] = ((BlockDuct)addBlock(new BlockDuct(j)));
    }
    for (IInitializer localIInitializer : initializerList) {
      localIInitializer.preInit();
    }
  }
  
  @Mod.EventHandler
  public void initialize(FMLInitializationEvent paramFMLInitializationEvent) // NO_UCD (unused code)
  {
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
    MinecraftForge.EVENT_BUS.register(proxy);
    for (IInitializer localIInitializer : initializerList) {
      localIInitializer.initialize();
    }
    FMLCommonHandler.instance().bus().register(TickHandler.instance);
    MinecraftForge.EVENT_BUS.register(TickHandler.instance);
    
    PacketDebug.initialize();
    DebugHelper.initialize();
  }
  
  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent paramFMLPostInitializationEvent) // NO_UCD (unused code)
  {
    for (IInitializer localIInitializer : initializerList) {
      localIInitializer.postInit();
    }
    proxy.registerRenderInformation();
  }
  
  void configOptions()
  {
    String str1 = "Duct.Transport";
    String str2 = "Must be between 0 and 120 ticks.";
    TileTransportDuctCrossover.CHARGE_TIME = (byte)MathHelper.clamp(config
      .get(str1, "CrossoverChargeTime", TileTransportDuctCrossover.CHARGE_TIME, str2), 0, TileTransportDuctCrossover.CHARGE_TIME);
    
    str2 = "This value affects the size of the inner duct model, such as fluids. Lower it if you experience texture z-fighting.";
    cofh.thermaldynamics.core.TDProps.smallInnerModelScaling = MathHelper.clamp((float)configClient.get("Render", "InnerModelScaling", 0.99D, str2), 0.5F, 0.99F);
    
    str2 = "This value affects the size of the inner duct model, such as fluids, on the large (octagonal) ducts. Lower it if you experience texture z-fighting.";
    cofh.thermaldynamics.core.TDProps.largeInnerModelScaling = MathHelper.clamp((float)configClient.get("Render", "LargeInnerModelScaling", 0.99D, str2), 0.5F, 0.99F);
  }
  
  LinkedList<IInitializer> initializerList = new LinkedList();
  
  public String getModId()
  {
    return "ThermalDynamics";
  }
  
  public String getModName()
  {
    return "Thermal Dynamics";
  }
  
  public String getModVersion()
  {
    return "1.7.10R1.2.0";
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\ThermalDynamics.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */