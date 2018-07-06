package com.q1.bt.randomParamSearch.paramDist;

import java.util.Random;

public class Uniform extends com.q1.bt.randomParamSearch.absClasses.Parameter
{
  double min;
  double max;
  double step;
  int scale;
  
  public Uniform(double min, double max, double step) {
    this.max = max;
    this.min = min;
    this.step = step;
    this.scale = ((int)((max - min) / step + 1.0D));
    this.distribution_type = com.q1.bt.randomParamSearch.enums.DistributionTypes.UNIFORM;
  }
  
  public double getNext(Random rand)
  {
    return rand.nextInt(this.scale) * this.step + this.min;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/paramDist/Uniform.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */