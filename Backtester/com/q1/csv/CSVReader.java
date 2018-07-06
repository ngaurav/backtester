package com.q1.csv;

import com.q1.math.MathLib;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class CSVReader
{
  BufferedReader reader;
  char splitChar;
  private static int defaultCharBufferSize = 8192;
  
  String[] dataLine;
  String[] nextDataLine;
  String dataLineString;
  boolean wait = false;
  
  public CSVReader(String fileName, char splitChar, int startIdx) throws IOException
  {
    this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
    this.splitChar = splitChar;
    for (int i = 0; i <= startIdx; i++) {
      String line = this.reader.readLine();
      if (line == null) {
        this.dataLine = null;
        this.dataLineString = null;
        break;
      }
      this.dataLine = MathLib.splitAsArray(line, ',');
      this.dataLineString = line;
    }
  }
  
  public CSVReader(String fileName, char splitChar, long startDate)
    throws IOException
  {
    this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
    this.splitChar = splitChar;
    String prevDateStr = "";
    for (;;)
    {
      String line = this.reader.readLine();
      String curDateStr = line.substring(0, 8);
      if (!curDateStr.equals(prevDateStr)) {
        long curDate = Long.parseLong(curDateStr);
        if (curDate >= startDate) {
          this.dataLine = MathLib.splitAsArray(line, ',');
          break;
        }
        prevDateStr = curDateStr;
      }
    }
  }
  
  public CSVReader(String fileName, char splitChar, long startDate, int skipLines) throws IOException
  {
    this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
    this.splitChar = splitChar;
    String prevDateStr = "";
    

    for (int i = 0; i < skipLines; i++) {
      this.reader.readLine();
    }
    for (;;) {
      String line = this.reader.readLine();
      String curDateStr = line.substring(0, 8);
      if (!curDateStr.equals(prevDateStr)) {
        long curDate = Long.parseLong(curDateStr);
        if (curDate >= startDate) {
          this.dataLine = MathLib.splitAsArray(line, ',');
          break;
        }
        prevDateStr = curDateStr;
      }
    }
  }
  

  public CSVReader(String fileName, char splitChar, long startDate, boolean lastLineCheck, int skipLines)
    throws IOException
  {
    if (!lastLineCheck) {
      return;
    }
    this.reader = new BufferedReader(new FileReader(fileName), defaultCharBufferSize);
    this.splitChar = splitChar;
    String prevDateStr = "";
    

    for (int i = 0; i < skipLines; i++) {
      this.reader.readLine();
    }
    String prevLine = null;
    for (;;)
    {
      String line = this.reader.readLine();
      String curDateStr = line.substring(0, 8);
      if (!curDateStr.equals(prevDateStr)) {
        long curDate = Long.parseLong(curDateStr);
        if (curDate >= startDate) {
          if (prevLine == null) {
            this.dataLine = MathLib.splitAsArray(line, ','); break;
          }
          this.dataLine = MathLib.splitAsArray(prevLine, ',');
          this.nextDataLine = MathLib.splitAsArray(line, ',');
          this.wait = true;
          
          break;
        }
        prevDateStr = curDateStr;
      }
      prevLine = line;
    }
  }
  
  public CSVReader(String fileName, char splitChar, int startIdx, int bufferSize) throws IOException
  {
    this.reader = new BufferedReader(new FileReader(fileName), bufferSize);
    this.splitChar = splitChar;
    for (int i = 0; i < startIdx; i++) {
      this.reader.readLine();
    }
  }
  
  public String[] getLastReadLine() throws IOException {
    return this.dataLine;
  }
  
  public String getLastReadDate() throws IOException
  {
    return this.dataLine[0];
  }
  
  public String[] getLastReadDateTime() throws IOException
  {
    String[] dateTime = { this.dataLine[0], this.dataLine[1] };
    return dateTime;
  }
  
  public String[] getNextLine()
    throws IOException
  {
    this.dataLine = readLine();
    

    if (this.dataLine == null) {
      close();
    }
    return this.dataLine;
  }
  
  public String[] getLine()
    throws IOException
  {
    String[] prevDataLine = this.dataLine;
    

    if (!this.wait) {
      this.dataLine = readLine();
    } else {
      this.dataLine = this.nextDataLine;
      this.wait = false;
    }
    

    if (prevDataLine == null) {
      close();
    }
    return prevDataLine;
  }
  
  public String getLineAsString()
    throws IOException
  {
    String prevDataLineString = this.dataLineString;
    
    this.dataLineString = readLineAsString();
    

    if (prevDataLineString == null) {
      close();
    }
    return prevDataLineString;
  }
  
  private String[] readLine() throws IOException
  {
    String line = this.reader.readLine();
    if (line == null)
      return null;
    return MathLib.splitAsArray(line, this.splitChar);
  }
  
  private String readLineAsString() throws IOException
  {
    String line = this.reader.readLine();
    return line;
  }
  
  public ArrayList<String[]> readAll()
    throws IOException
  {
    ArrayList<String[]> allLines = new ArrayList();
    String[] line; while ((line = getLine()) != null) { String[] line;
      allLines.add(line); }
    return allLines;
  }
  
  public void close() throws IOException
  {
    this.reader.close();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/CSVReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */