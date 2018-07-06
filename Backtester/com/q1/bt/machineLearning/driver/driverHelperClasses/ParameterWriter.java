package com.q1.bt.machineLearning.driver.driverHelperClasses;

import com.q1.bt.process.machinelearning.LookbackType;
import com.q1.bt.process.machinelearning.MergeType;
import com.q1.bt.process.parameter.MachineLearningParameter;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

public class ParameterWriter
{
  public void createParamDir(String sourcePath, String destPath, MachineLearningParameter mlParameter) throws Exception
  {
    try
    {
      new File(destPath + "/Parameters").mkdirs();
      String mlPath = destPath + "/ML";
      new File(mlPath).mkdirs();
      

      File paramFolder = new File(sourcePath + "\\Parameters\\");
      File[] arrayOfFile;
      int j = (arrayOfFile = paramFolder.listFiles()).length; for (int i = 0; i < j; i++) { File file = arrayOfFile[i];
        
        if (file.getName().contains("Parameters"))
        {
          com.q1.bt.machineLearning.utility.FileHandling.copyFolder(file, new File(destPath + "\\Parameters\\" + file.getName()));
        }
      }
      

      appendParamList(destPath, mlParameter);
      

      String factorFileName = destPath + "\\Parameters\\ML Factorlist.csv";
      CSVWriter writer = new CSVWriter(factorFileName, false, ",");
      
      Object factorSet = new TreeSet(mlParameter.getFactorList());
      
      for (String factor : (TreeSet)factorSet) {
        String[] curFactor = { factor };
        writer.writeLine(curFactor);
      }
      writer.flush();
      writer.close();
    }
    catch (IOException e) {
      System.out.println("Error in creating Parameter Files");
      throw new Exception();
    }
  }
  


  private void appendParamList(String destPath, MachineLearningParameter mlParameter)
    throws IOException
  {
    String folderName = destPath;
    File paramPathFilesrc = new File(folderName + "\\Parameters");
    String[] files = paramPathFilesrc.list();
    String[] arrayOfString1; int j = (arrayOfString1 = files).length; for (int i = 0; i < j; i++) { String file = arrayOfString1[i];
      
      if (file.contains("Parameters")) {
        CSVWriter writerParam = new CSVWriter(paramPathFilesrc + "\\" + file, true, ",");
        writerParam.writeLine(new String[] { "ML Algorithm", mlParameter.getMlAlgorithm().getModelName() });
        writerParam.writeLine(new String[] { "ML Timestamp", mlParameter.getMlAlgoTimeStamp() });
        writerParam.writeLine(new String[] { "ML Consolidation Function", 
          mlParameter.getVarList().getClass().getSimpleName() });
        writerParam.writeLine(new String[] { "ML Lookback Period", mlParameter.getWindowPeriod().toString() });
        writerParam.writeLine(new String[] { "ML Update Period", mlParameter.getUpdatePeriod().toString() });
        writerParam
          .writeLine(new String[] { "ML Blackout Period", mlParameter.getBlackoutPeriod().toString() });
        writerParam.writeLine(new String[] { "ML Model Merge Type", mlParameter.getModelMergeType().toString() });
        writerParam.writeLine(new String[] { "ML Selection Merge Type", mlParameter.getSelectionMergeType().toString() });
        writerParam.writeLine(new String[] { "ML Lookback Type", mlParameter.getLookbackType().getVal() });
        writerParam
          .writeLine(new String[] { "ML Segment Asset Count", mlParameter.getSegmentCount().toString() });
        writerParam
          .writeLine(new String[] { "ML Overall Asset Count", mlParameter.getOverallCount().toString() });
        writerParam
          .writeLine(new String[] { "ML Segment Correl Threshold", mlParameter.getSegmentCorrelThreshold().toString() });
        writerParam
          .writeLine(new String[] { "ML Overall Correl Threshold", mlParameter.getOverallCorrelThreshold().toString() });
        writerParam.writeLine(new String[] { "ML Correl Period", mlParameter.getCorrelPeriod() });
        writerParam.writeLine(new String[] { "ML Bias", mlParameter.getBias() });
        writerParam.close();
      }
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/ParameterWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */