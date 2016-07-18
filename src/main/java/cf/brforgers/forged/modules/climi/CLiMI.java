package cf.brforgers.forged.modules.climi;

import cf.brforgers.forged.modules.base.ForgedEvent;
import cf.brforgers.forged.modules.base.ForgedEventState;
import cf.brforgers.forged.modules.base.SimplerModule;
import cf.brforgers.forged.modules.climi.wailawrecker.WailaWrecker;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState.ModState;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameData;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Modified version of LiMI, from Vazkii.
 *
 * @author AdrianTodt (Original code by Vazkii)
 */
//@Mod(modid = "CLiMI")
public class CLiMI extends SimplerModule {
    /**
     * Private HashMap. Don't touch.
     */
    private static ConcurrentHashMap<String, String> modnames = new ConcurrentHashMap<String, String>();
    /**
     * Default Formatting
     */
    private static String defaultFormatting = "";

    /**
     * Add a Custom Display Name to your Mod
     *
     * @param modid The MODID of the Mod
     * @param name  The custom appearance of your mod
     */
    public static void addCustomFormatting(String modid, String name) {
        modnames.put(modid, name);
    }

    /// /// - CONSTANTS - /// ///

    /**
     * Set the Default Formatting
     *
     * @param formatting Default Formatting String
     */
    public static void setDefaultFormatting(String formatting) {
        defaultFormatting = formatting.replaceAll("&", "\u00a7");
    }

    /**
     * Get Formatted Name
     * <br> (Debug purposes)
     *
     * @param modid   The Mod MODID
     * @param modname The Mod Name
     * @return Formatted Name
     */
    public static String getFormattedName(String modid, String modname) {
        return (modnames.containsKey(modid)) ? modnames.get(modid) : defaultFormatting + modname;
    }


    /// /// - CONFIGS FOR MODS - /// ///

    /**
     * Find the Mod Container of the Item
     *
     * @param item The Item
     * @return The Mod Container (Null if not Found/Is Minecraft)
     */
    @SuppressWarnings("deprecation")
    public static ModContainer findModContainer(Item item) {
        ResourceLocation resource = (ResourceLocation) GameData.getItemRegistry().getNameForObject(item);
        for (ModContainer container : Loader.instance().getModList())
            if (resource.getResourceDomain().toLowerCase(Locale.US).equals(container.getModId().toLowerCase(Locale.US)))
                return container;
        return null;
    }

    public String name() {
        return "CLiMI";
    }

    /// /// - WHAT MATTERS - /// ///

    public ForgedEventState loadAt() {
        return ForgedEventState.POSTINITIALIZATION;
    }

    @Override
    public void load(ForgedEvent event) {
        if (WailaWrecker.wailaIn()) { // Waila is in, Let's wreck it.
            if (Loader.instance().getModState(WailaWrecker.getWailaContainer()) == ModState.POSTINITIALIZED) {
                WailaWrecker.Wreck();
            } else {
                WailaWrecker.wrapProxy();
            }
        } else {
            MinecraftForge.EVENT_BUS.register(new CLiMI());
        }
    }

    /**
     * Event Called on Tooltip Event
     *
     * @param event Forge Event
     */
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ModContainer mod = findModContainer(event.getItemStack().getItem());
        String modname = (mod == null) ? "Minecraft" : mod.getName();
        String modid = (mod == null) ? "Minecraft" : mod.getModId();

        event.getToolTip().add(getFormattedName(modid, modname));
    }
}
