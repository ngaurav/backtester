/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ public class MLFinalDecisionWriter
/*    */ {
/*    */   private String decisionFilePath;
/*    */   CSVWriter decisionFileWriter;
/*    */   HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap;
/*    */   
/*    */   public MLFinalDecisionWriter(String decisionFileLocation)
/*    */     throws Exception
/*    */   {
/* 19 */     this.decisionFilePath = (decisionFileLocation + "/" + "ML Decisions.csv");
/* 20 */     this.assetTimeStampDecisionMap = new HashMap();
/*    */     
/*    */     try
/*    */     {
/* 24 */       this.decisionFileWriter = new CSVWriter(this.decisionFilePath, false, ",");
/*    */       
/*    */ 
/* 27 */       this.decisionFileWriter.writeLine("TimeStamp, AssetName, Decision");
/*    */ 
/*    */     }
/*    */     catch (IOException e)
/*    */     {
/* 32 */       System.out.println("Unable to initialize writer for the ML Decision Writer : " + decisionFileLocation);
/* 33 */       throw new Exception();
/*    */     }
/*    */   }
/*    */   
/*    */   public void writeAndSaveInMemoryMLDecisions(HashMap<String, Boolean> result, Long tradeTS) throws Exception
/*    */   {
/* 39 */     for (Map.Entry<String, Boolean> assetDecisionMap : result.entrySet())
/*    */     {
/* 41 */       String assetName = (String)assetDecisionMap.getKey();
/* 42 */       Boolean decision = (Boolean)assetDecisionMap.getValue();
/*    */       
/* 44 */       TreeMap<Long, Boolean> timeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get(assetName);
/*    */       
/* 46 */       if (timeStampDecisionMap == null) {
/* 47 */         timeStampDecisionMap = new TreeMap();
/*    */       }
/* 49 */       timeStampDecisionMap.put(tradeTS, decision);
/* 50 */       this.assetTimeStampDecisionMap.put(assetName, timeStampDecisionMap);
/*    */       try {
/* 52 */         this.decisionFileWriter.writeLine(tradeTS + "," + assetName + "," + decision);
/*    */       }
/*    */       catch (IOException e) {
/* 55 */         System.out.println("Unable to write ML Decisions");
/* 56 */         throw new Exception();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void closeWriter() throws IOException
/*    */   {
/* 63 */     this.decisionFileWriter.close();
/*    */   }
/*    */   
/*    */   public HashMap<String, TreeMap<Long, Boolean>> getAssetTimeStampDecisionMap() {
/* 67 */     return this.assetTimeStampDecisionMap;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/MLFinalDecisionWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */