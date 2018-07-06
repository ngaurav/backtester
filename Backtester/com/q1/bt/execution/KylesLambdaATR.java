package com.q1.bt.execution;

import com.q1.math.MathLib;
import java.util.ArrayList;





public class KylesLambdaATR
{
  private Integer periodVol;
  private Integer periodReg;
  private ArrayList<Double> XY;
  private ArrayList<Double> X2;
  private ArrayList<Double> volList;
  private Integer volIndex;
  private Integer regIndex;
  private Double prevCl;
  private Double sumVol;
  private Double eta = Double.valueOf(0.0D);
  private Double sumXY;
  private Double sumX2;
  Double tr;
  Double atr;
  Double alpha;
  Double volJ;
  
  public KylesLambdaATR(Integer periodVol, Integer periodReg) { this.periodReg = periodReg;
    this.periodVol = periodVol;
    this.XY = new ArrayList();
    this.X2 = new ArrayList();
    this.volList = new ArrayList();
    this.volIndex = Integer.valueOf(0);
    this.regIndex = Integer.valueOf(0);
    this.prevCl = Double.valueOf(-1.0D);
    this.sumVol = Double.valueOf(0.0D);
    this.eta = Double.valueOf(0.0D);
    this.sumXY = Double.valueOf(0.0D);
    this.sumX2 = Double.valueOf(0.0D);
    this.tr = Double.valueOf(0.0D);
    this.atr = Double.valueOf(0.0D);
    this.alpha = Double.valueOf(2.0D / (periodVol.intValue() + 1.0D));
  }
  

  public void updateETA(Double hi, Double lo, Double cl, Double vol, Double rolloverCl)
  {
    if (this.prevCl.doubleValue() < 0.0D) {
      this.tr = Double.valueOf((hi.doubleValue() - lo.doubleValue()) / cl.doubleValue());
      this.atr = this.tr;
    } else {
      this.tr = Double.valueOf((MathLib.max(hi.doubleValue(), this.prevCl.doubleValue()) - MathLib.min(lo.doubleValue(), this.prevCl.doubleValue())) / cl.doubleValue());
      this.atr = Double.valueOf(this.tr.doubleValue() * this.alpha.doubleValue() + this.atr.doubleValue() * (1.0D - this.alpha.doubleValue()));
    }
    
    if ((this.prevCl.doubleValue() > 0.0D) && (vol.doubleValue() > 0.0D)) {
      Double ret = Double.valueOf(cl.doubleValue() / this.prevCl.doubleValue() - 1.0D);
      this.eta = getETA(ret, vol);
    }
    this.prevCl = cl;
    if (rolloverCl.doubleValue() > 0.0D) {
      shiftForRO(Double.valueOf(rolloverCl.doubleValue() - cl.doubleValue()));
    }
  }
  
  public double getATRRootPhi(Double tradeVol)
  {
    return this.atr.doubleValue() * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size()));
  }
  
  public double predictSlippage(Double tradeVol, Double constantSlippage) {
    if (this.XY.size() < 50) {
      return constantSlippage.doubleValue();
    }
    return this.eta.doubleValue() * this.atr.doubleValue() * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())) * 100.0D;
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
    if (this.volList.size() < this.periodVol.intValue()) {
      this.volList.add(vol);
      this.sumVol = Double.valueOf(this.sumVol.doubleValue() + vol.doubleValue());
    } else {
      this.sumVol = Double.valueOf(this.sumVol.doubleValue() + (vol.doubleValue() - ((Double)this.volList.get(this.volIndex.intValue())).doubleValue()));
      this.volList.set(this.volIndex.intValue(), vol);
      this.volIndex = Integer.valueOf((this.volIndex.intValue() + 1) % this.periodVol.intValue());
    }
    this.volJ = Double.valueOf(Math.sqrt(vol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())));
    
    return Double.valueOf(this.volJ.doubleValue() * this.atr.doubleValue());
  }
  
  private void shiftForRO(Double rollOver) {
    this.prevCl = Double.valueOf(this.prevCl.doubleValue() + rollOver.doubleValue());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/KylesLambdaATR.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */