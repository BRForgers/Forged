package cofh.thermaldynamics.block;

import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.ITileInfo;
import cofh.core.block.BlockCoFHBase;
import cofh.lib.util.helpers.ServerHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.repack.codechicken.lib.raytracer.RayTracer;
import cofh.repack.codechicken.lib.vec.BlockCoord;
import cofh.repack.codechicken.lib.vec.Vector3;
import cofh.thermaldynamics.duct.attachments.cover.Cover;
import cofh.thermaldynamics.duct.entity.EntityTransport;
import cofh.thermaldynamics.util.Utils;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventBus;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public abstract class BlockTDBase
  extends BlockCoFHBase
{
  protected BlockTDBase(Material paramMaterial)
  {
    super(paramMaterial);
  }
  
  public TileEntity func_149915_a(World paramWorld, int paramInt)
  {
    return null;
  }
  
  public float getSize(World paramWorld, int paramInt1, int paramInt2, int paramInt3)
  {
    return 0.3F;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\block\BlockTDBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */