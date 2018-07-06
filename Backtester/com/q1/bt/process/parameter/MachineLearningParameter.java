package com.q1.bt.process.parameter;

import com.q1.bt.machineLearning.absclasses.MLAlgo;
import com.q1.bt.machineLearning.absclasses.VarList;
import com.q1.bt.process.machinelearning.LookbackType;
import com.q1.bt.process.machinelearning.MergeType;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TreeSet;




public class MachineLearningParameter
{
  Integer windowPeriod;
  Integer updatePeriod;
  Integer blackoutPeriod;
  LookbackType lookbackType;
  MergeType modelMergeType;
  MergeType selectionMergeType;
  Integer segmentCount;
  Integer overallCount;
  Double segmentCorrelThreshold;
  Double overallCorrelThreshold;
  Integer correlPeriod;
  Boolean bias;
  ArrayList<String> factorList;
  VarList varList;
  MLAlgo mlAlgorithm;
  String mlAlgoTimeStamp;
  
  public MachineLearningParameter()
  {
    this.windowPeriod = Integer.valueOf(250);
    this.updatePeriod = Integer.valueOf(20);
    this.blackoutPeriod = Integer.valueOf(750);
    this.lookbackType = LookbackType.Hinged;
    this.modelMergeType = MergeType.All;
    this.selectionMergeType = MergeType.Strategy;
    this.segmentCount = Integer.valueOf(5);
    this.overallCount = Integer.valueOf(5);
    this.segmentCorrelThreshold = Double.valueOf(0.7D);
    this.overallCorrelThreshold = Double.valueOf(0.7D);
    this.correlPeriod = Integer.valueOf(30);
    this.factorList = new ArrayList();
    this.bias = Boolean.valueOf(false);
  }
  




  public MachineLearningParameter(Integer windowPeriod, Integer updatePeriod, Integer blackoutPeriod, LookbackType lookbackType, MergeType modelMergeType, MergeType selectionMergeType, Integer segmentCount, Integer overallCount, Double segmentCorrelThreshold, Double overallCorrelThreshold, int correlPeriod, boolean bias)
  {
    this.windowPeriod = windowPeriod;
    this.updatePeriod = updatePeriod;
    this.blackoutPeriod = blackoutPeriod;
    this.lookbackType = lookbackType;
    this.modelMergeType = modelMergeType;
    this.selectionMergeType = selectionMergeType;
    this.segmentCount = segmentCount;
    this.overallCount = overallCount;
    this.segmentCorrelThreshold = segmentCorrelThreshold;
    this.overallCorrelThreshold = overallCorrelThreshold;
    this.correlPeriod = Integer.valueOf(correlPeriod);
    this.bias = Boolean.valueOf(bias);
  }
  
  public String getMLParametersAsKey()
  {
    String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
      this.mlAlgoTimeStamp + "$" + this.varList.getClass().getSimpleName() + 
      "$" + this.windowPeriod + "$" + this.updatePeriod + "$" + 
      this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
      this.selectionMergeType.getVal() + "$" + 
      this.lookbackType.getVal() + "$" + this.segmentCount + "$" + 
      this.overallCount + "$" + this.segmentCorrelThreshold + "$" + 
      this.overallCorrelThreshold + "$" + this.correlPeriod + "$" + 
      this.bias + "$" + 
      getFactorParameters();
    return parameterKey;
  }
  
  public String getMLParametersAsOutputKey()
  {
    String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
      this.mlAlgoTimeStamp + "$" + this.windowPeriod + "$" + this.updatePeriod + 
      "$" + this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
      this.selectionMergeType.getVal() + "$" + 
      this.lookbackType.getVal() + "$" + this.correlPeriod + "$" + 
      this.bias + "$" + 
      getFactorParameters();
    return parameterKey;
  }
  
  public String getMLParametersAsInputKey() {
    String parameterKey = this.modelMergeType.getVal() + "$" + 
      getFactorParameters();
    return parameterKey;
  }
  


  public String getFactorParameters()
  {
    TreeSet<String> factorSet = new TreeSet(getFactorList());
    
    String factorParams = "";
    for (String factor : factorSet) {
      if (factorParams.equals("")) {
        factorParams = factor;
      } else {
        factorParams = factorParams + "$" + factor;
      }
    }
    return factorParams;
  }
  
  public Boolean getBias()
  {
    return this.bias;
  }
  
  public Integer getWindowPeriod() {
    return this.windowPeriod;
  }
  
  public void setBias(Boolean bias) {
    this.bias = bias;
  }
  
  public void setWindowPeriod(Integer windowPeriod) {
    this.windowPeriod = windowPeriod;
  }
  
  public Integer getUpdatePeriod() {
    return this.updatePeriod;
  }
  
  public void setUpdatePeriod(Integer updatePeriod) {
    this.updatePeriod = updatePeriod;
  }
  
  public Integer getBlackoutPeriod() {
    return this.blackoutPeriod;
  }
  
  public void setBlackoutPeriod(Integer blackoutPeriod) {
    this.blackoutPeriod = blackoutPeriod;
  }
  
  public LookbackType getLookbackType() {
    return this.lookbackType;
  }
  
  public void setLookbackType(LookbackType lookbackType) {
    this.lookbackType = lookbackType;
  }
  
  public MergeType getModelMergeType()
  {
    return this.modelMergeType;
  }
  
  public void setModelMergeType(MergeType modelMergeType) {
    this.modelMergeType = modelMergeType;
  }
  
  public MergeType getSelectionMergeType() {
    return this.selectionMergeType;
  }
  
  public void setSelectionMergeType(MergeType postModelSelectionMergeType) {
    this.selectionMergeType = postModelSelectionMergeType;
  }
  
  public Integer getSegmentCount() {
    return this.segmentCount;
  }
  
  public void setSegmentCount(Integer segmentCount) {
    this.segmentCount = segmentCount;
  }
  
  public Integer getOverallCount() {
    return this.overallCount;
  }
  
  public void setOverallCount(Integer overallCount) {
    this.overallCount = overallCount;
  }
  
  public Double getSegmentCorrelThreshold() {
    return this.segmentCorrelThreshold;
  }
  
  public void setSegmentCorrelThreshold(Double segmentCorrelThreshold) {
    this.segmentCorrelThreshold = segmentCorrelThreshold;
  }
  
  public Double getOverallCorrelThreshold() {
    return this.overallCorrelThreshold;
  }
  
  public void setOverallCorrelThreshold(Double overallCorrelThreshold) {
    this.overallCorrelThreshold = overallCorrelThreshold;
  }
  
  public Integer getCorrelPeriod() {
    return this.correlPeriod;
  }
  
  public void setCorrelPeriod(Integer correlPeriod) {
    this.correlPeriod = correlPeriod;
  }
  
  public ArrayList<String> getFactorList() {
    return this.factorList;
  }
  
  public void setFactorList(ArrayList<String> factorList) {
    this.factorList = factorList;
  }
  
  public VarList getVarList() {
    return this.varList;
  }
  
  public void setVarList(VarList varList)
  {
    this.varList = varList;
  }
  
  public MLAlgo getMlAlgorithm() {
    return this.mlAlgorithm;
  }
  

  public void setMlAlgorithm(MLAlgo mlAlgorithm, String mainPath)
  {
    String packageLocR = mlAlgorithm.getModelPackage().replaceAll("\\.", "/");
    String packagePathR = mainPath + "/src/" + packageLocR;
    String algoPathTS = packagePathR + "/" + mlAlgorithm.getModelName();
    File stratFile = new File(algoPathTS);
    SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    String mlAlgoTimeStamp = stratFormat.format(Long.valueOf(stratFile.lastModified()));
    setMlAlgoTimeStamp(mlAlgoTimeStamp);
    
    this.mlAlgorithm = mlAlgorithm;
  }
  
  public String getMlAlgoTimeStamp() {
    return this.mlAlgoTimeStamp;
  }
  
  public void setMlAlgoTimeStamp(String mlAlgoTimeStamp) {
    this.mlAlgoTimeStamp = mlAlgoTimeStamp;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/MachineLearningParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */