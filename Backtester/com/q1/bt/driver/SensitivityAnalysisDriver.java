/*     */ package com.q1.bt.driver;
/*     */ 
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class SensitivityAnalysisDriver
/*     */ {
/*  21 */   LinkedHashMap<String, Boolean> tempHashMap = new LinkedHashMap();
/*     */   
/*  23 */   LinkedHashMap<String, ArrayList<String>> parameterNameHashmap = new LinkedHashMap();
/*  24 */   LinkedHashMap<String, ArrayList<String>> parameterValueHashMap = new LinkedHashMap();
/*     */   
/*  26 */   LinkedHashMap<String, ArrayList<String>> performanceMeasureNameHashmap = new LinkedHashMap();
/*  27 */   LinkedHashMap<String, HashMap<String, ArrayList<String>>> performanceValueHashMap = new LinkedHashMap();
/*     */   
/*  29 */   LinkedHashMap<String, LinkedHashMap<Double, ArrayList<Double>>> dataPointsHashMap = new LinkedHashMap();
/*     */   
/*  31 */   LinkedHashMap<String, ArrayList<String>> backtestMap = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*  35 */   LinkedHashMap<Double, ArrayList<Double>> graphHashMap = new LinkedHashMap();
/*     */   
/*     */ 
/*  38 */   public TreeMap<Double, Double> finalGraphHashMap = new TreeMap();
/*     */   
/*     */ 
/*     */ 
/*  42 */   int ML = 0; int BT = 0;
/*     */   
/*     */   String backtestOutputFolder;
/*     */   
/*     */   String sensitivityOutputFolder;
/*     */   
/*     */   int startFolderIndex;
/*     */   int endFolderIndex;
/*     */   String parameterValueMapFile;
/*     */   String parameterNameMapFile;
/*     */   String performanceNameMapFile;
/*     */   String performanceValueMapFile;
/*  54 */   LinkedHashSet<String> performanceMeasureSetForST = new LinkedHashSet();
/*  55 */   LinkedHashSet<String> performanceMeasureSetForML = new LinkedHashSet();
/*     */   
/*     */   LinkedHashSet<String> strategySet;
/*     */   LinkedHashSet<String> scripListSet;
/*     */   LinkedHashSet<String> assetClassSet;
/*     */   LinkedHashSet<String> scripSet;
/*     */   LinkedHashSet<String> parameterSet;
/*     */   
/*     */   public SensitivityAnalysisDriver(String output_Folder, String sensitivityOutputFolder)
/*     */   {
/*  65 */     this.backtestOutputFolder = output_Folder;
/*  66 */     this.sensitivityOutputFolder = sensitivityOutputFolder;
/*  67 */     this.startFolderIndex = 1;
/*  68 */     this.endFolderIndex = 0;
/*     */     
/*  70 */     this.parameterValueMapFile = (sensitivityOutputFolder + "/ParameterValueMap.csv");
/*  71 */     this.parameterNameMapFile = (sensitivityOutputFolder + "/ParameterNameMap.csv");
/*  72 */     this.performanceNameMapFile = (sensitivityOutputFolder + "/PerformanceNameMap.csv");
/*  73 */     this.performanceValueMapFile = (sensitivityOutputFolder + "/PerformanceValueMap.csv");
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  78 */       new CSVWriter(this.parameterValueMapFile, false, ",").close();
/*  79 */       new CSVWriter(this.parameterNameMapFile, false, ",").close();
/*  80 */       new CSVWriter(this.performanceNameMapFile, false, ",").close();
/*  81 */       new CSVWriter(this.performanceValueMapFile, false, ",").close();
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/*  85 */       ioe.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*  89 */     this.performanceMeasureSetForST.add("BT");
/*  90 */     this.performanceMeasureSetForML.add("ML");
/*     */   }
/*     */   
/*     */ 
/*     */   public SensitivityAnalysisDriver(String output_folder, String sensitivityOutputFolder, int startFolderIndex, int endFolderIndex)
/*     */   {
/*  96 */     this.backtestOutputFolder = output_folder;
/*  97 */     this.sensitivityOutputFolder = sensitivityOutputFolder;
/*  98 */     this.startFolderIndex = startFolderIndex;
/*  99 */     this.endFolderIndex = endFolderIndex;
/*     */     
/* 101 */     this.parameterValueMapFile = (sensitivityOutputFolder + "/ParameterValueMap.csv");
/* 102 */     this.parameterNameMapFile = (sensitivityOutputFolder + "/ParameterNameMap.csv");
/* 103 */     this.performanceNameMapFile = (sensitivityOutputFolder + "/PerformanceNameMap.csv");
/* 104 */     this.performanceValueMapFile = (sensitivityOutputFolder + "/PerformanceValueMap.csv");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void generateMaps()
/*     */   {
/* 112 */     createParameterNameAndValueMapFile();
/* 113 */     createPerformanceValueMapFile();
/* 114 */     createPerformanceMeasureNameMapFile();
/*     */   }
/*     */   
/*     */   private String getContentOfParameterFile(String fileName, String strategyName)
/*     */   {
/* 119 */     String content = "";
/* 120 */     String parameterNameList = "";
/*     */     try {
/* 122 */       CSVReader csvReader = new CSVReader(fileName, ',', 0);
/* 123 */       String line; while ((line = csvReader.getLineAsString()) != null) { String line;
/* 124 */         content = content + line.substring(line.indexOf(',') + 1) + ",";
/* 125 */         parameterNameList = parameterNameList + line.substring(0, line.indexOf(',')) + ",";
/*     */       }
/* 127 */       if (this.tempHashMap.get(strategyName) == null)
/* 128 */         createParameterNameMapFile(strategyName, strategyName + "," + parameterNameList);
/* 129 */       csvReader.close();
/*     */     } catch (IOException io) {
/* 131 */       io.printStackTrace();
/*     */     }
/* 133 */     return content;
/*     */   }
/*     */   
/*     */   private void createParameterNameAndValueMapFile()
/*     */   {
/* 138 */     int j = 0;
/*     */     
/*     */ 
/* 141 */     File numberedInitialFolders = new File(this.backtestOutputFolder);
/*     */     
/* 143 */     String[] folderList = numberedInitialFolders.list();
/*     */     
/*     */ 
/* 146 */     File[] files = numberedInitialFolders.listFiles();
/* 147 */     Arrays.sort(files, new FolderComparator());
/* 148 */     int folderCount = 0;
/*     */     
/*     */     File[] arrayOfFile1;
/* 151 */     int j = (arrayOfFile1 = files).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
/* 152 */       folderList[folderCount] = f.toString().substring(f.toString().lastIndexOf("\\") + 1);
/* 153 */       folderCount++;
/*     */     }
/*     */     
/* 156 */     if (this.endFolderIndex == 0) {
/* 157 */       this.endFolderIndex = folderList.length;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 162 */       CSVWriter csvWriter = new CSVWriter(this.parameterValueMapFile, true, ",");
/*     */       
/*     */ 
/* 165 */       for (j = this.startFolderIndex - 1; j < this.endFolderIndex; j++) {
/* 166 */         File csvParameterFiles = new File(this.backtestOutputFolder + "/" + folderList[j] + "/Parameters/");
/* 167 */         File[] csvParameterFileList = csvParameterFiles.listFiles();
/*     */         File[] arrayOfFile2;
/* 169 */         int m = (arrayOfFile2 = csvParameterFileList).length; for (int k = 0; k < m; k++) { File file = arrayOfFile2[k];
/* 170 */           String fileName = file.getName();
/* 171 */           if (fileName.endsWith("Parameters.csv")) {
/* 172 */             String strategyName = fileName.split(" ")[0];
/* 173 */             String singleLine = folderList[j] + "," + strategyName + "," + 
/* 174 */               getContentOfParameterFile(file.getAbsolutePath(), strategyName);
/* 175 */             csvWriter.writeLine(singleLine);
/*     */           }
/*     */         }
/*     */       }
/* 179 */       csvWriter.close();
/*     */     }
/*     */     catch (IOException e) {
/* 182 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void createParameterNameMapFile(String strategyName, String parameterList)
/*     */   {
/* 190 */     this.tempHashMap.put(strategyName, Boolean.valueOf(true));
/*     */     try {
/* 192 */       String[] individualParameterName = parameterList.split(",");
/* 193 */       CSVWriter csvWriter = new CSVWriter(this.sensitivityOutputFolder + "/" + "ParameterNameMap.csv", true, 
/* 194 */         ",");
/* 195 */       csvWriter.writeLine(individualParameterName);
/* 196 */       csvWriter.close();
/*     */     } catch (IOException ioe) {
/* 198 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void createPerformanceValueMapFile() {
/*     */     try {
/* 204 */       File numberedFolders = new File(this.backtestOutputFolder);
/*     */       
/*     */ 
/*     */ 
/* 208 */       String[] folderList = numberedFolders.list();
/* 209 */       new CSVWriter(this.performanceValueMapFile, false, ",").close();
/*     */       
/*     */ 
/* 212 */       File[] files = numberedFolders.listFiles();
/* 213 */       Arrays.sort(files, new FolderComparator());
/* 214 */       int folderCount = 0;
/*     */       File[] arrayOfFile1;
/* 216 */       int j = (arrayOfFile1 = files).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
/* 217 */         folderList[folderCount] = f.toString().substring(f.toString().lastIndexOf("\\") + 1);
/* 218 */         folderCount++;
/*     */       }
/*     */       
/* 221 */       int numOfColumns = 0;
/*     */       
/* 223 */       if (this.endFolderIndex == 0) {
/* 224 */         this.endFolderIndex = folderList.length;
/*     */       }
/* 226 */       for (int i = this.startFolderIndex - 1; i < this.endFolderIndex; i++)
/*     */       {
/*     */ 
/* 229 */         CSVReader csvReader = new CSVReader(this.backtestOutputFolder + "/" + folderList[i] + "/Results/" + "Performance.csv", ',', 
/* 230 */           0);
/* 231 */         numOfColumns = csvReader.getLine().length - 1;
/* 232 */         int countRows = 0;
/* 233 */         while ((singleLine1 = csvReader.getLine()) != null) { String[] singleLine1;
/* 234 */           countRows++;
/*     */         }
/* 236 */         String[][] performanceValueStore = new String[numOfColumns][countRows + 2];
/* 237 */         csvReader.close();
/*     */         
/* 239 */         csvReader = new CSVReader(this.backtestOutputFolder + "/" + folderList[i] + "/Results/" + "Performance.csv", ',', 
/* 240 */           0);
/* 241 */         int columnIndex = 2;
/*     */         
/*     */ 
/* 244 */         for (int numCol = 0; numCol < numOfColumns; numCol++) {
/* 245 */           performanceValueStore[numCol][0] = folderList[i];
/*     */         }
/*     */         
/*     */ 
/* 249 */         String[] singleLine1 = csvReader.getLine();
/* 250 */         for (int k1 = 1; k1 <= numOfColumns; k1++) {
/* 251 */           performanceValueStore[(k1 - 1)][1] = singleLine1[k1];
/*     */         }
/*     */         
/*     */ 
/* 255 */         while ((singleLine1 = csvReader.getLine()) != null)
/*     */         {
/* 257 */           if (folderList[i].charAt(0) != 'M') this.performanceMeasureSetForST.add(singleLine1[0]); else {
/* 258 */             this.performanceMeasureSetForML.add(singleLine1[0]);
/*     */           }
/* 260 */           for (int k1 = 1; k1 <= numOfColumns; k1++) {
/* 261 */             performanceValueStore[(k1 - 1)][columnIndex] = singleLine1[k1];
/*     */           }
/* 263 */           columnIndex++;
/*     */         }
/* 265 */         csvReader.close();
/*     */         
/*     */ 
/* 268 */         CSVWriter csvWriter1 = new CSVWriter(this.performanceValueMapFile, true, ",");
/*     */         
/* 270 */         for (int jk = 0; jk < numOfColumns; jk++) {
/* 271 */           csvWriter1.writeLine(performanceValueStore[jk]);
/*     */         }
/* 273 */         csvWriter1.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ioe) {
/* 277 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void createPerformanceMeasureNameMapFile()
/*     */   {
/*     */     try {
/* 284 */       String[] performanceMeasureSetforBTString = new String[this.performanceMeasureSetForST.size()];
/* 285 */       int i = 0;
/*     */       
/* 287 */       for (String perf : this.performanceMeasureSetForST)
/*     */       {
/* 289 */         performanceMeasureSetforBTString[i] = perf;
/* 290 */         i++;
/*     */       }
/* 292 */       i = 0;
/*     */       
/*     */ 
/* 295 */       String[] performanceMeasureSetforMLString = new String[this.performanceMeasureSetForML.size()];
/*     */       
/* 297 */       for (String perf : this.performanceMeasureSetForML)
/*     */       {
/* 299 */         performanceMeasureSetforMLString[i] = perf;
/* 300 */         i++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 312 */       CSVWriter csvWriter = new CSVWriter(this.performanceNameMapFile, true, ",");
/*     */       
/* 314 */       if (performanceMeasureSetforBTString.length > 1)
/* 315 */         csvWriter.writeLine(performanceMeasureSetforBTString);
/* 316 */       if (performanceMeasureSetforMLString.length > 1)
/* 317 */         csvWriter.writeLine(performanceMeasureSetforMLString);
/* 318 */       csvWriter.close();
/*     */     } catch (IOException ioe) {
/* 320 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createHashmaps()
/*     */   {
/* 340 */     createParameterNameHashMap();
/* 341 */     createParameterValueHashMap();
/*     */     
/*     */ 
/* 344 */     createPerformanceNameHashMap();
/* 345 */     createPerformanceValueHashmap(this.performanceValueMapFile);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createParameterNameHashMap()
/*     */   {
/* 356 */     ArrayList<String> parameterNameList = null;
/*     */     try
/*     */     {
/* 359 */       CSVReader csvReader = new CSVReader(this.parameterNameMapFile, ',', 0);
/* 360 */       String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
/* 361 */         parameterNameList = new ArrayList(Arrays.asList(singleLine));
/* 362 */         String keyforParameterNameHashMap = (String)parameterNameList.remove(0);
/* 363 */         this.parameterNameHashmap.put(keyforParameterNameHashMap, parameterNameList);
/*     */       }
/*     */     } catch (IOException ioe) {
/* 366 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createParameterValueHashMap()
/*     */   {
/*     */     try
/*     */     {
/* 379 */       CSVReader csvReader = new CSVReader(this.parameterValueMapFile, ',', 0);
/*     */       
/*     */       String[] singleLine;
/* 382 */       while ((singleLine = csvReader.getLine()) != null) {
/*     */         String[] singleLine;
/* 384 */         ArrayList<String> csvSingleLine = new ArrayList(Arrays.asList(singleLine));
/* 385 */         this.parameterValueHashMap.put((String)csvSingleLine.remove(0) + "#" + (String)csvSingleLine.remove(0), csvSingleLine);
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 390 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void createPerformanceNameHashMap()
/*     */   {
/* 401 */     ArrayList<String> performanceNameList = null;
/*     */     try
/*     */     {
/* 404 */       CSVReader csvReader = new CSVReader(this.performanceNameMapFile, ',', 0);
/* 405 */       String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
/* 406 */         performanceNameList = new ArrayList(Arrays.asList(singleLine));
/* 407 */         String keyForPerfmHashmap = (String)performanceNameList.remove(0);
/* 408 */         this.performanceMeasureNameHashmap.put(keyForPerfmHashmap, performanceNameList);
/*     */       }
/*     */     } catch (IOException ioe) {
/* 411 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void createPerformanceValueHashmap(String outputValueMapFile)
/*     */   {
/* 421 */     ArrayList<String> outputValues = null;
/*     */     try
/*     */     {
/* 424 */       CSVReader csvReader = new CSVReader(outputValueMapFile, ',', 0);
/* 425 */       String[] singleLine; while ((singleLine = csvReader.getLine()) != null) { String[] singleLine;
/* 426 */         outputValues = new ArrayList(Arrays.asList(singleLine));
/* 427 */         String folderNo = (String)outputValues.remove(0);
/* 428 */         String assetKey = (String)outputValues.remove(0);
/*     */         
/* 430 */         HashMap<String, ArrayList<String>> innerMap = (HashMap)this.performanceValueHashMap.get(folderNo);
/* 431 */         if (innerMap == null)
/*     */         {
/* 433 */           innerMap = new HashMap();
/*     */         }
/*     */         
/* 436 */         innerMap.put(assetKey, outputValues);
/* 437 */         this.performanceValueHashMap.put(folderNo, innerMap);
/*     */       }
/*     */     }
/*     */     catch (IOException ioe) {
/* 441 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashSet<String> fetchStrategySet()
/*     */   {
/* 460 */     this.strategySet = new LinkedHashSet();
/* 461 */     this.strategySet.add("All");
/*     */     
/* 463 */     for (String key : this.parameterNameHashmap.keySet()) {
/* 464 */       this.strategySet.add(key);
/*     */     }
/* 466 */     return this.strategySet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashSet<String> fetchScripListSet(String strategy)
/*     */   {
/* 476 */     LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
/*     */     
/* 478 */     if (strategy.equals("All"))
/*     */     {
/* 480 */       iterableStrategySet = new LinkedHashSet(this.strategySet);
/* 481 */       iterableStrategySet.remove("All");
/*     */     }
/*     */     else {
/* 484 */       iterableStrategySet.add(strategy);
/*     */     }
/* 486 */     this.scripListSet = new LinkedHashSet();
/* 487 */     this.scripListSet.add("All");
/*     */     
/*     */     Iterator localIterator2;
/* 490 */     for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 494 */         localIterator2.hasNext())
/*     */     {
/* 490 */       String folderNo = (String)localIterator1.next();
/*     */       
/* 492 */       HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
/*     */       
/* 494 */       localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
/*     */       
/* 496 */       String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
/*     */       
/* 498 */       if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && (!strategyScripListScripNameParts[1].equals("All")))
/*     */       {
/* 500 */         this.scripListSet.add(strategyScripListScripNameParts[1]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 507 */     return this.scripListSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashSet<String> fetchAssetClassSet(String strategy, String scripList)
/*     */   {
/* 518 */     this.assetClassSet = new LinkedHashSet();
/* 519 */     this.assetClassSet.add("All");
/*     */     
/* 521 */     LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
/* 522 */     LinkedHashSet<String> iterableScripListSet = new LinkedHashSet();
/*     */     
/* 524 */     if (strategy.equals("All"))
/*     */     {
/* 526 */       iterableStrategySet = new LinkedHashSet(this.strategySet);
/* 527 */       iterableStrategySet.remove("All");
/*     */     }
/*     */     else {
/* 530 */       iterableStrategySet.add(strategy);
/*     */     }
/* 532 */     if (scripList.equals("All"))
/*     */     {
/* 534 */       iterableScripListSet = new LinkedHashSet(this.scripListSet);
/* 535 */       iterableScripListSet.remove("All");
/*     */     }
/*     */     else {
/* 538 */       iterableScripListSet.add(scripList); }
/*     */     Iterator localIterator2;
/* 540 */     for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 544 */         localIterator2.hasNext())
/*     */     {
/* 540 */       String folderNo = (String)localIterator1.next();
/*     */       
/* 542 */       HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
/*     */       
/* 544 */       localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
/* 545 */       String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
/*     */       
/* 547 */       if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && 
/* 548 */         (iterableScripListSet.contains(strategyScripListScripNameParts[1])) && 
/* 549 */         (!strategyScripListScripNameParts[2].equals("All"))) {
/* 550 */         this.assetClassSet.add(strategyScripListScripNameParts[2].split(" ")[1]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 555 */     return this.assetClassSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashSet<String> fetchScripSet(String strategy, String scripList, String assetClass)
/*     */   {
/* 567 */     this.scripSet = new LinkedHashSet();
/* 568 */     this.scripSet.add("All");
/*     */     
/* 570 */     LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
/* 571 */     LinkedHashSet<String> iterableScripListSet = new LinkedHashSet();
/* 572 */     LinkedHashSet<String> iterableAssetClassSet = new LinkedHashSet();
/*     */     
/* 574 */     if (strategy.equals("All"))
/*     */     {
/* 576 */       iterableStrategySet = new LinkedHashSet(this.strategySet);
/* 577 */       iterableStrategySet.remove("All");
/*     */     }
/*     */     else {
/* 580 */       iterableStrategySet.add(strategy);
/*     */     }
/* 582 */     if (scripList.equals("All"))
/*     */     {
/* 584 */       iterableScripListSet = new LinkedHashSet(this.scripListSet);
/* 585 */       iterableScripListSet.remove("All");
/*     */     }
/*     */     else {
/* 588 */       iterableScripListSet.add(scripList);
/*     */     }
/* 590 */     if (assetClass.equals("All"))
/*     */     {
/* 592 */       iterableAssetClassSet = new LinkedHashSet(this.assetClassSet);
/* 593 */       iterableAssetClassSet.remove("All");
/*     */     }
/*     */     else {
/* 596 */       iterableAssetClassSet.add(assetClass); }
/*     */     Iterator localIterator2;
/* 598 */     for (Iterator localIterator1 = this.performanceValueHashMap.keySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 601 */         localIterator2.hasNext())
/*     */     {
/* 598 */       String folderNo = (String)localIterator1.next();
/* 599 */       HashMap<String, ArrayList<String>> assetValueMap = (HashMap)this.performanceValueHashMap.get(folderNo);
/*     */       
/* 601 */       localIterator2 = assetValueMap.keySet().iterator(); continue;String strategyScripListScripName = (String)localIterator2.next();
/* 602 */       String[] strategyScripListScripNameParts = strategyScripListScripName.split("\\|");
/*     */       
/* 604 */       if ((iterableStrategySet.contains(strategyScripListScripNameParts[0])) && 
/* 605 */         (iterableScripListSet.contains(strategyScripListScripNameParts[1])) && 
/* 606 */         (!strategyScripListScripNameParts[2].equals("All")) && 
/* 607 */         (iterableAssetClassSet.contains(strategyScripListScripNameParts[2].split(" ")[1]))) {
/* 608 */         this.scripSet.add(strategyScripListScripNameParts[2]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 613 */     return this.scripSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinkedHashSet<String> fetchParameterSet(String strategy)
/*     */   {
/* 626 */     LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
/*     */     
/* 628 */     if (strategy.equals("All"))
/*     */     {
/* 630 */       iterableStrategySet = new LinkedHashSet(this.strategySet);
/* 631 */       iterableStrategySet.remove("All");
/*     */     }
/*     */     else {
/* 634 */       iterableStrategySet.add(strategy);
/*     */     }
/* 636 */     this.parameterSet = new LinkedHashSet();
/*     */     
/* 638 */     for (Map.Entry<String, ArrayList<String>> entry : this.parameterNameHashmap.entrySet())
/*     */     {
/* 640 */       if (iterableStrategySet.contains(entry.getKey()))
/*     */       {
/* 642 */         if (this.parameterSet.isEmpty()) {
/* 643 */           this.parameterSet.addAll((Collection)entry.getValue());
/*     */         } else {
/* 645 */           this.parameterSet.retainAll((Collection)entry.getValue());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 650 */     return this.parameterSet;
/*     */   }
/*     */   
/*     */   public ArrayList<String> fetchPerformanceMeasureNameSet()
/*     */   {
/* 655 */     ArrayList<String> performanceMeasureName = new ArrayList();
/*     */     
/* 657 */     for (Map.Entry<String, ArrayList<String>> entry : this.performanceMeasureNameHashmap.entrySet())
/*     */     {
/*     */ 
/* 660 */       performanceMeasureName = (ArrayList)entry.getValue();
/*     */     }
/*     */     
/* 663 */     return performanceMeasureName;
/*     */   }
/*     */   
/*     */   public ArrayList<String> fetchChartTypeSet()
/*     */   {
/* 668 */     ArrayList<String> chartTypeList = new ArrayList();
/* 669 */     chartTypeList.add("Line");
/* 670 */     chartTypeList.add("Scatter");
/*     */     
/* 672 */     return chartTypeList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[][] getDataSet(String strategyName, String scripList, String assetClass, String scrip, int parameterIndex, int performanceIndex)
/*     */   {
/* 680 */     HashMap<String, ArrayList<Double>> yAxisDataSet = getYAxisDataSet(strategyName, scripList, assetClass, scrip, performanceIndex);
/*     */     
/* 682 */     TreeMap<Double, ArrayList<Double>> mergedDatMap = getMergedDataSet(yAxisDataSet, parameterIndex);
/*     */     
/* 684 */     return convertDataMapToDataArray(mergedDatMap);
/*     */   }
/*     */   
/*     */   public double[][] getDataSet(String strategyName, String scripList, String assetClass, String scrip, int performanceIndex)
/*     */   {
/* 689 */     HashMap<String, ArrayList<Double>> yAxisDataSet = getYAxisDataSet(strategyName, scripList, assetClass, scrip, performanceIndex);
/*     */     
/* 691 */     return get_insample_oos_map(yAxisDataSet);
/*     */   }
/*     */   
/*     */   private double[][] get_insample_oos_map(HashMap<String, ArrayList<Double>> yAxisDataSet)
/*     */   {
/* 696 */     int dataLength = this.parameterValueHashMap.keySet().size() / 2;
/* 697 */     double[][] dataArray = new double[2][dataLength];
/*     */     
/* 699 */     int i = 0;
/*     */     
/* 701 */     Iterator<Map.Entry<String, ArrayList<String>>> it = this.parameterValueHashMap.entrySet().iterator();
/* 702 */     while (it.hasNext())
/*     */     {
/* 704 */       Map.Entry<String, ArrayList<String>> entry = (Map.Entry)it.next();
/* 705 */       ArrayList<Double> valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
/* 706 */       double averageYVal = MathLib.sumDouble(valueList) / valueList.size();
/* 707 */       dataArray[0][i] = MathLib.round(averageYVal, 2);
/*     */       
/* 709 */       entry = (Map.Entry)it.next();
/* 710 */       valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
/* 711 */       averageYVal = MathLib.sumDouble(valueList) / valueList.size();
/* 712 */       dataArray[1][i] = MathLib.round(averageYVal, 2);
/*     */       
/* 714 */       i++;
/*     */     }
/*     */     
/* 717 */     return dataArray;
/*     */   }
/*     */   
/*     */   private TreeMap<Double, ArrayList<Double>> getMergedDataSet(HashMap<String, ArrayList<Double>> yAxisDataSet, int parameterIndex)
/*     */   {
/* 722 */     TreeMap<Double, ArrayList<Double>> mergedDataMap = new TreeMap();
/*     */     
/* 724 */     for (Map.Entry<String, ArrayList<String>> entry : this.parameterValueHashMap.entrySet())
/*     */     {
/* 726 */       double parameter = Double.parseDouble((String)((ArrayList)entry.getValue()).get(parameterIndex));
/* 727 */       ArrayList<Double> valueList = (ArrayList)yAxisDataSet.get(entry.getKey());
/*     */       
/* 729 */       ArrayList<Double> existingValueList = (ArrayList)mergedDataMap.get(Double.valueOf(parameter));
/*     */       
/* 731 */       if (existingValueList == null)
/*     */       {
/* 733 */         existingValueList = new ArrayList();
/*     */       }
/*     */       
/*     */ 
/* 737 */       existingValueList.addAll(valueList);
/*     */       
/* 739 */       mergedDataMap.put(Double.valueOf(parameter), existingValueList);
/*     */     }
/*     */     
/* 742 */     return mergedDataMap;
/*     */   }
/*     */   
/*     */   private double[][] convertDataMapToDataArray(TreeMap<Double, ArrayList<Double>> mergedDatMap)
/*     */   {
/* 747 */     int dataLength = mergedDatMap.keySet().size();
/* 748 */     double[][] dataArray = new double[2][dataLength];
/*     */     
/* 750 */     int i = 0;
/*     */     
/* 752 */     for (Map.Entry<Double, ArrayList<Double>> entry : mergedDatMap.entrySet())
/*     */     {
/* 754 */       ArrayList<Double> yValueArray = (ArrayList)entry.getValue();
/* 755 */       double averageYVal = MathLib.sumDouble(yValueArray) / yValueArray.size();
/*     */       
/* 757 */       dataArray[0][i] = MathLib.round(((Double)entry.getKey()).doubleValue(), 2);
/* 758 */       dataArray[1][i] = MathLib.round(averageYVal, 2);
/*     */       
/* 760 */       i++;
/*     */     }
/*     */     
/* 763 */     return dataArray;
/*     */   }
/*     */   
/*     */   public HashMap<String, ArrayList<Double>> getYAxisDataSet(String strategyName, String scripList, String assetClass, String scrip, int performanceNameIndex)
/*     */   {
/* 768 */     HashMap<String, ArrayList<Double>> yAxisValueHashMap = new HashMap();
/*     */     
/* 770 */     LinkedHashSet<String> iterableStrategySet = new LinkedHashSet();
/*     */     
/* 772 */     if (strategyName.equals("All"))
/*     */     {
/* 774 */       iterableStrategySet = new LinkedHashSet(this.strategySet);
/* 775 */       iterableStrategySet.remove("All");
/*     */     }
/*     */     else
/*     */     {
/* 779 */       iterableStrategySet.add(strategyName);
/*     */     }
/*     */     Iterator localIterator2;
/* 782 */     for (Iterator localIterator1 = this.performanceValueHashMap.entrySet().iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/* 786 */         localIterator2.hasNext())
/*     */     {
/* 782 */       Map.Entry<String, HashMap<String, ArrayList<String>>> entry = (Map.Entry)localIterator1.next();
/*     */       
/* 784 */       HashMap<String, ArrayList<String>> assetValueMap = (HashMap)entry.getValue();
/*     */       
/* 786 */       localIterator2 = iterableStrategySet.iterator(); continue;String iterableStrategyName = (String)localIterator2.next();
/*     */       
/* 788 */       ArrayList<Double> valueList = new ArrayList();
/*     */       
/* 790 */       if ((scripList.equals("All")) && (!scrip.equals("All")))
/*     */       {
/* 792 */         for (String key : assetValueMap.keySet())
/*     */         {
/* 794 */           String[] keyParts = key.split("\\|");
/* 795 */           if ((keyParts[0].equals(iterableStrategyName)) && (!keyParts[1].equals("All")) && (keyParts[2].equals(scrip)))
/*     */           {
/* 797 */             Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
/* 798 */             valueList.add(fetchValue);
/*     */           }
/*     */         }
/*     */       }
/* 802 */       else if (assetClass.equals("All"))
/*     */       {
/*     */ 
/* 805 */         String fetchKey = iterableStrategyName + "|" + scripList + "|" + scrip;
/* 806 */         Object fetchValueArray = (ArrayList)assetValueMap.get(fetchKey);
/*     */         
/* 808 */         if (fetchValueArray != null)
/*     */         {
/* 810 */           Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)fetchValueArray).get(performanceNameIndex)));
/* 811 */           valueList.add(fetchValue);
/*     */         }
/*     */       } else {
/*     */         Object fetchValueArray;
/* 815 */         if ((!scrip.equals("All")) && (!scripList.equals("All")))
/*     */         {
/* 817 */           String fetchKey = iterableStrategyName + "|" + scripList + "|" + scrip;
/* 818 */           fetchValueArray = (ArrayList)assetValueMap.get(fetchKey);
/*     */           
/* 820 */           if (fetchValueArray != null)
/*     */           {
/* 822 */             Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)fetchValueArray).get(performanceNameIndex)));
/* 823 */             valueList.add(fetchValue);
/*     */           }
/*     */         }
/* 826 */         else if ((scrip.equals("All")) && (!scripList.equals("All"))) {
/* 827 */           for (fetchValueArray = assetValueMap.keySet().iterator(); ((Iterator)fetchValueArray).hasNext();) { String key = (String)((Iterator)fetchValueArray).next();
/*     */             
/* 829 */             String[] keyParts = key.split("\\|");
/* 830 */             if ((keyParts[0].equals(iterableStrategyName)) && (keyParts[1].equals(scripList)) && (!keyParts[2].equals("All")) && (keyParts[2].split(" ")[1].equals(assetClass))) {
/* 831 */               Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
/* 832 */               valueList.add(fetchValue);
/*     */             }
/*     */           }
/* 835 */         } else if ((scrip.equals("All")) && (scripList.equals("All"))) {
/* 836 */           for (fetchValueArray = assetValueMap.keySet().iterator(); ((Iterator)fetchValueArray).hasNext();) { String key = (String)((Iterator)fetchValueArray).next();
/*     */             
/* 838 */             String[] keyParts = key.split("\\|");
/* 839 */             if ((keyParts[0].equals(iterableStrategyName)) && (!keyParts[1].equals("All")) && (!keyParts[2].equals("All")) && (keyParts[2].split(" ")[1].equals(assetClass))) {
/* 840 */               Double fetchValue = Double.valueOf(convertToDouble((String)((ArrayList)assetValueMap.get(key)).get(performanceNameIndex)));
/* 841 */               valueList.add(fetchValue);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 847 */       if (valueList.size() > 0) {
/* 848 */         yAxisValueHashMap.put((String)entry.getKey() + "#" + iterableStrategyName, valueList);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 853 */     return yAxisValueHashMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<Double> getParameterValueListWrapperFunction(String strategy, String parameterName)
/*     */   {
/* 859 */     this.graphHashMap.clear();
/* 860 */     this.finalGraphHashMap.clear();
/* 861 */     if (strategy.compareTo("All") == 0)
/* 862 */       return getParameterValueList(parameterName);
/* 863 */     return getParameterValueList(strategy, parameterName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ArrayList<Double> getParameterValueList(String strategy, String parameterName)
/*     */   {
/* 876 */     ArrayList<Double> parameterValueListasDouble = new ArrayList();
/* 877 */     ArrayList<String> keySet = new ArrayList(this.parameterValueHashMap.keySet());
/*     */     
/*     */ 
/* 880 */     for (int i = 0; i < keySet.size(); i++) {
/* 881 */       String key = (String)keySet.get(i);
/* 882 */       if (key.substring(key.indexOf("#") + 1).compareTo(strategy) == 0)
/*     */       {
/* 884 */         if (!parameterValueListasDouble.contains(Double.valueOf(Double.parseDouble((String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))))) {
/* 885 */           parameterValueListasDouble.add(Double.valueOf(Double.parseDouble(
/* 886 */             (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))));
/* 887 */           this.graphHashMap.put(
/* 888 */             Double.valueOf(Double.parseDouble(
/* 889 */             (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))), 
/* 890 */             new ArrayList());
/*     */         } }
/*     */     }
/* 893 */     return parameterValueListasDouble;
/*     */   }
/*     */   
/*     */ 
/*     */   private ArrayList<Double> getParameterValueList(String parameterName)
/*     */   {
/* 899 */     ArrayList<Double> parameterValueListasDouble = new ArrayList();
/* 900 */     ArrayList<String> keySet = new ArrayList(this.parameterValueHashMap.keySet());
/*     */     
/*     */ 
/* 903 */     for (int i = 0; i < keySet.size(); i++) {
/* 904 */       String key = (String)keySet.get(i);
/*     */       
/* 906 */       if (!parameterValueListasDouble.contains(Double.valueOf(Double.parseDouble((String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))))) {
/* 907 */         parameterValueListasDouble.add(Double.valueOf(Double.parseDouble(
/* 908 */           (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))));
/* 909 */         this.graphHashMap.put(
/* 910 */           Double.valueOf(Double.parseDouble(
/* 911 */           (String)((ArrayList)this.parameterValueHashMap.get(key)).get(((ArrayList)this.parameterNameHashmap.get(key.split("#")[1])).indexOf(parameterName)))), 
/* 912 */           new ArrayList());
/*     */       }
/*     */     }
/*     */     
/* 916 */     return parameterValueListasDouble;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void coordinateProcessing()
/*     */   {
/* 928 */     ArrayList<Double> parameterKeySet = new ArrayList(this.graphHashMap.keySet());
/* 929 */     Double performanceValueSum = Double.valueOf(0.0D);
/* 930 */     for (int i = 0; i < parameterKeySet.size(); i++) {
/* 931 */       for (Double performanceValue : (ArrayList)this.graphHashMap.get(parameterKeySet.get(i))) {
/* 932 */         performanceValueSum = Double.valueOf(performanceValueSum.doubleValue() + performanceValue.doubleValue());
/*     */       }
/* 934 */       this.finalGraphHashMap.put((Double)parameterKeySet.get(i), 
/* 935 */         Double.valueOf(performanceValueSum.doubleValue() / ((ArrayList)this.graphHashMap.get(parameterKeySet.get(i))).size()));
/* 936 */       performanceValueSum = Double.valueOf(0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   private double convertToDouble(String str)
/*     */   {
/* 942 */     if (str.endsWith("%")) {
/* 943 */       str = str.substring(0, str.length() - 1);
/* 944 */     } else if (str.contains(" ")) {
/* 945 */       str = str.substring(0, str.indexOf(" "));
/*     */     }
/* 947 */     double value = Double.parseDouble(str);
/*     */     
/* 949 */     return value;
/*     */   }
/*     */   
/*     */   class FolderComparator implements java.util.Comparator<File> {
/*     */     FolderComparator() {}
/*     */     
/* 955 */     public int compare(File f1, File f2) { return Long.valueOf(f1.lastModified()).compareTo(Long.valueOf(f2.lastModified())); }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/SensitivityAnalysisDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */