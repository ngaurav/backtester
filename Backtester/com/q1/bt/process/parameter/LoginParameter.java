/*     */ package com.q1.bt.process.parameter;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class LoginParameter
/*     */ {
/*     */   String sqlIPAddress;
/*     */   String sqlDatabase;
/*     */   String sqlUsername;
/*     */   String sqlPassword;
/*     */   com.q1.bt.process.BacktesterProcess process;
/*     */   String dataPath;
/*     */   String mainPath;
/*     */   String outputPath;
/*     */   String sensitivityPath;
/*     */   Double capital;
/*     */   Double riskPerTrade;
/*     */   Long defStartDate;
/*     */   Long defEndDate;
/*     */   com.q1.bt.process.backtest.SlippageModel defaultSlippageModel;
/*     */   
/*     */   public LoginParameter() {
/*  23 */     this.sqlIPAddress = "127.0.0.1";
/*  24 */     this.sqlDatabase = "quantdatabase";
/*  25 */     this.sqlUsername = "root";
/*  26 */     this.sqlPassword = "";
/*  27 */     this.dataPath = null;
/*  28 */     this.mainPath = createPath("");
/*  29 */     this.outputPath = createPath("/Output");
/*  30 */     this.sensitivityPath = createPath("/Sensitivity Output");
/*  31 */     this.capital = Double.valueOf(500000.0D);
/*  32 */     this.riskPerTrade = Double.valueOf(1.0D);
/*  33 */     this.process = com.q1.bt.process.BacktesterProcess.Backtest;
/*  34 */     this.defaultSlippageModel = com.q1.bt.process.backtest.SlippageModel.AdaptiveModelSigma;
/*     */   }
/*     */   
/*     */ 
/*     */   public String createPath(String folder)
/*     */   {
/*  40 */     String path = LoginParameter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
/*     */     
/*     */     try
/*     */     {
/*  44 */       String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
/*  45 */       String[] acc = currentPath.split("/");
/*  46 */       String outPath = acc[0];
/*  47 */       for (int i = 1; i < acc.length - 1; i++) {
/*  48 */         if (acc[i].equals("lib"))
/*     */           break;
/*  50 */         outPath = outPath + "/" + acc[i];
/*     */       }
/*     */       
/*  53 */       String dataPath = outPath + folder;
/*  54 */       if (!new File(dataPath).exists())
/*  55 */         new File(dataPath).mkdirs();
/*     */     } catch (java.io.UnsupportedEncodingException e) {
/*  57 */       e.printStackTrace();
/*  58 */       return createPath(folder); }
/*     */     String dataPath;
/*  60 */     String currentPath; return dataPath;
/*     */   }
/*     */   
/*     */   public com.q1.bt.process.backtest.SlippageModel getDefaultSlippageModel()
/*     */   {
/*  65 */     return this.defaultSlippageModel;
/*     */   }
/*     */   
/*     */   public void setDefaultSlippageModel(com.q1.bt.process.backtest.SlippageModel defaultSlippageModel) {
/*  69 */     this.defaultSlippageModel = defaultSlippageModel;
/*     */   }
/*     */   
/*     */   public String getSqlIPAddress() {
/*  73 */     return this.sqlIPAddress;
/*     */   }
/*     */   
/*     */   public void setSqlIPAddress(String sqlIPAddress) {
/*  77 */     this.sqlIPAddress = sqlIPAddress;
/*     */   }
/*     */   
/*     */   public String getSqlDatabase() {
/*  81 */     return this.sqlDatabase;
/*     */   }
/*     */   
/*     */   public void setSqlDatabase(String sqlDatabase) {
/*  85 */     this.sqlDatabase = sqlDatabase;
/*     */   }
/*     */   
/*     */   public String getSqlUsername() {
/*  89 */     return this.sqlUsername;
/*     */   }
/*     */   
/*     */   public void setSqlUsername(String sqlUsername) {
/*  93 */     this.sqlUsername = sqlUsername;
/*     */   }
/*     */   
/*     */   public String getSqlPassword() {
/*  97 */     return this.sqlPassword;
/*     */   }
/*     */   
/*     */   public void setSqlPassword(String sqlPassword) {
/* 101 */     this.sqlPassword = sqlPassword;
/*     */   }
/*     */   
/*     */   public com.q1.bt.process.BacktesterProcess getProcess() {
/* 105 */     return this.process;
/*     */   }
/*     */   
/*     */   public void setProcess(com.q1.bt.process.BacktesterProcess process) {
/* 109 */     this.process = process;
/*     */   }
/*     */   
/*     */   public String getDataPath() {
/* 113 */     return this.dataPath;
/*     */   }
/*     */   
/*     */   public void setDataPath(String dataPath) {
/* 117 */     this.dataPath = dataPath;
/*     */   }
/*     */   
/*     */   public String getMainPath() {
/* 121 */     return this.mainPath;
/*     */   }
/*     */   
/*     */   public void setMainPath(String mainPath) {
/* 125 */     this.mainPath = mainPath;
/*     */   }
/*     */   
/*     */   public String getOutputPath() {
/* 129 */     return this.outputPath;
/*     */   }
/*     */   
/*     */   public void setOutputPath(String outputPath) {
/* 133 */     this.outputPath = outputPath;
/*     */   }
/*     */   
/*     */   public Double getCapital() {
/* 137 */     return this.capital;
/*     */   }
/*     */   
/*     */   public void setCapital(Double capital) {
/* 141 */     this.capital = capital;
/*     */   }
/*     */   
/*     */   public Double getRiskPerTrade() {
/* 145 */     return this.riskPerTrade;
/*     */   }
/*     */   
/*     */   public void setRiskPerTrade(Double riskPerTrade) {
/* 149 */     this.riskPerTrade = riskPerTrade;
/*     */   }
/*     */   
/*     */   public Long getDefaultStartDate() {
/* 153 */     return this.defStartDate;
/*     */   }
/*     */   
/*     */   public void setDefaultStartDate(Long defStartDate) {
/* 157 */     this.defStartDate = defStartDate;
/*     */   }
/*     */   
/*     */   public Long getDefaultEndDate() {
/* 161 */     return this.defEndDate;
/*     */   }
/*     */   
/*     */   public void setDefaultEndDate(Long defEndDate) {
/* 165 */     this.defEndDate = defEndDate;
/*     */   }
/*     */   
/*     */   public String getSensitivityPath() {
/* 169 */     return this.sensitivityPath;
/*     */   }
/*     */   
/*     */   public void setSensitivityPath(String sensitivityPath) {
/* 173 */     if (!new File(sensitivityPath).exists())
/* 174 */       new File(sensitivityPath).mkdirs();
/* 175 */     this.sensitivityPath = sensitivityPath;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/LoginParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */