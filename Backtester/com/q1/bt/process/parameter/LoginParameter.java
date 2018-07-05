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
/*     */   com.q1.bt.execution.RolloverMethod defaultRolloverMethod;
/*     */   
/*     */   public LoginParameter()
/*     */   {
/*  25 */     this.sqlIPAddress = "127.0.0.1";
/*  26 */     this.sqlDatabase = "quantdatabase";
/*  27 */     this.sqlUsername = "root";
/*  28 */     this.sqlPassword = "";
/*  29 */     this.dataPath = null;
/*  30 */     this.mainPath = createPath("");
/*  31 */     this.outputPath = createPath("/Output");
/*  32 */     this.sensitivityPath = createPath("/Sensitivity Output");
/*  33 */     this.capital = Double.valueOf(500000.0D);
/*  34 */     this.riskPerTrade = Double.valueOf(1.0D);
/*  35 */     this.process = com.q1.bt.process.BacktesterProcess.Backtest;
/*  36 */     this.defaultSlippageModel = com.q1.bt.process.backtest.SlippageModel.AdaptiveModelSigma;
/*  37 */     this.defaultRolloverMethod = com.q1.bt.execution.RolloverMethod.CloseToClose;
/*     */   }
/*     */   
/*     */ 
/*     */   public String createPath(String folder)
/*     */   {
/*  43 */     String path = LoginParameter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
/*     */     
/*     */     try
/*     */     {
/*  47 */       String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
/*  48 */       String[] acc = currentPath.split("/");
/*  49 */       String outPath = acc[0];
/*  50 */       for (int i = 1; i < acc.length - 1; i++) {
/*  51 */         if (acc[i].equals("lib"))
/*     */           break;
/*  53 */         outPath = outPath + "/" + acc[i];
/*     */       }
/*     */       
/*  56 */       String dataPath = outPath + folder;
/*  57 */       if (!new File(dataPath).exists())
/*  58 */         new File(dataPath).mkdirs();
/*     */     } catch (java.io.UnsupportedEncodingException e) {
/*  60 */       e.printStackTrace();
/*  61 */       return createPath(folder); }
/*     */     String dataPath;
/*  63 */     String currentPath; return dataPath;
/*     */   }
/*     */   
/*     */   public com.q1.bt.execution.RolloverMethod getDefaultRolloverMethod()
/*     */   {
/*  68 */     return this.defaultRolloverMethod;
/*     */   }
/*     */   
/*     */   public void setDefaultRolloverMethod(com.q1.bt.execution.RolloverMethod defaultRolloverMethod) {
/*  72 */     this.defaultRolloverMethod = defaultRolloverMethod;
/*     */   }
/*     */   
/*     */   public com.q1.bt.process.backtest.SlippageModel getDefaultSlippageModel() {
/*  76 */     return this.defaultSlippageModel;
/*     */   }
/*     */   
/*     */   public void setDefaultSlippageModel(com.q1.bt.process.backtest.SlippageModel defaultSlippageModel) {
/*  80 */     this.defaultSlippageModel = defaultSlippageModel;
/*     */   }
/*     */   
/*     */   public String getSqlIPAddress() {
/*  84 */     return this.sqlIPAddress;
/*     */   }
/*     */   
/*     */   public void setSqlIPAddress(String sqlIPAddress) {
/*  88 */     this.sqlIPAddress = sqlIPAddress;
/*     */   }
/*     */   
/*     */   public String getSqlDatabase() {
/*  92 */     return this.sqlDatabase;
/*     */   }
/*     */   
/*     */   public void setSqlDatabase(String sqlDatabase) {
/*  96 */     this.sqlDatabase = sqlDatabase;
/*     */   }
/*     */   
/*     */   public String getSqlUsername() {
/* 100 */     return this.sqlUsername;
/*     */   }
/*     */   
/*     */   public void setSqlUsername(String sqlUsername) {
/* 104 */     this.sqlUsername = sqlUsername;
/*     */   }
/*     */   
/*     */   public String getSqlPassword() {
/* 108 */     return this.sqlPassword;
/*     */   }
/*     */   
/*     */   public void setSqlPassword(String sqlPassword) {
/* 112 */     this.sqlPassword = sqlPassword;
/*     */   }
/*     */   
/*     */   public com.q1.bt.process.BacktesterProcess getProcess() {
/* 116 */     return this.process;
/*     */   }
/*     */   
/*     */   public void setProcess(com.q1.bt.process.BacktesterProcess process) {
/* 120 */     this.process = process;
/*     */   }
/*     */   
/*     */   public String getDataPath() {
/* 124 */     return this.dataPath;
/*     */   }
/*     */   
/*     */   public void setDataPath(String dataPath) {
/* 128 */     this.dataPath = dataPath;
/*     */   }
/*     */   
/*     */   public String getMainPath() {
/* 132 */     return this.mainPath;
/*     */   }
/*     */   
/*     */   public void setMainPath(String mainPath) {
/* 136 */     this.mainPath = mainPath;
/*     */   }
/*     */   
/*     */   public String getOutputPath() {
/* 140 */     return this.outputPath;
/*     */   }
/*     */   
/*     */   public void setOutputPath(String outputPath) {
/* 144 */     this.outputPath = outputPath;
/*     */   }
/*     */   
/*     */   public Double getCapital() {
/* 148 */     return this.capital;
/*     */   }
/*     */   
/*     */   public void setCapital(Double capital) {
/* 152 */     this.capital = capital;
/*     */   }
/*     */   
/*     */   public Double getRiskPerTrade() {
/* 156 */     return this.riskPerTrade;
/*     */   }
/*     */   
/*     */   public void setRiskPerTrade(Double riskPerTrade) {
/* 160 */     this.riskPerTrade = riskPerTrade;
/*     */   }
/*     */   
/*     */   public Long getDefaultStartDate() {
/* 164 */     return this.defStartDate;
/*     */   }
/*     */   
/*     */   public void setDefaultStartDate(Long defStartDate) {
/* 168 */     this.defStartDate = defStartDate;
/*     */   }
/*     */   
/*     */   public Long getDefaultEndDate() {
/* 172 */     return this.defEndDate;
/*     */   }
/*     */   
/*     */   public void setDefaultEndDate(Long defEndDate) {
/* 176 */     this.defEndDate = defEndDate;
/*     */   }
/*     */   
/*     */   public String getSensitivityPath() {
/* 180 */     return this.sensitivityPath;
/*     */   }
/*     */   
/*     */   public void setSensitivityPath(String sensitivityPath) {
/* 184 */     if (!new File(sensitivityPath).exists())
/* 185 */       new File(sensitivityPath).mkdirs();
/* 186 */     this.sensitivityPath = sensitivityPath;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/LoginParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */