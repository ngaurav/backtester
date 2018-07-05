/*    */ package com.q1.bt.machineLearning.driver.driverHelperClasses;
/*    */ 
/*    */ import com.q1.bt.process.machinelearning.LookbackType;
/*    */ import com.q1.bt.process.machinelearning.MergeType;
/*    */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*    */ import com.q1.csv.CSVWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.TreeSet;
/*    */ 
/*    */ public class ParameterWriter
/*    */ {
/*    */   public void createParamDir(String sourcePath, String destPath, MachineLearningParameter mlParameter) throws Exception
/*    */   {
/*    */     try
/*    */     {
/* 17 */       new File(destPath + "/Parameters").mkdirs();
/* 18 */       String mlPath = destPath + "/ML";
/* 19 */       new File(mlPath).mkdirs();
/*    */       
/*    */ 
/* 22 */       File paramFolder = new File(sourcePath + "\\Parameters\\");
/*    */       File[] arrayOfFile;
/* 24 */       int j = (arrayOfFile = paramFolder.listFiles()).length; for (int i = 0; i < j; i++) { File file = arrayOfFile[i];
/*    */         
/* 26 */         if (file.getName().contains("Parameters"))
/*    */         {
/* 28 */           com.q1.bt.machineLearning.utility.FileHandling.copyFolder(file, new File(destPath + "\\Parameters\\" + file.getName()));
/*    */         }
/*    */       }
/*    */       
/*    */ 
/* 33 */       appendParamList(destPath, mlParameter);
/*    */       
/*    */ 
/* 36 */       String factorFileName = destPath + "\\Parameters\\ML Factorlist.csv";
/* 37 */       CSVWriter writer = new CSVWriter(factorFileName, false, ",");
/*    */       
/* 39 */       Object factorSet = new TreeSet(mlParameter.getFactorList());
/*    */       
/* 41 */       for (String factor : (TreeSet)factorSet) {
/* 42 */         String[] curFactor = { factor };
/* 43 */         writer.writeLine(curFactor);
/*    */       }
/* 45 */       writer.flush();
/* 46 */       writer.close();
/*    */     }
/*    */     catch (IOException e) {
/* 49 */       System.out.println("Error in creating Parameter Files");
/* 50 */       throw new Exception();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private void appendParamList(String destPath, MachineLearningParameter mlParameter)
/*    */     throws IOException
/*    */   {
/* 59 */     String folderName = destPath;
/* 60 */     File paramPathFilesrc = new File(folderName + "\\Parameters");
/* 61 */     String[] files = paramPathFilesrc.list();
/* 62 */     String[] arrayOfString1; int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
/*    */       
/* 64 */       if (file.contains("Parameters")) {
/* 65 */         CSVWriter writerParam = new CSVWriter(paramPathFilesrc + "\\" + file, true, ",");
/* 66 */         writerParam.writeLine(new String[] { "ML Algorithm", mlParameter.getMlAlgorithm().getModelName() });
/* 67 */         writerParam.writeLine(new String[] { "ML Timestamp", mlParameter.getMlAlgoTimeStamp() });
/* 68 */         writerParam.writeLine(new String[] { "ML Consolidation Function", 
/* 69 */           mlParameter.getVarList().getClass().getSimpleName() });
/* 70 */         writerParam.writeLine(new String[] { "ML Lookback Period", mlParameter.getWindowPeriod().toString() });
/* 71 */         writerParam.writeLine(new String[] { "ML Update Period", mlParameter.getUpdatePeriod().toString() });
/* 72 */         writerParam
/* 73 */           .writeLine(new String[] { "ML Blackout Period", mlParameter.getBlackoutPeriod().toString() });
/* 74 */         writerParam.writeLine(new String[] { "ML Model Merge Type", mlParameter.getModelMergeType().toString() });
/* 75 */         writerParam.writeLine(new String[] { "ML Selection Merge Type", mlParameter.getSelectionMergeType().toString() });
/* 76 */         writerParam.writeLine(new String[] { "ML Lookback Type", mlParameter.getLookbackType().getVal() });
/* 77 */         writerParam
/* 78 */           .writeLine(new String[] { "ML Segment Asset Count", mlParameter.getSegmentCount().toString() });
/* 79 */         writerParam
/* 80 */           .writeLine(new String[] { "ML Overall Asset Count", mlParameter.getOverallCount().toString() });
/* 81 */         writerParam
/* 82 */           .writeLine(new String[] { "ML Segment Correl Threshold", mlParameter.getSegmentCorrelThreshold().toString() });
/* 83 */         writerParam
/* 84 */           .writeLine(new String[] { "ML Overall Correl Threshold", mlParameter.getOverallCorrelThreshold().toString() });
/* 85 */         writerParam.writeLine(new String[] { "ML Correl Period", mlParameter.getCorrelPeriod() });
/* 86 */         writerParam.writeLine(new String[] { "ML Bias", mlParameter.getBias() });
/* 87 */         writerParam.close();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/ParameterWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */