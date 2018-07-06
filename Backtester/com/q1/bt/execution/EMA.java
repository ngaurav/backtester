package com.q1.bt.execution;

import java.io.Serializable;

public class EMA
  implements Serializable
{
  private Double ema;
  private Double alpha;
  
  public EMA(int period)
  {
    this.alpha = Double.valueOf(2.0D / (period + 1.0D));
    this.ema = null;
  }
  

  public Double calculateEMA(double val)
  {
    if (this.ema == null) {
      this.ema = Double.valueOf(val);
    } else {
      this.ema = Double.valueOf(val * this.alpha.doubleValue() + this.ema.doubleValue() * (1.0D - this.alpha.doubleValue()));
    }
    return this.ema;
  }
  
  public Double getEMA()
  {
    return this.ema;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/EMA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */