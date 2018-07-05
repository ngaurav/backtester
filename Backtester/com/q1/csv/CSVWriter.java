/*    */ package com.q1.csv;
/*    */ 
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ public class CSVWriter
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   BufferedWriter writer;
/*    */   String splitChar;
/*    */   
/*    */   public CSVWriter(String fileName, boolean writeMode, String splitChar)
/*    */     throws IOException
/*    */   {
/* 20 */     this.writer = new BufferedWriter(new FileWriter(fileName, writeMode));
/* 21 */     this.splitChar = splitChar;
/*    */   }
/*    */   
/*    */   public void writeLine(ArrayList<String> line) throws IOException
/*    */   {
/* 26 */     String outLine = (String)line.get(0);
/* 27 */     for (int i = 1; i < line.size(); i++)
/* 28 */       outLine = outLine + this.splitChar + (String)line.get(i);
/* 29 */     this.writer.write(outLine);
/* 30 */     this.writer.write("\n");
/*    */   }
/*    */   
/*    */   public void writeLine(String[] line) throws IOException
/*    */   {
/* 35 */     String outLine = line[0];
/* 36 */     for (int i = 1; i < line.length; i++)
/* 37 */       outLine = outLine + this.splitChar + line[i];
/* 38 */     this.writer.write(outLine);
/* 39 */     this.writer.write("\n");
/*    */   }
/*    */   
/*    */   public void writeLine(String line) throws IOException
/*    */   {
/* 44 */     this.writer.write(line);
/* 45 */     this.writer.write("\n");
/*    */   }
/*    */   
/*    */   public void write(String str) throws IOException
/*    */   {
/* 50 */     this.writer.write(str);
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 55 */     this.writer.flush();
/* 56 */     this.writer.close();
/*    */   }
/*    */   
/*    */   public void flush() throws IOException
/*    */   {
/* 61 */     this.writer.flush();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/CSVWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */