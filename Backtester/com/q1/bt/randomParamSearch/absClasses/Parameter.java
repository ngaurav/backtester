package com.q1.bt.randomParamSearch.absClasses;

import com.q1.bt.randomParamSearch.enums.DistributionTypes;
import java.util.Random;

public abstract class Parameter
{
  protected DistributionTypes distribution_type;
  
  public abstract double getNext(Random paramRandom);
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/randomParamSearch/absClasses/Parameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */