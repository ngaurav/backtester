/*     */ package com.q1.bt.data.classes;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContractData
/*     */ {
/*     */   public String exchangeName;
/*     */   public String assetclassName;
/*     */   public String segmentName;
/*     */   public String scripName;
/*     */   String[] header;
/*     */   HashMap<String, Integer> indexMap;
/*  20 */   public Contract contract = new Contract();
/*     */   
/*     */   public ContractData(Scrip scrip, String[] header)
/*     */   {
/*  24 */     this.exchangeName = scrip.exchangeName;
/*  25 */     this.assetclassName = scrip.assetClassName;
/*  26 */     this.segmentName = scrip.segmentName;
/*  27 */     this.scripName = scrip.scripName;
/*  28 */     this.header = header;
/*     */     
/*     */ 
/*  31 */     createIndexMap(header);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ContractData(Scrip scrip)
/*     */   {
/*  38 */     this.exchangeName = scrip.exchangeName;
/*  39 */     this.assetclassName = scrip.assetClassName;
/*  40 */     this.segmentName = scrip.segmentName;
/*  41 */     this.scripName = scrip.scripName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ContractData(ContractData cD)
/*     */   {
/*  48 */     this.exchangeName = cD.exchangeName;
/*  49 */     this.assetclassName = cD.assetclassName;
/*  50 */     this.segmentName = cD.segmentName;
/*  51 */     this.scripName = cD.scripName;
/*  52 */     this.header = cD.header;
/*     */     
/*  54 */     this.indexMap = new HashMap(cD.indexMap);
/*     */     
/*  56 */     this.contract = new Contract(cD.contract);
/*     */   }
/*     */   
/*     */ 
/*     */   public void createIndexMap(String[] header)
/*     */   {
/*  62 */     this.indexMap = new HashMap();
/*     */     
/*     */ 
/*  65 */     for (int i = 0; i < header.length; i++) {
/*  66 */       this.indexMap.put(header[i], Integer.valueOf(i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addContractData(String[] dataLine)
/*     */     throws Exception
/*     */   {
/*  74 */     for (Map.Entry<String, Integer> entry : this.indexMap.entrySet()) {
/*  75 */       String header = (String)entry.getKey();
/*  76 */       Integer index = (Integer)entry.getValue();
/*  77 */       String value = dataLine[index.intValue()];
/*  78 */       if ((!header.equals("Date")) && (!header.equals("Time"))) {
/*  79 */         updateContractVariable(header, value);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateContractVariable(String header, String value)
/*     */     throws Exception
/*     */   {
/*  87 */     if (header.equals("Expiry")) {
/*  88 */       this.contract.exp = Integer.valueOf(Integer.parseInt(value));
/*     */ 
/*     */     }
/*  91 */     else if (header.equals("Open")) {
/*  92 */       this.contract.op = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/*  95 */     else if (header.equals("High")) {
/*  96 */       this.contract.hi = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/*  99 */     else if (header.equals("Low")) {
/* 100 */       this.contract.lo = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 103 */     else if (header.equals("Close")) {
/* 104 */       this.contract.cl = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 107 */     else if (header.equals("Rollover Close")) {
/* 108 */       this.contract.rolloverCl = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 111 */     else if (header.equals("Volume")) {
/* 112 */       this.contract.vol = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 115 */     else if (header.equals("OI")) {
/* 116 */       this.contract.oi = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 119 */     else if (header.equals("Total Volume")) {
/* 120 */       this.contract.totalVol = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 123 */     else if (header.equals("Total OI")) {
/* 124 */       this.contract.totalOI = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 127 */     else if (header.equals("Rollover Expiry")) {
/* 128 */       this.contract.rolloverExp = Integer.valueOf(Integer.parseInt(value));
/*     */ 
/*     */     }
/* 131 */     else if (header.equals("Original Expiry")) {
/* 132 */       this.contract.actualExp = Integer.valueOf(Integer.parseInt(value));
/*     */ 
/*     */     }
/* 135 */     else if (header.equals("Currency")) {
/* 136 */       this.contract.currency = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 139 */     else if (header.equals("Bid")) {
/* 140 */       this.contract.bid = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 143 */     else if (header.equals("Ask")) {
/* 144 */       this.contract.ask = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 147 */     else if (header.equals("BidQty")) {
/* 148 */       this.contract.bidQty = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 151 */     else if (header.equals("AskQty")) {
/* 152 */       this.contract.askQty = Double.valueOf(Double.parseDouble(value));
/*     */ 
/*     */     }
/* 155 */     else if (header.equals("OptionType")) {
/* 156 */       this.contract.optionType = value;
/*     */ 
/*     */     }
/* 159 */     else if (header.equals("OptionStrike")) {
/* 160 */       this.contract.optionStrike = Double.valueOf(Double.parseDouble(value));
/*     */     }
/*     */     else {
/* 163 */       throw new Exception(this.scripName + " - Unknown column in file: " + header);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/ContractData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */