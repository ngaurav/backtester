package com.q1.bt.process.parameter;


public class PackageParameter
{
  String strategyPackage;
  
  String scripListPackage;
  
  String mlAlgorithmPackage;
  
  String mlFactorListPackage;
  
  String mlConsolidationFunctionPackage;
  
  String aaOptimizationAlgorithmPackage;
  
  String aaNormalizationAlgorithmPackage;
  String aaCovarianceFactorListPackage;
  String aaCovarianceModelPackage;
  String aaReturnFactorListPackage;
  String aaReturnModelPackage;
  
  public PackageParameter()
  {
    this.strategyPackage = "com.q1.ajusal.bt.assetallocator.strategy";
    this.scripListPackage = "com.q1.ajusal.bt.data.scriplist";
    

    this.mlAlgorithmPackage = "com.q1.ajusal.bt.machineLearning.algo";
    this.mlFactorListPackage = "com.q1.ajusal.bt.machineLearning.factor";
    this.mlConsolidationFunctionPackage = "com.q1.ajusal.bt.machineLearning.varcreation";
    

    this.aaOptimizationAlgorithmPackage = "com.q1.ajusal.bt.assetallocator.optimization";
    this.aaNormalizationAlgorithmPackage = "com.q1.ajusal.bt.assetallocator.portnormalizer";
    this.aaCovarianceFactorListPackage = "com.q1.ajusal.bt.assetallocator.varcreation";
    this.aaCovarianceModelPackage = "com.q1.ajusal.bt.assetallocator.algo";
    this.aaReturnFactorListPackage = "com.q1.ajusal.bt.assetallocator.varcreation";
    this.aaReturnModelPackage = "com.q1.ajusal.bt.assetallocator.algo";
  }
  

  public String getStrategyPackage()
  {
    return this.strategyPackage;
  }
  
  public void setStrategyPackage(String strategyPackage) {
    this.strategyPackage = strategyPackage;
  }
  
  public String getScripListPackage() {
    return this.scripListPackage;
  }
  
  public void setScripListPackage(String scripListPackage) {
    this.scripListPackage = scripListPackage;
  }
  
  public String getMlAlgorithmPackage() {
    return this.mlAlgorithmPackage;
  }
  
  public void setMlAlgorithmPackage(String mlAlgorithmPackage) {
    this.mlAlgorithmPackage = mlAlgorithmPackage;
  }
  
  public String getMlFactorListPackage() {
    return this.mlFactorListPackage;
  }
  
  public void setMlFactorListPackage(String mlFactorListPackage) {
    this.mlFactorListPackage = mlFactorListPackage;
  }
  
  public String getMlConsolidationFunctionPackage() {
    return this.mlConsolidationFunctionPackage;
  }
  
  public void setMlConsolidationFunctionPackage(String mlConsolidationFunctionPackage) {
    this.mlConsolidationFunctionPackage = mlConsolidationFunctionPackage;
  }
  
  public String getAaOptimizationAlgorithmPackage() {
    return this.aaOptimizationAlgorithmPackage;
  }
  
  public void setAaOptimizationAlgorithmPackage(String aaOptimizationAlgorithmPackage) {
    this.aaOptimizationAlgorithmPackage = aaOptimizationAlgorithmPackage;
  }
  
  public String getAaNormalizationAlgorithmPackage() {
    return this.aaNormalizationAlgorithmPackage;
  }
  
  public void setAaNormalizationAlgorithmPackage(String aaNormalizationAlgorithmPackage) {
    this.aaNormalizationAlgorithmPackage = aaNormalizationAlgorithmPackage;
  }
  
  public String getAaCovarianceFactorListPackage() {
    return this.aaCovarianceFactorListPackage;
  }
  
  public void setAaCovarianceFactorListPackage(String aaCovarianceFactorListPackage) {
    this.aaCovarianceFactorListPackage = aaCovarianceFactorListPackage;
  }
  
  public String getAaCovarianceModelPackage() {
    return this.aaCovarianceModelPackage;
  }
  
  public void setAaCovarianceModelPackage(String aaCovarianceModelPackage) {
    this.aaCovarianceModelPackage = aaCovarianceModelPackage;
  }
  
  public String getAaReturnFactorListPackage() {
    return this.aaReturnFactorListPackage;
  }
  
  public void setAaReturnFactorListPackage(String aaReturnFactorListPackage) {
    this.aaReturnFactorListPackage = aaReturnFactorListPackage;
  }
  
  public String getAaReturnModelPackage() {
    return this.aaReturnModelPackage;
  }
  
  public void setAaReturnModelPackage(String aaReturnModelPackage) {
    this.aaReturnModelPackage = aaReturnModelPackage;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/PackageParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */