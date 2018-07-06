package com.q1.bt.data.classes;

import java.util.HashMap;
import java.util.Map.Entry;







public class ContractData
{
  public String exchangeName;
  public String assetclassName;
  public String segmentName;
  public String scripName;
  String[] header;
  HashMap<String, Integer> indexMap;
  public Contract contract = new Contract();
  
  public ContractData(Scrip scrip, String[] header)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
    this.header = header;
    

    createIndexMap(header);
  }
  


  public ContractData(Scrip scrip)
  {
    this.exchangeName = scrip.exchangeName;
    this.assetclassName = scrip.assetClassName;
    this.segmentName = scrip.segmentName;
    this.scripName = scrip.scripName;
  }
  


  public ContractData(ContractData cD)
  {
    this.exchangeName = cD.exchangeName;
    this.assetclassName = cD.assetclassName;
    this.segmentName = cD.segmentName;
    this.scripName = cD.scripName;
    this.header = cD.header;
    
    this.indexMap = new HashMap(cD.indexMap);
    
    this.contract = new Contract(cD.contract);
  }
  

  public void createIndexMap(String[] header)
  {
    this.indexMap = new HashMap();
    

    for (int i = 0; i < header.length; i++) {
      this.indexMap.put(header[i], Integer.valueOf(i));
    }
  }
  

  public void addContractData(String[] dataLine)
    throws Exception
  {
    for (Map.Entry<String, Integer> entry : this.indexMap.entrySet()) {
      String header = (String)entry.getKey();
      Integer index = (Integer)entry.getValue();
      String value = dataLine[index.intValue()];
      if ((!header.equals("Date")) && (!header.equals("Time"))) {
        updateContractVariable(header, value);
      }
    }
  }
  
  public void updateContractVariable(String header, String value)
    throws Exception
  {
    if (header.equals("Expiry")) {
      this.contract.exp = Integer.valueOf(Integer.parseInt(value));

    }
    else if (header.equals("Open")) {
      this.contract.op = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("High")) {
      this.contract.hi = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Low")) {
      this.contract.lo = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Close")) {
      this.contract.cl = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Rollover Close")) {
      this.contract.rolloverCl = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Volume")) {
      this.contract.vol = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("OI")) {
      this.contract.oi = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Total Volume")) {
      this.contract.totalVol = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Total OI")) {
      this.contract.totalOI = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Rollover Expiry")) {
      this.contract.rolloverExp = Integer.valueOf(Integer.parseInt(value));

    }
    else if (header.equals("Original Expiry")) {
      this.contract.actualExp = Integer.valueOf(Integer.parseInt(value));

    }
    else if (header.equals("Currency")) {
      this.contract.currency = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Bid")) {
      this.contract.bid = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("Ask")) {
      this.contract.ask = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("BidQty")) {
      this.contract.bidQty = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("AskQty")) {
      this.contract.askQty = Double.valueOf(Double.parseDouble(value));

    }
    else if (header.equals("OptionType")) {
      this.contract.optionType = value;

    }
    else if (header.equals("OptionStrike")) {
      this.contract.optionStrike = Double.valueOf(Double.parseDouble(value));
    }
    else {
      throw new Exception(this.scripName + " - Unknown column in file: " + header);
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/ContractData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */