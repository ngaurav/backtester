/*     */ package com.q1.bt.data;
/*     */ 
/*     */ import com.q1.bt.data.classes.ContractData;
/*     */ import com.q1.bt.data.classes.FundaData;
/*     */ import com.q1.bt.data.classes.MetaData;
/*     */ import com.q1.bt.data.classes.PreProcessData;
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class DataTypeHandler
/*     */   extends Thread
/*     */ {
/*  17 */   public HashMap<String, ScripDataHandler> scripDataHandlerMap = new HashMap();
/*     */   
/*     */   String dataType;
/*     */   
/*     */   public int scripCount;
/*     */   public Long dateTime;
/*  23 */   public boolean eof = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public DataTypeHandler(String dataPath, String dataType, ArrayList<Scrip> scripSet, Long startDate, String strategyDataType)
/*     */     throws Exception
/*     */   {
/*  30 */     this.scripCount = scripSet.size();
/*     */     
/*     */ 
/*  33 */     this.dataType = dataType;
/*     */     
/*     */ 
/*  36 */     String dataID = "";
/*  37 */     for (Scrip scrip : scripSet)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  42 */       if (dataType.equals("FD"))
/*     */       {
/*  44 */         dataID = scrip.segmentName + " " + dataType;
/*  45 */         String dataLocation = dataPath + "/FD/" + dataID + ".csv";
/*     */         
/*     */ 
/*  48 */         CSVReader dataReader = null;
/*  49 */         dataReader = new CSVReader(dataLocation, ',', 0);
/*  50 */         String[] metaInfoStr = dataReader.getLine();
/*  51 */         Object[] metaInfo = processMetaInfo(metaInfoStr);
/*  52 */         dataReader.close();
/*     */         
/*     */ 
/*  55 */         dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), 1);
/*     */         
/*     */ 
/*  58 */         FundaData fD = new FundaData(scrip.segmentName);
/*     */         
/*     */ 
/*  61 */         ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, fD, metaInfo);
/*  62 */         this.scripDataHandlerMap.put(scrip.scripID, sDHandler);
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/*  70 */         dataID = scrip.getDataID(dataType);
/*     */         
/*     */ 
/*  73 */         if (dataType.equals("MD"))
/*     */         {
/*  75 */           String dataLocation = dataPath + "/" + dataType + "/" + dataID + ".csv";
/*     */           
/*     */ 
/*  78 */           boolean fileExists = true;
/*  79 */           CSVReader dataReader = null;
/*     */           try {
/*  81 */             dataReader = new CSVReader(dataLocation, ',', 0);
/*     */           }
/*     */           catch (IOException e)
/*     */           {
/*  85 */             fileExists = false;
/*     */           }
/*  87 */           String[] header = dataReader.getLine();
/*  88 */           String[] metaInfoStr = dataReader.getLine();
/*  89 */           Object[] metaInfo = processMetaInfo(metaInfoStr);
/*  90 */           dataReader.close();
/*     */           
/*     */ 
/*  93 */           dataReader = new CSVReader(dataLocation, ',', 2);
/*     */           
/*     */ 
/*  96 */           MetaData mD = new MetaData(scrip, header);
/*     */           
/*     */ 
/*  99 */           ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, mD, fileExists, metaInfo);
/* 100 */           this.scripDataHandlerMap.put(scrip.scripID, sDHandler);
/*     */ 
/*     */ 
/*     */         }
/* 104 */         else if (dataType.equals("PP"))
/*     */         {
/* 106 */           String dataLocation = dataPath + "/" + dataType + "/" + dataID + ".csv";
/*     */           
/*     */ 
/* 109 */           CSVReader dataReader = new CSVReader(dataLocation, ',', 0);
/* 110 */           String[] header = dataReader.getLine();
/* 111 */           String[] metaInfoStr = dataReader.getLine();
/* 112 */           Object[] metaInfo = processMetaInfo(metaInfoStr);
/* 113 */           dataReader.close();
/*     */           
/*     */ 
/* 116 */           dataReader = new CSVReader(dataLocation, ',', 2);
/*     */           
/*     */ 
/* 119 */           PreProcessData pD = new PreProcessData(scrip, header);
/*     */           
/*     */ 
/* 122 */           ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, pD, metaInfo);
/* 123 */           this.scripDataHandlerMap.put(scrip.scripID, sDHandler);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 129 */           String dataLocation = dataPath + "/" + scrip.contractType + "/" + dataID + ".csv";
/*     */           
/*     */ 
/* 132 */           CSVReader dataReader = new CSVReader(dataLocation, ',', 0);
/* 133 */           String[] header = dataReader.getLine();
/* 134 */           String[] metaInfoStr = dataReader.getLine();
/* 135 */           Object[] metaInfo = processMetaInfo(metaInfoStr);
/* 136 */           dataReader.close();
/*     */           
/*     */ 
/* 139 */           if (dataType.equals(strategyDataType)) {
/* 140 */             dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), 2);
/*     */           } else {
/* 142 */             dataReader = new CSVReader(dataLocation, ',', startDate.longValue(), true, 2);
/*     */           }
/*     */           
/* 145 */           ContractData cD = new ContractData(scrip, header);
/*     */           
/*     */ 
/* 148 */           ScripDataHandler sDHandler = new ScripDataHandler(dataType, dataReader, cD, metaInfo);
/* 149 */           this.scripDataHandlerMap.put(scrip.scripID, sDHandler);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 155 */     this.dateTime = Long.valueOf(20910101235900L);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean readData()
/*     */     throws Exception
/*     */   {
/* 162 */     long curDateTime = 29910101235900L;
/*     */     
/*     */ 
/* 165 */     this.eof = true;
/*     */     
/*     */ 
/* 168 */     for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 174 */       this.eof &= sDHandler.eof;
/* 175 */       if (!sDHandler.eof)
/*     */       {
/*     */         long dataDateTime;
/*     */         
/*     */         long dataDateTime;
/* 180 */         if (sDHandler.noUpdate) {
/* 181 */           dataDateTime = sDHandler.dateTime.longValue();
/*     */ 
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 188 */           String[] dataLine = sDHandler.dataReader.getLine();
/*     */           
/*     */ 
/* 191 */           if (dataLine == null) {
/* 192 */             sDHandler.eof = true;
/* 193 */             continue;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 199 */           dataDateTime = getDateTime(dataLine).longValue();
/* 200 */           sDHandler.dateTime = Long.valueOf(dataDateTime);
/* 201 */           sDHandler.dataLine = dataLine;
/*     */           
/*     */ 
/* 204 */           sDHandler.updateData();
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 209 */         if (dataDateTime < curDateTime) {
/* 210 */           curDateTime = dataDateTime;
/*     */         }
/*     */       }
/*     */     }
/* 214 */     if (this.eof) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     this.dateTime = Long.valueOf(curDateTime);
/*     */     
/* 220 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void checkUpdateAcrossScrips(long curDateTime)
/*     */     throws Exception
/*     */   {
/* 227 */     for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values())
/*     */     {
/*     */ 
/* 230 */       if (sDHandler.dateTime.longValue() > curDateTime) {
/* 231 */         sDHandler.noUpdate = true;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 236 */         sDHandler.noUpdate = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws Exception
/*     */   {
/* 243 */     for (ScripDataHandler sDHandler : this.scripDataHandlerMap.values()) {
/* 244 */       sDHandler.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Long getDateTime(String[] dataLine)
/*     */   {
/* 251 */     if (this.dataType.equals("MD")) {
/* 252 */       return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
/*     */     }
/*     */     
/*     */ 
/* 256 */     if (this.dataType.equals("PP")) {
/* 257 */       return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
/*     */     }
/*     */     
/*     */ 
/* 261 */     if (this.dataType.equals("FD")) {
/* 262 */       return Long.valueOf(Long.parseLong(dataLine[0] + "000000"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 269 */     if (this.dataType.contains("M")) {
/* 270 */       return Long.valueOf(Long.parseLong(dataLine[0] + dataLine[1]));
/*     */     }
/*     */     
/*     */ 
/* 274 */     if (this.dataType.contains("D")) {
/* 275 */       return Long.valueOf(Long.parseLong(dataLine[0] + "235900"));
/*     */     }
/*     */     
/*     */ 
/* 279 */     return null;
/*     */   }
/*     */   
/*     */   public Object[] processMetaInfo(String[] metaInfo)
/*     */   {
/* 284 */     Long startDate = Long.valueOf(0L);Long endDate = Long.valueOf(0L);
/* 285 */     Integer dataCount = Integer.valueOf(0);
/*     */     String[] arrayOfString1;
/* 287 */     int j = (arrayOfString1 = metaInfo).length; for (int i = 0; i < j; i++) { String meta = arrayOfString1[i];
/* 288 */       String[] metaVal = meta.split("\\|");
/* 289 */       String key = metaVal[0];
/* 290 */       String value = metaVal[1];
/*     */       
/*     */ 
/* 293 */       if (key.equals("Start Date")) {
/* 294 */         startDate = Long.valueOf(Long.parseLong(value));
/*     */       }
/* 296 */       else if (key.equals("End Date")) {
/* 297 */         endDate = Long.valueOf(Long.parseLong(value));
/*     */       }
/* 299 */       else if (key.equals("Data Count")) {
/* 300 */         dataCount = Integer.valueOf(Integer.parseInt(value));
/*     */       }
/*     */       else {
/* 303 */         System.out.println("Invalid Meta Information: " + key);
/*     */       }
/*     */     }
/* 306 */     Object[] metaObj = { startDate, endDate, dataCount };
/*     */     
/* 308 */     return metaObj;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataTypeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */