package cofh.thermaldynamics.debughelper;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class NoComodSet<E>
  extends AbstractSet<E>
{
  public final Set<E> set;
  int modCount = 0;
  
  public NoComodSet()
  {
    this(new LinkedHashSet());
  }
  
  public NoComodSet(Set<E> paramSet)
  {
    set = paramSet;
  }
  
  public boolean add(E paramE)
  {
    updateModCount();
    return set.add(paramE);
  }
  
  public void updateModCount()
  {
    modCount += 1;
  }
  
  public void clear()
  {
    updateModCount();
    set.clear();
  }
  
  public boolean contains(Object paramObject)
  {
    return set.contains(paramObject);
  }
  
  public boolean remove(Object paramObject)
  {
    updateModCount();
    return set.remove(paramObject);
  }
  
  public Iterator<E> iterator()
  {
    return new NoComodIterator(set.iterator());
  }
  
  public int size()
  {
    return set.size();
  }
  
  public class NoComodIterator
    implements Iterator<E>
  {
    private final Iterator<E> iterator;
    int expectedModCount;
    
    public NoComodIterator()
    {
      Iterator localIterator;
      iterator = localIterator;
      expectedModCount = modCount;
    }
    
    public boolean hasNext()
    {
      return (expectedModCount == modCount) && (iterator.hasNext());
    }
    
    public E next()
    {
      return (E)iterator.next();
    }
    
    public void remove()
    {
      iterator.remove();
    }
  }
}


/* Location:              C:\Users\adria\Downloads\ThermalDynamics-[1.7.10]1.2.0-171.deobf.jar!\cofh\thermaldynamics\debughelper\NoComodSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */