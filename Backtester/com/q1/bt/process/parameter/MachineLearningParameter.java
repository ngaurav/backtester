/*     */ package com.q1.bt.process.parameter;
/*     */ 
/*     */ import com.q1.bt.machineLearning.absclasses.MLAlgo;
/*     */ import com.q1.bt.machineLearning.absclasses.VarList;
/*     */ import com.q1.bt.process.machinelearning.LookbackType;
/*     */ import com.q1.bt.process.machinelearning.MergeType;
/*     */ import java.io.File;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MachineLearningParameter
/*     */ {
/*     */   Integer windowPeriod;
/*     */   Integer updatePeriod;
/*     */   Integer blackoutPeriod;
/*     */   LookbackType lookbackType;
/*     */   MergeType modelMergeType;
/*     */   MergeType selectionMergeType;
/*     */   Integer segmentCount;
/*     */   Integer overallCount;
/*     */   Double segmentCorrelThreshold;
/*     */   Double overallCorrelThreshold;
/*     */   Integer correlPeriod;
/*     */   ArrayList<String> factorList;
/*     */   VarList varList;
/*     */   MLAlgo mlAlgorithm;
/*     */   String mlAlgoTimeStamp;
/*     */   
/*     */   public MachineLearningParameter()
/*     */   {
/*  36 */     this.windowPeriod = Integer.valueOf(250);
/*  37 */     this.updatePeriod = Integer.valueOf(20);
/*  38 */     this.blackoutPeriod = Integer.valueOf(750);
/*  39 */     this.lookbackType = LookbackType.Hinged;
/*  40 */     this.modelMergeType = MergeType.All;
/*  41 */     this.selectionMergeType = MergeType.Strategy;
/*  42 */     this.segmentCount = Integer.valueOf(5);
/*  43 */     this.overallCount = Integer.valueOf(5);
/*  44 */     this.segmentCorrelThreshold = Double.valueOf(0.7D);
/*  45 */     this.overallCorrelThreshold = Double.valueOf(0.7D);
/*  46 */     this.correlPeriod = Integer.valueOf(30);
/*  47 */     this.factorList = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MachineLearningParameter(Integer windowPeriod, Integer updatePeriod, Integer blackoutPeriod, LookbackType lookbackType, MergeType modelMergeType, MergeType selectionMergeType, Integer segmentCount, Integer overallCount, Double segmentCorrelThreshold, Double overallCorrelThreshold, int correlPeriod)
/*     */   {
/*  56 */     this.windowPeriod = windowPeriod;
/*  57 */     this.updatePeriod = updatePeriod;
/*  58 */     this.blackoutPeriod = blackoutPeriod;
/*  59 */     this.lookbackType = lookbackType;
/*  60 */     this.modelMergeType = modelMergeType;
/*  61 */     this.selectionMergeType = selectionMergeType;
/*  62 */     this.segmentCount = segmentCount;
/*  63 */     this.overallCount = overallCount;
/*  64 */     this.segmentCorrelThreshold = segmentCorrelThreshold;
/*  65 */     this.overallCorrelThreshold = overallCorrelThreshold;
/*  66 */     this.correlPeriod = Integer.valueOf(correlPeriod);
/*     */   }
/*     */   
/*     */   public String getMLParametersAsKey()
/*     */   {
/*  71 */     String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
/*  72 */       this.mlAlgoTimeStamp + "$" + this.varList.getClass().getSimpleName() + 
/*  73 */       "$" + this.windowPeriod + "$" + this.updatePeriod + "$" + 
/*  74 */       this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
/*  75 */       this.selectionMergeType.getVal() + "$" + 
/*  76 */       this.lookbackType.getVal() + "$" + this.segmentCount + "$" + 
/*  77 */       this.overallCount + "$" + this.segmentCorrelThreshold + "$" + 
/*  78 */       this.overallCorrelThreshold + "$" + this.correlPeriod + "$" + 
/*  79 */       getFactorParameters();
/*  80 */     return parameterKey;
/*     */   }
/*     */   
/*     */   public String getMLParametersAsOutputKey()
/*     */   {
/*  85 */     String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
/*  86 */       this.mlAlgoTimeStamp + "$" + this.windowPeriod + "$" + this.updatePeriod + 
/*  87 */       "$" + this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
/*  88 */       this.selectionMergeType.getVal() + "$" + 
/*  89 */       this.lookbackType.getVal() + "$" + this.correlPeriod + "$" + 
/*  90 */       getFactorParameters();
/*  91 */     return parameterKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFactorParameters()
/*     */   {
/*  98 */     TreeSet<String> factorSet = new TreeSet(getFactorList());
/*     */     
/* 100 */     String factorParams = "";
/* 101 */     for (String factor : factorSet) {
/* 102 */       if (factorParams.equals("")) {
/* 103 */         factorParams = factor;
/*     */       } else {
/* 105 */         factorParams = factorParams + "$" + factor;
/*     */       }
/*     */     }
/* 108 */     return factorParams;
/*     */   }
/*     */   
/*     */   public Integer getWindowPeriod()
/*     */   {
/* 113 */     return this.windowPeriod;
/*     */   }
/*     */   
/*     */   public void setWindowPeriod(Integer windowPeriod) {
/* 117 */     this.windowPeriod = windowPeriod;
/*     */   }
/*     */   
/*     */   public Integer getUpdatePeriod() {
/* 121 */     return this.updatePeriod;
/*     */   }
/*     */   
/*     */   public void setUpdatePeriod(Integer updatePeriod) {
/* 125 */     this.updatePeriod = updatePeriod;
/*     */   }
/*     */   
/*     */   public Integer getBlackoutPeriod() {
/* 129 */     return this.blackoutPeriod;
/*     */   }
/*     */   
/*     */   public void setBlackoutPeriod(Integer blackoutPeriod) {
/* 133 */     this.blackoutPeriod = blackoutPeriod;
/*     */   }
/*     */   
/*     */   public LookbackType getLookbackType() {
/* 137 */     return this.lookbackType;
/*     */   }
/*     */   
/*     */   public void setLookbackType(LookbackType lookbackType) {
/* 141 */     this.lookbackType = lookbackType;
/*     */   }
/*     */   
/*     */   public MergeType getModelMergeType()
/*     */   {
/* 146 */     return this.modelMergeType;
/*     */   }
/*     */   
/*     */   public void setModelMergeType(MergeType modelMergeType) {
/* 150 */     this.modelMergeType = modelMergeType;
/*     */   }
/*     */   
/*     */   public MergeType getSelectionMergeType() {
/* 154 */     return this.selectionMergeType;
/*     */   }
/*     */   
/*     */   public void setSelectionMergeType(MergeType postModelSelectionMergeType) {
/* 158 */     this.selectionMergeType = postModelSelectionMergeType;
/*     */   }
/*     */   
/*     */   public Integer getSegmentCount() {
/* 162 */     return this.segmentCount;
/*     */   }
/*     */   
/*     */   public void setSegmentCount(Integer segmentCount) {
/* 166 */     this.segmentCount = segmentCount;
/*     */   }
/*     */   
/*     */   public Integer getOverallCount() {
/* 170 */     return this.overallCount;
/*     */   }
/*     */   
/*     */   public void setOverallCount(Integer overallCount) {
/* 174 */     this.overallCount = overallCount;
/*     */   }
/*     */   
/*     */   public Double getSegmentCorrelThreshold() {
/* 178 */     return this.segmentCorrelThreshold;
/*     */   }
/*     */   
/*     */   public void setSegmentCorrelThreshold(Double segmentCorrelThreshold) {
/* 182 */     this.segmentCorrelThreshold = segmentCorrelThreshold;
/*     */   }
/*     */   
/*     */   public Double getOverallCorrelThreshold() {
/* 186 */     return this.overallCorrelThreshold;
/*     */   }
/*     */   
/*     */   public void setOverallCorrelThreshold(Double overallCorrelThreshold) {
/* 190 */     this.overallCorrelThreshold = overallCorrelThreshold;
/*     */   }
/*     */   
/*     */   public Integer getCorrelPeriod() {
/* 194 */     return this.correlPeriod;
/*     */   }
/*     */   
/*     */   public void setCorrelPeriod(Integer correlPeriod) {
/* 198 */     this.correlPeriod = correlPeriod;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getFactorList() {
/* 202 */     return this.factorList;
/*     */   }
/*     */   
/*     */   public void setFactorList(ArrayList<String> factorList) {
/* 206 */     this.factorList = factorList;
/*     */   }
/*     */   
/*     */   public VarList getVarList() {
/* 210 */     return this.varList;
/*     */   }
/*     */   
/*     */   public void setVarList(VarList varList)
/*     */   {
/* 215 */     this.varList = varList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MLAlgo getMlAlgorithm()
/*     */   {
/* 230 */     return this.mlAlgorithm;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMlAlgorithm(MLAlgo mlAlgorithm, String mainPath)
/*     */   {
/* 236 */     String packageLocR = mlAlgorithm.getModelPackage().replaceAll("\\.", 
/* 237 */       "/");
/* 238 */     String packagePathR = mainPath + "/src/" + packageLocR;
/* 239 */     String algoPathTS = packagePathR + "/" + mlAlgorithm.getModelName();
/* 240 */     File stratFile = new File(algoPathTS);
/* 241 */     SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/* 242 */     String mlAlgoTimeStamp = stratFormat.format(Long.valueOf(stratFile.lastModified()));
/* 243 */     setMlAlgoTimeStamp(mlAlgoTimeStamp);
/*     */     
/* 245 */     this.mlAlgorithm = mlAlgorithm;
/*     */   }
/*     */   
/*     */   public String getMlAlgoTimeStamp() {
/* 249 */     return this.mlAlgoTimeStamp;
/*     */   }
/*     */   
/*     */   public void setMlAlgoTimeStamp(String mlAlgoTimeStamp) {
/* 253 */     this.mlAlgoTimeStamp = mlAlgoTimeStamp;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/MachineLearningParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */