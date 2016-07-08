package cofh.thermaldynamics.duct.entity;

import cofh.CoFHCore;
import cofh.core.Proxy;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.SoundHelper;
import cofh.lib.util.position.BlockPosition;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.block.TileTDBase.ConnectionTypes;
import cofh.thermaldynamics.block.TileTDBase.NeighborTypes;
import cofh.thermaldynamics.multiblock.Route;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

public class EntityTransport
  extends Entity
{
  public static final byte DATAWATCHER_DIRECTIONS = 16; // NO_UCD (unused code)
  public static final byte DATAWATCHER_PROGRESS = 17; // NO_UCD (unused code)
  public static final byte DATAWATCHER_POSX = 18; // NO_UCD (unused code)
  public static final byte DATAWATCHER_POSY = 19; // NO_UCD (unused code)
  public static final byte DATAWATCHER_POSZ = 20; // NO_UCD (unused code)
  public static final byte DATAWATCHER_STEP = 21; // NO_UCD (unused code)
  public static final byte DATAWATCHER_PAUSE = 22; // NO_UCD (unused code)
  public static final int PIPE_LENGTH = 100; // NO_UCD (unused code)
  public static final int PIPE_LENGTH2 = 50; // NO_UCD (unused code)
  public byte progress;
  public byte direction = 7;
  public byte oldDirection;
  public byte step = 1;
  public boolean reRoute = false;
  public byte pause = 0;
  public float originalWidth = 0.0F;
  public float originalHeight = 0.0F;
  public float originalYOffset = 0.0F;
  public float originalEyeHeight = 0.0F;
  public Entity rider = null;
  Route myPath;
  BlockPosition pos;
  boolean initSound;
  public static final float DEFAULT_WIDTH = 0.05F; // NO_UCD (unused code)
  public static final float DEFAULT_HEIGHT = 0.05F; // NO_UCD (unused code)
  public static final float DEFAULT_YOFFSET = 0.0F; // NO_UCD (unused code)
  
  public boolean isEntityInvulnerable()
  {
    return true;
  }
  
  public double getYOffset()
  {
    return super.getYOffset();
  }
  
  public double getMountedYOffset()
  {
    Entity localEntity = riddenByEntity;
    if (localEntity == null) {
      return super.getMountedYOffset();
    }
    return -localEntity.getYOffset();
  }
  
  public EntityTransport(World paramWorld)
  {
    super(paramWorld);
    step = 0;
    height = 0.1F;
    width = 0.1F;
    noClip = true;
    isImmuneToFire = true;
  }
  
  public EntityTransport(TileTransportDuctBase paramTileTransportDuctBase, Route paramRoute, byte paramByte1, byte paramByte2)
  {
    this(paramTileTransportDuctBase.world());
    
    step = paramByte2;
    pos = new BlockPosition(paramTileTransportDuctBase);
    myPath = paramRoute;
    
    progress = 0;
    
    oldDirection = paramByte1;
    
    setPosition(0.0D);
  }
  
  public boolean shouldRiderSit()
  {
    return true;
  }
  
  public void start(Entity paramEntity)
  {
    loadRider(paramEntity);
    worldObj.spawnEntityInWorld(this);
    paramEntity.mountEntity(this);
  }
  
  public void loadRider(Entity paramEntity)
  {
    rider = paramEntity;
    originalWidth = width;
    originalHeight = height;
    originalYOffset = yOffset;
    if ((rider instanceof EntityPlayer)) {
      originalEyeHeight = rider).eyeHeight;
    }
  }
  
  public boolean isInvisible()
  {
    return true;
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer paramEntityPlayer)
  {
    return true;
  }
  
  public void onUpdate()
  {
    if ((!worldObj.isRemote) || (rider != null))
    {
      if ((riddenByEntity == null) || (riddenByEntity.isDead)) {
        setDead();
      }
    }
    else if (riddenByEntity == null) {
      return;
    }
    if (rider == null)
    {
      if (!(riddenByEntity instanceof EntityLivingBase))
      {
        riddenByEntity.mountEntity(null);
        setDead();
        return;
      }
      loadRider(riddenByEntity);
    }
    else
    {
      updateRider(rider);
    }
    int i = pause > 0 ? 1 : 0;
    if (worldObj.isRemote)
    {
      if (!initSound)
      {
        initSound = true;
        SoundHelper.playSound(getSound());
      }
      if (dataWatcher.hasChanges())
      {
        dataWatcher.func_111144_e();
        loadWatcherData();
      }
    }
    if ((direction == 7) || (pos == null)) {
      return;
    }
    TileEntity localTileEntity = worldObj.getTileEntity(pos.x, pos.y, pos.z);
    if ((localTileEntity == null) || (!(localTileEntity instanceof TileTransportDuctBase)))
    {
      if (worldObj.isRemote) {
        pos = null;
      } else {
        dropPassenger();
      }
      return;
    }
    TileTransportDuctBase localTileTransportDuctBase = (TileTransportDuctBase)localTileEntity;
    if (pause > 0)
    {
      pause = ((byte)(pause - 1));
      if (!worldObj.isRemote)
      {
        updateWatcherData();
      }
      else
      {
        setPosition(0.0D);
        if (riddenByEntity == CoFHCore.proxy.getClientPlayer()) {
          if (pause == 0) {
            CoFHCore.proxy.addIndexedChatMessage(null, -515781222);
          } else {
            CoFHCore.proxy.addIndexedChatMessage(new ChatComponentText("Charging - " + (TileTransportDuctCrossover.CHARGE_TIME - pause) + " / " + TileTransportDuctCrossover.CHARGE_TIME), -515781222);
          }
        }
        for (int j = 0; j < 10; j++) {
          worldObj.spawnParticle("portal", pos.x + 0.5D, pos.y + 0.5D, pos.z + 0.5D, MathHelper.RANDOM.nextGaussian() * 0.5D, MathHelper.RANDOM
            .nextGaussian() * 0.5D, MathHelper.RANDOM.nextGaussian() * 0.5D);
        }
      }
      return;
    }
    if (!worldObj.isRemote)
    {
      localTileTransportDuctBase.advanceEntity(this);
      updateWatcherData();
    }
    else
    {
      if ((i != 0) && (riddenByEntity == CoFHCore.proxy.getClientPlayer())) {
        CoFHCore.proxy.addIndexedChatMessage(null, -515781222);
      }
      localTileTransportDuctBase.advanceEntityClient(this);
    }
    setPosition(0.0D);
    if ((riddenByEntity != null) && (!riddenByEntity.isDead)) {
      updateRiderPosition();
    }
  }
  
  public void updateRider(Entity paramEntity)
  {
    width = 0.05F;
    height = 0.05F;
    yOffset = 0.0F;
    if ((paramEntity instanceof EntityPlayer)) {
      eyeHeight = 0.0F;
    }
    paramEntity.setPosition(posX, posY, posZ);
  }
  
  public void setDead()
  {
    if ((rider != null) && (!rider.isDead))
    {
      rider.height = originalHeight;
      rider.width = originalWidth;
      rider.yOffset = originalYOffset;
      if ((rider instanceof EntityPlayer)) {
        rider).eyeHeight = originalEyeHeight;
      }
      rider.setPosition(rider.posX, rider.posY, rider.posZ);
    }
    super.setDead();
  }
  
  public boolean trySimpleAdvance()
  {
    BlockPosition localBlockPosition = pos.copy().step(direction);
    
    TileEntity localTileEntity = worldObj.getTileEntity(x, y, z);
    if (!(localTileEntity instanceof TileTransportDuctBase))
    {
      pos = null;
      return false;
    }
    TileTDBase.NeighborTypes[] arrayOfNeighborTypes = neighborTypes;
    if (arrayOfNeighborTypes[(direction ^ 0x1)] != TileTDBase.NeighborTypes.MULTIBLOCK)
    {
      pos = null;
      return false;
    }
    pos = localBlockPosition;
    oldDirection = direction;
    progress = ((byte)(progress % 100));
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public ISound getSound()
  {
    return new SoundWoosh(this);
  }
  
  public void onEntityUpdate() {}
  
  public void setPosition(double paramDouble)
  {
    if (pos == null) {
      return;
    }
    if (pause > 0)
    {
      localVector31 = getPos(paramDouble);
      setPosition(x, y, z);
      lastTickPosX = (prevPosX = posX);
      lastTickPosY = (prevPosY = posY);
      lastTickPosZ = (prevPosZ = posZ);
      motionX = (motionY = motionZ = 0.0D);
      return;
    }
    Vector3 localVector31 = getPos(paramDouble - 1.0D);
    lastTickPosX = (prevPosX = x);
    lastTickPosY = (prevPosY = y);
    lastTickPosZ = (prevPosZ = z);
    
    Vector3 localVector32 = getPos(paramDouble);
    setPosition(x, y, z);
    
    motionX = (x - x);
    motionY = (y - y);
    motionZ = (z - z);
    
    updateRiderPosition();
  }
  
  public void dropPassenger()
  {
    if (!worldObj.isRemote)
    {
      rider.mountEntity(null);
      if ((direction >= 0) && (direction < 6))
      {
        double d1 = pos.x + net.minecraft.util.Facing.offsetsXForSide[direction] + 0.5D;
        double d2 = pos.y + net.minecraft.util.Facing.offsetsYForSide[direction];
        double d3 = pos.z + net.minecraft.util.Facing.offsetsZForSide[direction] + 0.5D;
        if (direction == 0) {
          d2 = Math.floor(pos.y - originalHeight);
        }
        rider.setPosition(d1, d2, d3);
        if ((rider instanceof EntityPlayerMP))
        {
          float f1;
          float f2;
          switch (direction)
          {
          case 0: 
            f1 = rider.rotationYaw;
            f2 = 0.0F;
            break;
          case 1: 
            f1 = rider.rotationYaw;
            f2 = 0.0F;
            break;
          case 2: 
            f1 = 180.0F;
            f2 = 0.0F;
            break;
          case 3: 
            f1 = 0.0F;
            f2 = 0.0F;
            break;
          case 4: 
            f1 = 90.0F;
            f2 = 0.0F;
            break;
          case 5: 
            f1 = 270.0F;
            f2 = 0.0F;
            break;
          default: 
            return;
          }
          rider).playerNetServerHandler.setPlayerLocation(d1, d2, d3, f1, f2);
        }
      }
      setDead();
    }
  }
  
  public boolean canTriggerWalking()
  {
    return false;
  }
  
  public void advanceTile(TileTransportDuctBaseRoute paramTileTransportDuctBaseRoute)
  {
    if ((neighborTypes[direction] == TileTDBase.NeighborTypes.MULTIBLOCK) && (connectionTypes[direction] == TileTDBase.ConnectionTypes.NORMAL))
    {
      TileTransportDuctBase localTileTransportDuctBase = (TileTransportDuctBase)paramTileTransportDuctBaseRoute.getPhysicalConnectedSide(direction);
      if ((localTileTransportDuctBase != null) && (neighborTypes[(direction ^ 0x1)] == TileTDBase.NeighborTypes.MULTIBLOCK))
      {
        pos = new BlockPosition(localTileTransportDuctBase);
        if (myPath.hasNextDirection())
        {
          oldDirection = direction;
          direction = myPath.getNextDirection();
        }
        else
        {
          reRoute = true;
        }
      }
      else
      {
        reRoute = true;
      }
    }
    else if ((neighborTypes[direction] == TileTDBase.NeighborTypes.OUTPUT) && (connectionTypes[direction].allowTransfer))
    {
      dropPassenger();
    }
    else
    {
      bouncePassenger(paramTileTransportDuctBaseRoute);
    }
  }
  
  public void bouncePassenger(TileTransportDuctBaseRoute paramTileTransportDuctBaseRoute)
  {
    if (internalGrid == null) {
      return;
    }
    myPath = paramTileTransportDuctBaseRoute.getRoute(this, direction, step);
    if (myPath == null)
    {
      dropPassenger();
    }
    else
    {
      oldDirection = direction;
      direction = myPath.getNextDirection();
      reRoute = false;
    }
  }
  
  protected void entityInit()
  {
    dataWatcher.addObject(16, Byte.valueOf((byte)0));
    dataWatcher.addObject(17, Byte.valueOf((byte)0));
    dataWatcher.addObject(18, Integer.valueOf(0));
    dataWatcher.addObject(19, Integer.valueOf(0));
    dataWatcher.addObject(20, Integer.valueOf(0));
    dataWatcher.addObject(21, Byte.valueOf((byte)1));
    dataWatcher.addObject(22, Byte.valueOf((byte)0));
  }
  
  public void updateWatcherData()
  {
    byte b = (byte)(direction | oldDirection << 3);
    dataWatcher.updateObject(16, Byte.valueOf(b));
    dataWatcher.updateObject(17, Byte.valueOf(progress));
    dataWatcher.updateObject(18, Integer.valueOf(pos.x));
    dataWatcher.updateObject(19, Integer.valueOf(pos.y));
    dataWatcher.updateObject(20, Integer.valueOf(pos.z));
    dataWatcher.updateObject(21, Byte.valueOf(step));
    dataWatcher.updateObject(22, Byte.valueOf(pause));
  }
  
  public void loadWatcherData()
  {
    int i = dataWatcher.getWatchableObjectByte(16);
    direction = ((byte)(i & 0x7));
    oldDirection = ((byte)(i >> 3));
    progress = dataWatcher.getWatchableObjectByte(17);
    
    pos = new BlockPosition(dataWatcher.getWatchableObjectInt(18), dataWatcher.getWatchableObjectInt(19), dataWatcher.getWatchableObjectInt(20));
    step = dataWatcher.getWatchableObjectByte(21);
    pause = dataWatcher.getWatchableObjectByte(22);
  }
  
  protected void readEntityFromNBT(NBTTagCompound paramNBTTagCompound)
  {
    if (paramNBTTagCompound.hasKey("route", 7)) {
      myPath = new Route(paramNBTTagCompound.getByteArray("route"));
    }
    pos = new BlockPosition(paramNBTTagCompound.getInteger("posx"), paramNBTTagCompound.getInteger("posy"), paramNBTTagCompound.getInteger("posz"));
    
    progress = paramNBTTagCompound.getByte("progress");
    direction = paramNBTTagCompound.getByte("direction");
    oldDirection = paramNBTTagCompound.getByte("oldDirection");
    step = paramNBTTagCompound.getByte("step");
    reRoute = paramNBTTagCompound.getBoolean("reRoute");
    
    originalWidth = paramNBTTagCompound.getFloat("originalWidth");
    originalHeight = paramNBTTagCompound.getFloat("originalHeight");
    originalYOffset = paramNBTTagCompound.getFloat("originalYOffset");
    originalEyeHeight = paramNBTTagCompound.getFloat("originalEyeHeight");
  }
  
  protected void writeEntityToNBT(NBTTagCompound paramNBTTagCompound)
  {
    if (myPath != null) {
      paramNBTTagCompound.setByteArray("route", myPath.toByteArray());
    }
    paramNBTTagCompound.setInteger("posx", pos.x);
    paramNBTTagCompound.setInteger("posy", pos.y);
    paramNBTTagCompound.setInteger("posz", pos.z);
    
    paramNBTTagCompound.setByte("progress", progress);
    paramNBTTagCompound.setByte("direction", direction);
    paramNBTTagCompound.setByte("oldDirection", oldDirection);
    paramNBTTagCompound.setByte("step", step);
    paramNBTTagCompound.setBoolean("reRoute", reRoute);
    
    paramNBTTagCompound.setFloat("originalWidth", originalWidth);
    paramNBTTagCompound.setFloat("originalHeight", originalHeight);
    paramNBTTagCompound.setFloat("originalYOffset", originalYOffset);
    paramNBTTagCompound.setFloat("originalEyeHeight", originalEyeHeight);
  }
  
  public Vector3 getPos(double paramDouble)
  {
    return getPos(progress, paramDouble);
  }
  
  public Vector3 getPos(byte paramByte, double paramDouble)
  {
    double d = (paramByte + step * paramDouble) / 100.0D - 0.5D;
    int i = d < 0.0D ? oldDirection : direction;
    
    Vector3 localVector3 = Vector3.center.copy();
    localVector3.add(d * net.minecraft.util.Facing.offsetsXForSide[i], d * net.minecraft.util.Facing.offsetsYForSide[i], d * net.minecraft.util.Facing.offsetsZForSide[i]);
    localVector3.add(pos.x, pos.y, pos.z);
    
    return localVector3;
  }
  
  public boolean handleWaterMovement()
  {
    return false;
  }
  
  public boolean canBeCollidedWith()
  {
    return false;
  }
  
  public boolean handleLavaMovement()
  {
    return false;
  }
  
  public void moveEntity(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    setPosition(0.0D);
  }
  
  public void addVelocity(double paramDouble1, double paramDouble2, double paramDouble3) {}
  
  public boolean isPushedByWater()
  {
    return false;
  }
  
  public boolean canBePushed()
  {
    return false;
  }
  
  public boolean isInRangeToRenderDist(double paramDouble)
  {
    return paramDouble < 4096.0D;
  }
  
  public void teleport(TileTransportDuctBaseRoute paramTileTransportDuctBaseRoute)
  {
    if ((worldObj.isRemote) || (isDead) || (rider == null) || (rider.isDead)) {
      return;
    }
    int i = dimension;
    int j = worldprovider.dimensionId;
    if (j != i)
    {
      MinecraftServer localMinecraftServer = MinecraftServer.getServer();
      
      WorldServer localWorldServer1 = localMinecraftServer.worldServerForDimension(i);
      WorldServer localWorldServer2 = localMinecraftServer.worldServerForDimension(j);
      
      rider.mountEntity(null);
      
      transferNormalEntity(i, j, localWorldServer1, localWorldServer2, this);
      if ((rider instanceof EntityPlayerMP)) {
        transferPlayer(j, rider);
      } else {
        transferNormalEntity(i, j, localWorldServer1, localWorldServer2, rider);
      }
      rider.mountEntity(this);
      
      localWorldServer1.resetUpdateEntityTick();
      localWorldServer2.resetUpdateEntityTick();
    }
    pos = new BlockPosition(paramTileTransportDuctBaseRoute);
    if (myPath.hasNextDirection())
    {
      oldDirection = direction;
      direction = myPath.getNextDirection();
    }
    else
    {
      reRoute = true;
    }
  }
  
  public void transferPlayer(int paramInt, Entity paramEntity)
  {
    paramEntity.travelToDimension(paramInt);
  }
  
  public void transferNormalEntity(int paramInt1, int paramInt2, WorldServer paramWorldServer1, WorldServer paramWorldServer2, Entity paramEntity)
  {
    worldObj.removeEntity(paramEntity);
    MinecraftServer.getServer().getConfigurationManager().transferEntityToWorld(paramEntity, paramInt1, paramWorldServer1, paramWorldServer2);
    paramWorldServer2.spawnEntityInWorld(paramEntity);
    dimension = paramInt2;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\entity\EntityTransport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */