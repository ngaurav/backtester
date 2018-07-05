/*     */ package com.q1.bt.postprocess;
/*     */ 
/*     */ import com.q1.math.DateTime;
/*     */ import com.q1.math.MathLib;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TradePostProcess
/*     */ {
/*     */   Double normSlipVal;
/*     */   Long normSlipQty;
/*     */   Long openTradeQty;
/*     */   Double opSlipVal;
/*     */   Long opSlipQty;
/*     */   Long totalTradeQty;
/*     */   Double dollarPNL;
/*     */   Double slippagePNL;
/*     */   Double normSlippagePNL;
/*  26 */   ArrayList<Long> tradeDurations = new ArrayList();
/*  27 */   ArrayList<Long> winDurations = new ArrayList();
/*  28 */   ArrayList<Long> lossDurations = new ArrayList();
/*  29 */   ArrayList<Double> tradePLs = new ArrayList();
/*  30 */   ArrayList<Double> winPLs = new ArrayList();
/*  31 */   ArrayList<Double> lossPLs = new ArrayList();
/*  32 */   ArrayList<Double> leverageVals = new ArrayList();
/*  33 */   HashMap<String, Double> tradeCounts = new HashMap();
/*  34 */   HashMap<String, HashSet<String>> scripSet = new HashMap();
/*  35 */   HashMap<String, Integer> tbIdxMap = new HashMap();
/*     */   
/*     */   int winCount;
/*     */   
/*     */   int lossCount;
/*     */   
/*     */   int tradeCount;
/*     */   Double avgTradeDuration;
/*     */   Double avgWinDuration;
/*     */   Double avgLossDuration;
/*     */   Double avgLeverage;
/*     */   Double maxLeverage;
/*     */   Double avgSlippage;
/*     */   Double avgOpenSlippage;
/*     */   Double avgNormSlippage;
/*     */   Double avgTradeReturn;
/*     */   Double slippageFactor;
/*     */   Double normSlippageFactor;
/*     */   Double tradeWinLoss;
/*     */   Double profitFactor;
/*     */   Double tradeHitRate;
/*     */   Double opTradePerc;
/*     */   Double maxTradesPerDay;
/*     */   Double avgTradePerDay;
/*     */   int scripCount;
/*     */   Double tradePnL;
/*     */   Double slipVal;
/*     */   Double slipQty;
/*  63 */   HashMap<String, HashMap<String, Long>> posMap = new HashMap();
/*     */   
/*     */   Integer last_exit_trade;
/*     */   
/*     */   public TradePostProcess(HashMap<String, HashSet<String>> scripSet, HashMap<String, Integer> tbIdxmap)
/*     */   {
/*  69 */     this.winCount = (this.lossCount = this.tradeCount = 0);
/*  70 */     this.normSlipVal = Double.valueOf(0.0D);
/*  71 */     this.normSlipQty = Long.valueOf(0L);
/*  72 */     this.openTradeQty = Long.valueOf(0L);
/*  73 */     this.opSlipVal = Double.valueOf(0.0D);
/*  74 */     this.totalTradeQty = Long.valueOf(0L);
/*  75 */     this.opSlipQty = Long.valueOf(0L);
/*  76 */     this.dollarPNL = Double.valueOf(0.0D);
/*  77 */     this.slippagePNL = Double.valueOf(0.0D);
/*  78 */     this.normSlippagePNL = Double.valueOf(0.0D);
/*  79 */     this.tradeDurations = new ArrayList();
/*  80 */     this.winDurations = new ArrayList();
/*  81 */     this.lossDurations = new ArrayList();
/*  82 */     this.tradePLs = new ArrayList();
/*  83 */     this.winPLs = new ArrayList();
/*  84 */     this.lossPLs = new ArrayList();
/*  85 */     this.leverageVals = new ArrayList();
/*  86 */     this.tradeCounts = new HashMap();
/*  87 */     this.scripSet = scripSet;
/*  88 */     this.tbIdxMap = tbIdxmap;
/*     */     
/*  90 */     for (String scripListID : scripSet.keySet()) {
/*  91 */       HashMap<String, Long> scripListPosMap = new HashMap();
/*  92 */       this.scripCount = 0;
/*  93 */       for (String scripID : (HashSet)scripSet.get(scripListID)) {
/*  94 */         scripListPosMap.put(scripID, Long.valueOf(0L));
/*  95 */         this.scripCount += 1;
/*     */       }
/*     */       
/*  98 */       this.posMap.put(scripListID, scripListPosMap);
/*     */     }
/*     */   }
/*     */   
/*     */   public void runSpreadTradePP(ArrayList<String[]> tradeBook)
/*     */     throws ParseException
/*     */   {
/* 105 */     boolean spreadinPos = false;
/* 106 */     this.tradePnL = Double.valueOf(0.0D);
/* 107 */     this.slipVal = Double.valueOf(0.0D);
/* 108 */     this.slipQty = Double.valueOf(0.0D);
/* 109 */     this.last_exit_trade = Integer.valueOf(0);
/*     */     
/* 111 */     Date tradeStartDT = null;Date tradeEndDT = null;
/* 112 */     int i = 0;
/* 113 */     while (i < tradeBook.size())
/*     */     {
/* 115 */       String[] trade = (String[])tradeBook.get(i);
/*     */       
/* 117 */       if (!spreadinPos)
/*     */       {
/*     */ 
/* 120 */         Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/* 121 */         tradeStartDT = dT;
/*     */         
/*     */ 
/* 124 */         for (int j = 0; j < this.scripCount; j++) {
/* 125 */           trade = (String[])tradeBook.get(i + j);
/* 126 */           processTrade(trade, true);
/*     */         }
/*     */         
/* 129 */         i = i + this.scripCount - 1;
/* 130 */         spreadinPos = true;
/*     */       }
/*     */       else {
/* 133 */         boolean exit = processTrade(trade, false);
/*     */         
/*     */ 
/*     */ 
/* 137 */         if (exit)
/*     */         {
/* 139 */           for (int j = 1; j < this.scripCount; j++) {
/* 140 */             trade = (String[])tradeBook.get(i + j);
/* 141 */             boolean exit_sync = processTrade(trade, false);
/* 142 */             if (!exit_sync) {
/* 143 */               throw new Error("Unsync exit. Please make sure all scrips in a scriplist have trade exits at the same dateTime");
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 148 */           Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/* 149 */           tradeEndDT = dT;
/*     */           
/* 151 */           long tradeDuration = DateTime.getDateTimeDiff(tradeStartDT, tradeEndDT);
/* 152 */           this.tradeDurations.add(Long.valueOf(tradeDuration));
/* 153 */           this.tradePLs.add(this.tradePnL);
/* 154 */           if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() > 0) {
/* 155 */             this.winPLs.add(this.tradePnL);
/* 156 */             this.winDurations.add(Long.valueOf(tradeDuration));
/* 157 */             this.winCount += 1;
/* 158 */           } else if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() < 0) {
/* 159 */             this.lossPLs.add(Double.valueOf(Math.abs(this.tradePnL.doubleValue())));
/* 160 */             this.lossDurations.add(Long.valueOf(tradeDuration));
/* 161 */             this.lossCount += 1;
/*     */           }
/* 163 */           this.tradeCount += 1;
/*     */           
/*     */ 
/* 166 */           this.tradePnL = Double.valueOf(0.0D);
/* 167 */           spreadinPos = false;
/*     */           
/* 169 */           i = i + this.scripCount - 1;
/*     */         }
/*     */       }
/* 172 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 176 */     this.avgTradeDuration = Double.valueOf(MathLib.simpleAvg(this.tradeDurations));
/* 177 */     this.avgWinDuration = Double.valueOf(MathLib.simpleAvg(this.winDurations));
/* 178 */     this.avgLossDuration = Double.valueOf(MathLib.simpleAvg(this.lossDurations));
/* 179 */     this.avgLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.leverageVals), 0.01D));
/* 180 */     this.maxLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.leverageVals), 0.01D));
/* 181 */     this.avgSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.slipVal.doubleValue() / this.slipQty.doubleValue()), 0.001D));
/* 182 */     this.avgOpenSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.opSlipVal.doubleValue() / this.opSlipQty.longValue()), 0.001D));
/* 183 */     this.avgNormSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.normSlipVal.doubleValue() / this.normSlipQty.longValue()), 0.001D));
/* 184 */     this.avgTradeReturn = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * MathLib.simpleAvgDouble(this.tradePLs), 0.001D));
/* 185 */     this.slippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.slippagePNL.doubleValue(), 0.01D));
/* 186 */     this.normSlippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.normSlippagePNL.doubleValue(), 0.01D));
/* 187 */     if (this.lossPLs.size() == 0) {
/* 188 */       this.tradeWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else
/* 190 */       this.tradeWinLoss = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvgDouble(this.winPLs) / MathLib.simpleAvgDouble(this.lossPLs), 0.01D));
/* 191 */     if (this.lossPLs.size() == 0) {
/* 192 */       this.profitFactor = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else
/* 194 */       this.profitFactor = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.sumDouble(this.winPLs) / MathLib.sumDouble(this.lossPLs), 0.01D));
/* 195 */     this.tradeHitRate = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * this.winCount / (this.winCount + this.lossCount), 0.01D));
/* 196 */     this.opTradePerc = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.openTradeQty.longValue() / this.totalTradeQty.longValue()), 0.01D));
/*     */     
/* 198 */     this.maxTradesPerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.tradeCounts.values()) / 2.0D, 0.01D));
/* 199 */     this.avgTradePerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.tradeCounts.values()) / 2.0D, 0.01D));
/*     */   }
/*     */   
/*     */   public void runSingleTradePP(ArrayList<String[]> tradeBook)
/*     */     throws ParseException
/*     */   {
/* 205 */     boolean inPos = false;
/* 206 */     this.tradePnL = Double.valueOf(0.0D);
/* 207 */     this.slipVal = Double.valueOf(0.0D);
/* 208 */     this.slipQty = Double.valueOf(0.0D);
/* 209 */     this.last_exit_trade = Integer.valueOf(0);
/*     */     
/* 211 */     Date tradeStartDT = null;Date tradeEndDT = null;
/* 212 */     int i = 0;
/* 213 */     while (i < tradeBook.size())
/*     */     {
/* 215 */       String[] trade = (String[])tradeBook.get(i);
/*     */       
/* 217 */       if (!inPos)
/*     */       {
/* 219 */         Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/* 220 */         tradeStartDT = dT;
/*     */         
/*     */ 
/* 223 */         trade = (String[])tradeBook.get(i);
/* 224 */         processTrade(trade, true);
/* 225 */         inPos = true;
/*     */       }
/*     */       else {
/* 228 */         boolean exit = processTrade(trade, false);
/*     */         
/*     */ 
/*     */ 
/* 232 */         if (exit)
/*     */         {
/*     */ 
/* 235 */           Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/* 236 */           tradeEndDT = dT;
/*     */           
/* 238 */           long tradeDuration = DateTime.getDateTimeDiff(tradeStartDT, tradeEndDT);
/* 239 */           this.tradeDurations.add(Long.valueOf(tradeDuration));
/* 240 */           this.tradePLs.add(this.tradePnL);
/* 241 */           if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() > 0) {
/* 242 */             this.winPLs.add(this.tradePnL);
/* 243 */             this.winDurations.add(Long.valueOf(tradeDuration));
/* 244 */             this.winCount += 1;
/* 245 */           } else if (MathLib.doubleCompare(this.tradePnL, Double.valueOf(0.0D)).intValue() < 0) {
/* 246 */             this.lossPLs.add(Double.valueOf(Math.abs(this.tradePnL.doubleValue())));
/* 247 */             this.lossDurations.add(Long.valueOf(tradeDuration));
/* 248 */             this.lossCount += 1;
/*     */           }
/* 250 */           this.tradeCount += 1;
/*     */           
/*     */ 
/* 253 */           this.tradePnL = Double.valueOf(0.0D);
/* 254 */           inPos = false;
/*     */         }
/*     */       }
/* 257 */       i++;
/*     */     }
/*     */     
/*     */ 
/* 261 */     this.avgTradeDuration = Double.valueOf(MathLib.simpleAvg(this.tradeDurations));
/* 262 */     this.avgWinDuration = Double.valueOf(MathLib.simpleAvg(this.winDurations));
/* 263 */     this.avgLossDuration = Double.valueOf(MathLib.simpleAvg(this.lossDurations));
/* 264 */     this.avgLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.leverageVals), 0.01D));
/* 265 */     this.maxLeverage = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.leverageVals), 0.01D));
/* 266 */     this.avgSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.slipVal.doubleValue() / this.slipQty.doubleValue()), 0.001D));
/* 267 */     this.avgOpenSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.opSlipVal.doubleValue() / this.opSlipQty.longValue()), 0.001D));
/* 268 */     this.avgNormSlippage = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.normSlipVal.doubleValue() / this.normSlipQty.longValue()), 0.001D));
/* 269 */     this.avgTradeReturn = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * MathLib.simpleAvgDouble(this.tradePLs), 0.001D));
/* 270 */     this.slippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.slippagePNL.doubleValue(), 0.01D));
/* 271 */     this.normSlippageFactor = Double.valueOf(MathLib.roundTickWithPrecision(1.0D + this.dollarPNL.doubleValue() / this.normSlippagePNL.doubleValue(), 0.01D));
/* 272 */     if (this.lossPLs.size() == 0) {
/* 273 */       this.tradeWinLoss = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else
/* 275 */       this.tradeWinLoss = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvgDouble(this.winPLs) / MathLib.simpleAvgDouble(this.lossPLs), 0.01D));
/* 276 */     if (this.lossPLs.size() == 0) {
/* 277 */       this.profitFactor = Double.valueOf(Double.POSITIVE_INFINITY);
/*     */     } else
/* 279 */       this.profitFactor = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.sumDouble(this.winPLs) / MathLib.sumDouble(this.lossPLs), 0.01D));
/* 280 */     this.tradeHitRate = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * this.winCount / (this.winCount + this.lossCount), 0.01D));
/* 281 */     this.opTradePerc = Double.valueOf(MathLib.roundTickWithPrecision(100.0D * (this.openTradeQty.longValue() / this.totalTradeQty.longValue()), 0.01D));
/* 282 */     this.maxTradesPerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.max(this.tradeCounts.values()) / 2.0D, 0.01D));
/* 283 */     this.avgTradePerDay = Double.valueOf(MathLib.roundTickWithPrecision(MathLib.simpleAvg(this.tradeCounts.values()) / 2.0D, 0.01D));
/*     */   }
/*     */   
/*     */ 
/*     */   public void runPortfolioPP(ArrayList<String[]> tradebook)
/*     */   {
/* 289 */     this.avgTradeDuration = Double.valueOf(0.0D);
/* 290 */     this.avgWinDuration = Double.valueOf(0.0D);
/* 291 */     this.avgLossDuration = Double.valueOf(0.0D);
/* 292 */     this.avgLeverage = Double.valueOf(0.0D);
/* 293 */     this.maxLeverage = Double.valueOf(0.0D);
/* 294 */     this.avgSlippage = Double.valueOf(0.0D);
/* 295 */     this.avgOpenSlippage = Double.valueOf(0.0D);
/* 296 */     this.avgNormSlippage = Double.valueOf(0.0D);
/* 297 */     this.avgTradeReturn = Double.valueOf(0.0D);
/* 298 */     this.slippageFactor = Double.valueOf(0.0D);
/* 299 */     this.normSlippageFactor = Double.valueOf(0.0D);
/* 300 */     this.tradeWinLoss = Double.valueOf(0.0D);
/* 301 */     this.profitFactor = Double.valueOf(0.0D);
/* 302 */     this.tradeHitRate = Double.valueOf(0.0D);
/* 303 */     this.opTradePerc = Double.valueOf(0.0D);
/* 304 */     this.maxTradesPerDay = Double.valueOf(0.0D);
/* 305 */     this.avgTradePerDay = Double.valueOf(0.0D);
/*     */   }
/*     */   
/*     */   public boolean processTrade(String[] trade, boolean entry) throws ParseException
/*     */   {
/* 310 */     Date dT = new SimpleDateFormat("yyyyMMddHHmmss").parse(trade[((Integer)this.tbIdxMap.get("dateTime")).intValue()]);
/* 311 */     String dateStr = new SimpleDateFormat("yyyyMMdd").format(dT);
/* 312 */     Long orderQty = Long.valueOf(Long.parseLong(trade[((Integer)this.tbIdxMap.get("qty")).intValue()]));
/* 313 */     Double execPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("execPrice")).intValue()]));
/* 314 */     Double capital = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("capital")).intValue()]));
/* 315 */     String orderSide = trade[((Integer)this.tbIdxMap.get("orderSide")).intValue()];
/* 316 */     String orderType = trade[((Integer)this.tbIdxMap.get("orderType")).intValue()];
/* 317 */     Double triggerPrice = Double.valueOf(Double.parseDouble(trade[((Integer)this.tbIdxMap.get("triggerPrice")).intValue()]));
/* 318 */     String triggerType = trade[((Integer)this.tbIdxMap.get("execType")).intValue()];
/* 319 */     String scripID = trade[((Integer)this.tbIdxMap.get("scripID")).intValue()];
/* 320 */     String scripListID = trade[((Integer)this.tbIdxMap.get("scripListID")).intValue()];
/*     */     
/*     */ 
/* 323 */     if (!orderType.equals("ROLLOVER")) {
/* 324 */       Double curCount = (Double)this.tradeCounts.get(dateStr);
/* 325 */       if (curCount == null) {
/* 326 */         this.tradeCounts.put(dateStr, Double.valueOf(1.0D));
/*     */       } else {
/* 328 */         this.tradeCounts.put(dateStr, Double.valueOf(curCount.doubleValue() + 1.0D));
/*     */       }
/* 330 */       this.totalTradeQty = Long.valueOf(this.totalTradeQty.longValue() + orderQty.longValue());
/* 331 */       if (triggerType.equalsIgnoreCase("OP")) {
/* 332 */         this.openTradeQty = Long.valueOf(this.openTradeQty.longValue() + orderQty.longValue());
/*     */       }
/*     */     }
/*     */     
/* 336 */     int tradeSignal = 0;
/* 337 */     if (orderSide.equalsIgnoreCase("Buy")) {
/* 338 */       tradeSignal = 1;
/* 339 */     } else if (orderSide.equalsIgnoreCase("Sell")) {
/* 340 */       tradeSignal = -1;
/*     */     }
/* 342 */     this.tradePnL = Double.valueOf(this.tradePnL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal / capital.doubleValue());
/* 343 */     this.dollarPNL = Double.valueOf(this.dollarPNL.doubleValue() + execPrice.doubleValue() * orderQty.longValue() * -tradeSignal);
/*     */     
/*     */ 
/* 346 */     if ((orderType.equals("STOP")) || (orderType.equals("EOD")) || (orderType.equals("OPEN")))
/*     */     {
/* 348 */       Double slip = Double.valueOf(tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()) / triggerPrice.doubleValue());
/* 349 */       this.slippagePNL = Double.valueOf(this.slippagePNL.doubleValue() + tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()));
/* 350 */       this.slipVal = Double.valueOf(this.slipVal.doubleValue() + slip.doubleValue());
/* 351 */       this.slipQty = Double.valueOf(this.slipQty.doubleValue() + orderQty.longValue());
/*     */       
/* 353 */       if (triggerType.equalsIgnoreCase("OP")) {
/* 354 */         this.opSlipVal = Double.valueOf(this.opSlipVal.doubleValue() + slip.doubleValue());
/* 355 */         this.opSlipQty = Long.valueOf(this.opSlipQty.longValue() + orderQty.longValue());
/*     */       }
/*     */       else
/*     */       {
/* 359 */         this.normSlippagePNL = Double.valueOf(this.normSlippagePNL.doubleValue() + tradeSignal * ((execPrice.doubleValue() - triggerPrice.doubleValue()) * orderQty.longValue()));
/* 360 */         this.normSlipVal = Double.valueOf(this.normSlipVal.doubleValue() + slip.doubleValue());
/* 361 */         this.normSlipQty = Long.valueOf(this.normSlipQty.longValue() + orderQty.longValue());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 366 */     Long newpos = Long.valueOf(((Long)((HashMap)this.posMap.get(scripListID)).get(scripID)).longValue() + tradeSignal * orderQty.longValue());
/*     */     
/* 368 */     HashMap<String, Long> scripListPosMap = (HashMap)this.posMap.get(scripListID);
/* 369 */     scripListPosMap.put(scripID, newpos);
/* 370 */     this.posMap.put(scripListID, scripListPosMap);
/*     */     
/*     */ 
/* 373 */     if (entry) {
/* 374 */       this.leverageVals.add(Double.valueOf(Math.abs(newpos.longValue()) * triggerPrice.doubleValue() / capital.doubleValue()));
/*     */     }
/*     */     
/* 377 */     if ((newpos.longValue() == 0L) && (!orderType.equals("ROLLOVER"))) {
/* 378 */       return true;
/*     */     }
/* 380 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/TradePostProcess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */