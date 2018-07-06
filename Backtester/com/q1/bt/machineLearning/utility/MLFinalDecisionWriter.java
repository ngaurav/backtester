package com.q1.bt.machineLearning.utility;

import com.q1.csv.CSVWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MLFinalDecisionWriter
{
  private String decisionFilePath;
  CSVWriter decisionFileWriter;
  HashMap<String, TreeMap<Long, Boolean>> assetTimeStampDecisionMap;
  
  public MLFinalDecisionWriter(String decisionFileLocation)
    throws Exception
  {
    this.decisionFilePath = (decisionFileLocation + "/" + "ML Decisions.csv");
    this.assetTimeStampDecisionMap = new HashMap();
    
    try
    {
      this.decisionFileWriter = new CSVWriter(this.decisionFilePath, false, ",");
      

      this.decisionFileWriter.writeLine("TimeStamp, AssetName, Decision");

    }
    catch (IOException e)
    {
      System.out.println("Unable to initialize writer for the ML Decision Writer : " + decisionFileLocation);
      throw new Exception();
    }
  }
  
  public void writeAndSaveInMemoryMLDecisions(HashMap<String, Boolean> result, Long tradeTS) throws Exception
  {
    for (Map.Entry<String, Boolean> assetDecisionMap : result.entrySet())
    {

      String assetName = (String)assetDecisionMap.getKey();
      Boolean decision = (Boolean)assetDecisionMap.getValue();
      


      TreeMap<Long, Boolean> timeStampDecisionMap = (TreeMap)this.assetTimeStampDecisionMap.get(assetName);
      
      if (timeStampDecisionMap == null) {
        timeStampDecisionMap = new TreeMap();
      }
      timeStampDecisionMap.put(tradeTS, decision);
      this.assetTimeStampDecisionMap.put(assetName, timeStampDecisionMap);
      try {
        this.decisionFileWriter.writeLine(tradeTS + "," + assetName + "," + decision);
      }
      catch (IOException e) {
        System.out.println("Unable to write ML Decisions");
        throw new Exception();
      }
    }
  }
  
  public void closeWriter() throws IOException
  {
    this.decisionFileWriter.close();
  }
  
  public HashMap<String, TreeMap<Long, Boolean>> getAssetTimeStampDecisionMap() {
    return this.assetTimeStampDecisionMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/MLFinalDecisionWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */