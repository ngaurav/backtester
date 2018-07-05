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
/*     */   Boolean bias;
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
/*  48 */     this.bias = Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MachineLearningParameter(Integer windowPeriod, Integer updatePeriod, Integer blackoutPeriod, LookbackType lookbackType, MergeType modelMergeType, MergeType selectionMergeType, Integer segmentCount, Integer overallCount, Double segmentCorrelThreshold, Double overallCorrelThreshold, int correlPeriod, boolean bias)
/*     */   {
/*  57 */     this.windowPeriod = windowPeriod;
/*  58 */     this.updatePeriod = updatePeriod;
/*  59 */     this.blackoutPeriod = blackoutPeriod;
/*  60 */     this.lookbackType = lookbackType;
/*  61 */     this.modelMergeType = modelMergeType;
/*  62 */     this.selectionMergeType = selectionMergeType;
/*  63 */     this.segmentCount = segmentCount;
/*  64 */     this.overallCount = overallCount;
/*  65 */     this.segmentCorrelThreshold = segmentCorrelThreshold;
/*  66 */     this.overallCorrelThreshold = overallCorrelThreshold;
/*  67 */     this.correlPeriod = Integer.valueOf(correlPeriod);
/*  68 */     this.bias = Boolean.valueOf(bias);
/*     */   }
/*     */   
/*     */   public String getMLParametersAsKey()
/*     */   {
/*  73 */     String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
/*  74 */       this.mlAlgoTimeStamp + "$" + this.varList.getClass().getSimpleName() + 
/*  75 */       "$" + this.windowPeriod + "$" + this.updatePeriod + "$" + 
/*  76 */       this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
/*  77 */       this.selectionMergeType.getVal() + "$" + 
/*  78 */       this.lookbackType.getVal() + "$" + this.segmentCount + "$" + 
/*  79 */       this.overallCount + "$" + this.segmentCorrelThreshold + "$" + 
/*  80 */       this.overallCorrelThreshold + "$" + this.correlPeriod + "$" + 
/*  81 */       this.bias + "$" + 
/*  82 */       getFactorParameters();
/*  83 */     return parameterKey;
/*     */   }
/*     */   
/*     */   public String getMLParametersAsOutputKey()
/*     */   {
/*  88 */     String parameterKey = this.mlAlgorithm.getModelName() + "$" + 
/*  89 */       this.mlAlgoTimeStamp + "$" + this.windowPeriod + "$" + this.updatePeriod + 
/*  90 */       "$" + this.blackoutPeriod + "$" + this.modelMergeType.getVal() + "$" + 
/*  91 */       this.selectionMergeType.getVal() + "$" + 
/*  92 */       this.lookbackType.getVal() + "$" + this.correlPeriod + "$" + 
/*  93 */       this.bias + "$" + 
/*  94 */       getFactorParameters();
/*  95 */     return parameterKey;
/*     */   }
/*     */   
/*     */   public String getMLParametersAsInputKey() {
/*  99 */     String parameterKey = this.modelMergeType.getVal() + "$" + 
/* 100 */       getFactorParameters();
/* 101 */     return parameterKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getFactorParameters()
/*     */   {
/* 108 */     TreeSet<String> factorSet = new TreeSet(getFactorList());
/*     */     
/* 110 */     String factorParams = "";
/* 111 */     for (String factor : factorSet) {
/* 112 */       if (factorParams.equals("")) {
/* 113 */         factorParams = factor;
/*     */       } else {
/* 115 */         factorParams = factorParams + "$" + factor;
/*     */       }
/*     */     }
/* 118 */     return factorParams;
/*     */   }
/*     */   
/*     */   public Boolean getBias()
/*     */   {
/* 123 */     return this.bias;
/*     */   }
/*     */   
/*     */   public Integer getWindowPeriod() {
/* 127 */     return this.windowPeriod;
/*     */   }
/*     */   
/*     */   public void setBias(Boolean bias) {
/* 131 */     this.bias = bias;
/*     */   }
/*     */   
/*     */   public void setWindowPeriod(Integer windowPeriod) {
/* 135 */     this.windowPeriod = windowPeriod;
/*     */   }
/*     */   
/*     */   public Integer getUpdatePeriod() {
/* 139 */     return this.updatePeriod;
/*     */   }
/*     */   
/*     */   public void setUpdatePeriod(Integer updatePeriod) {
/* 143 */     this.updatePeriod = updatePeriod;
/*     */   }
/*     */   
/*     */   public Integer getBlackoutPeriod() {
/* 147 */     return this.blackoutPeriod;
/*     */   }
/*     */   
/*     */   public void setBlackoutPeriod(Integer blackoutPeriod) {
/* 151 */     this.blackoutPeriod = blackoutPeriod;
/*     */   }
/*     */   
/*     */   public LookbackType getLookbackType() {
/* 155 */     return this.lookbackType;
/*     */   }
/*     */   
/*     */   public void setLookbackType(LookbackType lookbackType) {
/* 159 */     this.lookbackType = lookbackType;
/*     */   }
/*     */   
/*     */   public MergeType getModelMergeType()
/*     */   {
/* 164 */     return this.modelMergeType;
/*     */   }
/*     */   
/*     */   public void setModelMergeType(MergeType modelMergeType) {
/* 168 */     this.modelMergeType = modelMergeType;
/*     */   }
/*     */   
/*     */   public MergeType getSelectionMergeType() {
/* 172 */     return this.selectionMergeType;
/*     */   }
/*     */   
/*     */   public void setSelectionMergeType(MergeType postModelSelectionMergeType) {
/* 176 */     this.selectionMergeType = postModelSelectionMergeType;
/*     */   }
/*     */   
/*     */   public Integer getSegmentCount() {
/* 180 */     return this.segmentCount;
/*     */   }
/*     */   
/*     */   public void setSegmentCount(Integer segmentCount) {
/* 184 */     this.segmentCount = segmentCount;
/*     */   }
/*     */   
/*     */   public Integer getOverallCount() {
/* 188 */     return this.overallCount;
/*     */   }
/*     */   
/*     */   public void setOverallCount(Integer overallCount) {
/* 192 */     this.overallCount = overallCount;
/*     */   }
/*     */   
/*     */   public Double getSegmentCorrelThreshold() {
/* 196 */     return this.segmentCorrelThreshold;
/*     */   }
/*     */   
/*     */   public void setSegmentCorrelThreshold(Double segmentCorrelThreshold) {
/* 200 */     this.segmentCorrelThreshold = segmentCorrelThreshold;
/*     */   }
/*     */   
/*     */   public Double getOverallCorrelThreshold() {
/* 204 */     return this.overallCorrelThreshold;
/*     */   }
/*     */   
/*     */   public void setOverallCorrelThreshold(Double overallCorrelThreshold) {
/* 208 */     this.overallCorrelThreshold = overallCorrelThreshold;
/*     */   }
/*     */   
/*     */   public Integer getCorrelPeriod() {
/* 212 */     return this.correlPeriod;
/*     */   }
/*     */   
/*     */   public void setCorrelPeriod(Integer correlPeriod) {
/* 216 */     this.correlPeriod = correlPeriod;
/*     */   }
/*     */   
/*     */   public ArrayList<String> getFactorList() {
/* 220 */     return this.factorList;
/*     */   }
/*     */   
/*     */   public void setFactorList(ArrayList<String> factorList) {
/* 224 */     this.factorList = factorList;
/*     */   }
/*     */   
/*     */   public VarList getVarList() {
/* 228 */     return this.varList;
/*     */   }
/*     */   
/*     */   public void setVarList(VarList varList)
/*     */   {
/* 233 */     this.varList = varList;
/*     */   }
/*     */   
/*     */   public MLAlgo getMlAlgorithm() {
/* 237 */     return this.mlAlgorithm;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMlAlgorithm(MLAlgo mlAlgorithm, String mainPath)
/*     */   {
/* 243 */     String packageLocR = mlAlgorithm.getModelPackage().replaceAll("\\.", "/");
/* 244 */     String packagePathR = mainPath + "/src/" + packageLocR;
/* 245 */     String algoPathTS = packagePathR + "/" + mlAlgorithm.getModelName();
/* 246 */     File stratFile = new File(algoPathTS);
/* 247 */     SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/* 248 */     String mlAlgoTimeStamp = stratFormat.format(Long.valueOf(stratFile.lastModified()));
/* 249 */     setMlAlgoTimeStamp(mlAlgoTimeStamp);
/*     */     
/* 251 */     this.mlAlgorithm = mlAlgorithm;
/*     */   }
/*     */   
/*     */   public String getMlAlgoTimeStamp() {
/* 255 */     return this.mlAlgoTimeStamp;
/*     */   }
/*     */   
/*     */   public void setMlAlgoTimeStamp(String mlAlgoTimeStamp) {
/* 259 */     this.mlAlgoTimeStamp = mlAlgoTimeStamp;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/MachineLearningParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */