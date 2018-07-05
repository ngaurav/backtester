/*     */ package com.q1.bt.process.parameter;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ public class LoginParameter {
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
/*     */   
/*     */   public LoginParameter() {
/*  21 */     this.sqlIPAddress = "127.0.0.1";
/*  22 */     this.sqlDatabase = "quantdatabase";
/*  23 */     this.sqlUsername = "root";
/*  24 */     this.sqlPassword = "";
/*  25 */     this.dataPath = null;
/*  26 */     this.mainPath = createPath("");
/*  27 */     this.outputPath = createPath("/Output");
/*  28 */     this.sensitivityPath = createPath("/Sensitivity Output");
/*  29 */     this.capital = Double.valueOf(500000.0D);
/*  30 */     this.riskPerTrade = Double.valueOf(1.0D);
/*  31 */     this.process = com.q1.bt.process.BacktesterProcess.Backtest;
/*     */   }
/*     */   
/*     */ 
/*     */   public String createPath(String folder)
/*     */   {
/*  37 */     String path = LoginParameter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
/*     */     
/*     */     try
/*     */     {
/*  41 */       String currentPath = java.net.URLDecoder.decode(path, "UTF-8").substring(1);
/*  42 */       String[] acc = currentPath.split("/");
/*  43 */       String outPath = acc[0];
/*  44 */       for (int i = 1; i < acc.length - 1; i++) {
/*  45 */         if (acc[i].equals("lib"))
/*     */           break;
/*  47 */         outPath = outPath + "/" + acc[i];
/*     */       }
/*     */       
/*  50 */       String dataPath = outPath + folder;
/*  51 */       if (!new File(dataPath).exists())
/*  52 */         new File(dataPath).mkdirs();
/*     */     } catch (java.io.UnsupportedEncodingException e) {
/*  54 */       e.printStackTrace();
/*  55 */       return createPath(folder); }
/*     */     String dataPath;
/*  57 */     String currentPath; return dataPath;
/*     */   }
/*     */   
/*     */   public String getSqlIPAddress()
/*     */   {
/*  62 */     return this.sqlIPAddress;
/*     */   }
/*     */   
/*     */   public void setSqlIPAddress(String sqlIPAddress) {
/*  66 */     this.sqlIPAddress = sqlIPAddress;
/*     */   }
/*     */   
/*     */   public String getSqlDatabase() {
/*  70 */     return this.sqlDatabase;
/*     */   }
/*     */   
/*     */   public void setSqlDatabase(String sqlDatabase) {
/*  74 */     this.sqlDatabase = sqlDatabase;
/*     */   }
/*     */   
/*     */   public String getSqlUsername() {
/*  78 */     return this.sqlUsername;
/*     */   }
/*     */   
/*     */   public void setSqlUsername(String sqlUsername) {
/*  82 */     this.sqlUsername = sqlUsername;
/*     */   }
/*     */   
/*     */   public String getSqlPassword() {
/*  86 */     return this.sqlPassword;
/*     */   }
/*     */   
/*     */   public void setSqlPassword(String sqlPassword) {
/*  90 */     this.sqlPassword = sqlPassword;
/*     */   }
/*     */   
/*     */   public com.q1.bt.process.BacktesterProcess getProcess() {
/*  94 */     return this.process;
/*     */   }
/*     */   
/*     */   public void setProcess(com.q1.bt.process.BacktesterProcess process) {
/*  98 */     this.process = process;
/*     */   }
/*     */   
/*     */   public String getDataPath() {
/* 102 */     return this.dataPath;
/*     */   }
/*     */   
/*     */   public void setDataPath(String dataPath) {
/* 106 */     this.dataPath = dataPath;
/*     */   }
/*     */   
/*     */   public String getMainPath() {
/* 110 */     return this.mainPath;
/*     */   }
/*     */   
/*     */   public void setMainPath(String mainPath) {
/* 114 */     this.mainPath = mainPath;
/*     */   }
/*     */   
/*     */   public String getOutputPath() {
/* 118 */     return this.outputPath;
/*     */   }
/*     */   
/*     */   public void setOutputPath(String outputPath) {
/* 122 */     this.outputPath = outputPath;
/*     */   }
/*     */   
/*     */   public Double getCapital() {
/* 126 */     return this.capital;
/*     */   }
/*     */   
/*     */   public void setCapital(Double capital) {
/* 130 */     this.capital = capital;
/*     */   }
/*     */   
/*     */   public Double getRiskPerTrade() {
/* 134 */     return this.riskPerTrade;
/*     */   }
/*     */   
/*     */   public void setRiskPerTrade(Double riskPerTrade) {
/* 138 */     this.riskPerTrade = riskPerTrade;
/*     */   }
/*     */   
/*     */   public Long getDefaultStartDate() {
/* 142 */     return this.defStartDate;
/*     */   }
/*     */   
/*     */   public void setDefaultStartDate(Long defStartDate) {
/* 146 */     this.defStartDate = defStartDate;
/*     */   }
/*     */   
/*     */   public Long getDefaultEndDate() {
/* 150 */     return this.defEndDate;
/*     */   }
/*     */   
/*     */   public void setDefaultEndDate(Long defEndDate) {
/* 154 */     this.defEndDate = defEndDate;
/*     */   }
/*     */   
/*     */   public String getSensitivityPath() {
/* 158 */     return this.sensitivityPath;
/*     */   }
/*     */   
/*     */   public void setSensitivityPath(String sensitivityPath) {
/* 162 */     if (!new File(sensitivityPath).exists())
/* 163 */       new File(sensitivityPath).mkdirs();
/* 164 */     this.sensitivityPath = sensitivityPath;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/process/parameter/LoginParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */