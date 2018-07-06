package com.q1.bt.execution.order;

import com.q1.bt.data.classes.Contract;
import com.q1.bt.data.classes.ContractData;
import com.q1.bt.execution.ExecutionData;
import com.q1.exception.bt.MissingExpiryException;
import com.q1.math.MathLib;

public class OrderCondition
{
  public DataPoint dataPoint;
  public Condition condition;
  public Double Price;
  
  public OrderCondition(DataPoint dataPoint, Condition condition, Double Price)
  {
    this.dataPoint = dataPoint;
    this.condition = condition;
    this.Price = Price;
  }
  
  public boolean checkCondition(ExecutionData data)
    throws MissingExpiryException
  {
    Contract c = data.mainContractData.contract;
    
    if (c == null) {
      throw new MissingExpiryException();
    }
    
    int conditionVal = this.condition.getVal();
    int dataVal = this.dataPoint.getVal();
    Double checkData = Double.valueOf(0.0D);
    


    if (dataVal == 1) {
      checkData = c.op;

    }
    else if (dataVal == 2) {
      checkData = c.hi;

    }
    else if (dataVal == 3) {
      checkData = c.lo;

    }
    else if (dataVal == 4) {
      checkData = c.cl;
    } else {
      return false;
    }
    

    if (conditionVal == 1) {
      if (MathLib.doubleCompare(checkData, this.Price).intValue() > 0) {
        return true;
      }
    }
    else if (conditionVal == 2) {
      if (MathLib.doubleCompare(checkData, this.Price).intValue() >= 0) {
        return true;
      }
    }
    else if (conditionVal == 3) {
      if (MathLib.doubleCompare(checkData, this.Price).intValue() < 0) {
        return true;
      }
    }
    else if (conditionVal == 4) {
      if (MathLib.doubleCompare(checkData, this.Price).intValue() <= 0) {
        return true;
      }
    }
    else if (conditionVal == 5) {
      if (MathLib.doubleCompare(checkData, this.Price).intValue() == 0)
        return true;
    } else {
      return false;
    }
    
    return false;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderCondition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */