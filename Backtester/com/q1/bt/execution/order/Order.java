package com.q1.bt.execution.order;

import java.util.ArrayList;


public class Order
{
  public String scripID;
  public OrderSide side;
  public OrderType type;
  public Double price;
  public Long quantity;
  public ArrayList<OrderCondition> conditions = new ArrayList();
  
  public Order(String scripID, OrderSide side, OrderType type, double price, long quantity)
  {
    this.scripID = scripID;
    this.side = side;
    this.type = type;
    this.price = Double.valueOf(price);
    this.quantity = Long.valueOf(quantity);
  }
  
  public Order(OrderSide side, OrderType type, double price, long quantity)
  {
    this.side = side;
    this.type = type;
    this.price = Double.valueOf(price);
    this.quantity = Long.valueOf(quantity);
  }
  
  public void addCondition(DataPoint dataPoint, Condition condition, Double Price)
  {
    this.conditions.add(new OrderCondition(dataPoint, condition, Price));
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/Order.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */