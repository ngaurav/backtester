package com.q1.bt.randomParamSearch.paramDist;

import com.q1.bt.randomParamSearch.absClasses.Parameter;
import com.q1.bt.randomParamSearch.enums.DistributionTypes;
import java.util.Random;

public class Constant extends Parameter
{
  double constant;
  
  public Constant(double val)
  {
    this.constant = val;
    this.distribution_type = DistributionTypes.CONSTANT;
  }
  
  public double getNext(Random rand)
  {
    return this.constant;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/paramDist/Constant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */