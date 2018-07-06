package com.q1.bt.randomParamSearch.paramDist;

import java.util.Random;

public class Exp extends com.q1.bt.randomParamSearch.absClasses.Parameter
{
  double constant;
  double step;
  double scale;
  
  public Exp(double min, double max, double step) {
    this.constant = Math.log(min);
    this.step = step;
    this.scale = Math.log(max / min);
    this.distribution_type = com.q1.bt.randomParamSearch.enums.DistributionTypes.EXP;
  }
  
  public double getNext(Random rand)
  {
    int ret = (int)(Math.exp(rand.nextDouble() * this.scale + this.constant) / this.step);
    return this.step * ret;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/paramDist/Exp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */