package com.q1.bt.machineLearning.utility;

import com.q1.csv.CSVWriter;
import java.io.IOException;

public class PostProcessDataWriter
{
  CSVWriter writerPPD;
  com.q1.csv.CSVReader readerPPD;
  Long currentTS;
  String[] inData = null;
  
  public PostProcessDataWriter(String source, String destination) throws IOException {
    try {
      this.writerPPD = new CSVWriter(destination, false, ",");
      this.readerPPD = new com.q1.csv.CSVReader(source, ',', 0);
      this.inData = this.readerPPD.getLine();
      this.writerPPD.writeLine(this.inData);
    }
    catch (IOException e) {
      System.out.println(source + " not found");
      throw new IOException();
    }
    this.currentTS = Long.valueOf(0L);
  }
  
  public void process(Long resultTS, int write) throws IOException
  {
    if ((this.currentTS.longValue() > resultTS.longValue()) || (resultTS.longValue() == 0L)) {
      return;
    }
    if (this.currentTS.longValue() == resultTS.longValue()) {
      if (write == 0) {
        this.inData[6] = "0";
        this.inData[7] = "0";
        this.inData[8] = Double.toString(NaN.0D);
        this.inData[9] = Double.toString(NaN.0D);
      }
      this.writerPPD.writeLine(this.inData);
    }
    while ((this.inData = this.readerPPD.getLine()) != null) {
      this.currentTS = Long.valueOf(Long.parseLong(this.inData[0]) / 1000000L);
      if (this.currentTS.longValue() == resultTS.longValue()) {
        if (write == 0) {
          this.inData[6] = "0";
          this.inData[7] = "0";
          
          this.inData[8] = Double.toString(NaN.0D);
          this.inData[9] = Double.toString(NaN.0D);
        }
        this.writerPPD.writeLine(this.inData);
      } else { if (resultTS.longValue() <= this.currentTS.longValue()) {
          break;
        }
      }
    }
  }
  
  public void close() throws IOException
  {
    this.writerPPD.close();
    this.readerPPD.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/PostProcessDataWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */