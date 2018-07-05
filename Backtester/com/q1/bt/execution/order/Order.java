/*    */ package com.q1.bt.execution.order;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ public class Order
/*    */ {
/*    */   public String scripID;
/*    */   public OrderSide side;
/*    */   public OrderType type;
/*    */   public Double price;
/*    */   public Long quantity;
/* 13 */   public ArrayList<OrderCondition> conditions = new ArrayList();
/*    */   
/*    */   public Order(String scripID, OrderSide side, OrderType type, double price, long quantity)
/*    */   {
/* 17 */     this.scripID = scripID;
/* 18 */     this.side = side;
/* 19 */     this.type = type;
/* 20 */     this.price = Double.valueOf(price);
/* 21 */     this.quantity = Long.valueOf(quantity);
/*    */   }
/*    */   
/*    */   public Order(OrderSide side, OrderType type, double price, long quantity)
/*    */   {
/* 26 */     this.side = side;
/* 27 */     this.type = type;
/* 28 */     this.price = Double.valueOf(price);
/* 29 */     this.quantity = Long.valueOf(quantity);
/*    */   }
/*    */   
/*    */   public void addCondition(DataPoint dataPoint, Condition condition, Double Price)
/*    */   {
/* 34 */     this.conditions.add(new OrderCondition(dataPoint, condition, Price));
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/Order.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */