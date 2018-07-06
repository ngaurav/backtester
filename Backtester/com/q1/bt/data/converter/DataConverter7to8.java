package com.q1.bt.data.converter;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;

public class DataConverter7to8
{
  String inputPath;
  String outputPath;
  
  public DataConverter7to8(String inputPath, String outputPath)
  {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
  }
  
  public void convertData() throws java.io.IOException {
    File folderName = new File(this.inputPath);
    
    File[] fileList = folderName.listFiles();
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = fileList).length; for (int i = 0; i < j; i++) { File file = arrayOfFile1[i];
      
      if (file.toString().substring(file.toString().length() - 3, file.toString().length()).equalsIgnoreCase("csv")) {
        int count = 1;
        
        String date = null;
        

        CSVReader csvReader = new CSVReader(file.toString(), ',', 0);
        CSVWriter csvWriter = new CSVWriter(this.outputPath + "\\ " + file.getName(), false, ",");
        
        String write = "Date,Expiry,Open,High,Low,Close,Volume,OI,Rollover Close,Total Volume,Total OI";
        csvWriter.writeLine(write);
        
        String[] read = csvReader.getLine();
        
        write = "Start Date|" + read[0];
        
        while ((read = csvReader.getLine()) != null) {
          count++;
          date = read[0];
        }
        csvReader.close();
        
        write = write + ",End Date|" + date + "," + "Data Count|" + count;
        csvWriter.writeLine(write);
        csvReader = new CSVReader(file.toString(), ',', 0);
        
        while ((read = csvReader.getLine()) != null) {
          write = 
            read[0] + "," + read[1] + "," + read[2] + "," + read[3] + "," + read[4] + "," + read[5] + "," + read[6] + "," + read[7];
          
          if (Long.parseLong(read[8]) == 19000131L) {
            write = write + "," + "-1";
          } else {
            write = write + "," + read[12];
          }
          
          Double sum = Double.valueOf(Double.parseDouble(read[6]) + Double.parseDouble(read[13]));
          write = write + "," + sum;
          sum = Double.valueOf(Double.parseDouble(read[7]) + Double.parseDouble(read[14]));
          write = write + "," + sum;
          csvWriter.writeLine(write);
        }
        
        csvWriter.close();
      }
    }
  }
  
  public static void main(String[] args)
    throws java.io.IOException
  {}
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/converter/DataConverter7to8.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */