/*     */ package com.q1.bt.machineLearning.driver.correl;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public class OldTradeBookToNewOrderBook
/*     */ {
/*     */   public static void main(String[] args) throws IOException
/*     */   {
/*  13 */     String tradeBookPath = "C:/Q1/ML Correl Management/BT Tradebook Path";
/*  14 */     String orderBookPath = "C:/Q1/ML Correl Management/BT Orderbook Path";
/*  15 */     String dataPath = "E:/Q1/Datalib/DataLib 7.1/CC";
/*     */     
/*  17 */     generateOrderBooks(tradeBookPath, orderBookPath, dataPath);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void generateOrderBooks(String tradeBookPath, String orderBookPath, String dataPath)
/*     */     throws IOException
/*     */   {
/*  24 */     File masterTadeBookFolder = new File(tradeBookPath);
/*     */     
/*     */     File[] arrayOfFile1;
/*  27 */     int j = (arrayOfFile1 = masterTadeBookFolder.listFiles()).length; for (int i = 0; i < j; i++) { File strategyFolder = arrayOfFile1[i];
/*     */       
/*     */       File[] arrayOfFile2;
/*  30 */       int m = (arrayOfFile2 = strategyFolder.listFiles()).length; for (int k = 0; k < m; k++) { File tradebookFile = arrayOfFile2[k];
/*  31 */         System.out.println("Processing:" + tradebookFile.getName());
/*  32 */         String[] tradebookVal = tradebookFile.getName().split(" ");
/*  33 */         String strategyID = tradebookVal[0];
/*  34 */         String strategyDatatype = strategyID.split("_")[1];
/*  35 */         String scripListID = tradebookVal[1] + "$" + tradebookVal[2] + "$" + tradebookVal[3] + "$" + 
/*  36 */           tradebookVal[4] + "$" + tradebookVal[5];
/*  37 */         String scripID = tradebookVal[1] + " " + tradebookVal[2] + " " + tradebookVal[3] + " " + tradebookVal[4] + 
/*  38 */           " " + tradebookVal[5];
/*  39 */         String dataID = tradebookVal[1] + " " + tradebookVal[2] + " " + tradebookVal[3] + " " + tradebookVal[4] + 
/*  40 */           " " + strategyDatatype;
/*     */         
/*  42 */         String orderBookFilePath = orderBookPath + "/" + strategyID + " " + scripListID;
/*     */         
/*  44 */         if (!new File(orderBookFilePath).exists()) {
/*  45 */           new File(orderBookFilePath).mkdirs();
/*     */         }
/*  47 */         CSVReader tradeReader = new CSVReader(tradebookFile.getAbsolutePath(), ',', 0);
/*     */         
/*  49 */         CSVWriter writer = new CSVWriter(orderBookFilePath + "/" + scripID + " OrderBook.csv", false, ",");
/*     */         
/*  51 */         CSVReader dataReader = new CSVReader(dataPath + "/" + dataID + ".csv", ',', 0);
/*     */         
/*     */ 
/*  54 */         Long lastDateTime = Long.valueOf(0L);Long dataDateTime = Long.valueOf(0L);
/*  55 */         boolean datacheck = false;
/*  56 */         String[] tradeLine; while ((tradeLine = tradeReader.getLine()) != null) {
/*     */           String[] tradeLine;
/*  58 */           if (!tradeLine[4].equals("ROL"))
/*     */           {
/*  60 */             if (tradeLine[4].equals("OPEN")) {
/*  61 */               tradeLine[4] = "STOP";
/*  62 */             } else if (tradeLine[4].equals("SL")) {
/*  63 */               tradeLine[4] = "STOP";
/*  64 */             } else if (tradeLine[4].equals("LIM")) {
/*  65 */               tradeLine[4] = "LIMIT";
/*     */             }
/*  67 */             Long tbDateTime = Long.valueOf(Long.parseLong(tradeLine[0]));
/*     */             
/*  69 */             if (!dataDateTime.equals(tbDateTime)) {
/*     */               do
/*     */               {
/*  72 */                 String[] dataLine = dataReader.getLine();
/*     */                 
/*  74 */                 if (dataLine == null) {
/*     */                   try {
/*  76 */                     tradeReader.close();
/*  77 */                     dataReader.close();
/*  78 */                     writer.close();
/*     */                   }
/*     */                   catch (Exception localException) {}
/*     */                   
/*     */ 
/*     */ 
/*  84 */                   datacheck = true;
/*  85 */                   break;
/*     */                 }
/*     */                 
/*  88 */                 lastDateTime = dataDateTime;
/*     */                 
/*  90 */                 if (strategyDatatype.equals("1M")) {
/*  91 */                   dataDateTime = Long.valueOf(Long.parseLong(dataLine[0] + dataLine[1]));
/*     */                 } else {
/*  93 */                   dataDateTime = Long.valueOf(Long.parseLong(dataLine[0] + "235900"));
/*     */                 }
/*  95 */               } while (!dataDateTime.equals(tbDateTime));
/*     */             }
/*     */             
/*  98 */             if (datacheck) {
/*  99 */               datacheck = false;
/* 100 */               break;
/*     */             }
/* 102 */             Long orderDateTime = lastDateTime;
/* 103 */             String[] outOrderLine = { orderDateTime.toString(), scripListID, scripID, tradeLine[3], 
/* 104 */               tradeLine[4], tradeLine[5], tradeLine[7] };
/* 105 */             writer.writeLine(outOrderLine);
/*     */           }
/*     */         }
/* 108 */         try { tradeReader.close();
/* 109 */           dataReader.close();
/* 110 */           writer.close();
/*     */         }
/*     */         catch (Exception localException1) {}
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/OldTradeBookToNewOrderBook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */