/*    */ package com.q1.bt.execution;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ 
/*    */ public class ParamObject
/*    */ {
/*    */   public static java.util.ArrayList<String[]> getParameterList(String strategyPath, String strategyName) throws Exception
/*    */   {
/*  9 */     Class<?> stratClass = Class.forName(strategyPath + "." + strategyName);
/* 10 */     Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/* 11 */     Strategy stratTempObject = 
/* 12 */       (Strategy)stratTempConstructor.newInstance(new Object[0]);
/*    */     
/* 14 */     return stratTempObject.getParameterList();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ParamObject.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */