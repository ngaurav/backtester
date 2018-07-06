package com.q1.bt.preprocess;

import java.util.ArrayList;

public abstract class PreprocessAlgo { public ArrayList<String> paramList;
  
  public PreprocessAlgo() { this.paramList = new ArrayList(); }
  

  public String getparamList()
  {
    String paramString = "";
    for (String param : this.paramList) {
      paramString = paramString + "_" + param;
    }
    return paramString;
  }
  
  public abstract String getModelPackage();
  
  public abstract String getModelName();
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreprocessAlgo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */