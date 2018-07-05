/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class IntradayReader
/*     */ {
/*     */   CSVReader reader1D;
/*     */   CSVReader reader1M;
/*     */   CSVReader readerMD;
/*     */   Long currentDate;
/*     */   Long mdDate;
/*     */   Long currentTime;
/*     */   String[] curDailyData;
/*     */   String[] curMinData;
/*     */   String[] curMetaData;
/*     */   String[] prevMinData;
/*     */   
/*     */   public IntradayReader(String dSource, String mSource, String mdSource, Long startDate) throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  24 */       this.reader1D = new CSVReader(dSource, ',', 0);
/*  25 */       this.reader1M = new CSVReader(mSource, ',', 0);
/*  26 */       this.readerMD = new CSVReader(mdSource, ',', 0);
/*     */     } catch (IOException e) {
/*  28 */       System.out.println("Error in opening 1D/1M/MD files");
/*     */     }
/*  30 */     this.currentDate = Long.valueOf(0L);
/*  31 */     this.mdDate = Long.valueOf(0L);
/*  32 */     this.currentTime = Long.valueOf(0L);
/*  33 */     this.curDailyData = null;
/*  34 */     this.curMinData = null;
/*  35 */     this.curMetaData = null;
/*  36 */     this.prevMinData = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void process(Long dataDate, Long datatime, IntradayData id, DailyData dd, MetaData md)
/*     */     throws IOException
/*     */   {
/*  43 */     if ((this.curDailyData == null) || (this.curMetaData == null)) {
/*  44 */       this.curDailyData = this.reader1D.getLine();
/*  45 */       this.prevMinData = this.reader1M.getLine();
/*  46 */       this.curMetaData = this.readerMD.getLine();
/*  47 */       if (this.curDailyData == null) {
/*  48 */         System.out.println("No data in Daily File");
/*  49 */         return;
/*     */       }
/*  51 */       if (this.prevMinData == null) {
/*  52 */         System.out.println("No Minute data in Minute File");
/*  53 */         return;
/*     */       }
/*  55 */       if (this.curMetaData == null) {
/*  56 */         System.out.println("No Meta Data in MetaData File");
/*  57 */         return;
/*     */       }
/*     */     }
/*  60 */     if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) > 0) return;
/*  61 */     if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) == 0) {
/*  62 */       assignData(dataDate, datatime, id, dd, md);
/*  63 */       this.prevMinData = this.curMinData;
/*  64 */       return;
/*     */     }
/*  66 */     while (readNextPoint()) {
/*  67 */       this.currentDate = Long.valueOf(Long.parseLong(this.curDailyData[0]));
/*  68 */       this.mdDate = Long.valueOf(Long.parseLong(this.curMetaData[0]));
/*  69 */       this.currentTime = Long.valueOf(Long.parseLong(this.curMinData[1]));
/*  70 */       if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) < 0) {
/*  71 */         this.prevMinData = this.curMinData;
/*     */       }
/*     */       else {
/*  74 */         if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) == 0) {
/*  75 */           assignData(dataDate, datatime, id, dd, md);
/*  76 */           this.prevMinData = this.curMinData;
/*  77 */           return;
/*     */         }
/*  79 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean readNextPoint() throws IOException {
/*  85 */     this.curMinData = this.reader1M.getLine();
/*  86 */     if (this.curMinData == null) return false;
/*  87 */     while (Long.parseLong(this.curMinData[0]) > Long.parseLong(this.curDailyData[0])) {
/*  88 */       this.curDailyData = this.reader1D.getLine();
/*  89 */       if (this.curDailyData == null) { return false;
/*     */       }
/*     */     }
/*  92 */     while (Long.parseLong(this.curMinData[0]) > Long.parseLong(this.curMetaData[0])) {
/*  93 */       this.curMetaData = this.readerMD.getLine();
/*  94 */       if (this.curMetaData == null) return false;
/*     */     }
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   private int dateTimeCompare(Long date1, Long time1, Long date2, Long time2)
/*     */   {
/* 101 */     if (date1.longValue() > date2.longValue()) return 1;
/* 102 */     if (date1.longValue() < date2.longValue()) return -1;
/* 103 */     if (time1.longValue() == time2.longValue()) return 0;
/* 104 */     if (time1.longValue() > time2.longValue()) return 2;
/* 105 */     return -2;
/*     */   }
/*     */   
/*     */   private void assignData(Long dataDate, Long dataTime, IntradayData id, DailyData dd, MetaData md) {
/*     */     try {
/* 110 */       id.updateData(Double.valueOf(Double.parseDouble(this.prevMinData[3])), 
/* 111 */         Double.valueOf(Double.parseDouble(this.prevMinData[4])), 
/* 112 */         Double.valueOf(Double.parseDouble(this.prevMinData[5])), 
/* 113 */         Double.valueOf(Double.parseDouble(this.prevMinData[6])), 
/* 114 */         Double.valueOf(Double.parseDouble(this.prevMinData[7])), 
/* 115 */         dataTime);
/*     */       
/*     */ 
/* 118 */       Boolean isRoll = Boolean.valueOf(false);
/* 119 */       Double rollOver = Double.valueOf(0.0D);
/*     */       
/* 121 */       id.updateData(dd, md, isRoll.booleanValue(), rollOver);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 125 */       Double vol = Double.valueOf(0.0D);
/*     */       try {
/* 127 */         vol = Double.valueOf(Double.parseDouble(this.prevMinData[7]));
/*     */       }
/*     */       catch (Exception localException1) {}
/* 130 */       id.updateData(Double.valueOf(Double.parseDouble(this.prevMinData[3])), 
/* 131 */         Double.valueOf(Double.parseDouble(this.prevMinData[4])), 
/* 132 */         Double.valueOf(Double.parseDouble(this.prevMinData[5])), 
/* 133 */         Double.valueOf(Double.parseDouble(this.prevMinData[6])), 
/* 134 */         vol, 
/* 135 */         dataTime);
/*     */       
/*     */ 
/* 138 */       Boolean isRoll = Boolean.valueOf(false);
/* 139 */       Double rollOver = Double.valueOf(0.0D);
/* 140 */       id.updateData(dd, md, isRoll.booleanValue(), rollOver);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 149 */     this.reader1D.close();
/* 150 */     this.reader1M.close();
/* 151 */     this.readerMD.close();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/IntradayReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */