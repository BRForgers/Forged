package cofh.thermaldynamics.multiblock;

import cofh.thermaldynamics.core.WorldGridList;
import com.google.common.math.DoubleMath;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public abstract class MultiBlockGridTracking
  extends MultiBlockGrid
{
  private Tracker tracker;
  
  public MultiBlockGridTracking(WorldGridList paramWorldGridList)
  {
    super(paramWorldGridList);
  }
  
  public MultiBlockGridTracking(World paramWorld)
  {
    super(paramWorld);
  }
  
  public Tracker getTracker()
  {
    if (tracker == null) {
      tracker = new Tracker(getLevel());
    }
    tracker.life = 0;
    return tracker;
  }
  
  public abstract int getLevel();
  
  public int trackIn(int paramInt, boolean paramBoolean)
  {
    if ((!paramBoolean) && (tracker != null)) {
      tracker.stuffIn(paramInt);
    }
    return paramInt;
  }
  
  public int trackOut(int paramInt, boolean paramBoolean)
  {
    if ((!paramBoolean) && (tracker != null)) {
      tracker.stuffOut(paramInt);
    }
    return paramInt;
  }
  
  public int trackInOut(int paramInt, boolean paramBoolean)
  {
    if ((!paramBoolean) && (tracker != null))
    {
      tracker.stuffIn(paramInt);
      tracker.stuffOut(paramInt);
    }
    return paramInt;
  }
  
  public void tickGrid()
  {
    super.tickGrid();
    if (tracker != null)
    {
      tracker.newTick(getLevel());
      if (tracker.life > 3600) {
        tracker = null;
      }
    }
  }
  
  public void addInfo(List<IChatComponent> paramList, EntityPlayer paramEntityPlayer, boolean paramBoolean)
  {
    super.addInfo(paramList, paramEntityPlayer, paramBoolean);
    addInfo(paramList, "tracker.cur", format(getLevel()));
    if (tracker == null)
    {
      paramList.add(new ChatComponentTranslation("info.thermaldynamics.info.tracker.activate", new Object[0]));
      getTracker();
      return;
    }
    tracker.life = 0;
    
    addInfo(paramList, "tracker.avg", format(tracker.avgStuff()) + getUnit());
    addInfo(paramList, "tracker.avgInOut", String.format("+%s%s/-%s%s", new Object[] { format(tracker.avgStuffIn()), getUnit(), format(tracker.avgStuffOut()), getUnit() }));
  }
  
  private String format(double paramDouble)
  {
    if (paramDouble == 0.0D) {
      return "0";
    }
    if (DoubleMath.isMathematicalInteger(paramDouble)) {
      return Integer.toString((int)paramDouble);
    }
    return String.format("%.2f", new Object[] { Double.valueOf(paramDouble) });
  }
  
  protected abstract String getUnit();
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\MultiBlockGridTracking.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */