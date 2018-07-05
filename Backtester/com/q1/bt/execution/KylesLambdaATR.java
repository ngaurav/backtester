/*     */ package com.q1.bt.execution;
/*     */ 
/*     */ import com.q1.math.MathLib;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KylesLambdaATR
/*     */ {
/*     */   private Integer periodVol;
/*     */   private Integer periodReg;
/*     */   private ArrayList<Double> XY;
/*     */   private ArrayList<Double> X2;
/*     */   private ArrayList<Double> volList;
/*     */   private Integer volIndex;
/*     */   private Integer regIndex;
/*     */   private Double prevCl;
/*     */   private Double sumVol;
/*  21 */   private Double eta = Double.valueOf(0.0D);
/*     */   private Double sumXY;
/*     */   private Double sumX2;
/*     */   Double tr;
/*     */   Double atr;
/*     */   Double alpha;
/*     */   Double volJ;
/*     */   
/*  29 */   public KylesLambdaATR(Integer periodVol, Integer periodReg) { this.periodReg = periodReg;
/*  30 */     this.periodVol = periodVol;
/*  31 */     this.XY = new ArrayList();
/*  32 */     this.X2 = new ArrayList();
/*  33 */     this.volList = new ArrayList();
/*  34 */     this.volIndex = Integer.valueOf(0);
/*  35 */     this.regIndex = Integer.valueOf(0);
/*  36 */     this.prevCl = Double.valueOf(-1.0D);
/*  37 */     this.sumVol = Double.valueOf(0.0D);
/*  38 */     this.eta = Double.valueOf(0.0D);
/*  39 */     this.sumXY = Double.valueOf(0.0D);
/*  40 */     this.sumX2 = Double.valueOf(0.0D);
/*  41 */     this.tr = Double.valueOf(0.0D);
/*  42 */     this.atr = Double.valueOf(0.0D);
/*  43 */     this.alpha = Double.valueOf(2.0D / (periodVol.intValue() + 1.0D));
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateETA(Double hi, Double lo, Double cl, Double vol, Double rolloverCl)
/*     */   {
/*  49 */     if (this.prevCl.doubleValue() < 0.0D) {
/*  50 */       this.tr = Double.valueOf((hi.doubleValue() - lo.doubleValue()) / cl.doubleValue());
/*  51 */       this.atr = this.tr;
/*     */     } else {
/*  53 */       this.tr = Double.valueOf((MathLib.max(hi.doubleValue(), this.prevCl.doubleValue()) - MathLib.min(lo.doubleValue(), this.prevCl.doubleValue())) / cl.doubleValue());
/*  54 */       this.atr = Double.valueOf(this.tr.doubleValue() * this.alpha.doubleValue() + this.atr.doubleValue() * (1.0D - this.alpha.doubleValue()));
/*     */     }
/*     */     
/*  57 */     if ((this.prevCl.doubleValue() > 0.0D) && (vol.doubleValue() > 0.0D)) {
/*  58 */       Double ret = Double.valueOf(cl.doubleValue() / this.prevCl.doubleValue() - 1.0D);
/*  59 */       this.eta = getETA(ret, vol);
/*     */     }
/*  61 */     this.prevCl = cl;
/*  62 */     if (rolloverCl.doubleValue() > 0.0D) {
/*  63 */       shiftForRO(Double.valueOf(rolloverCl.doubleValue() - cl.doubleValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   public double getATRRootPhi(Double tradeVol)
/*     */   {
/*  69 */     return this.atr.doubleValue() * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size()));
/*     */   }
/*     */   
/*     */   public double predictSlippage(Double tradeVol, Double constantSlippage) {
/*  73 */     if (this.XY.size() < 50) {
/*  74 */       return constantSlippage.doubleValue();
/*     */     }
/*  76 */     return this.eta.doubleValue() * this.atr.doubleValue() * Math.sqrt(tradeVol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())) * 100.0D;
/*     */   }
/*     */   
/*     */   private Double getETA(Double ret, Double vol) {
/*  80 */     Double x = getX(ret, vol);
/*  81 */     Double y = Double.valueOf(Math.abs(ret.doubleValue()));
/*  82 */     Double xy = Double.valueOf(x.doubleValue() * y.doubleValue());
/*  83 */     Double x2 = Double.valueOf(x.doubleValue() * x.doubleValue());
/*  84 */     if (this.XY.size() < this.periodReg.intValue()) {
/*  85 */       this.XY.add(xy);
/*  86 */       this.sumXY = Double.valueOf(this.sumXY.doubleValue() + xy.doubleValue());
/*  87 */       this.X2.add(x2);
/*  88 */       this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + x2.doubleValue());
/*     */     } else {
/*  90 */       this.sumXY = Double.valueOf(this.sumXY.doubleValue() + (xy.doubleValue() - ((Double)this.XY.get(this.regIndex.intValue())).doubleValue()));
/*  91 */       this.XY.set(this.regIndex.intValue(), xy);
/*  92 */       this.sumX2 = Double.valueOf(this.sumX2.doubleValue() + (x2.doubleValue() - ((Double)this.X2.get(this.regIndex.intValue())).doubleValue()));
/*  93 */       this.X2.set(this.regIndex.intValue(), x2);
/*  94 */       this.regIndex = Integer.valueOf((this.regIndex.intValue() + 1) % this.periodReg.intValue());
/*     */     }
/*  96 */     this.eta = Double.valueOf(this.sumXY.doubleValue() / this.sumX2.doubleValue());
/*  97 */     return this.eta;
/*     */   }
/*     */   
/*     */   private Double getX(Double ret, Double vol) {
/* 101 */     if (this.volList.size() < this.periodVol.intValue()) {
/* 102 */       this.volList.add(vol);
/* 103 */       this.sumVol = Double.valueOf(this.sumVol.doubleValue() + vol.doubleValue());
/*     */     } else {
/* 105 */       this.sumVol = Double.valueOf(this.sumVol.doubleValue() + (vol.doubleValue() - ((Double)this.volList.get(this.volIndex.intValue())).doubleValue()));
/* 106 */       this.volList.set(this.volIndex.intValue(), vol);
/* 107 */       this.volIndex = Integer.valueOf((this.volIndex.intValue() + 1) % this.periodVol.intValue());
/*     */     }
/* 109 */     this.volJ = Double.valueOf(Math.sqrt(vol.doubleValue() / (this.sumVol.doubleValue() / this.volList.size())));
/*     */     
/* 111 */     return Double.valueOf(this.volJ.doubleValue() * this.atr.doubleValue());
/*     */   }
/*     */   
/*     */   private void shiftForRO(Double rollOver) {
/* 115 */     this.prevCl = Double.valueOf(this.prevCl.doubleValue() + rollOver.doubleValue());
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/execution/KylesLambdaATR.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */