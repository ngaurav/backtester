/*    */ package com.q1.bt.execution;
/*    */ 
/*    */ import com.q1.bt.data.DataTypeViewer;
/*    */ import com.q1.bt.data.ScripDataViewer;
/*    */ import com.q1.bt.data.classes.ContractData;
/*    */ import com.q1.bt.data.classes.MetaData;
/*    */ import java.io.Serializable;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutionData
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public String scripListID;
/*    */   public String scripID;
/*    */   public Long dateTime;
/*    */   public Long date;
/*    */   public String dataType;
/*    */   public ContractData mainContractData;
/*    */   public ContractData rolloverContractData;
/*    */   public MetaData metaData;
/* 31 */   public Long prevOrderTimestamp = Long.valueOf(0L);
/*    */   
/*    */   public ExecutionData(ExecutionData execData)
/*    */   {
/* 35 */     this.scripListID = execData.scripListID;
/* 36 */     this.scripID = execData.scripID;
/* 37 */     this.dateTime = execData.dateTime;
/* 38 */     this.date = execData.date;
/* 39 */     this.dataType = execData.dataType;
/* 40 */     this.mainContractData = new ContractData(execData.mainContractData);
/* 41 */     this.rolloverContractData = new ContractData(execData.rolloverContractData);
/* 42 */     this.metaData = new MetaData(execData.metaData);
/* 43 */     this.prevOrderTimestamp = execData.prevOrderTimestamp;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExecutionData(String scripListID, String scripID, DataTypeViewer mainData, HashMap<String, DataTypeViewer> dataViewerMap, Long prevOrderTimestamp)
/*    */   {
/* 51 */     this.scripListID = scripListID;
/*    */     
/*    */ 
/* 54 */     this.scripID = scripID;
/*    */     
/*    */ 
/* 57 */     this.dataType = mainData.dataType;
/*    */     
/*    */ 
/* 60 */     DataTypeViewer metaDataViewer = (DataTypeViewer)dataViewerMap.get("MD");
/*    */     
/*    */ 
/* 63 */     this.dateTime = mainData.dateTime;
/* 64 */     this.date = mainData.date;
/*    */     
/*    */ 
/* 67 */     this.prevOrderTimestamp = prevOrderTimestamp;
/*    */     
/*    */ 
/* 70 */     this.mainContractData = ((ScripDataViewer)mainData.scripDataViewerMap.get(scripID)).contractData;
/*    */     
/*    */ 
/* 73 */     if (this.dataType.contains("M")) {
/* 74 */       DataTypeViewer dailyData = (DataTypeViewer)dataViewerMap.get("1D");
/* 75 */       this.rolloverContractData = ((ScripDataViewer)dailyData.scripDataViewerMap.get(scripID)).contractData;
/* 76 */       this.date = ((ScripDataViewer)dailyData.scripDataViewerMap.get(scripID)).date;
/* 77 */     } else if (this.dataType.contains("D")) {
/* 78 */       this.rolloverContractData = this.mainContractData;
/*    */     }
/*    */     
/*    */ 
/* 82 */     this.metaData = ((ScripDataViewer)metaDataViewer.scripDataViewerMap.get(scripID)).metaData;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ExecutionData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */