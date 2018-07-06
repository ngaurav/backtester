package com.q1.bt.global;

import com.q1.bt.data.classes.ScripList;
import com.q1.bt.driver.OOSAnalysisDriver;
import com.q1.bt.driver.ResultDriver;
import com.q1.bt.driver.RollingAnalysisDriver;
import com.q1.bt.driver.backtest.enums.AggregationMode;
import com.q1.bt.gui.main.BacktestPanel;
import com.q1.bt.gui.main.LoginPanel;
import com.q1.bt.gui.main.MachineLearningPanel;
import com.q1.bt.gui.main.OOSAnalysisPanel;
import com.q1.bt.gui.main.PostProcessPanel;
import com.q1.bt.gui.main.ResultsPanel;
import com.q1.bt.gui.main.RollingAnalysisPanel;
import com.q1.bt.postprocess.PostProcess;
import com.q1.bt.process.BacktesterProcess;
import com.q1.bt.process.ProcessFlow;
import com.q1.bt.process.backtest.PostProcessMode;
import com.q1.bt.process.objects.Backtest;
import com.q1.bt.process.objects.MachineLearning;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.bt.process.parameter.PackageParameter;
import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeSet;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BacktesterGlobal
{
  BacktesterGUI btGuiObj = null;
  BacktesterNonGUI btNoGuiObj = null;
  
  public com.q1.bt.driver.BacktestMainDriver btDriver;
  
  public ResultDriver resultDriver;
  
  public RollingAnalysisDriver rollingAnalysisDriver;
  
  public OOSAnalysisDriver isOsDriver;
  
  public PackageParameter packageParameter;
  
  public ProcessFlow processFlow = new ProcessFlow();
  

  public LoginParameter loginParameter;
  
  public boolean isGui;
  
  public Integer progressBar = Integer.valueOf(0);
  
  public BacktesterGlobal(BacktesterGUI btGuiObj, LoginParameter loginParameter, PackageParameter packageParameter)
    throws Exception
  {
    this.btGuiObj = btGuiObj;
    this.packageParameter = packageParameter;
    this.loginParameter = loginParameter;
    this.isGui = true;
  }
  
  public BacktesterGlobal(BacktesterNonGUI btNoGuiObj, LoginParameter loginParameter, PackageParameter packageParameter)
    throws Exception
  {
    this.btNoGuiObj = btNoGuiObj;
    this.packageParameter = packageParameter;
    this.loginParameter = loginParameter;
    this.isGui = false;
  }
  
  public void displayMessage(String message)
  {
    if (this.isGui) {
      this.btGuiObj.sysMsgPanel.displayMessage(message);
    } else {
      System.out.println(message);
    }
  }
  
  public void backButtonAction() {
    this.processFlow.revert();
    shiftTab();
  }
  
  public void shiftTab()
  {
    try {
      setPreviousProgressBar(0);
    }
    catch (Exception localException) {}
    try
    {
      enableRunButton();
    }
    catch (Exception localException1) {}
    
    this.btGuiObj.shiftTab(this.processFlow.getCurrentTabIndex().intValue());
  }
  
  public void shiftTab(BacktesterProcess process)
  {
    this.btGuiObj.shiftTab(this.processFlow.getProcessTabIndex(process).intValue());
  }
  
  public LinkedHashMap<String, ArrayList<String>> getStrategyScripMap(String timeStamp)
  {
    LinkedHashMap<String, ArrayList<String>> ssMap = new LinkedHashMap();
    
    String mtmPath = this.loginParameter.getOutputPath() + "/" + timeStamp + "/MTM Data";
    File mtmPathFile = new File(mtmPath);
    File[] mtmFiles = mtmPathFile.listFiles();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = mtmFiles).length; for (int i = 0; i < j; i++) { File mtmFile = arrayOfFile1[i];
      String[] mtmVal = mtmFile.getName().split(" ");
      String strategy = mtmVal[0];
      String scrip = mtmVal[1] + " " + mtmVal[2] + " " + mtmVal[3] + " " + mtmVal[4] + " " + mtmVal[5];
      ArrayList<String> scrips = (ArrayList)ssMap.get(strategy);
      if (scrips == null) {
        scrips = new ArrayList();
        scrips.add(scrip);
        ssMap.put(strategy, scrips);
      } else {
        scrips.add(scrip);
        ssMap.put(strategy, scrips);
      }
    }
    
    return ssMap;
  }
  
  public LinkedHashMap<String, ArrayList<String>> getStrategyScripMap(String outputPath, String timestamp)
  {
    LinkedHashMap<String, ArrayList<String>> ssMap = new LinkedHashMap();
    
    String mtmPath = outputPath + "/" + timestamp + "/MTM Data";
    File mtmPathFile = new File(mtmPath);
    File[] mtmFiles = mtmPathFile.listFiles();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = mtmFiles).length; for (int i = 0; i < j; i++) { File mtmFile = arrayOfFile1[i];
      String[] mtmVal = mtmFile.getName().split(" ");
      String strategy = mtmVal[0];
      String scrip = mtmVal[1] + " " + mtmVal[2] + " " + mtmVal[3] + " " + mtmVal[4] + " " + mtmVal[5];
      ArrayList<String> scrips = (ArrayList)ssMap.get(strategy);
      if (scrips == null) {
        scrips = new ArrayList();
        scrips.add(scrip);
        ssMap.put(strategy, scrips);
      } else {
        scrips.add(scrip);
        ssMap.put(strategy, scrips);
      }
    }
    
    return ssMap;
  }
  
  /* Error */
  public void copyFile(File source, File dest)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: new 213	java/io/FileInputStream
    //   8: dup
    //   9: aload_1
    //   10: invokespecial 215	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   13: astore_3
    //   14: new 218	java/io/FileOutputStream
    //   17: dup
    //   18: aload_2
    //   19: invokespecial 220	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   22: astore 4
    //   24: sipush 1024
    //   27: newarray <illegal type>
    //   29: astore 5
    //   31: goto +13 -> 44
    //   34: aload 4
    //   36: aload 5
    //   38: iconst_0
    //   39: iload 6
    //   41: invokevirtual 221	java/io/OutputStream:write	([BII)V
    //   44: aload_3
    //   45: aload 5
    //   47: invokevirtual 227	java/io/InputStream:read	([B)I
    //   50: dup
    //   51: istore 6
    //   53: ifgt -19 -> 34
    //   56: goto +17 -> 73
    //   59: astore 7
    //   61: aload_3
    //   62: invokevirtual 233	java/io/InputStream:close	()V
    //   65: aload 4
    //   67: invokevirtual 236	java/io/OutputStream:close	()V
    //   70: aload 7
    //   72: athrow
    //   73: aload_3
    //   74: invokevirtual 233	java/io/InputStream:close	()V
    //   77: aload 4
    //   79: invokevirtual 236	java/io/OutputStream:close	()V
    //   82: return
    // Line number table:
    //   Java source line #171	-> byte code offset #0
    //   Java source line #172	-> byte code offset #2
    //   Java source line #174	-> byte code offset #5
    //   Java source line #175	-> byte code offset #14
    //   Java source line #176	-> byte code offset #24
    //   Java source line #178	-> byte code offset #31
    //   Java source line #179	-> byte code offset #34
    //   Java source line #178	-> byte code offset #44
    //   Java source line #181	-> byte code offset #56
    //   Java source line #182	-> byte code offset #61
    //   Java source line #183	-> byte code offset #65
    //   Java source line #184	-> byte code offset #70
    //   Java source line #182	-> byte code offset #73
    //   Java source line #183	-> byte code offset #77
    //   Java source line #185	-> byte code offset #82
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	83	0	this	BacktesterGlobal
    //   0	83	1	source	File
    //   0	83	2	dest	File
    //   1	73	3	is	java.io.InputStream
    //   3	75	4	os	java.io.OutputStream
    //   29	17	5	buffer	byte[]
    //   34	6	6	length	int
    //   51	3	6	length	int
    //   59	12	7	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   5	59	59	finally
  }
  
  public ArrayList<Long> getConsolDateList(String timestamp)
    throws IOException
  {
    TreeSet<Long> dateSet = new TreeSet();
    

    String mtmFolderPath = this.loginParameter.getOutputPath() + "/" + timestamp + "/MTM Data";
    File mtmFolderFile = new File(mtmFolderPath);
    File[] arrayOfFile1; int j = (arrayOfFile1 = mtmFolderFile.listFiles()).length; for (int i = 0; i < j; i++) { File scripListFolderFile = arrayOfFile1[i];
      File[] arrayOfFile2; int m = (arrayOfFile2 = scripListFolderFile.listFiles()).length; for (int k = 0; k < m; k++) { File scripFile = arrayOfFile2[k];
        String scripMTMPath = scripFile.getAbsolutePath();
        CSVReader reader = new CSVReader(scripMTMPath, ',', 0);
        String[] line;
        while ((line = reader.getLine()) != null) { String[] line;
          dateSet.add(Long.valueOf(Long.parseLong(line[0])));
        }
      }
    }
    
    return new ArrayList(dateSet);
  }
  
  public ArrayList<String> getPathClassFiles(String location)
  {
    File folder = new File(this.loginParameter.getMainPath() + "/src/" + location);
    File[] listOfFiles = folder.listFiles();
    ArrayList<String> classFiles = new ArrayList();
    if (listOfFiles == null)
      return classFiles;
    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        String[] fileVal = listOfFiles[i].getName().split("\\.");
        if (fileVal[1].equalsIgnoreCase("java"))
          classFiles.add(fileVal[0]);
      }
    }
    return classFiles;
  }
  

  public void addPackagetoTable(String packageName, JTable table)
  {
    String location = packageName.replaceAll("\\.", "/");
    ArrayList<String> classes = getPathClassFiles(location);
    

    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model.setRowCount(0);
    
    for (String cl : classes) {
      Object[] op = { cl };
      model.addRow(op);
    }
    
    table.setRowSelectionInterval(0, 0);
  }
  
  public void addPackagetoDataTable(String packageName, JTable table)
    throws Exception
  {
    String location = packageName.replaceAll("\\.", "/");
    ArrayList<String> classes = getPathClassFiles(location);
    

    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model.setRowCount(0);
    

    for (String scripList : classes)
    {

      Class<?> stratClass = Class.forName(packageName + "." + scripList);
      Constructor<?> constructor = stratClass.getConstructor(new Class[0]);
      ScripList scripListObj = (ScripList)constructor.newInstance(new Object[0]);
      
      Object[] op = { scripList, scripListObj.getScripCount(), scripListObj.getAssetClassNames(), 
        scripListObj.getScripNames() };
      model.addRow(op);
    }
    
    table.setRowSelectionInterval(0, 0);
  }
  
  public String getTimeStamp()
  {
    File folder = new File(this.loginParameter.getOutputPath());
    File[] folders = folder.listFiles();
    Integer maxVal = Integer.valueOf(0);
    File[] arrayOfFile1; int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File f = arrayOfFile1[i];
      Integer val = Integer.valueOf(-1);
      try {
        val = Integer.valueOf(Integer.parseInt(f.getName()));
      }
      catch (Exception localException) {}
      
      maxVal = Integer.valueOf(com.q1.math.MathLib.max(val.intValue(), maxVal.intValue()));
    }
    maxVal = Integer.valueOf(maxVal.intValue() + 1);
    return maxVal.toString();
  }
  

  public void moveToTimestamp(String scripID, String strategyName, String existingTS, String btTimeStamp)
    throws IOException
  {
    String outputPath = this.loginParameter.getOutputPath();
    

    String currentPath = outputPath + "/" + existingTS + "/MTM Data/" + strategyName + " " + scripID + " MTM.csv";
    String newPath = outputPath + "/" + btTimeStamp + "/MTM Data/" + strategyName + " " + scripID + " MTM.csv";
    if (!new File(outputPath + "/" + btTimeStamp + "/MTM Data").exists())
      new File(outputPath + "/" + btTimeStamp + "/MTM Data").mkdirs();
    File currentFile = new File(currentPath);
    File newFile = new File(newPath);
    currentFile.renameTo(newFile);
    

    currentPath = outputPath + "/" + existingTS + "/Trade Data/" + strategyName + " " + scripID + " Tradebook.csv";
    newPath = outputPath + "/" + btTimeStamp + "/Trade Data/" + strategyName + " " + scripID + " Tradebook.csv";
    if (!new File(outputPath + "/" + btTimeStamp + "/Trade Data").exists())
      new File(outputPath + "/" + btTimeStamp + "/Trade Data").mkdirs();
    currentFile = new File(currentPath);
    newFile = new File(newPath);
    currentFile.renameTo(newFile);
    

    currentPath = outputPath + "/" + existingTS + "/Post Process Data/" + strategyName + " " + scripID + 
      " Output.csv";
    newPath = outputPath + "/" + btTimeStamp + "/Post Process Data/" + strategyName + " " + scripID + " Output.csv";
    if (!new File(outputPath + "/" + btTimeStamp + "/Post Process Data").exists())
      new File(outputPath + "/" + btTimeStamp + "/Post Process Data").mkdirs();
    currentFile = new File(currentPath);
    newFile = new File(newPath);
    currentFile.renameTo(newFile);
    

    String paramPath = outputPath + "/" + existingTS + "/Parameters/" + strategyName + " ScripList.csv";
    CSVReader reader = new CSVReader(paramPath, ',', 0);
    
    ArrayList<String[]> scrips = new ArrayList();
    String[] inData; while ((inData = reader.getLine()) != null) { String[] inData;
      if (!inData[0].equals(scripID))
        scrips.add(inData); }
    CSVWriter writer = new CSVWriter(paramPath, false, ",");
    for (String[] scrip : scrips)
      writer.writeLine(scrip);
    writer.close();
    if (scrips.isEmpty()) {
      try {
        deleteDir(new File(outputPath + "/" + existingTS));
      }
      catch (Exception localException) {}
    }
  }
  

  public boolean deleteDir(File dir)
  {
    if (dir.isDirectory()) {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++) {
        boolean success = deleteDir(new File(dir, children[i]));
        if (!success) {
          return false;
        }
      }
    }
    return dir.delete();
  }
  

  public String createPath(String folder)
  {
    String path = BacktesterGlobal.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    try
    {
      String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
      String[] acc = currentPath.split("/");
      String outPath = acc[0];
      for (int i = 1; i < acc.length - 1; i++) {
        if (acc[i].equals("lib"))
          break;
        outPath = outPath + "/" + acc[i];
      }
      
      String dataPath = outPath + folder;
      if (!new File(dataPath).exists())
        new File(dataPath).mkdirs();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return createPath(folder); }
    String dataPath;
    String currentPath; return dataPath;
  }
  
  public void clearTables()
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
      this.btGuiObj.backtestPanel.clearTables();
    }
  }
  


  public void initializeProcess()
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Login)) {
      this.btGuiObj.loginPanel.initialize(this.loginParameter);

    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
      this.btGuiObj.backtestPanel.initialize();

    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.SensitivityAnalysis)) {
      this.btGuiObj.sensitivityPanel.initialize();
    }
  }
  



  public void initializeProcess(MachineLearning machineLearning)
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Results)) {
      try {
        this.btGuiObj.resultsPanel.initialize(machineLearning);
      } catch (Exception e) {
        displayMessage("Error initializing Results Pane");
        e.printStackTrace();
      }
    }
  }
  


  public void initializeProcess(Backtest backtest)
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.MachineLearning)) {
      this.btGuiObj.mlPanel.initialize(backtest);


    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.BatchProcess)) {
      this.btGuiObj.batchProcessPanel.initialize(backtest);


    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Results)) {
      try {
        this.btGuiObj.resultsPanel.initialize(backtest);
      } catch (Exception e) {
        displayMessage("Error initializing Results Pane");
        e.printStackTrace();
      }
      
    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.RollingAnalysis)) {
      try {
        this.btGuiObj.rollingAnalysisPanel.initialize(backtest);
      } catch (Exception e) {
        displayMessage("Error initializing Rolling Analysis Pane");
        e.printStackTrace();
      }
      
    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.IsOs)) {
      try {
        this.btGuiObj.isOsPanel.initialize(backtest);
      } catch (Exception e) {
        displayMessage("Error initializing IsOs Analysis Pane");
        e.printStackTrace();
      }
      
    }
    else if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.PostProcess)) {
      try {
        this.btGuiObj.postPanel.initialize(backtest, this.resultDriver);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  



  public void setProgressBar(int val)
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
      if (this.isGui) {
        this.btGuiObj.backtestPanel.setProgressBar(val);
      } else {
        this.progressBar = Integer.valueOf(val);
        System.out.println("Progress: " + this.progressBar + "%");
      }
    }
  }
  



  void setPreviousProgressBar(int val)
  {
    if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.Backtest)) {
      if (this.isGui) {
        this.btGuiObj.backtestPanel.setProgressBar(val);
      } else {
        this.progressBar = Integer.valueOf(val);
      }
    }
  }
  


  public int getProgressBarValue()
  {
    if (this.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
      if (this.isGui) {
        return this.btGuiObj.backtestPanel.getProgressBarValue();
      }
      return this.progressBar.intValue();
    }
    
    return 0;
  }
  


  void enableRunButton()
  {
    if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.Backtest)) {
      this.btGuiObj.backtestPanel.enableRunButton();


    }
    else if (this.processFlow.getPreviousProcess().equals(BacktesterProcess.MachineLearning)) {
      this.btGuiObj.mlPanel.enableRunButton();
    }
  }
  

  public HashMap<String, HashMap<String, HashMap<String, String>>> createOutputMap()
    throws IOException
  {
    HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
    
    File outputFile = new File(this.loginParameter.getOutputPath());
    File[] folders = outputFile.listFiles();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
      
      String curTimestamp = folder.getName();
      String basePath = this.loginParameter.getOutputPath() + "/" + curTimestamp;
      
      String key = "";
      

      try
      {
        Integer.parseInt(curTimestamp);
      }
      catch (Exception e) {
        continue;
      }
      if (new File(basePath + "/Parameters").exists())
      {

        File[] paramFiles = new File(basePath + "/Parameters").listFiles();
        File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
          String[] fileVal = paramFile.getName().split(" ");
          String[] paramVal = fileVal[1].split("\\.");
          if (paramVal[0].equals("Parameters"))
          {
            String strategy = fileVal[0];
            

            ArrayList<String> scripListDateList = new ArrayList();
            String scripParameterPath = basePath + "/Parameters/" + strategy + " ScripListDateMap.csv";
            if (new File(scripParameterPath).exists()) {
              CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
              String[] tsLine;
              while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
              }
            }
            
            String paramPath = basePath + "/Parameters/" + strategy + " Parameters.csv";
            String paramKey = "";
            if (new File(paramPath).exists())
            {
              CSVReader reader = new CSVReader(paramPath, ',', 0);
              String[] tsLine;
              while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                if (paramKey.equals("")) {
                  paramKey = tsLine[1];
                } else {
                  paramKey = paramKey + "$" + tsLine[1];
                }
              }
            }
            String primaryKey;
            String primaryKey;
            if (!key.equals("")) {
              primaryKey = key + " " + strategy + " " + paramKey;
            } else
              primaryKey = strategy + " " + paramKey;
            HashMap<String, HashMap<String, String>> scripListDateSet = (HashMap)outputMap.get(primaryKey);
            
            if (scripListDateSet == null) {
              scripListDateSet = new HashMap();
            }
            
            for (String scripListDate : scripListDateList)
            {
              String[] scripListDateVal = scripListDate.split(",");
              String scripListID = scripListDateVal[0];
              String dates = scripListDateVal[1] + " " + scripListDateVal[2];
              

              String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
              CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
              
              boolean fileError = false;
              String scripKey = scripListID;
              String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
                String scripID = scripLine[0];
                
                String mtmPath = basePath + "/MTM Data/" + strategy + " " + scripListID + "/" + scripID + 
                  " MTM.csv";
                String tradePath = basePath + "/Trade Data/" + strategy + " " + scripListID + "/" + scripID + 
                  " Tradebook.csv";
                
                if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
                  fileError = true;
                  break;
                }
                scripKey = scripKey + "|" + scripID;
              }
              

              if (!fileError)
              {


                if (scripListDateSet.containsKey(scripKey)) {
                  HashMap<String, String> newDate = (HashMap)scripListDateSet.get(scripKey);
                  newDate.put(dates, curTimestamp);
                  scripListDateSet.put(scripKey, newDate);
                } else {
                  HashMap<String, String> newDate = new HashMap();
                  newDate.put(dates, curTimestamp);
                  scripListDateSet.put(scripKey, newDate);
                } }
            }
            if (!scripListDateSet.isEmpty())
              outputMap.put(primaryKey, scripListDateSet);
          }
        }
      }
    }
    return outputMap;
  }
  

  public HashMap<String, HashMap<String, HashMap<String, String>>> createOutputMap(String checkKey)
    throws IOException
  {
    HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
    
    File outputFile = new File(this.loginParameter.getOutputPath());
    File[] folders = outputFile.listFiles();
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = folders).length; for (int i = 0; i < j; i++) { File folder = arrayOfFile1[i];
      
      String curTimestamp = folder.getName();
      String basePath = this.loginParameter.getOutputPath() + "/" + curTimestamp;
      


      String key = "";
      try {
        Integer.parseInt(curTimestamp);
      } catch (Exception e) {
        key = curTimestamp.substring(0, 2);
      }
      

      if (checkKey.equals(key))
      {

        if (new File(basePath + "/Parameters").exists())
        {

          File[] paramFiles = new File(basePath + "/Parameters").listFiles();
          File[] arrayOfFile2; int m = (arrayOfFile2 = paramFiles).length; for (int k = 0; k < m; k++) { File paramFile = arrayOfFile2[k];
            String[] fileVal = paramFile.getName().split(" ");
            String[] paramVal = fileVal[1].split("\\.");
            if (paramVal[0].equals("Parameters"))
            {
              String strategy = fileVal[0];
              

              ArrayList<String> scripListDateList = new ArrayList();
              String scripParameterPath = basePath + "/Parameters/" + strategy + " ScripListDateMap.csv";
              if (new File(scripParameterPath).exists()) {
                CSVReader reader = new CSVReader(scripParameterPath, ',', 0);
                String[] tsLine;
                while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                  scripListDateList.add(tsLine[0] + "," + tsLine[1] + "," + tsLine[2]);
                }
              }
              
              String paramPath = basePath + "/Parameters/" + strategy + " Parameters.csv";
              String paramKey = "";
              if (new File(paramPath).exists())
              {
                CSVReader reader = new CSVReader(paramPath, ',', 0);
                String[] tsLine;
                while ((tsLine = reader.getLine()) != null) { String[] tsLine;
                  if (paramKey.equals("")) {
                    paramKey = tsLine[1];
                  } else {
                    paramKey = paramKey + "$" + tsLine[1];
                  }
                }
              }
              String primaryKey;
              String primaryKey;
              if (!key.equals("")) {
                primaryKey = key + " " + strategy + " " + paramKey;
              } else {
                primaryKey = strategy + " " + paramKey;
              }
              HashMap<String, HashMap<String, String>> scripListDateSet = (HashMap)outputMap.get(primaryKey);
              if (scripListDateSet == null) {
                scripListDateSet = new HashMap();
              }
              
              for (String scripListDate : scripListDateList)
              {
                String[] scripListDateVal = scripListDate.split(",");
                String scripListID = scripListDateVal[0];
                String dates = scripListDateVal[1] + " " + scripListDateVal[2];
                

                String scripFileName = basePath + "/Parameters/" + scripListID + " ScripSet.csv";
                CSVReader scripReader = new CSVReader(scripFileName, ',', 0);
                
                boolean fileError = false;
                String scripKey = scripListID;
                String[] scripLine; while ((scripLine = scripReader.getLine()) != null) { String[] scripLine;
                  String scripID = scripLine[0];
                  
                  String mtmPath = basePath + "/MTM Data/" + strategy + " " + scripListID + "/" + scripID + 
                    " MTM.csv";
                  String tradePath = basePath + "/Trade Data/" + strategy + " " + scripListID + "/" + scripID + 
                    " Tradebook.csv";
                  
                  if ((!new File(mtmPath).exists()) || (!new File(tradePath).exists())) {
                    fileError = true;
                    break;
                  }
                  scripKey = scripKey + "|" + scripID;
                }
                

                if (!fileError)
                {


                  if (scripListDateSet.containsKey(scripKey)) {
                    HashMap<String, String> newDate = (HashMap)scripListDateSet.get(scripKey);
                    newDate.put(dates, curTimestamp);
                    scripListDateSet.put(scripKey, newDate);
                  } else {
                    HashMap<String, String> newDate = new HashMap();
                    newDate.put(dates, curTimestamp);
                    scripListDateSet.put(scripKey, newDate);
                  } }
              }
              if (!scripListDateSet.isEmpty())
                outputMap.put(primaryKey, scripListDateSet);
            }
          }
        } }
    }
    return outputMap;
  }
  
  public void displayRunParameters(String timeStamp) throws IOException
  {
    this.btGuiObj.lRunPanel.displayRunParameters(timeStamp);
  }
  
  public void updateResultDriver(String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
    throws Exception
  {
    this.resultDriver = new ResultDriver(this, timeStamp, postProcessMode, aggregationMode);
  }
  


  public void displayResults(PostProcess ppObj)
  {
    if (this.btGuiObj != null) {
      this.btGuiObj.resultsPanel.displayResults(ppObj);
    }
  }
  
  public void updateRollingAnalysisDriver(String timeStamp, PostProcessMode postProcessMode, AggregationMode aggregationMode)
    throws Exception
  {
    this.rollingAnalysisDriver = new RollingAnalysisDriver(this, timeStamp, postProcessMode, aggregationMode);
  }
  
  public void updateIsOsDriver(PostProcessMode postProcessMode, AggregationMode aggregationMode)
    throws Exception
  {
    this.isOsDriver = new OOSAnalysisDriver(this, postProcessMode, aggregationMode);
  }
  

  public void displayResults(HashMap<Long, HashMap<String, Double>> resultMaps)
  {
    if (this.btGuiObj != null) {
      this.btGuiObj.rollingAnalysisPanel.displayResults(resultMaps, "Sharpe Ratio");
    }
  }
  
  public void displayResults(ArrayList<HashMap<String, Double>> resultMapsIS, ArrayList<HashMap<String, Double>> resultMapsOS)
  {
    if (this.btGuiObj != null) {
      this.btGuiObj.isOsPanel.displayResults(resultMapsIS, resultMapsOS, "Sharpe Ratio");
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/global/BacktesterGlobal.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */