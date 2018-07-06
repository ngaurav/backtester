package com.q1.bt.machineLearning.driver;

import com.q1.bt.data.classes.Scrip;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.machineLearning.absclasses.Correl;
import com.q1.bt.machineLearning.absclasses.DailyIndColl;
import com.q1.bt.machineLearning.absclasses.Factor;
import com.q1.bt.machineLearning.absclasses.FactorType;
import com.q1.bt.machineLearning.absclasses.ValueType;
import com.q1.bt.machineLearning.absclasses.VarList;
import com.q1.bt.machineLearning.driver.driverHelperClasses.Asset;
import com.q1.bt.machineLearning.utility.CandleData;
import com.q1.bt.machineLearning.utility.DailyData;
import com.q1.bt.machineLearning.utility.DailyDataReader;
import com.q1.bt.machineLearning.utility.MetadataReader;
import com.q1.bt.machineLearning.utility.TradeAndMTMDataProcessor;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.parameter.MachineLearningParameter;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;



















public class MLInputFileGenerator
{
  public boolean bias;
  HashMap<String, DailyDataReader> dailyReaderCollection;
  HashMap<String, DailyIndColl> dailyPriceIndCollection;
  HashMap<String, DailyIndColl> dailyMTMIndCollection;
  HashMap<String, DailyData> dailyDataCollection;
  HashMap<String, CandleData> candleDataCollection;
  HashMap<String, HashMap<String, Correl>> correlMap;
  TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals;
  HashMap<String, CSVWriter> rInputWriterMap;
  HashMap<String, ArrayList<Double[]>> dailyPriceIndVars;
  HashMap<String, ArrayList<Double[]>> dailyMTMIndVars;
  private ArrayList<String> priceIndNames;
  private ArrayList<String> mtmIndNames;
  BacktesterGlobal btGlobal;
  Backtest backtest;
  MachineLearningParameter mlParameter;
  
  public MLInputFileGenerator(BacktesterGlobal btGlobal, Backtest backtest, MachineLearningParameter mlParameter, HashMap<String, DailyDataReader> dailyReaderCollection, boolean bias)
  {
    this.btGlobal = btGlobal;
    this.backtest = backtest;
    this.mlParameter = mlParameter;
    
    this.dailyReaderCollection = dailyReaderCollection;
    
    this.bias = bias;
  }
  


  public void createInputFile(boolean nextLayer, String sourcePath, String destPath, String dataPath, HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, TradeAndMTMDataProcessor stratTradePnL, ArrayList<Long> dateList, HashMap<String, TreeMap<Long, Long>> tradeStartEndMaps, HashMap<String, TreeMap<Long, Integer>> tradeStartDateTradeSideMaps, Long dailyStartDate)
    throws Exception
  {
    HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap;
    
    try
    {
      MetadataReader mdMap = new MetadataReader(sourcePath, dataPath, scripUniverse);
      mdScripwiseMap = mdMap.getMetadataMap();
    } catch (Exception e) { HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap;
      mdScripwiseMap = new HashMap();
    }
    


    File destDir = new File(destPath);
    if ((destDir.exists()) && (nextLayer))
    {
      for (int i = 1;; i++) {
        File tempDir = new File(destPath + "/Temp/Temp" + i);
        if (!tempDir.exists()) {
          tempDir.mkdirs();
          break;
        }
      }
      

      sourcePath = destPath + "/Temp/Temp" + i;
      String[] arrayOfString1;
      int j = (arrayOfString1 = destDir.list()).length; for (int i = 0; i < j; i++) { String dirName = arrayOfString1[i];
        if (!dirName.contains("Temp")) {
          File dir = new File(destPath + "/" + dirName);
          if (dir.isFile()) {
            if (dir.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName))) {
              System.out.println(dirName + " moved successfully");
            } else
              System.out.println(dirName + " could not be moved");
          } else {
            File newDir = new File(destPath + "/Temp/Temp" + i + "/" + dirName);
            newDir.mkdirs();
            String[] arrayOfString2; int m = (arrayOfString2 = dir.list()).length; for (int k = 0; k < m; k++) { String fileName = arrayOfString2[k];
              File file = new File(destPath + "/" + dirName + "/" + fileName);
              if (file.renameTo(new File(destPath + "/Temp/Temp" + i + "/" + dirName + "/" + fileName))) {
                System.out.println(fileName + " moved successfully");
              } else {
                System.out.println(fileName + " could not be moved");
              }
            }
          }
        }
      }
    }
    initializeML(segmentWiseAssetUniverseMap, scripUniverse, mdScripwiseMap, sourcePath, destPath, dataPath, 
      dailyStartDate);
    

    Calendar startDate = new GregorianCalendar();
    Calendar endDate = new GregorianCalendar();
    
    SimpleDateFormat numericDate = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat numericYear = new SimpleDateFormat("yyyy");
    try {
      startDate.setTime(numericDate.parse(dailyStartDate.toString()));
      endDate.setTime(numericDate.parse(((Long)dateList.get(dateList.size() - 1)).toString()));
    } catch (ParseException e1) {
      System.out.println("Error in parsing Start and End daily date");
      e1.printStackTrace();
    }
    




    this.mlParameter.getVarList().getVarNames();
    Long curYear = Long.valueOf(1770L);
    HashMap<String, ArrayList<Double[]>> priceIndVarsMap = new HashMap();
    HashMap<String, ArrayList<Double[]>> mtmIndVarsMap = new HashMap();
    
    endDate.add(5, 1);
    
    for (Calendar dt = startDate; dt.compareTo(endDate) <= 0; dt.add(5, 1))
    {


      priceIndVarsMap.clear();
      mtmIndVarsMap.clear();
      Long year;
      Long year; if (dt.compareTo(endDate) == 0) {
        Long date = Long.valueOf(99999999L);
        year = Long.valueOf(9999L);
      } else {
        date = Long.valueOf(Long.parseLong(numericDate.format(dt.getTime())));
        year = Long.valueOf(Long.parseLong(numericYear.format(dt.getTime())));
      }
      
      if (!curYear.equals(year)) {
        System.out.println("Processing Year: " + year);
        curYear = year;
      }
      

      dailyUpdate(segmentWiseAssetUniverseMap, scripUniverse, (Long)date, stratTradePnL);
      
      Object tradePnLList = (HashMap)stratTradePnL.getTradeMTMMat().get(date);
      
      priceIndVarsMap.putAll(this.dailyPriceIndVars);
      mtmIndVarsMap.putAll(this.dailyMTMIndVars);
      Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.entrySet().iterator(); localIterator1.hasNext(); 
          


          localIterator2.hasNext())
      {
        Map.Entry<String, ArrayList<Asset>> entry = (Map.Entry)localIterator1.next();
        String segmentName = (String)entry.getKey();
        ArrayList<Asset> segmentAssetUniverse = (ArrayList)entry.getValue();
        
        localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
        String assetName = asset.getAssetName();
        if (tradeStartEndMaps.get(assetName) != null)
        {
          Double mtm = tradePnLList == null ? null : (Double)((HashMap)tradePnLList).get(assetName);
          
          if (dt.compareTo(endDate) == 0) {
            mtm = Double.valueOf(0.0D);
          }
          
          if (mtm != null) {
            String scripName = asset.getScrip().scripID;
            
            if ((this.priceIndNames.size() == 0) || (priceIndVarsMap.get(scripName) != null))
            {

              if ((this.mtmIndNames.size() == 0) || (mtmIndVarsMap.get(assetName) != null))
              {

                CSVWriter rInputWriter = (CSVWriter)this.rInputWriterMap.get(segmentName);
                String tradeEndDate = "";
                
                if (((TreeMap)tradeStartEndMaps.get(assetName)).get(date) == null) {
                  tradeEndDate = "99999999";
                } else {
                  tradeEndDate = ((Long)((TreeMap)tradeStartEndMaps.get(assetName)).get(date)).toString();
                }
                

                Integer tradeSide = (Integer)((TreeMap)tradeStartDateTradeSideMaps.get(assetName)).get(date);
                
                if (this.bias)
                {
                  if (tradeSide == null)
                  {
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(1), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-1), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);


                  }
                  else if (tradeSide.equals(Integer.valueOf(0)))
                  {
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-1), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
                  }
                  else
                  {
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, tradeSide, priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
                    writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(-tradeSide.intValue()), priceIndVarsMap, mtmIndVarsMap, scripName, Double.valueOf(0.0D), false);
                  }
                  

                }
                else
                {
                  writeLineInInputFile(rInputWriter, (Long)date, tradeEndDate, assetName, Integer.valueOf(0), priceIndVarsMap, mtmIndVarsMap, scripName, mtm, false);
                }
              }
            }
          }
        }
      }
    }
    







    for (Object date = this.rInputWriterMap.values().iterator(); ((Iterator)date).hasNext();) { CSVWriter rInputWriter = (CSVWriter)((Iterator)date).next();
      rInputWriter.flush();
      rInputWriter.close();
    }
  }
  







  private void initializeML(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, HashMap<String, TreeMap<Long, ArrayList<Long>>> mdScripwiseMap, String sourcePath, String destPath, String dataPath, Long dailyStartDate)
    throws Exception
  {
    this.dailyMTMIndVars = new HashMap();
    this.dailyPriceIndVars = new HashMap();
    
    initDailyData(segmentWiseAssetUniverseMap);
    initCandleData(scripUniverse);
    
    initIndicators(segmentWiseAssetUniverseMap, scripUniverse, this.mlParameter.getFactorList(), 
      this.mlParameter.getVarList(), dataPath);
    initCorelMap(scripUniverse, this.mlParameter.getCorrelPeriod().intValue());
    

    initwriters(dataPath, segmentWiseAssetUniverseMap.keySet(), sourcePath, destPath);
  }
  


  private void initCorelMap(ArrayList<String> scripList, int correlPeriod)
  {
    this.correlMap = new HashMap();
    this.correlVals = new TreeMap();
    for (int i = 0; i < scripList.size(); i++) {
      String scrip1 = (String)scripList.get(i);
      this.correlMap.put(scrip1, new HashMap());
      for (int j = 0; j < i; j++) {
        String scrip2 = (String)scripList.get(j);
        Correl corEle = new Correl(correlPeriod);
        ((HashMap)this.correlMap.get(scrip1)).put(scrip2, corEle);
      }
    }
  }
  



  private void initIndicators(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripList, ArrayList<String> factorList, VarList varListType, String dataPath)
    throws Exception
  {
    this.dailyPriceIndCollection = new HashMap();
    this.dailyMTMIndCollection = new HashMap();
    
    this.priceIndNames = new ArrayList();
    this.mtmIndNames = new ArrayList();
    
    Iterator localIterator2;
    for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
      localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
      

      DailyIndColl dailyMTMIndicator = new DailyIndColl(factorList, varListType, FactorType.MTM, 
        ValueType.Numerical);
      

      this.dailyMTMIndCollection.put(asset.getAssetName(), dailyMTMIndicator);
    }
    


    for (String scripName : scripList) {
      DailyIndColl dailyPriceIndicator = new DailyIndColl(factorList, varListType, FactorType.Price, 
        ValueType.Numerical);
      this.dailyPriceIndCollection.put(scripName, dailyPriceIndicator);
    }
    

    DailyIndColl Price1DColl = (DailyIndColl)this.dailyPriceIndCollection.get(scripList.get(0));
    DailyIndColl MTM1DColl = 
      (DailyIndColl)this.dailyMTMIndCollection.get(((Asset)((ArrayList)segmentWiseAssetUniverseMap.values().iterator().next()).get(0)).getAssetName());
    
    for (Factor factor : Price1DColl.getFactorList()) {
      this.priceIndNames.add(factor.getName());
    }
    
    for (Factor factor : MTM1DColl.getFactorList()) {
      this.mtmIndNames.add(factor.getName());
    }
  }
  
  private void initDailyData(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap)
  {
    this.dailyDataCollection = new HashMap();
    Iterator localIterator2; for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
      localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
      this.dailyDataCollection.put(asset.getAssetName(), 
        new DailyData(asset.getScrip().scripID, asset.getScrip().segmentName));
    }
  }
  
  private void initCandleData(ArrayList<String> scripList)
  {
    this.candleDataCollection = new HashMap();
    for (String scripName : scripList) {
      String[] scripNameParts = scripName.split(" ");
      this.candleDataCollection.put(scripName, new CandleData(scripNameParts[3], scripNameParts[1]));
    }
  }
  








  private void initwriters(String dataPath, Set<String> segmentSet, String sourcePath, String destPath)
    throws Exception
  {
    this.rInputWriterMap = new HashMap();
    for (String segmentName : segmentSet)
    {
      if (this.rInputWriterMap.get(segmentName) == null)
      {

        CSVWriter inputWriter = new CSVWriter(destPath + "/ML/" + segmentName + " Input.csv", false, ",");
        

        this.rInputWriterMap.put(segmentName, inputWriter);
        

        inputWriter.write("Date,EndDate,Asset, TradeSide");
        
        for (int i = 0; i < this.priceIndNames.size(); i++) {
          inputWriter.write("," + (String)this.priceIndNames.get(i));
        }
        
        for (int i = 0; i < this.mtmIndNames.size(); i++)
          inputWriter.write("," + (String)this.mtmIndNames.get(i));
        inputWriter.write(",MTM\n");
      }
    }
  }
  




  private void dailyUpdate(HashMap<String, ArrayList<Asset>> segmentWiseAssetUniverseMap, ArrayList<String> scripUniverse, Long date, TradeAndMTMDataProcessor stratTradePnL)
    throws IOException
  {
    Iterator localIterator2;
    


    for (Iterator localIterator1 = segmentWiseAssetUniverseMap.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      ArrayList<Asset> segmentAssetUniverse = (ArrayList)localIterator1.next();
      localIterator2 = segmentAssetUniverse.iterator(); continue;Asset asset = (Asset)localIterator2.next();
      String assetName = asset.getAssetName();
      Long prevDate = ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).getPrevDate();
      try {
        ((DailyDataReader)this.dailyReaderCollection.get(asset.getScrip().scripID)).process(date, (CandleData)this.dailyDataCollection.get(assetName), 
          (Double)((TreeMap)stratTradePnL.getDailyMTMMap().get(asset.getAssetName())).get(prevDate));
      }
      catch (Exception e) {
        System.err.println("Error in fetching Daily Data Reader for " + assetName);
        e.printStackTrace();
      }
      
      getDailyMTMIndValues(date, asset.getAssetName());
    }
    
    for (String scripName : scripUniverse) {
      try
      {
        ((DailyDataReader)this.dailyReaderCollection.get(scripName)).process(date, (CandleData)this.candleDataCollection.get(scripName));
      }
      catch (Exception e) {
        System.err.println("Error in fetching Daily Data Reader for " + scripName);
        e.printStackTrace();
      }
    }
    


    getDailyPriceIndValues(date, scripUniverse);
    updateCorrelMatrix(date, scripUniverse);
  }
  
  private void updateCorrelMatrix(Long date, ArrayList<String> scripList)
  {
    this.correlVals.put(date, new HashMap());
    for (int i = 0; i < scripList.size(); i++) {
      String scrip1 = (String)scripList.get(i);
      ((HashMap)this.correlVals.get(date)).put(scrip1, new HashMap());
      ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip1, Double.valueOf(1.0D));
      for (int j = 0; j < i; j++) {
        String scrip2 = (String)scripList.get(j);
        ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).updateCorrel((CandleData)this.candleDataCollection.get(scrip1), 
          (CandleData)this.candleDataCollection.get(scrip2), date);
        Double correlVal = ((Correl)((HashMap)this.correlMap.get(scrip1)).get(scrip2)).getVal();
        ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip1)).put(scrip2, correlVal);
        ((HashMap)((HashMap)this.correlVals.get(date)).get(scrip2)).put(scrip1, correlVal);
      }
    }
  }
  






  private void getDailyMTMIndValues(Long date, String assetName)
    throws IOException
  {
    DailyData[] dailyDataArray = { (DailyData)this.dailyDataCollection.get(assetName) };
    this.dailyMTMIndVars.put(assetName, ((DailyIndColl)this.dailyMTMIndCollection.get(assetName)).process(dailyDataArray, date));
  }
  
  private void getDailyPriceIndValues(Long date, ArrayList<String> scripList) throws IOException
  {
    for (String scripName : scripList) {
      CandleData[] candleDataArray = { (CandleData)this.candleDataCollection.get(scripName) };
      this.dailyPriceIndVars.put(scripName, ((DailyIndColl)this.dailyPriceIndCollection.get(scripName)).process(candleDataArray, date));
    }
  }
  


  private void writeLineInInputFile(CSVWriter rInputWriter, Long date, String tradeEndDate, String assetName, Integer tradeSide, HashMap<String, ArrayList<Double[]>> priceIndVarsMap, HashMap<String, ArrayList<Double[]>> mtmIndVarsMap, String scripName, Double mtm, boolean predictionDateFlag)
  {
    String tradeSideAssetName = "";
    
    if (this.bias)
    {
      if (tradeSide.intValue() == 1) {
        tradeSideAssetName = "LON#" + assetName;
      } else if (tradeSide.intValue() == -1) {
        tradeSideAssetName = "SHO#" + assetName;
      } else {
        tradeSideAssetName = "NON#" + assetName;
      }
    }
    else {
      tradeSideAssetName = assetName;
    }
    
    try
    {
      rInputWriter.write(date.toString());
      rInputWriter.write("," + tradeEndDate);
      rInputWriter.write("," + tradeSideAssetName);
      rInputWriter.write("," + tradeSide);
      
      if (priceIndVarsMap.get(scripName) != null) {
        for (int k = 0; k < ((ArrayList)priceIndVarsMap.get(scripName)).size(); k++) {
          if (this.bias) {
            rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
          }
          else {
            if (!((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0].equals(((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[1]))
            {
              System.err.println("Getting different values in unbiased mode.");
              System.err.println("Check definition of :" + (String)this.priceIndNames.get(k));
              System.exit(0);
            }
            rInputWriter.write("," + ((Double[])((ArrayList)priceIndVarsMap.get(scripName)).get(k))[0]);
          }
        }
      }
      
      if (mtmIndVarsMap.get(assetName) != null) {
        for (int k = 0; k < ((ArrayList)mtmIndVarsMap.get(assetName)).size(); k++) {
          if (this.bias) {
            rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[((1 - tradeSide.intValue()) / 2)]);
          }
          else {
            if (!((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0].equals(((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[1]))
            {
              System.err.println("Getting different values in unbiased mode.");
              System.err.println("Check definition of :" + (String)this.mtmIndNames.get(k));
              System.exit(0);
            }
            rInputWriter.write("," + ((Double[])((ArrayList)mtmIndVarsMap.get(assetName)).get(k))[0]);
          }
        }
      }
      
      rInputWriter.write("," + mtm + "\n");
      rInputWriter.flush();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  





  public HashMap<String, DailyDataReader> getDailyReaderCollection()
  {
    return this.dailyReaderCollection;
  }
  
  public TreeMap<Long, HashMap<String, HashMap<String, Double>>> getCorrelVals() {
    return this.correlVals;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/MLInputFileGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */