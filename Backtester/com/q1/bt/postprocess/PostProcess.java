/*     */ package com.q1.bt.postprocess;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ 
/*     */ public class PostProcess { public Double mtmSharpe;
/*     */   public Double mtmSortino;
/*     */   public Double mtmStdDev;
/*     */   public Double mtmCalmar;
/*     */   public Double mtmSmoothCalmar;
/*     */   public Double mtmHitRate;
/*     */   public Double mtmWinLoss;
/*     */   public Double trades;
/*     */   public Double maxDrawdown;
/*     */   public Double avgDrawdown;
/*     */   public Double avgDrawdownDuration;
/*     */   public Double profitFactor;
/*     */   public Long maxDrawdownDuration;
/*     */   public Double avgDailyReturn;
/*     */   public Double avgTradeReturn;
/*     */   public Double slippageFactor;
/*     */   public Double normSlippageFactor;
/*     */   public Double expectancy;
/*     */   public Double monthlyHitRate;
/*     */   public Double monthlyWinLoss;
/*     */   public Double avgTradeDuration;
/*     */   public Double avgWinDuration;
/*     */   public Double avgLossDuration;
/*     */   public Double maxTradesPerDay;
/*     */   public Double avgTradePerDay;
/*     */   public Double avgSlippage;
/*     */   public Double avgOpenSlippage;
/*     */   public Double opTradePerc;
/*     */   public Double avgNormSlippage;
/*     */   public Integer tradeCount;
/*     */   public Double avgWinTradePL;
/*     */   public Double avgLossTradePL;
/*     */   public Double tradeHitRate;
/*     */   public Double tradeWinLoss;
/*     */   public Double annualReturn;
/*     */   public Double annualVolatility;
/*  41 */   public Double annualHitRate; public Double annualWinLoss; public Double tradingDays; public Double avgLeverage; public Double maxLeverage; public java.util.ArrayList<String[]> tradeBook = new java.util.ArrayList();
/*  42 */   public java.util.TreeMap<Long, Double> consolMTM = new java.util.TreeMap();
/*     */   public com.q1.bt.global.BacktesterGlobal btGlobal;
/*     */   public String ppName;
/*  45 */   java.util.HashMap<String, Integer> tbIdxMap = getTradeBookIndexMap();
/*     */   
/*     */   com.q1.bt.process.backtest.PostProcessMode postProcessMode;
/*     */   
/*     */ 
/*     */   public PostProcess(java.util.TreeMap<Long, Double> consolMTM, String ppName, java.util.ArrayList<String[]> tradeBook, com.q1.bt.global.BacktesterGlobal btGlobal, com.q1.bt.process.backtest.PostProcessMode postProcessMode)
/*     */   {
/*  52 */     this.consolMTM = trimMTMMap(consolMTM);
/*  53 */     this.ppName = ppName;
/*  54 */     this.tradeBook = tradeBook;
/*  55 */     this.btGlobal = btGlobal;
/*  56 */     this.postProcessMode = postProcessMode;
/*     */   }
/*     */   
/*     */ 
/*     */   public java.util.TreeMap<Long, Double> trimMTMMap(java.util.TreeMap<Long, Double> consolMTM)
/*     */   {
/*  62 */     java.util.TreeMap<Long, Double> outputMTM = new java.util.TreeMap();
/*     */     
/*  64 */     boolean checkMTM = false;
/*     */     
/*  66 */     for (java.util.Map.Entry<Long, Double> entry : consolMTM.entrySet())
/*     */     {
/*  68 */       Double mtm = (Double)entry.getValue();
/*     */       
/*  70 */       if ((!checkMTM) && (com.q1.math.MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() != 0)) {
/*  71 */         checkMTM = true;
/*     */       }
/*  73 */       if (checkMTM) {
/*  74 */         outputMTM.put((Long)entry.getKey(), mtm);
/*     */       }
/*     */     }
/*     */     
/*  78 */     return outputMTM;
/*     */   }
/*     */   
/*     */   public void writeToFileForFolder(String outputPath) throws java.io.IOException
/*     */   {
/*  83 */     String filePath = outputPath + "/Results";
/*     */     
/*  85 */     LinkedHashMap<String, String> resultsMap = new LinkedHashMap();
/*     */     
/*  87 */     if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
/*  88 */       resultsMap.put("Annual Return", "0.0%");
/*  89 */       resultsMap.put("Annual Vol", "0.0%");
/*  90 */       resultsMap.put("Return Per Trade", "0.0%");
/*  91 */       resultsMap.put("Sharpe Ratio", "0.0");
/*  92 */       resultsMap.put("Sortino Ratio", "0.0");
/*  93 */       resultsMap.put("Calmar Ratio", "0.0");
/*  94 */       resultsMap.put("Smooth Calmar Ratio", "0.0");
/*  95 */       resultsMap.put("Max Drawdown", "0.0%");
/*  96 */       resultsMap.put("Avg Drawdown", "0.0%");
/*  97 */       resultsMap.put("Max Drawdown Duration", "0 days");
/*  98 */       resultsMap.put("Avg Drawdown Duration", "0 days");
/*  99 */       resultsMap.put("Daily Win Loss", "0.0");
/* 100 */       resultsMap.put("Daily Hit Rate", "0.0%");
/* 101 */       resultsMap.put("Daily Expectancy", "0.0");
/* 102 */       resultsMap.put("Profit Factor", "0.0");
/* 103 */       resultsMap.put("Avg Trade Duration", "0 days");
/* 104 */       resultsMap.put("Win-Trade Duration", "0 days");
/* 105 */       resultsMap.put("Loss-Trade Duration", "0 days");
/* 106 */       resultsMap.put("Average Slippage", "0.0%");
/* 107 */       resultsMap.put("Trade Win Loss", "0.0");
/* 108 */       resultsMap.put("Trade Hit Rate", "0.0%");
/* 109 */       resultsMap.put("Trade Count", "0");
/* 110 */       resultsMap.put("Trading Days", "0%");
/* 111 */       resultsMap.put("Max Trades Per Day", "0.0");
/* 112 */       resultsMap.put("Avg Trades Per Day", "0.0");
/* 113 */       resultsMap.put("Open Slippage", "0.0%");
/* 114 */       resultsMap.put("Normal Slippage", "0.0%");
/* 115 */       resultsMap.put("Slippage Factor", "0.0");
/* 116 */       resultsMap.put("Normal Slippage Factor", "0.0");
/* 117 */       resultsMap.put("Open Trades", "0.0%");
/* 118 */       resultsMap.put("Avg Leverage", "0.0");
/* 119 */       resultsMap.put("Max Leverage", "0.0");
/*     */     } else {
/* 121 */       resultsMap.put("Annual Return", this.annualReturn + "%");
/* 122 */       resultsMap.put("Annual Vol", this.annualVolatility + "%");
/* 123 */       resultsMap.put("Return Per Trade", this.avgTradeReturn + "%");
/* 124 */       resultsMap.put("Sharpe Ratio", this.mtmSharpe.toString());
/* 125 */       resultsMap.put("Sortino Ratio", this.mtmSortino.toString());
/* 126 */       resultsMap.put("Calmar Ratio", this.mtmCalmar.toString());
/* 127 */       resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar.toString());
/* 128 */       resultsMap.put("Max Drawdown", this.maxDrawdown + "%");
/* 129 */       resultsMap.put("Avg Drawdown", this.avgDrawdown + "%");
/* 130 */       resultsMap.put("Max Drawdown Duration", this.maxDrawdownDuration + " days");
/* 131 */       resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration + " days");
/* 132 */       resultsMap.put("Daily Win Loss", this.mtmWinLoss.toString());
/* 133 */       resultsMap.put("Daily Hit Rate", this.mtmHitRate + "%");
/* 134 */       resultsMap.put("Daily Expectancy", this.expectancy.toString());
/* 135 */       resultsMap.put("Profit Factor", this.profitFactor.toString());
/* 136 */       resultsMap.put("Avg Trade Duration", getDurationVal(this.avgTradeDuration));
/* 137 */       resultsMap.put("Win-Trade Duration", getDurationVal(this.avgWinDuration));
/* 138 */       resultsMap.put("Loss-Trade Duration", getDurationVal(this.avgLossDuration));
/* 139 */       resultsMap.put("Average Slippage", this.avgSlippage + "%");
/* 140 */       resultsMap.put("Trade Win Loss", this.tradeWinLoss.toString());
/* 141 */       resultsMap.put("Trade Hit Rate", this.tradeHitRate + "%");
/* 142 */       resultsMap.put("Trade Count", this.tradeCount.toString());
/* 143 */       resultsMap.put("Trading Days", this.tradingDays + "%");
/* 144 */       resultsMap.put("Max Trades Per Day", this.maxTradesPerDay.toString());
/* 145 */       resultsMap.put("Avg Trades Per Day", this.avgTradePerDay.toString());
/* 146 */       resultsMap.put("Open Slippage", this.avgOpenSlippage + "%");
/* 147 */       resultsMap.put("Normal Slippage", this.avgNormSlippage + "%");
/* 148 */       resultsMap.put("Slippage Factor", this.slippageFactor);
/* 149 */       resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
/* 150 */       resultsMap.put("Open Trades", this.opTradePerc + "%");
/* 151 */       resultsMap.put("Avg Leverage", this.avgLeverage.toString());
/* 152 */       resultsMap.put("Max Leverage", this.maxLeverage.toString());
/*     */     }
/*     */     
/* 155 */     java.util.ArrayList<String[]> currentData = new java.util.ArrayList();
/* 156 */     if (new java.io.File(filePath + "/Performance.csv").exists()) {
/* 157 */       com.q1.csv.CSVReader reader = new com.q1.csv.CSVReader(filePath + "/Performance.csv", ',', 0);
/* 158 */       currentData = reader.readAll();
/* 159 */       reader.close();
/*     */     }
/* 161 */     if (currentData.size() == 0) {
/* 162 */       com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
/* 163 */       String[] header = { "Parameters", this.ppName };
/* 164 */       writer.writeLine(header);
/* 165 */       for (java.util.Map.Entry<String, String> entry : resultsMap.entrySet()) {
/* 166 */         String[] outData = { (String)entry.getKey(), (String)entry.getValue() };
/* 167 */         writer.writeLine(outData);
/*     */       }
/* 169 */       writer.close();
/*     */     } else {
/* 171 */       com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
/* 172 */       String[] curHeader = (String[])currentData.get(0);
/* 173 */       int len = curHeader.length;
/* 174 */       String[] header = new String[len + 1];
/* 175 */       for (int k = 0; k < len; k++) {
/* 176 */         header[k] = curHeader[k];
/*     */       }
/* 178 */       header[len] = this.ppName;
/* 179 */       writer.writeLine(header);
/* 180 */       for (int i = 1; i < currentData.size(); i++) {
/* 181 */         String[] curData = (String[])currentData.get(i);
/* 182 */         String[] outData = new String[len + 1];
/* 183 */         String key = curData[0];
/* 184 */         for (int k = 0; k < len; k++)
/* 185 */           outData[k] = curData[k];
/* 186 */         outData[len] = ((String)resultsMap.get(key));
/* 187 */         writer.writeLine(outData);
/*     */       }
/* 189 */       writer.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeToFile(String btTimeStamp)
/*     */     throws java.io.IOException
/*     */   {
/* 196 */     String filePath = this.btGlobal.loginParameter.getOutputPath() + "/" + btTimeStamp + "/Results";
/* 197 */     if (!new java.io.File(filePath).exists()) {
/* 198 */       new java.io.File(filePath).mkdirs();
/*     */     }
/* 200 */     LinkedHashMap<String, String> resultsMap = new LinkedHashMap();
/*     */     
/* 202 */     if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
/* 203 */       resultsMap.put("Annual Return", "0.0%");
/* 204 */       resultsMap.put("Annual Vol", "0.0%");
/* 205 */       resultsMap.put("Return Per Trade", "0.0%");
/* 206 */       resultsMap.put("Sharpe Ratio", "0.0");
/* 207 */       resultsMap.put("Sortino Ratio", "0.0");
/* 208 */       resultsMap.put("Calmar Ratio", "0.0");
/* 209 */       resultsMap.put("Smooth Calmar Ratio", "0.0");
/* 210 */       resultsMap.put("Max Drawdown", "0.0%");
/* 211 */       resultsMap.put("Avg Drawdown", "0.0%");
/* 212 */       resultsMap.put("Max Drawdown Duration", "0 days");
/* 213 */       resultsMap.put("Avg Drawdown Duration", "0 days");
/* 214 */       resultsMap.put("Daily Win Loss", "0.0");
/* 215 */       resultsMap.put("Daily Hit Rate", "0.0%");
/* 216 */       resultsMap.put("Daily Expectancy", "0.0");
/* 217 */       resultsMap.put("Profit Factor", "0.0");
/* 218 */       resultsMap.put("Avg Trade Duration", "0 days");
/* 219 */       resultsMap.put("Win-Trade Duration", "0 days");
/* 220 */       resultsMap.put("Loss-Trade Duration", "0 days");
/* 221 */       resultsMap.put("Average Slippage", "0.0%");
/* 222 */       resultsMap.put("Trade Win Loss", "0.0");
/* 223 */       resultsMap.put("Trade Hit Rate", "0.0%");
/* 224 */       resultsMap.put("Trade Count", "0");
/* 225 */       resultsMap.put("Trading Days", "0%");
/* 226 */       resultsMap.put("Max Trades Per Day", "0.0");
/* 227 */       resultsMap.put("Avg Trades Per Day", "0.0");
/* 228 */       resultsMap.put("Open Slippage", "0.0%");
/* 229 */       resultsMap.put("Normal Slippage", "0.0%");
/* 230 */       resultsMap.put("Slippage Factor", "0.0");
/* 231 */       resultsMap.put("Normal Slippage Factor", "0.0");
/* 232 */       resultsMap.put("Open Trades", "0.0%");
/* 233 */       resultsMap.put("Avg Leverage", "0.0");
/* 234 */       resultsMap.put("Max Leverage", "0.0");
/*     */     } else {
/* 236 */       resultsMap.put("Annual Return", this.annualReturn + "%");
/* 237 */       resultsMap.put("Annual Vol", this.annualVolatility + "%");
/* 238 */       resultsMap.put("Return Per Trade", this.avgTradeReturn + "%");
/* 239 */       resultsMap.put("Sharpe Ratio", this.mtmSharpe.toString());
/* 240 */       resultsMap.put("Sortino Ratio", this.mtmSortino.toString());
/* 241 */       resultsMap.put("Calmar Ratio", this.mtmCalmar.toString());
/* 242 */       resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar.toString());
/* 243 */       resultsMap.put("Max Drawdown", this.maxDrawdown + "%");
/* 244 */       resultsMap.put("Avg Drawdown", this.avgDrawdown + "%");
/* 245 */       resultsMap.put("Max Drawdown Duration", this.maxDrawdownDuration + " days");
/* 246 */       resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration + " days");
/* 247 */       resultsMap.put("Daily Win Loss", this.mtmWinLoss.toString());
/* 248 */       resultsMap.put("Daily Hit Rate", this.mtmHitRate + "%");
/* 249 */       resultsMap.put("Daily Expectancy", this.expectancy.toString());
/* 250 */       resultsMap.put("Profit Factor", this.profitFactor.toString());
/* 251 */       resultsMap.put("Avg Trade Duration", getDurationVal(this.avgTradeDuration));
/* 252 */       resultsMap.put("Win-Trade Duration", getDurationVal(this.avgWinDuration));
/* 253 */       resultsMap.put("Loss-Trade Duration", getDurationVal(this.avgLossDuration));
/* 254 */       resultsMap.put("Average Slippage", this.avgSlippage + "%");
/* 255 */       resultsMap.put("Trade Win Loss", this.tradeWinLoss.toString());
/* 256 */       resultsMap.put("Trade Hit Rate", this.tradeHitRate + "%");
/* 257 */       resultsMap.put("Trade Count", this.tradeCount.toString());
/* 258 */       resultsMap.put("Trading Days", this.tradingDays + "%");
/* 259 */       resultsMap.put("Max Trades Per Day", this.maxTradesPerDay.toString());
/* 260 */       resultsMap.put("Avg Trades Per Day", this.avgTradePerDay.toString());
/* 261 */       resultsMap.put("Open Slippage", this.avgOpenSlippage + "%");
/* 262 */       resultsMap.put("Normal Slippage", this.avgNormSlippage + "%");
/* 263 */       resultsMap.put("Slippage Factor", this.slippageFactor);
/* 264 */       resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
/* 265 */       resultsMap.put("Open Trades", this.opTradePerc + "%");
/* 266 */       resultsMap.put("Avg Leverage", this.avgLeverage.toString());
/* 267 */       resultsMap.put("Max Leverage", this.maxLeverage.toString());
/*     */     }
/*     */     
/* 270 */     java.util.ArrayList<String[]> currentData = new java.util.ArrayList();
/* 271 */     if (new java.io.File(filePath + "/Performance.csv").exists()) {
/* 272 */       com.q1.csv.CSVReader reader = new com.q1.csv.CSVReader(filePath + "/Performance.csv", ',', 0);
/* 273 */       currentData = reader.readAll();
/* 274 */       reader.close();
/*     */     }
/* 276 */     if (currentData.size() == 0) {
/* 277 */       com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
/* 278 */       String[] header = { "Parameters", this.ppName };
/* 279 */       writer.writeLine(header);
/* 280 */       for (java.util.Map.Entry<String, String> entry : resultsMap.entrySet()) {
/* 281 */         String[] outData = { (String)entry.getKey(), (String)entry.getValue() };
/* 282 */         writer.writeLine(outData);
/*     */       }
/* 284 */       writer.close();
/*     */     } else {
/* 286 */       com.q1.csv.CSVWriter writer = new com.q1.csv.CSVWriter(filePath + "/Performance.csv", false, ",");
/* 287 */       String[] curHeader = (String[])currentData.get(0);
/* 288 */       int len = curHeader.length;
/* 289 */       String[] header = new String[len + 1];
/* 290 */       for (int k = 0; k < len; k++) {
/* 291 */         header[k] = curHeader[k];
/*     */       }
/* 293 */       header[len] = this.ppName;
/* 294 */       writer.writeLine(header);
/* 295 */       for (int i = 1; i < currentData.size(); i++) {
/* 296 */         String[] curData = (String[])currentData.get(i);
/* 297 */         String[] outData = new String[len + 1];
/* 298 */         String key = curData[0];
/* 299 */         for (int k = 0; k < len; k++)
/* 300 */           outData[k] = curData[k];
/* 301 */         outData[len] = ((String)resultsMap.get(key));
/* 302 */         writer.writeLine(outData);
/*     */       }
/* 304 */       writer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void runPostprocess()
/*     */     throws java.text.ParseException
/*     */   {
/* 312 */     Long dataCount = Long.valueOf(0L);
/* 313 */     Integer tradingCount = Integer.valueOf(0);
/* 314 */     int mtmCount = 0;
/* 315 */     int winCount = 0;
/* 316 */     int lossCount = 0;
/* 317 */     Double cumReturn = Double.valueOf(0.0D);
/* 318 */     Double cumWinReturn = Double.valueOf(0.0D);
/* 319 */     Double cumLossReturn = Double.valueOf(0.0D);
/* 320 */     Double currentMax = Double.valueOf(0.0D);
/* 321 */     Double drawdown = Double.valueOf(0.0D);Double prevDrawdown = Double.valueOf(0.0D);
/* 322 */     java.util.ArrayList<Long> durations = new java.util.ArrayList();
/* 323 */     java.util.ArrayList<Double> drawdowns = new java.util.ArrayList();
/* 324 */     Double curDrawdown = Double.valueOf(0.0D);
/* 325 */     java.util.Date drawStart = null;
/* 326 */     java.util.Date date = null;
/*     */     
/*     */ 
/* 329 */     for (java.util.Map.Entry<Long, Double> entry : this.consolMTM.entrySet())
/*     */     {
/* 331 */       date = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)entry.getKey()).toString());
/* 332 */       Double mtm = (Double)entry.getValue();
/*     */       
/* 334 */       cumReturn = Double.valueOf(cumReturn.doubleValue() + mtm.doubleValue());
/*     */       
/* 336 */       if (mtm.doubleValue() > 0.0D)
/*     */       {
/* 338 */         cumWinReturn = Double.valueOf(cumWinReturn.doubleValue() + mtm.doubleValue());
/* 339 */         winCount++;
/*     */ 
/*     */       }
/* 342 */       else if (mtm.doubleValue() < 0.0D) {
/* 343 */         cumLossReturn = Double.valueOf(cumLossReturn.doubleValue() + mtm.doubleValue());
/* 344 */         lossCount++;
/*     */       }
/*     */       
/* 347 */       if (cumReturn.doubleValue() > currentMax.doubleValue()) {
/* 348 */         currentMax = cumReturn;
/*     */       }
/* 350 */       drawdown = Double.valueOf(currentMax.doubleValue() - cumReturn.doubleValue());
/*     */       
/* 352 */       if (drawdown.doubleValue() > curDrawdown.doubleValue()) {
/* 353 */         curDrawdown = drawdown;
/*     */       }
/* 355 */       if ((com.q1.math.MathLib.doubleCompare(drawdown, Double.valueOf(0.0D)).intValue() == 0) && (com.q1.math.MathLib.doubleCompare(prevDrawdown, Double.valueOf(0.0D)).intValue() > 0)) {
/* 356 */         java.util.Date drawEnd = date;
/* 357 */         drawdowns.add(curDrawdown);
/* 358 */         durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
/* 359 */         curDrawdown = Double.valueOf(0.0D);
/* 360 */         drawStart = null;
/*     */       }
/*     */       
/* 363 */       if ((drawStart == null) && 
/* 364 */         (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() > 0)) {
/* 365 */         drawStart = date;
/*     */       }
/* 367 */       prevDrawdown = drawdown;
/* 368 */       if (com.q1.math.MathLib.doubleCompare(mtm, Double.valueOf(0.0D)).intValue() != 0) {
/* 369 */         mtmCount++;
/*     */       }
/*     */     }
/* 372 */     if (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() != 0) {
/* 373 */       java.util.Date drawEnd = date;
/* 374 */       drawdowns.add(curDrawdown);
/* 375 */       durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
/* 376 */       curDrawdown = Double.valueOf(0.0D);
/* 377 */       drawStart = null;
/*     */     }
/* 379 */     java.util.Collections.sort(drawdowns);
/*     */     
/*     */ 
/* 382 */     Double smoothDraw = Double.valueOf(0.0D);
/* 383 */     if (drawdowns.size() != 0) {
/* 384 */       smoothDraw = (Double)drawdowns.get(0);
/* 385 */       for (int i = 1; i < drawdowns.size(); i++) {
/* 386 */         smoothDraw = Double.valueOf(((Double)drawdowns.get(i)).doubleValue() * 0.5D + 0.5D * smoothDraw.doubleValue());
/*     */       }
/*     */     }
/*     */     
/* 390 */     if (com.q1.math.MathLib.doubleCompare(curDrawdown, Double.valueOf(0.0D)).intValue() > 0) {
/* 391 */       java.util.Date drawEnd = date;
/* 392 */       drawdowns.add(curDrawdown);
/* 393 */       durations.add(Long.valueOf(com.q1.math.DateTime.getDateDiff(drawStart, drawEnd)));
/*     */     }
/*     */     
/*     */ 
/* 397 */     if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
/* 398 */       return;
/*     */     }
/*     */     
/* 401 */     java.util.Date startDate = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)this.consolMTM.firstKey()).toString());
/* 402 */     java.util.Date endDate = new java.text.SimpleDateFormat("yyyyMMdd").parse(((Long)this.consolMTM.lastKey()).toString());
/*     */     
/* 404 */     dataCount = Long.valueOf(com.q1.math.DateTime.getDateDiff(startDate, endDate) + 1L);
/* 405 */     tradingCount = Integer.valueOf(this.consolMTM.size());
/*     */     
/* 407 */     Double tradingDayCount = Double.valueOf(365.0D * (tradingCount.doubleValue() / dataCount.doubleValue()));
/*     */     
/*     */ 
/* 410 */     this.avgDailyReturn = Double.valueOf(cumReturn.doubleValue() / tradingCount.intValue());
/*     */     
/* 412 */     Double avgWinReturn = Double.valueOf(cumWinReturn.doubleValue() / winCount);
/*     */     
/* 414 */     Double avgLossReturn = Double.valueOf(0.0D);
/* 415 */     if (lossCount > 0) {
/* 416 */       avgLossReturn = Double.valueOf(-cumLossReturn.doubleValue() / lossCount);
/*     */     }
/*     */     
/* 419 */     Double cumSqrDeviation = Double.valueOf(0.0D);
/* 420 */     Double cumNegDeviation = Double.valueOf(0.0D);
/* 421 */     Integer negCount = Integer.valueOf(0);
/*     */     
/* 423 */     for (Double mtm : this.consolMTM.values()) {
/* 424 */       cumSqrDeviation = Double.valueOf(cumSqrDeviation.doubleValue() + Math.pow(mtm.doubleValue() - this.avgDailyReturn.doubleValue(), 2.0D));
/* 425 */       if (com.q1.math.MathLib.doubleCompare(Double.valueOf(mtm.doubleValue() - this.avgDailyReturn.doubleValue()), Double.valueOf(0.0D)).intValue() < 0) {
/* 426 */         cumNegDeviation = Double.valueOf(cumNegDeviation.doubleValue() + Math.pow(mtm.doubleValue() - this.avgDailyReturn.doubleValue(), 2.0D));
/* 427 */         negCount = Integer.valueOf(negCount.intValue() + 1);
/*     */       }
/*     */     }
/* 430 */     this.annualReturn = Double.valueOf(tradingDayCount.doubleValue() * (this.avgDailyReturn.doubleValue() * 100.0D));
/* 431 */     this.maxDrawdown = Double.valueOf(com.q1.math.MathLib.max(drawdowns) * 100.0D);
/* 432 */     this.avgDrawdown = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(com.q1.math.MathLib.simpleAvgDouble(drawdowns) * 100.0D, 0.01D));
/* 433 */     this.maxDrawdownDuration = com.q1.math.MathLib.maxLong(durations);
/* 434 */     this.avgDrawdownDuration = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(com.q1.math.MathLib.simpleAvg(durations), 0.01D));
/*     */     
/* 436 */     if (com.q1.math.MathLib.doubleCompare(this.maxDrawdown, Double.valueOf(0.0D)).intValue() == 0) {
/* 437 */       this.mtmCalmar = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else {
/* 439 */       this.mtmCalmar = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / this.maxDrawdown.doubleValue(), 0.01D));
/*     */     }
/* 441 */     if (com.q1.math.MathLib.doubleCompare(smoothDraw, Double.valueOf(0.0D)).intValue() == 0) {
/* 442 */       this.mtmSmoothCalmar = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else {
/* 444 */       this.mtmSmoothCalmar = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / (smoothDraw.doubleValue() * 100.0D), 0.01D));
/*     */     }
/* 446 */     this.maxDrawdown = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.maxDrawdown.doubleValue(), 0.01D));
/*     */     
/* 448 */     this.mtmStdDev = Double.valueOf(Math.sqrt(cumSqrDeviation.doubleValue() / tradingCount.intValue()));
/*     */     
/* 450 */     Double mtmNegDev = Double.valueOf(Math.sqrt(cumNegDeviation.doubleValue() / negCount.intValue()));
/*     */     
/* 452 */     this.mtmHitRate = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(winCount / (winCount + lossCount) * 100.0D, 0.01D));
/*     */     
/* 454 */     if (com.q1.math.MathLib.doubleCompare(avgLossReturn, Double.valueOf(0.0D)).intValue() == 0) {
/* 455 */       this.mtmWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else {
/* 457 */       this.mtmWinLoss = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(avgWinReturn.doubleValue() / avgLossReturn.doubleValue(), 0.01D));
/*     */     }
/*     */     
/* 460 */     this.expectancy = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.mtmHitRate.doubleValue() / 100.0D * this.mtmWinLoss.doubleValue() - (1.0D - this.mtmHitRate.doubleValue() / 100.0D), 0.01D));
/*     */     
/*     */ 
/* 463 */     if (com.q1.math.MathLib.doubleCompare(this.mtmStdDev, Double.valueOf(0.0D)).intValue() == 0) {
/* 464 */       this.mtmSharpe = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else {
/* 466 */       this.mtmSharpe = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(Math.sqrt(tradingDayCount.doubleValue()) * (this.avgDailyReturn.doubleValue() / this.mtmStdDev.doubleValue()), 0.01D));
/*     */     }
/* 468 */     if (com.q1.math.MathLib.doubleCompare(mtmNegDev, Double.valueOf(0.0D)).intValue() == 0) {
/* 469 */       this.mtmSortino = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else {
/* 471 */       this.mtmSortino = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(Math.sqrt(tradingDayCount.doubleValue()) * (this.avgDailyReturn.doubleValue() / mtmNegDev.doubleValue()), 
/* 472 */         0.01D));
/*     */     }
/* 474 */     this.annualReturn = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue(), 0.01D));
/*     */     
/* 476 */     this.annualVolatility = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(this.annualReturn.doubleValue() / this.mtmSharpe.doubleValue(), 0.01D));
/*     */     
/* 478 */     this.tradingDays = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(100.0D * mtmCount / dataCount.longValue(), 0.01D));
/*     */     
/* 480 */     runTradePostProcess();
/*     */   }
/*     */   
/*     */   public void runTradePostProcess() throws java.text.ParseException
/*     */   {
/* 485 */     java.util.HashMap<String, Integer> tbIdxMap = getTradeBookIndexMap();
/* 486 */     java.util.HashMap<String, java.util.HashSet<String>> scripSet = getScripSet();
/*     */     
/* 488 */     TradePostProcess tradepp = new TradePostProcess(scripSet, tbIdxMap);
/*     */     
/* 490 */     if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.SingleScrip)) {
/* 491 */       tradepp.runSingleTradePP(this.tradeBook);
/* 492 */     } else if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.Spread)) {
/* 493 */       tradepp.runSpreadTradePP(this.tradeBook);
/* 494 */     } else if (this.postProcessMode.equals(com.q1.bt.process.backtest.PostProcessMode.Portfolio)) {
/* 495 */       tradepp.runPortfolioPP(this.tradeBook);
/*     */     } else {
/* 497 */       throw new Error("Unknown op mode");
/*     */     }
/*     */     
/* 500 */     this.avgTradeReturn = tradepp.avgTradeReturn;
/* 501 */     this.profitFactor = tradepp.profitFactor;
/* 502 */     this.avgTradeDuration = tradepp.avgTradeDuration;
/* 503 */     this.avgWinDuration = tradepp.avgWinDuration;
/* 504 */     this.avgLossDuration = tradepp.avgLossDuration;
/* 505 */     this.avgSlippage = tradepp.avgSlippage;
/* 506 */     this.avgOpenSlippage = tradepp.avgOpenSlippage;
/* 507 */     this.avgNormSlippage = tradepp.avgNormSlippage;
/* 508 */     this.tradeWinLoss = tradepp.tradeWinLoss;
/* 509 */     this.tradeHitRate = tradepp.tradeHitRate;
/* 510 */     this.tradeCount = Integer.valueOf(tradepp.tradeCount);
/* 511 */     this.slippageFactor = tradepp.slippageFactor;
/* 512 */     this.normSlippageFactor = tradepp.normSlippageFactor;
/* 513 */     this.opTradePerc = tradepp.opTradePerc;
/* 514 */     this.maxTradesPerDay = tradepp.maxTradesPerDay;
/* 515 */     this.avgTradePerDay = tradepp.avgTradePerDay;
/* 516 */     this.avgLeverage = tradepp.avgLeverage;
/* 517 */     this.maxLeverage = tradepp.maxLeverage;
/*     */   }
/*     */   
/*     */   public void printResults()
/*     */   {
/* 522 */     System.out.println("----- Backtest Results ------");
/* 523 */     System.out.println("Annual Return: " + this.annualReturn + "%");
/* 524 */     System.out.println("Annual Vol: " + this.annualVolatility + "%");
/* 525 */     System.out.println("Return Per Trade: " + this.avgTradeReturn + "%");
/* 526 */     System.out.println("Sharpe Ratio: " + this.mtmSharpe);
/* 527 */     System.out.println("Sortino Ratio: " + this.mtmSortino);
/* 528 */     System.out.println("Calmar Ratio: " + this.mtmCalmar);
/* 529 */     System.out.println("Smooth Calmar Ratio: " + this.mtmSmoothCalmar);
/* 530 */     System.out.println("Max Drawdown: " + this.maxDrawdown + "%");
/* 531 */     System.out.println("Avg Drawdown: " + this.avgDrawdown + "%");
/* 532 */     System.out.println("Max Drawdown Duration: " + this.maxDrawdownDuration + " days");
/* 533 */     System.out.println("Avg Drawdown Duration: " + this.avgDrawdownDuration + " days");
/* 534 */     System.out.println("Daily Win Loss: " + this.mtmWinLoss);
/* 535 */     System.out.println("Daily Hit Rate: " + this.mtmHitRate + "%");
/* 536 */     System.out.println("Daily Expectancy: " + this.expectancy);
/* 537 */     System.out.println("Profit Factor: " + this.profitFactor);
/* 538 */     System.out.println("Avg Trade Duration: " + getDurationVal(this.avgTradeDuration));
/* 539 */     System.out.println("Win-Trade Duration: " + getDurationVal(this.avgWinDuration));
/* 540 */     System.out.println("Loss-Trade Duration: " + getDurationVal(this.avgLossDuration));
/* 541 */     System.out.println("Average Slippage: " + this.avgSlippage + "%");
/* 542 */     System.out.println("Trade Win Loss: " + this.tradeWinLoss);
/* 543 */     System.out.println("Trade Hit Rate: " + this.tradeHitRate + "%");
/* 544 */     System.out.println("Trade Count: " + this.tradeCount);
/* 545 */     System.out.println("Open Trades: " + this.opTradePerc);
/* 546 */     System.out.println("Avg Leverage: " + this.avgLeverage);
/* 547 */     System.out.println("Max Leverage: " + this.maxLeverage);
/*     */   }
/*     */   
/*     */   public void displayResults(com.q1.bt.global.BacktesterNonGUI bt) {
/* 551 */     printResults();
/*     */   }
/*     */   
/*     */   public String getDurationVal(Double avgTradeDuration)
/*     */   {
/* 556 */     Double curDuration = Double.valueOf(0.0D);
/* 557 */     String durationType; if (avgTradeDuration.doubleValue() > 1440.0D) {
/* 558 */       String durationType = "Days";
/* 559 */       curDuration = Double.valueOf(avgTradeDuration.doubleValue() / 1440.0D);
/* 560 */     } else if (avgTradeDuration.doubleValue() > 60.0D) {
/* 561 */       String durationType = "Hours";
/* 562 */       curDuration = Double.valueOf(avgTradeDuration.doubleValue() / 60.0D);
/*     */     } else {
/* 564 */       curDuration = avgTradeDuration;
/* 565 */       durationType = "Minutes";
/*     */     }
/* 567 */     curDuration = Double.valueOf(com.q1.math.MathLib.roundTickWithPrecision(curDuration.doubleValue(), 0.01D));
/* 568 */     return curDuration + " " + durationType;
/*     */   }
/*     */   
/*     */   public java.util.HashMap<String, Integer> getTradeBookIndexMap() {
/* 572 */     java.util.HashMap<String, Integer> tbIdxMap = new java.util.HashMap();
/*     */     
/* 574 */     tbIdxMap.put("dateTime", Integer.valueOf(0));
/* 575 */     tbIdxMap.put("capital", Integer.valueOf(1));
/* 576 */     tbIdxMap.put("scripID", Integer.valueOf(2));
/* 577 */     tbIdxMap.put("orderSide", Integer.valueOf(3));
/* 578 */     tbIdxMap.put("orderType", Integer.valueOf(4));
/* 579 */     tbIdxMap.put("triggerPrice", Integer.valueOf(5));
/* 580 */     tbIdxMap.put("execPrice", Integer.valueOf(6));
/* 581 */     tbIdxMap.put("qty", Integer.valueOf(7));
/* 582 */     tbIdxMap.put("execType", Integer.valueOf(8));
/* 583 */     tbIdxMap.put("scripListID", Integer.valueOf(9));
/* 584 */     return tbIdxMap;
/*     */   }
/*     */   
/*     */ 
/*     */   public java.util.HashMap<String, java.util.HashSet<String>> getScripSet()
/*     */   {
/* 590 */     java.util.HashMap<String, java.util.HashSet<String>> scripset = new java.util.HashMap();
/*     */     
/* 592 */     for (int i = 0; i < this.tradeBook.size(); i++)
/*     */     {
/* 594 */       String[] trade = (String[])this.tradeBook.get(i);
/*     */       
/* 596 */       if (scripset.get(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()]) == null) {
/* 597 */         java.util.HashSet<String> scripList = new java.util.HashSet();
/* 598 */         scripList.add(trade[((Integer)this.tbIdxMap.get("scripID")).intValue()]);
/* 599 */         scripset.put(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()], scripList);
/*     */       } else {
/* 601 */         java.util.HashSet<String> scripList = (java.util.HashSet)scripset.get(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()]);
/* 602 */         scripList.add(trade[((Integer)this.tbIdxMap.get("scripID")).intValue()]);
/* 603 */         scripset.put(trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()], scripList);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 608 */     return scripset;
/*     */   }
/*     */   
/*     */   public java.util.HashMap<String, Double> getResultsMap()
/*     */   {
/* 613 */     java.util.HashMap<String, Double> resultsMap = new java.util.HashMap();
/* 614 */     if ((this.consolMTM.isEmpty()) || (this.tradeBook.size() == 0)) {
/* 615 */       resultsMap.put("Annual Return", Double.valueOf(0.0D));
/* 616 */       resultsMap.put("Annual Vol", Double.valueOf(0.0D));
/* 617 */       resultsMap.put("Return Per Trade", Double.valueOf(0.0D));
/* 618 */       resultsMap.put("Sharpe Ratio", Double.valueOf(0.0D));
/* 619 */       resultsMap.put("Sortino Ratio", Double.valueOf(0.0D));
/* 620 */       resultsMap.put("Calmar Ratio", Double.valueOf(0.0D));
/* 621 */       resultsMap.put("Smooth Calmar Ratio", Double.valueOf(0.0D));
/* 622 */       resultsMap.put("Max Drawdown", Double.valueOf(0.0D));
/* 623 */       resultsMap.put("Avg Drawdown", Double.valueOf(0.0D));
/* 624 */       resultsMap.put("Max Drawdown Duration", Double.valueOf(0.0D));
/* 625 */       resultsMap.put("Avg Drawdown Duration", Double.valueOf(0.0D));
/* 626 */       resultsMap.put("Daily Win Loss", Double.valueOf(0.0D));
/* 627 */       resultsMap.put("Daily Hit Rate", Double.valueOf(0.0D));
/* 628 */       resultsMap.put("Daily Expectancy", Double.valueOf(0.0D));
/* 629 */       resultsMap.put("Profit Factor", Double.valueOf(0.0D));
/* 630 */       resultsMap.put("Avg Trade Duration", Double.valueOf(0.0D));
/* 631 */       resultsMap.put("Win-Trade Duration", Double.valueOf(0.0D));
/* 632 */       resultsMap.put("Loss-Trade Duration", Double.valueOf(0.0D));
/* 633 */       resultsMap.put("Average Slippage", Double.valueOf(0.0D));
/* 634 */       resultsMap.put("Trade Win Loss", Double.valueOf(0.0D));
/* 635 */       resultsMap.put("Trade Hit Rate", Double.valueOf(0.0D));
/* 636 */       resultsMap.put("Trade Count", Double.valueOf(0.0D));
/* 637 */       resultsMap.put("Trading Days", Double.valueOf(0.0D));
/* 638 */       resultsMap.put("Max Trades Per Day", Double.valueOf(0.0D));
/* 639 */       resultsMap.put("Avg Trades Per Day", Double.valueOf(0.0D));
/* 640 */       resultsMap.put("Open Slippage", Double.valueOf(0.0D));
/* 641 */       resultsMap.put("Normal Slippage", Double.valueOf(0.0D));
/* 642 */       resultsMap.put("Slippage Factor", Double.valueOf(0.0D));
/* 643 */       resultsMap.put("Normal Slippage Factor", Double.valueOf(0.0D));
/* 644 */       resultsMap.put("Open Trades", Double.valueOf(0.0D));
/* 645 */       resultsMap.put("Avg Leverage", Double.valueOf(0.0D));
/* 646 */       resultsMap.put("Max Leverage", Double.valueOf(0.0D));
/*     */     } else {
/* 648 */       resultsMap.put("Annual Return", this.annualReturn);
/* 649 */       resultsMap.put("Annual Vol", this.annualVolatility);
/* 650 */       resultsMap.put("Return Per Trade", this.avgTradeReturn);
/* 651 */       resultsMap.put("Sharpe Ratio", this.mtmSharpe);
/* 652 */       resultsMap.put("Sortino Ratio", this.mtmSortino);
/* 653 */       resultsMap.put("Calmar Ratio", this.mtmCalmar);
/* 654 */       resultsMap.put("Smooth Calmar Ratio", this.mtmSmoothCalmar);
/* 655 */       resultsMap.put("Max Drawdown", this.maxDrawdown);
/* 656 */       resultsMap.put("Avg Drawdown", this.avgDrawdown);
/* 657 */       resultsMap.put("Max Drawdown Duration", Double.valueOf(this.maxDrawdownDuration.longValue()));
/* 658 */       resultsMap.put("Avg Drawdown Duration", this.avgDrawdownDuration);
/* 659 */       resultsMap.put("Daily Win Loss", this.mtmWinLoss);
/* 660 */       resultsMap.put("Daily Hit Rate", this.mtmHitRate);
/* 661 */       resultsMap.put("Daily Expectancy", this.expectancy);
/* 662 */       resultsMap.put("Profit Factor", this.profitFactor);
/* 663 */       resultsMap.put("Avg Trade Duration", this.avgTradeDuration);
/* 664 */       resultsMap.put("Win-Trade Duration", this.avgWinDuration);
/* 665 */       resultsMap.put("Loss-Trade Duration", this.avgLossDuration);
/* 666 */       resultsMap.put("Average Slippage", this.avgSlippage);
/* 667 */       resultsMap.put("Trade Win Loss", this.tradeWinLoss);
/* 668 */       resultsMap.put("Trade Hit Rate", this.tradeHitRate);
/* 669 */       resultsMap.put("Trade Count", Double.valueOf(this.tradeCount.intValue()));
/* 670 */       resultsMap.put("Trading Days", this.tradingDays);
/* 671 */       resultsMap.put("Max Trades Per Day", this.maxTradesPerDay);
/* 672 */       resultsMap.put("Avg Trades Per Day", this.avgTradePerDay);
/* 673 */       resultsMap.put("Open Slippage", this.avgOpenSlippage);
/* 674 */       resultsMap.put("Normal Slippage", this.avgNormSlippage);
/* 675 */       resultsMap.put("Slippage Factor", this.slippageFactor);
/* 676 */       resultsMap.put("Normal Slippage Factor", this.normSlippageFactor);
/* 677 */       resultsMap.put("Open Trades", this.opTradePerc);
/* 678 */       resultsMap.put("Avg Leverage", this.avgLeverage);
/* 679 */       resultsMap.put("Max Leverage", this.maxLeverage);
/*     */     }
/*     */     
/* 682 */     return resultsMap;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/PostProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */