package com.q1.bt.process;

import java.util.ArrayList;
import java.util.HashMap;






public class ProcessFlow
{
  ArrayList<BacktesterProcess> processList;
  Integer currentIndex;
  public static HashMap<BacktesterProcess, Integer> tabIndexMap = new HashMap();
  static { tabIndexMap.put(BacktesterProcess.Login, Integer.valueOf(0));
    tabIndexMap.put(BacktesterProcess.Backtest, Integer.valueOf(1));
    tabIndexMap.put(BacktesterProcess.BatchProcess, Integer.valueOf(2));
    tabIndexMap.put(BacktesterProcess.MachineLearning, Integer.valueOf(3));
    tabIndexMap.put(BacktesterProcess.Results, Integer.valueOf(4));
    tabIndexMap.put(BacktesterProcess.RollingAnalysis, Integer.valueOf(5));
    tabIndexMap.put(BacktesterProcess.SensitivityAnalysis, Integer.valueOf(6));
    tabIndexMap.put(BacktesterProcess.IsOs, Integer.valueOf(7));
    tabIndexMap.put(BacktesterProcess.PostProcess, Integer.valueOf(8));
  }
  
  public ProcessFlow()
  {
    this.processList = new ArrayList();
    this.currentIndex = null;
  }
  
  public void add(BacktesterProcess process)
  {
    if (this.processList.isEmpty()) {
      this.processList.add(process);
      this.currentIndex = Integer.valueOf(0);
    } else {
      this.processList.add(process);
    }
  }
  
  public void update() {
    this.currentIndex = Integer.valueOf(this.currentIndex.intValue() + 1);
  }
  
  public void revert()
  {
    this.processList.remove(this.processList.size() - 1);
    this.currentIndex = Integer.valueOf(this.currentIndex.intValue() - 1);
  }
  
  public BacktesterProcess getCurrentProcess()
  {
    if ((this.currentIndex.intValue() > this.processList.size()) || (this.currentIndex.intValue() < 0))
      return null;
    return (BacktesterProcess)this.processList.get(this.currentIndex.intValue());
  }
  
  public Integer getCurrentTabIndex()
  {
    return (Integer)tabIndexMap.get(getCurrentProcess());
  }
  
  public Integer getProcessTabIndex(BacktesterProcess process)
  {
    return (Integer)tabIndexMap.get(process);
  }
  
  public BacktesterProcess getPreviousProcess()
  {
    Integer previousIndex = Integer.valueOf(this.currentIndex.intValue() - 1);
    if ((previousIndex.intValue() > this.processList.size()) || (previousIndex.intValue() < 0))
      return null;
    return (BacktesterProcess)this.processList.get(previousIndex.intValue());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/ProcessFlow.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */