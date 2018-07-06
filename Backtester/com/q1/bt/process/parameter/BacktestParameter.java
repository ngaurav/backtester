package com.q1.bt.process.parameter;

import com.q1.bt.driver.backtest.enums.AggregationMode;
import com.q1.bt.execution.RolloverMethod;
import com.q1.bt.process.backtest.PostProcessMode;
import com.q1.bt.process.backtest.SlippageModel;



public class BacktestParameter
{
  long startDate;
  long endDate;
  Double capital;
  Double riskPerTrade;
  boolean skipExistingBacktest;
  boolean defaultParametersCheck;
  boolean exportResultsCheck;
  boolean generateOutputCheck;
  boolean orderBookBacktest;
  SlippageModel slippageModel;
  RolloverMethod rolloverMethod;
  PostProcessMode postProcessMode;
  AggregationMode aggregationMode;
  
  public BacktestParameter()
  {
    this.startDate = 0L;
    this.endDate = 20910101L;
    this.capital = Double.valueOf(1.0E7D);
    this.riskPerTrade = Double.valueOf(1.0D);
    this.skipExistingBacktest = false;
    this.defaultParametersCheck = true;
    this.exportResultsCheck = true;
    this.generateOutputCheck = false;
    this.orderBookBacktest = false;
    this.slippageModel = SlippageModel.AdaptiveModelSigma;
    this.rolloverMethod = RolloverMethod.CloseToClose;
    this.postProcessMode = PostProcessMode.SingleScrip;
    this.aggregationMode = AggregationMode.Fixed;
  }
  

  public void setAggregationMode(AggregationMode aggregationMode)
  {
    this.aggregationMode = aggregationMode;
  }
  
  public void setRolloverMethod(RolloverMethod rolloverMethod) {
    this.rolloverMethod = rolloverMethod;
  }
  
  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }
  
  public void setEndDate(long endDate) {
    this.endDate = endDate;
  }
  
  public void setCapital(Double capital) {
    this.capital = capital;
  }
  
  public void setRiskPerTrade(Double riskPerTrade) {
    this.riskPerTrade = riskPerTrade;
  }
  
  public void setSkipExistingBacktest(boolean skipExistingBacktest) {
    this.skipExistingBacktest = skipExistingBacktest;
  }
  
  public void setDefaultParametersCheck(boolean defaultParametersCheck) {
    this.defaultParametersCheck = defaultParametersCheck;
  }
  
  public void setExportResultsCheck(boolean exportResultsCheck) {
    this.exportResultsCheck = exportResultsCheck;
  }
  
  public void setGenerateOutputCheck(boolean generateOutputCheck) {
    this.generateOutputCheck = generateOutputCheck;
  }
  
  public void setSlippageModel(SlippageModel slippageModel) {
    this.slippageModel = slippageModel;
  }
  
  public void setPostProcessMode(PostProcessMode postProcessMode) {
    this.postProcessMode = postProcessMode;
  }
  

  public AggregationMode getAggregationMode()
  {
    return this.aggregationMode;
  }
  
  public RolloverMethod getRolloverMethod() {
    return this.rolloverMethod;
  }
  
  public long getStartDate() {
    return this.startDate;
  }
  
  public long getEndDate() {
    return this.endDate;
  }
  
  public Double getCapital() {
    return this.capital;
  }
  
  public Double getRiskPerTrade() {
    return this.riskPerTrade;
  }
  
  public boolean isSkipExistingBacktest() {
    return this.skipExistingBacktest;
  }
  
  public boolean isDefaultParametersCheck() {
    return this.defaultParametersCheck;
  }
  
  public boolean isExportResultsCheck() {
    return this.exportResultsCheck;
  }
  
  public boolean isGenerateOutputCheck() {
    return this.generateOutputCheck;
  }
  
  public SlippageModel getSlippageModel() {
    return this.slippageModel;
  }
  
  public PostProcessMode getPostProcessMode() {
    return this.postProcessMode;
  }
  
  public boolean isOrderBookBacktest() {
    return this.orderBookBacktest;
  }
  
  public void setOrderBookBacktest(boolean orderBookBacktest) {
    this.orderBookBacktest = orderBookBacktest;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/BacktestParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */