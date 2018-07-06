package com.q1.bt.execution.order;
import java.io.Serializable;

public enum OrderType implements Serializable { STOP(1),  LIMIT(2),  MARKET(3),  EOD(4),  OPEN(5),  OPENWITHSLIP(6),  ROLLOVER(7),  LIMITATPRICE(8),  LIMITWITHSLIP(9);
  
  private final int Val;
  
  private OrderType(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
  
  public String getStringVal() {
    if (this.Val == 1)
      return "STOP";
    if (this.Val == 2)
      return "LIMIT";
    if (this.Val == 3)
      return "MARKET";
    if (this.Val == 4)
      return "EOD";
    if (this.Val == 5)
      return "OPEN";
    if (this.Val == 6)
      return "OPENWITHSLIP";
    if (this.Val == 7)
      return "ROLLOVER";
    if (this.Val == 8)
      return "LIMITATPRICE";
    if (this.Val == 9) {
      return "LIMITWITHSLIP";
    }
    return null;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/order/OrderType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */