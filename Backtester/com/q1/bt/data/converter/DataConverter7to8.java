/*    */ package com.q1.bt.data.converter;
/*    */ 
/*    */ import com.q1.csv.CSVReader;
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.File;
/*    */ 
/*    */ public class DataConverter7to8
/*    */ {
/*    */   String inputPath;
/*    */   String outputPath;
/*    */   
/*    */   public DataConverter7to8(String inputPath, String outputPath)
/*    */   {
/* 14 */     this.inputPath = inputPath;
/* 15 */     this.outputPath = outputPath;
/*    */   }
/*    */   
/*    */   public void convertData() throws java.io.IOException {
/* 19 */     File folderName = new File(this.inputPath);
/*    */     
/* 21 */     File[] fileList = folderName.listFiles();
/*    */     File[] arrayOfFile1;
/* 23 */     int j = (arrayOfFile1 = fileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
/*    */       
/* 25 */       if (file.toString().substring(file.toString().length() - 3, file.toString().length()).equalsIgnoreCase("csv")) {
/* 26 */         int count = 1;
/*    */         
/* 28 */         String date = null;
/*    */         
/*    */ 
/* 31 */         CSVReader csvReader = new CSVReader(file.toString(), ',', 0);
/* 32 */         CSVWriter csvWriter = new CSVWriter(this.outputPath + "\\ " + file.getName(), false, ",");
/*    */         
/* 34 */         String write = "Date,Expiry,Open,High,Low,Close,Volume,OI,Rollover Close,Total Volume,Total OI";
/* 35 */         csvWriter.writeLine(write);
/*    */         
/* 37 */         String[] read = csvReader.getLine();
/*    */         
/* 39 */         write = "Start Date|" + read[0];
/*    */         
/* 41 */         while ((read = csvReader.getLine()) != null) {
/* 42 */           count++;
/* 43 */           date = read[0];
/*    */         }
/* 45 */         csvReader.close();
/*    */         
/* 47 */         write = write + ",End Date|" + date + "," + "Data Count|" + count;
/* 48 */         csvWriter.writeLine(write);
/* 49 */         csvReader = new CSVReader(file.toString(), ',', 0);
/*    */         
/* 51 */         while ((read = csvReader.getLine()) != null) {
/* 52 */           write = 
/* 53 */             read[0] + "," + read[1] + "," + read[2] + "," + read[3] + "," + read[4] + "," + read[5] + "," + read[6] + "," + read[7];
/*    */           
/* 55 */           if (Long.parseLong(read[8]) == 19000131L) {
/* 56 */             write = write + "," + "-1";
/*    */           } else {
/* 58 */             write = write + "," + read[12];
/*    */           }
/*    */           
/* 61 */           Double sum = Double.valueOf(Double.parseDouble(read[6]) + Double.parseDouble(read[13]));
/* 62 */           write = write + "," + sum;
/* 63 */           sum = Double.valueOf(Double.parseDouble(read[7]) + Double.parseDouble(read[14]));
/* 64 */           write = write + "," + sum;
/* 65 */           csvWriter.writeLine(write);
/*    */         }
/*    */         
/* 68 */         csvWriter.close();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws java.io.IOException
/*    */   {}
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/converter/DataConverter7to8.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */