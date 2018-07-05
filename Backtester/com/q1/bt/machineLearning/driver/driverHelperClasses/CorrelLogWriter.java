/*    */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*    */ 
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CorrelLogWriter
/*    */ {
/*    */   CSVWriter correlLogWriter;
/*    */   String mlPath;
/*    */   
/*    */   public CorrelLogWriter(String mlPath)
/*    */   {
/* 21 */     this.mlPath = mlPath;
/*    */   }
/*    */   
/*    */   public void writeCorrelLog(TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, ArrayList<String> scripList)
/*    */     throws IOException
/*    */   {
/* 27 */     this.correlLogWriter = new CSVWriter(this.mlPath + "\\DailyCorrelLog" + ".csv", false, ",");
/* 28 */     this.correlLogWriter.write("Date");
/* 29 */     Iterator localIterator2; for (Iterator localIterator1 = scripList.iterator(); localIterator1.hasNext(); 
/*    */         
/* 31 */         localIterator2.hasNext())
/*    */     {
/* 29 */       String scrip1 = (String)localIterator1.next();
/*    */       
/* 31 */       localIterator2 = scripList.iterator(); continue;String scrip2 = (String)localIterator2.next();
/* 32 */       this.correlLogWriter.write("," + scrip1);
/*    */     }
/*    */     
/* 35 */     this.correlLogWriter.write("\n");
/*    */     
/* 37 */     for (localIterator1 = scripList.iterator(); localIterator1.hasNext(); 
/* 38 */         localIterator2.hasNext())
/*    */     {
/* 37 */       String scrip1 = (String)localIterator1.next();
/* 38 */       localIterator2 = scripList.iterator(); continue;String scrip2 = (String)localIterator2.next();
/* 39 */       this.correlLogWriter.write("," + scrip2);
/*    */     }
/*    */     
/* 42 */     this.correlLogWriter.write("\n");
/* 43 */     for (Map.Entry<Long, HashMap<String, HashMap<String, Double>>> entry : correlVals.entrySet()) {
/* 44 */       this.correlLogWriter.write(((Long)entry.getKey()).toString());
/* 45 */       Iterator localIterator3; for (localIterator2 = scripList.iterator(); localIterator2.hasNext(); 
/* 46 */           localIterator3.hasNext())
/*    */       {
/* 45 */         String scrip1 = (String)localIterator2.next();
/* 46 */         localIterator3 = scripList.iterator(); continue;String scrip2 = (String)localIterator3.next();
/* 47 */         this.correlLogWriter.write("," + ((HashMap)((HashMap)entry.getValue()).get(scrip1)).get(scrip2));
/*    */       }
/*    */       
/* 50 */       this.correlLogWriter.write("\n");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CSVWriter getCorrelLogWriter()
/*    */   {
/* 58 */     return this.correlLogWriter;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/CorrelLogWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */