package com.q1.bt.postprocess;

public enum OutputShape
{
  Line(1),  Scatter(2),  Area(3);
  
  private final int Val;
  
  private OutputShape(int Val) {
    this.Val = Val;
  }
  
  public int getVal() {
    return this.Val;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/OutputShape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */