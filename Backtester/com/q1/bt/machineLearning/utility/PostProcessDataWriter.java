/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PostProcessDataWriter
/*    */ {
/*    */   CSVWriter writerPPD;
/*    */   com.q1.csv.CSVReader readerPPD;
/*    */   Long currentTS;
/* 11 */   String[] inData = null;
/*    */   
/*    */   public PostProcessDataWriter(String source, String destination) throws IOException {
/*    */     try {
/* 15 */       this.writerPPD = new CSVWriter(destination, false, ",");
/* 16 */       this.readerPPD = new com.q1.csv.CSVReader(source, ',', 0);
/* 17 */       this.inData = this.readerPPD.getLine();
/* 18 */       this.writerPPD.writeLine(this.inData);
/*    */     }
/*    */     catch (IOException e) {
/* 21 */       System.out.println(source + " not found");
/* 22 */       throw new IOException();
/*    */     }
/* 24 */     this.currentTS = Long.valueOf(0L);
/*    */   }
/*    */   
/*    */   public void process(Long resultTS, int write) throws IOException
/*    */   {
/* 29 */     if ((this.currentTS.longValue() > resultTS.longValue()) || (resultTS.longValue() == 0L)) {
/* 30 */       return;
/*    */     }
/* 32 */     if (this.currentTS.longValue() == resultTS.longValue()) {
/* 33 */       if (write == 0) {
/* 34 */         this.inData[6] = "0";
/* 35 */         this.inData[7] = "0";
/* 36 */         this.inData[8] = Double.toString(NaN.0D);
/* 37 */         this.inData[9] = Double.toString(NaN.0D);
/*    */       }
/* 39 */       this.writerPPD.writeLine(this.inData);
/*    */     }
/* 41 */     while ((this.inData = this.readerPPD.getLine()) != null) {
/* 42 */       this.currentTS = Long.valueOf(Long.parseLong(this.inData[0]) / 1000000L);
/* 43 */       if (this.currentTS.longValue() == resultTS.longValue()) {
/* 44 */         if (write == 0) {
/* 45 */           this.inData[6] = "0";
/* 46 */           this.inData[7] = "0";
/*    */           
/* 48 */           this.inData[8] = Double.toString(NaN.0D);
/* 49 */           this.inData[9] = Double.toString(NaN.0D);
/*    */         }
/* 51 */         this.writerPPD.writeLine(this.inData);
/* 52 */       } else { if (resultTS.longValue() <= this.currentTS.longValue()) {
/*    */           break;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 61 */     this.writerPPD.close();
/* 62 */     this.readerPPD.close();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/PostProcessDataWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */