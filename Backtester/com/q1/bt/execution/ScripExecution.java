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
/* 100 */     if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
/* 101 */       (execData.rolloverContractData != null) && 
/* 102 */       (!this.prevExecData.date.equals(execData.date)))
/*     */     {
/*     */ 
/* 105 */       double dayTurnover = execData.rolloverContractData.contract.cl.doubleValue() * 
/* 106 */         execData.rolloverContractData.contract.vol.doubleValue() * execData.metaData.lotSize.doubleValue() * 
/* 107 */         execData.metaData.lotFactor.doubleValue();
/* 108 */       if (dayTurnover > 0.0D) {
/* 109 */         this.avgDailyTO = this.dailyTurnoverEMA.calculateEMA(dayTurnover).doubleValue();
/*     */       }
/*     */       
/* 112 */       this.kylesLambaSigma.updateETA(execData.rolloverContractData.contract.cl, 
/* 113 */         execData.rolloverContractData.contract.vol, execData.rolloverContractData.contract.rolloverCl);
/* 114 */       this.kylesLambaATR.updateETA(execData.rolloverContractData.contract.hi, 
/* 115 */         execData.rolloverContractData.contract.lo, execData.rolloverContractData.contract.cl, 
/* 116 */         execData.rolloverContractData.contract.vol, execData.rolloverContractData.contract.rolloverCl);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */     for (Order order : orderBook)
/*     */     {
/*     */ 
/* 126 */       if (checkOrderConditions(order))
/*     */       {
/*     */ 
/*     */ 
/* 130 */         boolean orderFill = checkOrderFill(order, contract);
/*     */         
/*     */ 
/* 133 */         if (orderFill) {
/* 134 */           executeOrder(order, contract, prevContract);
/*     */         }
/*     */       }
/*     */     }
/* 138 */     if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
/* 139 */       (execData.rolloverContractData != null) && 
/* 140 */       (!this.prevExecData.date.equals(execData.date)))
/*     */     {
/*     */ 
/* 143 */       double rolloverCl = execData.rolloverContractData.contract.rolloverCl.doubleValue();
/* 144 */       if (rolloverCl > 0.0D)
/*     */       {
/* 146 */         if (this.rolloverMethod.equals(RolloverMethod.CloseToClose)) {
/* 147 */           runRolloverRoutine();
/*     */         }
/* 149 */         else if (this.rolloverMethod.equals(RolloverMethod.ExitAtClose)) {
/* 150 */           runExitRoutine();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */     this.euroTradeCumMTM = Double.valueOf(this.euroTradeCash.doubleValue() + this.position.longValue() * this.currentContract.cl.doubleValue());
/*     */     
/*     */ 
/* 162 */     this.euroTradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() - this.euroTradePrevCumMTM.doubleValue());
/* 163 */     this.euroTradePrevCumMTM = this.euroTradeCumMTM;
/*     */     
/*     */ 
/*     */ 
/* 167 */     this.$TradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue());
/*     */     
/*     */ 
/* 170 */     this.$TradeCumMTM = Double.valueOf(this.$TradeCumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*     */     
/*     */ 
/* 173 */     this.$CumMTM = Double.valueOf(this.$CumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*     */     
/*     */ 
/* 176 */     if (this.exit)
/*     */     {
/* 178 */       this.euroTradeCash = (this.euroTradePrevCumMTM = this.$TradeCumMTM = Double.valueOf(0.0D));
/* 179 */       this.exit = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Long getQuantity(Double price, Double capital, Double lotSize, Double leverage)
/*     */   {
/* 186 */     return Long.valueOf(MathLib.roundTick(capital.doubleValue() * leverage.doubleValue() / price.doubleValue(), lotSize.doubleValue()));
/*     */   }
/*     */   
/*     */   public boolean checkOrderConditions(Order order) throws MissingExpiryException
/*     */   {
/* 191 */     boolean conditionVal = true;
/* 192 */     for (OrderCondition orderCondition : order.conditions) {
/* 193 */       conditionVal = (conditionVal) && (orderCondition.checkCondition(this.execData));
/* 194 */       if (!conditionVal)
/* 195 */         return conditionVal;
/*     */     }
/* 197 */     return conditionVal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean checkOrderFill(Order order, Contract contract)
/*     */   {
/* 204 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/*     */ 
/* 207 */       if (order.type.equals(OrderType.STOP)) {
/* 208 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 209 */           return true;
/*     */         }
/*     */       }
/* 212 */       else if (order.type.equals(OrderType.LIMIT)) {
/* 213 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() < 0) {
/* 214 */           return true;
/*     */         }
/*     */       }
/* 217 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/* 218 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 219 */           return true;
/*     */         }
/*     */       }
/* 222 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/* 223 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 224 */           return true;
/*     */         }
/*     */       }
/*     */       else {
/* 228 */         return true;
/*     */       }
/*     */     }
/* 231 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/*     */ 
/* 234 */       if (order.type.equals(OrderType.STOP)) {
/* 235 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/* 236 */           return true;
/*     */         }
/*     */       }
/* 239 */       else if (order.type.equals(OrderType.LIMIT)) {
/* 240 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() > 0) {
/* 241 */           return true;
/*     */         }
/*     */       }
/* 244 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/* 245 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 246 */           return true;
/*     */         }
/*     */       }
/* 249 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/* 250 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/* 251 */           return true;
/*     */         }
/*     */       }
/*     */       else
/* 255 */         return true;
/*     */     }
/* 257 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HashMap<String, Object> getTriggerDetails(Order order, Contract contract, Contract prevContract)
/*     */   {
/* 264 */     String triggerType = "NORM";
/* 265 */     Double triggerPrice = Double.valueOf(0.0D);
/* 266 */     HashMap<String, Object> triggerMap = new HashMap();
/* 267 */     triggerMap.put("Trigger Price", triggerPrice);
/* 268 */     triggerMap.put("Capital Price", order.price);
/* 269 */     triggerMap.put("Trigger Type", triggerType);
/*     */     
/*     */ 
/* 272 */     Double prevClose = null;
/* 273 */     if (prevContract != null) {
/* 274 */       prevClose = prevContract.cl;
/*     */     } else {
/* 276 */       prevClose = contract.cl;
/*     */     }
/*     */     
/* 279 */     if (order.type.equals(OrderType.EOD)) {
/* 280 */       triggerPrice = contract.cl;
/* 281 */       triggerMap.put("Trigger Price", triggerPrice);
/* 282 */       triggerMap.put("Capital Price", prevClose);
/* 283 */       return triggerMap;
/*     */     }
/*     */     
/* 286 */     if (order.type.equals(OrderType.ROLLOVER)) {
/* 287 */       triggerPrice = contract.cl;
/* 288 */       triggerMap.put("Trigger Price", triggerPrice);
/* 289 */       triggerMap.put("Capital Price", Double.valueOf(0.0D));
/* 290 */       return triggerMap;
/*     */     }
/*     */     
/* 293 */     if ((order.type.equals(OrderType.OPEN)) || (order.type.equals(OrderType.OPENWITHSLIP))) {
/* 294 */       triggerPrice = contract.op;
/* 295 */       triggerMap.put("Trigger Price", triggerPrice);
/* 296 */       triggerMap.put("Capital Price", prevClose);
/* 297 */       return triggerMap;
/*     */     }
/*     */     
/*     */ 
/* 301 */     triggerPrice = order.price;
/*     */     
/*     */ 
/* 304 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/* 306 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/* 308 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0) {
/* 309 */           triggerPrice = contract.op;
/* 310 */           triggerType = "OP";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 315 */       else if (order.type.equals(OrderType.LIMIT))
/*     */       {
/* 317 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0)
/*     */         {
/* 319 */           if (this.execData.dataType.contains("D")) {
/* 320 */             triggerPrice = contract.op;
/* 321 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 324 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 325 */             triggerPrice = contract.op;
/* 326 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 332 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/* 334 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*     */         {
/* 336 */           if (this.execData.dataType.contains("D")) {
/* 337 */             triggerPrice = contract.op;
/* 338 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 341 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 342 */             triggerPrice = contract.op;
/* 343 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 349 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/* 351 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*     */         {
/* 353 */           if (this.execData.dataType.contains("D")) {
/* 354 */             triggerPrice = contract.op;
/* 355 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 358 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/* 359 */             triggerPrice = contract.op;
/* 360 */             triggerType = "OP";
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/* 366 */       else if (order.type.equals(OrderType.MARKET)) {
/* 367 */         triggerPrice = contract.hi;
/* 368 */         triggerMap.put("Capital Price", prevClose);
/*     */       }
/*     */       
/*     */     }
/* 372 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/* 374 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/* 376 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0) {
/* 377 */           triggerPrice = contract.op;
/* 378 */           triggerType = "OP";
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 383 */       else if (order.type.equals(OrderType.LIMIT))
/*     */       {
/* 385 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 387 */           if (this.execData.dataType.contains("D")) {
/* 388 */             triggerPrice = contract.op;
/* 389 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 392 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 393 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 398 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/* 400 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 402 */           if (this.execData.dataType.contains("D")) {
/* 403 */             triggerPrice = contract.op;
/* 404 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 407 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 408 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 413 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/* 415 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*     */         {
/* 417 */           if (this.execData.dataType.contains("D")) {
/* 418 */             triggerPrice = contract.op;
/* 419 */             triggerType = "OP";
/*     */ 
/*     */           }
/* 422 */           else if ((prevClose == null) || (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/* 423 */             triggerPrice = contract.op;
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 428 */       else if (order.type.equals(OrderType.MARKET)) {
/* 429 */         triggerPrice = contract.lo;
/* 430 */         triggerMap.put("Capital Price", prevClose);
/*     */       }
/*     */     }
/* 433 */     triggerMap.put("Trigger Price", triggerPrice);
/* 434 */     triggerMap.put("Trigger Type", triggerType);
/* 435 */     return triggerMap;
/*     */   }
/*     */   
/*     */   public HashMap<String, Double> runSlippageModel(Order order, Double triggerPrice)
/*     */     throws Exception
/*     */   {
/* 441 */     Double slippage = Double.valueOf(0.0D);
/* 442 */     Double openSlippage = Double.valueOf(0.0D);
/*     */     
/*     */ 
/* 445 */     if (this.slippageModel.equals(SlippageModel.ConstantSlippage)) {
/* 446 */       slippage = this.execData.metaData.slippage;
/* 447 */       openSlippage = this.execData.metaData.openSlippage;
/*     */ 
/*     */ 
/*     */     }
/* 451 */     else if (this.slippageModel.equals(SlippageModel.TickSlippage)) {
/* 452 */       slippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/* 453 */       openSlippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/*     */ 
/*     */ 
/*     */     }
/* 457 */     else if ((this.slippageModel.equals(SlippageModel.LinearModel)) || 
/* 458 */       (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)))
/*     */     {
/* 460 */       Double slippageSlope = this.execData.metaData.slippageSlope;
/*     */       
/* 462 */       if (slippageSlope == null) {
/* 463 */         throw new Exception("Slippage Slope not available. Can't use Linear Model!!");
/*     */       }
/* 465 */       if (this.execData.dataType.contains("D")) {
/* 466 */         slippageSlope = Double.valueOf(2.0D * this.execData.metaData.slippageSlope.doubleValue());
/*     */       }
/*     */       
/*     */ 
/* 470 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue() / 1000000.0D;
/* 471 */       Double slippageIntercept = this.execData.metaData.slippageIntercept;
/* 472 */       if (slippageIntercept == null)
/* 473 */         throw new Exception("Slippage Intercept not available. Can't use Linear Model!!");
/* 474 */       slippage = Double.valueOf(slippageIntercept.doubleValue() + turnover * slippageSlope.doubleValue());
/*     */       
/* 476 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 481 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelATR))
/*     */     {
/* 483 */       Double dailyMovementSlope = Double.valueOf(0.004601D);
/* 484 */       Double kylesSlippageSlope = Double.valueOf(0.8510395D);
/* 485 */       Double tickSizeRatioSlope = Double.valueOf(-0.1511881D);
/*     */       
/*     */ 
/* 488 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - 
/* 489 */         this.execData.rolloverContractData.contract.lo.doubleValue()) / this.execData.rolloverContractData.contract.cl.doubleValue();
/* 490 */       double kylesSlippage = this.kylesLambaATR.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 491 */         this.execData.metaData.slippage) / 100.0D;
/* 492 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*     */       
/*     */ 
/* 495 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * kylesSlippage + 
/* 496 */         tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
/*     */       
/* 498 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/* 499 */         slippage = this.execData.metaData.slippage;
/*     */       }
/* 501 */       if (slippage.doubleValue() < 0.0D) {
/* 502 */         slippage = Double.valueOf(0.0D);
/*     */       }
/* 504 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 509 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelSigma))
/*     */     {
/* 511 */       Double dailyMovementSlope = Double.valueOf(0.0062692D);
/* 512 */       Double kylesSlippageSlope = Double.valueOf(0.6521675D);
/* 513 */       Double stdDevJSlope = Double.valueOf(-0.8228327D);
/* 514 */       Double expbyTurnOverSlope = Double.valueOf(0.0785597D);
/* 515 */       Double tickSizeRatioSlope = Double.valueOf(-0.1388063D);
/*     */       
/*     */ 
/* 518 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - 
/* 519 */         this.execData.rolloverContractData.contract.lo.doubleValue()) / this.execData.rolloverContractData.contract.cl.doubleValue();
/* 520 */       double kylesSlippage = this.kylesLambaSigma.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 521 */         this.execData.metaData.slippage) / 100.0D;
/* 522 */       double stdDevJ = this.kylesLambaSigma.getStdDevJs(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
/* 523 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue();
/* 524 */       double expByTurnover = turnover / this.avgDailyTO;
/* 525 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*     */       
/*     */ 
/* 528 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * kylesSlippage + stdDevJSlope.doubleValue() * stdDevJ + 
/* 529 */         expbyTurnOverSlope.doubleValue() * expByTurnover + tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
/*     */       
/* 531 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/* 532 */         slippage = this.execData.metaData.slippage;
/*     */       }
/* 534 */       if (slippage.doubleValue() < 0.0D) {
/* 535 */         slippage = Double.valueOf(0.0D);
/*     */       }
/* 537 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 542 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaSigma))
/*     */     {
/* 544 */       slippage = Double.valueOf(this.kylesLambaSigma.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 545 */         this.execData.metaData.slippage));
/*     */       
/* 547 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */     }
/* 551 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaATR))
/*     */     {
/* 553 */       slippage = Double.valueOf(this.kylesLambaATR.predictSlippage(Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/* 554 */         this.execData.metaData.slippage));
/*     */       
/* 556 */       openSlippage = slippage;
/*     */ 
/*     */ 
/*     */     }
/* 560 */     else if (!this.slippageModel.equals(SlippageModel.ZeroSlippage)) {}
/*     */     
/*     */ 
/*     */ 
/* 564 */     HashMap<String, Double> slippageMap = new HashMap();
/* 565 */     slippageMap.put("Slippage", slippage);
/* 566 */     slippageMap.put("Open Slippage", openSlippage);
/*     */     
/* 568 */     return slippageMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getExecutionPrice(Order order, HashMap<String, Double> slippageMap, Double triggerPrice, String triggerType)
/*     */   {
/* 576 */     Double slippage = (Double)slippageMap.get("Slippage");
/* 577 */     Double openSlippage = (Double)slippageMap.get("Open Slippage");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 582 */     Double lotExposure = Double.valueOf(this.execData.metaData.lotSize.doubleValue() * triggerPrice.doubleValue());
/* 583 */     Double transactionCharges = Double.valueOf((this.execData.metaData.exchangeFees.doubleValue() + this.execData.metaData.brokerage.doubleValue()) * 
/* 584 */       this.execData.rolloverContractData.contract.currency.doubleValue() / lotExposure.doubleValue());
/*     */     
/*     */ 
/* 587 */     if (order.side.equals(OrderSide.BUY))
/*     */     {
/*     */ 
/* 590 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/*     */ 
/* 593 */         if (triggerType.equals("OP")) {
/* 594 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 597 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 601 */       if (order.type.equals(OrderType.LIMIT))
/*     */       {
/*     */ 
/* 604 */         slippage = Double.valueOf(0.0D);
/* 605 */         if (triggerType.equals("OP")) {
/* 606 */           if (this.execData.dataType.contains("D"))
/* 607 */             slippage = openSlippage;
/*     */         } else {
/* 609 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 612 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 616 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/*     */ 
/* 619 */         slippage = Double.valueOf(0.0D);
/* 620 */         if (triggerType.equals("OP")) {
/* 621 */           if (this.execData.dataType.contains("D"))
/* 622 */             slippage = openSlippage;
/*     */         } else {
/* 624 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 627 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 631 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/*     */ 
/* 634 */         if (triggerType.equals("OP")) {
/* 635 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 638 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 642 */       if (order.type.equals(OrderType.MARKET))
/*     */       {
/* 644 */         return triggerPrice;
/*     */       }
/*     */       
/* 647 */       if ((order.type.equals(OrderType.EOD)) || (order.type.equals(OrderType.ROLLOVER)))
/*     */       {
/* 649 */         if (this.execData.dataType.contains("D"))
/* 650 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/* 651 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/* 652 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()) + this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*     */         }
/* 654 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 658 */       if (order.type.equals(OrderType.OPEN))
/*     */       {
/*     */ 
/* 661 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 665 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*     */       {
/*     */ 
/* 668 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 673 */     else if (order.side.equals(OrderSide.SELL))
/*     */     {
/* 675 */       if (order.type.equals(OrderType.STOP))
/*     */       {
/*     */ 
/* 678 */         if (triggerType.equals("OP")) {
/* 679 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 682 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 687 */       if (order.type.equals(OrderType.LIMIT))
/*     */       {
/*     */ 
/* 690 */         slippage = Double.valueOf(0.0D);
/* 691 */         if (triggerType.equals("OP")) {
/* 692 */           if (this.execData.dataType.contains("D"))
/* 693 */             slippage = openSlippage;
/*     */         } else {
/* 695 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 698 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 702 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*     */       {
/*     */ 
/* 705 */         slippage = Double.valueOf(0.0D);
/* 706 */         if (triggerType.equals("OP")) {
/* 707 */           if (this.execData.dataType.contains("D"))
/* 708 */             slippage = openSlippage;
/*     */         } else {
/* 710 */           slippage = Double.valueOf(0.0D);
/*     */         }
/*     */         
/* 713 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 717 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*     */       {
/*     */ 
/* 720 */         if (triggerType.equals("OP")) {
/* 721 */           slippage = openSlippage;
/*     */         }
/*     */         
/* 724 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */       
/*     */ 
/* 728 */       if (order.type.equals(OrderType.MARKET)) {
/* 729 */         return triggerPrice;
/*     */       }
/*     */       
/* 732 */       if ((order.type.equals(OrderType.EOD)) || (order.type.equals(OrderType.ROLLOVER)))
/*     */       {
/* 734 */         if (this.execData.dataType.contains("D"))
/* 735 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/* 736 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/* 737 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()) - this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*     */         }
/* 739 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/*     */       }
/*     */       
/*     */ 
/* 743 */       if (order.type.equals(OrderType.OPEN)) {
/* 744 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()));
/*     */       }
/*     */       
/* 747 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*     */       {
/* 749 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 754 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updatePosition(Double capitalPrice, Double execPrice, Order order)
/*     */   {
/* 762 */     this.exit = false;
/*     */     
/*     */ 
/* 765 */     int signal = -1;
/* 766 */     if (order.side.equals(OrderSide.BUY)) {
/* 767 */       signal = 1;
/*     */     }
/*     */     
/*     */ 
/* 771 */     if ((this.position.equals(Long.valueOf(0L))) && (!order.type.equals(OrderType.ROLLOVER))) {
/* 772 */       this.euroTradeCash = Double.valueOf(0.0D);
/*     */ 
/*     */     }
/* 775 */     else if (this.position.longValue() * signal > 0L) {
/* 776 */       this.usedCapital = Double.valueOf(this.usedCapital.doubleValue() + capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * order.quantity.longValue() / this.execData.metaData.leverage.intValue());
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 781 */       long absPosition = Math.abs(this.position.longValue());
/*     */       
/*     */ 
/* 784 */       if (order.quantity.longValue() > absPosition) {
/* 785 */         this.usedCapital = Double.valueOf(capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * (order.quantity.longValue() - absPosition) / this.execData.metaData.leverage.intValue());
/*     */ 
/*     */ 
/*     */       }
/* 789 */       else if (!order.type.equals(OrderType.ROLLOVER)) {
/* 790 */         this.usedCapital = Double.valueOf(0.0D);
/* 791 */         this.exit = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 796 */     this.position = Long.valueOf(this.position.longValue() + signal * order.quantity.longValue());
/* 797 */     this.euroTradeCash = Double.valueOf(this.euroTradeCash.doubleValue() - signal * execPrice.doubleValue() * order.quantity.longValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTradebook(Order order, Double triggerPrice, String triggerType, Double execPrice)
/*     */     throws IOException
/*     */   {
/* 806 */     Double tradeCumMTM = this.$TradeCumMTM;
/* 807 */     if (!this.exit) {
/* 808 */       tradeCumMTM = Double.valueOf(0.0D);
/*     */     }
/*     */     else
/*     */     {
/* 812 */       double $TradeMTM = this.euroTradeCash.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue();
/*     */       
/*     */ 
/* 815 */       tradeCumMTM = Double.valueOf((this.$TradeCumMTM.doubleValue() + $TradeMTM) / this.capital.doubleValue());
/*     */     }
/* 817 */     String[] trade = { this.execData.dateTime.toString(), this.capital.toString(), this.execData.scripID, 
/* 818 */       order.side.getStringVal(), order.type.getStringVal(), triggerPrice.toString(), execPrice.toString(), 
/* 819 */       order.quantity.toString(), triggerType, this.execData.scripListID, this.execData.prevOrderTimestamp.toString(), 
/* 820 */       tradeCumMTM.toString() };
/* 821 */     this.tradeBook.add(trade);
/* 822 */     this.tbWriter.writeLine(trade);
/*     */     
/*     */ 
/* 825 */     if (order.side == OrderSide.BUY) {
/* 826 */       this.buyTrades.add(execPrice);
/* 827 */     } else if (order.side == OrderSide.SELL) {
/* 828 */       this.sellTrades.add(execPrice);
/*     */     }
/*     */   }
/*     */   
/*     */   public void runExitRoutine() throws Exception
/*     */   {
/* 834 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/* 835 */     Contract rolloverToContract = new Contract();
/* 836 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*     */     
/*     */ 
/* 839 */     if (this.position.longValue() > 0L)
/*     */     {
/*     */ 
/* 842 */       Long orderQty = this.position;
/*     */       
/*     */ 
/* 845 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.EOD, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 848 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/*     */       
/*     */ 
/* 851 */       this.currentContract = rolloverToContract;
/*     */     }
/* 853 */     else if (this.position.longValue() < 0L)
/*     */     {
/*     */ 
/* 856 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*     */       
/*     */ 
/* 859 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.EOD, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 862 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/*     */       
/*     */ 
/* 865 */       this.currentContract = rolloverToContract;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void runRolloverRoutine()
/*     */     throws Exception
/*     */   {
/* 873 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/* 874 */     Contract rolloverToContract = new Contract();
/* 875 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*     */     
/*     */ 
/* 878 */     if (this.position.longValue() > 0L)
/*     */     {
/*     */ 
/* 881 */       Long orderQty = this.position;
/*     */       
/*     */ 
/* 884 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/* 885 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 888 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/* 889 */       executeOrder(rollBuyOrder, rolloverToContract, null);
/*     */       
/*     */ 
/* 892 */       this.currentContract = rolloverToContract;
/*     */     }
/* 894 */     else if (this.position.longValue() < 0L)
/*     */     {
/*     */ 
/* 897 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*     */       
/*     */ 
/* 900 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/* 901 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 0.0D, orderQty.longValue());
/*     */       
/*     */ 
/* 904 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/* 905 */       executeOrder(rollSellOrder, rolloverToContract, null);
/*     */       
/*     */ 
/* 908 */       this.currentContract = rolloverToContract;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void executeOrder(Order order, Contract contract, Contract prevContract)
/*     */     throws Exception
/*     */   {
/* 917 */     HashMap<String, Object> triggerMap = getTriggerDetails(order, contract, prevContract);
/*     */     
/*     */ 
/* 920 */     Double triggerPrice = (Double)triggerMap.get("Trigger Price");
/* 921 */     Double capitalPrice = (Double)triggerMap.get("Capital Price");
/* 922 */     String triggerType = (String)triggerMap.get("Trigger Type");
/*     */     
/*     */ 
/* 925 */     HashMap<String, Double> slippageMap = runSlippageModel(order, triggerPrice);
/*     */     
/*     */ 
/* 928 */     Double executionPrice = getExecutionPrice(order, slippageMap, triggerPrice, triggerType);
/*     */     
/*     */ 
/* 931 */     updatePosition(capitalPrice, executionPrice, order);
/*     */     
/*     */ 
/* 934 */     updateTradebook(order, capitalPrice, triggerType, executionPrice);
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ScripExecution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */