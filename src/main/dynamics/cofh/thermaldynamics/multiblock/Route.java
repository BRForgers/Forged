package cofh.thermaldynamics.multiblock;

import cofh.repack.codechicken.lib.vec.BlockCoord;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.list.linked.TByteLinkedList;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Route
  implements Comparable<Route>
{
  public TByteLinkedList pathDirections = new TByteLinkedList();
  public IMultiBlockRoute endPoint;
  public int pathWeight = 0;
  public boolean routeFinished = false;
  public BlockCoord dest;
  public static final byte[] tmpBuffer = new byte['Ā'];
  
  public Route(IMultiBlockRoute paramIMultiBlockRoute)
  {
    endPoint = paramIMultiBlockRoute;
  }
  
  public Route(Route paramRoute, IMultiBlockRoute paramIMultiBlockRoute, byte paramByte, boolean paramBoolean)
  {
    pathDirections = new TByteLinkedList(pathDirections);
    pathWeight += paramIMultiBlockRoute.getWeight();
    endPoint = paramIMultiBlockRoute;
    
    pathDirections.add(paramByte);
    routeFinished = paramBoolean;
  }
  
  public Route(Route paramRoute, boolean paramBoolean)
  {
    pathDirections = new TByteLinkedList(pathDirections);
    pathWeight = pathWeight;
    endPoint = endPoint;
    
    routeFinished = true;
  }
  
  public Route(Route paramRoute)
  {
    pathDirections = new TByteLinkedList(pathDirections);
    pathWeight = pathWeight;
    endPoint = endPoint;
    
    routeFinished = routeFinished;
  }
  
  public int compareTo(Route paramRoute)
  {
    if (pathWeight < pathWeight) {
      return -1;
    }
    if (pathWeight > pathWeight) {
      return 1;
    }
    if (pathDirections.size() < pathDirections.size()) {
      return -1;
    }
    if (pathDirections.size() > pathDirections.size()) {
      return 1;
    }
    return 0;
  }
  
  public Route copy()
  {
    return new Route(this);
  }
  
  public byte getNextDirection()
  {
    return pathDirections.removeAt(0);
  }
  
  public boolean hasNextDirection()
  {
    return pathDirections.size() > 0;
  }
  
  public int getCurrentDirection()
  {
    return pathDirections.get(0);
  }
  
  public int checkNextDirection()
  {
    return pathDirections.get(1);
  }
  
  public int getLastSide()
  {
    return pathDirections.size() > 0 ? pathDirections.get(pathDirections.size() - 1) : 0;
  }
  
  public Route(byte[] paramArrayOfByte)
  {
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    try
    {
      byte[] arrayOfByte;
      Object localObject;
      int j;
      if (localByteArrayInputStream.read() == 0)
      {
        arrayOfByte = new byte[localByteArrayInputStream.available()];
        localByteArrayInputStream.read(arrayOfByte);
      }
      else
      {
        localObject = new ByteArrayOutputStream();
        GZIPInputStream localGZIPInputStream = new GZIPInputStream(localByteArrayInputStream);
        while ((j = localGZIPInputStream.read(tmpBuffer)) >= 0) {
          ((ByteArrayOutputStream)localObject).write(tmpBuffer, 0, j);
        }
        localGZIPInputStream.close();
        arrayOfByte = ((ByteArrayOutputStream)localObject).toByteArray();
      }
      for (int k : arrayOfByte)
      {
        byte b1 = (byte)(k & 0x7);
        if (b1 < 6) {
          pathDirections.add(b1);
        }
        byte b2 = (byte)(k >> 3);
        if (b2 < 6) {
          pathDirections.add(b2);
        }
      }
    }
    catch (IOException localIOException) {}
  }
  
  public byte[] toByteArray()
  {
    byte[] arrayOfByte = new byte[(pathDirections.size() + 1) / 2];
    int i = 0;
    
    TByteIterator localTByteIterator = pathDirections.iterator();
    while (localTByteIterator.hasNext())
    {
      arrayOfByte[i] = localTByteIterator.next();
      if (localTByteIterator.hasNext())
      {
        int tmp53_52 = i; byte[] tmp53_51 = arrayOfByte;tmp53_51[tmp53_52] = ((byte)(tmp53_51[tmp53_52] | localTByteIterator.next() << 3));
      }
      else
      {
        int tmp71_70 = i; byte[] tmp71_69 = arrayOfByte;tmp71_69[tmp71_70] = ((byte)(tmp71_69[tmp71_70] | 0x30));
      }
      i++;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      if (arrayOfByte.length <= 20)
      {
        localByteArrayOutputStream.write(0);
        localByteArrayOutputStream.write(arrayOfByte);
      }
      else
      {
        localByteArrayOutputStream.write(1);
        
        GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(localByteArrayOutputStream);
        localGZIPOutputStream.write(arrayOfByte);
        localGZIPOutputStream.close();
      }
    }
    catch (IOException localIOException) {}
    return localByteArrayOutputStream.toByteArray();
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\multiblock\Route.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */