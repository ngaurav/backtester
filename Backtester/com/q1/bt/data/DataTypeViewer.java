/*     */ package com.q1.bt.data;
/*     */ 
/*     */ import com.q1.bt.data.classes.ContractData;
/*     */ import com.q1.bt.data.classes.FundaData;
/*     */ import com.q1.bt.data.classes.MetaData;
/*     */ import com.q1.bt.data.classes.PreProcessData;
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.sql.SQLdata;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ public class DataTypeViewer
/*     */ {
/*  19 */   public HashMap<String, ScripDataViewer> scripDataViewerMap = new HashMap();
/*     */   
/*     */   public String dataType;
/*     */   
/*     */   public int scripCount;
/*     */   public Long dateTime;
/*     */   public Long date;
/*     */   public Long time;
/*  27 */   public boolean eof = false;
/*  28 */   public boolean skipBacktest = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public DataTypeViewer(BacktesterGlobal btGlobal, String dataType, ArrayList<Scrip> scripSet, String strategyDataType)
/*     */     throws ClassNotFoundException, SQLException
/*     */   {
/*  35 */     this.scripCount = scripSet.size();
/*     */     
/*     */ 
/*  38 */     this.dataType = dataType;
/*     */     
/*     */ 
/*  41 */     for (Scrip scrip : scripSet)
/*     */     {
/*     */ 
/*  44 */       if (dataType.equals("FD"))
/*     */       {
/*     */ 
/*  47 */         FundaData fD = new FundaData(scrip.segmentName);
/*     */         
/*     */ 
/*  50 */         ScripDataViewer sDViewer = new ScripDataViewer(dataType, fD);
/*  51 */         this.scripDataViewerMap.put(scrip.scripID, sDViewer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*  59 */       else if (dataType.equals("PP"))
/*     */       {
/*     */ 
/*  62 */         PreProcessData pD = new PreProcessData(scrip);
/*     */         
/*     */ 
/*  65 */         ScripDataViewer sDViewer = new ScripDataViewer(dataType, pD);
/*  66 */         this.scripDataViewerMap.put(scrip.scripID, sDViewer);
/*     */ 
/*     */ 
/*     */       }
/*  70 */       else if (dataType.equals("MD"))
/*     */       {
/*     */ 
/*  73 */         SQLdata sqlObject = null;
/*  74 */         sqlObject = new SQLdata(btGlobal.loginParameter.getSqlIPAddress(), 
/*  75 */           btGlobal.loginParameter.getSqlDatabase(), btGlobal.loginParameter.getSqlUsername(), 
/*  76 */           btGlobal.loginParameter.getSqlPassword());
/*     */         
/*     */ 
/*  79 */         MetaData mD = new MetaData(scrip);
/*     */         
/*     */         try
/*     */         {
/*  83 */           mD.readMetaDataFromDatabase(sqlObject, strategyDataType);
/*     */         } catch (SQLException e) {
/*  85 */           e.printStackTrace();
/*  86 */           sqlObject.close();
/*  87 */           return;
/*     */         }
/*     */         
/*     */ 
/*  91 */         ScripDataViewer sDViewer = new ScripDataViewer(dataType, mD);
/*  92 */         this.scripDataViewerMap.put(scrip.scripID, sDViewer);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*  97 */         ContractData cD = new ContractData(scrip);
/*     */         
/*     */ 
/* 100 */         ScripDataViewer sDViewer = new ScripDataViewer(dataType, cD);
/* 101 */         this.scripDataViewerMap.put(scrip.scripID, sDViewer);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 108 */     this.dateTime = Long.valueOf(20910101235900L);
/* 109 */     this.date = Long.valueOf(0L);
/* 110 */     this.time = Long.valueOf(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateData(DataTypeHandler dtHandler)
/*     */   {
/* 117 */     this.dateTime = dtHandler.dateTime;
/* 118 */     this.date = Long.valueOf(this.dateTime.longValue() / 1000000L);
/* 119 */     this.time = Long.valueOf(this.dateTime.longValue() - this.date.longValue() * 1000000L);
/*     */     
/*     */ 
/* 122 */     for (Map.Entry<String, ScripDataViewer> entry : this.scripDataViewerMap.entrySet()) {
/* 123 */       String scripID = (String)entry.getKey();
/* 124 */       ScripDataViewer sDViewer = (ScripDataViewer)entry.getValue();
/* 125 */       ScripDataHandler sDHandler = (ScripDataHandler)dtHandler.scripDataHandlerMap.get(scripID);
/*     */       
/*     */ 
/* 128 */       if (sDHandler.dateTime.equals(dtHandler.dateTime)) {
/* 129 */         sDViewer.updateData(sDHandler);
/*     */       }
/*     */     }
/* 132 */     this.skipBacktest = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateData(Long dateTime)
/*     */   {
/* 140 */     this.dateTime = dateTime;
/*     */     
/*     */ 
/* 143 */     this.skipBacktest = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateEOF(boolean eof)
/*     */   {
/* 149 */     this.eof = eof;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataTypeViewer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */