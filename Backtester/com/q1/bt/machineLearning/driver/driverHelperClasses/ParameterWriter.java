/*    */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*    */ 
/*    */ import com.q1.bt.process.machinelearning.LookbackType;
/*    */ import com.q1.bt.process.machinelearning.MergeType;
/*    */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.TreeSet;
/*    */ 
/*    */ public class ParameterWriter
/*    */ {
/*    */   public void createParamDir(String sourcePath, String destPath, String mlPath, MachineLearningParameter mlParameter, String algoLastModifiedTimeStamp) throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 18 */       new File(destPath + "/Parameters").mkdirs();
/* 19 */       mlPath = destPath + "/ML";
/* 20 */       new File(mlPath).mkdirs();
/* 21 */       com.q1.bt.machineLearning.utility.FileHandling.copyFolder(new File(sourcePath + "\\Parameters"), new File(destPath + "\\Parameters"));
/*    */       
/*    */ 
/* 24 */       appendParamList(destPath, algoLastModifiedTimeStamp, mlParameter);
/*    */       
/*    */ 
/* 27 */       String factorFileName = destPath + "\\Parameters\\ML Factorlist.csv";
/* 28 */       CSVWriter writer = new CSVWriter(factorFileName, false, ",");
/*    */       
/* 30 */       TreeSet<String> factorSet = new TreeSet(mlParameter.getFactorList());
/*    */       
/* 32 */       for (String factor : factorSet) {
/* 33 */         String[] curFactor = { factor };
/* 34 */         writer.writeLine(curFactor);
/*    */       }
/* 36 */       writer.flush();
/* 37 */       writer.close();
/*    */     }
/*    */     catch (IOException e) {
/* 40 */       System.out.println("Error in creating Parameter Files");
/* 41 */       throw new Exception();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private void appendParamList(String destPath, String algoLastModifiedTimeStamp, MachineLearningParameter mlParameter)
/*    */     throws IOException
/*    */   {
/* 51 */     String folderName = destPath;
/* 52 */     File paramPathFilesrc = new File(folderName + "\\Parameters");
/* 53 */     String[] files = paramPathFilesrc.list();
/* 54 */     String[] arrayOfString1; int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
/*    */       
/* 56 */       if (file.contains("Parameters")) {
/* 57 */         CSVWriter writerParam = new CSVWriter(paramPathFilesrc + "\\" + file, true, ",");
/* 58 */         writerParam.writeLine(new String[] { "ML Algorithm", mlParameter.getMlAlgorithm().getModelName() });
/* 59 */         writerParam.writeLine(new String[] { "ML Timestamp", algoLastModifiedTimeStamp });
/* 60 */         writerParam.writeLine(new String[] { "ML Consolidation Function", 
/* 61 */           mlParameter.getVarList().getClass().getSimpleName() });
/* 62 */         writerParam.writeLine(new String[] { "ML Lookback Period", mlParameter.getWindowPeriod().toString() });
/* 63 */         writerParam.writeLine(new String[] { "ML Update Period", mlParameter.getUpdatePeriod().toString() });
/* 64 */         writerParam
/* 65 */           .writeLine(new String[] { "ML Blackout Period", mlParameter.getBlackoutPeriod().toString() });
/* 66 */         writerParam.writeLine(new String[] { "ML Model Merge Type", mlParameter.getModelMergeType().toString() });
/* 67 */         writerParam.writeLine(new String[] { "ML Selection Merge Type", mlParameter.getSelectionMergeType().toString() });
/* 68 */         writerParam.writeLine(new String[] { "ML Lookback Type", mlParameter.getLookbackType().getVal() });
/* 69 */         writerParam
/* 70 */           .writeLine(new String[] { "ML Segment Asset Count", mlParameter.getSegmentCount().toString() });
/* 71 */         writerParam
/* 72 */           .writeLine(new String[] { "ML Overall Asset Count", mlParameter.getOverallCount().toString() });
/* 73 */         writerParam
/* 74 */           .writeLine(new String[] { "ML Segment Correl Threshold", mlParameter.getSegmentCorrelThreshold().toString() });
/* 75 */         writerParam
/* 76 */           .writeLine(new String[] { "ML Overall Correl Threshold", mlParameter.getOverallCorrelThreshold().toString() });
/* 77 */         writerParam.writeLine(new String[] { "ML Correl Period", mlParameter.getCorrelPeriod() });
/* 78 */         writerParam.close();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/ParameterWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */