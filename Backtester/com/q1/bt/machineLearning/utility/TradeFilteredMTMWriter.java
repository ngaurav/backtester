/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class TradeFilteredMTMWriter
/*     */ {
/*     */   com.q1.csv.CSVWriter writerMTM;
/*     */   CSVReader readerTD;
/*     */   CSVReader readerDD;
/*     */   CSVReader readerMTM;
/*     */   Long currentDate;
/*     */   Long unWrittenDate;
/*     */   Double consumedVal;
/*     */   Double residualVal;
/*     */   String[] inTradeData;
/*     */   boolean inTrade;
/*     */   private Long entryDate;
/*     */   private Long entryTS;
/*     */   private Double capital;
/*     */   private Long exitDate;
/*     */   private double entryPrice;
/*     */   private String entrySide;
/*     */   private Double positionSize;
/*     */   private double exitPrice;
/*     */   private String[] inDailyData;
/*     */   private TreeMap<Long, Double> dailyMTMMap;
/*     */   private Double prevCl;
/*     */   private String[] inMTMData;
/*     */   private String sourceMTM;
/*     */   private Long ddDate;
/*     */   private boolean dateFlag;
/*     */   private java.util.ArrayList<Long> dateList;
/*     */   
/*     */   public TradeFilteredMTMWriter(String dataPath, String sourceMTM, String sourceTD, String destination, java.util.ArrayList<Long> dateList) throws IOException
/*     */   {
/*  38 */     this.sourceMTM = sourceMTM;
/*     */     try
/*     */     {
/*  41 */       this.writerMTM = new com.q1.csv.CSVWriter(destination, false, ",");
/*  42 */       this.readerTD = new CSVReader(sourceTD, ',', 0);
/*  43 */       this.readerDD = new CSVReader(dataPath, ',', 2);
/*     */     } catch (IOException e) {
/*  45 */       System.out.println("file not found");
/*  46 */       throw new IOException();
/*     */     }
/*  48 */     this.inTrade = false;
/*  49 */     this.dailyMTMMap = new TreeMap();
/*  50 */     this.ddDate = Long.valueOf(0L);
/*  51 */     this.exitDate = Long.valueOf(0L);
/*  52 */     this.entryTS = Long.valueOf(0L);
/*  53 */     this.dateList = dateList;
/*     */   }
/*     */   
/*     */   public double process(Long resultTS, int write) throws IOException {
/*  57 */     Double val = Double.valueOf(0.0D);
/*     */     
/*  59 */     Double tsMTM = Double.valueOf(0.0D);
/*  60 */     while (this.entryTS.compareTo(resultTS) <= 0) {
/*     */       try {
/*     */         do {
/*  63 */           if (!this.inTrade) {
/*  64 */             this.capital = Double.valueOf(Double.parseDouble(this.inTradeData[1]));
/*  65 */             this.positionSize = Double.valueOf(Double.parseDouble(this.inTradeData[7]));
/*  66 */             Long dateTime = Long.valueOf(Long.parseLong(this.inTradeData[0]));
/*  67 */             this.entryDate = Long.valueOf(dateTime.longValue() / 1000000L);
/*  68 */             this.entryTS = Long.valueOf(dateTime.longValue() / 1000000L);
/*     */             
/*  70 */             this.entryPrice = Double.parseDouble(this.inTradeData[6]);
/*  71 */             this.entrySide = this.inTradeData[3];
/*  72 */             this.inTrade = true;
/*  73 */             if (this.entryDate.equals(this.exitDate)) {
/*  74 */               this.dateFlag = true;
/*     */             }
/*  76 */             if (this.entryTS.compareTo(resultTS) > 0) {
/*     */               break;
/*     */             }
/*     */           } else {
/*  80 */             this.exitDate = Long.valueOf(Long.parseLong(this.inTradeData[0]) / 1000000L);
/*  81 */             this.exitPrice = Double.parseDouble(this.inTradeData[6]);
/*  82 */             this.inTrade = false;
/*     */             
/*     */ 
/*  85 */             if (this.entryTS.compareTo(resultTS) >= 0) {
/*     */               break;
/*     */             }
/*     */           }
/*  62 */         } while ((this.inTradeData = this.readerTD.getLine()) != null);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */         if ((this.inTradeData != null) && (this.entryTS.equals(resultTS)) && (!this.inTrade)) {
/*     */           do { Double close;
/*  94 */             Double mtm; do { if (this.inDailyData == null) {
/*  95 */                 this.inDailyData = this.readerDD.getLine();
/*     */               }
/*     */               
/*  98 */               if ((this.ddDate.compareTo(this.exitDate) < 0) && (!this.dateFlag)) {
/*  99 */                 this.inDailyData = this.readerDD.getLine();
/*     */               }
/* 101 */               this.dateFlag = false;
/*     */               
/*     */ 
/* 104 */               Double rollCl = Double.valueOf(Double.parseDouble(this.inDailyData[8]));
/*     */               Double close;
/* 106 */               if (rollCl.doubleValue() > 0.0D) {
/* 107 */                 close = rollCl;
/*     */               } else {
/* 109 */                 close = Double.valueOf(Double.parseDouble(this.inDailyData[5]));
/*     */               }
/* 111 */               this.ddDate = Long.valueOf(Long.parseLong(this.inDailyData[0]));
/* 112 */               mtm = Double.valueOf(0.0D);
/* 113 */             } while (this.ddDate.longValue() < this.entryDate.longValue());
/*     */             
/* 115 */             if (write == 1) {
/* 116 */               if (this.ddDate.equals(this.entryDate))
/*     */               {
/* 118 */                 mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (this.entryPrice - close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (close.doubleValue() - this.entryPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
/*     */               }
/*     */               
/* 121 */               if ((this.ddDate.longValue() > this.entryDate.longValue()) && (this.ddDate.longValue() <= this.exitDate.longValue()))
/*     */               {
/* 123 */                 mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-close.doubleValue() + this.prevCl.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-this.prevCl.doubleValue() + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue()));
/*     */               }
/* 125 */               if (this.ddDate.equals(this.exitDate))
/*     */               {
/* 127 */                 mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-this.exitPrice + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-close.doubleValue() + this.exitPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
/*     */               }
/*     */             }
/*     */             
/* 131 */             tsMTM = Double.valueOf(tsMTM.doubleValue() + mtm.doubleValue());
/*     */             
/* 133 */             if (this.dailyMTMMap.get(this.ddDate) == null) {
/* 134 */               this.dailyMTMMap.put(this.ddDate, mtm);
/*     */             } else {
/* 136 */               this.dailyMTMMap.put(this.ddDate, Double.valueOf(((Double)this.dailyMTMMap.get(this.ddDate)).doubleValue() + mtm.doubleValue()));
/*     */             }
/* 138 */             this.prevCl = close;
/*     */           }
/* 140 */           while (!this.ddDate.equals(this.exitDate));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 145 */         Long endDate = (Long)this.dateList.get(this.dateList.size() - 1);
/*     */         
/* 147 */         if (this.inTradeData == null) {
/* 148 */           if ((!this.entryTS.equals(resultTS)) || (!this.inTrade)) break;
/* 149 */           while (this.ddDate.compareTo(endDate) <= 0) {
/* 150 */             if (this.inDailyData == null) {
/* 151 */               this.inDailyData = this.readerDD.getLine();
/*     */             }
/*     */             
/* 154 */             if ((this.ddDate.compareTo(endDate) <= 0) && (!this.dateFlag)) {
/* 155 */               this.inDailyData = this.readerDD.getLine();
/*     */             }
/*     */             
/* 158 */             if (this.inDailyData == null) {
/*     */               break;
/*     */             }
/* 161 */             this.dateFlag = false;
/*     */             
/*     */ 
/*     */ 
/* 165 */             Double rollCl = Double.valueOf(Double.parseDouble(this.inDailyData[8]));
/*     */             Double close;
/* 167 */             Double close; if (rollCl.doubleValue() > 0.0D) {
/* 168 */               close = rollCl;
/*     */             } else {
/* 170 */               close = Double.valueOf(Double.parseDouble(this.inDailyData[5]));
/*     */             }
/* 172 */             this.ddDate = Long.valueOf(Long.parseLong(this.inDailyData[0]));
/* 173 */             Double mtm = Double.valueOf(0.0D);
/* 174 */             if (this.ddDate.longValue() >= this.entryDate.longValue())
/*     */             {
/* 176 */               if (write == 1) {
/* 177 */                 if (this.ddDate.equals(this.entryDate))
/*     */                 {
/* 179 */                   mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (this.entryPrice - close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (close.doubleValue() - this.entryPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
/*     */                 }
/*     */                 
/* 182 */                 if ((this.ddDate.longValue() > this.entryDate.longValue()) && (this.ddDate.longValue() <= endDate.longValue()))
/*     */                 {
/* 184 */                   mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-close.doubleValue() + this.prevCl.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-this.prevCl.doubleValue() + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue()));
/*     */                 }
/*     */               }
/*     */               
/* 188 */               tsMTM = Double.valueOf(tsMTM.doubleValue() + mtm.doubleValue());
/*     */               
/* 190 */               if (this.dailyMTMMap.get(this.ddDate) == null) {
/* 191 */                 this.dailyMTMMap.put(this.ddDate, mtm);
/*     */               } else {
/* 193 */                 this.dailyMTMMap.put(this.ddDate, Double.valueOf(((Double)this.dailyMTMMap.get(this.ddDate)).doubleValue() + mtm.doubleValue()));
/*     */               }
/* 195 */               this.prevCl = close;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 208 */     return val.doubleValue();
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Double> writeInFile(Long firstOutputDate) throws IOException {
/* 212 */     this.readerMTM = new CSVReader(this.sourceMTM, ',', 0);
/* 213 */     while ((this.inMTMData = this.readerMTM.getLine()) != null) {
/* 214 */       Long date = Long.valueOf(Long.parseLong(this.inMTMData[0]));
/*     */       
/* 216 */       if (date.compareTo(firstOutputDate) >= 0)
/*     */       {
/*     */ 
/* 219 */         String[] toWriteLine = new String[2];
/* 220 */         toWriteLine[0] = this.inMTMData[0];
/* 221 */         if (this.dailyMTMMap.get(date) != null) {
/* 222 */           toWriteLine[1] = ((Double)this.dailyMTMMap.get(date)).toString();
/*     */         } else {
/* 224 */           toWriteLine[1] = "0.0";
/* 225 */           this.dailyMTMMap.put(date, Double.valueOf(0.0D));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 230 */         this.writerMTM.writeLine(toWriteLine);
/*     */       } }
/* 232 */     return this.dailyMTMMap;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 236 */     this.writerMTM.close();
/* 237 */     this.readerTD.close();
/* 238 */     this.readerDD.close();
/* 239 */     if (this.readerMTM != null) {
/* 240 */       this.readerMTM.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeFilteredMTMWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */