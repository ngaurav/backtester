package com.q1.bt.data;

import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.FundaData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.data.classes.PreProcessData;
import com.q1.bt.data.classes.Scrip;
import com.q1.bt.global.BacktesterGlobal;
import com.q1.bt.process.parameter.LoginParameter;
import com.q1.sql.SQLdata;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class DataTypeViewer
{
  public HashMap<String, ScripDataViewer> scripDataViewerMap = new HashMap();
  
  public String dataType;
  
  public int scripCount;
  public Long dateTime;
  public Long date;
  public Long time;
  public boolean eof = false;
  public boolean skipBacktest = false;
  


  public DataTypeViewer(BacktesterGlobal btGlobal, String dataType, ArrayList<Scrip> scripSet, String strategyDataType)
    throws ClassNotFoundException, SQLException
  {
    this.scripCount = scripSet.size();
    

    this.dataType = dataType;
    

    for (Scrip scrip : scripSet)
    {

      if (dataType.equals("FD"))
      {

        FundaData fD = new FundaData(scrip.segmentName);
        

        ScripDataViewer sDViewer = new ScripDataViewer(dataType, fD);
        this.scripDataViewerMap.put(scrip.scripID, sDViewer);






      }
      else if (dataType.equals("PP"))
      {

        PreProcessData pD = new PreProcessData(scrip);
        

        ScripDataViewer sDViewer = new ScripDataViewer(dataType, pD);
        this.scripDataViewerMap.put(scrip.scripID, sDViewer);


      }
      else if (dataType.equals("MD"))
      {

        SQLdata sqlObject = null;
        sqlObject = new SQLdata(btGlobal.loginParameter.getSqlIPAddress(), 
          btGlobal.loginParameter.getSqlDatabase(), btGlobal.loginParameter.getSqlUsername(), 
          btGlobal.loginParameter.getSqlPassword());
        

        MetaData mD = new MetaData(scrip);
        
        try
        {
          mD.readMetaDataFromDatabase(sqlObject, strategyDataType);
        } catch (SQLException e) {
          e.printStackTrace();
          sqlObject.close();
          return;
        }
        

        ScripDataViewer sDViewer = new ScripDataViewer(dataType, mD);
        this.scripDataViewerMap.put(scrip.scripID, sDViewer);

      }
      else
      {
        ContractData cD = new ContractData(scrip);
        

        ScripDataViewer sDViewer = new ScripDataViewer(dataType, cD);
        this.scripDataViewerMap.put(scrip.scripID, sDViewer);
      }
    }
    



    this.dateTime = Long.valueOf(20910101235900L);
    this.date = Long.valueOf(0L);
    this.time = Long.valueOf(0L);
  }
  


  public void updateData(DataTypeHandler dtHandler)
  {
    this.dateTime = dtHandler.dateTime;
    this.date = Long.valueOf(this.dateTime.longValue() / 1000000L);
    this.time = Long.valueOf(this.dateTime.longValue() - this.date.longValue() * 1000000L);
    

    for (Map.Entry<String, ScripDataViewer> entry : this.scripDataViewerMap.entrySet()) {
      String scripID = (String)entry.getKey();
      ScripDataViewer sDViewer = (ScripDataViewer)entry.getValue();
      ScripDataHandler sDHandler = (ScripDataHandler)dtHandler.scripDataHandlerMap.get(scripID);
      

      if (sDHandler.dateTime.equals(dtHandler.dateTime)) {
        sDViewer.updateData(sDHandler);
      }
    }
    this.skipBacktest = false;
  }
  



  public void updateData(Long dateTime)
  {
    this.dateTime = dateTime;
    

    this.skipBacktest = true;
  }
  

  public void updateEOF(boolean eof)
  {
    this.eof = eof;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/DataTypeViewer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */