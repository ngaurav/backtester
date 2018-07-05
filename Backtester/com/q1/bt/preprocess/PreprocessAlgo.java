/*    */ package com.q1.bt.preprocess;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class PreprocessAlgo { public ArrayList<String> paramList;
/*    */   
/*  7 */   public PreprocessAlgo() { this.paramList = new ArrayList(); }
/*    */   
/*    */ 
/*    */   public String getparamList()
/*    */   {
/* 12 */     String paramString = "";
/* 13 */     for (String param : this.paramList) {
/* 14 */       paramString = paramString + "_" + param;
/*    */     }
/* 16 */     return paramString;
/*    */   }
/*    */   
/*    */   public abstract String getModelPackage();
/*    */   
/*    */   public abstract String getModelName();
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreprocessAlgo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */