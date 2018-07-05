/*     */ package com.q1.bt.data;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class DataDriver
/*     */ {
/*  15 */   public HashMap<String, DataTypeHandler> dataTypeHandlerMap = new HashMap();
/*     */   
/*     */ 
/*  18 */   public HashMap<String, DataTypeViewer> dataTypeViewerMap = new HashMap();
/*     */   
/*     */   String scripListID;
/*     */   
/*     */   ArrayList<Scrip> scripSet;
/*     */   String strategyDataType;
/*  24 */   public Long strategyDate = Long.valueOf(-100L);
/*     */   
/*     */ 
/*     */   BacktesterGlobal btGlobal;
/*     */   
/*     */   Backtest btObj;
/*     */   
/*  31 */   public Long curDateTime = Long.valueOf(29910101235900L); public Long prevDateTime = Long.valueOf(29910101235900L);
/*  32 */   public Long curDate = Long.valueOf(29910101L);
/*     */   
/*     */ 
/*     */ 
/*     */   public DataDriver(BacktesterGlobal btGlobal, Long startDate, Backtest btObj, String scripListID, ArrayList<Scrip> scripSet, String strategyDataType)
/*     */     throws Exception
/*     */   {
/*  39 */     this.btGlobal = btGlobal;
/*  40 */     this.btObj = btObj;
/*  41 */     this.scripListID = scripListID;
/*  42 */     this.scripSet = new ArrayList(scripSet);
/*  43 */     this.strategyDataType = strategyDataType;
/*     */     
/*     */ 
/*  46 */     createDataTypeObjects(startDate);
/*     */   }
/*     */   
/*     */ 
/*     */   public void createDataTypeObjects(Long startDate)
/*     */     throws Exception
/*     */   {
/*  53 */     if (this.strategyDataType.contains("M"))
/*     */     {
/*     */ 
/*  56 */       createHandlerAndViewer(this.strategyDataType, startDate);
/*     */       
/*     */ 
/*  59 */       createHandlerAndViewer("1D", startDate);
/*     */       
/*     */ 
/*  62 */       createHandlerAndViewer("MD", startDate);
/*     */       
/*     */ 
/*  65 */       createHandlerAndViewer("PP", startDate);
/*     */       
/*     */ 
/*  68 */       createHandlerAndViewer("FD", startDate);
/*     */     }
/*  70 */     else if (this.strategyDataType.contains("D"))
/*     */     {
/*     */ 
/*  73 */       createHandlerAndViewer(this.strategyDataType, startDate);
/*     */       
/*     */ 
/*  76 */       createHandlerAndViewer("MD", startDate);
/*     */       
/*     */ 
/*  79 */       createHandlerAndViewer("PP", startDate);
/*     */       
/*     */ 
/*  82 */       createHandlerAndViewer("FD", startDate);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createHandlerAndViewer(String dataType, Long startDate)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  93 */       DataTypeHandler dataHandler = new DataTypeHandler(this.btGlobal.loginParameter.getDataPath(), dataType, this.scripSet, 
/*  94 */         startDate, this.strategyDataType);
/*  95 */       this.dataTypeHandlerMap.put(dataType, dataHandler);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  99 */       if ((dataType.equals("1M")) || (dataType.equals("1D"))) {
/* 100 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 105 */       DataTypeViewer dataViewer = new DataTypeViewer(this.btGlobal, dataType, this.scripSet, this.strategyDataType);
/* 106 */       this.dataTypeViewerMap.put(dataType, dataViewer);
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 111 */       this.btGlobal.displayMessage("Error Creating Data Type Viewer " + this.scripListID + " " + dataType);
/* 112 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean updateData()
/*     */     throws Exception
/*     */   {
/* 120 */     this.curDateTime = Long.valueOf(29910101235900L);
/*     */     
/*     */     DataTypeHandler dtHandler;
/* 123 */     for (Map.Entry<String, DataTypeHandler> entry : this.dataTypeHandlerMap.entrySet())
/*     */     {
/*     */ 
/* 126 */       String dataType = (String)entry.getKey();
/* 127 */       dtHandler = (DataTypeHandler)entry.getValue();
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 132 */         if ((!dataType.equals("MD")) && 
/* 133 */           (dtHandler.eof)) {
/* 134 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 138 */         if (!dtHandler.readData()) {
/* 139 */           ((DataTypeViewer)this.dataTypeViewerMap.get(dataType)).updateEOF(true);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 144 */         else if (dtHandler.dateTime.longValue() < this.curDateTime.longValue()) {
/* 145 */           this.curDateTime = dtHandler.dateTime;
/*     */         }
/*     */       } catch (IOException e) {
/* 148 */         this.btGlobal.displayMessage("Error reading datapoint for: " + this.scripListID + " " + dataType);
/* 149 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 155 */     for (DataTypeHandler dtHandler : this.dataTypeHandlerMap.values()) {
/* 156 */       dtHandler.checkUpdateAcrossScrips(this.curDateTime.longValue());
/*     */     }
/*     */     
/*     */ 
/* 160 */     if (this.strategyDataType.contains("M"))
/*     */     {
/*     */ 
/* 163 */       DataTypeHandler mHandler = (DataTypeHandler)this.dataTypeHandlerMap.get(this.strategyDataType);
/*     */       
/*     */ 
/* 166 */       DataTypeHandler dHandler = (DataTypeHandler)this.dataTypeHandlerMap.get("1D");
/*     */       
/*     */ 
/* 169 */       for (Map.Entry<String, ScripDataHandler> entry : mHandler.scripDataHandlerMap.entrySet())
/*     */       {
/* 171 */         String scripID = (String)entry.getKey();
/* 172 */         ScripDataHandler sdHandlerM = (ScripDataHandler)entry.getValue();
/*     */         
/* 174 */         String[] forwardDateTime = { "", "" };
/* 175 */         String forwardDate = "";
/*     */         try {
/* 177 */           forwardDateTime = sdHandlerM.dataReader.getLastReadDateTime();
/* 178 */           forwardDate = forwardDateTime[0];
/*     */         }
/*     */         catch (NullPointerException localNullPointerException) {}
/*     */         
/* 182 */         String minuteDateStr = sdHandlerM.date.toString();
/*     */         
/*     */ 
/* 185 */         if ((!forwardDate.equals(minuteDateStr)) && (sdHandlerM.dateTime.equals(this.curDateTime)))
/*     */         {
/*     */ 
/* 188 */           ScripDataHandler sdhandlerD = (ScripDataHandler)dHandler.scripDataHandlerMap.get(scripID);
/*     */           
/*     */ 
/* 191 */           if (minuteDateStr.equals(sdhandlerD.date.toString()))
/*     */           {
/*     */ 
/* 194 */             long dateTime = sdhandlerD.updateRolloverData(sdHandlerM).longValue();
/*     */             
/*     */ 
/* 197 */             if (dateTime < dHandler.dateTime.longValue()) {
/* 198 */               dHandler.dateTime = Long.valueOf(dateTime);
/*     */             }
/*     */           } else {
/* 201 */             throw new Exception("Daily Data Date Missing: " + this.scripListID + " " + scripID);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 210 */     this.curDate = Long.valueOf(this.curDateTime.longValue() / 1000000L);
/*     */     
/*     */ 
/* 213 */     if (this.curDate.longValue() > this.btObj.backtestParameter.getEndDate()) {
/* 214 */       return false;
/*     */     }
/*     */     
/* 217 */     if (this.curDateTime.equals(Long.valueOf(29910101235900L))) {
/* 218 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 222 */     this.prevDateTime = this.curDateTime;
/*     */     
/*     */ 
/* 225 */     this.strategyDate = Long.valueOf(((DataTypeHandler)this.dataTypeHandlerMap.get(this.strategyDataType)).dateTime.longValue() / 1000000L);
/*     */     
/*     */ 
/* 228 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateDataViewers()
/*     */   {
/* 235 */     for (DataTypeHandler dtHandler : this.dataTypeHandlerMap.values())
/*     */     {
/*     */ 
/* 238 */       if (dtHandler.dateTime.equals(this.curDateTime)) {
/* 239 */         ((DataTypeViewer)this.dataTypeViewerMap.get(dtHandler.dataType)).updateData(dtHandler);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 244 */         ((DataTypeViewer)this.dataTypeViewerMap.get(dtHandler.dataType)).updateData(this.curDateTime);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDataIDs(String[] scripIdList, String dataType)
/*     */   {
/* 252 */     String dataIDs = "";
/* 253 */     String[] arrayOfString; int j = (arrayOfString = scripIdList).length; for (int i = 0; i < j; i++) { String scripID = arrayOfString[i];
/* 254 */       if (dataIDs == "") {
/* 255 */         dataIDs = scripID + " " + dataType;
/*     */       } else
/* 257 */         dataIDs = dataIDs + "," + scripID + " " + dataType;
/*     */     }
/* 259 */     return dataIDs;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */