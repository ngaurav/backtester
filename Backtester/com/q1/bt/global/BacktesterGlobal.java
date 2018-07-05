/*     */ package com.q1.bt.global;
/*     */ 
/*     */ import com.q1.bt.data.classes.ScripList;
/*     */ import com.q1.bt.driver.OOSAnalysisDriver;
/*     */ import com.q1.bt.driver.ResultDriver;
/*     */ import com.q1.bt.driver.RollingAnalysisDriver;
/*     */ import com.q1.bt.driver.backtest.enums.AggregationMode;
/*     */ import com.q1.bt.gui.main.BacktestPanel;
/*     */ import com.q1.bt.gui.main.LoginPanel;
/*     */ import com.q1.bt.gui.main.MachineLearningPanel;
/*     */ import com.q1.bt.gui.main.OOSAnalysisPanel;
/*     */ import com.q1.bt.gui.main.PostProcessPanel;
/*     */ import com.q1.bt.gui.main.ResultsPanel;
/*     */ import com.q1.bt.gui.main.RollingAnalysisPanel;
/*     */ import com.q1.bt.postprocess.PostProcess;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.backtest.PostProcessMode;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.objects.MachineLearning;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.bt.process.parameter.PackageParameter;
/*     */ import com.q1.csv.CSVReader;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ 
/*     */ public class BacktesterGlobal
/*     */ {
/*  41 */   BacktesterGUI btGuiObj = null;
/*  42 */   BacktesterNonGUI btNoGuiObj = null;
/*     */   
/*     */   public com.q1.bt.driver.BacktestMainDriver btDriver;
/*     */   
/*     */   public ResultDriver resultDriver;
/*     */   
/*     */   public RollingAnalysisDriver rollingAnalysisDriver;
/*     */   
/*     */   public OOSAnalysisDriver isOsDriver;
/*     */   
/*     */   public PackageParameter packageParameter;
/*     */   
/*  54 */   public ProcessFlow processFlow = new ProcessFlow();
/*     */   
/*     */ 
/*     */   public LoginParameter loginParameter;
/*     */   
/*     */   public boolean isGui;
/*     */   
/*  61 */   public Integer progressBar = Integer.valueOf(0);
/*     */   
/*     */   public BacktesterGlobal(BacktesterGUI btGuiObj, LoginParameter loginParameter, PackageParameter packageParameter)
/*     */     throws Exception
/*     */   {
/*  66 */     this.btGuiObj = btGuiObj;
/*  67 */     this.packageParameter = packageParameter;
/*  68 */     this.loginParameter = loginParameter;
/*  69 */     this.isGui = true;
/*     */   }
/*     */   
/*     */   public BacktesterGlobal(BacktesterNonGUI btNoGuiObj, LoginParameter loginParameter, PackageParameter packageParameter)
/*     */     throws Exception
/*     */   {
/*  75 */     this.btNoGuiObj = btNoGuiObj;
/*  76 */     this.packageParameter = packageParameter;
/*  77 */     this.loginParameter = loginParameter;
/*  78 */     this.isGui = false;
/*     */   }
/*     */   
/*     */   public void displayMessage(String message)
/*     */   {
/*  83 */     if (this.isGui) {
/*  84 */       this.btGuiObj.sysMsgPanel.displayMessage(message);
/*     */     } else {
/*  86 */       System.out.println(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void backButtonAction() {
/*  91 */     this.processFlow.revert();
/*  92 */     shiftTab();
/*     */   }
/*     */   
/*     */   public void shiftTab()
/*     */   {
/*     */     try {
/*  98 */       setPreviousProgressBar(0);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     try
/*     */     {
/* 103 */       enableRunButton();
/*     */     }
/*     */     catch (Exception localException1) {}
/*     */     
/* 107 */     this.btGuiObj.shiftTab(this.processFlow.getCurrentTabIndex().intValue());
/*     */   }
/*     */   
/*     */   public void shiftTab(BacktesterProcess process)
/*     */   {
/* 112 */     this.btGuiObj.shiftTab(this.processFlow.getProcessTabIndex(process).intValue());
/*     */   }
/*     */   
/*     */   public LinkedHashMap<String, ArrayList<String>> getStrategyScripMap(String timeStamp)
/*     */   {
/* 117 */     LinkedHashMap<String, ArrayList<String>> ssMap = new LinkedHashMap();
/*     */     
/* 119 */     String mtmPath = this.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data";
/* 120 */     File mtmPathFile = new File(mtmPath);
/* 121 */     File[] mtmFiles = mtmPathFile.listFiles();
/*     */     
/*     */     File[] arrayOfFile1;
/* 124 */     int j = (arrayOfFile1 = mtmFiles).length; for (int i = 0; i < j; i++) { File mtmFile = arrayOfFile1[i];
/* 125 */       String[] mtmVal = mtmFile.getName().split(" ");
/* 126 */       String strategy = mtmVal[0];
/* 127 */       String scrip = mtmVal[1] + " " + mtmVal[2] + " " + mtmVal[3] + " " + mtmVal[4] + " " + mtmVal[5];
/* 128 */       ArrayList<String> scrips = (ArrayList)ssMap.get(strategy);
/* 129 */       if (scrips == null) {
/* 130 */         scrips = new ArrayList();
/* 131 */         scrips.add(scrip);
/* 132 */         ssMap.put(strategy, scrips);
/*     */       } else {
/* 134 */         scrips.add(scrip);
/* 135 */         ssMap.put(strategy, scrips);
/*     */       }
/*     */     }
/*     */     
/* 139 */     return ssMap;
/*     */   }
/*     */   
/*     */   public LinkedHashMap<String, ArrayList<String>> getStrategyScripMap(String outputPath, String timestamp)
/*     */   {
/* 144 */     LinkedHashMap<String, ArrayList<String>> ssMap = new LinkedHashMap();
/*     */     
/* 146 */     String mtmPath = outputPath + "/" + timestamp + "/MTM Data";
/* 147 */     File mtmPathFile = new File(mtmPath);
/* 148 */     File[] mtmFiles = mtmPathFile.listFiles();
/*     */     
/*     */     File[] arrayOfFile1;
/* 151 */     int j = (arrayOfFile1 = mtmFiles).length; for (int i = 0; i < j; i++) { File mtmFile = arrayOfFile1[i];
/* 152 */       String[] mtmVal = mtmFile.getName().split(" ");
/* 153 */       String strategy = mtmVal[0];
/* 154 */       String scrip = mtmVal[1] + " " + mtmVal[2] + " " + mtmVal[3] + " " + mtmVal[4] + " " + mtmVal[5];
/* 155 */       ArrayList<String> scrips = (ArrayList)ssMap.get(strategy);
/* 156 */       if (scrips == null) {
/* 157 */         scrips = new ArrayList();
/* 158 */         scrips.add(scrip);
/* 159 */         ssMap.put(strategy, scrips);
/*     */       } else {
/* 161 */         scrips.add(scrip);
/* 162 */         ssMap.put(strategy, scrips);
/*     */       }
/*     */     }
/*     */     
/* 166 */     return ssMap;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void copyFile(File source, File dest)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_3
/*     */     //   2: aconst_null
/*     */     //   3: astore 4
/*     */     //   5: new 213	java/io/FileInputStream
/*     */     //   8: dup
/*     */     //   9: aload_1
/*     */     //   10: invokespecial 215	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   13: astore_3
/*     */     //   14: new 218	java/io/FileOutputStream
/*     */     //   17: dup
/*     */     //   18: aload_2
/*     */     //   19: invokespecial 220	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   22: astore 4
/*     */     //   24: sipush 1024
/*     */     //   27: newarray <illegal type>
/*     */     //   29: astore 5
/*     */     //   31: goto +13 -> 44
/*     */     //   34: aload 4
/*     */     //   36: aload 5
/*     */     //   38: iconst_0
/*     */     //   39: iload 6
/*     */     //   41: invokevirtual 221	java/io/OutputStream:write	([BII)V
/*     */     //   44: aload_3
/*     */     //   45: aload 5
/*     */     //   47: invokevirtual 227	java/io/InputStream:read	([B)I
/*     */     //   50: dup
/*     */     //   51: istore 6
/*     */     //   53: ifgt -19 -> 34
/*     */     //   56: goto +17 -> 73
/*     */     //   59: astore 7
/*     */     //   61: aload_3
/*     */     //   62: invokevirtual 233	java/io/InputStream:close	()V
/*     */     //   65: aload 4
/*     */     //   67: invokevirtual 236	java/io/OutputStream:close	()V
/*     */     //   70: aload 7
/*     */     //   72: athrow
/*     */     //   73: aload_3
/*     */     //   74: invokevirtual 233	java/io/InputStream:close	()V
/*     */     //   77: aload 4
/*     */     //   79: invokevirtual 236	java/io/OutputStream:close	()V
/*     */     //   82: return
/*     */     // Line number table:
/*     */     //   Java source line #171	-> byte code offset #0
/*     */     //   Java source line #172	-> byte code offset #2
/*     */     //   Java source line #174	-> byte code offset #5
/*     */     //   Java source line #175	-> byte code offset #14
/*     */     //   Java source line #176	-> byte code offset #24
/*     */     //   Java source line #178	-> byte code offset #31
/*     */     //   Java source line #179	-> byte code offset #34
/*     */     //   Java source line #178	-> byte code offset #44
/*     */     //   Java source line #181	-> byte code offset #56
/*     */     //   Java source line #182	-> byte code offset #61
/*     */     //   Java source line #183	-> byte code offset #65
/*     */     //   Java source line #184	-> byte code offset #70
/*     */     //   Java source line #182	-> byte code offset #73
/*     */     //   Java source line #183	-> byte code offset #77
/*     */     //   Java source line #185	-> byte code offset #82
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	83	0	this	BacktesterGlobal
/*     */     //   0	83	1	source	File
/*     */     //   0	83	2	dest	File
/*     */     //   1	73	3	is	java.io.InputStream
/*     */     //   3	75	4	os	java.io.OutputStream
/*     */     //   29	17	5	buffer	byte[]
/*     */     //   34	6	6	length	int
/*     */     //   51	3	6	length	int
/*     */     //   59	12	7	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   5	59	59	finally
/*     */   }
/*     */   
/*     */   public ArrayList<Long> getConsolDateList(String timestamp)
/*     */     throws IOException
/*     */   {
/* 190 */     TreeSet<Long> dateSet = new TreeSet();
/*     */     
/*     */ 
/* 193 */     String mtmFolderPath = this.loginParameter.getOutputPath() + "/" + timestamp + "/MTM Data";
/* 194 */     File mtmFolderFile = new File(mtmFolderPath);
/* 195 */     File[] arrayOfFile1; int j = (arrayOfFile1 = mtmFolderFile.listFiles()).length; for (int i = 0; i < j; i++) { File scripListFolderFile = arrayOfFile1[i];
/* 196 */       File[] arrayOfFile2; int m = (arrayOfFile2 = scripListFolderFile.listFiles()).length; for (int k = 0; k < m; k++) { File scripFile = arrayOfFile2[k];
/* 197 */         String scripMTMPath = scripFile.getAbsolutePath();
/* 198 */         CSVReader reader = new CSVReader(scripMTMPath, ',', 0);
/*     */         String[] line;
/* 200 */         while ((line = reader.getLine()) != null) { String[] line;
/* 201 */           dateSet.add(Long.valueOf(Long.parseLong(line[0])));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 206 */     return new ArrayList(dateSet);
/*     */   }
/*     */   
/*     */   public ArrayList<String> getPathClassFiles(String location)
/*     */   {
/* 211 */     File folder = new File(this.loginParameter.getMainPath() + "/src/" + location);
/* 212 */     File[] listOfFiles = folder.listFiles();
/* 213 */     ArrayList<String> classFiles = new ArrayList();
/* 214 */     if (listOfFiles == null)
/* 215 */       return classFiles;
/* 216 */     for (int i = 0; i < listOfFiles.length; i++) {
/* 217 */       if (listOfFiles[i].isFile()) {
/* 218 */         String[] fileVal = listOfFiles[i].getName().split("\\.");
/* 219 */         if (fileVal[1].equalsIgnoreCase("java"))
/* 220 */           classFiles.add(fileVal[0]);
/*     */       }
/*     */     }
/* 223 */     return classFiles;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addPackagetoTable(String packageName, JTable table)
/*     */   {
/* 229 */     String location = packageName.replaceAll("\\.", "/");
/* 230 */     ArrayList<String> classes = getPathClassFiles(location);
/*     */     
/*     */ 
/* 233 */     DefaultTableModel model = (DefaultTableModel)table.getModel();
/* 234 */     model.setRowCount(0);
/*     */     
/* 236 */     for (String cl : classes) {
/* 237 */       Object[] op = { cl };
/* 238 */       model.addRow(op);
/*     */     }
/*     */     
/* 241 */     table.setRowSelectionInterval(0, 0);
/*     */   }
/*     */   
/*     */   public void addPackagetoDataTable(String packageName, JTable table)
/*     */     throws Exception
/*     */   {
/* 247 */     String location = packageName.replaceAll("\\.", "/");
/* 248 */     ArrayList<String> classes = getPathClassFiles(location);
/*     */     
/*     */ 
/* 251 */     DefaultTableModel model = (DefaultTableModel)table.getModel();
/* 252 */     model.setRowCount(0);
/*     */     
/*     */ 
/* 255 */     for (String scripList : classes)
/*     */     {
/*     */ 
/* 258 */       Class<?> stratClass = Class.forName(packageName + "." + scripList);
/* 259 */       Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
/* 260 */       ScripList scripListObj = (ScripList)constructor.newInstance(new Object[0]);
/*     */       
/* 262 */       Object[] op = { scripList, scripListObj.getScripCount(), scripListObj.getAssetClassNames(), 
/* 263 */         scripListObj.getScripNames() };
/* 264 */       model.addRow(op);
/*     */     }
/*     */     
/* 267 */     table.setRowSelectionInterval(0, 0);
/*     */   }
/*     */   
/*     */   public String getTimeStamp()
/*     */   {
/* 272 */     File folder = new File(this.loginParameter.getOutputPath());
/* 273 */     File[] folders = folder.listFiles();
/* 274 */     Integer maxVal = Integer.valueOf(0);
/* 275 */     File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
/* 276 */       Integer val = Integer.valueOf(-1);
/*     */       try {
/* 278 */         val = Integer.valueOf(Integer.parseInt(f.getName()));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/* 282 */       maxVal = Integer.valueOf(com.q1.math.MathLib.max(val.intValue(), maxVal.intValue()));
/*     */     }
/* 284 */     maxVal = Integer.valueOf(maxVal.intValue() + 1);
/* 285 */     return maxVal.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public void moveToTimestamp(String scripID, String strategyName, String existingTS, String btTimeStamp)
/*     */     throws IOException
/*     */   {
/* 292 */     String outputPath = this.loginParameter.getOutputPath();
/*     */     
/*     */ 
/* 295 */     String currentPath = outputPath + "/" + existingTS + "/MTM Data/" + strategyName + " " + scripID + " MTM.csv";
/* 296 */     String newPath = outputPath + "/" + btTimeStamp + "/MTM Data/" + strategyName + " " + scripID + " MTM.csv";
/* 297 */     if (!new File(outputPath + "/" + btTimeStamp + "/MTM Data").exists())
/* 298 */       new File(outputPath + "/" + btTimeStamp + "/MTM Data").mkdirs();
/* 299 */     File currentFile = new File(currentPath);
/* 300 */     File newFile = new File(newPath);
/* 301 */     currentFile.renameTo(newFile);
/*     */     
/*     */ 
/* 304 */     currentPath = outputPath + "/" + existingTS + "/Trade Data/" + strategyName + " " + scripID + " Tradebook.csv";
/* 305 */     newPath = outputPath + "/" + btTimeStamp + "/Trade Data/" + strategyName + " " + scripID + " Tradebook.csv";
/* 306 */     if (!new File(outputPath + "/" + btTimeStamp + "/Trade Data").exists())
/* 307 */       new File(outputPath + "/" + btTimeStamp + "/Trade Data").mkdirs();
/* 308 */     currentFile = new File(currentPath);
/* 309 */     newFile = new File(newPath);
/* 310 */     currentFile.renameTo(newFile);
/*     */     
/*     */ 
/* 313 */     currentPath = outputPath + "/" + existingTS + "/Post Process Data/" + strategyName + " " + scripID + 
/* 314 */       " Output.csv";
/* 315 */     newPath = outputPath + "/" + btTimeStamp + "/Post Process Data/" + strategyName + " " + scripID + " Output.csv";
/* 316 */     if (!new File(outputPath + "/" + btTimeStamp + "/Post Process Data").exists())
/* 317 */       new File(outputPath + "/" + btTimeStamp + "/Post Process Data").mkdirs();
/* 318 */     currentFile = new File(currentPath);
/* 319 */     newFile = new File(newPath);
/* 320 */     currentFile.renameTo(newFile);
/*     */     
/*     */ 
/* 323 */     String paramPath = outputPath + "/" + existingTS + "/Parameters/" + strategyName + " ScripList.csv";
/* 324 */     CSVReader reader = new CSVReader(paramPath, ',', 0);
/*     */     
/* 326 */     ArrayList<String[]> scrips = new ArrayList();
/* 327 */     String[] inData; while ((inData = reader.getLine()) != null) { String[] inData;
/* 328 */       if (!inData[0].equals(scripID))
/* 329 */         scrips.add(inData); }
/* 330 */     CSVWriter writer = new CSVWriter(paramPath, false, ",");
/* 331 */     for (String[] scrip : scrips)
/* 332 */       writer.writeLine(scrip);
/* 333 */     writer.close();
/* 334 */     if (scrips.isEmpty()) {
/*     */       try {
/* 336 */         deleteDir(new File(outputPath + "/" + existingTS));
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean deleteDir(File dir)
/*     */   {
/* 345 */     if (dir.isDirectory()) {
/* 346 */       String[] children = dir.list();
/* 347 */       for (int i = 0; i < children.length; i++) {
/* 348 */         boolean success = deleteDir(new File(dir, children[i]));
/* 349 */         if (!success) {
/* 350 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 354 */     return dir.delete();
/*     */   }
/*     */   
/*     */ 
/*     */   public String createPath(String folder)
/*     */   {
/* 360 */     String path = BacktesterGlobal.class.getProtectionDomain().getCodeSource().getLocation().getPath();
/*     */     
/*     */     try
/*     */     {
/* 364 */       String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
/* 365 */       String[] acc = currentPath.split("/");
/* 366 */       String outPath = acc[0];
/* 367 */       for (int i = 1; i < acc.length - 1; i++) {
/* 368 */         if (acc[i].equals("lib"))
/*     */           break;
/* 370 */         outPath = outPath + "/" + acc[i];
/*     */       }
/*     */       
/* 373 */       String dataPath = outPath + folder;
/* 374 */       if (!new File(dataPath).exists())
/* 375 */         new File(dataPath).mkdirs();
/*     */     } catch (UnsupportedEncodingException e) {
/* 377 */       e.printStackTrace();
/* 378 */       return createPath(folder); }
/*     */     String dataPath;
/* 380 */     String currentPath; return dataPath;
/*     */   }
/*     */   
/*     */   public void clearTables()
/*     */   {
/* 385 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/* 386 */       this.btGuiObj.backtestPanel.clearTables();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initializeProcess()
/*     */   {
/* 394 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Login)) {
/* 395 */       this.btGuiObj.loginPanel.initialize(this.loginParameter);
/*     */ 
/*     */     }
/* 398 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/* 399 */       this.btGuiObj.backtestPanel.initialize();
/*     */ 
/*     */     }
/* 402 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.SensitivityAnalysis)) {
/* 403 */       this.btGuiObj.sensitivityPanel.initialize();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initializeProcess(MachineLearning machineLearning)
/*     */   {
/* 412 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Results)) {
/*     */       try {
/* 414 */         this.btGuiObj.resultsPanel.initialize(machineLearning);
/*     */       } catch (Exception e) {
/* 416 */         displayMessage("Error initializing Results Pane");
/* 417 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initializeProcess(Backtest backtest)
/*     */   {
/* 426 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.MachineLearning)) {
/* 427 */       this.btGuiObj.mlPanel.initialize(backtest);
/*     */ 
/*     */ 
/*     */     }
/* 431 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.BatchProcess)) {
/* 432 */       this.btGuiObj.batchProcessPanel.initialize(backtest);
/*     */ 
/*     */ 
/*     */     }
/* 436 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Results)) {
/*     */       try {
/* 438 */         this.btGuiObj.resultsPanel.initialize(backtest);
/*     */       } catch (Exception e) {
/* 440 */         displayMessage("Error initializing Results Pane");
/* 441 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */     }
/* 445 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.RollingAnalysis)) {
/*     */       try {
/* 447 */         this.btGuiObj.rollingAnalysisPanel.initialize(backtest);
/*     */       } catch (Exception e) {
/* 449 */         displayMessage("Error initializing Rolling Analysis Pane");
/* 450 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */     }
/* 454 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.IsOs)) {
/*     */       try {
/* 456 */         this.btGuiObj.isOsPanel.initialize(backtest);
/*     */       } catch (Exception e) {
/* 458 */         displayMessage("Error initializing IsOs Analysis Pane");
/* 459 */         e.printStackTrace();
/*     */       }
/*     */       
/*     */     }
/* 463 */     else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.PostProcess)) {
/*     */       try {
/* 465 */         this.btGuiObj.postPanel.initialize(backtest, this.resultDriver);
/*     */       }
/*     */       catch (Exception e) {
/* 468 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProgressBar(int val)
/*     */   {
/* 478 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/* 479 */       if (this.isGui) {
/* 480 */         this.btGuiObj.backtestPanel.setProgressBar(val);
/*     */       } else {
/* 482 */         this.progressBar = Integer.valueOf(val);
/* 483 */         System.out.println("Progress: " + this.progressBar + "%");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setPreviousProgressBar(int val)
/*     */   {
/* 493 */     if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.Backtest)) {
/* 494 */       if (this.isGui) {
/* 495 */         this.btGuiObj.backtestPanel.setProgressBar(val);
/*     */       } else {
/* 497 */         this.progressBar = Integer.valueOf(val);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getProgressBarValue()
/*     */   {
/* 506 */     if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/* 507 */       if (this.isGui) {
/* 508 */         return this.btGuiObj.backtestPanel.getProgressBarValue();
/*     */       }
/* 510 */       return this.progressBar.intValue();
/*     */     }
/*     */     
/* 513 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void enableRunButton()
/*     */   {
/* 520 */     if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.Backtest)) {
/* 521 */       this.btGuiObj.backtestPanel.enableRunButton();
/*     */ 
/*     */ 
/*     */     }
/* 525 */     else if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.MachineLearning)) {
/* 526 */       this.btGuiObj.mlPanel.enableRunButton();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public HashMap<String, HashMap<String, HashMap<String, String>>> createOutputMap()
/*     */     throws IOException
/*     */   {
/* 534 */     HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
/*     */     
/* 536 */     File outputFile = new File(this.loginParameter.getOutputPath());
/* 537 */     File[] folders = outputFile.listFiles();
/*     */     
/*     */     File[] arrayOfFile1;
/* 540 */     int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*     */       
/* 542 */       String curTimestamp = folder.getName();
/* 543 */       String basePath = this.loginParameter.getOutputPath() + "/" + curTimestamp;
/*     */       
/* 545 */       String key = "";
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 550 */         Integer.parseInt(curTimestamp);
/*     */       }
/*     */       catch (Exception e) {
/*     */         continue;
/*     */       }
/* 555 */       if (new File(basePath + "/Parameters").exists())
/*     */       {
/*     */ 
/* 558 */         File[] paramFiles = new File(basePath + "/Parameters").listFiles();
/* 559 */         File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
/* 560 */           String[] fileVal = paramFile.getName().split(" ");
/* 561 */           String[] paramVal = fileVal[1].split("\\.");
/* 562 */           if (paramVal[0].equals("Parameters"))
/*     */           {
/* 564 */             String strategy = fileVal[0];
/*     */             
/*     */ 
/* 567 */             ArrayList<String> scripListDateList = new ArrayList();
/* 568 */             String scripParameterPath = basePath + "/Parameters/" + strategy + " ScripListDateMap.csv";
/* 569 */             if (new File(scripParameterPath).exists()) {
/* 570 */               CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
/*     */               String[] tsLine;
/* 572 */               while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/* 573 */                 scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
/*     */               }
/*     */             }
/*     */             
/* 577 */             String paramPath = basePath + "/Parameters/" + strategy + " Parameters.csv";
/* 578 */             String paramKey = "";
/* 579 */             if (new File(paramPath).exists())
/*     */             {
/* 581 */               CSVReader reader = new CSVReader(paramPath, ',', 0);
/*     */               String[] tsLine;
/* 583 */               while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/* 584 */                 if (paramKey.equals("")) {
/* 585 */                   paramKey = tsLine[1];
/*     */                 } else {
/* 587 */                   paramKey = paramKey + "$" + tsLine[1];
/*     */                 }
/*     */               }
/*     */             }
/*     */             String primaryKey;
/*     */             String primaryKey;
/* 593 */             if (!key.equals("")) {
/* 594 */               primaryKey = key + " " + strategy + " " + paramKey;
/*     */             } else
/* 596 */               primaryKey = strategy + " " + paramKey;
/* 597 */             HashMap<String, HashMap<String, String>> scripListDateSet = (HashMap)outputMap.get(primaryKey);
/*     */             
/* 599 */             if (scripListDateSet == null) {
/* 600 */               scripListDateSet = new HashMap();
/*     */             }
/*     */             
/* 603 */             for (String scripListDate : scripListDateList)
/*     */             {
/* 605 */               String[] scripListDateVal = scripListDate.split(",");
/* 606 */               String scripListID = scripListDateVal[0];
/* 607 */               String dates = scripListDateVal[1] + " " + scripListDateVal[2];
/*     */               
/*     */ 
/* 610 */               String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
/* 611 */               CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*     */               
/* 613 */               boolean fileError = false;
/* 614 */               String scripKey = scripListID;
/* 615 */               String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/* 616 */                 String scripID = scripLine[0];
/*     */                 
/* 618 */                 String mtmPath = basePath + "/MTM Data/" + strategy + " " + scripListID + "/" + scripID + 
/* 619 */                   " MTM.csv";
/* 620 */                 String tradePath = basePath + "/Trade Data/" + strategy + " " + scripListID + "/" + scripID + 
/* 621 */                   " Tradebook.csv";
/*     */                 
/* 623 */                 if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/* 624 */                   fileError = true;
/* 625 */                   break;
/*     */                 }
/* 627 */                 scripKey = scripKey + "|" + scripID;
/*     */               }
/*     */               
/*     */ 
/* 631 */               if (!fileError)
/*     */               {
/*     */ 
/*     */ 
/* 635 */                 if (scripListDateSet.containsKey(scripKey)) {
/* 636 */                   HashMap<String, String> newDate = (HashMap)scripListDateSet.get(scripKey);
/* 637 */                   newDate.put(dates, curTimestamp);
/* 638 */                   scripListDateSet.put(scripKey, newDate);
/*     */                 } else {
/* 640 */                   HashMap<String, String> newDate = new HashMap();
/* 641 */                   newDate.put(dates, curTimestamp);
/* 642 */                   scripListDateSet.put(scripKey, newDate);
/*     */                 } }
/*     */             }
/* 645 */             if (!scripListDateSet.isEmpty())
/* 646 */               outputMap.put(primaryKey, scripListDateSet);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 651 */     return outputMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public HashMap<String, HashMap<String, HashMap<String, String>>> createOutputMap(String checkKey)
/*     */     throws IOException
/*     */   {
/* 658 */     HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
/*     */     
/* 660 */     File outputFile = new File(this.loginParameter.getOutputPath());
/* 661 */     File[] folders = outputFile.listFiles();
/*     */     
/*     */     File[] arrayOfFile1;
/* 664 */     int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
/*     */       
/* 666 */       String curTimestamp = folder.getName();
/* 667 */       String basePath = this.loginParameter.getOutputPath() + "/" + curTimestamp;
/*     */       
/*     */ 
/*     */ 
/* 671 */       String key = "";
/*     */       try {
/* 673 */         Integer.parseInt(curTimestamp);
/*     */       } catch (Exception e) {
/* 675 */         key = curTimestamp.substring(0, 2);
/*     */       }
/*     */       
/*     */ 
/* 679 */       if (checkKey.equals(key))
/*     */       {
/*     */ 
/* 682 */         if (new File(basePath + "/Parameters").exists())
/*     */         {
/*     */ 
/* 685 */           File[] paramFiles = new File(basePath + "/Parameters").listFiles();
/* 686 */           File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
/* 687 */             String[] fileVal = paramFile.getName().split(" ");
/* 688 */             String[] paramVal = fileVal[1].split("\\.");
/* 689 */             if (paramVal[0].equals("Parameters"))
/*     */             {
/* 691 */               String strategy = fileVal[0];
/*     */               
/*     */ 
/* 694 */               ArrayList<String> scripListDateList = new ArrayList();
/* 695 */               String scripParameterPath = basePath + "/Parameters/" + strategy + " ScripListDateMap.csv";
/* 696 */               if (new File(scripParameterPath).exists()) {
/* 697 */                 CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
/*     */                 String[] tsLine;
/* 699 */                 while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/* 700 */                   scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
/*     */                 }
/*     */               }
/*     */               
/* 704 */               String paramPath = basePath + "/Parameters/" + strategy + " Parameters.csv";
/* 705 */               String paramKey = "";
/* 706 */               if (new File(paramPath).exists())
/*     */               {
/* 708 */                 CSVReader reader = new CSVReader(paramPath, ',', 0);
/*     */                 String[] tsLine;
/* 710 */                 while ((tsLine = reader.getLine()) != null) { String[] tsLine;
/* 711 */                   if (paramKey.equals("")) {
/* 712 */                     paramKey = tsLine[1];
/*     */                   } else {
/* 714 */                     paramKey = paramKey + "$" + tsLine[1];
/*     */                   }
/*     */                 }
/*     */               }
/*     */               String primaryKey;
/*     */               String primaryKey;
/* 720 */               if (!key.equals("")) {
/* 721 */                 primaryKey = key + " " + strategy + " " + paramKey;
/*     */               } else {
/* 723 */                 primaryKey = strategy + " " + paramKey;
/*     */               }
/* 725 */               HashMap<String, HashMap<String, String>> scripListDateSet = (HashMap)outputMap.get(primaryKey);
/* 726 */               if (scripListDateSet == null) {
/* 727 */                 scripListDateSet = new HashMap();
/*     */               }
/*     */               
/* 730 */               for (String scripListDate : scripListDateList)
/*     */               {
/* 732 */                 String[] scripListDateVal = scripListDate.split(",");
/* 733 */                 String scripListID = scripListDateVal[0];
/* 734 */                 String dates = scripListDateVal[1] + " " + scripListDateVal[2];
/*     */                 
/*     */ 
/* 737 */                 String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
/* 738 */                 CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
/*     */                 
/* 740 */                 boolean fileError = false;
/* 741 */                 String scripKey = scripListID;
/* 742 */                 String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
/* 743 */                   String scripID = scripLine[0];
/*     */                   
/* 745 */                   String mtmPath = basePath + "/MTM Data/" + strategy + " " + scripListID + "/" + scripID + 
/* 746 */                     " MTM.csv";
/* 747 */                   String tradePath = basePath + "/Trade Data/" + strategy + " " + scripListID + "/" + scripID + 
/* 748 */                     " Tradebook.csv";
/*     */                   
/* 750 */                   if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
/* 751 */                     fileError = true;
/* 752 */                     break;
/*     */                   }
/* 754 */                   scripKey = scripKey + "|" + scripID;
/*     */                 }
/*     */                 
/*     */ 
/* 758 */                 if (!fileError)
/*     */                 {
/*     */ 
/*     */ 
/* 762 */                   if (scripListDateSet.containsKey(scripKey)) {
/* 763 */                     HashMap<String, String> newDate = (HashMap)scripListDateSet.get(scripKey);
/* 764 */                     newDate.put(dates, curTimestamp);
/* 765 */                     scripListDateSet.put(scripKey, newDate);
/*     */                   } else {
/* 767 */                     HashMap<String, String> newDate = new HashMap();
/* 768 */                     newDate.put(dates, curTimestamp);
/* 769 */                     scripListDateSet.put(scripKey, newDate);
/*     */                   } }
/*     */               }
/* 772 */               if (!scripListDateSet.isEmpty())
/* 773 */                 outputMap.put(primaryKey, scripListDateSet);
/*     */             }
/*     */           }
/*     */         } }
/*     */     }
/* 778 */     return outputMap;
/*     */   }
/*     */   
/*     */   public void displayRunParameters(String timeStamp) throws IOException
/*     */   {
/* 783 */     this.btGuiObj.lRunPanel.displayRunParameters(timeStamp);
/*     */   }
/*     */   
/*     */   public void updateResultDriver(String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
/*     */     throws Exception
/*     */   {
/* 789 */     this.resultDriver = new ResultDriver(this, timeStamp, postProcessMode, aggregationMode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void displayResults(PostProcess ppObj)
/*     */   {
/* 796 */     if (this.btGuiObj != null) {
/* 797 */       this.btGuiObj.resultsPanel.displayResults(ppObj);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateRollingAnalysisDriver(String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
/*     */     throws Exception
/*     */   {
/* 804 */     this.rollingAnalysisDriver = new RollingAnalysisDriver(this, timeStamp, postProcessMode, aggregationMode);
/*     */   }
/*     */   
/*     */   public void updateIsOsDriver(PostProcessMode postProcessMode, AggregationMode aggregationMode)
/*     */     throws Exception
/*     */   {
/* 810 */     this.isOsDriver = new OOSAnalysisDriver(this, postProcessMode, aggregationMode);
/*     */   }
/*     */   
/*     */ 
/*     */   public void displayResults(HashMap<Long, HashMap<String, Double>> resultMaps)
/*     */   {
/* 816 */     if (this.btGuiObj != null) {
/* 817 */       this.btGuiObj.rollingAnalysisPanel.displayResults(resultMaps, "Sharpe Ratio");
/*     */     }
/*     */   }
/*     */   
/*     */   public void displayResults(ArrayList<HashMap<String, Double>> resultMapsIS, ArrayList<HashMap<String, Double>> resultMapsOS)
/*     */   {
/* 823 */     if (this.btGuiObj != null) {
/* 824 */       this.btGuiObj.isOsPanel.displayResults(resultMapsIS, resultMapsOS, "Sharpe Ratio");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterGlobal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */