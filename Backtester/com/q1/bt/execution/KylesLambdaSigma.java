package com.q1.bt.execution;

import java.util.ArrayList;


public class KylesLambdaSigma
{
  private Integer periodVol;
  private Integer periodReg;
  private ArrayList<Double> XY;
  private ArrayList<Double> X2;
  private ArrayList<Double> absRetList;
  private ArrayList<Double> volList;
  private Integer volIndex;
  private Integer regIndex;
  private Double prevCl;
  private Double sumRet;
  private Double sumRet2;
  private Double sumVol;
  public Double eta = Double.valueOf(0.0D);
  private Double sumXY;
  private Double sumX2;
  private double sigma;
  
  public KylesLambdaSigma(Integer periodVol, Integer periodReg) { this.periodReg = periodReg;
    this.periodVol = periodVol;
    this.XY = new ArrayList();
    this.X2 = new ArrayList();
    this.absRetList = new ArrayList();
    this.volList = new ArrayList();
    this.volIndex = Integer.valueOf(0);
    this.regIndex = Integer.valueOf(0);
    this.prevCl = Double.valueOf(-1.0D);
    this.sumRet = Double.valueOf(0.0D);
    this.sumRet2 = Double.valueOf(0.0D);
    this.sumVol = Double.valueOf(0.0D);
    this.eta = Double.valueOf(0.0D);
    this.sumXY = Double.valueOf(0.0D);
    this.sumX2 = Double.valueOf(0.0D);
  }
  
  public void updateETA(Double cl, Double vol, Double rolloverCl) {
    if ((this.prevCl.doubleValue() > 0.0D) && (vol.doubleValue() > 0.0D)) {
      Double ret = Double.valueOf(cl.doubleValue() / this.prevCl.doubleValue() - 1.0D);
      this.eta = getETA(ret, vol);
    }
    this.prevCl = cl;
    if (rolloverCl.doubleValue() > 0.0D) {
      shiftForRO(Double.valueOf(rolloverCl.doubleValue() - cl.doubleValue()));
    }
  }
  
  public double predictSlippage(Double tradeVol, Double constantSlippage)
  {
    if (this.XY.size() < 50) {
      return constantSlippage.doubleValue();
    }
    return this.eta.doubleValue() * this.sigma * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())) * 100.0D;
  }
  
  public double getStdDevJs(Double tradeVol) {
    return this.sigma * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size()));
  }
  
  private Double getETA(Double ret, Double vol) {
    Double x = getX(ret, vol);
    Double y = Double.valueOf(Math.abs(ret.doubleValue()));
    Double xy = Double.valueOf(x.doubleValue() * y.doubleValue());
    Double x2 = Double.valueOf(x.doubleValue() * x.doubleValue());
    if (this.XY.size() < this.periodReg.intValue()) {
      this.XY.add(xy);
      this.sumXY = Double.valueOf(this.sumXY.doubleValue() + xy.doubleValue());
      this.X2.add(x2);
      this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + x2.doubleValue());
    } else {
      this.sumXY = Double.valueOf(this.sumXY.doubleValue() + (xy.doubleValue() - ((Double)this.XY.get(this.regIndex.intValue())).doubleValue()));
      this.XY.set(this.regIndex.intValue(), xy);
      this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + (x2.doubleValue() - ((Double)this.X2.get(this.regIndex.intValue())).doubleValue()));
      this.X2.set(this.regIndex.intValue(), x2);
      this.regIndex = Integer.valueOf((this.regIndex.intValue() + 1) % this.periodReg.intValue());
    }
    this.eta = Double.valueOf(this.sumXY.doubleValue() / this.sumX2.doubleValue());
    return this.eta;
  }
  
  private Double getX(Double ret, Double vol) {
    if (this.absRetList.size() < this.periodVol.intValue()) {
      this.absRetList.add(ret);
      this.sumRet2 = Double.valueOf(this.sumRet2.doubleValue() + ret.doubleValue() * ret.doubleValue());
      this.sumRet = Double.valueOf(this.sumRet.doubleValue() + ret.doubleValue());
      this.volList.add(vol);
      this.sumVol = Double.valueOf(this.sumVol.doubleValue() + vol.doubleValue());
    } else {
      this.sumRet2 = Double.valueOf(this.sumRet2.doubleValue() + (ret.doubleValue() * ret.doubleValue() - ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue() * ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue()));
      this.sumRet = Double.valueOf(this.sumRet.doubleValue() + (ret.doubleValue() - ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue()));
      this.sumVol = Double.valueOf(this.sumVol.doubleValue() + (vol.doubleValue() - ((Double)this.volList.get(this.volIndex.intValue())).doubleValue()));
      this.absRetList.set(this.volIndex.intValue(), ret);
      this.volList.set(this.volIndex.intValue(), vol);
      this.volIndex = Integer.valueOf((this.volIndex.intValue() + 1) % this.periodVol.intValue());
    }
    this.sigma = Math.sqrt((this.sumRet2.doubleValue() * this.absRetList.size() - this.sumRet.doubleValue() * this.sumRet.doubleValue()) / this.absRetList.size() / this.absRetList.size());
    Double volJ = Double.valueOf(Math.sqrt(vol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())));
    return Double.valueOf(this.sigma * volJ.doubleValue());
  }
  
  private void shiftForRO(Double rollOver) {
    this.prevCl = Double.valueOf(this.prevCl.doubleValue() + rollOver.doubleValue());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/KylesLambdaSigma.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */