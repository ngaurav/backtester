package com.q1.bt.preprocess;

import java.util.ArrayList;

public abstract class PreprocessVarList { protected ArrayList<PreprocessAlgo> preprocessVarlist;
  
  public PreprocessVarList() { this.preprocessVarlist = new ArrayList(); }
  
  public abstract void getPreprocessList();
  
  public ArrayList<String> getNames() {
    ArrayList<String> preProcessNameList = new ArrayList();
    for (PreprocessAlgo algo : this.preprocessVarlist) {
      String name = algo.getClass().getSimpleName() + algo.getparamList();
      preProcessNameList.add(name);
    }
    return preProcessNameList;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/preprocess/PreprocessVarList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */