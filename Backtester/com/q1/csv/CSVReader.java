/*     */ package com.q1.csv;
/*     */ 
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CSVReader
/*     */ {
/*     */   BufferedReader reader;
/*     */   char splitChar;
/*  15 */   private static int defaultCharBufferSize = 8192;
/*     */   
/*     */   String[] dataLine;
/*     */   String[] nextDataLine;
/*     */   String dataLineString;
/*  20 */   boolean wait = false;
/*     */   
/*     */   public CSVReader(String fileName, char splitChar, int startIdx) throws IOException
/*     */   {
/*  24 */     this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
/*  25 */     this.splitChar = splitChar;
/*  26 */     for (int i = 0; i <= startIdx; i++) {
/*  27 */       String line = this.reader.readLine();
/*  28 */       if (line == null) {
/*  29 */         this.dataLine = null;
/*  30 */         this.dataLineString = null;
/*  31 */         break;
/*     */       }
/*  33 */       this.dataLine = MathLib.splitAsArray(line, ',');
/*  34 */       this.dataLineString = line;
/*     */     }
/*     */   }
/*     */   
/*     */   public CSVReader(String fileName, char splitChar, long startDate)
/*     */     throws IOException
/*     */   {
/*  41 */     this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
/*  42 */     this.splitChar = splitChar;
/*  43 */     String prevDateStr = "";
/*     */     for (;;)
/*     */     {
/*  46 */       String line = this.reader.readLine();
/*  47 */       String curDateStr = line.substring(0, 8);
/*  48 */       if (!curDateStr.equals(prevDateStr)) {
/*  49 */         long curDate = Long.parseLong(curDateStr);
/*  50 */         if (curDate >= startDate) {
/*  51 */           this.dataLine = MathLib.splitAsArray(line, ',');
/*  52 */           break;
/*     */         }
/*  54 */         prevDateStr = curDateStr;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CSVReader(String fileName, char splitChar, long startDate, int skipLines) throws IOException
/*     */   {
/*  61 */     this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
/*  62 */     this.splitChar = splitChar;
/*  63 */     String prevDateStr = "";
/*     */     
/*     */ 
/*  66 */     for (int i = 0; i < skipLines; i++) {
/*  67 */       this.reader.readLine();
/*     */     }
/*     */     for (;;) {
/*  70 */       String line = this.reader.readLine();
/*  71 */       String curDateStr = line.substring(0, 8);
/*  72 */       if (!curDateStr.equals(prevDateStr)) {
/*  73 */         long curDate = Long.parseLong(curDateStr);
/*  74 */         if (curDate >= startDate) {
/*  75 */           this.dataLine = MathLib.splitAsArray(line, ',');
/*  76 */           break;
/*     */         }
/*  78 */         prevDateStr = curDateStr;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public CSVReader(String fileName, char splitChar, long startDate, boolean lastLineCheck, int skipLines)
/*     */     throws IOException
/*     */   {
/*  87 */     if (!lastLineCheck) {
/*  88 */       return;
/*     */     }
/*  90 */     this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
/*  91 */     this.splitChar = splitChar;
/*  92 */     String prevDateStr = "";
/*     */     
/*     */ 
/*  95 */     for (int i = 0; i < skipLines; i++) {
/*  96 */       this.reader.readLine();
/*     */     }
/*  98 */     String prevLine = null;
/*     */     for (;;)
/*     */     {
/* 101 */       String line = this.reader.readLine();
/* 102 */       String curDateStr = line.substring(0, 8);
/* 103 */       if (!curDateStr.equals(prevDateStr)) {
/* 104 */         long curDate = Long.parseLong(curDateStr);
/* 105 */         if (curDate >= startDate) {
/* 106 */           if (prevLine == null) {
/* 107 */             this.dataLine = MathLib.splitAsArray(line, ','); break;
/*     */           }
/* 109 */           this.dataLine = MathLib.splitAsArray(prevLine, ',');
/* 110 */           this.nextDataLine = MathLib.splitAsArray(line, ',');
/* 111 */           this.wait = true;
/*     */           
/* 113 */           break;
/*     */         }
/* 115 */         prevDateStr = curDateStr;
/*     */       }
/* 117 */       prevLine = line;
/*     */     }
/*     */   }
/*     */   
/*     */   public CSVReader(String fileName, char splitChar, int startIdx, int bufferSize) throws IOException
/*     */   {
/* 123 */     this.reader = new BufferedReader(new FileReader(fileName), bufferSize);
/* 124 */     this.splitChar = splitChar;
/* 125 */     for (int i = 0; i < startIdx; i++) {
/* 126 */       this.reader.readLine();
/*     */     }
/*     */   }
/*     */   
/*     */   public String[] getLastReadLine() throws IOException {
/* 131 */     return this.dataLine;
/*     */   }
/*     */   
/*     */   public String getLastReadDate() throws IOException
/*     */   {
/* 136 */     return this.dataLine[0];
/*     */   }
/*     */   
/*     */   public String[] getLastReadDateTime() throws IOException
/*     */   {
/* 141 */     String[] dateTime = { this.dataLine[0], this.dataLine[1] };
/* 142 */     return dateTime;
/*     */   }
/*     */   
/*     */   public String[] getNextLine()
/*     */     throws IOException
/*     */   {
/* 148 */     this.dataLine = readLine();
/*     */     
/*     */ 
/* 151 */     if (this.dataLine == null) {
/* 152 */       close();
/*     */     }
/* 154 */     return this.dataLine;
/*     */   }
/*     */   
/*     */   public String[] getLine()
/*     */     throws IOException
/*     */   {
/* 160 */     String[] prevDataLine = this.dataLine;
/*     */     
/*     */ 
/* 163 */     if (!this.wait) {
/* 164 */       this.dataLine = readLine();
/*     */     } else {
/* 166 */       this.dataLine = this.nextDataLine;
/* 167 */       this.wait = false;
/*     */     }
/*     */     
/*     */ 
/* 171 */     if (prevDataLine == null) {
/* 172 */       close();
/*     */     }
/* 174 */     return prevDataLine;
/*     */   }
/*     */   
/*     */   public String getLineAsString()
/*     */     throws IOException
/*     */   {
/* 180 */     String prevDataLineString = this.dataLineString;
/*     */     
/* 182 */     this.dataLineString = readLineAsString();
/*     */     
/*     */ 
/* 185 */     if (prevDataLineString == null) {
/* 186 */       close();
/*     */     }
/* 188 */     return prevDataLineString;
/*     */   }
/*     */   
/*     */   private String[] readLine() throws IOException
/*     */   {
/* 193 */     String line = this.reader.readLine();
/* 194 */     if (line == null)
/* 195 */       return null;
/* 196 */     return MathLib.splitAsArray(line, this.splitChar);
/*     */   }
/*     */   
/*     */   private String readLineAsString() throws IOException
/*     */   {
/* 201 */     String line = this.reader.readLine();
/* 202 */     return line;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> readAll()
/*     */     throws IOException
/*     */   {
/* 208 */     ArrayList<String[]> allLines = new ArrayList();
/* 209 */     String[] line; while ((line = getLine()) != null) { String[] line;
/* 210 */       allLines.add(line); }
/* 211 */     return allLines;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 216 */     this.reader.close();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/CSVReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */