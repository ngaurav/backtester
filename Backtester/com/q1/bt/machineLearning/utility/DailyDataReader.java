/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class DailyDataReader
/*     */ {
/*     */   com.q1.csv.CSVReader reader1D;
/*     */   Long currentDate;
/*     */   String[] curData;
/*     */   String[] prevData;
/*     */   private Long mtmDate;
/*  13 */   private boolean startFileFlag = true;
/*     */   
/*     */   public DailyDataReader(String source, Long startDate) throws IOException {
/*     */     try {
/*  17 */       this.reader1D = new com.q1.csv.CSVReader(source, ',', 2);
/*     */     } catch (IOException e) {
/*  19 */       System.out.println(source + " not found");
/*  20 */       e.printStackTrace();
/*  21 */       throw new IOException();
/*     */     }
/*  23 */     this.currentDate = Long.valueOf(0L);
/*  24 */     this.curData = null;
/*  25 */     this.prevData = null;
/*  26 */     this.mtmDate = Long.valueOf(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void process(Long dataDate, DailyData dd, Double mtm)
/*     */     throws IOException
/*     */   {
/*  36 */     if ((this.curData == null) && (this.startFileFlag)) {
/*  37 */       this.startFileFlag = false;
/*  38 */       this.prevData = this.reader1D.getLine();
/*  39 */       if (this.prevData == null) {
/*  40 */         System.out.println("No data in Daily File");
/*  41 */         return;
/*     */       }
/*     */     }
/*  44 */     if (this.currentDate.longValue() > dataDate.longValue())
/*  45 */       return;
/*  46 */     if (this.currentDate.longValue() == dataDate.longValue()) {
/*  47 */       assignData(dataDate, dd, mtm);
/*     */       
/*     */ 
/*  50 */       return; }
/*  51 */     if (dataDate.longValue() == 99999999L) {
/*  52 */       assignData(dataDate, dd, mtm);
/*  53 */       return;
/*     */     }
/*     */     try
/*     */     {
/*     */       do {
/*  58 */         this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
/*  59 */         if (this.currentDate.longValue() < dataDate.longValue()) {
/*  60 */           this.prevData = this.curData;
/*     */         } else {
/*  62 */           if (this.currentDate.longValue() == dataDate.longValue()) {
/*  63 */             assignData(dataDate, dd, mtm);
/*     */             
/*     */ 
/*  66 */             return;
/*     */           }
/*  68 */           return;
/*     */         }
/*  57 */       } while ((this.curData = this.reader1D.getLine()) != null);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
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
/*     */   public void process(Long dataDate, CandleData cd)
/*     */     throws IOException
/*     */   {
/*  80 */     if ((this.curData == null) && (this.startFileFlag)) {
/*  81 */       this.startFileFlag = false;
/*  82 */       this.prevData = this.reader1D.getLine();
/*  83 */       if (this.prevData == null) {
/*  84 */         System.out.println("No data in Daily File");
/*  85 */         return;
/*     */       }
/*     */     }
/*  88 */     if (this.currentDate.longValue() > dataDate.longValue())
/*  89 */       return;
/*  90 */     if (this.currentDate.longValue() == dataDate.longValue()) {
/*  91 */       assignData(dataDate, cd);
/*  92 */       this.mtmDate = dataDate;
/*  93 */       this.prevData = this.curData;
/*  94 */       return; }
/*  95 */     if (dataDate.longValue() == 99999999L) {
/*  96 */       assignData(dataDate, cd);
/*  97 */       return;
/*     */     }
/*     */     try {
/*     */       do {
/* 101 */         this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
/* 102 */         if (this.currentDate.longValue() < dataDate.longValue()) {
/* 103 */           this.prevData = this.curData;
/*     */         } else {
/* 105 */           if (this.currentDate.longValue() == dataDate.longValue()) {
/* 106 */             assignData(dataDate, cd);
/* 107 */             this.mtmDate = dataDate;
/* 108 */             this.prevData = this.curData;
/* 109 */             return;
/*     */           }
/* 111 */           return;
/*     */         }
/* 100 */       } while ((this.curData = this.reader1D.getLine()) != null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */   private void assignData(Long dataDate, DailyData dd, Double mtm) {
/* 120 */     if (dataDate.equals(Long.valueOf(20011129L)))
/* 121 */       System.out.println();
/*     */     try {
/* 123 */       Boolean rollOver = Boolean.valueOf(Double.parseDouble(this.prevData[8]) != -1.0D);
/*     */       
/*     */ 
/*     */       Double roDiff;
/*     */       
/*     */       Double roDiff;
/*     */       
/* 130 */       if (rollOver.booleanValue()) {
/* 131 */         roDiff = Double.valueOf(Double.parseDouble(this.prevData[8]) - Double.parseDouble(this.prevData[5]));
/*     */       } else {
/* 133 */         roDiff = Double.valueOf(0.0D);
/*     */       }
/* 135 */       dd.updateData(Double.valueOf(Double.parseDouble(this.prevData[2])), 
/* 136 */         Double.valueOf(Double.parseDouble(this.prevData[3])), 
/* 137 */         Double.valueOf(Double.parseDouble(this.prevData[4])), 
/* 138 */         Double.valueOf(Double.parseDouble(this.prevData[5])), 
/* 139 */         Double.valueOf(Double.parseDouble(this.prevData[6])), roDiff, rollOver, 
/* 140 */         dataDate, mtm);
/*     */     } catch (Exception e) {
/* 142 */       System.out.println(dataDate);
/* 143 */       Double vol = Double.valueOf(0.0D);
/*     */       try {
/* 145 */         vol = Double.valueOf(Double.parseDouble(this.prevData[5]));
/*     */       }
/*     */       catch (Exception localException1) {}
/* 148 */       dd.updateData(Double.valueOf(Double.parseDouble(this.prevData[2])), 
/* 149 */         Double.valueOf(Double.parseDouble(this.prevData[3])), 
/* 150 */         Double.valueOf(Double.parseDouble(this.prevData[4])), 
/* 151 */         Double.valueOf(Double.parseDouble(this.prevData[5])), vol, Double.valueOf(0.0D), Boolean.valueOf(false), dataDate, 
/* 152 */         mtm);
/*     */     }
/*     */   }
/*     */   
/*     */   private void assignData(Long dataDate, CandleData cd) {
/* 157 */     if (dataDate.equals(Long.valueOf(20011129L)))
/* 158 */       System.out.println();
/*     */     try {
/* 160 */       Boolean rollOver = Boolean.valueOf(Double.parseDouble(this.prevData[8]) != -1.0D);
/*     */       
/*     */ 
/*     */       Double roDiff;
/*     */       
/*     */       Double roDiff;
/*     */       
/* 167 */       if (rollOver.booleanValue()) {
/* 168 */         roDiff = Double.valueOf(Double.parseDouble(this.prevData[8]) - Double.parseDouble(this.prevData[5]));
/*     */       } else {
/* 170 */         roDiff = Double.valueOf(0.0D);
/*     */       }
/* 172 */       cd.updateData(Double.valueOf(Double.parseDouble(this.prevData[2])), 
/* 173 */         Double.valueOf(Double.parseDouble(this.prevData[3])), 
/* 174 */         Double.valueOf(Double.parseDouble(this.prevData[4])), 
/* 175 */         Double.valueOf(Double.parseDouble(this.prevData[5])), 
/* 176 */         Double.valueOf(Double.parseDouble(this.prevData[6])), roDiff, rollOver, dataDate);
/*     */     } catch (Exception e) {
/* 178 */       System.out.println(dataDate);
/* 179 */       Double vol = Double.valueOf(0.0D);
/*     */       try {
/* 181 */         vol = Double.valueOf(Double.parseDouble(this.prevData[5]));
/*     */       }
/*     */       catch (Exception localException1) {}
/* 184 */       cd.updateData(Double.valueOf(Double.parseDouble(this.prevData[2])), 
/* 185 */         Double.valueOf(Double.parseDouble(this.prevData[3])), 
/* 186 */         Double.valueOf(Double.parseDouble(this.prevData[4])), 
/* 187 */         Double.valueOf(Double.parseDouble(this.prevData[5])), vol, Double.valueOf(0.0D), Boolean.valueOf(false), dataDate);
/*     */     }
/*     */   }
/*     */   
/*     */   public Long getPrevDate() {
/* 192 */     return this.mtmDate;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 196 */     this.reader1D.close();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/DailyDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */