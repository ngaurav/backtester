package com.q1.bt.machineLearning.utility;

import com.q1.csv.CSVReader;
import java.io.IOException;
import java.util.TreeMap;

public class TradeFilteredMTMWriter
{
  com.q1.csv.CSVWriter writerMTM;
  CSVReader readerTD;
  CSVReader readerDD;
  CSVReader readerMTM;
  Long currentDate;
  Long unWrittenDate;
  Double consumedVal;
  Double residualVal;
  String[] inTradeData;
  boolean inTrade;
  private Long entryDate;
  private Long entryTS;
  private Double capital;
  private Long exitDate;
  private double entryPrice;
  private String entrySide;
  private Double positionSize;
  private double exitPrice;
  private String[] inDailyData;
  private TreeMap<Long, Double> dailyMTMMap;
  private Double prevCl;
  private String[] inMTMData;
  private String sourceMTM;
  private Long ddDate;
  private boolean dateFlag;
  private java.util.ArrayList<Long> dateList;
  
  public TradeFilteredMTMWriter(String dataPath, String sourceMTM, String sourceTD, String destination, java.util.ArrayList<Long> dateList) throws IOException
  {
    this.sourceMTM = sourceMTM;
    try
    {
      this.writerMTM = new com.q1.csv.CSVWriter(destination, false, ",");
      this.readerTD = new CSVReader(sourceTD, ',', 0);
      this.readerDD = new CSVReader(dataPath, ',', 2);
    } catch (IOException e) {
      System.out.println("file not found");
      throw new IOException();
    }
    this.inTrade = false;
    this.dailyMTMMap = new TreeMap();
    this.ddDate = Long.valueOf(0L);
    this.exitDate = Long.valueOf(0L);
    this.entryTS = Long.valueOf(0L);
    this.dateList = dateList;
  }
  
  public double process(Long resultTS, int write) throws IOException {
    Double val = Double.valueOf(0.0D);
    
    Double tsMTM = Double.valueOf(0.0D);
    while (this.entryTS.compareTo(resultTS) <= 0) {
      try {
        do {
          if (!this.inTrade) {
            this.capital = Double.valueOf(Double.parseDouble(this.inTradeData[1]));
            this.positionSize = Double.valueOf(Double.parseDouble(this.inTradeData[7]));
            Long dateTime = Long.valueOf(Long.parseLong(this.inTradeData[0]));
            this.entryDate = Long.valueOf(dateTime.longValue() / 1000000L);
            this.entryTS = Long.valueOf(dateTime.longValue() / 1000000L);
            
            this.entryPrice = Double.parseDouble(this.inTradeData[6]);
            this.entrySide = this.inTradeData[3];
            this.inTrade = true;
            if (this.entryDate.equals(this.exitDate)) {
              this.dateFlag = true;
            }
            if (this.entryTS.compareTo(resultTS) > 0) {
              break;
            }
          } else {
            this.exitDate = Long.valueOf(Long.parseLong(this.inTradeData[0]) / 1000000L);
            this.exitPrice = Double.parseDouble(this.inTradeData[6]);
            this.inTrade = false;
            

            if (this.entryTS.compareTo(resultTS) >= 0) {
              break;
            }
          }
        } while ((this.inTradeData = this.readerTD.getLine()) != null);
        




























        if ((this.inTradeData != null) && (this.entryTS.equals(resultTS)) && (!this.inTrade)) {
          do { Double close;
            Double mtm; do { if (this.inDailyData == null) {
                this.inDailyData = this.readerDD.getLine();
              }
              
              if ((this.ddDate.compareTo(this.exitDate) < 0) && (!this.dateFlag)) {
                this.inDailyData = this.readerDD.getLine();
              }
              this.dateFlag = false;
              

              Double rollCl = Double.valueOf(Double.parseDouble(this.inDailyData[8]));
              Double close;
              if (rollCl.doubleValue() > 0.0D) {
                close = rollCl;
              } else {
                close = Double.valueOf(Double.parseDouble(this.inDailyData[5]));
              }
              this.ddDate = Long.valueOf(Long.parseLong(this.inDailyData[0]));
              mtm = Double.valueOf(0.0D);
            } while (this.ddDate.longValue() < this.entryDate.longValue());
            
            if (write == 1) {
              if (this.ddDate.equals(this.entryDate))
              {
                mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (this.entryPrice - close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (close.doubleValue() - this.entryPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
              }
              
              if ((this.ddDate.longValue() > this.entryDate.longValue()) && (this.ddDate.longValue() <= this.exitDate.longValue()))
              {
                mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-close.doubleValue() + this.prevCl.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-this.prevCl.doubleValue() + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue()));
              }
              if (this.ddDate.equals(this.exitDate))
              {
                mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-this.exitPrice + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-close.doubleValue() + this.exitPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
              }
            }
            
            tsMTM = Double.valueOf(tsMTM.doubleValue() + mtm.doubleValue());
            
            if (this.dailyMTMMap.get(this.ddDate) == null) {
              this.dailyMTMMap.put(this.ddDate, mtm);
            } else {
              this.dailyMTMMap.put(this.ddDate, Double.valueOf(((Double)this.dailyMTMMap.get(this.ddDate)).doubleValue() + mtm.doubleValue()));
            }
            this.prevCl = close;
          }
          while (!this.ddDate.equals(this.exitDate));
        }
        


        Long endDate = (Long)this.dateList.get(this.dateList.size() - 1);
        
        if (this.inTradeData == null) {
          if ((!this.entryTS.equals(resultTS)) || (!this.inTrade)) break;
          while (this.ddDate.compareTo(endDate) <= 0) {
            if (this.inDailyData == null) {
              this.inDailyData = this.readerDD.getLine();
            }
            
            if ((this.ddDate.compareTo(endDate) <= 0) && (!this.dateFlag)) {
              this.inDailyData = this.readerDD.getLine();
            }
            
            if (this.inDailyData == null) {
              break;
            }
            this.dateFlag = false;
            


            Double rollCl = Double.valueOf(Double.parseDouble(this.inDailyData[8]));
            Double close;
            Double close; if (rollCl.doubleValue() > 0.0D) {
              close = rollCl;
            } else {
              close = Double.valueOf(Double.parseDouble(this.inDailyData[5]));
            }
            this.ddDate = Long.valueOf(Long.parseLong(this.inDailyData[0]));
            Double mtm = Double.valueOf(0.0D);
            if (this.ddDate.longValue() >= this.entryDate.longValue())
            {
              if (write == 1) {
                if (this.ddDate.equals(this.entryDate))
                {
                  mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (this.entryPrice - close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (close.doubleValue() - this.entryPrice) * this.positionSize.doubleValue() / this.capital.doubleValue()));
                }
                
                if ((this.ddDate.longValue() > this.entryDate.longValue()) && (this.ddDate.longValue() <= endDate.longValue()))
                {
                  mtm = Double.valueOf(mtm.doubleValue() + (this.entrySide.equals("SELL") ? (-close.doubleValue() + this.prevCl.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue() : (-this.prevCl.doubleValue() + close.doubleValue()) * this.positionSize.doubleValue() / this.capital.doubleValue()));
                }
              }
              
              tsMTM = Double.valueOf(tsMTM.doubleValue() + mtm.doubleValue());
              
              if (this.dailyMTMMap.get(this.ddDate) == null) {
                this.dailyMTMMap.put(this.ddDate, mtm);
              } else {
                this.dailyMTMMap.put(this.ddDate, Double.valueOf(((Double)this.dailyMTMMap.get(this.ddDate)).doubleValue() + mtm.doubleValue()));
              }
              this.prevCl = close;
            }
          }
        }
      }
      catch (Exception e) {}
    }
    





    return val.doubleValue();
  }
  
  public TreeMap<Long, Double> writeInFile(Long firstOutputDate) throws IOException {
    this.readerMTM = new CSVReader(this.sourceMTM, ',', 0);
    while ((this.inMTMData = this.readerMTM.getLine()) != null) {
      Long date = Long.valueOf(Long.parseLong(this.inMTMData[0]));
      
      if (date.compareTo(firstOutputDate) >= 0)
      {

        String[] toWriteLine = new String[2];
        toWriteLine[0] = this.inMTMData[0];
        if (this.dailyMTMMap.get(date) != null) {
          toWriteLine[1] = ((Double)this.dailyMTMMap.get(date)).toString();
        } else {
          toWriteLine[1] = "0.0";
          this.dailyMTMMap.put(date, Double.valueOf(0.0D));
        }
        


        this.writerMTM.writeLine(toWriteLine);
      } }
    return this.dailyMTMMap;
  }
  
  public void close() throws IOException {
    this.writerMTM.close();
    this.readerTD.close();
    this.readerDD.close();
    if (this.readerMTM != null) {
      this.readerMTM.close();
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/TradeFilteredMTMWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */