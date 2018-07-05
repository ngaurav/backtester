/*     */ package com.q1.bt.process.parameter;
/*     */ 
/*     */ 
/*     */ public class PackageParameter
/*     */ {
/*     */   String strategyPackage;
/*     */   
/*     */   String scripListPackage;
/*     */   
/*     */   String mlAlgorithmPackage;
/*     */   
/*     */   String mlFactorListPackage;
/*     */   
/*     */   String mlConsolidationFunctionPackage;
/*     */   
/*     */   String aaOptimizationAlgorithmPackage;
/*     */   
/*     */   String aaNormalizationAlgorithmPackage;
/*     */   String aaCovarianceFactorListPackage;
/*     */   String aaCovarianceModelPackage;
/*     */   String aaReturnFactorListPackage;
/*     */   String aaReturnModelPackage;
/*     */   
/*     */   public PackageParameter()
/*     */   {
/*  26 */     this.strategyPackage = "com.q1.ajusal.bt.assetallocator.strategy";
/*  27 */     this.scripListPackage = "com.q1.ajusal.bt.data.scriplist";
/*     */     
/*     */ 
/*  30 */     this.mlAlgorithmPackage = "com.q1.ajusal.bt.machineLearning.algo";
/*  31 */     this.mlFactorListPackage = "com.q1.ajusal.bt.machineLearning.factor";
/*  32 */     this.mlConsolidationFunctionPackage = "com.q1.ajusal.bt.machineLearning.varcreation";
/*     */     
/*     */ 
/*  35 */     this.aaOptimizationAlgorithmPackage = "com.q1.ajusal.bt.assetallocator.optimization";
/*  36 */     this.aaNormalizationAlgorithmPackage = "com.q1.ajusal.bt.assetallocator.portnormalizer";
/*  37 */     this.aaCovarianceFactorListPackage = "com.q1.ajusal.bt.assetallocator.varcreation";
/*  38 */     this.aaCovarianceModelPackage = "com.q1.ajusal.bt.assetallocator.algo";
/*  39 */     this.aaReturnFactorListPackage = "com.q1.ajusal.bt.assetallocator.varcreation";
/*  40 */     this.aaReturnModelPackage = "com.q1.ajusal.bt.assetallocator.algo";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStrategyPackage()
/*     */   {
/*  46 */     return this.strategyPackage;
/*     */   }
/*     */   
/*     */   public void setStrategyPackage(String strategyPackage) {
/*  50 */     this.strategyPackage = strategyPackage;
/*     */   }
/*     */   
/*     */   public String getScripListPackage() {
/*  54 */     return this.scripListPackage;
/*     */   }
/*     */   
/*     */   public void setScripListPackage(String scripListPackage) {
/*  58 */     this.scripListPackage = scripListPackage;
/*     */   }
/*     */   
/*     */   public String getMlAlgorithmPackage() {
/*  62 */     return this.mlAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public void setMlAlgorithmPackage(String mlAlgorithmPackage) {
/*  66 */     this.mlAlgorithmPackage = mlAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public String getMlFactorListPackage() {
/*  70 */     return this.mlFactorListPackage;
/*     */   }
/*     */   
/*     */   public void setMlFactorListPackage(String mlFactorListPackage) {
/*  74 */     this.mlFactorListPackage = mlFactorListPackage;
/*     */   }
/*     */   
/*     */   public String getMlConsolidationFunctionPackage() {
/*  78 */     return this.mlConsolidationFunctionPackage;
/*     */   }
/*     */   
/*     */   public void setMlConsolidationFunctionPackage(String mlConsolidationFunctionPackage) {
/*  82 */     this.mlConsolidationFunctionPackage = mlConsolidationFunctionPackage;
/*     */   }
/*     */   
/*     */   public String getAaOptimizationAlgorithmPackage() {
/*  86 */     return this.aaOptimizationAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public void setAaOptimizationAlgorithmPackage(String aaOptimizationAlgorithmPackage) {
/*  90 */     this.aaOptimizationAlgorithmPackage = aaOptimizationAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public String getAaNormalizationAlgorithmPackage() {
/*  94 */     return this.aaNormalizationAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public void setAaNormalizationAlgorithmPackage(String aaNormalizationAlgorithmPackage) {
/*  98 */     this.aaNormalizationAlgorithmPackage = aaNormalizationAlgorithmPackage;
/*     */   }
/*     */   
/*     */   public String getAaCovarianceFactorListPackage() {
/* 102 */     return this.aaCovarianceFactorListPackage;
/*     */   }
/*     */   
/*     */   public void setAaCovarianceFactorListPackage(String aaCovarianceFactorListPackage) {
/* 106 */     this.aaCovarianceFactorListPackage = aaCovarianceFactorListPackage;
/*     */   }
/*     */   
/*     */   public String getAaCovarianceModelPackage() {
/* 110 */     return this.aaCovarianceModelPackage;
/*     */   }
/*     */   
/*     */   public void setAaCovarianceModelPackage(String aaCovarianceModelPackage) {
/* 114 */     this.aaCovarianceModelPackage = aaCovarianceModelPackage;
/*     */   }
/*     */   
/*     */   public String getAaReturnFactorListPackage() {
/* 118 */     return this.aaReturnFactorListPackage;
/*     */   }
/*     */   
/*     */   public void setAaReturnFactorListPackage(String aaReturnFactorListPackage) {
/* 122 */     this.aaReturnFactorListPackage = aaReturnFactorListPackage;
/*     */   }
/*     */   
/*     */   public String getAaReturnModelPackage() {
/* 126 */     return this.aaReturnModelPackage;
/*     */   }
/*     */   
/*     */   public void setAaReturnModelPackage(String aaReturnModelPackage) {
/* 130 */     this.aaReturnModelPackage = aaReturnModelPackage;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/PackageParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */