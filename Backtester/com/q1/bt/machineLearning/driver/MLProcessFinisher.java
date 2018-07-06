package com.q1.bt.machineLearning.driver;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
import com.q1.bt.machineLearning.utility.DailyDataReader;
import com.q1.bt.machineLearning.utility.PostProcessDataWriter;
import com.q1.bt.machineLearning.utility.TradeFilteredMTMWriter;
import com.q1.bt.machineLearning.utility.TradeFilteredTDWriter;
import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;









public class MLProcessFinisher
{
  public void endProcess(boolean postProcess, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, PostProcessDataWriter> postProcessWriterCollection, HashMap<String, DailyDataReader> dailyReaderCollection, CSVWriter mlLogWriter, CSVWriter combinedMTM, CSVWriter correlLogWriter, String destPath, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap)
    throws IOException
  {
    closeFiles(postProcess, algorithmMap, mtmWriterCollection, tdWriterCollection, postProcessWriterCollection, 
      dailyReaderCollection, mlLogWriter, combinedMTM, correlLogWriter);
    
    ArrayList<String> assetList = null;
    
    filterForTradeData(destPath, assetList, tdWriterCollection, modelSegmentWiseAssetUniverseMap);
  }
  



  private void closeFiles(boolean postProcess, HashMap<String, MLAlgo> algorithmMap, HashMap<String, TradeFilteredMTMWriter> mtmWriterCollection, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, PostProcessDataWriter> postProcessWriterCollection, HashMap<String, DailyDataReader> dailyReaderCollection, CSVWriter mlLogWriter, CSVWriter combinedMTM, CSVWriter correlLogWriter)
    throws IOException
  {
    for (MLAlgo mlalgo : algorithmMap.values()) {
      mlalgo.close();
    }
    for (String assetName : mtmWriterCollection.keySet())
    {
      ((TradeFilteredMTMWriter)mtmWriterCollection.get(assetName)).close();
      ((TradeFilteredTDWriter)tdWriterCollection.get(assetName)).close();
      if (postProcess) {
        ((PostProcessDataWriter)postProcessWriterCollection.get(assetName)).close();
      }
    }
    mlLogWriter.close();
    try {
      correlLogWriter.close();
    }
    catch (Exception localException) {}
    
    combinedMTM.close();
  }
  



  private void filterForTradeData(String destPath, ArrayList<String> assetList, HashMap<String, TradeFilteredTDWriter> tdWriterCollection, HashMap<String, ArrayList<Asset>> modelSegmentWiseAssetUniverseMap)
  {
    tdWriterCollection = new HashMap();
    String destFolderTD = destPath + "/Trade Data";
    
    String destFolderMTM = destPath + "/MTM Data";
    Iterator localIterator2;
    for (Iterator localIterator1 = modelSegmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
      localIterator2 = ((ArrayList)entry.getValue()).iterator(); continue;Asset asset = (Asset)localIterator2.next();
      String strategyName = asset.getStrategyName();
      String scripListName = asset.getScripListName();
      String scripName = asset.getScrip().scripID;
      String tradeBookFile = scripName + " Tradebook.csv";
      String mtmFile = scripName + " MTM.csv";
      String strategyScripListFolder = strategyName + " " + scripListName;
      String assetName = asset.getAssetName();
      CSVReader cread = null;
      try
      {
        cread = new CSVReader(destFolderTD + "/" + strategyScripListFolder + "/" + tradeBookFile, ',', 0);
      } catch (IOException e) {
        cread = null;
        File folder = new File(destFolderTD + "/" + strategyScripListFolder);
        if (!folder.exists()) {
          folder.mkdirs();
        }
        try {
          CSVWriter cwrite = new CSVWriter(destFolderTD + "/" + strategyScripListFolder + "/" + tradeBookFile, 
            false, ",");
          cwrite.close();
        }
        catch (IOException e1) {
          e1.printStackTrace();
        }
      }
      try
      {
        if ((cread == null) || (cread.getLine() == null)) {
          File folder = new File(destFolderMTM + "/" + strategyScripListFolder);
          if (!folder.exists())
            folder.mkdirs();
          CSVWriter cwrite = new CSVWriter(destFolderMTM + "/" + strategyScripListFolder + "/" + mtmFile, 
            false, ",");
          cwrite.close();
        }
      } catch (IOException e) {
        System.out.println("Error in Processing empty Tradebooks " + assetName);
      }
      if (cread != null) {
        try {
          cread.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLProcessFinisher.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */