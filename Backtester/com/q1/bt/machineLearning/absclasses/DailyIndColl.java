/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import com.q1.bt.machineLearning.utility.CandleData;
/*    */ import com.q1.bt.machineLearning.utility.DailyData;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class DailyIndColl
/*    */ {
/*    */   ArrayList<Factor> factorList;
/*    */   HashMap<String, CandleIndVar> normalizerMap;
/*    */   ArrayList<Factor> finalFactorList;
/*    */   
/*    */   public DailyIndColl(ArrayList<String> factorFilter, VarList VarListType, FactorType fType, ValueType vType) throws Exception
/*    */   {
/* 17 */     VarListType.populateNormalizerList();
/* 18 */     this.normalizerMap = VarListType.getNormalizerList();
/* 19 */     this.factorList = VarListType.getFactorList(this.normalizerMap);
/* 20 */     this.finalFactorList = new ArrayList();
/* 21 */     Iterator localIterator2; for (Iterator localIterator1 = factorFilter.iterator(); localIterator1.hasNext(); 
/* 22 */         localIterator2.hasNext())
/*    */     {
/* 21 */       String factorName = (String)localIterator1.next();
/* 22 */       localIterator2 = this.factorList.iterator(); continue;Factor factor = (Factor)localIterator2.next();
/* 23 */       if ((factor.factorName.equals(factorName)) && 
/* 24 */         (!factor.factorName.contains("1M")) && 
/* 25 */         (factor.dv.fType.equals(fType)) && 
/* 26 */         (factor.dv.vType.equals(vType))) {
/* 27 */         this.finalFactorList.add(factor);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public ArrayList<Factor> getFactorList()
/*    */   {
/* 34 */     return this.finalFactorList;
/*    */   }
/*    */   
/*    */   public HashMap<String, CandleIndVar> getNormaLizeMap() {
/* 38 */     return this.normalizerMap;
/*    */   }
/*    */   
/*    */   public ArrayList<Double[]> process(DailyData[] dd, Long date)
/*    */     throws java.io.IOException
/*    */   {
/* 44 */     if (dd[0].date == null)
/* 45 */       return null;
/* 46 */     ArrayList<Double[]> indValues = new ArrayList();
/*    */     
/*    */ 
/* 49 */     for (CandleIndVar normalizer : this.normalizerMap.values()) {
/* 50 */       if (dd[0].date == date) {
/* 51 */         normalizer.updateInd(dd);
/*    */       }
/*    */     }
/* 54 */     if (this.finalFactorList.size() == 0) {
/* 55 */       return null;
/*    */     }
/* 57 */     for (Factor factor : this.finalFactorList)
/*    */     {
/* 59 */       if (dd[0].date == date) {
/* 60 */         factor.updateInd(dd);
/* 61 */         Double[] indVal = factor.getValue();
/* 62 */         indValues.add(indVal);
/*    */       } else {
/* 64 */         return null;
/*    */       }
/*    */     }
/* 67 */     return indValues;
/*    */   }
/*    */   
/*    */   public ArrayList<Double[]> process(CandleData[] cd, Long date) throws java.io.IOException
/*    */   {
/* 72 */     if (cd[0].date == null)
/* 73 */       return null;
/* 74 */     ArrayList<Double[]> indValues = new ArrayList();
/*    */     
/* 76 */     for (CandleIndVar normalizer : this.normalizerMap.values()) {
/* 77 */       if (cd[0].date == date) {
/* 78 */         normalizer.updateInd(cd);
/*    */       }
/*    */     }
/*    */     
/* 82 */     for (Factor factor : this.finalFactorList)
/*    */     {
/* 84 */       if (cd[0].date == date) {
/* 85 */         factor.updateInd(cd);
/* 86 */         Double[] indVal = factor.getValue();
/* 87 */         indValues.add(indVal);
/*    */       } else {
/* 89 */         return null;
/*    */       }
/*    */     }
/* 92 */     return indValues;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/DailyIndColl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */