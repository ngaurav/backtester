package com.q1.bt.risk;

import java.lang.reflect.Constructor;


public class RMObject
{
  public static ConsolRM createConsolRMObject(String riskPackage, String className)
    throws Exception
  {
    Class<?> stratClass = Class.forName(riskPackage + "." + className);
    Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
    ConsolRM object = (ConsolRM)constructor.newInstance(new Object[0]);
    return object;
  }
  

  public static StrategyRM createStrategyRMObject(String riskPackage, String className, String strategy)
    throws Exception
  {
    Class<?> stratClass = Class.forName(riskPackage + "." + className);
    Constructor<?> constructor = stratClass.getConstructor(new Class[] { String.class });
    StrategyRM object = (StrategyRM)constructor.newInstance(new Object[] { strategy });
    return object;
  }
  

  public static ScripStratRM createScripStratRMObject(String riskPackage, String className, String strategy, String scrip)
    throws Exception
  {
    Class<?> stratClass = Class.forName(riskPackage + "." + className);
    Constructor<?> constructor = stratClass.getConstructor(new Class[] { String.class, String.class });
    ScripStratRM object = (ScripStratRM)constructor.newInstance(new Object[] { strategy, scrip });
    return object;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/RMObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */