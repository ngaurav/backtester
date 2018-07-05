/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.bt.data.DataDriver;
/*     */ import com.q1.bt.data.DataTypeViewer;
/*     */ import com.q1.bt.data.ScripDataViewer;
/*     */ import com.q1.bt.data.classes.Contract;
/*     */ import com.q1.bt.data.classes.ContractData;
/*     */ import com.q1.bt.data.classes.MetaData;
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class DailyDataReader
/*     */ {
/*     */   Long currentDate;
/*     */   Contract cur1DData;
/*     */   MetaData curMDData;
/*     */   Contract prev1DData;
/*     */   MetaData prevMDData;
/*     */   private Long mtmDate;
/*  23 */   private boolean startFileFlag = true;
/*     */   
/*     */   com.q1.bt.global.BacktesterGlobal btGlobal;
/*     */   
/*     */   Backtest backtest;
/*     */   DataDriver dataDriver;
/*     */   Scrip scrip;
/*     */   
/*     */   public DailyDataReader(Long startDate, com.q1.bt.global.BacktesterGlobal btGlobal, Backtest backtest, Scrip scrip, String scripListName)
/*     */     throws java.io.IOException
/*     */   {
/*  34 */     this.btGlobal = btGlobal;
/*  35 */     this.backtest = backtest;
/*  36 */     this.scrip = scrip;
/*     */     
/*     */ 
/*     */ 
/*  40 */     ArrayList<Scrip> scripSet = new ArrayList();
/*  41 */     scripSet.add(scrip);
/*     */     try
/*     */     {
/*  44 */       this.dataDriver = new DataDriver(btGlobal, startDate, backtest, scripListName, scripSet, "1D");
/*     */     } catch (Exception e) {
/*  46 */       System.out.println("Error in creation of data driver in ML Stage");
/*  47 */       e.printStackTrace();
/*     */     }
/*  49 */     this.currentDate = Long.valueOf(0L);
/*  50 */     this.cur1DData = null;
/*  51 */     this.curMDData = null;
/*  52 */     this.prev1DData = null;
/*  53 */     this.prevMDData = null;
/*  54 */     this.mtmDate = Long.valueOf(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void processFirstDateData()
/*     */     throws Exception
/*     */   {
/*  63 */     if ((this.cur1DData == null) && (this.startFileFlag)) {
/*  64 */       this.startFileFlag = false;
/*     */       
/*  66 */       this.dataDriver.updateData();
/*  67 */       this.dataDriver.updateDataViewers();
/*     */       
/*  69 */       DataTypeViewer dailyDataTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("1D");
/*  70 */       ContractData dailyContractData = ((ScripDataViewer)dailyDataTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).contractData;
/*  71 */       this.prev1DData = dailyContractData.contract;
/*     */       
/*  73 */       DataTypeViewer metaDtaTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("MD");
/*  74 */       this.prevMDData = ((ScripDataViewer)metaDtaTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).metaData;
/*     */       
/*  76 */       if (this.prev1DData == null) {
/*  77 */         System.out.println("No data in Daily File");
/*  78 */         return;
/*     */       }
/*  80 */       if (this.prevMDData == null) {
/*  81 */         System.out.println("No data in Metadata File");
/*  82 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateDataDriver()
/*     */   {
/*  89 */     this.dataDriver.updateDataViewers();
/*  90 */     DataTypeViewer dailyDataTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("1D");
/*  91 */     ContractData dailyContractData = ((ScripDataViewer)dailyDataTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).contractData;
/*  92 */     this.cur1DData = dailyContractData.contract;
/*     */     
/*  94 */     DataTypeViewer metaDtaTypeViewer = (DataTypeViewer)this.dataDriver.dataTypeViewerMap.get("MD");
/*  95 */     this.curMDData = ((ScripDataViewer)metaDtaTypeViewer.scripDataViewerMap.get(this.scrip.scripID)).metaData;
/*     */     
/*  97 */     this.currentDate = this.dataDriver.curDate;
/*     */   }
/*     */   
/*     */   public void process(Long dataDate, CandleData data) throws Exception
/*     */   {
/* 102 */     process(dataDate, data, null);
/*     */   }
/*     */   
/*     */   public void process(Long dataDate, CandleData data, Double mtm) throws Exception {
/* 106 */     processFirstDateData();
/* 107 */     if (this.currentDate.longValue() > dataDate.longValue())
/* 108 */       return;
/* 109 */     if (this.currentDate.longValue() == dataDate.longValue()) {
/* 110 */       assignData(dataDate, data, mtm);
/* 111 */       if (data.getClass() == CandleData.class) {
/* 112 */         this.mtmDate = dataDate;
/* 113 */         this.prev1DData = this.cur1DData;
/* 114 */         this.prevMDData = this.curMDData;
/*     */       }
/* 116 */       return; }
/* 117 */     if (dataDate.longValue() == 99999999L) {
/* 118 */       assignData(dataDate, data, mtm);
/* 119 */       return;
/*     */     }
/*     */     
/*     */ 
/* 123 */     while (this.dataDriver.updateData()) {
/* 124 */       updateDataDriver();
/*     */       
/* 126 */       if (this.currentDate.longValue() < dataDate.longValue()) {
/* 127 */         this.prev1DData = this.cur1DData;
/* 128 */         this.prevMDData = this.curMDData;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 133 */         if (this.currentDate.longValue() == dataDate.longValue())
/*     */         {
/* 135 */           assignData(dataDate, data, mtm);
/* 136 */           if (data.getClass() == CandleData.class)
/*     */           {
/* 138 */             this.mtmDate = dataDate;
/* 139 */             this.prev1DData = this.cur1DData;
/* 140 */             this.prevMDData = this.curMDData;
/*     */           }
/*     */         }
/* 143 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void assignData(Long dataDate, CandleData data, Double mtm)
/*     */   {
/* 150 */     HashMap<String, String> metaDataMap = new HashMap();
/*     */     
/* 152 */     Boolean rollOver = Boolean.valueOf(this.prev1DData.rolloverCl.doubleValue() != -1.0D);
/*     */     Double roDiff;
/*     */     Double roDiff;
/* 155 */     if (rollOver.booleanValue()) {
/* 156 */       roDiff = Double.valueOf(this.prev1DData.rolloverCl.doubleValue() - this.prev1DData.cl.doubleValue());
/*     */     } else {
/* 158 */       roDiff = Double.valueOf(0.0D);
/*     */     }
/* 160 */     metaDataMap = this.prevMDData.dataMap;
/*     */     
/* 162 */     if (data.getClass().equals(CandleData.class))
/*     */     {
/* 164 */       data.updateData(this.prev1DData.op, this.prev1DData.hi, this.prev1DData.lo, this.prev1DData.cl, this.prev1DData.vol, roDiff, rollOver, 
/* 165 */         dataDate, this.prev1DData.exp, this.prev1DData.actualExp, this.prev1DData.rolloverExp, metaDataMap);
/*     */     }
/* 167 */     else if (data.getClass().equals(DailyData.class))
/*     */     {
/* 169 */       data.updateData(this.prev1DData.op, this.prev1DData.hi, this.prev1DData.lo, this.prev1DData.cl, this.prev1DData.vol, roDiff, rollOver, 
/* 170 */         dataDate, this.prev1DData.exp, this.prev1DData.actualExp, this.prev1DData.rolloverExp, mtm, metaDataMap);
/*     */     }
/*     */     else
/*     */     {
/* 174 */       System.err.println("Incoming class does not match with either DailyData or CandleData"); }
/*     */   }
/*     */   
/*     */   public Long getPrevDate() {
/* 178 */     return this.mtmDate;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/DailyDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */