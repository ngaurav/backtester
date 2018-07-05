/*    */ package com.q1.bt.process;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProcessFlow
/*    */ {
/*    */   ArrayList<BacktesterProcess> processList;
/*    */   Integer currentIndex;
/* 15 */   public static HashMap<BacktesterProcess, Integer> tabIndexMap = new HashMap();
/* 16 */   static { tabIndexMap.put(BacktesterProcess.Login, Integer.valueOf(0));
/* 17 */     tabIndexMap.put(BacktesterProcess.Backtest, Integer.valueOf(1));
/* 18 */     tabIndexMap.put(BacktesterProcess.BatchProcess, Integer.valueOf(2));
/* 19 */     tabIndexMap.put(BacktesterProcess.MachineLearning, Integer.valueOf(3));
/* 20 */     tabIndexMap.put(BacktesterProcess.Results, Integer.valueOf(4));
/* 21 */     tabIndexMap.put(BacktesterProcess.RollingAnalysis, Integer.valueOf(5));
/* 22 */     tabIndexMap.put(BacktesterProcess.SensitivityAnalysis, Integer.valueOf(6));
/* 23 */     tabIndexMap.put(BacktesterProcess.IsOs, Integer.valueOf(7));
/* 24 */     tabIndexMap.put(BacktesterProcess.PostProcess, Integer.valueOf(8));
/*    */   }
/*    */   
/*    */   public ProcessFlow()
/*    */   {
/* 29 */     this.processList = new ArrayList();
/* 30 */     this.currentIndex = null;
/*    */   }
/*    */   
/*    */   public void add(BacktesterProcess process)
/*    */   {
/* 35 */     if (this.processList.isEmpty()) {
/* 36 */       this.processList.add(process);
/* 37 */       this.currentIndex = Integer.valueOf(0);
/*    */     } else {
/* 39 */       this.processList.add(process);
/*    */     }
/*    */   }
/*    */   
/*    */   public void update() {
/* 44 */     this.currentIndex = Integer.valueOf(this.currentIndex.intValue() + 1);
/*    */   }
/*    */   
/*    */   public void revert()
/*    */   {
/* 49 */     this.processList.remove(this.processList.size() - 1);
/* 50 */     this.currentIndex = Integer.valueOf(this.currentIndex.intValue() - 1);
/*    */   }
/*    */   
/*    */   public BacktesterProcess getCurrentProcess()
/*    */   {
/* 55 */     if ((this.currentIndex.intValue() > this.processList.size()) || (this.currentIndex.intValue() < 0))
/* 56 */       return null;
/* 57 */     return (BacktesterProcess)this.processList.get(this.currentIndex.intValue());
/*    */   }
/*    */   
/*    */   public Integer getCurrentTabIndex()
/*    */   {
/* 62 */     return (Integer)tabIndexMap.get(getCurrentProcess());
/*    */   }
/*    */   
/*    */   public Integer getProcessTabIndex(BacktesterProcess process)
/*    */   {
/* 67 */     return (Integer)tabIndexMap.get(process);
/*    */   }
/*    */   
/*    */   public BacktesterProcess getPreviousProcess()
/*    */   {
/* 72 */     Integer previousIndex = Integer.valueOf(this.currentIndex.intValue() - 1);
/* 73 */     if ((previousIndex.intValue() > this.processList.size()) || (previousIndex.intValue() < 0))
/* 74 */       return null;
/* 75 */     return (BacktesterProcess)this.processList.get(previousIndex.intValue());
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/ProcessFlow.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */