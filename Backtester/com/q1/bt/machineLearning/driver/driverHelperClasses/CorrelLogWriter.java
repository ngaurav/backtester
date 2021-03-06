package com.q1.bt.machineLearning.driver.driverHelperClasses;

import com.q1.csv.CSVWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;




public class CorrelLogWriter
{
  CSVWriter correlLogWriter;
  String mlPath;
  
  public CorrelLogWriter(String mlPath)
  {
    this.mlPath = mlPath;
  }
  
  public void writeCorrelLog(TreeMap<Long, HashMap<String, HashMap<String, Double>>> correlVals, ArrayList<String> scripList)
    throws IOException
  {
    this.correlLogWriter = new CSVWriter(this.mlPath + "\\DailyCorrelLog" + ".csv", false, ",");
    this.correlLogWriter.write("Date");
    Iterator localIterator2; for (Iterator localIterator1 = scripList.iterator(); localIterator1.hasNext(); 
        
        localIterator2.hasNext())
    {
      String scrip1 = (String)localIterator1.next();
      
      localIterator2 = scripList.iterator(); continue;String scrip2 = (String)localIterator2.next();
      this.correlLogWriter.write("," + scrip1);
    }
    
    this.correlLogWriter.write("\n");
    
    for (localIterator1 = scripList.iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      String scrip1 = (String)localIterator1.next();
      localIterator2 = scripList.iterator(); continue;String scrip2 = (String)localIterator2.next();
      this.correlLogWriter.write("," + scrip2);
    }
    
    this.correlLogWriter.write("\n");
    for (Map.Entry<Long, HashMap<String, HashMap<String, Double>>> entry : correlVals.entrySet()) {
      this.correlLogWriter.write(((Long)entry.getKey()).toString());
      Iterator localIterator3; for (localIterator2 = scripList.iterator(); localIterator2.hasNext(); 
          localIterator3.hasNext())
      {
        String scrip1 = (String)localIterator2.next();
        localIterator3 = scripList.iterator(); continue;String scrip2 = (String)localIterator3.next();
        this.correlLogWriter.write("," + ((HashMap)((HashMap)entry.getValue()).get(scrip1)).get(scrip2));
      }
      
      this.correlLogWriter.write("\n");
    }
  }
  


  public CSVWriter getCorrelLogWriter()
  {
    return this.correlLogWriter;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/CorrelLogWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */