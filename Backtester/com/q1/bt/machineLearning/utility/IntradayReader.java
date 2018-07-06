package com.q1.bt.machineLearning.utility;

import com.q1.csv.CSVReader;
import java.io.IOException;
import java.io.PrintStream;

public class IntradayReader
{
  CSVReader reader1D;
  CSVReader reader1M;
  CSVReader readerMD;
  Long currentDate;
  Long mdDate;
  Long currentTime;
  String[] curDailyData;
  String[] curMinData;
  String[] curMetaData;
  String[] prevMinData;
  
  public IntradayReader(String dSource, String mSource, String mdSource, Long startDate) throws IOException
  {
    try
    {
      this.reader1D = new CSVReader(dSource, ',', 0);
      this.reader1M = new CSVReader(mSource, ',', 0);
      this.readerMD = new CSVReader(mdSource, ',', 0);
    } catch (IOException e) {
      System.out.println("Error in opening 1D/1M/MD files");
    }
    this.currentDate = Long.valueOf(0L);
    this.mdDate = Long.valueOf(0L);
    this.currentTime = Long.valueOf(0L);
    this.curDailyData = null;
    this.curMinData = null;
    this.curMetaData = null;
    this.prevMinData = null;
  }
  

  public void process(Long dataDate, Long datatime, IntradayData id, DailyData dd, MetaData md)
    throws IOException
  {
    if ((this.curDailyData == null) || (this.curMetaData == null)) {
      this.curDailyData = this.reader1D.getLine();
      this.prevMinData = this.reader1M.getLine();
      this.curMetaData = this.readerMD.getLine();
      if (this.curDailyData == null) {
        System.out.println("No data in Daily File");
        return;
      }
      if (this.prevMinData == null) {
        System.out.println("No Minute data in Minute File");
        return;
      }
      if (this.curMetaData == null) {
        System.out.println("No Meta Data in MetaData File");
        return;
      }
    }
    if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) > 0) return;
    if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) == 0) {
      assignData(dataDate, datatime, id, dd, md);
      this.prevMinData = this.curMinData;
      return;
    }
    while (readNextPoint()) {
      this.currentDate = Long.valueOf(Long.parseLong(this.curDailyData[0]));
      this.mdDate = Long.valueOf(Long.parseLong(this.curMetaData[0]));
      this.currentTime = Long.valueOf(Long.parseLong(this.curMinData[1]));
      if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) < 0) {
        this.prevMinData = this.curMinData;
      }
      else {
        if (dateTimeCompare(this.currentDate, this.currentTime, dataDate, datatime) == 0) {
          assignData(dataDate, datatime, id, dd, md);
          this.prevMinData = this.curMinData;
          return;
        }
        return;
      }
    }
  }
  
  private boolean readNextPoint() throws IOException {
    this.curMinData = this.reader1M.getLine();
    if (this.curMinData == null) return false;
    while (Long.parseLong(this.curMinData[0]) > Long.parseLong(this.curDailyData[0])) {
      this.curDailyData = this.reader1D.getLine();
      if (this.curDailyData == null) { return false;
      }
    }
    while (Long.parseLong(this.curMinData[0]) > Long.parseLong(this.curMetaData[0])) {
      this.curMetaData = this.readerMD.getLine();
      if (this.curMetaData == null) return false;
    }
    return true;
  }
  
  private int dateTimeCompare(Long date1, Long time1, Long date2, Long time2)
  {
    if (date1.longValue() > date2.longValue()) return 1;
    if (date1.longValue() < date2.longValue()) return -1;
    if (time1.longValue() == time2.longValue()) return 0;
    if (time1.longValue() > time2.longValue()) return 2;
    return -2;
  }
  
  private void assignData(Long dataDate, Long dataTime, IntradayData id, DailyData dd, MetaData md) {
    try {
      id.updateData(Double.valueOf(Double.parseDouble(this.prevMinData[3])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[4])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[5])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[6])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[7])), 
        dataTime);
      

      Boolean isRoll = Boolean.valueOf(false);
      Double rollOver = Double.valueOf(0.0D);
      
      id.updateData(dd, md, isRoll.booleanValue(), rollOver);
    }
    catch (Exception e)
    {
      Double vol = Double.valueOf(0.0D);
      try {
        vol = Double.valueOf(Double.parseDouble(this.prevMinData[7]));
      }
      catch (Exception localException1) {}
      id.updateData(Double.valueOf(Double.parseDouble(this.prevMinData[3])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[4])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[5])), 
        Double.valueOf(Double.parseDouble(this.prevMinData[6])), 
        vol, 
        dataTime);
      

      Boolean isRoll = Boolean.valueOf(false);
      Double rollOver = Double.valueOf(0.0D);
      id.updateData(dd, md, isRoll.booleanValue(), rollOver);
    }
  }
  


  public void close()
    throws IOException
  {
    this.reader1D.close();
    this.reader1M.close();
    this.readerMD.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/IntradayReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */