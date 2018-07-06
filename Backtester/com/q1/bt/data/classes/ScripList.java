package com.q1.bt.data.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;



public abstract class ScripList
{
  public LinkedHashMap<String, Scrip> scripMap;
  
  public ScripList()
  {
    this.scripMap = new LinkedHashMap();
    
    ArrayList<Scrip> scripList = getScripList();
    
    for (Scrip scrip : scripList) {
      this.scripMap.put(scrip.scripID, scrip);
    }
  }
  

  public abstract ArrayList<Scrip> getScripList();
  
  public String getScripNames()
  {
    String scripNames = "";
    
    for (Scrip scrip : this.scripMap.values())
    {
      if (scripNames == "") {
        scripNames = scrip.scripName;
      } else {
        scripNames = scripNames + "," + scrip.scripName;
      }
    }
    return scripNames;
  }
  

  public String getAssetClassNames()
  {
    String assetClassNames = "";
    
    HashSet<String> assetClassSet = new HashSet();
    for (Scrip scrip : this.scripMap.values()) {
      assetClassSet.add(scrip.assetClassName);
    }
    for (String assetClassName : assetClassSet)
    {
      if (assetClassNames == "") {
        assetClassNames = assetClassName;
      } else {
        assetClassNames = assetClassNames + "," + assetClassName;
      }
    }
    return assetClassNames;
  }
  

  public LinkedHashSet<String> getScripIDSet()
  {
    LinkedHashSet<String> scripSet = new LinkedHashSet();
    for (Scrip scrip : this.scripMap.values()) {
      scripSet.add(scrip.scripID);
    }
    return scripSet;
  }
  
  public String getScripCount()
  {
    Integer count = Integer.valueOf(this.scripMap.size());
    return count.toString();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/ScripList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */