package cofh.thermaldynamics.debughelper;

import cofh.core.network.PacketHandler;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermaldynamics.core.TickHandler;
import cofh.thermaldynamics.core.WorldGridList;
import cofh.thermaldynamics.multiblock.IMultiBlock;
import cofh.thermaldynamics.multiblock.MultiBlockGrid;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.map.hash.TObjectLongHashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.ChunkEvent.Unload;

public class DebugTickHandler
{
  public static DebugTickHandler INSTANCE = new DebugTickHandler();
  public final Random rand = new Random();
  public static boolean showParticles;
  public static boolean showLoading;
  
  @SubscribeEvent
  public void chunkLoad(ChunkEvent.Load paramLoad)
  {
    printChunkEvent(paramLoad);
  }
  
  @SubscribeEvent
  public void chunkLoad(ChunkEvent.Unload paramUnload)
  {
    printChunkEvent(paramUnload);
  }
  
  public void printChunkEvent(ChunkEvent paramChunkEvent)
  {
    if (!showLoading) {
      return;
    }
    DebugHelper.info(
      "[" + getChunkxPosition + "," + getChunkzPosition + "]_" + (getChunkworldObj.isRemote ? "Client" : "Server"));
  }
  
  public static volatile long lag = 0L;
  
  @SubscribeEvent
  public void lag(TickEvent.ServerTickEvent paramServerTickEvent)
  {
    if ((lag <= 0L) || (phase == TickEvent.Phase.END)) {
      return;
    }
    long l = System.nanoTime();
    while (System.nanoTime() < l + lag) {}
  }
  
  @SubscribeEvent
  public void subTicks(TickEvent.ServerTickEvent paramServerTickEvent)
  {
    if (DebugHelper.subTicks.isEmpty()) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    TObjectLongIterator localTObjectLongIterator = DebugHelper.subTicks.iterator();
    
    int i = 0;
    while (localTObjectLongIterator.hasNext())
    {
      localTObjectLongIterator.advance();
      int j = DebugHelper.subTickCalls.get(localTObjectLongIterator.key());
      i = (i != 0) || (j > 0) ? 1 : 0;
      double d = localTObjectLongIterator.value() * 1.0E-6D;
      localStringBuilder.append((String)localTObjectLongIterator.key()).append("={").append(d).append(" ms").append(", n=").append(j).append(", avg=").append(d / (j == 0 ? 1 : j)).append("} ");
      
      localTObjectLongIterator.setValue(0L);
      DebugHelper.subTickCalls.put(localTObjectLongIterator.key(), 0);
    }
    if (i != 0) {
      DebugHelper.info(localStringBuilder.toString());
    }
  }
  
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void showParticles(TickEvent.WorldTickEvent paramWorldTickEvent)
  {
    if ((phase == TickEvent.Phase.START) || (getMinecrafttheWorld == null) || 
      (getMinecrafttheWorld.provider.dimensionId != world.provider.dimensionId)) {
      return;
    }
    if (ServerHelper.isClientWorld(world)) {
      return;
    }
    if (!showParticles) {
      return;
    }
    for (MultiBlockGrid localMultiBlockGrid : getTickHandlerworld).tickingGrids)
    {
      rand.setSeed(localMultiBlockGrid.hashCode());
      
      d1 = rand.nextDouble();d2 = rand.nextDouble();d3 = rand.nextDouble();
      double d4 = 1.0D / (d3 > d2 ? d3 : d1 > d2 ? d1 : d3 > d1 ? d3 : d2);
      d1 *= d4;
      d2 *= d4;
      d3 *= d4;
      for (localIterator2 = nodeSet.iterator(); localIterator2.hasNext();)
      {
        localIMultiBlock = (IMultiBlock)localIterator2.next();
        getMinecrafttheWorld.spawnParticle("reddust", localIMultiBlock.x() + 0.5D, localIMultiBlock.y() + 0.75D, localIMultiBlock.z() + 0.5D, d1, d2, d3);
      }
      d1 *= 0.8D;
      d2 *= 0.8D;
      d3 *= 0.8D;
      for (localIterator2 = idleSet.iterator(); localIterator2.hasNext();)
      {
        localIMultiBlock = (IMultiBlock)localIterator2.next();
        getMinecrafttheWorld.spawnParticle("reddust", localIMultiBlock.x() + 0.5D, localIMultiBlock.y() + 0.75D, localIMultiBlock.z() + 0.5D, d1, d2, d3);
      }
    }
    double d1;
    double d2;
    double d3;
    Iterator localIterator2;
    IMultiBlock localIMultiBlock;
  }
  
  public static enum DebugEvent
  {
    GRID_FORMED,  GRID_BROKEN,  NEIGHBOUR_CHANGE,  NEIGHBOUR_WEAK_CHANGE,  TILE_INVALIDATED,  NEIGHBOUR_CHUNK_UNLOADED,  TILE_TICKED,  PACKET_FORMED,  GRID_MERGED,  GRID_DESTROYED,  ROUTE_SEARCH,  ROUTE_INVALIDATED,  ROUTE_RESET,  ITEM_POLL,  ITEM_REPOLL;
    
    static final int n = values().length;
    
    private DebugEvent() {}
  }
  
  int servertick = 0;
  
  @SubscribeEvent
  public void onServerTick(TickEvent.ServerTickEvent paramServerTickEvent)
  {
    if (phase != TickEvent.Phase.END) {
      return;
    }
    int i = (servertick + 1) % values.length;
    for (int j = 0; j < DebugEvent.n; j++)
    {
      displayValue[j] = 0;
      for (Object localObject2 : values) {
        displayValue[j] += localObject2[j];
      }
      values[i][j] = 0;
    }
    servertick = i;
    
    PacketDebug localPacketDebug = new PacketDebug(displayValue);
    for (??? = debugPlayers.iterator(); ((Iterator)???).hasNext();)
    {
      EntityPlayer localEntityPlayer = (EntityPlayer)((Iterator)???).next();
      PacketHandler.sendTo(localPacketDebug, localEntityPlayer);
    }
  }
  
  public static HashSet<EntityPlayer> debugPlayers = new HashSet();
  public int[] displayValue = new int[DebugEvent.values().length];
  public int[][] values = new int[20][DebugEvent.values().length];
  
  public static void tickEvent(DebugEvent paramDebugEvent)
  {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
      INSTANCEvalues[INSTANCEservertick][paramDebugEvent.ordinal()] += 1;
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\debughelper\DebugTickHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */