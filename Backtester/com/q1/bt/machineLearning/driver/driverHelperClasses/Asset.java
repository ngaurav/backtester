package com.q1.bt.machineLearning.driver.driverHelperClasses;

import com.q1.bt.data.classes.Scrip;

public class Asset {
  private String assetName;
  private String strategyName;
  private String scripListName;
  private Scrip scrip;
  
  public Asset(String strategyName, String scripListName, String scripID) {
    this.strategyName = strategyName;
    this.scripListName = scripListName;
    this.assetName = (strategyName + " " + scripListName + " " + scripID);
    
    String[] scripIDParts = scripID.split(" ");
    String scripExchange = scripIDParts[0];
    String scripSegment = scripIDParts[1];
    String scripClass = scripIDParts[2];
    String scripName = scripIDParts[3];
    String scripContractType = scripIDParts[4];
    
    this.scrip = new Scrip(scripExchange, scripSegment, scripClass, scripName, scripContractType);
  }
  
  public String getAssetName() {
    return this.assetName;
  }
  
  public String getStrategyName() {
    return this.strategyName;
  }
  
  public String getScripListName() {
    return this.scripListName;
  }
  
  public Scrip getScrip()
  {
    return this.scrip;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/driver/driverHelperClasses/Asset.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */