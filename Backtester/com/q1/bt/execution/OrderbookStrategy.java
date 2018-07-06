package com.q1.bt.execution;

import com.q1.bt.data.DataTypeViewer;
import com.q1.bt.execution.order.Order;
import com.q1.bt.execution.order.OrderSide;
import com.q1.bt.execution.order.OrderType;
import com.q1.bt.postprocess.PostProcessData;
import com.q1.csv.CSVReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class OrderbookStrategy
  extends Strategy
{
  String strategyDataType;
  public transient HashMap<String, CSVReader> scripOrderBookReaderMap = new HashMap();
  

  public OrderbookStrategy(String strategyDataType, String orderBookPath)
    throws IOException
  {
    this.strategyDataType = strategyDataType;
    

    File orderBookFile = new File(orderBookPath);
    File[] arrayOfFile; int j = (arrayOfFile = orderBookFile.listFiles()).length; for (int i = 0; i < j; i++) { File orderBook = arrayOfFile[i];
      String scripID = orderBook.getName().substring(0, orderBook.getName().length() - 14);
      CSVReader orderBookReader = new CSVReader(orderBook.getAbsolutePath(), ',', 0);
      this.scripOrderBookReaderMap.put(scripID, orderBookReader);
    }
  }
  



  public ArrayList<Order> processStrategy(HashMap<String, DataTypeViewer> dataMap, HashMap<String, Long> positionMap, HashMap<String, Double> mtmMap, HashMap<String, Double> dayMTMMap, HashMap<String, ArrayList<String[]>> tradeBook, Double capital, Double riskPerTrade)
  {
    ArrayList<Order> orderBook = new ArrayList();
    DataTypeViewer data = (DataTypeViewer)dataMap.get(this.strategyDataType);
    Long curTimestamp = data.dateTime;
    

    ArrayList<String> scripIDList = new ArrayList(data.scripDataViewerMap.keySet());
    

    for (String scripID : scripIDList)
    {
      CSVReader orderBookReader = (CSVReader)this.scripOrderBookReaderMap.get(scripID);
      
      try
      {
        String[] dataLine = orderBookReader.getLastReadLine();
        

        if (dataLine != null)
        {


          Long curOrderTimestamp = Long.valueOf(Long.parseLong(dataLine[0]));
          if (curOrderTimestamp.equals(curTimestamp))
          {

            Order order = new Order(OrderSide.valueOf(dataLine[3]), OrderType.valueOf(dataLine[4]), 
              Double.parseDouble(dataLine[5]), Long.parseLong(dataLine[6]));
            orderBook.add(order);
            

            while ((dataLine = orderBookReader.getNextLine()) != null) {
              curOrderTimestamp = Long.valueOf(Long.parseLong(dataLine[0]));
              if (!curOrderTimestamp.equals(curTimestamp))
                break;
              order = new Order(OrderSide.valueOf(dataLine[3]), OrderType.valueOf(dataLine[4]), 
                Double.parseDouble(dataLine[5]), Long.parseLong(dataLine[6]));
              orderBook.add(order);
            }
          }
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    return orderBook;
  }
  
  public ArrayList<String[]> getParameterList()
  {
    return new ArrayList();
  }
  
  public ArrayList<PostProcessData> getPostProcessData()
  {
    return new ArrayList();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/OrderbookStrategy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */