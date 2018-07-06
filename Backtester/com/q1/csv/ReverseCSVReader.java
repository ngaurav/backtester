package com.q1.csv;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.commons.io.input.ReversedLinesFileReader;





public class ReverseCSVReader
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  ReversedLinesFileReader reader;
  char splitChar;
  
  public ReverseCSVReader(String fileName, char splitChar, int startIdx)
    throws IOException
  {
    this.reader = new ReversedLinesFileReader(new File(fileName));
    this.splitChar = splitChar;
    for (int i = 0; i < startIdx; i++) {
      this.reader.readLine();
    }
  }
  
  public ArrayList<String> readLine() throws IOException {
    String line = this.reader.readLine();
    if (line == null)
      return null;
    return split(line, this.splitChar);
  }
  
  public String[] readLineAsArray() throws IOException
  {
    String line = this.reader.readLine();
    if (line == null)
      return null;
    return splitAsArray(line, this.splitChar);
  }
  
  public String readLineAsString() throws IOException
  {
    String line = this.reader.readLine();
    return line;
  }
  
  public ArrayList<String[]> readAll()
    throws IOException
  {
    ArrayList<String[]> allLines = new ArrayList();
    String[] line; while ((line = readLineAsArray()) != null) { String[] line;
      allLines.add(line); }
    return allLines;
  }
  
  public void close() throws IOException
  {
    this.reader.close();
  }
  
  public static ArrayList<String> split(String line, char splitChar)
  {
    ArrayList<String> str = new ArrayList();
    int index = line.indexOf(splitChar);
    int prevIndex = index;
    if (index >= 0)
      str.add(line.substring(0, index));
    for (;;) {
      index = line.indexOf(splitChar, index + 1);
      if (index < 0)
        break;
      str.add(line.substring(prevIndex + 1, index));
      prevIndex = index;
    }
    str.add(line.substring(prevIndex + 1));
    return str;
  }
  
  public static String[] splitAsArray(String line, char splitChar)
  {
    ArrayList<String> str = new ArrayList();
    int index = line.indexOf(splitChar);
    int prevIndex = index;
    if (index >= 0)
      str.add(line.substring(0, index));
    for (;;) {
      index = line.indexOf(splitChar, index + 1);
      if (index < 0)
        break;
      str.add(line.substring(prevIndex + 1, index));
      prevIndex = index;
    }
    str.add(line.substring(prevIndex + 1));
    
    String[] outArray = new String[str.size()];
    for (int i = 0; i < str.size(); i++) {
      outArray[i] = ((String)str.get(i));
    }
    return outArray;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/csv/ReverseCSVReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */