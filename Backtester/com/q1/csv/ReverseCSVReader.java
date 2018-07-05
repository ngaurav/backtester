/*     */ package com.q1.csv;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.commons.io.input.ReversedLinesFileReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReverseCSVReader
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   ReversedLinesFileReader reader;
/*     */   char splitChar;
/*     */   
/*     */   public ReverseCSVReader(String fileName, char splitChar, int startIdx)
/*     */     throws IOException
/*     */   {
/*  23 */     this.reader = new ReversedLinesFileReader(new File(fileName));
/*  24 */     this.splitChar = splitChar;
/*  25 */     for (int i = 0; i < startIdx; i++) {
/*  26 */       this.reader.readLine();
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<String> readLine() throws IOException {
/*  31 */     String line = this.reader.readLine();
/*  32 */     if (line == null)
/*  33 */       return null;
/*  34 */     return split(line, this.splitChar);
/*     */   }
/*     */   
/*     */   public String[] readLineAsArray() throws IOException
/*     */   {
/*  39 */     String line = this.reader.readLine();
/*  40 */     if (line == null)
/*  41 */       return null;
/*  42 */     return splitAsArray(line, this.splitChar);
/*     */   }
/*     */   
/*     */   public String readLineAsString() throws IOException
/*     */   {
/*  47 */     String line = this.reader.readLine();
/*  48 */     return line;
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> readAll()
/*     */     throws IOException
/*     */   {
/*  54 */     ArrayList<String[]> allLines = new ArrayList();
/*  55 */     String[] line; while ((line = readLineAsArray()) != null) { String[] line;
/*  56 */       allLines.add(line); }
/*  57 */     return allLines;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/*  62 */     this.reader.close();
/*     */   }
/*     */   
/*     */   public static ArrayList<String> split(String line, char splitChar)
/*     */   {
/*  67 */     ArrayList<String> str = new ArrayList();
/*  68 */     int index = line.indexOf(splitChar);
/*  69 */     int prevIndex = index;
/*  70 */     if (index >= 0)
/*  71 */       str.add(line.substring(0, index));
/*     */     for (;;) {
/*  73 */       index = line.indexOf(splitChar, index + 1);
/*  74 */       if (index < 0)
/*     */         break;
/*  76 */       str.add(line.substring(prevIndex + 1, index));
/*  77 */       prevIndex = index;
/*     */     }
/*  79 */     str.add(line.substring(prevIndex + 1));
/*  80 */     return str;
/*     */   }
/*     */   
/*     */   public static String[] splitAsArray(String line, char splitChar)
/*     */   {
/*  85 */     ArrayList<String> str = new ArrayList();
/*  86 */     int index = line.indexOf(splitChar);
/*  87 */     int prevIndex = index;
/*  88 */     if (index >= 0)
/*  89 */       str.add(line.substring(0, index));
/*     */     for (;;) {
/*  91 */       index = line.indexOf(splitChar, index + 1);
/*  92 */       if (index < 0)
/*     */         break;
/*  94 */       str.add(line.substring(prevIndex + 1, index));
/*  95 */       prevIndex = index;
/*     */     }
/*  97 */     str.add(line.substring(prevIndex + 1));
/*     */     
/*  99 */     String[] outArray = new String[str.size()];
/* 100 */     for (int i = 0; i < str.size(); i++) {
/* 101 */       outArray[i] = ((String)str.get(i));
/*     */     }
/* 103 */     return outArray;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/ReverseCSVReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */