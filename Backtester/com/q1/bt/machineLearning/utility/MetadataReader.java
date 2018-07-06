package com.q1.bt.machineLearning.utility;

import com.q1.csv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class MetadataReader
{
  CSVReader readerMD;
  Long currentDate;
  String[] curData;
  String[] prevData;
  private String dataPath;
  private ArrayList<String> scripList;
  
  public MetadataReader(String sourcePath, String dataPath, ArrayList<String> scripList)
  {
    this.dataPath = dataPath;
    this.scripList = scripList;
  }
  
  public MetadataReader(String dataPath, String asset) throws Exception {
    this.dataPath = dataPath;
    try {
      this.readerMD = new CSVReader(dataPath, ',', 0);
    } catch (IOException e) {
      System.out.println(dataPath + " not found");
      throw new IOException();
    }
    this.currentDate = Long.valueOf(0L);
    this.curData = null;
    this.prevData = null;
  }
  
  public ArrayList<String> getScripList(String sourcePath) {
    TreeSet<String> scripSet = new TreeSet();
    String path = sourcePath + "/" + "Trade Data";
    File tradeFolder = new File(path);
    File[] tradeFiles = tradeFolder.listFiles();
    File[] arrayOfFile1; int j = (arrayOfFile1 = tradeFiles).length; for (int i = 0; i < j; i++) { File tradeFile = arrayOfFile1[i];
      String[] nameParts = tradeFile.getName().split(" ");
      String assetName = nameParts[1] + " " + 
        nameParts[2] + " " + nameParts[3] + " " + nameParts[4] + 
        " " + nameParts[5];
      scripSet.add(assetName);
    }
    return new ArrayList(scripSet);
  }
  
  public ArrayList<String> getScripList() {
    return this.scripList;
  }
  
  public HashMap<String, TreeMap<Long, ArrayList<Long>>> getMetadataMap()
    throws Exception
  {
    HashMap<String, TreeMap<Long, ArrayList<Long>>> scripwiseMetadataMap = new HashMap();
    for (String asset : this.scripList) {
      TreeMap<Long, ArrayList<Long>> scripDatewiseMD = new TreeMap();
      String assetDataPath = this.dataPath + "\\MD\\" + asset + " " + 
        "MD.csv";
      
      CSVReader reader = new CSVReader(assetDataPath, ',', 0);
      
      String[] metaDataLine;
      while ((metaDataLine = reader.getLine()) != null) { String[] metaDataLine;
        Long date = Long.valueOf(Long.parseLong(metaDataLine[0]));
        ArrayList<Long> data = new ArrayList();
        for (int i = 5; i <= 12; i++)
          data.add(Long.valueOf(Long.parseLong(metaDataLine[i])));
        scripDatewiseMD.put(date, data);
      }
      
      scripwiseMetadataMap.put(asset, scripDatewiseMD);
    }
    
    return scripwiseMetadataMap;
  }
  
  public void process(Long dataDate, MetaData md) throws IOException
  {
    if (this.curData == null) {
      this.curData = this.readerMD.getLine();
      if (this.curData == null)
      {
        return;
      }
      this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
    }
    if (this.currentDate.longValue() > dataDate.longValue())
      return;
    if (this.currentDate.longValue() == dataDate.longValue()) {
      assignData(dataDate, md);
      return;
    }
    while ((this.curData = this.readerMD.getLine()) != null) {
      this.currentDate = Long.valueOf(Long.parseLong(this.curData[0]));
      if (this.currentDate.longValue() >= dataDate.longValue())
      {
        if (this.currentDate.longValue() == dataDate.longValue()) {
          assignData(dataDate, md);
          return;
        }
        return;
      }
    }
  }
  
  private void assignData(Long dataDate, MetaData md)
  {
    md.updateData(this.currentDate, Double.valueOf(Double.parseDouble(this.curData[3])), 
      Double.valueOf(Double.parseDouble(this.curData[4])), Long.valueOf(Long.parseLong(this.curData[5])), 
      Long.valueOf(Long.parseLong(this.curData[6])), Long.valueOf(Long.parseLong(this.curData[7])), 
      Long.valueOf(Long.parseLong(this.curData[8])), Long.valueOf(Long.parseLong(this.curData[9])), 
      Long.valueOf(Long.parseLong(this.curData[10])), Long.valueOf(Long.parseLong(this.curData[11])), 
      Long.valueOf(Long.parseLong(this.curData[12])));
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/MetadataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */