/*     */ package com.q1.bt.execution;
/*     */ 
/*     */ import com.q1.bt.data.classes.Contract;
/*     */ import com.q1.bt.data.classes.ContractData;
/*     */ import com.q1.bt.data.classes.MetaData;
/*     */ import com.q1.bt.execution.order.Order;
/*     */ import com.q1.bt.execution.order.OrderCondition;
/*     */ import com.q1.bt.execution.order.OrderSide;
/*     */ import com.q1.bt.execution.order.OrderType;
/*     */ import com.q1.bt.process.backtest.SlippageModel;
/*     */ import com.q1.csv.CSVWriter;
/*     */ import com.q1.exception.bt.MissingExpiryException;
/*     */ import com.q1.math.MathLib;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class ScripExecution
/*     */ {
/*     */   ExecutionData execData;
/*  21 */   ExecutionData prevExecData = null;
/*     */   
/*     */   Contract currentContract;
/*     */   
/*     */   SlippageModel slippageModel;
/*     */   RolloverMethod rolloverMethod;
/*     */   public ArrayList<Double> buyTrades;
/*     */   public ArrayList<Double> sellTrades;
/*  29 */   public ArrayList<String[]> tradeBook = new ArrayList();
/*     */   
/*     */   CSVWriter tbWriter;
/*     */   
/*  33 */   public Double $CumMTM = Double.valueOf(0.0D); public Double $PrevDayCumMTM = Double.valueOf(0.0D);
/*     */   
/*     */ 
/*  36 */   public Double $TradeMTM = Double.valueOf(0.0D); public Double $TradeCumMTM = Double.valueOf(0.0D); public Double $TradePrevDayCumMTM = Double.valueOf(0.0D);
/*     */   
/*     */ 
/*  39 */   public Double euroTradeCash = Double.valueOf(0.0D);
/*  40 */   public Double euroTradeMTM = Double.valueOf(0.0D); public Double euroTradeCumMTM = Double.valueOf(0.0D); public Double euroTradePrevCumMTM = Double.valueOf(0.0D); public Double euroTradePrevDayCumMTM = Double.valueOf(0.0D);
/*     */   
/*     */ 
/*  43 */   public Long position = Long.valueOf(0L);
/*  44 */   public Double usedCapital = Double.valueOf(0.0D);
/*     */   
/*     */   public Double capital;
/*     */   
/*     */   public Double exchangeRate;
/*     */   private boolean exit;
/*  50 */   EMA dailyTurnoverEMA = new EMA(250);
/*  51 */   double avgDailyTO = 1.0D;
/*     */   
/*     */ 
/*  54 */   KylesLambdaSigma kylesLambaSigma = new KylesLambdaSigma(Integer.valueOf(20), Integer.valueOf(1250));
/*  55 */   KylesLambdaATR kylesLambaATR = new KylesLambdaATR(Integer.valueOf(20), Integer.valueOf(1250));
/*     */   
/*     */   public ScripExecution(CSVWriter tbWriter, SlippageModel slippageModel, RolloverMethod rolloverMethod)
/*     */   {
/*  59 */     this.tbWriter = tbWriter;
/*  60 */     this.slippageModel = slippageModel;
/*  61 */     this.rolloverMethod = rolloverMethod;
/*     */   }
/*     */   
/*     */   public void updateWriter(CSVWriter tbWriter)
/*     */   {
/*  66 */     this.tbWriter = tbWriter;
/*     */   }
/*     */   
/*     */ 
/*     */   public void processOrders(ExecutionData execData, ArrayList<Order> orderBook, Double capital)
/*     */     throws Exception
/*     */   {
/*  73 */     this.tradeBook = new ArrayList();
/*     */     
/*     */ 
/*  76 */     this.capital = capital;
/*  77 */     this.usedCapital = Double.valueOf(0.0D);
/*  78 */     this.$TradeMTM = Double.valueOf(0.0D);
/*  79 */     this.exchangeRate = execData.rolloverContractData.contract.currency;
/*     */     
/*     */ 
/*  82 */     this.buyTrades = new ArrayList();
/*  83 */     this.sellTrades = new ArrayList();
/*     */     
/*     */ 
/*  86 */     this.prevExecData = this.execData;
/*  87 */     this.execData = new ExecutionData(execData);
/*     */     
/*     */ 
/*  90 */     Contract contract = execData.mainContractData.contract;
/*     */     Contract prevContract;
/*  92 */     Contract prevContract; if (this.prevExecData == null) {
/*  93 */       prevContract = null;
/*     */     } else {
/*  95 */       prevContract = this.prevExecData.mainContractData.contract;
/*     */     }
/*  97 */     this.currentContract = contract;
/*     */     
/*     */ 
/* 100 */     for (Order order : orderBook)
/*     */     {
/*     */ 
/* 103 */       if (checkOrderConditions(order))
/*     */       {
/*     */ 
/*     */ 
/* 107 */         boolean orderFill = checkOrderFill(order, contract);
/*     */         
/*     */ 
/* 110 */         if (orderFill) {
/* 111 */           executeOrder(order, contract, prevContract);
/*     */         }
/*     */       }
/*     */     }
/* 115 */     if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
/* 116 */       (execData.rolloverContractData != null) && 
/* 117 */       (!this.prevExecData.date.equals(execData.date)))
/*     */     {
/*     */ 
/* 120 */       double dayTurnover = execData.rolloverContractData.contract.cl.doubleValue() * 
/* 121 */         execData.rolloverContractData.contract.vol.doubleValue() * execData.metaData.lotSize.doubleValue() * 
/* 122 */         execData.metaData.lotFactor.doubleValue();
/* 123 */       if (dayTurnover > 0.0D) {
/* 124 */         this.avgDailyTO = this.dailyTurnoverEMA.calculateEMA(dayTurnover).doubleValue();
/*     */       }
/*     */       
/* 127 */       this.kylesLambaSigma.updateETA(execData.rolloverContractData.contract.cl, 
/* 128 */         execData.rolloverContractData.contract.vol, execData.rolloverContractData.contract.rolloverCl);
/* 129 */       this.kylesLambaATR.updateETA(execData.rolloverContractData.contract.hi, 
/* 130 */         execData.rolloverContractData.contract.lo, execData.rolloverContractData.contract.cl, 
/* 131 */         execData.rolloverContractData.contract.vol, execData.rolloverContractData.contract.rolloverCl);
/*     */       
/*     */ 
/* 134 */       double rolloverCl = execData.rolloverContractData.contract.rolloverCl.doubleValue();
/* 135 */       if (rolloverCl > 0.0D)
/*     */       {
/* 137 */         if (this.rolloverMethod.equals(RolloverMethod.CloseToClose)) {
/* 138 */           runRolloverRoutine();
/*     */         }
/* 140 */         else if (this.rolloverMethod.equals(RolloverMethod.ExitAtClose)) {
/* 141 */           runExitRoutine();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */     this.euroTradeCumMTM = Double.valueOf(this.euroTradeCash.doubleValue() + this.position.longValue() * this.currentContract.cl.doubleValue());
/*     */     
/*     */ 
/* 153 */     this.euroTradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() - this.euroTradePrevCumMTM.doubleValue());
/* 154 */     this.euroTradePrevCumMTM = this.euroTradeCumMTM;
/*     */     
/*     */ 
/*     */ 
/* 158 */     this.$TradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue());
/*     */     
/*     */ 
/* 161 */     this.$TradeCumMTM = Double.valueOf(this.$TradeCumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*     */     
/*     */ 
/* 164 */     this.$CumMTM = Double.valueOf(this.$CumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*     */     
/*     */ 
/* 167 */     if (this.exit)
/*     */     {
/* 169 */       this.euroTradeCash = (this.euroTradePrevCumMTM = this.$TradeCumMTM = Double.valueOf(0.0D));
/* 170 */       this.exit = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Long getQuantity(Double price, Double capital, Double lotSize, Double leverage)
/*     */   {
/* 177 */     return Long.valueOf(MathLib.roundTick(capital.doubleValue() * leverage.doubleValue() / price.doubleValue(), lotSize.doubleValue()));
/*     */   }
/*     */   
/*     */   public boolean checkOrderConditions(Order order) throws MissingExpiryException
/*     */   {
/* 182 */     boolean conditionVal = true;
/* 183 */     for (OrderCondition orderCondition : order.conditions) {
/* 184 */       conditionVal = (conditionVal) && (orderCondition.checkCondition(this.execData));
/* 185 */       if (!conditionVal)
/* 186 */         return conditionVal;
/*     */     }
/* 188 */     return conditionVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkOrderFill(Order order, Contract contract)
/*     */   {
/* 195 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/*     */ 
/* 198 */       if (order.type.equals(OrderType.STOP)) {
/* 199 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 200 */           return true;
/*     */         }
/*     */       }
/* 203 */       else if (order.type.equals(OrderType.LIMIT)) {
/* 204 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() < 0) {
/* 205 */           return true;
/*     */         }
/*     */       }
/* 208 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/* 209 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 210 */           return true;
/*     */         }
/*     */       }
/* 213 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/* 214 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 215 */           return true;
/*     */         }
/*     */       }
/*     */       else {
/* 219 */         return true;
/*     */       }
/*     */     }
/* 222 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/*     */ 
/* 225 */       if (order.type.equals(OrderType.STOP)) {
/* 226 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 227 */           return true;
/*     */         }
/*     */       }
/* 230 */       else if (order.type.equals(OrderType.LIMIT)) {
/* 231 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() > 0) {
/* 232 */           return true;
/*     */         }
/*     */       }
/* 235 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/* 236 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 237 */           return true;
/*     */         }
/*     */       }
/* 240 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/* 241 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 242 */           return true;
/*     */         }
/*     */       }
/*     */       else
/* 246 */         return true;
/*     */     }
/* 248 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HashMap<String, Object> getTriggerDetails(Order order, Contract contract, Contract prevContract)
/*     */   {
/* 255 */     String triggerType = "NORM";
/* 256 */     Double triggerPrice = Double.valueOf(0.0D);
/* 257 */     HashMap<String, Object> triggerMap = new HashMap();
/* 258 */     triggerMap.put("Trigger Price", triggerPrice);
/* 259 */     triggerMap.put("Capital Price", order.price);
/* 260 */     triggerMap.put("Trigger Type", triggerType);
/*     */     
/*     */ 
/* 263 */     Double prevClose = null;
/* 264 */     if (prevContract != null) {
/* 265 */       prevClose = prevContract.cl;
/*     */     } else {
/* 267 */       prevClose = contract.cl;
/*     */     }
/*     */     
/* 270 */     if (order.type.equals(OrderType.EOD)) {
/* 271 */       triggerPrice = contract.cl;
/* 272 */       triggerMap.put("Trigger Price", triggerPrice);
/* 273 */       triggerMap.put("Capital Price", prevClose);
/* 274 */       return triggerMap;
/*     */     }
/*     */     
/* 277 */     if (order.type.equals(OrderType.ROLLOVER)) {
/* 278 */       triggerPrice = contract.cl;
/* 279 */       triggerMap.put("Trigger Price", triggerPrice);
/* 280 */       triggerMap.put("Capital Price", Double.valueOf(0.0D));
/* 281 */       return triggerMap;
/*     */     }
/*     */     
/* 284 */     if ((order.type.equals(OrderType.OPEN)) || (order.type.equals(OrderType.OPENWITHSLIP))) {
/* 285 */       triggerPrice = contract.op;
/* 286 */       triggerMap.put("Trigger Price", triggerPrice);
/* 287 */       triggerMap.put("Capital Price", prevClose);
/* 288 */       return triggerMap;
/*     */     }
/*     */     
/*     */ 
/* 292 */     triggerPrice = order.price;
/*     */     
/*     */ 
/* 295 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/* 297 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/* 299 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0) {
/* 300 */           triggerPrice = contract.op;
/* 301 */           triggerType = "OP";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 306 */       else if (order.type.equals(OrderType.LIMIT))
/*     */       {
/* 308 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0)
/*     */         {
/* 310 */           if (this.execData.dataType.contains("D")) {
/* 311 */             triggerPrice = contract.op;
/* 312 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 315 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 316 */             triggerPrice = contract.op;
/* 317 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 323 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/* 325 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*     */         {
/* 327 */           if (this.execData.dataType.contains("D")) {
/* 328 */             triggerPrice = contract.op;
/* 329 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 332 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 333 */             triggerPrice = contract.op;
/* 334 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 340 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/* 342 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*     */         {
/* 344 */           if (this.execData.dataType.contains("D")) {
/* 345 */             triggerPrice = contract.op;
/* 346 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 349 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 350 */             triggerPrice = contract.op;
/* 351 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 357 */       else if (order.type.equals(OrderType.MARKET)) {
/* 358 */         triggerPrice = contract.hi;
/* 359 */         triggerMap.put("Capital Price", prevClose);
/*     */       }
/*     */       
/*     */     }
/* 363 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/* 365 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/* 367 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0) {
/* 368 */           triggerPrice = contract.op;
/* 369 */           triggerType = "OP";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 374 */       else if (order.type.equals(OrderType.LIMIT))
/*     */       {
/* 376 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 378 */           if (this.execData.dataType.contains("D")) {
/* 379 */             triggerPrice = contract.op;
/* 380 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 383 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 384 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 389 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/* 391 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 393 */           if (this.execData.dataType.contains("D")) {
/* 394 */             triggerPrice = contract.op;
/* 395 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 398 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 399 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 404 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/* 406 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 408 */           if (this.execData.dataType.contains("D")) {
/* 409 */             triggerPrice = contract.op;
/* 410 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 413 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 414 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 419 */       else if (order.type.equals(OrderType.MARKET)) {
/* 420 */         triggerPrice = contract.lo;
/* 421 */         triggerMap.put("Capital Price", prevClose);
/*     */       }
/*     */     }
/* 424 */     triggerMap.put("Trigger Price", triggerPrice);
/* 425 */     triggerMap.put("Trigger Type", triggerType);
/* 426 */     return triggerMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, Double> runSlippageModel(Order order, Double triggerPrice)
/*     */     throws Exception
/*     */   {
/* 432 */     Double slippage = Double.valueOf(0.0D);
/* 433 */     Double openSlippage = Double.valueOf(0.0D);
/*     */     
/*     */ 
/* 436 */     if (this.slippageModel.equals(SlippageModel.ConstantSlippage)) {
/* 437 */       slippage = this.execData.metaData.slippage;
/* 438 */       openSlippage = this.execData.metaData.openSlippage;
/*     */ 
/*     */ 
/*     */     }
/* 442 */     else if (this.slippageModel.equals(SlippageModel.TickSlippage)) {
/* 443 */       slippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/* 444 */       openSlippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/*     */ 
/*     */ 
/*     */     }
/* 448 */     else if ((this.slippageModel.equals(SlippageModel.LinearModel)) || 
/* 449 */       (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)))
/*     */     {
/* 451 */       Double slippageSlope = this.execData.metaData.slippageSlope;
/*     */       
/* 453 */       if (slippageSlope == null) {
/* 454 */         throw new Exception("Slippage Slope not available. Can't use Linear Model!!");
/*     */       }
/* 456 */       if (this.execData.dataType.contains("D")) {
/* 457 */         slippageSlope = Double.valueOf(2.0D * this.execData.metaData.slippageSlope.doubleValue());
/*     */       }
/*     */       
/*     */ 
/* 461 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue() / 1000000.0D;
/* 462 */       Double slippageIntercept = this.execData.metaData.slippageIntercept;
/* 463 */       if (slippageIntercept == null)
/* 464 */         throw new Exception("Slippage Intercept not available. Can't use Linear Model!!");
/* 465 */       slippage = Double.valueOf(slippageIntercept.doubleValue() + turnover * slippageSlope.doubleValue());
/*     */       
/* 467 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 472 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelATR))
/*     */     {
/* 474 */       Double dailyMovementSlope = Double.valueOf(0.004601D);
/* 475 */       Double kylesSlippageSlope = Double.valueOf(0.8510395D);
/* 476 */       Double tickSizeRatioSlope = Double.valueOf(-0.1511881D);
/*     */       
/*     */ 
/* 479 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - 
/* 480 */         this.execData.rolloverContractData.contract.lo.doubleValue()) / this.execData.rolloverContractData.contract.cl.doubleValue();
/* 481 */       double kylesSlippage = this.kylesLambaATR.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 482 */         this.execData.metaData.slippage) / 100.0D;
/* 483 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*     */       
/*     */ 
/* 486 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * kylesSlippage + 
/* 487 */         tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
/*     */       
/* 489 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/* 490 */         slippage = this.execData.metaData.slippage;
/*     */       }
/* 492 */       if (slippage.doubleValue() < 0.0D) {
/* 493 */         slippage = Double.valueOf(0.0D);
/*     */       }
/* 495 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 500 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelSigma))
/*     */     {
/* 502 */       Double dailyMovementSlope = Double.valueOf(0.0062692D);
/* 503 */       Double kylesSlippageSlope = Double.valueOf(0.6521675D);
/* 504 */       Double stdDevJSlope = Double.valueOf(-0.8228327D);
/* 505 */       Double expbyTurnOverSlope = Double.valueOf(0.0785597D);
/* 506 */       Double tickSizeRatioSlope = Double.valueOf(-0.1388063D);
/*     */       
/*     */ 
/* 509 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - 
/* 510 */         this.execData.rolloverContractData.contract.lo.doubleValue()) / this.execData.rolloverContractData.contract.cl.doubleValue();
/* 511 */       double kylesSlippage = this.kylesLambaSigma.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 512 */         this.execData.metaData.slippage) / 100.0D;
/* 513 */       double stdDevJ = this.kylesLambaSigma.getStdDevJs(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
/* 514 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue();
/* 515 */       double expByTurnover = turnover / this.avgDailyTO;
/* 516 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*     */       
/*     */ 
/* 519 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * kylesSlippage + stdDevJSlope.doubleValue() * stdDevJ + 
/* 520 */         expbyTurnOverSlope.doubleValue() * expByTurnover + tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
/*     */       
/* 522 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/* 523 */         slippage = this.execData.metaData.slippage;
/*     */       }
/* 525 */       if (slippage.doubleValue() < 0.0D) {
/* 526 */         slippage = Double.valueOf(0.0D);
/*     */       }
/* 528 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 533 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaSigma))
/*     */     {
/* 535 */       slippage = Double.valueOf(this.kylesLambaSigma.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 536 */         this.execData.metaData.slippage));
/*     */       
/* 538 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */     }
/* 542 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaATR))
/*     */     {
/* 544 */       slippage = Double.valueOf(this.kylesLambaATR.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 545 */         this.execData.metaData.slippage));
/*     */       
/* 547 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */     }
/* 551 */     else if (!this.slippageModel.equals(SlippageModel.ZeroSlippage)) {}
/*     */     
/*     */ 
/*     */ 
/* 555 */     HashMap<String, Double> slippageMap = new HashMap();
/* 556 */     slippageMap.put("Slippage", slippage);
/* 557 */     slippageMap.put("Open Slippage", openSlippage);
/*     */     
/* 559 */     return slippageMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getExecutionPrice(Order order, HashMap<String, Double> slippageMap, Double triggerPrice, String triggerType)
/*     */   {
/* 567 */     Double slippage = (Double)slippageMap.get("Slippage");
/* 568 */     Double openSlippage = (Double)slippageMap.get("Open Slippage");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 573 */     Double lotExposure = Double.valueOf(this.execData.metaData.lotSize.doubleValue() * triggerPrice.doubleValue());
/* 574 */     Double transactionCharges = Double.valueOf((this.execData.metaData.exchangeFees.doubleValue() + this.execData.metaData.brokerage.doubleValue()) * 
/* 575 */       this.execData.rolloverContractData.contract.currency.doubleValue() / lotExposure.doubleValue());
/*     */     
/*     */ 
/* 578 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/*     */ 
/* 581 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/*     */ 
/* 584 */         if (triggerType.equals("OP")) {
/* 585 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 588 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 592 */       if (order.type.equals(OrderType.LIMIT))
/*     */       {
/*     */ 
/* 595 */         slippage = Double.valueOf(0.0D);
/* 596 */         if (triggerType.equals("OP")) {
/* 597 */           if (this.execData.dataType.contains("D"))
/* 598 */             slippage = openSlippage;
/*     */         } else {
/* 600 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 603 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 607 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/*     */ 
/* 610 */         slippage = Double.valueOf(0.0D);
/* 611 */         if (triggerType.equals("OP")) {
/* 612 */           if (this.execData.dataType.contains("D"))
/* 613 */             slippage = openSlippage;
/*     */         } else {
/* 615 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 618 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 622 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/*     */ 
/* 625 */         if (triggerType.equals("OP")) {
/* 626 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 629 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 633 */       if (order.type.equals(OrderType.MARKET))
/*     */       {
/* 635 */         return triggerPrice;
/*     */       }
/*     */       
/* 638 */       if ((order.type.equals(OrderType.EOD)) || (order.type.equals(OrderType.ROLLOVER)))
/*     */       {
/* 640 */         if (this.execData.dataType.contains("D"))
/* 641 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/* 642 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/* 643 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()) + this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*     */         }
/* 645 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 649 */       if (order.type.equals(OrderType.OPEN))
/*     */       {
/*     */ 
/* 652 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 656 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*     */       {
/*     */ 
/* 659 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 664 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/* 666 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/*     */ 
/* 669 */         if (triggerType.equals("OP")) {
/* 670 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 673 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 678 */       if (order.type.equals(OrderType.LIMIT))
/*     */       {
/*     */ 
/* 681 */         slippage = Double.valueOf(0.0D);
/* 682 */         if (triggerType.equals("OP")) {
/* 683 */           if (this.execData.dataType.contains("D"))
/* 684 */             slippage = openSlippage;
/*     */         } else {
/* 686 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 689 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 693 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/*     */ 
/* 696 */         slippage = Double.valueOf(0.0D);
/* 697 */         if (triggerType.equals("OP")) {
/* 698 */           if (this.execData.dataType.contains("D"))
/* 699 */             slippage = openSlippage;
/*     */         } else {
/* 701 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 704 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 708 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/*     */ 
/* 711 */         if (triggerType.equals("OP")) {
/* 712 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 715 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 719 */       if (order.type.equals(OrderType.MARKET)) {
/* 720 */         return triggerPrice;
/*     */       }
/*     */       
/* 723 */       if ((order.type.equals(OrderType.EOD)) || (order.type.equals(OrderType.ROLLOVER)))
/*     */       {
/* 725 */         if (this.execData.dataType.contains("D"))
/* 726 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/* 727 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/* 728 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()) - this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*     */         }
/* 730 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 734 */       if (order.type.equals(OrderType.OPEN)) {
/* 735 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()));
/*     */       }
/*     */       
/* 738 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*     */       {
/* 740 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 745 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updatePosition(Double capitalPrice, Double execPrice, Order order)
/*     */   {
/* 753 */     this.exit = false;
/*     */     
/*     */ 
/* 756 */     int signal = -1;
/* 757 */     if (order.side.equals(OrderSide.BUY)) {
/* 758 */       signal = 1;
/*     */     }
/*     */     
/*     */ 
/* 762 */     if ((this.position.equals(Long.valueOf(0L))) && (!order.type.equals(OrderType.ROLLOVER))) {
/* 763 */       this.euroTradeCash = Double.valueOf(0.0D);
/*     */ 
/*     */     }
/* 766 */     else if (this.position.longValue() * signal > 0L) {
/* 767 */       this.usedCapital = Double.valueOf(this.usedCapital.doubleValue() + capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * order.quantity.longValue() / this.execData.metaData.leverage.intValue());
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 772 */       long absPosition = Math.abs(this.position.longValue());
/*     */       
/*     */ 
/* 775 */       if (order.quantity.longValue() > absPosition) {
/* 776 */         this.usedCapital = Double.valueOf(capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * (order.quantity.longValue() - absPosition) / this.execData.metaData.leverage.intValue());
/*     */ 
/*     */ 
/*     */       }
/* 780 */       else if (!order.type.equals(OrderType.ROLLOVER)) {
/* 781 */         this.usedCapital = Double.valueOf(0.0D);
/* 782 */         this.exit = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 787 */     this.position = Long.valueOf(this.position.longValue() + signal * order.quantity.longValue());
/* 788 */     this.euroTradeCash = Double.valueOf(this.euroTradeCash.doubleValue() - signal * execPrice.doubleValue() * order.quantity.longValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTradebook(Order order, Double triggerPrice, String triggerType, Double execPrice)
/*     */     throws IOException
/*     */   {
/* 797 */     Double tradeCumMTM = this.$TradeCumMTM;
/* 798 */     if (!this.exit) {
/* 799 */       tradeCumMTM = Double.valueOf(0.0D);
/*     */     }
/*     */     else
/*     */     {
/* 803 */       double $TradeMTM = this.euroTradeCash.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue();
/*     */       
/*     */ 
/* 806 */       tradeCumMTM = Double.valueOf((this.$TradeCumMTM.doubleValue() + $TradeMTM) / this.capital.doubleValue());
/*     */     }
/* 808 */     String[] trade = { this.execData.dateTime.toString(), this.capital.toString(), this.execData.scripID, 
/* 809 */       order.side.getStringVal(), order.type.getStringVal(), triggerPrice.toString(), execPrice.toString(), 
/* 810 */       order.quantity.toString(), triggerType, this.execData.scripListID, this.execData.prevOrderTimestamp.toString(), 
/* 811 */       tradeCumMTM.toString() };
/* 812 */     this.tradeBook.add(trade);
/* 813 */     this.tbWriter.writeLine(trade);
/*     */     
/*     */ 
/* 816 */     if (order.side == OrderSide.BUY) {
/* 817 */       this.buyTrades.add(execPrice);
/* 818 */     } else if (order.side == OrderSide.SELL) {
/* 819 */       this.sellTrades.add(execPrice);
/*     */     }
/*     */   }
/*     */   
/*     */   public void runExitRoutine() throws Exception
/*     */   {
/* 825 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/* 826 */     Contract rolloverToContract = new Contract();
/* 827 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*     */     
/*     */ 
/* 830 */     if (this.position.longValue() > 0L)
/*     */     {
/*     */ 
/* 833 */       Long orderQty = this.position;
/*     */       
/*     */ 
/* 836 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.EOD, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 839 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/*     */       
/*     */ 
/* 842 */       this.currentContract = rolloverToContract;
/*     */     }
/* 844 */     else if (this.position.longValue() < 0L)
/*     */     {
/*     */ 
/* 847 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*     */       
/*     */ 
/* 850 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.EOD, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 853 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/*     */       
/*     */ 
/* 856 */       this.currentContract = rolloverToContract;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void runRolloverRoutine()
/*     */     throws Exception
/*     */   {
/* 864 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/* 865 */     Contract rolloverToContract = new Contract();
/* 866 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*     */     
/*     */ 
/* 869 */     if (this.position.longValue() > 0L)
/*     */     {
/*     */ 
/* 872 */       Long orderQty = this.position;
/*     */       
/*     */ 
/* 875 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/* 876 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 879 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/* 880 */       executeOrder(rollBuyOrder, rolloverToContract, null);
/*     */       
/*     */ 
/* 883 */       this.currentContract = rolloverToContract;
/*     */     }
/* 885 */     else if (this.position.longValue() < 0L)
/*     */     {
/*     */ 
/* 888 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*     */       
/*     */ 
/* 891 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/* 892 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 895 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/* 896 */       executeOrder(rollSellOrder, rolloverToContract, null);
/*     */       
/*     */ 
/* 899 */       this.currentContract = rolloverToContract;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void executeOrder(Order order, Contract contract, Contract prevContract)
/*     */     throws Exception
/*     */   {
/* 908 */     HashMap<String, Object> triggerMap = getTriggerDetails(order, contract, prevContract);
/*     */     
/*     */ 
/* 911 */     Double triggerPrice = (Double)triggerMap.get("Trigger Price");
/* 912 */     Double capitalPrice = (Double)triggerMap.get("Capital Price");
/* 913 */     String triggerType = (String)triggerMap.get("Trigger Type");
/*     */     
/*     */ 
/* 916 */     HashMap<String, Double> slippageMap = runSlippageModel(order, triggerPrice);
/*     */     
/*     */ 
/* 919 */     Double executionPrice = getExecutionPrice(order, slippageMap, triggerPrice, triggerType);
/*     */     
/*     */ 
/* 922 */     updatePosition(capitalPrice, executionPrice, order);
/*     */     
/*     */ 
/* 925 */     updateTradebook(order, capitalPrice, triggerType, executionPrice);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ScripExecution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */