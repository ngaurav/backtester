/*     */ package com.q1.bt.machineLearning.utility;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class MetadataReader
/*     */ {
/*     */   CSVReader readerMD;
/*     */   Long currentDate;
/*     */   String[] curData;
/*     */   String[] prevData;
/*     */   private String dataPath;
/*     */   private ArrayList<String> scripList;
/*     */   
/*     */   public MetadataReader(String sourcePath, String dataPath, ArrayList<String> scripList)
/*     */   {
/*  23 */     this.dataPath = dataPath;
/*  24 */     this.scripList = scripList;
/*     */   }
/*     */   
/*     */   public MetadataReader(String dataPath, String asset) throws Exception {
/*  28 */     this.dataPath = dataPath;
/*     */     try {
/*  30 */       this.readerMD = new CSVReader(dataPath, ',', 0);
/*     */     } catch (IOException e) {
/*  32 */       System.out.println(dataPath + " not found");
/*  33 */       throw new IOException();
/*     */     }
/*  35 */     this.currentDate = Long.valueOf(0L);
/*  36 */     this.curData = null;
/*  37 */     this.prevData = null;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getScripList(String sourcePath) {
/*  41 */     TreeSet<String> scripSet = new TreeSet();
/*  42 */     String path = sourcePath + "/" + "Trade Data";
/*  43 */     File tradeFolder = new File(path);
/*  44 */     File[] tradeFiles = tradeFolder.listFiles();
/*  45 */     File[] arrayOfFile1; int j = (arrayOfFile1 = tradeFiles).length; for (int i = 0; i < j; i++) { File tradeFile = arrayOfFile1[i];
/*  46 */       String[] nameParts = tradeFile.getName().split(" ");
/*  47 */       String assetName = nameParts[1] + " " + 
/*  48 */         nameParts[2] + " " + nameParts[3] + " " + nameParts[4] + 
/*  49 */         " " + nameParts[5];
/*  50 */       scripSet.add(assetName);
/*     */     }
/*  52 */     return new ArrayList(scripSet);
/*     */   }
/*     */   
/*     */   public ArrayList<String> getScripList() {
/*  56 */     return this.scripList;
/*     */   }
/*     */   
/*     */   public HashMap<String, TreeMap<Long, ArrayList<Long>>> getMetadataMap()
/*     */     throws Exception
/*     */   {
/*  62 */     HashMap<String, TreeMap<Long, ArrayList<Long>>> scripwiseMetadataMap = new HashMap();
/*  63 */     for (String asset : this.scripList) {
/*  64 */       TreeMap<Long, ArrayList<Long>> scripDatewiseMD = new TreeMap();
/*  65 */       String assetDataPath = this.dataPath + "\\MD\\" + asset + " " + 
/*  66 */         "MD.csv";
/*     */       
/*  68 */       CSVReader reader = new CSVReader(assetDataPath, ',', 0);
/*     */       
/*     */       String[] metaDataLine;
/*  71 */       while ((metaDataLine = reader.getLine()) != null) { String[] metaDataLine;
/*  72 */         Long date = Long.valueOf(Long.parseLong(metaDataLine[0]));
/*  73 */         ArrayList<Long> data = new ArrayList();
/*  74 */         for (int i = 5; i <= 12; i++)
/*  75 */           data.add(Long.valueOf(Long.parseLong(metaDataLine[i])));
/*  76 */         scripDatewiseMD.put(date, data);
/*     */       }
/*     */       
/*  79 */       scripwiseMetadataMap.put(asset, scripDatewiseMD);
/*     */     }
/*     */     
/*  82 */     return scripwiseMetadataMap;
/*     */   }
/*     */   
/*     */   public void process(Long dataDate, MetaData md) throws IOException
/*     */   {
/*  87 */     if (this.curData == null) {
/*  88 */       this.curData = this.readerMD.getLine();
/*  89 */       if (this.curData == null)
/*     */       {
/*  91 */         return;
/*     */       }
/*  93 */       this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
/*     */     }
/*  95 */     if (this.currentDate.longValue() > dataDate.longValue())
/*  96 */       return;
/*  97 */     if (this.currentDate.longValue() == dataDate.longValue()) {
/*  98 */       assignData(dataDate, md);
/*  99 */       return;
/*     */     }
/* 101 */     while ((this.curData = this.readerMD.getLine()) != null) {
/* 102 */       this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
/* 103 */       if (this.currentDate.longValue() >= dataDate.longValue())
/*     */       {
/* 105 */         if (this.currentDate.longValue() == dataDate.longValue()) {
/* 106 */           assignData(dataDate, md);
/* 107 */           return;
/*     */         }
/* 109 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void assignData(Long dataDate, MetaData md)
/*     */   {
/* 116 */     md.updateData(this.currentDate, Double.valueOf(Double.parseDouble(this.curData[3])), 
/* 117 */       Double.valueOf(Double.parseDouble(this.curData[4])), Long.valueOf(Long.parseLong(this.curData[5])), 
/* 118 */       Long.valueOf(Long.parseLong(this.curData[6])), Long.valueOf(Long.parseLong(this.curData[7])), 
/* 119 */       Long.valueOf(Long.parseLong(this.curData[8])), Long.valueOf(Long.parseLong(this.curData[9])), 
/* 120 */       Long.valueOf(Long.parseLong(this.curData[10])), Long.valueOf(Long.parseLong(this.curData[11])), 
/* 121 */       Long.valueOf(Long.parseLong(this.curData[12])));
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/MetadataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */