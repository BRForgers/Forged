package cofh.thermaldynamics.debughelper;

import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.repack.codechicken.lib.raytracer.RayTracer;
import cofh.thermaldynamics.block.TileTDBase;
import com.google.common.base.Throwables;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.util.ForgeDirection;

public class CommandThermalDebug
  extends CommandBase
{
  public String getCommandName()
  {
    return "debug";
  }
  
  public boolean canCommandSenderUseCommand(ICommandSender paramICommandSender)
  {
    return true;
  }
  
  public String getCommandUsage(ICommandSender paramICommandSender)
  {
    return "debug";
  }
  
  Random rand = new Random();
  Field chunksToUnload;
  private static final String[] trueWords = { "true", "t", "1", "yes", "oui", "affirmative", "truth", "yarp", "uhuh", "yep", "doit", "yea", "tango", "heckyeah", "win" };
  private static final String[] falseWords = { "false", "f", "0", "no", "non", "negative", "cake", "narp", "nuhuh", "nope", "dont", "nay", "foxtrot", "hellno", "fail" };
  private static final String[] mixWords = { "random", "r", "0.5", "imfeelinglucky", "yesno", "supriseme", "whatever", "schrodinger" };
  
  public static boolean textToBoolean(String paramString)
  {
    paramString = paramString.trim();
    String str;
    for (str : trueWords) {
      if (str.equalsIgnoreCase(paramString)) {
        return true;
      }
    }
    for (str : falseWords) {
      if (str.equalsIgnoreCase(paramString)) {
        return false;
      }
    }
    for (str : mixWords) {
      if (str.equalsIgnoreCase(paramString)) {
        return MathHelper.RANDOM.nextBoolean();
      }
    }
    throw new RuntimeException("Unable to interpret word " + paramString + " as true/false");
  }
  
  public String randString()
  {
    StringBuilder localStringBuilder = new StringBuilder("rand_");
    int i = MathHelper.RANDOM.nextInt(10) + 1;
    for (int j = 0; j < i; j++) {
      localStringBuilder.append((char)(97 + MathHelper.RANDOM.nextInt(26)));
    }
    return localStringBuilder.toString();
  }
  
  public static volatile boolean serverOverclock = false;
  
  public void processCommand(ICommandSender paramICommandSender, String[] paramArrayOfString)
  {
    if (paramArrayOfString.length == 0) {
      return;
    }
    if ("overclock".equals(paramArrayOfString[0]))
    {
      serverOverclock = !serverOverclock;
      paramICommandSender.addChatMessage(new ChatComponentText("Server Overclock = " + serverOverclock));
    }
    if ("lag".equals(paramArrayOfString[0])) {
      if (paramArrayOfString.length == 1) {
        DebugTickHandler.lag = 0L;
      } else {
        DebugTickHandler.lag = (parseDouble(paramICommandSender, paramArrayOfString[1]) * 1000.0D * 1000.0D);
      }
    }
    EntityPlayerMP localEntityPlayerMP;
    Object localObject1;
    int m;
    if ("longRange".equals(paramArrayOfString[0]))
    {
      if (!(paramICommandSender instanceof EntityPlayerMP)) {
        return;
      }
      localEntityPlayerMP = (EntityPlayerMP)paramICommandSender;
      localObject1 = new BlockPosition((int)Math.floor(posX), (int)Math.floor(posY) - 5, (int)Math.floor(posZ));
      
      World localWorld = localEntityPlayerMP.getEntityWorld();
      
      ((BlockPosition)localObject1).setOrientation(ForgeDirection.NORTH);
      
      int j = Integer.valueOf(paramArrayOfString[1]).intValue();
      for (m = 0; m < j; m++)
      {
        localWorld.setBlock(x, y, z, cofh.thermaldynamics.ThermalDynamics.blockDuct[4], 1, 3);
        ((TileTDBase)((BlockPosition)localObject1).getTileEntity(localWorld, TileTDBase.class)).blockPlaced();
        ((BlockPosition)localObject1).moveForwards(1);
      }
      for (m = 0; m < 4; m++)
      {
        localWorld.setBlock(x, y, z, cofh.thermaldynamics.ThermalDynamics.blockDuct[4], 1, 3);
        ((TileTDBase)((BlockPosition)localObject1).getTileEntity(localWorld, TileTDBase.class)).blockPlaced();
        ((BlockPosition)localObject1).moveRight(1);
      }
      for (m = 0; m < j; m++)
      {
        if (!localWorld.setBlock(x, y, z, cofh.thermaldynamics.ThermalDynamics.blockDuct[4], 1, 3)) {
          localWorld.setBlock(x, y, z, cofh.thermaldynamics.ThermalDynamics.blockDuct[4], 1, 3);
        }
        ((TileTDBase)((BlockPosition)localObject1).getTileEntity(localWorld, TileTDBase.class)).blockPlaced();
        ((BlockPosition)localObject1).moveBackwards(1);
      }
      return;
    }
    Object localObject2;
    if ("addRandNBT".equals(paramArrayOfString[0]))
    {
      if (!(paramICommandSender instanceof EntityPlayerMP)) {
        return;
      }
      localEntityPlayerMP = (EntityPlayerMP)paramICommandSender;
      
      localObject1 = localEntityPlayerMP.getHeldItem();
      if (localObject1 == null) {
        return;
      }
      ((ItemStack)localObject1).setStackDisplayName(randString());
      for (int i = 0; i < 4; i++)
      {
        NBTTagCompound localNBTTagCompound = new NBTTagCompound();
        for (m = 0; m < 5; m++) {
          localNBTTagCompound.setString(randString(), randString());
        }
        for (m = 0; m < 5; m++) {
          localNBTTagCompound.setInteger(randString(), MathHelper.RANDOM.nextInt());
        }
        stackTagCompound.setTag(randString(), localNBTTagCompound);
      }
      localObject2 = stackTagCompound;
      for (int k = 0; k < 5; k++) {
        ((NBTTagCompound)localObject2).setString(randString(), randString());
      }
      for (k = 0; k < 5; k++) {
        ((NBTTagCompound)localObject2).setInteger(randString(), MathHelper.RANDOM.nextInt());
      }
      if (MathHelper.RANDOM.nextInt(4) == 0) {
        ((NBTTagCompound)localObject2).setTag("ench", new NBTTagCompound());
      }
      localEntityPlayerMP.updateHeldItem();
    }
    else if ("showLoading".equals(paramArrayOfString[0]))
    {
      DebugTickHandler.showLoading = !DebugTickHandler.showLoading;
    }
    else
    {
      Object localObject3;
      if ("unload".equals(paramArrayOfString[0]))
      {
        if (!(paramICommandSender instanceof EntityPlayerMP)) {
          return;
        }
        if (chunksToUnload == null) {
          chunksToUnload = ReflectionHelper.findField(ChunkProviderServer.class, new String[] { "chunksToUnload" });
        }
        localEntityPlayerMP = (EntityPlayerMP)paramICommandSender;
        localObject1 = RayTracer.reTrace(worldObj, localEntityPlayerMP, 100.0D);
        localObject2 = worldObj.getChunkFromBlockCoords(blockX, blockZ);
        try
        {
          localObject3 = (Set)chunksToUnload.get(getServerForPlayertheChunkProviderServer);
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          throw Throwables.propagate(localIllegalAccessException);
        }
        ((Set)localObject3).add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(xPosition, zPosition)));
      }
      else if ("grids".equals(paramArrayOfString[0]))
      {
        DebugTickHandler.showParticles = !DebugTickHandler.showParticles;
      }
      else if (("generate".equals(paramArrayOfString[0])) && (paramArrayOfString.length == 2))
      {
        if (!(paramICommandSender instanceof EntityPlayerMP)) {
          return;
        }
        localEntityPlayerMP = (EntityPlayerMP)paramICommandSender;
        localObject1 = new BlockPosition((int)Math.floor(posX), (int)Math.floor(posY) - 5, (int)Math.floor(posZ));
        
        localObject2 = localEntityPlayerMP.getEntityWorld();
        if (((BlockPosition)localObject1).getBlock((World)localObject2) != Blocks.air) {
          return;
        }
        ((BlockPosition)localObject1).setOrientation(ForgeDirection.NORTH);
        
        localObject3 = new LinkedList();
        
        int n = Integer.valueOf(paramArrayOfString[1]).intValue();
        for (int i1 = 0; i1 < n; i1++)
        {
          if (rand.nextInt(20) == 0) {
            ((LinkedList)localObject3).add(((BlockPosition)localObject1).copy());
          }
          ((World)localObject2).setBlock(x, y, z, cofh.thermaldynamics.ThermalDynamics.blockDuct[2], 0, 3);
          
          ((TileTDBase)((BlockPosition)localObject1).getTileEntity((World)localObject2, TileTDBase.class)).blockPlaced();
          if (rand.nextInt(4) == 0) {
            ((BlockPosition)localObject1).setOrientation(orientation.getRotation(rand.nextBoolean() ? ForgeDirection.UP : ForgeDirection.DOWN));
          }
          ((BlockPosition)localObject1).moveForwards(1);
        }
        BlockPosition localBlockPosition;
        for (Iterator localIterator = ((LinkedList)localObject3).iterator(); localIterator.hasNext(); localBlockPosition = (BlockPosition)localIterator.next()) {}
      }
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\debughelper\CommandThermalDebug.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */