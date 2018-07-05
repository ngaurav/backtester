/*     */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class OutputProcessor
/*     */ {
/*     */   private HashMap<String, Boolean> segmentDoneReading;
/*     */   private HashMap<Long, HashMap<String, Double>> tempModelOutput;
/*     */   private HashMap<String, Long> tempTS;
/*     */   private String currentTSsegment;
/*  15 */   private Long prevNextTS = Long.valueOf(Long.MAX_VALUE);
/*     */   
/*     */   private HashMap<String, Long> firstOutputDateCollector;
/*     */   
/*     */   boolean rcompleteReading;
/*     */   
/*     */   Long rwriteTS;
/*     */   
/*  23 */   HashMap<String, String[]> tempRDataCollector = new HashMap();
/*     */   
/*     */   public OutputProcessor() {
/*  26 */     this.segmentDoneReading = new HashMap();
/*  27 */     this.tempModelOutput = new HashMap();
/*  28 */     this.tempTS = new HashMap();
/*  29 */     this.firstOutputDateCollector = new HashMap();
/*  30 */     this.rcompleteReading = false;
/*  31 */     this.rwriteTS = Long.valueOf(0L);
/*     */   }
/*     */   
/*     */ 
/*     */   public HashMap<String, Double> processOutput(HashMap<String, CSVReader> readerOutputCollector)
/*     */   {
/*  37 */     HashMap<String, Double> modelOutput = new HashMap();
/*  38 */     Long currentTS = null;
/*  39 */     boolean allFilesReadingDone = true;
/*  40 */     for (String segment : readerOutputCollector.keySet())
/*  41 */       if (this.segmentDoneReading.get(segment) == null)
/*     */       {
/*  43 */         String[] inData = null;
/*     */         
/*     */ 
/*  46 */         Long lineTS = Long.valueOf(0L);
/*  47 */         Long segmentTS = Long.valueOf(0L);
/*  48 */         String[] tempRData = (String[])this.tempRDataCollector.get(segment);
/*  49 */         if (tempRData == null) {
/*     */           try {
/*  51 */             tempRData = ((CSVReader)readerOutputCollector.get(segment)).getLine();
/*  52 */             this.tempRDataCollector.put(segment, tempRData);
/*  53 */             Long firstOutputDate = Long.valueOf(Long.parseLong(tempRData[0]));
/*  54 */             this.firstOutputDateCollector.put(segment, firstOutputDate);
/*     */           }
/*     */           catch (IOException e) {
/*  57 */             System.out.println("ML Error: R output is Empty");
/*  58 */             e.printStackTrace();
/*     */           }
/*     */         }
/*  61 */         segmentTS = Long.valueOf(Long.parseLong(tempRData[0]));
/*     */         
/*     */ 
/*  64 */         if (currentTS == null) {
/*  65 */           currentTS = segmentTS;
/*  66 */           this.currentTSsegment = segment;
/*  67 */         } else { if (segmentTS.compareTo(currentTS) > 0) {
/*     */             continue;
/*     */           }
/*  70 */           if (segmentTS.compareTo(currentTS) < 0) {
/*  71 */             if (this.tempTS.get(this.currentTSsegment) == null)
/*  72 */               this.tempTS.put(this.currentTSsegment, currentTS);
/*  73 */             if (((Long)this.tempTS.get(this.currentTSsegment)).equals(currentTS)) {
/*  74 */               if (this.tempModelOutput.get(this.tempTS.get(this.currentTSsegment)) == null) {
/*  75 */                 this.tempModelOutput.put((Long)this.tempTS.get(this.currentTSsegment), new HashMap(modelOutput));
/*     */               } else {
/*  77 */                 ((HashMap)this.tempModelOutput.get(this.tempTS.get(this.currentTSsegment))).putAll(new HashMap(modelOutput));
/*     */               }
/*     */             }
/*  80 */             currentTS = segmentTS;
/*  81 */             this.currentTSsegment = segment;
/*  82 */             modelOutput.clear();
/*     */           }
/*     */         }
/*     */         
/*  86 */         if (currentTS.equals(segmentTS)) {
/*  87 */           modelOutput.put(tempRData[1], Double.valueOf(tempRData[2]));
/*     */         }
/*     */         
/*  90 */         if (this.prevNextTS.compareTo(Long.valueOf(Long.parseLong(tempRData[0]))) >= 0)
/*     */         {
/*  92 */           if (!this.tempModelOutput.isEmpty()) {
/*  93 */             for (Long tsIterator : this.tempModelOutput.keySet()) {
/*  94 */               if (tsIterator.equals(currentTS)) {
/*  95 */                 modelOutput.putAll((Map)this.tempModelOutput.get(tsIterator));
/*  96 */                 this.tempModelOutput.remove(tsIterator);
/*     */               }
/*     */             }
/*     */           }
/*     */           try
/*     */           {
/* 102 */             while ((inData = ((CSVReader)readerOutputCollector.get(segment)).getLine()) != null)
/*     */             {
/* 104 */               lineTS = Long.valueOf(Long.parseLong(inData[0]));
/* 105 */               if (lineTS.longValue() == currentTS.longValue()) {
/* 106 */                 modelOutput.put(inData[1], Double.valueOf(inData[2]));
/*     */               } else {
/* 108 */                 this.tempRDataCollector.put(segment, inData);
/* 109 */                 this.prevNextTS = Long.valueOf(Long.parseLong(inData[0]));
/* 110 */                 if (this.tempTS.get(segment) == null) break;
/* 111 */                 this.tempModelOutput.remove(this.tempTS.get(segment));
/* 112 */                 this.tempTS.remove(segment);
/*     */                 
/*     */ 
/* 115 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (NumberFormatException|IOException e) {
/* 120 */             System.out.println("ML Error: Error in reading the R Output file");
/* 121 */             e.printStackTrace();
/*     */           }
/* 123 */           if (inData != null) {
/* 124 */             allFilesReadingDone = false;
/*     */           } else {
/* 126 */             this.segmentDoneReading.put(segment, Boolean.valueOf(true));
/*     */             try {
/* 128 */               ((CSVReader)readerOutputCollector.get(segment)).close();
/*     */             } catch (IOException e) {
/* 130 */               e.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 135 */     if (allFilesReadingDone) {
/* 136 */       this.rcompleteReading = true;
/*     */     }
/* 138 */     this.rwriteTS = currentTS;
/*     */     
/* 140 */     return modelOutput;
/*     */   }
/*     */   
/*     */ 
/*     */   public HashMap<String, Long> getFirstOutputDateCollector()
/*     */   {
/* 146 */     return this.firstOutputDateCollector;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRcompleteReading()
/*     */   {
/* 152 */     return this.rcompleteReading;
/*     */   }
/*     */   
/*     */ 
/*     */   public Long getRwriteTS()
/*     */   {
/* 158 */     return this.rwriteTS;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/OutputProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */