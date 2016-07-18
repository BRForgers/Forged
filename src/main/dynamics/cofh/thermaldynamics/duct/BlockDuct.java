package cofh.thermaldynamics.duct;

import cofh.api.block.IBlockAppearance;
import cofh.api.block.IBlockConfigGui;
import cofh.core.block.TileCoFHBase;
import cofh.core.network.PacketHandler;
import cofh.core.render.IconRegistry;
import cofh.core.render.hitbox.ICustomHitBox;
import cofh.core.render.hitbox.RenderHitbox;
import cofh.core.util.CoreUtils;
import cofh.repack.codechicken.lib.raytracer.RayTracer;
import cofh.thermaldynamics.ThermalDynamics;
import cofh.thermaldynamics.block.Attachment;
import cofh.thermaldynamics.block.BlockTDBase;
import cofh.thermaldynamics.block.TileTDBase;
import cofh.thermaldynamics.core.TDProps;
import cofh.thermaldynamics.duct.attachments.cover.Cover;
import cofh.thermaldynamics.duct.energy.EnergyGrid;
import cofh.thermaldynamics.duct.energy.TileEnergyDuct;
import cofh.thermaldynamics.duct.energy.TileEnergyDuctSuper;
import cofh.thermaldynamics.duct.energy.subgrid.SubTileEnergyRedstone;
import cofh.thermaldynamics.duct.entity.EntityTransport;
import cofh.thermaldynamics.duct.entity.TileTransportDuct;
import cofh.thermaldynamics.duct.entity.TileTransportDuctCrossover;
import cofh.thermaldynamics.duct.entity.TileTransportDuctLongRange;
import cofh.thermaldynamics.duct.entity.TransportHandler;
import cofh.thermaldynamics.duct.fluid.PacketFluid;
import cofh.thermaldynamics.duct.fluid.TileFluidDuct;
import cofh.thermaldynamics.duct.fluid.TileFluidDuctFlux;
import cofh.thermaldynamics.duct.fluid.TileFluidDuctFragile;
import cofh.thermaldynamics.duct.fluid.TileFluidDuctSuper;
import cofh.thermaldynamics.duct.item.TileItemDuct;
import cofh.thermaldynamics.duct.item.TileItemDuctEnder;
import cofh.thermaldynamics.duct.item.TileItemDuctFlux;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDuct
  extends BlockTDBase
  implements IBlockAppearance, IBlockConfigGui
{
  public int offset;
  
  public BlockDuct(int paramInt)
  {
    super(Material.glass);
    func_149711_c(1.0F);
    func_149752_b(10.0F);
    func_149672_a(Block.soundTypeMetal);
    func_149663_c("thermaldynamics.duct");
    func_149647_a(ThermalDynamics.tabCommon);
    offset = (paramInt * 16);
  }
  
  public void func_149666_a(Item paramItem, CreativeTabs paramCreativeTabs, List paramList)
  {
    for (int i = 0; i < 16; i++) {
      if (TDDucts.isValid(i + offset)) {
        paramList.add(getDuctoffset).itemStack.copy());
      }
    }
  }
  
  public TileEntity func_149915_a(World paramWorld, int paramInt)
  {
    Duct localDuct = TDDucts.getType(paramInt + offset);
    return factory.createTileEntity(localDuct, paramWorld);
  }
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onBlockHighlight(DrawBlockHighlightEvent paramDrawBlockHighlightEvent) // NO_UCD (unused code)
  {
    if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
      if (player.worldObj.getBlock(target.blockX, target.blockY, target.blockZ).getUnlocalizedName().equals(func_149739_a()))
      {
        RayTracer.retraceBlock(player.worldObj, player, target.blockX, target.blockY, target.blockZ);
        
        ICustomHitBox localICustomHitBox = (ICustomHitBox)player.worldObj.getTileEntity(target.blockX, target.blockY, target.blockZ);
        if (localICustomHitBox.shouldRenderCustomHitBox(target.subHit, player))
        {
          paramDrawBlockHighlightEvent.setCanceled(true);
          RenderHitbox.drawSelectionBox(player, target, partialTicks, localICustomHitBox.getCustomHitBox(target.subHit, player));
        }
      }
    }
  }
  
  @SideOnly(Side.CLIENT)
  public void func_149651_a(IIconRegister paramIIconRegister) // NO_UCD (unused code)
  {
    if (offset != 0) {
      return;
    }
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 2; j++)
      {
        IconRegistry.addIcon("ServoBase" + (i * 2 + j), "thermaldynamics:duct/attachment/servo/ServoBase" + i + "" + j, paramIIconRegister);
        IconRegistry.addIcon("RetrieverBase" + (i * 2 + j), "thermaldynamics:duct/attachment/retriever/RetrieverBase" + i + "" + j, paramIIconRegister);
      }
    }
    IconRegistry.addIcon("Signaller", "thermaldynamics:duct/attachment/signallers/Signaller", paramIIconRegister);
    
    IconRegistry.addIcon("CoverBase", "thermaldynamics:duct/attachment/cover/support", paramIIconRegister);
    for (i = 0; i < 5; i++) {
      IconRegistry.addIcon("FilterBase" + i, "thermaldynamics:duct/attachment/filter/Filter" + i + "0", paramIIconRegister);
    }
    IconRegistry.addIcon("SideDucts", "thermaldynamics:duct/sideDucts", paramIIconRegister);
    for (i = 0; i < TDDucts.ductList.size(); i++) {
      if (TDDucts.isValid(i)) {
        ((Duct)TDDucts.ductList.get(i)).registerIcons(paramIIconRegister);
      }
    }
    TDDucts.structureInvis.registerIcons(paramIIconRegister);
  }
  
  public ItemStack getPickBlock(MovingObjectPosition paramMovingObjectPosition, World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    TileTDBase localTileTDBase;
    ItemStack localItemStack;
    if ((subHit >= 14) && (subHit < 20))
    {
      localTileTDBase = (TileTDBase)paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
      localItemStack = attachments[(subHit - 14)].getPickBlock();
      if (localItemStack != null) {
        return localItemStack;
      }
    }
    if ((subHit >= 20) && (subHit < 26))
    {
      localTileTDBase = (TileTDBase)paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
      localItemStack = covers[(subHit - 20)].getPickBlock();
      if (localItemStack != null) {
        return localItemStack;
      }
    }
    return super.getPickBlock(paramMovingObjectPosition, paramWorld, paramInt1, paramInt2, paramInt3);
  }
  
  public int func_149645_b()
  {
    return TDProps.renderDuctId;
  }
  
  public boolean canRenderInPass(int paramInt)
  {
    cofh.core.block.BlockCoFHBase.renderPass = paramInt;
    return paramInt < 2;
  }
  
  public int func_149701_w()
  {
    return 1;
  }
  
  public Block getVisualBlock(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    TileEntity localTileEntity = paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileEntity instanceof TileTDBase))
    {
      Cover localCover = covers[paramForgeDirection.ordinal()];
      if (localCover != null) {
        return block;
      }
    }
    return this;
  }
  
  public int getVisualMeta(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection)
  {
    TileEntity localTileEntity = paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileEntity instanceof TileTDBase))
    {
      Cover localCover = covers[paramForgeDirection.ordinal()];
      if (localCover != null) {
        return meta;
      }
    }
    return paramIBlockAccess.getBlockMetadata(paramInt1, paramInt2, paramInt3);
  }
  
  public boolean supportsVisualConnections()
  {
    return true;
  }
  
  public boolean openConfigGui(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, ForgeDirection paramForgeDirection, EntityPlayer paramEntityPlayer)
  {
    TileTDBase localTileTDBase = (TileTDBase)paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileTDBase instanceof IBlockConfigGui)) {
      return ((IBlockConfigGui)localTileTDBase).openConfigGui(paramIBlockAccess, paramInt1, paramInt2, paramInt3, paramForgeDirection, paramEntityPlayer);
    }
    int i = paramForgeDirection.ordinal();
    Object localObject;
    if ((paramIBlockAccess instanceof World))
    {
      localObject = RayTracer.retraceBlock((World)paramIBlockAccess, paramEntityPlayer, paramInt1, paramInt2, paramInt3);
      if (localObject == null) {
        return false;
      }
      if ((i > 13) && (i < 20)) {
        i = subHit - 14;
      }
    }
    if ((i > 13) && (i < 20))
    {
      localObject = attachments[(i - 14)];
      if ((localObject instanceof IBlockConfigGui)) {
        return ((IBlockConfigGui)localObject).openConfigGui(paramIBlockAccess, paramInt1, paramInt2, paramInt3, paramForgeDirection, paramEntityPlayer);
      }
    }
    return false;
  }
  
  public static enum ConnectionTypes
  {
    NONE(false),  DUCT,  TILECONNECTION,  STRUCTURE,  CLEANDUCT;
    
    private final boolean renderDuct;
    
    private ConnectionTypes()
    {
      renderDuct = true;
    }
    
    private ConnectionTypes(boolean paramBoolean)
    {
      renderDuct = paramBoolean;
    }
    
    public boolean renderDuct()
    {
      return renderDuct;
    }
  }
  
  public void func_149689_a(World paramWorld, int paramInt1, int paramInt2, int paramInt3, EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack)
  {
    super.func_149689_a(paramWorld, paramInt1, paramInt2, paramInt3, paramEntityLivingBase, paramItemStack);
    TileEntity localTileEntity = paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileEntity instanceof TileTDBase)) {
      ((TileTDBase)localTileEntity).onPlacedBy(paramEntityLivingBase, paramItemStack);
    }
  }
  
  public ArrayList<ItemStack> dismantleBlock(EntityPlayer paramEntityPlayer, NBTTagCompound paramNBTTagCompound, World paramWorld, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    TileEntity localTileEntity = paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
    int i = paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3);
    ItemStack localItemStack1;
    if ((localTileEntity instanceof TileTDBase)) {
      localItemStack1 = ((TileTDBase)localTileEntity).getDrop();
    } else {
      localItemStack1 = new ItemStack(this, 1, i);
    }
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(localItemStack1);
    if ((localTileEntity instanceof TileTDBase))
    {
      TileTDBase localTileTDBase = (TileTDBase)localTileEntity;
      Object localObject2;
      for (localObject2 : attachments) {
        if (localObject2 != null) {
          localArrayList.addAll(((Attachment)localObject2).getDrops());
        }
      }
      for (localObject2 : covers) {
        if (localObject2 != null) {
          localArrayList.addAll(((Cover)localObject2).getDrops());
        }
      }
      localTileTDBase.dropAdditional(localArrayList);
    }
    if (paramNBTTagCompound != null) {
      localItemStack1.setTagCompound(paramNBTTagCompound);
    }
    if (!paramBoolean2)
    {
      if ((localTileEntity instanceof TileCoFHBase)) {
        ((TileCoFHBase)localTileEntity).blockDismantled();
      }
      paramWorld.setBlockToAir(paramInt1, paramInt2, paramInt3);
      if (!paramBoolean1)
      {
        float f = 0.3F;
        for (??? = localArrayList.iterator(); ((Iterator)???).hasNext();)
        {
          ItemStack localItemStack2 = (ItemStack)((Iterator)???).next();
          double d1 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
          double d2 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
          double d3 = rand.nextFloat() * f + (1.0F - f) * 0.5D;
          EntityItem localEntityItem = new EntityItem(paramWorld, paramInt1 + d1, paramInt2 + d2, paramInt3 + d3, localItemStack2);
          delayBeforeCanPickup = 10;
          paramWorld.spawnEntityInWorld(localEntityItem);
        }
        if (paramEntityPlayer != null) {
          CoreUtils.dismantleLog(paramEntityPlayer.getCommandSenderName(), this, i, paramInt1, paramInt2, paramInt3);
        }
      }
    }
    return localArrayList;
  }
  
  @SideOnly(Side.CLIENT)
  public void func_149734_b(World paramWorld, int paramInt1, int paramInt2, int paramInt3, Random paramRandom)
  {
    super.func_149734_b(paramWorld, paramInt1, paramInt2, paramInt3, paramRandom);
    
    TileEntity localTileEntity = paramWorld.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileEntity instanceof TileTDBase)) {
      ((TileTDBase)localTileEntity).randomDisplayTick();
    }
  }
  
  public float getSize(World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    return TDDucts.getDuct(offset + paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3)).isLargeTube() ? 0.05F : super.getSize(paramWorld, paramInt1, paramInt2, paramInt3);
  }
  
  public boolean preInit()
  {
    GameRegistry.registerBlock(this, ItemBlockDuct.class, "ThermalDynamics_" + offset);
    for (int i = 0; i < 16; i++) {
      if (TDDucts.isValid(offset + i)) {
        getTypeoffset + i).itemStack = new ItemStack(this, 1, i);
      }
    }
    return true;
  }
  
  public boolean initialize()
  {
    MinecraftForge.EVENT_BUS.register(this);
    if (offset != 0) {
      return true;
    }
    GameRegistry.registerTileEntity(TileEnergyDuct.class, "thermaldynamics.FluxDuct");
    GameRegistry.registerTileEntity(TileEnergyDuctSuper.class, "thermaldynamics.FluxDuctSuperConductor");
    
    EnergyGrid.initialize();
    SubTileEnergyRedstone.initialize();
    
    PacketHandler.instance.registerPacket(PacketFluid.class);
    GameRegistry.registerTileEntity(TileFluidDuct.class, "thermaldynamics.FluidDuct");
    GameRegistry.registerTileEntity(TileFluidDuctFragile.class, "thermaldynamics.FluidDuctFragile");
    GameRegistry.registerTileEntity(TileFluidDuctFlux.class, "thermaldynamics.FluidDuctFlux");
    GameRegistry.registerTileEntity(TileFluidDuctSuper.class, "thermaldynamics.FluidDuctSuper");
    
    GameRegistry.registerTileEntity(TileItemDuct.class, "thermaldynamics.ItemDuct");
    GameRegistry.registerTileEntity(TileItemDuctEnder.class, "thermaldynamics.ItemDuctEnder");
    GameRegistry.registerTileEntity(TileItemDuctFlux.class, "thermaldynamics.ItemDuctFlux");
    
    GameRegistry.registerTileEntity(TileStructuralDuct.class, "thermaldynamics.StructuralDuct");
    
    GameRegistry.registerTileEntity(TileTransportDuct.class, "thermaldynamics.TransportDuct");
    GameRegistry.registerTileEntity(TileTransportDuctLongRange.class, "thermaldynamics.TransportDuctLongRange");
    GameRegistry.registerTileEntity(TileTransportDuctCrossover.class, "thermaldynamics.TransportDuctCrossover");
    EntityRegistry.registerModEntity(EntityTransport.class, "Transport", 0, ThermalDynamics.instance, 64, 1, true);
    MinecraftForge.EVENT_BUS.register(TransportHandler.INSTANCE);
    FMLCommonHandler.instance().bus().register(TransportHandler.INSTANCE);
    
    return true;
  }
  
  public boolean postInit()
  {
    return true;
  }
  
  public int func_149709_b(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    TileTDBase localTileTDBase = (TileTDBase)paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    if ((localTileTDBase != null) && (attachments[(paramInt4 ^ 0x1)] != null)) {
      return attachments[(paramInt4 ^ 0x1)].getRSOutput();
    }
    return 0;
  }
  
  public int func_149748_c(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return 0;
  }
  
  public boolean canConnectRedstone(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt4 == -1) {
      return false;
    }
    int i;
    if (paramInt4 == 0) {
      i = 2;
    } else if (paramInt4 == 1) {
      i = 5;
    } else if (paramInt4 == 2) {
      i = 3;
    } else {
      i = 4;
    }
    TileTDBase localTileTDBase = (TileTDBase)paramIBlockAccess.getTileEntity(paramInt1, paramInt2, paramInt3);
    return (localTileTDBase != null) && (attachments[i] != null) && (attachments[i].shouldRSConnect());
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\duct\BlockDuct.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */