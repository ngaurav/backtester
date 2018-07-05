/*     */ package com.q1.bt.driver.backtest;
/*     */ 
/*     */ import com.q1.bt.data.classes.Scrip;
/*     */ import com.q1.bt.driver.backtest.enums.OutputMode;
/*     */ import com.q1.bt.execution.OrderbookStrategy;
/*     */ import com.q1.bt.execution.Strategy;
/*     */ import com.q1.bt.global.BacktesterGlobal;
/*     */ import com.q1.bt.gui.main.ParamUI;
/*     */ import com.q1.bt.process.BacktesterProcess;
/*     */ import com.q1.bt.process.ProcessFlow;
/*     */ import com.q1.bt.process.backtest.SlippageModel;
/*     */ import com.q1.bt.process.objects.Backtest;
/*     */ import com.q1.bt.process.parameter.BacktestParameter;
/*     */ import com.q1.bt.process.parameter.LoginParameter;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class StrategyDriver
/*     */ {
/*     */   String strategyID;
/*     */   String strategyName;
/*     */   String strategyDataType;
/*     */   BacktesterGlobal btGlobal;
/*     */   Backtest backtest;
/*  31 */   ArrayList<String> strategyParameterList = new ArrayList();
/*  32 */   HashMap<String, HashMap<String, HashMap<String, String>>> outputMap = new HashMap();
/*     */   
/*  34 */   String parameterKey = "";
/*     */   
/*  36 */   int finalMode = 0;
/*     */   
/*     */   Long maxEndDate;
/*     */   
/*     */   String strategyPackage;
/*     */   
/*  42 */   public HashMap<String, ScripListDriver> scripListDriverMap = new HashMap();
/*     */   
/*     */   public StrategyDriver(StrategyDriver sD, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
/*     */   {
/*  46 */     this.strategyID = sD.strategyID;
/*  47 */     this.btGlobal = sD.btGlobal;
/*  48 */     this.backtest = sD.backtest;
/*  49 */     this.strategyParameterList = new ArrayList(sD.strategyParameterList);
/*  50 */     this.outputMap = outputMap;
/*  51 */     this.parameterKey = sD.parameterKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StrategyDriver(BacktesterGlobal btGlobal, String strategyID, Backtest backtest, LinkedHashMap<String, ArrayList<Scrip>> scripListMap, HashMap<String, HashMap<String, HashMap<String, String>>> outputMap)
/*     */     throws Exception
/*     */   {
/*  60 */     this.strategyID = strategyID;
/*  61 */     String[] strategyVal = strategyID.split("_");
/*  62 */     this.strategyName = strategyVal[0];
/*  63 */     this.strategyDataType = strategyVal[1];
/*  64 */     this.outputMap = outputMap;
/*     */     
/*     */ 
/*  67 */     this.btGlobal = btGlobal;
/*  68 */     this.backtest = backtest;
/*     */     String scripListID;
/*  70 */     if (backtest.fileBacktest)
/*     */     {
/*  72 */       for (Map.Entry<String, ArrayList<Scrip>> entry : scripListMap.entrySet())
/*     */       {
/*  74 */         scripListID = (String)entry.getKey();
/*  75 */         ArrayList<Scrip> scripSet = (ArrayList)entry.getValue();
/*     */         
/*     */ 
/*  78 */         if (btGlobal.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/*  79 */           btGlobal.displayMessage(strategyID + " " + scripListID + ": Backtest started");
/*     */         }
/*  81 */         ScripListDriver scripListDriver = new ScripListDriver(scripListID, scripSet, strategyID, 
/*  82 */           this.strategyDataType, btGlobal, backtest, this.parameterKey, outputMap);
/*     */         
/*     */ 
/*     */         try
/*     */         {
/*  87 */           Strategy strategy = new OrderbookStrategy(this.strategyDataType, 
/*  88 */             backtest.orderBookPath + "/" + strategyID + " " + scripListID);
/*  89 */           strategy.strategyID = strategyID;
/*     */         } catch (Exception e) {
/*  91 */           btGlobal.displayMessage("Error creating strategy object for: " + strategyID);
/*  92 */           e.printStackTrace();
/*  93 */           continue;
/*     */         }
/*     */         
/*     */         Strategy strategy;
/*  97 */         scripListDriver.createBacktestDriver(strategy);
/*     */         
/*     */ 
/* 100 */         scripListDriver.start();
/*     */         
/*     */ 
/* 103 */         this.scripListDriverMap.put(strategyID + " " + scripListID, scripListDriver);
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 109 */       ArrayList<String[]> inputParameterList = getParameterList();
/*     */       
/*     */ 
/* 112 */       backtest.strategyParameterMap.put(strategyID, inputParameterList);
/*     */       
/*     */ 
/* 115 */       writeParameterListToFile(inputParameterList);
/*     */       
/*     */ 
/* 118 */       for (Object entry : scripListMap.entrySet())
/*     */       {
/* 120 */         String scripListID = (String)((Map.Entry)entry).getKey();
/* 121 */         ArrayList<Scrip> scripSet = (ArrayList)((Map.Entry)entry).getValue();
/*     */         
/*     */ 
/* 124 */         if (btGlobal.processFlow.getCurrentProcess().equals(BacktesterProcess.Backtest)) {
/* 125 */           btGlobal.displayMessage(strategyID + " " + scripListID + ": Backtest started");
/*     */         }
/* 127 */         ScripListDriver scripListDriver = new ScripListDriver(scripListID, scripSet, strategyID, 
/* 128 */           this.strategyDataType, btGlobal, backtest, this.parameterKey, outputMap);
/*     */         
/*     */ 
/* 131 */         if (scripListDriver.outputMode.equals(OutputMode.Normal))
/*     */         {
/*     */ 
/*     */           try
/*     */           {
/*     */ 
/* 137 */             Strategy strategy = getStrategyInstance(scripSet);
/* 138 */             strategy.strategyID = strategyID;
/*     */           } catch (Exception e) {
/* 140 */             btGlobal.displayMessage("Error creating strategy object for: " + strategyID);
/* 141 */             e.printStackTrace();
/* 142 */             continue;
/*     */           }
/*     */           
/*     */           Strategy strategy;
/* 146 */           scripListDriver.createBacktestDriver(strategy);
/*     */           
/*     */ 
/* 149 */           scripListDriver.start();
/*     */           
/*     */ 
/* 152 */           this.scripListDriverMap.put(strategyID + " " + scripListID, scripListDriver);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static String createDataPath(String folder)
/*     */   {
/* 160 */     if (!new File(folder).exists())
/* 161 */       new File(folder).mkdirs();
/* 162 */     return folder;
/*     */   }
/*     */   
/*     */   public Strategy getStrategyInstance(ArrayList<Scrip> scripSet)
/*     */     throws Exception
/*     */   {
/* 168 */     Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyID);
/* 169 */     Constructor<?> stratConstructor = stratClass.getConstructor(new Class[] { ArrayList.class, ArrayList.class });
/* 170 */     Strategy stratObject = 
/* 171 */       (Strategy)stratConstructor.newInstance(new Object[] { this.strategyParameterList, scripSet });
/* 172 */     return stratObject;
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<String[]> getParameterList()
/*     */     throws Exception
/*     */   {
/* 179 */     ArrayList<String[]> inputParameterList = (ArrayList)this.backtest.strategyParameterMap.get(this.strategyID);
/*     */     
/*     */ 
/* 182 */     if (inputParameterList == null) {
/* 183 */       inputParameterList = new ArrayList();
/*     */     }
/*     */     
/* 186 */     this.strategyPackage = this.btGlobal.packageParameter.getStrategyPackage();
/* 187 */     if (inputParameterList.size() > 0) {
/* 188 */       return inputParameterList;
/*     */     }
/*     */     
/* 191 */     if (this.backtest.backtestParameter.isDefaultParametersCheck()) {
/* 192 */       Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyID);
/* 193 */       Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/* 194 */       Strategy stratTempObject = (Strategy)stratTempConstructor.newInstance(new Object[0]);
/* 195 */       return stratTempObject.getParameterList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 200 */     ParamUI pUI = new ParamUI(this.strategyID, this.strategyPackage);
/* 201 */     return pUI.getParameters();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writeParameterListToFile(ArrayList<String[]> inputParameterList)
/*     */     throws java.io.IOException
/*     */   {
/* 209 */     String[] stratFolder = this.strategyPackage.split("\\.");
/* 210 */     String stratPath = "";
/* 211 */     String[] arrayOfString1; int j = (arrayOfString1 = stratFolder).length; for (int i = 0; i < j; i++) { String sF = arrayOfString1[i];
/* 212 */       stratPath = stratPath + "/" + sF; }
/* 213 */     File stratFile = new File(
/* 214 */       this.btGlobal.loginParameter.getMainPath() + "/src" + stratPath + "/" + this.strategyID + ".java");
/* 215 */     SimpleDateFormat stratFormat = new SimpleDateFormat("yyyyMMddHHmmss");
/* 216 */     String timeStamp = stratFormat.format(Long.valueOf(stratFile.lastModified()));
/*     */     
/* 218 */     String outPath = this.btGlobal.loginParameter.getOutputPath() + "/" + this.backtest.timeStamp + "/Parameters";
/* 219 */     if (!new File(outPath).exists())
/* 220 */       new File(outPath).mkdirs();
/* 221 */     String paramPath = outPath + "/" + this.strategyID + " Parameters.csv";
/*     */     
/*     */ 
/* 224 */     CSVWriter writer = new CSVWriter(paramPath, false, ",");
/*     */     
/*     */ 
/* 227 */     ArrayList<String> parameterList = new ArrayList();
/*     */     
/*     */ 
/* 230 */     String[] tsLine = { "Strategy Timestamp", timeStamp };
/* 231 */     parameterList.add(timeStamp);
/* 232 */     writer.writeLine(tsLine);
/*     */     
/*     */ 
/* 235 */     Double capital = this.btGlobal.loginParameter.getCapital();
/*     */     String capOut;
/* 237 */     String capOut; if (Double.compare(Math.floor(capital.doubleValue()), capital.doubleValue()) == 0) {
/* 238 */       Long cInt = Long.valueOf(capital.longValue());
/* 239 */       capOut = cInt.toString();
/*     */     } else {
/* 241 */       capOut = capital.toString();
/*     */     }
/* 243 */     String[] capLine = { "Capital", capOut };
/* 244 */     parameterList.add(capOut);
/* 245 */     writer.writeLine(capLine);
/*     */     
/*     */ 
/* 248 */     Double rpt = this.btGlobal.loginParameter.getRiskPerTrade();
/*     */     String rptOut;
/* 250 */     String rptOut; if (Double.compare(Math.floor(rpt.doubleValue()), rpt.doubleValue()) == 0) {
/* 251 */       Integer cInt = Integer.valueOf(rpt.intValue());
/* 252 */       rptOut = cInt.toString();
/*     */     } else {
/* 254 */       rptOut = rpt.toString();
/*     */     }
/* 256 */     String[] rptLine = { "Risk Per Trade", rptOut };
/* 257 */     parameterList.add(rptOut);
/* 258 */     writer.writeLine(rptLine);
/*     */     
/*     */ 
/* 261 */     if (this.backtest.backtestParameter.isExportResultsCheck()) {
/* 262 */       String[] outputLine = { "Export Results", "1" };
/* 263 */       parameterList.add("1");
/* 264 */       writer.writeLine(outputLine);
/*     */     } else {
/* 266 */       String[] outputLine = { "Export Results", "0" };
/* 267 */       parameterList.add("0");
/* 268 */       writer.writeLine(outputLine);
/*     */     }
/*     */     
/*     */ 
/* 272 */     if (this.backtest.backtestParameter.isGenerateOutputCheck()) {
/* 273 */       String[] outputLine = { "Output", "1" };
/* 274 */       parameterList.add("1");
/* 275 */       writer.writeLine(outputLine);
/*     */     } else {
/* 277 */       String[] outputLine = { "Output", "0" };
/* 278 */       parameterList.add("0");
/* 279 */       writer.writeLine(outputLine);
/*     */     }
/*     */     
/*     */ 
/* 283 */     String slippageModelStr = this.backtest.backtestParameter.getSlippageModel().toString();
/* 284 */     String[] slippageLine = { "Slippage Model", slippageModelStr };
/* 285 */     parameterList.add(slippageModelStr);
/* 286 */     writer.writeLine(slippageLine);
/*     */     
/*     */ 
/* 289 */     String rolloverMethodStr = this.backtest.backtestParameter.getRolloverMethod().toString();
/* 290 */     String[] rollLine = { "Rollover Method", rolloverMethodStr };
/* 291 */     parameterList.add(rolloverMethodStr);
/* 292 */     writer.writeLine(rollLine);
/*     */     
/*     */ 
/* 295 */     String postProcessModeStr = this.backtest.backtestParameter.getPostProcessMode().toString();
/* 296 */     String[] postProcessLine = { "Post Process Mode", postProcessModeStr };
/* 297 */     parameterList.add(postProcessModeStr);
/* 298 */     writer.writeLine(postProcessLine);
/*     */     
/*     */ 
/* 301 */     for (String[] param : inputParameterList) {
/*     */       try {
/* 303 */         Double p = Double.valueOf(Double.parseDouble(param[1]));
/* 304 */         if (Double.compare(Math.floor(p.doubleValue()), p.doubleValue()) == 0) {
/* 305 */           Integer pInt = Integer.valueOf(p.intValue());
/* 306 */           String pStr = pInt.toString();
/* 307 */           parameterList.add(pStr);
/* 308 */           this.strategyParameterList.add(pStr);
/* 309 */           param[1] = pInt.toString();
/*     */         } else {
/* 311 */           parameterList.add(param[1]);
/* 312 */           this.strategyParameterList.add(param[1]);
/*     */         }
/*     */       } catch (NumberFormatException ne) {
/* 315 */         parameterList.add(param[1]);
/* 316 */         this.strategyParameterList.add(param[1]);
/*     */       }
/*     */       
/* 319 */       String[] paramLine = { param[0], param[1] };
/* 320 */       writer.writeLine(paramLine);
/*     */     }
/* 322 */     writer.close();
/*     */     
/* 324 */     this.parameterKey = "";
/* 325 */     for (String param : parameterList) {
/* 326 */       if (this.parameterKey.equals("")) {
/* 327 */         this.parameterKey = param;
/*     */       } else {
/* 329 */         this.parameterKey = (this.parameterKey + "$" + param);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/driver/backtest/StrategyDriver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */