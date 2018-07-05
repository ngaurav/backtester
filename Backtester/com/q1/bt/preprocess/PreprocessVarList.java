/*    */ package com.q1.bt.preprocess;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public abstract class PreprocessVarList { protected ArrayList<PreprocessAlgo> preprocessVarlist;
/*    */   
/*  7 */   public PreprocessVarList() { this.preprocessVarlist = new ArrayList(); }
/*    */   
/*    */   public abstract void getPreprocessList();
/*    */   
/*    */   public ArrayList<String> getNames() {
/* 12 */     ArrayList<String> preProcessNameList = new ArrayList();
/* 13 */     for (PreprocessAlgo algo : this.preprocessVarlist) {
/* 14 */       String name = algo.getClass().getSimpleName() + algo.getparamList();
/* 15 */       preProcessNameList.add(name);
/*    */     }
/* 17 */     return preProcessNameList;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreprocessVarList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */