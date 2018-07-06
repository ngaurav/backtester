package com.q1.bt.process.parameter;

import java.io.File;

public class LoginParameter
{
  String sqlIPAddress;
  String sqlDatabase;
  String sqlUsername;
  String sqlPassword;
  com.q1.bt.process.BacktesterProcess process;
  String dataPath;
  String mainPath;
  String outputPath;
  String sensitivityPath;
  Double capital;
  Double riskPerTrade;
  Long defStartDate;
  Long defEndDate;
  com.q1.bt.process.backtest.SlippageModel defaultSlippageModel;
  com.q1.bt.execution.RolloverMethod defaultRolloverMethod;
  
  public LoginParameter()
  {
    this.sqlIPAddress = "127.0.0.1";
    this.sqlDatabase = "quantdatabase";
    this.sqlUsername = "root";
    this.sqlPassword = "";
    this.dataPath = null;
    this.mainPath = createPath("");
    this.outputPath = createPath("/Output");
    this.sensitivityPath = createPath("/Sensitivity Output");
    this.capital = Double.valueOf(500000.0D);
    this.riskPerTrade = Double.valueOf(1.0D);
    this.process = com.q1.bt.process.BacktesterProcess.Backtest;
    this.defaultSlippageModel = com.q1.bt.process.backtest.SlippageModel.AdaptiveModelSigma;
    this.defaultRolloverMethod = com.q1.bt.execution.RolloverMethod.CloseToClose;
  }
  

  public String createPath(String folder)
  {
    String path = LoginParameter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    
    try
    {
      String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
      String[] acc = currentPath.split("/");
      String outPath = acc[0];
      for (int i = 1; i < acc.length - 1; i++) {
        if (acc[i].equals("lib"))
          break;
        outPath = outPath + "/" + acc[i];
      }
      
      String dataPath = outPath + folder;
      if (!new File(dataPath).exists())
        new File(dataPath).mkdirs();
    } catch (java.io.UnsupportedEncodingException e) {
      e.printStackTrace();
      return createPath(folder); }
    String dataPath;
    String currentPath; return dataPath;
  }
  
  public com.q1.bt.execution.RolloverMethod getDefaultRolloverMethod()
  {
    return this.defaultRolloverMethod;
  }
  
  public void setDefaultRolloverMethod(com.q1.bt.execution.RolloverMethod defaultRolloverMethod) {
    this.defaultRolloverMethod = defaultRolloverMethod;
  }
  
  public com.q1.bt.process.backtest.SlippageModel getDefaultSlippageModel() {
    return this.defaultSlippageModel;
  }
  
  public void setDefaultSlippageModel(com.q1.bt.process.backtest.SlippageModel defaultSlippageModel) {
    this.defaultSlippageModel = defaultSlippageModel;
  }
  
  public String getSqlIPAddress() {
    return this.sqlIPAddress;
  }
  
  public void setSqlIPAddress(String sqlIPAddress) {
    this.sqlIPAddress = sqlIPAddress;
  }
  
  public String getSqlDatabase() {
    return this.sqlDatabase;
  }
  
  public void setSqlDatabase(String sqlDatabase) {
    this.sqlDatabase = sqlDatabase;
  }
  
  public String getSqlUsername() {
    return this.sqlUsername;
  }
  
  public void setSqlUsername(String sqlUsername) {
    this.sqlUsername = sqlUsername;
  }
  
  public String getSqlPassword() {
    return this.sqlPassword;
  }
  
  public void setSqlPassword(String sqlPassword) {
    this.sqlPassword = sqlPassword;
  }
  
  public com.q1.bt.process.BacktesterProcess getProcess() {
    return this.process;
  }
  
  public void setProcess(com.q1.bt.process.BacktesterProcess process) {
    this.process = process;
  }
  
  public String getDataPath() {
    return this.dataPath;
  }
  
  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }
  
  public String getMainPath() {
    return this.mainPath;
  }
  
  public void setMainPath(String mainPath) {
    this.mainPath = mainPath;
  }
  
  public String getOutputPath() {
    return this.outputPath;
  }
  
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
  
  public Double getCapital() {
    return this.capital;
  }
  
  public void setCapital(Double capital) {
    this.capital = capital;
  }
  
  public Double getRiskPerTrade() {
    return this.riskPerTrade;
  }
  
  public void setRiskPerTrade(Double riskPerTrade) {
    this.riskPerTrade = riskPerTrade;
  }
  
  public Long getDefaultStartDate() {
    return this.defStartDate;
  }
  
  public void setDefaultStartDate(Long defStartDate) {
    this.defStartDate = defStartDate;
  }
  
  public Long getDefaultEndDate() {
    return this.defEndDate;
  }
  
  public void setDefaultEndDate(Long defEndDate) {
    this.defEndDate = defEndDate;
  }
  
  public String getSensitivityPath() {
    return this.sensitivityPath;
  }
  
  public void setSensitivityPath(String sensitivityPath) {
    if (!new File(sensitivityPath).exists())
      new File(sensitivityPath).mkdirs();
    this.sensitivityPath = sensitivityPath;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/LoginParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */