package petrieditor.modules.controllability;

import java.math.*;

public class Macierz {
  private BigInteger m[];
  private int ry, rx;

  public Macierz (int ry, int rx)
  {
    int i;
    
    this.ry = ry;
    this.rx = rx;
    m = new BigInteger[rx*ry];
    for (i=0; i<rx*ry; i++) m[i] = BigInteger.ZERO;
  }
  public Macierz (Macierz mac)
  {
    int i;

    rx = mac.rx;
    ry = mac.ry;
    m = new BigInteger[rx*ry];
    for (i=0; i<rx*ry; i++) m[i] = mac.m[i];
  }

  public final BigInteger get (int y, int x)
  {
    return m[y*rx+x];
  }
  public final void set (int y, int x, BigInteger w)
  {
    m[y*rx+x] = w;
  }
  public final void set (int y, int x, long w)
  {
    m[y*rx+x] = BigInteger.valueOf (w);
  }
  public final void add (int y, int x, long w)
  {
    m[y*rx+x] = m[y*rx+x].add (BigInteger.valueOf(w));
  }
  public final int get_rx ()
  {
    return rx;
  }
  public final int get_ry ()
  {
    return ry;
  }

  public int rzad () //niszczy macierz
  {
    int i, j, k, mk;
    BigInteger t, t2;

    for (i=0,j=0; i<rx&&j<ry; i++)
    {
      mk = -1;
      for (k=j; k<ry; k++)
        if (m[k*rx+i].signum() != 0)
        {
          mk = k;
          break;
        }
      if (mk < 0)
        continue;
      for (k=mk+1; k<ry; k++)
        if (m[k*rx+i].signum() != 0 && m[k*rx+i].abs().compareTo(m[mk*rx+i].abs()) < 0)
          mk = k;
      if (mk > j)
        for (k=i; k<rx; k++)
        {
          t = m[j*rx+k];
          m[j*rx+k] = m[mk*rx+k];
          m[mk*rx+k] = t;
        }
      t = m[j*rx+i];
      for (k=j+1; k<ry; k++)
      {
        t2 = m[k*rx+i];
        if (t2.signum() != 0)
        {
          m[k*rx+i] = BigInteger.ZERO;
          for (mk=i+1; mk<rx; mk++)
            m[k*rx+mk] = m[k*rx+mk].multiply(t).subtract(m[j*rx+mk].multiply(t2));
        }
      }
      j++;
    }
    return j;
  }
}
