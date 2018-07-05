/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class MLAlgoRA extends MLAlgo
/*    */ {
/*    */   public ArrayList<String[]> paramList;
/*    */   
/*    */   public MLAlgoRA()
/*    */   {
/* 11 */     this.paramList = new ArrayList();
/*    */   }
/*    */   
/*    */   public abstract String getModelPackage();
/*    */   
/*    */   public abstract String getModelName();
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/MLAlgoRA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */