/*      */ package com.q1.bt.execution;
/*      */ 
/*      */ import com.q1.bt.data.classes.Contract;
/*      */ import com.q1.bt.data.classes.ContractData;
/*      */ import com.q1.bt.data.classes.MetaData;
/*      */ import com.q1.bt.execution.order.Order;
/*      */ import com.q1.bt.execution.order.OrderCondition;
/*      */ import com.q1.bt.execution.order.OrderSide;
/*      */ import com.q1.bt.execution.order.OrderType;
/*      */ import com.q1.bt.process.backtest.SlippageModel;
/*      */ import com.q1.csv.CSVWriter;
/*      */ import com.q1.exception.bt.MissingExpiryException;
/*      */ import com.q1.math.MathLib;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ 
/*      */ public class ScripExecution
/*      */ {
/*      */   ExecutionData execData;
/*   21 */   ExecutionData prevExecData = null;
/*      */   
/*      */   Contract currentContract;
/*      */   
/*      */   SlippageModel slippageModel;
/*      */   RolloverMethod rolloverMethod;
/*      */   public ArrayList<Double> buyTrades;
/*      */   public ArrayList<Double> sellTrades;
/*   29 */   public ArrayList<String[]> tradeBook = new ArrayList();
/*      */   
/*      */   CSVWriter tbWriter;
/*      */   
/*   33 */   public Double $CumMTM = Double.valueOf(0.0D); public Double $PrevDayCumMTM = Double.valueOf(0.0D);
/*      */   
/*      */ 
/*   36 */   public Double $TradeMTM = Double.valueOf(0.0D); public Double $TradeCumMTM = Double.valueOf(0.0D);
/*   37 */   public Double $TradePrevDayCumMTM = Double.valueOf(0.0D);
/*      */   
/*      */ 
/*   40 */   public Double euroTradeCash = Double.valueOf(0.0D);
/*   41 */   public Double euroTradeMTM = Double.valueOf(0.0D); public Double euroTradeCumMTM = Double.valueOf(0.0D);
/*   42 */   public Double euroTradePrevCumMTM = Double.valueOf(0.0D); public Double euroTradePrevDayCumMTM = Double.valueOf(0.0D);
/*      */   
/*      */ 
/*   45 */   public Long position = Long.valueOf(0L);
/*   46 */   public Double usedCapital = Double.valueOf(0.0D);
/*      */   
/*      */   public Double capital;
/*      */   
/*      */   public Double exchangeRate;
/*      */   private boolean exit;
/*   52 */   EMA dailyTurnoverEMA = new EMA(250);
/*   53 */   double avgDailyTO = 1.0D;
/*      */   
/*      */ 
/*   56 */   KylesLambdaSigma kylesLambaSigma = new KylesLambdaSigma(Integer.valueOf(20), Integer.valueOf(1250));
/*   57 */   KylesLambdaATR kylesLambaATR = new KylesLambdaATR(Integer.valueOf(20), Integer.valueOf(1250));
/*      */   
/*      */ 
/*      */   public ScripExecution(CSVWriter tbWriter, SlippageModel slippageModel, RolloverMethod rolloverMethod)
/*      */   {
/*   62 */     this.tbWriter = tbWriter;
/*   63 */     this.slippageModel = slippageModel;
/*   64 */     this.rolloverMethod = rolloverMethod;
/*      */   }
/*      */   
/*      */   public void updateWriter(CSVWriter tbWriter)
/*      */   {
/*   69 */     this.tbWriter = tbWriter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void processOrders(ExecutionData execData, ArrayList<Order> orderBook, Double capital)
/*      */     throws Exception
/*      */   {
/*   77 */     this.tradeBook = new ArrayList();
/*      */     
/*      */ 
/*   80 */     this.capital = capital;
/*   81 */     this.usedCapital = Double.valueOf(0.0D);
/*   82 */     this.$TradeMTM = Double.valueOf(0.0D);
/*   83 */     this.exchangeRate = execData.rolloverContractData.contract.currency;
/*      */     
/*      */ 
/*   86 */     this.buyTrades = new ArrayList();
/*   87 */     this.sellTrades = new ArrayList();
/*      */     
/*      */ 
/*   90 */     this.prevExecData = this.execData;
/*   91 */     this.execData = new ExecutionData(execData);
/*      */     
/*      */ 
/*   94 */     Contract contract = execData.mainContractData.contract;
/*      */     Contract prevContract;
/*   96 */     Contract prevContract; if (this.prevExecData == null) {
/*   97 */       prevContract = null;
/*      */     } else {
/*   99 */       prevContract = this.prevExecData.mainContractData.contract;
/*      */     }
/*  101 */     this.currentContract = contract;
/*      */     
/*      */ 
/*  104 */     if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
/*  105 */       (execData.rolloverContractData != null) && 
/*  106 */       (!this.prevExecData.date.equals(execData.date)))
/*      */     {
/*      */ 
/*  109 */       double dayTurnover = execData.rolloverContractData.contract.cl.doubleValue() * 
/*  110 */         execData.rolloverContractData.contract.vol.doubleValue() * 
/*  111 */         execData.metaData.lotSize.doubleValue() * 
/*  112 */         execData.metaData.lotFactor.doubleValue();
/*  113 */       if (dayTurnover > 0.0D) {
/*  114 */         this.avgDailyTO = this.dailyTurnoverEMA.calculateEMA(dayTurnover).doubleValue();
/*      */       }
/*      */       
/*  117 */       this.kylesLambaSigma.updateETA(
/*  118 */         execData.rolloverContractData.contract.cl, 
/*  119 */         execData.rolloverContractData.contract.vol, 
/*  120 */         execData.rolloverContractData.contract.rolloverCl);
/*  121 */       this.kylesLambaATR.updateETA(
/*  122 */         execData.rolloverContractData.contract.hi, 
/*  123 */         execData.rolloverContractData.contract.lo, 
/*  124 */         execData.rolloverContractData.contract.cl, 
/*  125 */         execData.rolloverContractData.contract.vol, 
/*  126 */         execData.rolloverContractData.contract.rolloverCl);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */     for (Order order : orderBook)
/*      */     {
/*      */ 
/*  136 */       if (checkOrderConditions(order))
/*      */       {
/*      */ 
/*      */ 
/*  140 */         boolean orderFill = checkOrderFill(order, contract);
/*      */         
/*      */ 
/*  143 */         if (orderFill) {
/*  144 */           executeOrder(order, contract, prevContract);
/*      */         }
/*      */       }
/*      */     }
/*  148 */     if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
/*  149 */       (execData.rolloverContractData != null) && 
/*  150 */       (!this.prevExecData.date.equals(execData.date)))
/*      */     {
/*      */ 
/*  153 */       double rolloverCl = execData.rolloverContractData.contract.rolloverCl.doubleValue();
/*  154 */       if (rolloverCl > 0.0D)
/*      */       {
/*  156 */         if (this.rolloverMethod.equals(RolloverMethod.CloseToClose)) {
/*  157 */           runRolloverRoutine();
/*      */         }
/*  159 */         else if (this.rolloverMethod.equals(RolloverMethod.ExitAtClose)) {
/*  160 */           runExitRoutine();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */     this.euroTradeCumMTM = Double.valueOf(this.euroTradeCash.doubleValue() + this.position.longValue() * this.currentContract.cl.doubleValue());
/*      */     
/*      */ 
/*  172 */     this.euroTradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() - this.euroTradePrevCumMTM.doubleValue());
/*  173 */     this.euroTradePrevCumMTM = this.euroTradeCumMTM;
/*      */     
/*      */ 
/*      */ 
/*  177 */     this.$TradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue());
/*      */     
/*      */ 
/*  180 */     this.$TradeCumMTM = Double.valueOf(this.$TradeCumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*      */     
/*      */ 
/*  183 */     this.$CumMTM = Double.valueOf(this.$CumMTM.doubleValue() + this.$TradeMTM.doubleValue());
/*      */     
/*      */ 
/*  186 */     if (this.exit)
/*      */     {
/*  188 */       this.euroTradeCash = (this.euroTradePrevCumMTM = this.$TradeCumMTM = Double.valueOf(0.0D));
/*  189 */       this.exit = false;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Long getQuantity(Double price, Double capital, Double lotSize, Double leverage)
/*      */   {
/*  197 */     return Long.valueOf(MathLib.roundTick(capital.doubleValue() * leverage.doubleValue() / price.doubleValue(), lotSize.doubleValue()));
/*      */   }
/*      */   
/*      */   public boolean checkOrderConditions(Order order)
/*      */     throws MissingExpiryException
/*      */   {
/*  203 */     boolean conditionVal = true;
/*  204 */     for (OrderCondition orderCondition : order.conditions) {
/*  205 */       conditionVal = (conditionVal) && 
/*  206 */         (orderCondition.checkCondition(this.execData));
/*  207 */       if (!conditionVal)
/*  208 */         return conditionVal;
/*      */     }
/*  210 */     return conditionVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean checkOrderFill(Order order, Contract contract)
/*      */   {
/*  217 */     if (order.side.equals(OrderSide.BUY))
/*      */     {
/*      */ 
/*  220 */       if (order.type.equals(OrderType.STOP)) {
/*  221 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/*  222 */           return true;
/*      */         }
/*      */       }
/*  225 */       else if (order.type.equals(OrderType.LIMIT)) {
/*  226 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() < 0) {
/*  227 */           return true;
/*      */         }
/*      */       }
/*  230 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/*  231 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/*  232 */           return true;
/*      */         }
/*      */       }
/*  235 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/*  236 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/*  237 */           return true;
/*      */         }
/*      */       }
/*      */       else {
/*  241 */         return true;
/*      */       }
/*      */     }
/*  244 */     else if (order.side.equals(OrderSide.SELL))
/*      */     {
/*      */ 
/*  247 */       if (order.type.equals(OrderType.STOP)) {
/*  248 */         if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
/*  249 */           return true;
/*      */         }
/*      */       }
/*  252 */       else if (order.type.equals(OrderType.LIMIT)) {
/*  253 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() > 0) {
/*  254 */           return true;
/*      */         }
/*      */       }
/*  257 */       else if (order.type.equals(OrderType.LIMITATPRICE)) {
/*  258 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/*  259 */           return true;
/*      */         }
/*      */       }
/*  262 */       else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
/*  263 */         if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
/*  264 */           return true;
/*      */         }
/*      */       }
/*      */       else
/*  268 */         return true;
/*      */     }
/*  270 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HashMap<String, Object> getTriggerDetails(Order order, Contract contract, Contract prevContract)
/*      */   {
/*  278 */     String triggerType = "NORM";
/*  279 */     Double triggerPrice = Double.valueOf(0.0D);
/*  280 */     HashMap<String, Object> triggerMap = new HashMap();
/*  281 */     triggerMap.put("Trigger Price", triggerPrice);
/*  282 */     triggerMap.put("Capital Price", order.price);
/*  283 */     triggerMap.put("Trigger Type", triggerType);
/*      */     
/*      */ 
/*  286 */     Double prevClose = null;
/*  287 */     if (prevContract != null) {
/*  288 */       prevClose = prevContract.cl;
/*      */     } else {
/*  290 */       prevClose = contract.cl;
/*      */     }
/*      */     
/*  293 */     if (order.type.equals(OrderType.EOD)) {
/*  294 */       triggerPrice = contract.cl;
/*  295 */       triggerMap.put("Trigger Price", triggerPrice);
/*  296 */       triggerMap.put("Capital Price", prevClose);
/*  297 */       return triggerMap;
/*      */     }
/*      */     
/*  300 */     if (order.type.equals(OrderType.ROLLOVER)) {
/*  301 */       triggerPrice = contract.cl;
/*  302 */       triggerMap.put("Trigger Price", triggerPrice);
/*  303 */       triggerMap.put("Capital Price", Double.valueOf(0.0D));
/*  304 */       return triggerMap;
/*      */     }
/*      */     
/*  307 */     if ((order.type.equals(OrderType.OPEN)) || 
/*  308 */       (order.type.equals(OrderType.OPENWITHSLIP))) {
/*  309 */       triggerPrice = contract.op;
/*  310 */       triggerMap.put("Trigger Price", triggerPrice);
/*  311 */       triggerMap.put("Capital Price", prevClose);
/*  312 */       return triggerMap;
/*      */     }
/*      */     
/*      */ 
/*  316 */     triggerPrice = order.price;
/*      */     
/*      */ 
/*  319 */     if (order.side.equals(OrderSide.BUY))
/*      */     {
/*  321 */       if (order.type.equals(OrderType.STOP))
/*      */       {
/*  323 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0) {
/*  324 */           triggerPrice = contract.op;
/*  325 */           triggerType = "OP";
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  330 */       else if (order.type.equals(OrderType.LIMIT))
/*      */       {
/*  332 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0)
/*      */         {
/*  334 */           if (this.execData.dataType.contains("D")) {
/*  335 */             triggerPrice = contract.op;
/*  336 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  339 */           else if ((prevClose == null) || 
/*  340 */             (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/*  341 */             triggerPrice = contract.op;
/*  342 */             triggerType = "OP";
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*  348 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*      */       {
/*  350 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*      */         {
/*  352 */           if (this.execData.dataType.contains("D")) {
/*  353 */             triggerPrice = contract.op;
/*  354 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  357 */           else if ((prevClose == null) || 
/*  358 */             (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/*  359 */             triggerPrice = contract.op;
/*  360 */             triggerType = "OP";
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*  366 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*      */       {
/*  368 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
/*      */         {
/*  370 */           if (this.execData.dataType.contains("D")) {
/*  371 */             triggerPrice = contract.op;
/*  372 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  375 */           else if ((prevClose == null) || 
/*  376 */             (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
/*  377 */             triggerPrice = contract.op;
/*  378 */             triggerType = "OP";
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */       }
/*  384 */       else if (order.type.equals(OrderType.MARKET)) {
/*  385 */         triggerPrice = contract.hi;
/*  386 */         triggerMap.put("Capital Price", prevClose);
/*      */       }
/*      */       
/*      */     }
/*  390 */     else if (order.side.equals(OrderSide.SELL))
/*      */     {
/*  392 */       if (order.type.equals(OrderType.STOP))
/*      */       {
/*  394 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0) {
/*  395 */           triggerPrice = contract.op;
/*  396 */           triggerType = "OP";
/*      */         }
/*      */         
/*      */ 
/*      */       }
/*  401 */       else if (order.type.equals(OrderType.LIMIT))
/*      */       {
/*  403 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*      */         {
/*  405 */           if (this.execData.dataType.contains("D")) {
/*  406 */             triggerPrice = contract.op;
/*  407 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  410 */           else if ((prevClose == null) || 
/*  411 */             (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/*  412 */             triggerPrice = contract.op;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  417 */       else if (order.type.equals(OrderType.LIMITATPRICE))
/*      */       {
/*  419 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*      */         {
/*  421 */           if (this.execData.dataType.contains("D")) {
/*  422 */             triggerPrice = contract.op;
/*  423 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  426 */           else if ((prevClose == null) || 
/*  427 */             (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/*  428 */             triggerPrice = contract.op;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  433 */       else if (order.type.equals(OrderType.LIMITWITHSLIP))
/*      */       {
/*  435 */         if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
/*      */         {
/*  437 */           if (this.execData.dataType.contains("D")) {
/*  438 */             triggerPrice = contract.op;
/*  439 */             triggerType = "OP";
/*      */ 
/*      */           }
/*  442 */           else if ((prevClose == null) || 
/*  443 */             (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
/*  444 */             triggerPrice = contract.op;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  449 */       else if (order.type.equals(OrderType.MARKET)) {
/*  450 */         triggerPrice = contract.lo;
/*  451 */         triggerMap.put("Capital Price", prevClose);
/*      */       }
/*      */     }
/*  454 */     triggerMap.put("Trigger Price", triggerPrice);
/*  455 */     triggerMap.put("Trigger Type", triggerType);
/*  456 */     return triggerMap;
/*      */   }
/*      */   
/*      */ 
/*      */   public HashMap<String, Double> runSlippageModel(Order order, Double triggerPrice)
/*      */     throws Exception
/*      */   {
/*  463 */     Double slippage = Double.valueOf(0.0D);
/*  464 */     Double openSlippage = Double.valueOf(0.0D);
/*      */     
/*      */ 
/*  467 */     if (this.slippageModel.equals(SlippageModel.ConstantSlippage)) {
/*  468 */       slippage = this.execData.metaData.slippage;
/*  469 */       openSlippage = this.execData.metaData.openSlippage;
/*      */ 
/*      */ 
/*      */     }
/*  473 */     else if (this.slippageModel.equals(SlippageModel.TickSlippage)) {
/*  474 */       slippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/*  475 */       openSlippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
/*      */ 
/*      */ 
/*      */     }
/*  479 */     else if ((this.slippageModel.equals(SlippageModel.LinearModel)) || 
/*  480 */       (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)))
/*      */     {
/*  482 */       Double slippageSlope = this.execData.metaData.slippageSlope;
/*      */       
/*  484 */       if (slippageSlope == null) {
/*  485 */         throw new Exception(
/*  486 */           "Slippage Slope not available. Can't use Linear Model!!");
/*      */       }
/*  488 */       if (this.execData.dataType.contains("D")) {
/*  489 */         slippageSlope = Double.valueOf(2.0D * this.execData.metaData.slippageSlope.doubleValue());
/*      */       }
/*      */       
/*      */ 
/*  493 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue() / 1000000.0D;
/*  494 */       Double slippageIntercept = this.execData.metaData.slippageIntercept;
/*  495 */       if (slippageIntercept == null)
/*  496 */         throw new Exception(
/*  497 */           "Slippage Intercept not available. Can't use Linear Model!!");
/*  498 */       slippage = Double.valueOf(slippageIntercept.doubleValue() + turnover * slippageSlope.doubleValue());
/*      */       
/*  500 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  505 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModel))
/*      */     {
/*  507 */       Double atrRootPhiSlope = Double.valueOf(0.53D);
/*      */       
/*      */ 
/*  510 */       double atrRootPhi = this.kylesLambaATR.getATRRootPhi(
/*  511 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
/*      */       
/*      */ 
/*  514 */       slippage = Double.valueOf(atrRootPhiSlope.doubleValue() * atrRootPhi * 100.0D);
/*      */       
/*  516 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/*  517 */         slippage = this.execData.metaData.slippage;
/*      */       }
/*  519 */       if (slippage.doubleValue() < 0.0D) {
/*  520 */         slippage = Double.valueOf(0.0D);
/*      */       }
/*  522 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  527 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelATR))
/*      */     {
/*  529 */       Double dailyMovementSlope = Double.valueOf(0.004601D);
/*  530 */       Double kylesSlippageSlope = Double.valueOf(0.8510395D);
/*  531 */       Double tickSizeRatioSlope = Double.valueOf(-0.1511881D);
/*      */       
/*      */ 
/*  534 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - this.execData.rolloverContractData.contract.lo.doubleValue()) / 
/*  535 */         this.execData.rolloverContractData.contract.cl.doubleValue();
/*  536 */       double kylesSlippage = this.kylesLambaATR.predictSlippage(
/*  537 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage) / 100.0D;
/*  538 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*      */       
/*      */ 
/*  541 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * 
/*  542 */         kylesSlippage + tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
/*      */       
/*  544 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/*  545 */         slippage = this.execData.metaData.slippage;
/*      */       }
/*  547 */       if (slippage.doubleValue() < 0.0D) {
/*  548 */         slippage = Double.valueOf(0.0D);
/*      */       }
/*  550 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  555 */     else if (this.slippageModel.equals(SlippageModel.AdaptiveModelSigma))
/*      */     {
/*  557 */       Double dailyMovementSlope = Double.valueOf(0.0062692D);
/*  558 */       Double kylesSlippageSlope = Double.valueOf(0.6521675D);
/*  559 */       Double stdDevJSlope = Double.valueOf(-0.8228327D);
/*  560 */       Double expbyTurnOverSlope = Double.valueOf(0.0785597D);
/*  561 */       Double tickSizeRatioSlope = Double.valueOf(-0.1388063D);
/*      */       
/*      */ 
/*  564 */       double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - this.execData.rolloverContractData.contract.lo.doubleValue()) / 
/*  565 */         this.execData.rolloverContractData.contract.cl.doubleValue();
/*  566 */       double kylesSlippage = this.kylesLambaSigma.predictSlippage(
/*  567 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
/*  568 */         this.execData.metaData.slippage) / 100.0D;
/*  569 */       double stdDevJ = this.kylesLambaSigma.getStdDevJs(
/*  570 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
/*  571 */       double turnover = triggerPrice.doubleValue() * order.quantity.longValue();
/*  572 */       double expByTurnover = turnover / this.avgDailyTO;
/*  573 */       double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
/*      */       
/*      */ 
/*  576 */       slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * 
/*  577 */         kylesSlippage + stdDevJSlope.doubleValue() * stdDevJ + 
/*  578 */         expbyTurnOverSlope.doubleValue() * expByTurnover + tickSizeRatioSlope.doubleValue() * 
/*  579 */         tickSizeRatio) * 100.0D);
/*      */       
/*  581 */       if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
/*  582 */         slippage = this.execData.metaData.slippage;
/*      */       }
/*  584 */       if (slippage.doubleValue() < 0.0D) {
/*  585 */         slippage = Double.valueOf(0.0D);
/*      */       }
/*  587 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  592 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaSigma))
/*      */     {
/*  594 */       slippage = Double.valueOf(this.kylesLambaSigma.predictSlippage(
/*  595 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage));
/*      */       
/*  597 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */     }
/*  601 */     else if (this.slippageModel.equals(SlippageModel.KylesLambdaATR))
/*      */     {
/*  603 */       slippage = Double.valueOf(this.kylesLambaATR.predictSlippage(
/*  604 */         Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage));
/*      */       
/*  606 */       openSlippage = slippage;
/*      */ 
/*      */ 
/*      */     }
/*  610 */     else if (!this.slippageModel.equals(SlippageModel.ZeroSlippage)) {}
/*      */     
/*      */ 
/*      */ 
/*  614 */     HashMap<String, Double> slippageMap = new HashMap();
/*  615 */     slippageMap.put("Slippage", slippage);
/*  616 */     slippageMap.put("Open Slippage", openSlippage);
/*      */     
/*  618 */     return slippageMap;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Double getExecutionPrice(Order order, HashMap<String, Double> slippageMap, Double triggerPrice, String triggerType)
/*      */   {
/*  627 */     Double slippage = (Double)slippageMap.get("Slippage");
/*  628 */     Double openSlippage = (Double)slippageMap.get("Open Slippage");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  633 */     Double lotExposure = Double.valueOf(this.execData.metaData.lotSize.doubleValue() * triggerPrice.doubleValue());
/*  634 */     Double transactionCharges = Double.valueOf((this.execData.metaData.exchangeFees.doubleValue() + this.execData.metaData.brokerage.doubleValue()) * this.execData.rolloverContractData.contract.currency.doubleValue() / 
/*  635 */       lotExposure.doubleValue());
/*      */     
/*      */ 
/*  638 */     if (order.side.equals(OrderSide.BUY))
/*      */     {
/*      */ 
/*  641 */       if (order.type.equals(OrderType.STOP))
/*      */       {
/*      */ 
/*  644 */         if (triggerType.equals("OP")) {
/*  645 */           slippage = openSlippage;
/*      */         }
/*      */         
/*  648 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  649 */           1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  653 */       if (order.type.equals(OrderType.LIMIT))
/*      */       {
/*      */ 
/*  656 */         slippage = Double.valueOf(0.0D);
/*  657 */         if (triggerType.equals("OP")) {
/*  658 */           if (this.execData.dataType.contains("D"))
/*  659 */             slippage = openSlippage;
/*      */         } else {
/*  661 */           slippage = Double.valueOf(0.0D);
/*      */         }
/*      */         
/*  664 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  665 */           1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  669 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*      */       {
/*      */ 
/*  672 */         slippage = Double.valueOf(0.0D);
/*  673 */         if (triggerType.equals("OP")) {
/*  674 */           if (this.execData.dataType.contains("D"))
/*  675 */             slippage = openSlippage;
/*      */         } else {
/*  677 */           slippage = Double.valueOf(0.0D);
/*      */         }
/*      */         
/*  680 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  681 */           1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  685 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*      */       {
/*      */ 
/*  688 */         if (triggerType.equals("OP")) {
/*  689 */           slippage = openSlippage;
/*      */         }
/*      */         
/*  692 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  693 */           1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  697 */       if (order.type.equals(OrderType.MARKET))
/*      */       {
/*  699 */         return triggerPrice;
/*      */       }
/*      */       
/*  702 */       if ((order.type.equals(OrderType.EOD)) || 
/*  703 */         (order.type.equals(OrderType.ROLLOVER)))
/*      */       {
/*  705 */         if (this.execData.dataType.contains("D")) {
/*  706 */           return Double.valueOf(triggerPrice.doubleValue() * (
/*  707 */             1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/*      */         }
/*  709 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/*  710 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()) + 
/*  711 */             this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*      */         }
/*  713 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  714 */           1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
/*      */       }
/*      */       
/*      */ 
/*  718 */       if (order.type.equals(OrderType.OPEN))
/*      */       {
/*      */ 
/*  721 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()));
/*      */       }
/*      */       
/*      */ 
/*  725 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*      */       {
/*      */ 
/*  728 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  729 */           1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*      */     }
/*  734 */     else if (order.side.equals(OrderSide.SELL))
/*      */     {
/*  736 */       if (order.type.equals(OrderType.STOP))
/*      */       {
/*      */ 
/*  739 */         if (triggerType.equals("OP")) {
/*  740 */           slippage = openSlippage;
/*      */         }
/*      */         
/*  743 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  744 */           1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  749 */       if (order.type.equals(OrderType.LIMIT))
/*      */       {
/*      */ 
/*  752 */         slippage = Double.valueOf(0.0D);
/*  753 */         if (triggerType.equals("OP")) {
/*  754 */           if (this.execData.dataType.contains("D"))
/*  755 */             slippage = openSlippage;
/*      */         } else {
/*  757 */           slippage = Double.valueOf(0.0D);
/*      */         }
/*      */         
/*  760 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  761 */           1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  765 */       if (order.type.equals(OrderType.LIMITATPRICE))
/*      */       {
/*      */ 
/*  768 */         slippage = Double.valueOf(0.0D);
/*  769 */         if (triggerType.equals("OP")) {
/*  770 */           if (this.execData.dataType.contains("D"))
/*  771 */             slippage = openSlippage;
/*      */         } else {
/*  773 */           slippage = Double.valueOf(0.0D);
/*      */         }
/*      */         
/*  776 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  777 */           1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  781 */       if (order.type.equals(OrderType.LIMITWITHSLIP))
/*      */       {
/*      */ 
/*  784 */         if (triggerType.equals("OP")) {
/*  785 */           slippage = openSlippage;
/*      */         }
/*      */         
/*  788 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  789 */           1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */       
/*      */ 
/*  793 */       if (order.type.equals(OrderType.MARKET)) {
/*  794 */         return triggerPrice;
/*      */       }
/*      */       
/*  797 */       if ((order.type.equals(OrderType.EOD)) || 
/*  798 */         (order.type.equals(OrderType.ROLLOVER)))
/*      */       {
/*  800 */         if (this.execData.dataType.contains("D")) {
/*  801 */           return Double.valueOf(triggerPrice.doubleValue() * (
/*  802 */             1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/*      */         }
/*  804 */         if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
/*  805 */           return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()) - 
/*  806 */             this.execData.metaData.tickSize.doubleValue() / 2.0D);
/*      */         }
/*  808 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  809 */           1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
/*      */       }
/*      */       
/*      */ 
/*  813 */       if (order.type.equals(OrderType.OPEN)) {
/*  814 */         return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()));
/*      */       }
/*      */       
/*  817 */       if (order.type.equals(OrderType.OPENWITHSLIP))
/*      */       {
/*  819 */         return Double.valueOf(triggerPrice.doubleValue() * (
/*  820 */           1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  825 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updatePosition(Double capitalPrice, Double execPrice, Order order)
/*      */   {
/*  834 */     this.exit = false;
/*      */     
/*      */ 
/*  837 */     int signal = -1;
/*  838 */     if (order.side.equals(OrderSide.BUY)) {
/*  839 */       signal = 1;
/*      */     }
/*      */     
/*      */ 
/*  843 */     if ((this.position.equals(Long.valueOf(0L))) && (!order.type.equals(OrderType.ROLLOVER))) {
/*  844 */       this.euroTradeCash = Double.valueOf(0.0D);
/*      */ 
/*      */     }
/*  847 */     else if (this.position.longValue() * signal > 0L) {
/*  848 */       this.usedCapital = 
/*  849 */         Double.valueOf(this.usedCapital.doubleValue() + capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * order.quantity.longValue() / this.execData.metaData.leverage.intValue());
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  854 */       long absPosition = Math.abs(this.position.longValue());
/*      */       
/*      */ 
/*  857 */       if (order.quantity.longValue() > absPosition) {
/*  858 */         this.usedCapital = 
/*      */         
/*  860 */           Double.valueOf(capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * (order.quantity.longValue() - absPosition) / this.execData.metaData.leverage.intValue());
/*      */ 
/*      */ 
/*      */       }
/*  864 */       else if (!order.type.equals(OrderType.ROLLOVER)) {
/*  865 */         this.usedCapital = Double.valueOf(0.0D);
/*  866 */         this.exit = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  871 */     this.position = Long.valueOf(this.position.longValue() + signal * order.quantity.longValue());
/*  872 */     this.euroTradeCash = Double.valueOf(this.euroTradeCash.doubleValue() - signal * execPrice.doubleValue() * order.quantity.longValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateTradebook(Order order, Double triggerPrice, String triggerType, Double execPrice)
/*      */     throws IOException
/*      */   {
/*  881 */     Double tradeCumMTM = this.$TradeCumMTM;
/*  882 */     if (!this.exit) {
/*  883 */       tradeCumMTM = Double.valueOf(0.0D);
/*      */     }
/*      */     else
/*      */     {
/*  887 */       double $TradeMTM = this.euroTradeCash.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue();
/*      */       
/*      */ 
/*  890 */       tradeCumMTM = Double.valueOf((this.$TradeCumMTM.doubleValue() + $TradeMTM) / this.capital.doubleValue());
/*      */     }
/*  892 */     String[] trade = { this.execData.dateTime.toString(), this.capital.toString(), 
/*  893 */       this.execData.scripID, order.side.getStringVal(), 
/*  894 */       order.type.getStringVal(), triggerPrice.toString(), 
/*  895 */       execPrice.toString(), order.quantity.toString(), triggerType, 
/*  896 */       this.execData.scripListID, this.execData.prevOrderTimestamp.toString(), 
/*  897 */       tradeCumMTM.toString() };
/*  898 */     this.tradeBook.add(trade);
/*  899 */     this.tbWriter.writeLine(trade);
/*      */     
/*      */ 
/*  902 */     if (order.side == OrderSide.BUY) {
/*  903 */       this.buyTrades.add(execPrice);
/*  904 */     } else if (order.side == OrderSide.SELL) {
/*  905 */       this.sellTrades.add(execPrice);
/*      */     }
/*      */   }
/*      */   
/*      */   public void runExitRoutine() throws Exception
/*      */   {
/*  911 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/*  912 */     Contract rolloverToContract = new Contract();
/*  913 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*      */     
/*      */ 
/*  916 */     if (this.position.longValue() > 0L)
/*      */     {
/*      */ 
/*  919 */       Long orderQty = this.position;
/*      */       
/*      */ 
/*  922 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.EOD, 0.0D, 
/*  923 */         orderQty.longValue());
/*      */       
/*      */ 
/*  926 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/*      */       
/*      */ 
/*  929 */       this.currentContract = rolloverToContract;
/*      */     }
/*  931 */     else if (this.position.longValue() < 0L)
/*      */     {
/*      */ 
/*  934 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*      */       
/*      */ 
/*  937 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.EOD, 0.0D, 
/*  938 */         orderQty.longValue());
/*      */       
/*      */ 
/*  941 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/*      */       
/*      */ 
/*  944 */       this.currentContract = rolloverToContract;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void runRolloverRoutine()
/*      */     throws Exception
/*      */   {
/*  952 */     Contract rolloverFromContract = this.execData.rolloverContractData.contract;
/*  953 */     Contract rolloverToContract = new Contract();
/*  954 */     rolloverToContract.cl = rolloverFromContract.rolloverCl;
/*      */     
/*      */ 
/*  957 */     if (this.position.longValue() > 0L)
/*      */     {
/*      */ 
/*  960 */       Long orderQty = this.position;
/*      */       
/*      */ 
/*  963 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 
/*  964 */         0.0D, orderQty.longValue());
/*  965 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 
/*  966 */         0.0D, orderQty.longValue());
/*      */       
/*      */ 
/*  969 */       executeOrder(rollSellOrder, rolloverFromContract, null);
/*  970 */       executeOrder(rollBuyOrder, rolloverToContract, null);
/*      */       
/*      */ 
/*  973 */       this.currentContract = rolloverToContract;
/*      */     }
/*  975 */     else if (this.position.longValue() < 0L)
/*      */     {
/*      */ 
/*  978 */       Long orderQty = Long.valueOf(-this.position.longValue());
/*      */       
/*      */ 
/*  981 */       Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 
/*  982 */         0.0D, orderQty.longValue());
/*  983 */       Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 
/*  984 */         0.0D, orderQty.longValue());
/*      */       
/*      */ 
/*  987 */       executeOrder(rollBuyOrder, rolloverFromContract, null);
/*  988 */       executeOrder(rollSellOrder, rolloverToContract, null);
/*      */       
/*      */ 
/*  991 */       this.currentContract = rolloverToContract;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void executeOrder(Order order, Contract contract, Contract prevContract)
/*      */     throws Exception
/*      */   {
/* 1001 */     HashMap<String, Object> triggerMap = getTriggerDetails(order, contract, 
/* 1002 */       prevContract);
/*      */     
/*      */ 
/* 1005 */     Double triggerPrice = (Double)triggerMap.get("Trigger Price");
/* 1006 */     Double capitalPrice = (Double)triggerMap.get("Capital Price");
/* 1007 */     String triggerType = (String)triggerMap.get("Trigger Type");
/*      */     
/*      */ 
/* 1010 */     HashMap<String, Double> slippageMap = runSlippageModel(order, 
/* 1011 */       triggerPrice);
/*      */     
/*      */ 
/* 1014 */     Double executionPrice = getExecutionPrice(order, slippageMap, 
/* 1015 */       triggerPrice, triggerType);
/*      */     
/*      */ 
/* 1018 */     updatePosition(capitalPrice, executionPrice, order);
/*      */     
/*      */ 
/* 1021 */     updateTradebook(order, capitalPrice, triggerType, executionPrice);
/*      */   }
/*      */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ScripExecution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */