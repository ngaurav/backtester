package com.q1.bt.machineLearning.driver.driverHelperClasses;

import com.q1.csv.CSVReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class OutputProcessor
{
  private HashMap<String, Boolean> segmentDoneReading;
  private HashMap<Long, HashMap<String, Double>> tempModelOutput;
  private HashMap<String, Long> tempTS;
  private String currentTSsegment;
  private Long prevNextTS = Long.valueOf(Long.MAX_VALUE);
  
  private HashMap<String, Long> firstOutputDateCollector;
  
  boolean rcompleteReading;
  
  Long rwriteTS;
  
  HashMap<String, String[]> tempRDataCollector = new HashMap();
  
  public OutputProcessor() {
    this.segmentDoneReading = new HashMap();
    this.tempModelOutput = new HashMap();
    this.tempTS = new HashMap();
    this.firstOutputDateCollector = new HashMap();
    this.rcompleteReading = false;
    this.rwriteTS = Long.valueOf(0L);
  }
  

  public HashMap<String, Double> processOutput(HashMap<String, CSVReader> readerOutputCollector)
  {
    HashMap<String, Double> modelOutput = new HashMap();
    Long currentTS = null;
    boolean allFilesReadingDone = true;
    for (String segment : readerOutputCollector.keySet())
      if (this.segmentDoneReading.get(segment) == null)
      {
        String[] inData = null;
        

        Long lineTS = Long.valueOf(0L);
        Long segmentTS = Long.valueOf(0L);
        String[] tempRData = (String[])this.tempRDataCollector.get(segment);
        if (tempRData == null) {
          try {
            tempRData = ((CSVReader)readerOutputCollector.get(segment)).getLine();
            this.tempRDataCollector.put(segment, tempRData);
            Long firstOutputDate = Long.valueOf(Long.parseLong(tempRData[0]));
            this.firstOutputDateCollector.put(segment, firstOutputDate);
          }
          catch (IOException e) {
            System.out.println("ML Error: R output is Empty");
            e.printStackTrace();
          }
        }
        segmentTS = Long.valueOf(Long.parseLong(tempRData[0]));
        

        if (currentTS == null) {
          currentTS = segmentTS;
          this.currentTSsegment = segment;
        } else { if (segmentTS.compareTo(currentTS) > 0) {
            continue;
          }
          if (segmentTS.compareTo(currentTS) < 0) {
            if (this.tempTS.get(this.currentTSsegment) == null)
              this.tempTS.put(this.currentTSsegment, currentTS);
            if (((Long)this.tempTS.get(this.currentTSsegment)).equals(currentTS)) {
              if (this.tempModelOutput.get(this.tempTS.get(this.currentTSsegment)) == null) {
                this.tempModelOutput.put((Long)this.tempTS.get(this.currentTSsegment), new HashMap(modelOutput));
              } else {
                ((HashMap)this.tempModelOutput.get(this.tempTS.get(this.currentTSsegment))).putAll(new HashMap(modelOutput));
              }
            }
            currentTS = segmentTS;
            this.currentTSsegment = segment;
            modelOutput.clear();
          }
        }
        
        if (currentTS.equals(segmentTS)) {
          modelOutput.put(tempRData[1], Double.valueOf(tempRData[2]));
        }
        
        if (this.prevNextTS.compareTo(Long.valueOf(Long.parseLong(tempRData[0]))) >= 0)
        {
          if (!this.tempModelOutput.isEmpty()) {
            for (Long tsIterator : this.tempModelOutput.keySet()) {
              if (tsIterator.equals(currentTS)) {
                modelOutput.putAll((Map)this.tempModelOutput.get(tsIterator));
                this.tempModelOutput.remove(tsIterator);
              }
            }
          }
          try
          {
            while ((inData = ((CSVReader)readerOutputCollector.get(segment)).getLine()) != null)
            {
              lineTS = Long.valueOf(Long.parseLong(inData[0]));
              if (lineTS.longValue() == currentTS.longValue()) {
                modelOutput.put(inData[1], Double.valueOf(inData[2]));
              } else {
                this.tempRDataCollector.put(segment, inData);
                this.prevNextTS = Long.valueOf(Long.parseLong(inData[0]));
                if (this.tempTS.get(segment) == null) break;
                this.tempModelOutput.remove(this.tempTS.get(segment));
                this.tempTS.remove(segment);
                

                break;
              }
            }
          }
          catch (NumberFormatException|IOException e) {
            System.out.println("ML Error: Error in reading the R Output file");
            e.printStackTrace();
          }
          if (inData != null) {
            allFilesReadingDone = false;
          } else {
            this.segmentDoneReading.put(segment, Boolean.valueOf(true));
            try {
              ((CSVReader)readerOutputCollector.get(segment)).close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    if (allFilesReadingDone) {
      this.rcompleteReading = true;
    }
    this.rwriteTS = currentTS;
    
    return modelOutput;
  }
  

  public HashMap<String, Long> getFirstOutputDateCollector()
  {
    return this.firstOutputDateCollector;
  }
  

  public boolean isRcompleteReading()
  {
    return this.rcompleteReading;
  }
  

  public Long getRwriteTS()
  {
    return this.rwriteTS;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/OutputProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */