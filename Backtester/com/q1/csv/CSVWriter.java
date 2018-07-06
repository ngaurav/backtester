package com.q1.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class CSVWriter
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  BufferedWriter writer;
  String splitChar;
  
  public CSVWriter(String fileName, boolean writeMode, String splitChar)
    throws IOException
  {
    this.writer = new BufferedWriter(new FileWriter(fileName, writeMode));
    this.splitChar = splitChar;
  }
  
  public void writeLine(ArrayList<String> line) throws IOException
  {
    String outLine = (String)line.get(0);
    for (int i = 1; i < line.size(); i++)
      outLine = outLine + this.splitChar + (String)line.get(i);
    this.writer.write(outLine);
    this.writer.write("\n");
  }
  
  public void writeLine(String[] line) throws IOException
  {
    String outLine = line[0];
    for (int i = 1; i < line.length; i++)
      outLine = outLine + this.splitChar + line[i];
    this.writer.write(outLine);
    this.writer.write("\n");
  }
  
  public void writeLine(String line) throws IOException
  {
    this.writer.write(line);
    this.writer.write("\n");
  }
  
  public void write(String str) throws IOException
  {
    this.writer.write(str);
  }
  
  public void close() throws IOException
  {
    this.writer.flush();
    this.writer.close();
  }
  
  public void flush() throws IOException
  {
    this.writer.flush();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/CSVWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */