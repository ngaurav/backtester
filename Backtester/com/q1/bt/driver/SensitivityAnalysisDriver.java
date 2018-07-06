package com.q1.bt.driver;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import com.q1.math.MathLib;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class SensitivityAnalysisDriver
{
  LinkedHashMap<String, Boolean> tempHashMap = new LinkedHashMap();
  
  LinkedHashMap<String, ArrayList<String>> parameterNameHashmap = new LinkedHashMap();
  LinkedHashMap<String, ArrayList<String>> parameterValueHashMap = new LinkedHashMap();
  
  LinkedHashMap<String, ArrayList<String>> performanceMeasureNameHashmap = new LinkedHashMap();
  LinkedHashMap<String, HashMap<String, ArrayList<String>>> performanceValueHashMap = new LinkedHashMap();
  
  LinkedHashMap<String, LinkedHashMap<Double, ArrayList<Double>>> dataPointsHashMap = new LinkedHashMap();
  
  LinkedHashMap<String, ArrayList<String>> backtestMap = new LinkedHashMap();
  


  LinkedHashMap<Double, ArrayList<Double>> graphHashMap = new LinkedHashMap();
  

  public TreeMap<Double, Double> finalGraphHashMap = new TreeMap();
  


  int ML = 0; int BT = 0;
  
  String backtestOutputFolder;
  
  String sensitivityOutputFolder;
  
  int startFolderIndex;
  int endFolderIndex;
  String parameterValueMapFile;
  String parameterNameMapFile;
  String performanceNameMapFile;
  String performanceValueMapFile;
  LinkedHashSet<String> performanceMeasureSetForST = new LinkedHashSet();
  LinkedHashSet<String> performanceMeasureSetForML = new LinkedHashSet();
  
  LinkedHashSet<String> strategySet;
  LinkedHashSet<String> scripListSet;
  LinkedHashSet<String> assetClassSet;
  LinkedHashSet<String> scripSet;
  LinkedHashSet<String> parameterSet;
  
  public SensitivityAnalysisDriver(String output_Folder, String sensitivityOutputFolder)
  {
    this.backtestOutputFolder = output_Folder;
    this.sensitivityOutputFolder = sensitivityOutputFolder;
    this.startFolderIndex = 1;
    this.endFolderIndex = 0;
    
    this.parameterValueMapFile = (sensitivityOutputFolder + "/ParameterValueMap.csv");
    this.parameterNameMapFile = (sensitivityOutputFolder + "/ParameterNameMap.csv");
    this.performanceNameMapFile = (sensitivityOutputFolder + "/PerformanceNameMap.csv");
    this.performanceValueMapFile = (sensitivityOutputFolder + "/PerformanceValueMap.csv");
    

    try
    {
      new CSVWriter(this.parameterValueMapFile, false, ",").close();
      new CSVWriter(this.parameterNameMapFile, false, ",").close();
      new CSVWriter(this.performanceNameMapFile, false, ",").close();
      new CSVWriter(this.performanceValueMapFile, false, ",").close();
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    

    this.performanceMeasureSetForST.add("BT");
    this.performanceMeasureSetForML.add("ML");
  }
  

  public SensitivityAnalysisDriver(String output_folder, String sensitivityOutputFolder, int startFolderIndex, int endFolderIndex)
  {
    this.backtestOutputFolder = output_folder;
    this.sensitivityOutputFolder = sensitivityOutputFolder;
    this.startFolderIndex = startFolderIndex;
    this.endFolderIndex = endFolderIndex;
    
    this.parameterValueMapFile = (sensitivityOutputFolder + "/ParameterValueMap.csv");
    this.parameterNameMapFile = (sensitivityOutputFolder + "/ParameterNameMap.csv");
    this.performanceNameMapFile = (sensitivityOutputFolder + "/PerformanceNameMap.csv");
    this.performanceValueMapFile = (sensitivityOutputFolder + "/PerformanceValueMap.csv");
  }
  



  public void generateMaps()
  {
    createParameterNameAndValueMapFile();
    createPerformanceValueMapFile();
    createPerformanceMeasureNameMapFile();
  }
  
  private String getContentOfParameterFile(String fileName, String strategyName)
  {
    String content = "";
    String parameterNameList = "";
    try {
      CSVReader csvReader = new CSVReader(fileName, ',', 0);
      String line; while ((line = csvReader.getLineAsString()) != null) { String line;
        content = content + line.substring(line.indexOf(',') + 1) + ",";
        parameterNameList = parameterNameList + line.substring(0, line.indexOf(',')) + ",";
      }
      if (this.tempHashMap.get(strategyName) == null)
        createParameterNameMapFile(strategyName, strategyName + "," + parameterNameList);
      csvReader.close();
    } catch (IOException io) {
      io.printStackTrace();
    }
    return content;
  }
  
  private void createParameterNameAndValueMapFile()
  {
    int j = 0;
    

    File numberedInitialFolders = new File(this.backtestOutputFolder);
    
    String[] folderList = numberedInitialFolders.list();
    

    File[] files = numberedInitialFolders.listFiles();
    Arrays.sort(files, new FolderComparator());
    int folderCount = 0;
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = files).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
      folderList[folderCount] = f.toString().substring(f.toString().lastIndexOf("\\") + 1);
      folderCount++;
    }
    
    if (this.endFolderIndex == 0) {
      this.endFolderIndex = folderList.length;
    }
    
    try
    {
      CSVWriter csvWriter = new CSVWriter(this.parameterValueMapFile, true, ",");
      

      for (j = this.startFolderIndex - 1; j < this.endFolderIndex; j++) {
        File csvParameterFiles = new File(this.backtestOutputFolder + "/" + folderList[j] + "/Parameters/");
        File[] csvParameterFileList = csvParameterFiles.listFiles();
        File[] arrayOfFile2;
        int m = (arrayOfFile2 = csvParameterFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
          String fileName = file.getName();
          if (fileName.endsWith("Parameters.csv")) {
            String strategyName = fileName.split(" ")[0];
            String singleLine = folderList[j] + "," + strategyName + "," + 
              getContentOfParameterFile(file.getAbsolutePath(), strategyName);
            csvWriter.writeLine(singleLine);
          }
        }
      }
      csvWriter.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
  


  private void createParameterNameMapFile(String strategyName, String parameterList)
  {
    this.tempHashMap.put(strategyName, Boolean.valueOf(true));
    try {
      String[] individualParameterName = parameterList.split(",");
      CSVWriter csvWriter = new CSVWriter(this.sensitivityOutputFolder + "/" + "ParameterNameMap.csv", true, 
        ",");
      csvWriter.writeLine(individualParameterName);
      csvWriter.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  
  private void createPerformanceValueMapFile() {
    try {
      File numberedFolders = new File(this.backtestOutputFolder);
      


      String[] folderList = numberedFolders.list();
      new CSVWriter(this.performanceValueMapFile, false, ",").close();
      

      File[] files = numberedFolders.listFiles();
      Arrays.sort(files, new FolderComparator());
      int folderCount = 0;
      File[] arrayOfFile1;
      int j = (arrayOfFile1 = files).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
        folderList[folderCount] = f.toString().substring(f.toString().lastIndexOf("\\") + 1);
        folderCount++;
      }
      
      int numOfColumns = 0;
      
      if (this.endFolderIndex == 0) {
        this.endFolderIndex = folderList.length;
      }
      for (int i = this.startFolderIndex - 1; i < this.endFolderIndex; i++)
      {

        CSVReader csvReader = new CSVReader(this.backtestOutputFolder + "/" + folderList[i] + "/Results/" + "Performance.csv", ',', 
          0);
        numOfColumns = csvReader.getLine().length - 1;
        int countRows = 0;
        while ((singleLine1 = csvReader.getLine()) != null) { String[] singleLine1;
          countRows++;
        }
        String[][] performanceValueStore = new String[numOfColumns][countRows + 2];
        csvReader.close();
        
        csvReader = new CSVReader(this.backtestOutputFolder + "/" + folderList[i] + "/Results/" + "Performance.csv", ',', 
          0);
        int columnIndex = 2;
        

        for (int numCol = 0; numCol < numOfColumns; numCol++) {
          performanceValueStore[numCol][0] = folderList[i];
        }
        

        String[] singleLine1 = csvReader.getLine();
        for (int k1 = 1; k1 <= numOfColumns; k1++) {
          performanceValueStore[(k1 - 1)][1] = singleLine1[k1];
        }
        

        while ((singleLine1 = csvReader.getLine()) != null)
        {
          if (folderList[i].charAt(0) != 'M') this.performanceMeasureSetForST.add(singleLine1[0]); else {
            this.performanceMeasureSetForML.add(singleLine1[0]);
          }
          for (int k1 = 1; k1 <= numOfColumns; k1++) {
            performanceValueStore[(k1 - 1)][columnIndex] = singleLine1[k1];
          }
          columnIndex++;
        }
        csvReader.close();
        

        CSVWriter csvWriter1 = new CSVWriter(this.performanceValueMapFile, true, ",");
        
        for (int jk = 0; jk < numOfColumns; jk++) {
          csvWriter1.writeLine(performanceValueStore[jk]);
        }
        csvWriter1.close();
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  
  private void createPerformanceMeasureNameMapFile()
  {
    try {
      String[] performanceMeasureSetforBTString = new String[this.performanceMeasureSetForST.size()];
      int i = 0;
      
      for (String perf : this.performanceMeasureSetForST)
      {
        performanceMeasureSetforBTString[i] = perf;
        i++;
      }
      i = 0;
      

      String[] performanceMeasureSetforMLString = new String[this.performanceMeasureSetForML.size()];
      
      for (String perf : this.performanceMeasureSetForML)
      {
        performanceMeasureSetforMLString[i] = perf;
        i++;
      }
      









      CSVWriter csvWriter = new CSVWriter(this.performanceNameMapFile, true, ",");
      
      if (performanceMeasureSetforBTString.length > 1)
        csvWriter.writeLine(performanceMeasureSetforBTString);
      if (performanceMeasureSetforMLString.length > 1)
        csvWriter.writeLine(performanceMeasureSetforMLString);
      csvWriter.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  














  public void createHashmaps()
  {
    createParameterNameHashMap();
    createParameterValueHashMap();
    

    createPerformanceNameHashMap();
    createPerformanceValueHashmap(this.performanceValueMapFile);
  }
  






  private void createParameterNameHashMap()
  {
    ArrayList<String> parameterNameList = null;
    try
    {
      CSVReader csvReader = new CSVReader(this.parameterNameMapFile, ',', 0);
      String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
        parameterNameList = new ArrayList(Arrays.asList(singleLine));
        String keyforParameterNameHashMap = (String)parameterNameList.remove(0);
        this.parameterNameHashmap.put(keyforParameterNameHashMap, parameterNameList);
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  





  public void createParameterValueHashMap()
  {
    try
    {
      CSVReader csvReader = new CSVReader(this.parameterValueMapFile, ',', 0);
      
      String[] singleLine;
      while ((singleLine = csvReader.getLine()) != null) {
        String[] singleLine;
        ArrayList<String> csvSingleLine = new ArrayList(Arrays.asList(singleLine));
        this.parameterValueHashMap.put((String)csvSingleLine.remove(0) + "#" + (String)csvSingleLine.remove(0), csvSingleLine);
      }
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
  }
  





  private void createPerformanceNameHashMap()
  {
    ArrayList<String> performanceNameList = null;
    try
    {
      CSVReader csvReader = new CSVReader(this.performanceNameMapFile, ',', 0);
      String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
        performanceNameList = new ArrayList(Arrays.asList(singleLine));
        String keyForPerfmHashmap = (String)performanceNameList.remove(0);
        this.performanceMeasureNameHashmap.put(keyForPerfmHashmap, performanceNameList);
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  




  public void createPerformanceValueHashmap(String outputValueMapFile)
  {
    ArrayList<String> outputValues = null;
    try
    {
      CSVReader csvReader = new CSVReader(outputValueMapFile, ',', 0);
      String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
        outputValues = new ArrayList(Arrays.asList(singleLine));
        String folderNo = (String)outputValues.remove(0);
        String assetKey = (String)outputValues.remove(0);
        
        HashMap<String, ArrayList<String>> innerMap = (HashMap)this.performanceValueHashMap.get(folderNo);
        if (innerMap == null)
        {
          innerMap = new HashMap();
        }
        
        innerMap.put(assetKey, outputValues);
        this.performanceValueHashMap.put(folderNo, innerMap);
      }
    }
    catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
  













  public LinkedHashSet<String> fetchStrategySet()
  {
    this.strategySet = new LinkedHashSet();
    this.strategySet.add("All");
    
    for (String key : this.parameterNameHashmap.keySet()) {
      this.strategySet.add(key);
    }
    return this.strategySet;
  }
  





  public LinkedHashSet<String> fetchScripListSet(String strategy)
  {
    LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
    
    if (strategy.equals("All"))
    {
      iterableStrategySet = new LinkedHashSet(this.strategySet);
      iterableStrategySet.remove("All");
    }
    else {
      iterableStrategySet.add(strategy);
    }
    this.scripListSet = new LinkedHashSet();
    this.scripListSet.add("All");
    
    Iterator localIterator2;
    for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
        


        localIterator2.hasNext())
    {
      String folderNo = (String)localIterator1.next();
      
      HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
      
      localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
      
      String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
      
      if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && (!strategyScripListScripNameParts[1].equals("All")))
      {
        this.scripListSet.add(strategyScripListScripNameParts[1]);
      }
    }
    



    return this.scripListSet;
  }
  






  public LinkedHashSet<String> fetchAssetClassSet(String strategy, String scripList)
  {
    this.assetClassSet = new LinkedHashSet();
    this.assetClassSet.add("All");
    
    LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
    LinkedHashSet<String> iterableScripListSet = new LinkedHashSet();
    
    if (strategy.equals("All"))
    {
      iterableStrategySet = new LinkedHashSet(this.strategySet);
      iterableStrategySet.remove("All");
    }
    else {
      iterableStrategySet.add(strategy);
    }
    if (scripList.equals("All"))
    {
      iterableScripListSet = new LinkedHashSet(this.scripListSet);
      iterableScripListSet.remove("All");
    }
    else {
      iterableScripListSet.add(scripList); }
    Iterator localIterator2;
    for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
        


        localIterator2.hasNext())
    {
      String folderNo = (String)localIterator1.next();
      
      HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
      
      localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
      String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
      
      if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && 
        (iterableScripListSet.contains(strategyScripListScripNameParts[1])) && 
        (!strategyScripListScripNameParts[2].equals("All"))) {
        this.assetClassSet.add(strategyScripListScripNameParts[2].split(" ")[1]);
      }
    }
    

    return this.assetClassSet;
  }
  







  public LinkedHashSet<String> fetchScripSet(String strategy, String scripList, String assetClass)
  {
    this.scripSet = new LinkedHashSet();
    this.scripSet.add("All");
    
    LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
    LinkedHashSet<String> iterableScripListSet = new LinkedHashSet();
    LinkedHashSet<String> iterableAssetClassSet = new LinkedHashSet();
    
    if (strategy.equals("All"))
    {
      iterableStrategySet = new LinkedHashSet(this.strategySet);
      iterableStrategySet.remove("All");
    }
    else {
      iterableStrategySet.add(strategy);
    }
    if (scripList.equals("All"))
    {
      iterableScripListSet = new LinkedHashSet(this.scripListSet);
      iterableScripListSet.remove("All");
    }
    else {
      iterableScripListSet.add(scripList);
    }
    if (assetClass.equals("All"))
    {
      iterableAssetClassSet = new LinkedHashSet(this.assetClassSet);
      iterableAssetClassSet.remove("All");
    }
    else {
      iterableAssetClassSet.add(assetClass); }
    Iterator localIterator2;
    for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
        

        localIterator2.hasNext())
    {
      String folderNo = (String)localIterator1.next();
      HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
      
      localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
      String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
      
      if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && 
        (iterableScripListSet.contains(strategyScripListScripNameParts[1])) && 
        (!strategyScripListScripNameParts[2].equals("All")) && 
        (iterableAssetClassSet.contains(strategyScripListScripNameParts[2].split(" ")[1]))) {
        this.scripSet.add(strategyScripListScripNameParts[2]);
      }
    }
    

    return this.scripSet;
  }
  








  public LinkedHashSet<String> fetchParameterSet(String strategy)
  {
    LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
    
    if (strategy.equals("All"))
    {
      iterableStrategySet = new LinkedHashSet(this.strategySet);
      iterableStrategySet.remove("All");
    }
    else {
      iterableStrategySet.add(strategy);
    }
    this.parameterSet = new LinkedHashSet();
    
    for (Map.Entry<String, ArrayList<String>> entry : this.parameterNameHashmap.entrySet())
    {
      if (iterableStrategySet.contains(entry.getKey()))
      {
        if (this.parameterSet.isEmpty()) {
          this.parameterSet.addAll((Collection)entry.getValue());
        } else {
          this.parameterSet.retainAll((Collection)entry.getValue());
        }
      }
    }
    
    return this.parameterSet;
  }
  
  public ArrayList<String> fetchPerformanceMeasureNameSet()
  {
    ArrayList<String> performanceMeasureName = new ArrayList();
    
    for (Map.Entry<String, ArrayList<String>> entry : this.performanceMeasureNameHashmap.entrySet())
    {

      performanceMeasureName = (ArrayList)entry.getValue();
    }
    
    return performanceMeasureName;
  }
  
  public ArrayList<String> fetchChartTypeSet()
  {
    ArrayList<String> chartTypeList = new ArrayList();
    chartTypeList.add("Line");
    chartTypeList.add("Scatter");
    
    return chartTypeList;
  }
  



  public double[][] getDataSet(String strategyName, String scripList, String assetClass, String scrip, int parameterIndex, int performanceIndex)
  {
    HashMap<String, ArrayList<Double>> yAxisDataSet = getYAxisDataSet(strategyName, scripList, assetClass, scrip, performanceIndex);
    
    TreeMap<Double, ArrayList<Double>> mergedDatMap = getMergedDataSet(yAxisDataSet, parameterIndex);
    
    return convertDataMapToDataArray(mergedDatMap);
  }
  
  public double[][] getDataSet(String strategyName, String scripList, String assetClass, String scrip, int performanceIndex)
  {
    HashMap<String, ArrayList<Double>> yAxisDataSet = getYAxisDataSet(strategyName, scripList, assetClass, scrip, performanceIndex);
    
    return get_insample_oos_map(yAxisDataSet);
  }
  
  private double[][] get_insample_oos_map(HashMap<String, ArrayList<Double>> yAxisDataSet)
  {
    int dataLength = this.parameterValueHashMap.keySet().size() / 2;
    double[][] dataArray = new double[2][dataLength];
    
    int i = 0;
    
    Iterator<Map.Entry<String, ArrayList<String>>> it = this.parameterValueHashMap.entrySet().iterator();
    while (it.hasNext())
    {
      Map.Entry<String, ArrayList<String>> entry = (Map.Entry)it.next();
      ArrayList<Double> valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
      double averageYVal = MathLib.sumDouble(valueList) / valueList.size();
      dataArray[0][i] = MathLib.round(averageYVal, 2);
      
      entry = (Map.Entry)it.next();
      valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
      averageYVal = MathLib.sumDouble(valueList) / valueList.size();
      dataArray[1][i] = MathLib.round(averageYVal, 2);
      
      i++;
    }
    
    return dataArray;
  }
  
  private TreeMap<Double, ArrayList<Double>> getMergedDataSet(HashMap<String, ArrayList<Double>> yAxisDataSet, int parameterIndex)
  {
    TreeMap<Double, ArrayList<Double>> mergedDataMap = new TreeMap();
    
    for (Map.Entry<String, ArrayList<String>> entry : this.parameterValueHashMap.entrySet())
    {
      double parameter = Double.parseDouble((String)((ArrayList)entry.getValue()).get(parameterIndex));
      ArrayList<Double> valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
      
      ArrayList<Double> existingValueList = (ArrayList)mergedDataMap.get(Double.valueOf(parameter));
      
      if (existingValueList == null)
      {
        existingValueList = new ArrayList();
      }
      

      existingValueList.addAll(valueList);
      
      mergedDataMap.put(Double.valueOf(parameter), existingValueList);
    }
    
    return mergedDataMap;
  }
  
  private double[][] convertDataMapToDataArray(TreeMap<Double, ArrayList<Double>> mergedDatMap)
  {
    int dataLength = mergedDatMap.keySet().size();
    double[][] dataArray = new double[2][dataLength];
    
    int i = 0;
    
    for (Map.Entry<Double, ArrayList<Double>> entry : mergedDatMap.entrySet())
    {
      ArrayList<Double> yValueArray = (ArrayList)entry.getValue();
      double averageYVal = MathLib.sumDouble(yValueArray) / yValueArray.size();
      
      dataArray[0][i] = MathLib.round(((Double)entry.getKey()).doubleValue(), 2);
      dataArray[1][i] = MathLib.round(averageYVal, 2);
      
      i++;
    }
    
    return dataArray;
  }
  
  public HashMap<String, ArrayList<Double>> getYAxisDataSet(String strategyName, String scripList, String assetClass, String scrip, int performanceNameIndex)
  {
    HashMap<String, ArrayList<Double>> yAxisValueHashMap = new HashMap();
    
    LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
    
    if (strategyName.equals("All"))
    {
      iterableStrategySet = new LinkedHashSet(this.strategySet);
      iterableStrategySet.remove("All");
    }
    else
    {
      iterableStrategySet.add(strategyName);
    }
    Iterator localIterator2;
    for (Iterator localIterator1 = this.performanceValueHashMap.entrySet().iterator(); localIterator1.hasNext(); 
        


        localIterator2.hasNext())
    {
      Map.Entry<String, HashMap<String, ArrayList<String>>> entry = (Map.Entry)localIterator1.next();
      
      HashMap<String, ArrayList<String>> assetValueMap = (HashMap)entry.getValue();
      
      localIterator2 = iterableStrategySet.iterator(); continue;String iterableStrategyName = (String)localIterator2.next();
      
      ArrayList<Double> valueList = new ArrayList();
      
      if ((scripList.equals("All")) && (!scrip.equals("All")))
      {
        for (String key : assetValueMap.keySet())
        {
          String[] keyParts = key.split("\\|");
          if ((keyParts[0].equals(iterableStrategyName)) && (!keyParts[1].equals("All")) && (keyParts[2].equals(scrip)))
          {
            Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
            valueList.add(fetchValue);
          }
        }
      }
      else if (assetClass.equals("All"))
      {

        String fetchKey = iterableStrategyName + "|" + scripList + "|" + scrip;
        Object fetchValueArray = (ArrayList)assetValueMap.get(fetchKey);
        
        if (fetchValueArray != null)
        {
          Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)fetchValueArray).get(performanceNameIndex)));
          valueList.add(fetchValue);
        }
      } else {
        Object fetchValueArray;
        if ((!scrip.equals("All")) && (!scripList.equals("All")))
        {
          String fetchKey = iterableStrategyName + "|" + scripList + "|" + scrip;
          fetchValueArray = (ArrayList)assetValueMap.get(fetchKey);
          
          if (fetchValueArray != null)
          {
            Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)fetchValueArray).get(performanceNameIndex)));
            valueList.add(fetchValue);
          }
        }
        else if ((scrip.equals("All")) && (!scripList.equals("All"))) {
          for (fetchValueArray = assetValueMap.keySet().iterator(); ((Iterator)fetchValueArray).hasNext();) { String key = (String)((Iterator)fetchValueArray).next();
            
            String[] keyParts = key.split("\\|");
            if ((keyParts[0].equals(iterableStrategyName)) && (keyParts[1].equals(scripList)) && (!keyParts[2].equals("All")) && (keyParts[2].split(" ")[1].equals(assetClass))) {
              Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
              valueList.add(fetchValue);
            }
          }
        } else if ((scrip.equals("All")) && (scripList.equals("All"))) {
          for (fetchValueArray = assetValueMap.keySet().iterator(); ((Iterator)fetchValueArray).hasNext();) { String key = (String)((Iterator)fetchValueArray).next();
            
            String[] keyParts = key.split("\\|");
            if ((keyParts[0].equals(iterableStrategyName)) && (!keyParts[1].equals("All")) && (!keyParts[2].equals("All")) && (keyParts[2].split(" ")[1].equals(assetClass))) {
              Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
              valueList.add(fetchValue);
            }
          }
        }
      }
      
      if (valueList.size() > 0) {
        yAxisValueHashMap.put((String)entry.getKey() + "#" + iterableStrategyName, valueList);
      }
    }
    

    return yAxisValueHashMap;
  }
  

  public ArrayList<Double> getParameterValueListWrapperFunction(String strategy, String parameterName)
  {
    this.graphHashMap.clear();
    this.finalGraphHashMap.clear();
    if (strategy.compareTo("All") == 0)
      return getParameterValueList(parameterName);
    return getParameterValueList(strategy, parameterName);
  }
  








  private ArrayList<Double> getParameterValueList(String strategy, String parameterName)
  {
    ArrayList<Double> parameterValueListasDouble = new ArrayList();
    ArrayList<String> keySet = new ArrayList(this.parameterValueHashMap.keySet());
    

    for (int i = 0; i < keySet.size(); i++) {
      String key = (String)keySet.get(i);
      if (key.substring(key.indexOf("#") + 1).compareTo(strategy) == 0)
      {
        if (!parameterValueListasDouble.contains(Double.valueOf(Double.parseDouble((String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))))) {
          parameterValueListasDouble.add(Double.valueOf(Double.parseDouble(
            (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))));
          this.graphHashMap.put(
            Double.valueOf(Double.parseDouble(
            (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))), 
            new ArrayList());
        } }
    }
    return parameterValueListasDouble;
  }
  

  private ArrayList<Double> getParameterValueList(String parameterName)
  {
    ArrayList<Double> parameterValueListasDouble = new ArrayList();
    ArrayList<String> keySet = new ArrayList(this.parameterValueHashMap.keySet());
    

    for (int i = 0; i < keySet.size(); i++) {
      String key = (String)keySet.get(i);
      
      if (!parameterValueListasDouble.contains(Double.valueOf(Double.parseDouble((String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))))) {
        parameterValueListasDouble.add(Double.valueOf(Double.parseDouble(
          (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))));
        this.graphHashMap.put(
          Double.valueOf(Double.parseDouble(
          (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))), 
          new ArrayList());
      }
    }
    
    return parameterValueListasDouble;
  }
  







  public void coordinateProcessing()
  {
    ArrayList<Double> parameterKeySet = new ArrayList(this.graphHashMap.keySet());
    Double performanceValueSum = Double.valueOf(0.0D);
    for (int i = 0; i < parameterKeySet.size(); i++) {
      for (Double performanceValue : (ArrayList)this.graphHashMap.get(parameterKeySet.get(i))) {
        performanceValueSum = Double.valueOf(performanceValueSum.doubleValue() + performanceValue.doubleValue());
      }
      this.finalGraphHashMap.put((Double)parameterKeySet.get(i), 
        Double.valueOf(performanceValueSum.doubleValue() / ((ArrayList)this.graphHashMap.get(parameterKeySet.get(i))).size()));
      performanceValueSum = Double.valueOf(0.0D);
    }
  }
  
  private double convertToDouble(String str)
  {
    if (str.endsWith("%")) {
      str = str.substring(0, str.length() - 1);
    } else if (str.contains(" ")) {
      str = str.substring(0, str.indexOf(" "));
    }
    double value = Double.parseDouble(str);
    
    return value;
  }
  
  class FolderComparator implements java.util.Comparator<File> {
    FolderComparator() {}
    
    public int compare(File f1, File f2) { return Long.valueOf(f1.lastModified()).compareTo(Long.valueOf(f2.lastModified())); }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/SensitivityAnalysisDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */