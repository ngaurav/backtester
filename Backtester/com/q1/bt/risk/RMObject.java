/*    */ package com.q1.bt.risk;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ 
/*    */ public class RMObject
/*    */ {
/*    */   public static ConsolRM createConsolRMObject(String riskPackage, String className)
/*    */     throws Exception
/*    */   {
/* 11 */     Class<?> stratClass = Class.forName(riskPackage + "." + className);
/* 12 */     Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
/* 13 */     ConsolRM object = (ConsolRM)constructor.newInstance(new Object[0]);
/* 14 */     return object;
/*    */   }
/*    */   
/*    */ 
/*    */   public static StrategyRM createStrategyRMObject(String riskPackage, String className, String strategy)
/*    */     throws Exception
/*    */   {
/* 21 */     Class<?> stratClass = Class.forName(riskPackage + "." + className);
/* 22 */     Constructor<?> constructor = stratClass.getConstructor(new Class[] { String.class });
/* 23 */     StrategyRM object = (StrategyRM)constructor.newInstance(new Object[] { strategy });
/* 24 */     return object;
/*    */   }
/*    */   
/*    */ 
/*    */   public static ScripStratRM createScripStratRMObject(String riskPackage, String className, String strategy, String scrip)
/*    */     throws Exception
/*    */   {
/* 31 */     Class<?> stratClass = Class.forName(riskPackage + "." + className);
/* 32 */     Constructor<?> constructor = stratClass.getConstructor(new Class[] { String.class, String.class });
/* 33 */     ScripStratRM object = (ScripStratRM)constructor.newInstance(new Object[] { strategy, scrip });
/* 34 */     return object;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/risk/RMObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */