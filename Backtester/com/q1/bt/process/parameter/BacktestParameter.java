/*     */ package com.q1.bt.process.parameter;
/*     */ 
/*     */ import com.q1.bt.driver.backtest.enums.AggregationMode;
/*     */ import com.q1.bt.execution.RolloverMethod;
/*     */ import com.q1.bt.process.backtest.PostProcessMode;
/*     */ import com.q1.bt.process.backtest.SlippageModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BacktestParameter
/*     */ {
/*     */   long startDate;
/*     */   long endDate;
/*     */   Double capital;
/*     */   Double riskPerTrade;
/*     */   boolean skipExistingBacktest;
/*     */   boolean defaultParametersCheck;
/*     */   boolean exportResultsCheck;
/*     */   boolean generateOutputCheck;
/*     */   boolean orderBookBacktest;
/*     */   SlippageModel slippageModel;
/*     */   RolloverMethod rolloverMethod;
/*     */   PostProcessMode postProcessMode;
/*     */   AggregationMode aggregationMode;
/*     */   
/*     */   public BacktestParameter()
/*     */   {
/*  28 */     this.startDate = 0L;
/*  29 */     this.endDate = 20910101L;
/*  30 */     this.capital = Double.valueOf(1.0E7D);
/*  31 */     this.riskPerTrade = Double.valueOf(1.0D);
/*  32 */     this.skipExistingBacktest = false;
/*  33 */     this.defaultParametersCheck = true;
/*  34 */     this.exportResultsCheck = true;
/*  35 */     this.generateOutputCheck = false;
/*  36 */     this.orderBookBacktest = false;
/*  37 */     this.slippageModel = SlippageModel.AdaptiveModelSigma;
/*  38 */     this.rolloverMethod = RolloverMethod.CloseToClose;
/*  39 */     this.postProcessMode = PostProcessMode.SingleScrip;
/*  40 */     this.aggregationMode = AggregationMode.Fixed;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAggregationMode(AggregationMode aggregationMode)
/*     */   {
/*  46 */     this.aggregationMode = aggregationMode;
/*     */   }
/*     */   
/*     */   public void setRolloverMethod(RolloverMethod rolloverMethod) {
/*  50 */     this.rolloverMethod = rolloverMethod;
/*     */   }
/*     */   
/*     */   public void setStartDate(long startDate) {
/*  54 */     this.startDate = startDate;
/*     */   }
/*     */   
/*     */   public void setEndDate(long endDate) {
/*  58 */     this.endDate = endDate;
/*     */   }
/*     */   
/*     */   public void setCapital(Double capital) {
/*  62 */     this.capital = capital;
/*     */   }
/*     */   
/*     */   public void setRiskPerTrade(Double riskPerTrade) {
/*  66 */     this.riskPerTrade = riskPerTrade;
/*     */   }
/*     */   
/*     */   public void setSkipExistingBacktest(boolean skipExistingBacktest) {
/*  70 */     this.skipExistingBacktest = skipExistingBacktest;
/*     */   }
/*     */   
/*     */   public void setDefaultParametersCheck(boolean defaultParametersCheck) {
/*  74 */     this.defaultParametersCheck = defaultParametersCheck;
/*     */   }
/*     */   
/*     */   public void setExportResultsCheck(boolean exportResultsCheck) {
/*  78 */     this.exportResultsCheck = exportResultsCheck;
/*     */   }
/*     */   
/*     */   public void setGenerateOutputCheck(boolean generateOutputCheck) {
/*  82 */     this.generateOutputCheck = generateOutputCheck;
/*     */   }
/*     */   
/*     */   public void setSlippageModel(SlippageModel slippageModel) {
/*  86 */     this.slippageModel = slippageModel;
/*     */   }
/*     */   
/*     */   public void setPostProcessMode(PostProcessMode postProcessMode) {
/*  90 */     this.postProcessMode = postProcessMode;
/*     */   }
/*     */   
/*     */ 
/*     */   public AggregationMode getAggregationMode()
/*     */   {
/*  96 */     return this.aggregationMode;
/*     */   }
/*     */   
/*     */   public RolloverMethod getRolloverMethod() {
/* 100 */     return this.rolloverMethod;
/*     */   }
/*     */   
/*     */   public long getStartDate() {
/* 104 */     return this.startDate;
/*     */   }
/*     */   
/*     */   public long getEndDate() {
/* 108 */     return this.endDate;
/*     */   }
/*     */   
/*     */   public Double getCapital() {
/* 112 */     return this.capital;
/*     */   }
/*     */   
/*     */   public Double getRiskPerTrade() {
/* 116 */     return this.riskPerTrade;
/*     */   }
/*     */   
/*     */   public boolean isSkipExistingBacktest() {
/* 120 */     return this.skipExistingBacktest;
/*     */   }
/*     */   
/*     */   public boolean isDefaultParametersCheck() {
/* 124 */     return this.defaultParametersCheck;
/*     */   }
/*     */   
/*     */   public boolean isExportResultsCheck() {
/* 128 */     return this.exportResultsCheck;
/*     */   }
/*     */   
/*     */   public boolean isGenerateOutputCheck() {
/* 132 */     return this.generateOutputCheck;
/*     */   }
/*     */   
/*     */   public SlippageModel getSlippageModel() {
/* 136 */     return this.slippageModel;
/*     */   }
/*     */   
/*     */   public PostProcessMode getPostProcessMode() {
/* 140 */     return this.postProcessMode;
/*     */   }
/*     */   
/*     */   public boolean isOrderBookBacktest() {
/* 144 */     return this.orderBookBacktest;
/*     */   }
/*     */   
/*     */   public void setOrderBookBacktest(boolean orderBookBacktest) {
/* 148 */     this.orderBookBacktest = orderBookBacktest;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/BacktestParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */