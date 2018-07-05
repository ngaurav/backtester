/*     */ package com.q1.bt.driver.backtest;
/*     */ 
/*     */ import com.q1.bt.data.DataDriver;
/*     */ import com.q1.bt.data.DataTypeHandler;
/*     */ import com.q1.bt.data.ScripDataHandler;
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.driver.backtest.enums.OutputMode;
/*     */ import com.q1.bt.execution.ScripExecution;
/*     */ import com.q1.bt.execution.Strategy;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.postprocess.PostProcess;
/*     */ import com.q1.bt.process.backtest.PostProcessMode;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.csv.ReverseCSVReader;
/*     */ import com.q1.exception.bt.ExpiredContractException;
/*     */ import com.q1.exception.bt.IllegalOrderTypeException;
/*     */ import com.q1.exception.bt.IllegalQuantityException;
/*     */ import com.q1.exception.bt.MissingExpiryException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ 
/*     */ public class ScripListDriver extends Thread
/*     */ {
/*     */   BacktesterGlobal btGlobal;
/*     */   public DataDriver dataDriver;
/*  38 */   public HashMap<String, CSVWriter> scripMTMWriterMap = new HashMap();
/*  39 */   Long prevDate = null;
/*     */   
/*     */   PostProcess consolPP;
/*     */   
/*     */   Double capital;
/*     */   
/*     */   String scripListID;
/*     */   
/*     */   String strategyID;
/*     */   ArrayList<Scrip> scripSet;
/*     */   Backtest backtest;
/*     */   OutputMode outputMode;
/*     */   String strategyDataType;
/*     */   String backtestKey;
/*     */   Long startDate;
/*     */   Long endDate;
/*     */   BacktestDriver btDriver;
/*     */   
/*     */   public ScripListDriver(String scripListID, ArrayList<Scrip> scripSet, String strategyID, String strategyDataType, BacktesterGlobal btGlobal, Backtest backtest, String parameterKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
/*     */     throws Exception
/*     */   {
/*  60 */     this.btGlobal = btGlobal;
/*  61 */     this.backtest = backtest;
/*     */     
/*  63 */     this.scripListID = scripListID;
/*  64 */     this.strategyID = strategyID;
/*  65 */     this.scripSet = scripSet;
/*     */     
/*  67 */     this.capital = btGlobal.loginParameter.getCapital();
/*     */     
/*  69 */     this.backtestKey = (strategyID + " " + scripListID);
/*     */     
/*  71 */     this.strategyDataType = strategyDataType;
/*     */     
/*     */ 
/*  74 */     this.dataDriver = new DataDriver(btGlobal, Long.valueOf(backtest.backtestParameter.getStartDate()), backtest, scripListID, scripSet, strategyDataType);
/*     */     
/*     */ 
/*  77 */     updateStartAndEndDate();
/*     */     
/*     */ 
/*  80 */     checkOutput(parameterKey, outputMap);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*  87 */     String mtmPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
/*  88 */       this.backtestKey;
/*  89 */     createPath(mtmPath);
/*  90 */     for (Scrip scrip : this.scripSet) {
/*     */       try {
/*  92 */         CSVWriter mtmWriter = new CSVWriter(mtmPath + "/" + scrip.scripID + " MTM.csv", false, ",");
/*  93 */         this.scripMTMWriterMap.put(scrip.scripID, mtmWriter);
/*     */       }
/*     */       catch (IOException e) {
/*  96 */         e.printStackTrace();
/*  97 */         this.btGlobal.displayMessage("Error creating MTM Writer for: " + this.backtestKey + " " + scrip.scripID);
/*  98 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 104 */       while (this.dataDriver.updateData())
/*     */       {
/*     */ 
/* 107 */         runDailyMTMRoutine(false);
/*     */         
/*     */ 
/* 110 */         this.dataDriver.updateDataViewers();
/*     */         
/*     */ 
/* 113 */         if (!runBacktestRoutine()) {
/* 114 */           return;
/*     */         }
/*     */       }
/*     */     } catch (Exception e1) {
/* 118 */       e1.printStackTrace();
/* 119 */       return;
/*     */     }
/*     */     
/*     */ 
/* 123 */     runDailyMTMRoutine(true);
/*     */     
/*     */ 
/* 126 */     closeFiles();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateStartAndEndDate()
/*     */   {
/* 133 */     Collection<ScripDataHandler> sdHandlerList = 
/* 134 */       ((DataTypeHandler)this.dataDriver.dataTypeHandlerMap.get(this.strategyDataType)).scripDataHandlerMap.values();
/*     */     
/* 136 */     PostProcessMode ppMode = this.backtest.backtestParameter.getPostProcessMode();
/*     */     
/* 138 */     this.startDate = Long.valueOf(this.backtest.backtestParameter.getStartDate());
/* 139 */     this.endDate = Long.valueOf(this.backtest.backtestParameter.getEndDate());
/*     */     
/* 141 */     if (!ppMode.equals(PostProcessMode.Portfolio)) {
/* 142 */       for (ScripDataHandler sdHandler : sdHandlerList) {
/* 143 */         if (sdHandler.startDate.longValue() > this.startDate.longValue())
/* 144 */           this.startDate = sdHandler.startDate;
/* 145 */         if (sdHandler.endDate.longValue() < this.endDate.longValue()) {
/* 146 */           this.endDate = sdHandler.endDate;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void createBacktestDriver(Strategy strategy)
/*     */     throws IOException
/*     */   {
/* 156 */     this.btDriver = new BacktestDriver(this.scripListID, this.scripSet, strategy, this.dataDriver.dataTypeViewerMap, this.btGlobal, this.backtest, 
/* 157 */       this.startDate, this.endDate);
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeMTMToFile(CSVWriter writer, String key, Double mtm)
/*     */     throws IOException
/*     */   {
/* 164 */     String[] mtmVal = { this.prevDate.toString(), mtm.toString() };
/* 165 */     writer.writeLine(mtmVal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void closeFiles()
/*     */   {
/* 172 */     this.btDriver.closeFileStreams();
/*     */     try {
/* 174 */       this.btDriver.serializeObjects();
/*     */     } catch (IOException e) {
/* 176 */       e.printStackTrace();
/* 177 */       this.btGlobal.displayMessage("Error Serializing");
/*     */     }
/*     */     
/*     */ 
/* 181 */     for (CSVWriter writer : this.scripMTMWriterMap.values()) {
/*     */       try {
/* 183 */         writer.close();
/*     */       }
/*     */       catch (IOException e) {
/* 186 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Double> convertMap(TreeMap<Long, Double[]> mtmMap) {
/* 192 */     TreeMap<Long, Double> consolMTM = new TreeMap();
/*     */     
/* 194 */     Double cumMTM = Double.valueOf(0.0D);
/* 195 */     for (Map.Entry<Long, Double[]> entry : mtmMap.entrySet()) {
/* 196 */       Long dateTime = (Long)entry.getKey();
/* 197 */       Double[] mtmVal = (Double[])entry.getValue();
/* 198 */       cumMTM = Double.valueOf(cumMTM.doubleValue() + mtmVal[0].doubleValue() / mtmVal[1].doubleValue());
/* 199 */       if (com.q1.math.MathLib.doubleCompare(cumMTM, Double.valueOf(0.0D)).intValue() != 0) {
/* 200 */         consolMTM.put(dateTime, Double.valueOf(mtmVal[0].doubleValue() / mtmVal[1].doubleValue()));
/*     */       }
/*     */     }
/*     */     
/* 204 */     return consolMTM;
/*     */   }
/*     */   
/*     */   public TreeMap<Long, Double> getMTMMap(String key, String mtmPath) {
/* 208 */     String mtmFilePath = mtmPath + "/" + key + " MTM.csv";
/* 209 */     TreeMap<Long, Double> mtmMap = new TreeMap();
/*     */     try {
/* 211 */       CSVReader reader = new CSVReader(mtmFilePath, ',', 0);
/*     */       String[] mtmLine;
/* 213 */       while ((mtmLine = reader.getLine()) != null) { String[] mtmLine;
/* 214 */         Long date = Long.valueOf(Long.parseLong(mtmLine[0]));
/* 215 */         Double mtm = Double.valueOf(Double.parseDouble(mtmLine[1]));
/* 216 */         mtmMap.put(date, mtm);
/*     */       }
/*     */     }
/*     */     catch (IOException e1) {
/* 220 */       this.btGlobal.displayMessage("Could not find MTM File for: " + key);
/* 221 */       e1.printStackTrace();
/* 222 */       return null;
/*     */     }
/* 224 */     return mtmMap;
/*     */   }
/*     */   
/*     */   public static void createPath(String folder)
/*     */   {
/* 229 */     if (!new File(folder).exists()) {
/* 230 */       new File(folder).mkdirs();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void runDailyMTMRoutine(boolean forceWrite)
/*     */   {
/* 237 */     if (this.prevDate == null) {
/* 238 */       if (!this.dataDriver.strategyDate.equals(Long.valueOf(0L))) {
/* 239 */         this.prevDate = this.dataDriver.strategyDate;
/*     */       }
/*     */     }
/* 242 */     else if ((!this.prevDate.equals(this.dataDriver.strategyDate)) || (forceWrite))
/*     */     {
/* 244 */       for (Map.Entry<String, CSVWriter> entry : this.scripMTMWriterMap.entrySet())
/*     */       {
/* 246 */         String scripID = (String)entry.getKey();
/* 247 */         CSVWriter writer = (CSVWriter)entry.getValue();
/*     */         
/*     */ 
/* 250 */         Double $mtm = Double.valueOf(0.0D);
/* 251 */         ScripExecution scripExec = (ScripExecution)this.btDriver.execution.executionMap.get(scripID);
/* 252 */         $mtm = Double.valueOf($mtm.doubleValue() + scripExec.$CumMTM.doubleValue() - scripExec.$PrevDayCumMTM.doubleValue());
/* 253 */         scripExec.$PrevDayCumMTM = scripExec.$CumMTM;
/*     */         
/*     */ 
/* 256 */         Double percMTM = Double.valueOf($mtm.doubleValue() / this.capital.doubleValue());
/*     */         
/*     */ 
/* 259 */         if ((this.prevDate.longValue() > this.startDate.longValue()) || (this.prevDate.equals(this.startDate))) {
/*     */           try {
/* 261 */             writeMTMToFile(writer, this.backtestKey, percMTM);
/*     */           } catch (IOException e) {
/* 263 */             e.printStackTrace();
/* 264 */             this.btGlobal.displayMessage("Error writing MTM to file for: " + this.backtestKey);
/* 265 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 272 */       this.prevDate = this.dataDriver.strategyDate;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean runBacktestRoutine()
/*     */   {
/*     */     try {
/* 279 */       this.btDriver.runBacktest();
/*     */     } catch (IllegalQuantityException e) {
/* 281 */       this.btGlobal.displayMessage(
/* 282 */         this.strategyID + " " + this.scripListID + ": Order quantity exceeds maximum permissible quantity");
/* 283 */       e.printStackTrace();
/* 284 */       return false;
/*     */     } catch (MissingExpiryException e) {
/* 286 */       this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Order expiry not found in current data point");
/* 287 */       e.printStackTrace();
/* 288 */       return false;
/*     */     } catch (IllegalOrderTypeException e) {
/* 290 */       this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + 
/* 291 */         ": Illegal Order Type. Order type should either be Buy (1.0) or Sell (2.0)");
/* 292 */       e.printStackTrace();
/* 293 */       return false;
/*     */     } catch (ExpiredContractException e) {
/* 295 */       this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Position still exists in expired contract");
/* 296 */       e.printStackTrace();
/* 297 */       return false;
/*     */     } catch (IOException e) {
/* 299 */       this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Error writing orderBook");
/* 300 */       e.printStackTrace();
/* 301 */       return false;
/*     */     } catch (NullPointerException e) {
/* 303 */       this.btGlobal.displayMessage(this.strategyID + " " + this.scripListID + ": Strategy Error");
/* 304 */       e.printStackTrace();
/* 305 */       return false;
/*     */     }
/*     */     catch (Exception e) {
/* 308 */       e.printStackTrace();
/* 309 */       return false;
/*     */     }
/* 311 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public String checkIfBacktestExists(String strategyParamKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
/*     */     throws IOException
/*     */   {
/* 318 */     HashMap<String, HashMap<String, String>> scripListDateMap = (HashMap)outputMap.get(strategyParamKey);
/*     */     
/*     */ 
/* 321 */     String scripKey = this.scripListID;
/* 322 */     TreeSet<String> scripIDSet = new TreeSet();
/* 323 */     for (Scrip scrip : this.scripSet)
/* 324 */       scripIDSet.add(scrip.scripID);
/* 325 */     for (String scripID : scripIDSet) {
/* 326 */       scripKey = scripKey + "|" + scripID;
/*     */     }
/*     */     
/* 329 */     if (scripListDateMap != null)
/*     */     {
/* 331 */       HashMap<String, String> dateMap = (HashMap)scripListDateMap.get(scripKey);
/*     */       
/* 333 */       if (dateMap == null) {
/* 334 */         return null;
/*     */       }
/* 336 */       String outputTS = (String)dateMap.get(this.startDate.toString() + " " + this.endDate.toString());
/*     */       
/*     */ 
/* 339 */       if (outputTS != null) {
/* 340 */         this.outputMode = OutputMode.Existing;
/* 341 */         return outputTS;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 347 */       String maxKey = "";
/* 348 */       Long maxEndDate = Long.valueOf(0L);
/* 349 */       for (String key : dateMap.keySet()) {
/* 350 */         String[] dateVal = key.split(" ");
/* 351 */         Long sDate = Long.valueOf(Long.parseLong(dateVal[0]));
/* 352 */         Long eDate = Long.valueOf(Long.parseLong(dateVal[1]));
/* 353 */         if ((eDate.longValue() > maxEndDate.longValue()) && (sDate.equals(this.startDate))) {
/* 354 */           maxEndDate = eDate;
/* 355 */           maxKey = key;
/*     */         }
/*     */       }
/*     */       
/* 359 */       if (maxEndDate.longValue() > this.endDate.longValue()) {
/* 360 */         this.outputMode = OutputMode.Chop;
/* 361 */         return (String)dateMap.get(maxKey);
/*     */       }
/* 363 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 367 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void checkOutput(String parameterKey, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
/*     */     throws IOException
/*     */   {
/* 376 */     this.outputMode = OutputMode.Normal;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 382 */     String primaryKey = this.strategyID + " " + parameterKey;
/* 383 */     String existingTS = null;
/*     */     
/*     */ 
/* 386 */     if (!this.backtest.backtestParameter.isSkipExistingBacktest())
/* 387 */       existingTS = checkIfBacktestExists(primaryKey, outputMap);
/*     */     CSVReader reader;
/*     */     ArrayList<String[]> scripListDates;
/*     */     String[] inData;
/* 391 */     if (this.outputMode.equals(OutputMode.Existing))
/*     */     {
/*     */ 
/* 394 */       String currentPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/MTM Data/" + this.strategyID + 
/* 395 */         " " + this.scripListID;
/* 396 */       String newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
/* 397 */         this.strategyID + " " + this.scripListID;
/* 398 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").exists())
/* 399 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").mkdirs();
/* 400 */       File currentFile = new File(currentPath);
/* 401 */       File newFile = new File(newPath);
/* 402 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       try {
/* 404 */         FileUtils.deleteDirectory(currentFile);
/*     */       } catch (IOException e) {
/* 406 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/* 410 */       currentPath = 
/* 411 */         this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Trade Data/" + this.strategyID + " " + this.scripListID;
/* 412 */       newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data/" + this.strategyID + 
/* 413 */         " " + this.scripListID;
/*     */       
/* 415 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").exists())
/* 416 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").mkdirs();
/* 417 */       currentFile = new File(currentPath);
/* 418 */       newFile = new File(newPath);
/* 419 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       try {
/* 421 */         FileUtils.deleteDirectory(currentFile);
/*     */       } catch (IOException e) {
/* 423 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/* 427 */       currentPath = 
/* 428 */         this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Post Process Data/" + this.strategyID + " " + this.scripListID;
/* 429 */       newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data/" + 
/* 430 */         this.strategyID + " " + this.scripListID;
/*     */       
/* 432 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").exists())
/*     */       {
/* 434 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").mkdirs(); }
/* 435 */       currentFile = new File(currentPath);
/* 436 */       newFile = new File(newPath);
/* 437 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       try {
/* 439 */         FileUtils.deleteDirectory(currentFile);
/*     */       } catch (IOException e) {
/* 441 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */ 
/* 445 */       currentPath = 
/* 446 */         this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Parameters/" + this.scripListID + " ScripSet.csv";
/* 447 */       newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters/" + this.scripListID + 
/* 448 */         " ScripSet.csv";
/* 449 */       currentFile = new File(currentPath);
/* 450 */       newFile = new File(newPath);
/* 451 */       FileUtils.copyFile(currentFile, newFile);
/*     */       
/*     */ 
/* 454 */       String paramPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Parameters/" + this.strategyID + 
/* 455 */         " ScripListDateMap.csv";
/* 456 */       reader = new CSVReader(paramPath, ',', 0);
/*     */       
/* 458 */       scripListDates = new ArrayList();
/* 459 */       while ((inData = reader.getLine()) != null) { String[] inData;
/* 460 */         if (!inData[0].equals(this.scripListID))
/* 461 */           scripListDates.add(inData); }
/* 462 */       CSVWriter writer = new CSVWriter(paramPath, false, ",");
/* 463 */       for (String[] scrip : scripListDates)
/* 464 */         writer.writeLine(scrip);
/* 465 */       writer.close();
/*     */ 
/*     */ 
/*     */     }
/* 469 */     else if (this.outputMode.equals(OutputMode.Chop))
/*     */     {
/*     */ 
/* 472 */       String currentPath = this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/MTM Data/" + this.strategyID + 
/* 473 */         " " + this.scripListID;
/* 474 */       String newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data/" + 
/* 475 */         this.strategyID + " " + this.scripListID;
/* 476 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").exists())
/* 477 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/MTM Data").mkdirs();
/* 478 */       File currentFile = new File(currentPath);
/* 479 */       File newFile = new File(newPath);
/* 480 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       
/* 482 */       inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File mtmFile = scripListDates[reader];
/* 483 */         String curPath = mtmFile.getAbsolutePath();
/* 484 */         chopFile(curPath, this.endDate);
/*     */       }
/*     */       
/*     */ 
/* 488 */       currentPath = 
/* 489 */         this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Trade Data/" + this.strategyID + " " + this.scripListID;
/* 490 */       newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data/" + this.strategyID + 
/* 491 */         " " + this.scripListID;
/*     */       
/* 493 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").exists())
/* 494 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Trade Data").mkdirs();
/* 495 */       currentFile = new File(currentPath);
/* 496 */       newFile = new File(newPath);
/* 497 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       
/* 499 */       inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File tradeBookFile = scripListDates[reader];
/* 500 */         String curPath = tradeBookFile.getAbsolutePath();
/* 501 */         chopFileDateTime(curPath, this.endDate);
/*     */       }
/*     */       
/*     */ 
/* 505 */       currentPath = 
/* 506 */         this.btGlobal.loginParameter.getOutputPath() + "/" + existingTS + "/Post Process Data/" + this.strategyID + " " + this.scripListID;
/* 507 */       newPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data/" + 
/* 508 */         this.strategyID + " " + this.scripListID;
/*     */       
/* 510 */       if (!new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").exists())
/*     */       {
/* 512 */         new File(this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Post Process Data").mkdirs(); }
/* 513 */       currentFile = new File(currentPath);
/* 514 */       newFile = new File(newPath);
/* 515 */       FileUtils.copyDirectory(currentFile, newFile);
/*     */       
/* 517 */       inData = (scripListDates = newFile.listFiles()).length; for (reader = 0; reader < inData; reader++) { File postFile = scripListDates[reader];
/* 518 */         String curPath = postFile.getAbsolutePath();
/* 519 */         chopFile(curPath, this.endDate);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 525 */     String outPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters";
/* 526 */     if (!new File(outPath).exists())
/* 527 */       new File(outPath).mkdirs();
/* 528 */     String scripListDatePath = outPath + "/" + this.strategyID + " ScripListDateMap.csv";
/* 529 */     CSVWriter writer = new CSVWriter(scripListDatePath, true, ",");
/*     */     
/*     */ 
/* 532 */     String[] scripOutput = { this.scripListID, this.startDate.toString(), this.endDate.toString() };
/* 533 */     writer.writeLine(scripOutput);
/* 534 */     writer.close();
/*     */     
/*     */ 
/* 537 */     String scripListScripPath = outPath + "/" + this.scripListID + " ScripSet.csv";
/* 538 */     if (!new File(scripListScripPath).exists()) {
/* 539 */       writer = new CSVWriter(scripListScripPath, false, ",");
/*     */       
/* 541 */       TreeSet<String> scripIDSet = new TreeSet();
/* 542 */       for (Scrip scrip : this.scripSet) {
/* 543 */         scripIDSet.add(scrip.scripID);
/*     */       }
/* 545 */       for (String scripID : scripIDSet)
/* 546 */         writer.writeLine(scripID);
/* 547 */       writer.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void chopFile(String fileName, Long endDate)
/*     */     throws IOException
/*     */   {
/* 554 */     ReverseCSVReader reader = new ReverseCSVReader(fileName, ',', 0);
/*     */     
/*     */ 
/* 557 */     Long deleteCount = Long.valueOf(0L);
/*     */     String[] line;
/* 559 */     while ((line = reader.readLineAsArray()) != null) {
/*     */       String[] line;
/* 561 */       Long curDate = Long.valueOf(Long.parseLong(line[0]));
/*     */       
/* 563 */       if (curDate.longValue() <= endDate.longValue()) break;
/* 564 */       String[] arrayOfString1; int j = (arrayOfString1 = line).length; for (int i = 0; i < j; i++) { String token = arrayOfString1[i];
/* 565 */         deleteCount = Long.valueOf(deleteCount.longValue() + token.length());
/*     */       }
/* 567 */       deleteCount = Long.valueOf(deleteCount.longValue() + line.length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 572 */     reader.close();
/*     */     
/*     */ 
/* 575 */     RandomAccessFile f = new RandomAccessFile(fileName, "rw");
/* 576 */     long fileLength = f.length();
/* 577 */     f.setLength(fileLength - deleteCount.longValue());
/* 578 */     f.close();
/*     */   }
/*     */   
/*     */   public static void chopFileDateTime(String fileName, Long endDate) throws IOException
/*     */   {
/* 583 */     ReverseCSVReader reader = new ReverseCSVReader(fileName, ',', 0);
/*     */     
/*     */ 
/* 586 */     Long deleteCount = Long.valueOf(0L);
/*     */     String[] line;
/* 588 */     while ((line = reader.readLineAsArray()) != null) {
/*     */       String[] line;
/* 590 */       Long curDate = Long.valueOf(Long.parseLong(line[0]) / 1000000L);
/*     */       
/* 592 */       if (curDate.longValue() <= endDate.longValue()) break;
/* 593 */       String[] arrayOfString1; int j = (arrayOfString1 = line).length; for (int i = 0; i < j; i++) { String token = arrayOfString1[i];
/* 594 */         deleteCount = Long.valueOf(deleteCount.longValue() + token.length());
/*     */       }
/* 596 */       deleteCount = Long.valueOf(deleteCount.longValue() + line.length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 601 */     reader.close();
/*     */     
/*     */ 
/* 604 */     RandomAccessFile f = new RandomAccessFile(fileName, "rw");
/* 605 */     long fileLength = f.length();
/* 606 */     f.setLength(fileLength - deleteCount.longValue());
/* 607 */     f.close();
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/ScripListDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */