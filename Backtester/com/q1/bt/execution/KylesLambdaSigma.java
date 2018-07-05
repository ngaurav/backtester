/*     */ package com.q1.bt.execution;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ public class KylesLambdaSigma
/*     */ {
/*     */   private Integer periodVol;
/*     */   private Integer periodReg;
/*     */   private ArrayList<Double> XY;
/*     */   private ArrayList<Double> X2;
/*     */   private ArrayList<Double> absRetList;
/*     */   private ArrayList<Double> volList;
/*     */   private Integer volIndex;
/*     */   private Integer regIndex;
/*     */   private Double prevCl;
/*     */   private Double sumRet;
/*     */   private Double sumRet2;
/*     */   private Double sumVol;
/*  20 */   public Double eta = Double.valueOf(0.0D);
/*     */   private Double sumXY;
/*     */   private Double sumX2;
/*     */   private double sigma;
/*     */   
/*  25 */   public KylesLambdaSigma(Integer periodVol, Integer periodReg) { this.periodReg = periodReg;
/*  26 */     this.periodVol = periodVol;
/*  27 */     this.XY = new ArrayList();
/*  28 */     this.X2 = new ArrayList();
/*  29 */     this.absRetList = new ArrayList();
/*  30 */     this.volList = new ArrayList();
/*  31 */     this.volIndex = Integer.valueOf(0);
/*  32 */     this.regIndex = Integer.valueOf(0);
/*  33 */     this.prevCl = Double.valueOf(-1.0D);
/*  34 */     this.sumRet = Double.valueOf(0.0D);
/*  35 */     this.sumRet2 = Double.valueOf(0.0D);
/*  36 */     this.sumVol = Double.valueOf(0.0D);
/*  37 */     this.eta = Double.valueOf(0.0D);
/*  38 */     this.sumXY = Double.valueOf(0.0D);
/*  39 */     this.sumX2 = Double.valueOf(0.0D);
/*     */   }
/*     */   
/*     */   public void updateETA(Double cl, Double vol, Double rolloverCl) {
/*  43 */     if ((this.prevCl.doubleValue() > 0.0D) && (vol.doubleValue() > 0.0D)) {
/*  44 */       Double ret = Double.valueOf(cl.doubleValue() / this.prevCl.doubleValue() - 1.0D);
/*  45 */       this.eta = getETA(ret, vol);
/*     */     }
/*  47 */     this.prevCl = cl;
/*  48 */     if (rolloverCl.doubleValue() > 0.0D) {
/*  49 */       shiftForRO(Double.valueOf(rolloverCl.doubleValue() - cl.doubleValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public double predictSlippage(Double tradeVol, Double constantSlippage)
/*     */   {
/*  55 */     if (this.XY.size() < 50) {
/*  56 */       return constantSlippage.doubleValue();
/*     */     }
/*  58 */     return this.eta.doubleValue() * this.sigma * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())) * 100.0D;
/*     */   }
/*     */   
/*     */   public double getStdDevJs(Double tradeVol) {
/*  62 */     return this.sigma * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size()));
/*     */   }
/*     */   
/*     */   private Double getETA(Double ret, Double vol) {
/*  66 */     Double x = getX(ret, vol);
/*  67 */     Double y = Double.valueOf(Math.abs(ret.doubleValue()));
/*  68 */     Double xy = Double.valueOf(x.doubleValue() * y.doubleValue());
/*  69 */     Double x2 = Double.valueOf(x.doubleValue() * x.doubleValue());
/*  70 */     if (this.XY.size() < this.periodReg.intValue()) {
/*  71 */       this.XY.add(xy);
/*  72 */       this.sumXY = Double.valueOf(this.sumXY.doubleValue() + xy.doubleValue());
/*  73 */       this.X2.add(x2);
/*  74 */       this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + x2.doubleValue());
/*     */     } else {
/*  76 */       this.sumXY = Double.valueOf(this.sumXY.doubleValue() + (xy.doubleValue() - ((Double)this.XY.get(this.regIndex.intValue())).doubleValue()));
/*  77 */       this.XY.set(this.regIndex.intValue(), xy);
/*  78 */       this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + (x2.doubleValue() - ((Double)this.X2.get(this.regIndex.intValue())).doubleValue()));
/*  79 */       this.X2.set(this.regIndex.intValue(), x2);
/*  80 */       this.regIndex = Integer.valueOf((this.regIndex.intValue() + 1) % this.periodReg.intValue());
/*     */     }
/*  82 */     this.eta = Double.valueOf(this.sumXY.doubleValue() / this.sumX2.doubleValue());
/*  83 */     return this.eta;
/*     */   }
/*     */   
/*     */   private Double getX(Double ret, Double vol) {
/*  87 */     if (this.absRetList.size() < this.periodVol.intValue()) {
/*  88 */       this.absRetList.add(ret);
/*  89 */       this.sumRet2 = Double.valueOf(this.sumRet2.doubleValue() + ret.doubleValue() * ret.doubleValue());
/*  90 */       this.sumRet = Double.valueOf(this.sumRet.doubleValue() + ret.doubleValue());
/*  91 */       this.volList.add(vol);
/*  92 */       this.sumVol = Double.valueOf(this.sumVol.doubleValue() + vol.doubleValue());
/*     */     } else {
/*  94 */       this.sumRet2 = Double.valueOf(this.sumRet2.doubleValue() + (ret.doubleValue() * ret.doubleValue() - ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue() * ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue()));
/*  95 */       this.sumRet = Double.valueOf(this.sumRet.doubleValue() + (ret.doubleValue() - ((Double)this.absRetList.get(this.volIndex.intValue())).doubleValue()));
/*  96 */       this.sumVol = Double.valueOf(this.sumVol.doubleValue() + (vol.doubleValue() - ((Double)this.volList.get(this.volIndex.intValue())).doubleValue()));
/*  97 */       this.absRetList.set(this.volIndex.intValue(), ret);
/*  98 */       this.volList.set(this.volIndex.intValue(), vol);
/*  99 */       this.volIndex = Integer.valueOf((this.volIndex.intValue() + 1) % this.periodVol.intValue());
/*     */     }
/* 101 */     this.sigma = Math.sqrt((this.sumRet2.doubleValue() * this.absRetList.size() - this.sumRet.doubleValue() * this.sumRet.doubleValue()) / this.absRetList.size() / this.absRetList.size());
/* 102 */     Double volJ = Double.valueOf(Math.sqrt(vol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())));
/* 103 */     return Double.valueOf(this.sigma * volJ.doubleValue());
/*     */   }
/*     */   
/*     */   private void shiftForRO(Double rollOver) {
/* 107 */     this.prevCl = Double.valueOf(this.prevCl.doubleValue() + rollOver.doubleValue());
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/KylesLambdaSigma.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */