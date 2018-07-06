package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.CandleData;

public class Correl
{
  Double asset1Prevcl;
  Double asset2Prevcl;
  Double asset1sqr;
  Double asset2sqr;
  Double asset1sum;
  Double asset2sum;
  Double asset1xasset2;
  Integer period;
  java.util.LinkedList<Double> asset1ClList;
  java.util.LinkedList<Double> asset2ClList;
  
  public Correl(int period)
  {
    this.period = Integer.valueOf(period);
    this.asset1sqr = Double.valueOf(0.0D);
    this.asset2sqr = Double.valueOf(0.0D);
    this.asset1sum = Double.valueOf(0.0D);
    this.asset2sum = Double.valueOf(0.0D);
    this.asset1xasset2 = Double.valueOf(0.0D);
    this.asset1ClList = new java.util.LinkedList();
    this.asset2ClList = new java.util.LinkedList();
  }
  
  public void updateCorrel(CandleData dd1, CandleData dd2, Long date) {
    if ((dd1.date.longValue() != date.longValue()) || (dd2.date.longValue() != date.longValue())) return;
    if ((this.asset1Prevcl == null) || (this.asset2Prevcl == null)) {
      this.asset1Prevcl = dd1.cl;
      this.asset2Prevcl = dd2.cl;
      return;
    }
    
    Double ret1 = Double.valueOf(dd1.cl.doubleValue() / this.asset1Prevcl.doubleValue() - 1.0D);
    Double ret2 = Double.valueOf(dd2.cl.doubleValue() / this.asset2Prevcl.doubleValue() - 1.0D);
    this.asset1ClList.add(ret1);
    this.asset2ClList.add(ret2);
    this.asset1sqr = Double.valueOf(this.asset1sqr.doubleValue() + ret1.doubleValue() * ret1.doubleValue());
    this.asset2sqr = Double.valueOf(this.asset2sqr.doubleValue() + ret2.doubleValue() * ret2.doubleValue());
    this.asset1sum = Double.valueOf(this.asset1sum.doubleValue() + ret1.doubleValue());
    this.asset2sum = Double.valueOf(this.asset2sum.doubleValue() + ret2.doubleValue());
    this.asset1xasset2 = Double.valueOf(this.asset1xasset2.doubleValue() + ret1.doubleValue() * ret2.doubleValue());
    this.asset1Prevcl = dd1.cl;
    this.asset2Prevcl = dd2.cl;
    
    if (this.asset1ClList.size() > this.period.intValue()) {
      ret1 = (Double)this.asset1ClList.getFirst();
      ret2 = (Double)this.asset2ClList.getFirst();
      this.asset1sqr = Double.valueOf(this.asset1sqr.doubleValue() - ret1.doubleValue() * ret1.doubleValue());
      this.asset2sqr = Double.valueOf(this.asset2sqr.doubleValue() - ret2.doubleValue() * ret2.doubleValue());
      this.asset1sum = Double.valueOf(this.asset1sum.doubleValue() - ret1.doubleValue());
      this.asset2sum = Double.valueOf(this.asset2sum.doubleValue() - ret2.doubleValue());
      this.asset1xasset2 = Double.valueOf(this.asset1xasset2.doubleValue() - ret1.doubleValue() * ret2.doubleValue());
      this.asset1ClList.removeFirst();
      this.asset2ClList.removeFirst();
    }
    
    if (dd1.isRoll.booleanValue())
      this.asset1Prevcl = Double.valueOf(this.asset1Prevcl.doubleValue() + dd1.rollOver.doubleValue());
    if (dd2.isRoll.booleanValue())
      this.asset2Prevcl = Double.valueOf(this.asset2Prevcl.doubleValue() + dd2.rollOver.doubleValue());
  }
  
  public Double getVal() {
    int count = this.asset1ClList.size();
    if ((this.asset1sqr.doubleValue() == 0.0D) || (count == 1))
      return Double.valueOf(0.0D);
    return Double.valueOf(Math.abs((count * this.asset1xasset2.doubleValue() - this.asset1sum.doubleValue() * this.asset2sum.doubleValue()) / 
      Math.sqrt((count * this.asset1sqr.doubleValue() - this.asset1sum.doubleValue() * this.asset1sum.doubleValue()) * (
      count * this.asset2sqr.doubleValue() - this.asset2sum.doubleValue() * this.asset2sum.doubleValue()))));
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/Correl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */