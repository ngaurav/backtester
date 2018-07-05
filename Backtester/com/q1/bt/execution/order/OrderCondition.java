/*    */ package com.q1.bt.execution.order;
/*    */ 
/*    */ import com.q1.bt.data.classes.Contract;
/*    */ import com.q1.bt.data.classes.ContractData;
/*    */ import com.q1.bt.execution.ExecutionData;
/*    */ import com.q1.exception.bt.MissingExpiryException;
/*    */ import com.q1.math.MathLib;
/*    */ 
/*    */ public class OrderCondition
/*    */ {
/*    */   public DataPoint dataPoint;
/*    */   public Condition condition;
/*    */   public Double Price;
/*    */   
/*    */   public OrderCondition(DataPoint dataPoint, Condition condition, Double Price)
/*    */   {
/* 17 */     this.dataPoint = dataPoint;
/* 18 */     this.condition = condition;
/* 19 */     this.Price = Price;
/*    */   }
/*    */   
/*    */   public boolean checkCondition(ExecutionData data)
/*    */     throws MissingExpiryException
/*    */   {
/* 25 */     Contract c = data.mainContractData.contract;
/*    */     
/* 27 */     if (c == null) {
/* 28 */       throw new MissingExpiryException();
/*    */     }
/*    */     
/* 31 */     int conditionVal = this.condition.getVal();
/* 32 */     int dataVal = this.dataPoint.getVal();
/* 33 */     Double checkData = Double.valueOf(0.0D);
/*    */     
/*    */ 
/*    */ 
/* 37 */     if (dataVal == 1) {
/* 38 */       checkData = c.op;
/*    */ 
/*    */     }
/* 41 */     else if (dataVal == 2) {
/* 42 */       checkData = c.hi;
/*    */ 
/*    */     }
/* 45 */     else if (dataVal == 3) {
/* 46 */       checkData = c.lo;
/*    */ 
/*    */     }
/* 49 */     else if (dataVal == 4) {
/* 50 */       checkData = c.cl;
/*    */     } else {
/* 52 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 56 */     if (conditionVal == 1) {
/* 57 */       if (MathLib.doubleCompare(checkData, this.Price).intValue() > 0) {
/* 58 */         return true;
/*    */       }
/*    */     }
/* 61 */     else if (conditionVal == 2) {
/* 62 */       if (MathLib.doubleCompare(checkData, this.Price).intValue() >= 0) {
/* 63 */         return true;
/*    */       }
/*    */     }
/* 66 */     else if (conditionVal == 3) {
/* 67 */       if (MathLib.doubleCompare(checkData, this.Price).intValue() < 0) {
/* 68 */         return true;
/*    */       }
/*    */     }
/* 71 */     else if (conditionVal == 4) {
/* 72 */       if (MathLib.doubleCompare(checkData, this.Price).intValue() <= 0) {
/* 73 */         return true;
/*    */       }
/*    */     }
/* 76 */     else if (conditionVal == 5) {
/* 77 */       if (MathLib.doubleCompare(checkData, this.Price).intValue() == 0)
/* 78 */         return true;
/*    */     } else {
/* 80 */       return false;
/*    */     }
/*    */     
/* 83 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderCondition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */