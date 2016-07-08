package cofh.thermaldynamics.multiblock;

import java.util.Arrays;

public class Tracker
{
  private static final byte MEMORY = 101;
  private static final double AVG_MULTIPLIER = 0.01D;
  public static final short LIFESPAN = 3600;
  byte i = 0;
  short life = 0;
  final int[] mem = new int[101];
  final long[] memIn = new long[101];
  final long[] memOut = new long[101];
  
  public Tracker(int paramInt)
  {
    Arrays.fill(mem, paramInt);
  }
  
  public void newTick(int paramInt)
  {
    i = ((byte)(i + 1));
    life = ((short)(life + 1));
    if (i >= 101) {
      i = 0;
    }
    mem[i] = paramInt;
    memIn[i] = 0L;
    memOut[i] = 0L;
  }
  
  public void stuffIn(int paramInt)
  {
    memIn[i] += paramInt;
  }
  
  public void stuffOut(int paramInt)
  {
    memOut[i] += paramInt;
  }
  
  public double avgStuff()
  {
    double d = 0.0D;
    for (int j = 0; j < mem.length; j++) {
      if (i != j)
      {
        int k = mem[j];
        d += k;
      }
    }
    return d * 0.01D;
  }
  
  public double avgStuffIn()
  {
    return getUnsignedLongAverage(memIn);
  }
  
  public double avgStuffOut()
  {
    return getUnsignedLongAverage(memOut);
  }
  
  public double getUnsignedLongAverage(long[] paramArrayOfLong)
  {
    double d1 = 0.0D;
    for (int j = 0; j < paramArrayOfLong.length; j++) {
      if (j != i)
      {
        long l = paramArrayOfLong[j];
        if (l < 0L)
        {
          double d2 = l & 0x7FFFFFFFFFFFFFFF;
          d2 += 9.223372036854776E18D;
          d1 += d2;
        }
        else
        {
          d1 += l;
        }
      }
    }
    return d1 * 0.01D;
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\Tracker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */