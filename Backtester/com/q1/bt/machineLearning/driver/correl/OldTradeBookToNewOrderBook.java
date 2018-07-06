package com.q1.bt.machineLearning.driver.correl;

import com.q1.csv.CSVReader;
import com.q1.csv.CSVWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class OldTradeBookToNewOrderBook
{
  public static void main(String[] args) throws IOException
  {
    String tradeBookPath = "C:/Q1/ML Correl Management/BT Tradebook Path";
    String orderBookPath = "C:/Q1/ML Correl Management/BT Orderbook Path";
    String dataPath = "E:/Q1/Datalib/DataLib 7.1/CC";
    
    generateOrderBooks(tradeBookPath, orderBookPath, dataPath);
  }
  

  public static void generateOrderBooks(String tradeBookPath, String orderBookPath, String dataPath)
    throws IOException
  {
    File masterTadeBookFolder = new File(tradeBookPath);
    
    File[] arrayOfFile1;
    int j = (arrayOfFile1 = masterTadeBookFolder.listFiles()).length; for (int i = 0; i < j; i++) { File strategyFolder = arrayOfFile1[i];
      
      File[] arrayOfFile2;
      int m = (arrayOfFile2 = strategyFolder.listFiles()).length; for (int k = 0; k < m; k++) { File tradebookFile = arrayOfFile2[k];
        System.out.println("Processing:" + tradebookFile.getName());
        String[] tradebookVal = tradebookFile.getName().split(" ");
        String strategyID = tradebookVal[0];
        String strategyDatatype = strategyID.split("_")[1];
        String scripListID = tradebookVal[1] + "$" + tradebookVal[2] + "$" + tradebookVal[3] + "$" + 
          tradebookVal[4] + "$" + tradebookVal[5];
        String scripID = tradebookVal[1] + " " + tradebookVal[2] + " " + tradebookVal[3] + " " + tradebookVal[4] + 
          " " + tradebookVal[5];
        String dataID = tradebookVal[1] + " " + tradebookVal[2] + " " + tradebookVal[3] + " " + tradebookVal[4] + 
          " " + strategyDatatype;
        
        String orderBookFilePath = orderBookPath + "/" + strategyID + " " + scripListID;
        
        if (!new File(orderBookFilePath).exists()) {
          new File(orderBookFilePath).mkdirs();
        }
        CSVReader tradeReader = new CSVReader(tradebookFile.getAbsolutePath(), ',', 0);
        
        CSVWriter writer = new CSVWriter(orderBookFilePath + "/" + scripID + " OrderBook.csv", false, ",");
        
        CSVReader dataReader = new CSVReader(dataPath + "/" + dataID + ".csv", ',', 0);
        

        Long lastDateTime = Long.valueOf(0L);Long dataDateTime = Long.valueOf(0L);
        boolean datacheck = false;
        String[] tradeLine; while ((tradeLine = tradeReader.getLine()) != null) {
          String[] tradeLine;
          if (!tradeLine[4].equals("ROL"))
          {
            if (tradeLine[4].equals("OPEN")) {
              tradeLine[4] = "STOP";
            } else if (tradeLine[4].equals("SL")) {
              tradeLine[4] = "STOP";
            } else if (tradeLine[4].equals("LIM")) {
              tradeLine[4] = "LIMIT";
            }
            Long tbDateTime = Long.valueOf(Long.parseLong(tradeLine[0]));
            
            if (!dataDateTime.equals(tbDateTime)) {
              do
              {
                String[] dataLine = dataReader.getLine();
                
                if (dataLine == null) {
                  try {
                    tradeReader.close();
                    dataReader.close();
                    writer.close();
                  }
                  catch (Exception localException) {}
                  


                  datacheck = true;
                  break;
                }
                
                lastDateTime = dataDateTime;
                
                if (strategyDatatype.equals("1M")) {
                  dataDateTime = Long.valueOf(Long.parseLong(dataLine[0] + dataLine[1]));
                } else {
                  dataDateTime = Long.valueOf(Long.parseLong(dataLine[0] + "235900"));
                }
              } while (!dataDateTime.equals(tbDateTime));
            }
            
            if (datacheck) {
              datacheck = false;
              break;
            }
            Long orderDateTime = lastDateTime;
            String[] outOrderLine = { orderDateTime.toString(), scripListID, scripID, tradeLine[3], 
              tradeLine[4], tradeLine[5], tradeLine[7] };
            writer.writeLine(outOrderLine);
          }
        }
        try { tradeReader.close();
          dataReader.close();
          writer.close();
        }
        catch (Exception localException1) {}
      }
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/correl/OldTradeBookToNewOrderBook.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */