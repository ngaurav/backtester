/*    */ package com.q1.bt.data;
/*    */ 
/*    */ import com.q1.bt.data.classes.ContractData;
/*    */ import com.q1.bt.data.classes.FundaData;
/*    */ import com.q1.bt.data.classes.MetaData;
/*    */ import com.q1.bt.data.classes.PreProcessData;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScripDataViewer
/*    */   extends Thread
/*    */ {
/*    */   String dataType;
/* 16 */   HashMap<String, Long> dateTimeMap = new HashMap();
/*    */   
/*    */   public Long date;
/*    */   
/*    */   public Long time;
/*    */   
/*    */   public Long dateTime;
/* 23 */   public ContractData contractData = null;
/* 24 */   public MetaData metaData = null;
/* 25 */   PreProcessData preData = null;
/* 26 */   FundaData fundaData = null;
/*    */   
/*    */   public Long startDate;
/*    */   
/*    */   public Long endDate;
/*    */   
/*    */   public int dataCount;
/* 33 */   public boolean eof = false;
/* 34 */   public boolean noUpdate = false;
/* 35 */   public boolean fileExists = true;
/*    */   
/*    */   public ScripDataViewer(String dataType, MetaData metaData)
/*    */   {
/* 39 */     this.metaData = metaData;
/* 40 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */   public ScripDataViewer(String dataType, ContractData contractData)
/*    */   {
/* 45 */     this.contractData = contractData;
/* 46 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */   public ScripDataViewer(String dataType, PreProcessData preData)
/*    */   {
/* 51 */     this.preData = preData;
/* 52 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */   public ScripDataViewer(String dataType, FundaData fundaData)
/*    */   {
/* 57 */     this.fundaData = fundaData;
/* 58 */     this.dataType = dataType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void updateData(ScripDataHandler sdHandler)
/*    */   {
/* 65 */     this.date = sdHandler.date;
/* 66 */     this.time = sdHandler.time;
/* 67 */     this.dateTime = sdHandler.dateTime;
/*    */     
/*    */ 
/* 70 */     this.startDate = sdHandler.startDate;
/* 71 */     this.endDate = sdHandler.endDate;
/* 72 */     this.dataCount = sdHandler.dataCount;
/*    */     
/*    */ 
/* 75 */     if ((this.fileExists) && (this.dataType.equals("MD")))
/*    */     {
/*    */ 
/* 78 */       this.fileExists = sdHandler.fileExists;
/* 79 */       if (!this.fileExists) {
/* 80 */         return;
/*    */       }
/* 82 */       this.metaData.copyMetaData(sdHandler.metaData);
/*    */ 
/*    */ 
/*    */     }
/* 86 */     else if (this.dataType.equals("PP")) {
/* 87 */       this.preData = new PreProcessData(sdHandler.preData);
/*    */ 
/*    */     }
/* 90 */     else if (this.dataType.equals("FD")) {
/* 91 */       this.fundaData = new FundaData(sdHandler.fundaData);
/*    */     }
/*    */     else
/*    */     {
/* 95 */       this.contractData = new ContractData(sdHandler.contractData);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/ScripDataViewer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */