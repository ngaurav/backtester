/*     */ package com.q1.bt.data;
/*     */ 
/*     */ import com.q1.bt.data.classes.ContractData;
/*     */ import com.q1.bt.data.classes.FundaData;
/*     */ import com.q1.bt.data.classes.MetaData;
/*     */ import com.q1.bt.data.classes.PreProcessData;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScripDataHandler
/*     */   extends Thread
/*     */ {
/*     */   CSVReader dataReader;
/*     */   String[] dataLine;
/*     */   String dataType;
/*  21 */   HashMap<String, Long> dateTimeMap = new HashMap();
/*     */   
/*     */   public Long date;
/*     */   
/*     */   public Long time;
/*     */   
/*     */   public Long dateTime;
/*  28 */   ContractData contractData = null;
/*  29 */   MetaData metaData = null;
/*  30 */   PreProcessData preData = null;
/*  31 */   FundaData fundaData = null;
/*     */   
/*     */   public Long startDate;
/*     */   
/*     */   public Long endDate;
/*     */   
/*     */   public int dataCount;
/*  38 */   public boolean eof = false;
/*  39 */   public boolean noUpdate = false;
/*  40 */   public boolean fileExists = true;
/*     */   
/*     */   public ScripDataHandler(String dataType, CSVReader dataReader, MetaData metaData, boolean fileExists, Object[] metaInfo)
/*     */   {
/*  44 */     this.dataReader = dataReader;
/*  45 */     this.metaData = metaData;
/*  46 */     this.fileExists = fileExists;
/*  47 */     this.dataType = dataType;
/*  48 */     processMetaInfo(metaInfo);
/*     */   }
/*     */   
/*     */   public ScripDataHandler(String dataType, CSVReader dataReader, ContractData contractData, Object[] metaInfo)
/*     */   {
/*  53 */     this.dataReader = dataReader;
/*  54 */     this.contractData = contractData;
/*  55 */     this.dataType = dataType;
/*  56 */     processMetaInfo(metaInfo);
/*     */   }
/*     */   
/*     */   public ScripDataHandler(String dataType, CSVReader dataReader, PreProcessData preData, Object[] metaInfo)
/*     */   {
/*  61 */     this.dataReader = dataReader;
/*  62 */     this.preData = preData;
/*  63 */     this.dataType = dataType;
/*  64 */     processMetaInfo(metaInfo);
/*     */   }
/*     */   
/*     */   public ScripDataHandler(String dataType, CSVReader dataReader, FundaData fundaData, Object[] metaInfo)
/*     */   {
/*  69 */     this.dataReader = dataReader;
/*  70 */     this.fundaData = fundaData;
/*  71 */     this.dataType = dataType;
/*  72 */     processMetaInfo(metaInfo);
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/*  77 */     this.dataReader.close();
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateData()
/*     */     throws Exception
/*     */   {
/*  84 */     if (this.dataType.equals("MD"))
/*     */     {
/*     */ 
/*  87 */       this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
/*     */       
/*     */ 
/*  90 */       this.metaData.addData(this.dataLine);
/*     */ 
/*     */ 
/*     */     }
/*  94 */     else if (this.dataType.equals("PP"))
/*     */     {
/*     */ 
/*  97 */       this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
/*     */       
/*     */ 
/* 100 */       this.preData.addData(this.dataLine);
/*     */ 
/*     */ 
/*     */     }
/* 104 */     else if (this.dataType.equals("FD"))
/*     */     {
/*     */ 
/* 107 */       this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
/*     */       
/*     */ 
/* 110 */       this.fundaData.addData(this.dataLine);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 117 */       if (this.dataType.contains("M")) {
/* 118 */         this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
/* 119 */         this.time = Long.valueOf(Long.parseLong(this.dataLine[1]));
/*     */ 
/*     */ 
/*     */       }
/* 123 */       else if (this.dataType.contains("D")) {
/* 124 */         this.date = Long.valueOf(Long.parseLong(this.dataLine[0]));
/*     */       }
/*     */       
/*     */ 
/* 128 */       this.contractData.addContractData(this.dataLine);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Long updateRolloverData(ScripDataHandler sdHandlerM)
/*     */   {
/* 135 */     this.dateTime = sdHandlerM.dateTime;
/* 136 */     this.time = sdHandlerM.time;
/* 137 */     this.noUpdate = false;
/*     */     
/* 139 */     return this.dateTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Long updateRolloverData(String[] forwardDateTime)
/*     */   {
/* 146 */     this.dateTime = Long.valueOf(Long.parseLong(forwardDateTime[0] + forwardDateTime[1]));
/* 147 */     this.time = Long.valueOf(Long.parseLong(forwardDateTime[1]));
/* 148 */     this.noUpdate = false;
/*     */     
/* 150 */     return this.dateTime;
/*     */   }
/*     */   
/*     */ 
/*     */   public void processMetaInfo(Object[] metaInfo)
/*     */   {
/* 156 */     this.startDate = ((Long)metaInfo[0]);
/* 157 */     this.endDate = ((Long)metaInfo[1]);
/* 158 */     this.dataCount = ((Integer)metaInfo[2]).intValue();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/ScripDataHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */