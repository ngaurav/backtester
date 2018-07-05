/*     */ package com.q1.bt.data.classes;
/*     */ 
/*     */ import com.q1.sql.SQLdata;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetaData
/*     */ {
/*     */   public String exchangeName;
/*     */   public String assetclassName;
/*     */   public String segmentName;
/*     */   public String scripName;
/*     */   public Double slippage;
/*     */   public Double openSlippage;
/*     */   public Double slippageSlope;
/*     */   public Double slippageIntercept;
/*     */   public Double lotSize;
/*     */   public Double lotFactor;
/*     */   public Double tickSize;
/*     */   public Integer leverage;
/*     */   public Double exchangeFees;
/*     */   public Double brokerage;
/*     */   String[] header;
/*  30 */   public HashMap<String, String> dataMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public MetaData(Scrip scrip, String[] header)
/*     */   {
/*  36 */     this.exchangeName = scrip.exchangeName;
/*  37 */     this.assetclassName = scrip.assetClassName;
/*  38 */     this.segmentName = scrip.segmentName;
/*  39 */     this.scripName = scrip.scripName;
/*     */     
/*     */ 
/*  42 */     this.header = header;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MetaData(Scrip scrip)
/*     */   {
/*  50 */     this.exchangeName = scrip.exchangeName;
/*  51 */     this.assetclassName = scrip.assetClassName;
/*  52 */     this.segmentName = scrip.segmentName;
/*  53 */     this.scripName = scrip.scripName;
/*     */   }
/*     */   
/*     */ 
/*     */   public MetaData(MetaData mD)
/*     */   {
/*  59 */     this.exchangeName = mD.exchangeName;
/*  60 */     this.assetclassName = mD.assetclassName;
/*  61 */     this.segmentName = mD.segmentName;
/*  62 */     this.scripName = mD.scripName;
/*  63 */     this.header = mD.header;
/*  64 */     this.slippage = mD.slippage;
/*  65 */     this.openSlippage = mD.openSlippage;
/*  66 */     this.slippageSlope = mD.slippageSlope;
/*  67 */     this.slippageIntercept = mD.slippageIntercept;
/*  68 */     this.lotSize = mD.lotSize;
/*  69 */     this.lotFactor = mD.lotFactor;
/*  70 */     this.tickSize = mD.tickSize;
/*  71 */     this.leverage = mD.leverage;
/*  72 */     this.exchangeFees = mD.exchangeFees;
/*  73 */     this.brokerage = mD.brokerage;
/*  74 */     this.dataMap = new HashMap(mD.dataMap);
/*     */   }
/*     */   
/*     */   public void copyMetaData(MetaData mD)
/*     */   {
/*  79 */     this.exchangeName = mD.exchangeName;
/*  80 */     this.assetclassName = mD.assetclassName;
/*  81 */     this.segmentName = mD.segmentName;
/*  82 */     this.scripName = mD.scripName;
/*  83 */     this.header = mD.header;
/*  84 */     if (mD.slippage != null)
/*  85 */       this.slippage = mD.slippage;
/*  86 */     if (mD.openSlippage != null)
/*  87 */       this.openSlippage = mD.openSlippage;
/*  88 */     if (mD.slippageSlope != null)
/*  89 */       this.slippageSlope = mD.slippageSlope;
/*  90 */     if (mD.slippageIntercept != null)
/*  91 */       this.slippageIntercept = mD.slippageIntercept;
/*  92 */     if (mD.lotSize != null)
/*  93 */       this.lotSize = mD.lotSize;
/*  94 */     if (mD.lotFactor != null)
/*  95 */       this.lotFactor = mD.lotFactor;
/*  96 */     if (mD.tickSize != null)
/*  97 */       this.tickSize = mD.tickSize;
/*  98 */     if (mD.leverage != null)
/*  99 */       this.leverage = mD.leverage;
/* 100 */     if (mD.exchangeFees != null)
/* 101 */       this.exchangeFees = mD.exchangeFees;
/* 102 */     if (mD.brokerage != null)
/* 103 */       this.brokerage = mD.brokerage;
/* 104 */     this.dataMap = new HashMap(mD.dataMap);
/*     */   }
/*     */   
/*     */   public void readMetaDataFromDatabase(SQLdata sqlObject, String dataType) throws SQLException
/*     */   {
/* 109 */     this.tickSize = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 110 */       "Segment", this.segmentName, "Tick Size"));
/*     */     
/* 112 */     this.slippage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 113 */       "Segment", this.segmentName, dataType + " Slippage"));
/*     */     
/* 115 */     this.openSlippage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 116 */       "Segment", this.segmentName, dataType + " Open Slippage"));
/*     */     
/* 118 */     this.slippageSlope = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 119 */       "Segment", this.segmentName, "Slippage Slope"));
/*     */     
/* 121 */     this.slippageIntercept = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", 
/* 122 */       this.scripName, "Segment", this.segmentName, "Slippage Intercept"));
/*     */     
/* 124 */     this.lotSize = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 125 */       "Segment", this.segmentName, "Lot Size"));
/*     */     
/* 127 */     this.lotFactor = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 128 */       "Segment", this.segmentName, "Lot Factor"));
/*     */     
/* 130 */     this.leverage = Integer.valueOf(sqlObject.getIntValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 131 */       "Segment", this.segmentName, "Leverage"));
/*     */     
/* 133 */     this.exchangeFees = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 134 */       "Segment", this.segmentName, "Transaction Charges"));
/*     */     
/* 136 */     this.brokerage = Double.valueOf(sqlObject.getDoubleValwithThreeKeys("scripinfo", "Exchange", this.exchangeName, "Scrip", this.scripName, 
/* 137 */       "Segment", this.segmentName, "Brokerage"));
/* 138 */     sqlObject.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addData(String[] dataLine)
/*     */   {
/* 145 */     this.dataMap = new HashMap();
/*     */     
/*     */ 
/* 148 */     for (int i = 0; i < dataLine.length; i++) {
/* 149 */       boolean checkExistingVariable = updateContractVariable(this.header[i], dataLine[i]);
/* 150 */       if (!checkExistingVariable) {
/* 151 */         this.dataMap.put(this.header[i], dataLine[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean updateContractVariable(String header, String value)
/*     */   {
/* 159 */     if (header.equals("Slippage")) {
/* 160 */       this.slippage = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 163 */     else if (header.equals("Open Slippage")) {
/* 164 */       this.openSlippage = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 167 */     else if (header.equals("Slippage Slope")) {
/* 168 */       this.slippageSlope = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 171 */     else if (header.equals("Slippage Intercept")) {
/* 172 */       this.slippageIntercept = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 175 */     else if (header.equals("Lot Size")) {
/* 176 */       this.lotSize = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 179 */     else if (header.equals("Lot Factor")) {
/* 180 */       this.lotFactor = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 183 */     else if (header.equals("Tick Size")) {
/* 184 */       this.tickSize = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 187 */     else if (header.equals("Leverage")) {
/* 188 */       this.leverage = Integer.valueOf(Integer.parseInt(value));
/*     */ 
/*     */     }
/* 191 */     else if (header.equals("Exchange Fee")) {
/* 192 */       this.exchangeFees = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 195 */     else if (header.equals("Brokerage")) {
/* 196 */       this.brokerage = Double.valueOf(Double.parseDouble(value));
/*     */     }
/*     */     else {
/* 199 */       return false;
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/MetaData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */