package com.q1.bt.execution;

import com.q1.bt.data.classes.Contract;
import com.q1.bt.data.classes.ContractData;
import com.q1.bt.data.classes.MetaData;
import com.q1.bt.execution.order.Order;
import com.q1.bt.execution.order.OrderCondition;
import com.q1.bt.execution.order.OrderSide;
import com.q1.bt.execution.order.OrderType;
import com.q1.bt.process.backtest.SlippageModel;
import com.q1.csv.CSVWriter;
import com.q1.exception.bt.MissingExpiryException;
import com.q1.math.MathLib;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ScripExecution
{
  ExecutionData execData;
  ExecutionData prevExecData = null;
  
  Contract currentContract;
  
  SlippageModel slippageModel;
  RolloverMethod rolloverMethod;
  public ArrayList<Double> buyTrades;
  public ArrayList<Double> sellTrades;
  public ArrayList<String[]> tradeBook = new ArrayList();
  
  CSVWriter tbWriter;
  
  public Double $CumMTM = Double.valueOf(0.0D); public Double $PrevDayCumMTM = Double.valueOf(0.0D);
  

  public Double $TradeMTM = Double.valueOf(0.0D); public Double $TradeCumMTM = Double.valueOf(0.0D);
  public Double $TradePrevDayCumMTM = Double.valueOf(0.0D);
  

  public Double euroTradeCash = Double.valueOf(0.0D);
  public Double euroTradeMTM = Double.valueOf(0.0D); public Double euroTradeCumMTM = Double.valueOf(0.0D);
  public Double euroTradePrevCumMTM = Double.valueOf(0.0D); public Double euroTradePrevDayCumMTM = Double.valueOf(0.0D);
  

  public Long position = Long.valueOf(0L);
  public Double usedCapital = Double.valueOf(0.0D);
  
  public Double capital;
  
  public Double exchangeRate;
  private boolean exit;
  EMA dailyTurnoverEMA = new EMA(250);
  double avgDailyTO = 1.0D;
  

  KylesLambdaSigma kylesLambaSigma = new KylesLambdaSigma(Integer.valueOf(20), Integer.valueOf(1250));
  KylesLambdaATR kylesLambaATR = new KylesLambdaATR(Integer.valueOf(20), Integer.valueOf(1250));
  

  public ScripExecution(CSVWriter tbWriter, SlippageModel slippageModel, RolloverMethod rolloverMethod)
  {
    this.tbWriter = tbWriter;
    this.slippageModel = slippageModel;
    this.rolloverMethod = rolloverMethod;
  }
  
  public void updateWriter(CSVWriter tbWriter)
  {
    this.tbWriter = tbWriter;
  }
  


  public void processOrders(ExecutionData execData, ArrayList<Order> orderBook, Double capital)
    throws Exception
  {
    this.tradeBook = new ArrayList();
    

    this.capital = capital;
    this.usedCapital = Double.valueOf(0.0D);
    this.$TradeMTM = Double.valueOf(0.0D);
    this.exchangeRate = execData.rolloverContractData.contract.currency;
    

    this.buyTrades = new ArrayList();
    this.sellTrades = new ArrayList();
    

    this.prevExecData = this.execData;
    this.execData = new ExecutionData(execData);
    

    Contract contract = execData.mainContractData.contract;
    Contract prevContract;
    Contract prevContract; if (this.prevExecData == null) {
      prevContract = null;
    } else {
      prevContract = this.prevExecData.mainContractData.contract;
    }
    this.currentContract = contract;
    

    if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
      (execData.rolloverContractData != null) && 
      (!this.prevExecData.date.equals(execData.date)))
    {

      double dayTurnover = execData.rolloverContractData.contract.cl.doubleValue() * 
        execData.rolloverContractData.contract.vol.doubleValue() * 
        execData.metaData.lotSize.doubleValue() * 
        execData.metaData.lotFactor.doubleValue();
      if (dayTurnover > 0.0D) {
        this.avgDailyTO = this.dailyTurnoverEMA.calculateEMA(dayTurnover).doubleValue();
      }
      
      this.kylesLambaSigma.updateETA(
        execData.rolloverContractData.contract.cl, 
        execData.rolloverContractData.contract.vol, 
        execData.rolloverContractData.contract.rolloverCl);
      this.kylesLambaATR.updateETA(
        execData.rolloverContractData.contract.hi, 
        execData.rolloverContractData.contract.lo, 
        execData.rolloverContractData.contract.cl, 
        execData.rolloverContractData.contract.vol, 
        execData.rolloverContractData.contract.rolloverCl);
    }
    




    for (Order order : orderBook)
    {

      if (checkOrderConditions(order))
      {


        boolean orderFill = checkOrderFill(order, contract);
        

        if (orderFill) {
          executeOrder(order, contract, prevContract);
        }
      }
    }
    if ((this.prevExecData != null) && (this.prevExecData.rolloverContractData != null) && 
      (execData.rolloverContractData != null) && 
      (!this.prevExecData.date.equals(execData.date)))
    {

      double rolloverCl = execData.rolloverContractData.contract.rolloverCl.doubleValue();
      if (rolloverCl > 0.0D)
      {
        if (this.rolloverMethod.equals(RolloverMethod.CloseToClose)) {
          runRolloverRoutine();
        }
        else if (this.rolloverMethod.equals(RolloverMethod.ExitAtClose)) {
          runExitRoutine();
        }
      }
    }
    




    this.euroTradeCumMTM = Double.valueOf(this.euroTradeCash.doubleValue() + this.position.longValue() * this.currentContract.cl.doubleValue());
    

    this.euroTradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() - this.euroTradePrevCumMTM.doubleValue());
    this.euroTradePrevCumMTM = this.euroTradeCumMTM;
    


    this.$TradeMTM = Double.valueOf(this.euroTradeCumMTM.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue());
    

    this.$TradeCumMTM = Double.valueOf(this.$TradeCumMTM.doubleValue() + this.$TradeMTM.doubleValue());
    

    this.$CumMTM = Double.valueOf(this.$CumMTM.doubleValue() + this.$TradeMTM.doubleValue());
    

    if (this.exit)
    {
      this.euroTradeCash = (this.euroTradePrevCumMTM = this.$TradeCumMTM = Double.valueOf(0.0D));
      this.exit = false;
    }
  }
  


  public Long getQuantity(Double price, Double capital, Double lotSize, Double leverage)
  {
    return Long.valueOf(MathLib.roundTick(capital.doubleValue() * leverage.doubleValue() / price.doubleValue(), lotSize.doubleValue()));
  }
  
  public boolean checkOrderConditions(Order order)
    throws MissingExpiryException
  {
    boolean conditionVal = true;
    for (OrderCondition orderCondition : order.conditions) {
      conditionVal = (conditionVal) && 
        (orderCondition.checkCondition(this.execData));
      if (!conditionVal)
        return conditionVal;
    }
    return conditionVal;
  }
  


  public boolean checkOrderFill(Order order, Contract contract)
  {
    if (order.side.equals(OrderSide.BUY))
    {

      if (order.type.equals(OrderType.STOP)) {
        if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMIT)) {
        if (MathLib.doubleCompare(contract.lo, order.price).intValue() < 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMITATPRICE)) {
        if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
        if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
          return true;
        }
      }
      else {
        return true;
      }
    }
    else if (order.side.equals(OrderSide.SELL))
    {

      if (order.type.equals(OrderType.STOP)) {
        if (MathLib.doubleCompare(contract.lo, order.price).intValue() <= 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMIT)) {
        if (MathLib.doubleCompare(contract.hi, order.price).intValue() > 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMITATPRICE)) {
        if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
          return true;
        }
      }
      else if (order.type.equals(OrderType.LIMITWITHSLIP)) {
        if (MathLib.doubleCompare(contract.hi, order.price).intValue() >= 0) {
          return true;
        }
      }
      else
        return true;
    }
    return false;
  }
  



  public HashMap<String, Object> getTriggerDetails(Order order, Contract contract, Contract prevContract)
  {
    String triggerType = "NORM";
    Double triggerPrice = Double.valueOf(0.0D);
    HashMap<String, Object> triggerMap = new HashMap();
    triggerMap.put("Trigger Price", triggerPrice);
    triggerMap.put("Capital Price", order.price);
    triggerMap.put("Trigger Type", triggerType);
    

    Double prevClose = null;
    if (prevContract != null) {
      prevClose = prevContract.cl;
    } else {
      prevClose = contract.cl;
    }
    
    if (order.type.equals(OrderType.EOD)) {
      triggerPrice = contract.cl;
      triggerMap.put("Trigger Price", triggerPrice);
      triggerMap.put("Capital Price", prevClose);
      return triggerMap;
    }
    
    if (order.type.equals(OrderType.ROLLOVER)) {
      triggerPrice = contract.cl;
      triggerMap.put("Trigger Price", triggerPrice);
      triggerMap.put("Capital Price", Double.valueOf(0.0D));
      return triggerMap;
    }
    
    if ((order.type.equals(OrderType.OPEN)) || 
      (order.type.equals(OrderType.OPENWITHSLIP))) {
      triggerPrice = contract.op;
      triggerMap.put("Trigger Price", triggerPrice);
      triggerMap.put("Capital Price", prevClose);
      return triggerMap;
    }
    

    triggerPrice = order.price;
    

    if (order.side.equals(OrderSide.BUY))
    {
      if (order.type.equals(OrderType.STOP))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0) {
          triggerPrice = contract.op;
          triggerType = "OP";
        }
        

      }
      else if (order.type.equals(OrderType.LIMIT))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
            triggerPrice = contract.op;
            triggerType = "OP";
          }
          
        }
        
      }
      else if (order.type.equals(OrderType.LIMITATPRICE))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
            triggerPrice = contract.op;
            triggerType = "OP";
          }
          
        }
        
      }
      else if (order.type.equals(OrderType.LIMITWITHSLIP))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() <= 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() < 0)) {
            triggerPrice = contract.op;
            triggerType = "OP";
          }
          
        }
        
      }
      else if (order.type.equals(OrderType.MARKET)) {
        triggerPrice = contract.hi;
        triggerMap.put("Capital Price", prevClose);
      }
      
    }
    else if (order.side.equals(OrderSide.SELL))
    {
      if (order.type.equals(OrderType.STOP))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() < 0) {
          triggerPrice = contract.op;
          triggerType = "OP";
        }
        

      }
      else if (order.type.equals(OrderType.LIMIT))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
            triggerPrice = contract.op;
          }
          
        }
      }
      else if (order.type.equals(OrderType.LIMITATPRICE))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
            triggerPrice = contract.op;
          }
          
        }
      }
      else if (order.type.equals(OrderType.LIMITWITHSLIP))
      {
        if (MathLib.doubleCompare(contract.op, order.price).intValue() > 0)
        {
          if (this.execData.dataType.contains("D")) {
            triggerPrice = contract.op;
            triggerType = "OP";

          }
          else if ((prevClose == null) || 
            (MathLib.doubleCompare(prevClose, order.price).intValue() > 0)) {
            triggerPrice = contract.op;
          }
          
        }
      }
      else if (order.type.equals(OrderType.MARKET)) {
        triggerPrice = contract.lo;
        triggerMap.put("Capital Price", prevClose);
      }
    }
    triggerMap.put("Trigger Price", triggerPrice);
    triggerMap.put("Trigger Type", triggerType);
    return triggerMap;
  }
  

  public HashMap<String, Double> runSlippageModel(Order order, Double triggerPrice)
    throws Exception
  {
    Double slippage = Double.valueOf(0.0D);
    Double openSlippage = Double.valueOf(0.0D);
    

    if (this.slippageModel.equals(SlippageModel.ConstantSlippage)) {
      slippage = this.execData.metaData.slippage;
      openSlippage = this.execData.metaData.openSlippage;


    }
    else if (this.slippageModel.equals(SlippageModel.TickSlippage)) {
      slippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);
      openSlippage = Double.valueOf(this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue() * 0.5D * 100.0D);


    }
    else if ((this.slippageModel.equals(SlippageModel.LinearModel)) || 
      (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)))
    {
      Double slippageSlope = this.execData.metaData.slippageSlope;
      
      if (slippageSlope == null) {
        throw new Exception(
          "Slippage Slope not available. Can't use Linear Model!!");
      }
      if (this.execData.dataType.contains("D")) {
        slippageSlope = Double.valueOf(2.0D * this.execData.metaData.slippageSlope.doubleValue());
      }
      

      double turnover = triggerPrice.doubleValue() * order.quantity.longValue() / 1000000.0D;
      Double slippageIntercept = this.execData.metaData.slippageIntercept;
      if (slippageIntercept == null)
        throw new Exception(
          "Slippage Intercept not available. Can't use Linear Model!!");
      slippage = Double.valueOf(slippageIntercept.doubleValue() + turnover * slippageSlope.doubleValue());
      
      openSlippage = slippage;



    }
    else if (this.slippageModel.equals(SlippageModel.AdaptiveModel))
    {
      Double atrRootPhiSlope = Double.valueOf(0.53D);
      

      double atrRootPhi = this.kylesLambaATR.getATRRootPhi(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
      

      slippage = Double.valueOf(atrRootPhiSlope.doubleValue() * atrRootPhi * 100.0D);
      
      if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
        slippage = this.execData.metaData.slippage;
      }
      if (slippage.doubleValue() < 0.0D) {
        slippage = Double.valueOf(0.0D);
      }
      openSlippage = slippage;



    }
    else if (this.slippageModel.equals(SlippageModel.AdaptiveModelATR))
    {
      Double dailyMovementSlope = Double.valueOf(0.004601D);
      Double kylesSlippageSlope = Double.valueOf(0.8510395D);
      Double tickSizeRatioSlope = Double.valueOf(-0.1511881D);
      

      double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - this.execData.rolloverContractData.contract.lo.doubleValue()) / 
        this.execData.rolloverContractData.contract.cl.doubleValue();
      double kylesSlippage = this.kylesLambaATR.predictSlippage(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage) / 100.0D;
      double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
      

      slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * 
        kylesSlippage + tickSizeRatioSlope.doubleValue() * tickSizeRatio) * 100.0D);
      
      if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
        slippage = this.execData.metaData.slippage;
      }
      if (slippage.doubleValue() < 0.0D) {
        slippage = Double.valueOf(0.0D);
      }
      openSlippage = slippage;



    }
    else if (this.slippageModel.equals(SlippageModel.AdaptiveModelSigma))
    {
      Double dailyMovementSlope = Double.valueOf(0.0062692D);
      Double kylesSlippageSlope = Double.valueOf(0.6521675D);
      Double stdDevJSlope = Double.valueOf(-0.8228327D);
      Double expbyTurnOverSlope = Double.valueOf(0.0785597D);
      Double tickSizeRatioSlope = Double.valueOf(-0.1388063D);
      

      double dailyMovement = (this.execData.rolloverContractData.contract.hi.doubleValue() - this.execData.rolloverContractData.contract.lo.doubleValue()) / 
        this.execData.rolloverContractData.contract.cl.doubleValue();
      double kylesSlippage = this.kylesLambaSigma.predictSlippage(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), 
        this.execData.metaData.slippage) / 100.0D;
      double stdDevJ = this.kylesLambaSigma.getStdDevJs(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()));
      double turnover = triggerPrice.doubleValue() * order.quantity.longValue();
      double expByTurnover = turnover / this.avgDailyTO;
      double tickSizeRatio = this.execData.metaData.tickSize.doubleValue() / triggerPrice.doubleValue();
      

      slippage = Double.valueOf((dailyMovementSlope.doubleValue() * dailyMovement + kylesSlippageSlope.doubleValue() * 
        kylesSlippage + stdDevJSlope.doubleValue() * stdDevJ + 
        expbyTurnOverSlope.doubleValue() * expByTurnover + tickSizeRatioSlope.doubleValue() * 
        tickSizeRatio) * 100.0D);
      
      if ((slippage.isNaN()) || (this.avgDailyTO < 1.5D)) {
        slippage = this.execData.metaData.slippage;
      }
      if (slippage.doubleValue() < 0.0D) {
        slippage = Double.valueOf(0.0D);
      }
      openSlippage = slippage;



    }
    else if (this.slippageModel.equals(SlippageModel.KylesLambdaSigma))
    {
      slippage = Double.valueOf(this.kylesLambaSigma.predictSlippage(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage));
      
      openSlippage = slippage;


    }
    else if (this.slippageModel.equals(SlippageModel.KylesLambdaATR))
    {
      slippage = Double.valueOf(this.kylesLambaATR.predictSlippage(
        Double.valueOf(order.quantity.longValue() / this.execData.metaData.lotSize.doubleValue()), this.execData.metaData.slippage));
      
      openSlippage = slippage;


    }
    else if (!this.slippageModel.equals(SlippageModel.ZeroSlippage)) {}
    


    HashMap<String, Double> slippageMap = new HashMap();
    slippageMap.put("Slippage", slippage);
    slippageMap.put("Open Slippage", openSlippage);
    
    return slippageMap;
  }
  




  public Double getExecutionPrice(Order order, HashMap<String, Double> slippageMap, Double triggerPrice, String triggerType)
  {
    Double slippage = (Double)slippageMap.get("Slippage");
    Double openSlippage = (Double)slippageMap.get("Open Slippage");
    



    Double lotExposure = Double.valueOf(this.execData.metaData.lotSize.doubleValue() * triggerPrice.doubleValue());
    Double transactionCharges = Double.valueOf((this.execData.metaData.exchangeFees.doubleValue() + this.execData.metaData.brokerage.doubleValue()) * this.execData.rolloverContractData.contract.currency.doubleValue() / 
      lotExposure.doubleValue());
    

    if (order.side.equals(OrderSide.BUY))
    {

      if (order.type.equals(OrderType.STOP))
      {

        if (triggerType.equals("OP")) {
          slippage = openSlippage;
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.LIMIT))
      {

        slippage = Double.valueOf(0.0D);
        if (triggerType.equals("OP")) {
          if (this.execData.dataType.contains("D"))
            slippage = openSlippage;
        } else {
          slippage = Double.valueOf(0.0D);
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.LIMITATPRICE))
      {

        slippage = Double.valueOf(0.0D);
        if (triggerType.equals("OP")) {
          if (this.execData.dataType.contains("D"))
            slippage = openSlippage;
        } else {
          slippage = Double.valueOf(0.0D);
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.LIMITWITHSLIP))
      {

        if (triggerType.equals("OP")) {
          slippage = openSlippage;
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.MARKET))
      {
        return triggerPrice;
      }
      
      if ((order.type.equals(OrderType.EOD)) || 
        (order.type.equals(OrderType.ROLLOVER)))
      {
        if (this.execData.dataType.contains("D")) {
          return Double.valueOf(triggerPrice.doubleValue() * (
            1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
        }
        if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
          return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()) + 
            this.execData.metaData.tickSize.doubleValue() / 2.0D);
        }
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + slippage.doubleValue() / 100.0D + transactionCharges.doubleValue()));
      }
      

      if (order.type.equals(OrderType.OPEN))
      {

        return Double.valueOf(triggerPrice.doubleValue() * (1.0D + transactionCharges.doubleValue()));
      }
      

      if (order.type.equals(OrderType.OPENWITHSLIP))
      {

        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D + (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

    }
    else if (order.side.equals(OrderSide.SELL))
    {
      if (order.type.equals(OrderType.STOP))
      {

        if (triggerType.equals("OP")) {
          slippage = openSlippage;
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      


      if (order.type.equals(OrderType.LIMIT))
      {

        slippage = Double.valueOf(0.0D);
        if (triggerType.equals("OP")) {
          if (this.execData.dataType.contains("D"))
            slippage = openSlippage;
        } else {
          slippage = Double.valueOf(0.0D);
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.LIMITATPRICE))
      {

        slippage = Double.valueOf(0.0D);
        if (triggerType.equals("OP")) {
          if (this.execData.dataType.contains("D"))
            slippage = openSlippage;
        } else {
          slippage = Double.valueOf(0.0D);
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.LIMITWITHSLIP))
      {

        if (triggerType.equals("OP")) {
          slippage = openSlippage;
        }
        
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
      

      if (order.type.equals(OrderType.MARKET)) {
        return triggerPrice;
      }
      
      if ((order.type.equals(OrderType.EOD)) || 
        (order.type.equals(OrderType.ROLLOVER)))
      {
        if (this.execData.dataType.contains("D")) {
          return Double.valueOf(triggerPrice.doubleValue() * (
            1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
        }
        if (this.slippageModel.equals(SlippageModel.LinearModelHalfTick)) {
          return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()) - 
            this.execData.metaData.tickSize.doubleValue() / 2.0D);
        }
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - slippage.doubleValue() / 100.0D - transactionCharges.doubleValue()));
      }
      

      if (order.type.equals(OrderType.OPEN)) {
        return Double.valueOf(triggerPrice.doubleValue() * (1.0D - transactionCharges.doubleValue()));
      }
      
      if (order.type.equals(OrderType.OPENWITHSLIP))
      {
        return Double.valueOf(triggerPrice.doubleValue() * (
          1.0D - (slippage.doubleValue() / 100.0D + transactionCharges.doubleValue())));
      }
    }
    

    return null;
  }
  




  public void updatePosition(Double capitalPrice, Double execPrice, Order order)
  {
    this.exit = false;
    

    int signal = -1;
    if (order.side.equals(OrderSide.BUY)) {
      signal = 1;
    }
    

    if ((this.position.equals(Long.valueOf(0L))) && (!order.type.equals(OrderType.ROLLOVER))) {
      this.euroTradeCash = Double.valueOf(0.0D);

    }
    else if (this.position.longValue() * signal > 0L) {
      this.usedCapital = 
        Double.valueOf(this.usedCapital.doubleValue() + capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * order.quantity.longValue() / this.execData.metaData.leverage.intValue());

    }
    else
    {
      long absPosition = Math.abs(this.position.longValue());
      

      if (order.quantity.longValue() > absPosition) {
        this.usedCapital = 
        
          Double.valueOf(capitalPrice.doubleValue() * this.exchangeRate.doubleValue() * (order.quantity.longValue() - absPosition) / this.execData.metaData.leverage.intValue());


      }
      else if (!order.type.equals(OrderType.ROLLOVER)) {
        this.usedCapital = Double.valueOf(0.0D);
        this.exit = true;
      }
    }
    

    this.position = Long.valueOf(this.position.longValue() + signal * order.quantity.longValue());
    this.euroTradeCash = Double.valueOf(this.euroTradeCash.doubleValue() - signal * execPrice.doubleValue() * order.quantity.longValue());
  }
  



  public void updateTradebook(Order order, Double triggerPrice, String triggerType, Double execPrice)
    throws IOException
  {
    Double tradeCumMTM = this.$TradeCumMTM;
    if (!this.exit) {
      tradeCumMTM = Double.valueOf(0.0D);
    }
    else
    {
      double $TradeMTM = this.euroTradeCash.doubleValue() * this.exchangeRate.doubleValue() - this.$TradeCumMTM.doubleValue();
      

      tradeCumMTM = Double.valueOf((this.$TradeCumMTM.doubleValue() + $TradeMTM) / this.capital.doubleValue());
    }
    String[] trade = { this.execData.dateTime.toString(), this.capital.toString(), 
      this.execData.scripID, order.side.getStringVal(), 
      order.type.getStringVal(), triggerPrice.toString(), 
      execPrice.toString(), order.quantity.toString(), triggerType, 
      this.execData.scripListID, this.execData.prevOrderTimestamp.toString(), 
      tradeCumMTM.toString() };
    this.tradeBook.add(trade);
    this.tbWriter.writeLine(trade);
    

    if (order.side == OrderSide.BUY) {
      this.buyTrades.add(execPrice);
    } else if (order.side == OrderSide.SELL) {
      this.sellTrades.add(execPrice);
    }
  }
  
  public void runExitRoutine() throws Exception
  {
    Contract rolloverFromContract = this.execData.rolloverContractData.contract;
    Contract rolloverToContract = new Contract();
    rolloverToContract.cl = rolloverFromContract.rolloverCl;
    

    if (this.position.longValue() > 0L)
    {

      Long orderQty = this.position;
      

      Order rollSellOrder = new Order(OrderSide.SELL, OrderType.EOD, 0.0D, 
        orderQty.longValue());
      

      executeOrder(rollSellOrder, rolloverFromContract, null);
      

      this.currentContract = rolloverToContract;
    }
    else if (this.position.longValue() < 0L)
    {

      Long orderQty = Long.valueOf(-this.position.longValue());
      

      Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.EOD, 0.0D, 
        orderQty.longValue());
      

      executeOrder(rollBuyOrder, rolloverFromContract, null);
      

      this.currentContract = rolloverToContract;
    }
  }
  

  public void runRolloverRoutine()
    throws Exception
  {
    Contract rolloverFromContract = this.execData.rolloverContractData.contract;
    Contract rolloverToContract = new Contract();
    rolloverToContract.cl = rolloverFromContract.rolloverCl;
    

    if (this.position.longValue() > 0L)
    {

      Long orderQty = this.position;
      

      Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 
        0.0D, orderQty.longValue());
      Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 
        0.0D, orderQty.longValue());
      

      executeOrder(rollSellOrder, rolloverFromContract, null);
      executeOrder(rollBuyOrder, rolloverToContract, null);
      

      this.currentContract = rolloverToContract;
    }
    else if (this.position.longValue() < 0L)
    {

      Long orderQty = Long.valueOf(-this.position.longValue());
      

      Order rollBuyOrder = new Order(OrderSide.BUY, OrderType.ROLLOVER, 
        0.0D, orderQty.longValue());
      Order rollSellOrder = new Order(OrderSide.SELL, OrderType.ROLLOVER, 
        0.0D, orderQty.longValue());
      

      executeOrder(rollBuyOrder, rolloverFromContract, null);
      executeOrder(rollSellOrder, rolloverToContract, null);
      

      this.currentContract = rolloverToContract;
    }
  }
  



  public void executeOrder(Order order, Contract contract, Contract prevContract)
    throws Exception
  {
    HashMap<String, Object> triggerMap = getTriggerDetails(order, contract, 
      prevContract);
    

    Double triggerPrice = (Double)triggerMap.get("Trigger Price");
    Double capitalPrice = (Double)triggerMap.get("Capital Price");
    String triggerType = (String)triggerMap.get("Trigger Type");
    

    HashMap<String, Double> slippageMap = runSlippageModel(order, 
      triggerPrice);
    

    Double executionPrice = getExecutionPrice(order, slippageMap, 
      triggerPrice, triggerType);
    

    updatePosition(capitalPrice, executionPrice, order);
    

    updateTradebook(order, capitalPrice, triggerType, executionPrice);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/ScripExecution.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */