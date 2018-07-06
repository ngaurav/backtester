package com.q1.bt.preprocess;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.rosuda.JRI.Rengine;








public class PreProcess
{
  public void runPreprocessor(String dataPath, String libPath, Boolean append, Boolean overwrite, String mainPath, PreprocessVarList preProcessVarType, HashSet<String> assetList)
    throws Exception
  {
    preProcessVarType.getPreprocessList();
    String removeListFile = dataPath + "/PP/removeList.tmp";
    String addListFile = dataPath + "/PP/addList.tmp";
    
    new File(dataPath + "/PP").mkdirs();
    HashMap<String, ArrayList<String>> assetPPMap = getAssetPPMap(dataPath);
    
    CSVWriter addListWriter = new CSVWriter(addListFile, false, ",");
    CSVWriter removeListWriter = new CSVWriter(removeListFile, false, ",");
    
    ArrayList<String> preProcessVarList = preProcessVarType.getNames();
    if ((append.booleanValue()) && (overwrite.booleanValue())) {
      for (String scrip : assetList) {
        ArrayList<String> removeList = new ArrayList();
        removeList.add(scrip);
        if (assetPPMap.size() != 0) {
          for (String preProcessItem : preProcessVarList) {
            if (((ArrayList)assetPPMap.get(scrip)).contains(preProcessItem)) {
              removeList.add(preProcessItem);
            }
          }
        }
        removeListWriter.writeLine(removeList);
        addListWriter.write(scrip + ",");
        addListWriter.writeLine(preProcessVarList);
      }
      
    }
    else {
      for (String scrip : assetList) {
        ArrayList<String> addList = new ArrayList();
        addList.add(scrip);
        for (String preProcessItem : preProcessVarList) {
          if ((assetPPMap.size() == 0) || (!append.booleanValue()) || (assetPPMap.get(scrip) == null) || 
            (!((ArrayList)assetPPMap.get(scrip)).contains(preProcessItem))) {
            addList.add(preProcessItem);
          }
        }
        addListWriter.writeLine(addList);
      }
    }
    
    removeListWriter.close();
    addListWriter.close();
    
    String driverPath = mainPath + "/lib/Pre Process/PreProcessDriver.R";
    
    String[] newargs1 = { "--no-save" };
    Rengine engine = Rengine.getMainEngine();
    if (engine == null) {
      engine = new Rengine(newargs1, false, null);
    }
    
    String packagePath = mainPath + 
      "/src/" + 
      ((PreprocessAlgo)preProcessVarType.preprocessVarlist.get(0)).getModelPackage()
      .replace('.', '/');
    
    String algoCommand = "preProcess(\"" + dataPath + "\",\"" + packagePath + 
      "\"," + (append.booleanValue() ? 1 : 0) + ")";
    System.out.println("source(\"" + driverPath + "\")");
    System.out.println(algoCommand);
    
    engine.eval("source(\"" + driverPath + "\")");
    engine.eval(algoCommand);
    engine.end();
  }
  
  private HashMap<String, ArrayList<String>> getAssetPPMap(String dataPath)
    throws IOException
  {
    HashMap<String, ArrayList<String>> assetPPMap = new HashMap();
    File folder = new File(dataPath + "//PP");
    File[] fileList = folder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = fileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
      String fileName = file.getName();
      if (fileName.substring(fileName.length() - 3).equals("csv")) {
        String scrip = fileName.substring(0, fileName.length() - 7);
        CSVReader fileReader = new CSVReader(file.getPath(), ',', 0);
        String[] line = fileReader.getLine();
        ArrayList<String> ppList = new ArrayList();
        if (line != null) {
          for (int i = 1; i < line.length; i++) {
            ppList.add(line[i]);
          }
        }
        assetPPMap.put(scrip, ppList);
      }
    }
    return assetPPMap;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */