/*    */ package com.q1.bt.machineLearning.absclasses;
/*    */ 
/*    */ import com.q1.bt.machineLearning.utility.CandleData;
/*    */ 
/*    */ public class Correl
/*    */ {
/*    */   Double asset1Prevcl;
/*    */   Double asset2Prevcl;
/*    */   Double asset1sqr;
/*    */   Double asset2sqr;
/*    */   Double asset1sum;
/*    */   Double asset2sum;
/*    */   Double asset1xasset2;
/*    */   Integer period;
/*    */   java.util.LinkedList<Double> asset1ClList;
/*    */   java.util.LinkedList<Double> asset2ClList;
/*    */   
/*    */   public Correl(int period)
/*    */   {
/* 20 */     this.period = Integer.valueOf(period);
/* 21 */     this.asset1sqr = Double.valueOf(0.0D);
/* 22 */     this.asset2sqr = Double.valueOf(0.0D);
/* 23 */     this.asset1sum = Double.valueOf(0.0D);
/* 24 */     this.asset2sum = Double.valueOf(0.0D);
/* 25 */     this.asset1xasset2 = Double.valueOf(0.0D);
/* 26 */     this.asset1ClList = new java.util.LinkedList();
/* 27 */     this.asset2ClList = new java.util.LinkedList();
/*    */   }
/*    */   
/*    */   public void updateCorrel(CandleData dd1, CandleData dd2, Long date) {
/* 31 */     if ((dd1.date.longValue() != date.longValue()) || (dd2.date.longValue() != date.longValue())) return;
/* 32 */     if ((this.asset1Prevcl == null) || (this.asset2Prevcl == null)) {
/* 33 */       this.asset1Prevcl = dd1.cl;
/* 34 */       this.asset2Prevcl = dd2.cl;
/* 35 */       return;
/*    */     }
/*    */     
/* 38 */     Double ret1 = Double.valueOf(dd1.cl.doubleValue() / this.asset1Prevcl.doubleValue() - 1.0D);
/* 39 */     Double ret2 = Double.valueOf(dd2.cl.doubleValue() / this.asset2Prevcl.doubleValue() - 1.0D);
/* 40 */     this.asset1ClList.add(ret1);
/* 41 */     this.asset2ClList.add(ret2);
/* 42 */     this.asset1sqr = Double.valueOf(this.asset1sqr.doubleValue() + ret1.doubleValue() * ret1.doubleValue());
/* 43 */     this.asset2sqr = Double.valueOf(this.asset2sqr.doubleValue() + ret2.doubleValue() * ret2.doubleValue());
/* 44 */     this.asset1sum = Double.valueOf(this.asset1sum.doubleValue() + ret1.doubleValue());
/* 45 */     this.asset2sum = Double.valueOf(this.asset2sum.doubleValue() + ret2.doubleValue());
/* 46 */     this.asset1xasset2 = Double.valueOf(this.asset1xasset2.doubleValue() + ret1.doubleValue() * ret2.doubleValue());
/* 47 */     this.asset1Prevcl = dd1.cl;
/* 48 */     this.asset2Prevcl = dd2.cl;
/*    */     
/* 50 */     if (this.asset1ClList.size() > this.period.intValue()) {
/* 51 */       ret1 = (Double)this.asset1ClList.getFirst();
/* 52 */       ret2 = (Double)this.asset2ClList.getFirst();
/* 53 */       this.asset1sqr = Double.valueOf(this.asset1sqr.doubleValue() - ret1.doubleValue() * ret1.doubleValue());
/* 54 */       this.asset2sqr = Double.valueOf(this.asset2sqr.doubleValue() - ret2.doubleValue() * ret2.doubleValue());
/* 55 */       this.asset1sum = Double.valueOf(this.asset1sum.doubleValue() - ret1.doubleValue());
/* 56 */       this.asset2sum = Double.valueOf(this.asset2sum.doubleValue() - ret2.doubleValue());
/* 57 */       this.asset1xasset2 = Double.valueOf(this.asset1xasset2.doubleValue() - ret1.doubleValue() * ret2.doubleValue());
/* 58 */       this.asset1ClList.removeFirst();
/* 59 */       this.asset2ClList.removeFirst();
/*    */     }
/*    */     
/* 62 */     if (dd1.isRoll.booleanValue())
/* 63 */       this.asset1Prevcl = Double.valueOf(this.asset1Prevcl.doubleValue() + dd1.rollOver.doubleValue());
/* 64 */     if (dd2.isRoll.booleanValue())
/* 65 */       this.asset2Prevcl = Double.valueOf(this.asset2Prevcl.doubleValue() + dd2.rollOver.doubleValue());
/*    */   }
/*    */   
/*    */   public Double getVal() {
/* 69 */     int count = this.asset1ClList.size();
/* 70 */     if ((this.asset1sqr.doubleValue() == 0.0D) || (count == 1))
/* 71 */       return Double.valueOf(0.0D);
/* 72 */     return Double.valueOf(Math.abs((count * this.asset1xasset2.doubleValue() - this.asset1sum.doubleValue() * this.asset2sum.doubleValue()) / 
/* 73 */       Math.sqrt((count * this.asset1sqr.doubleValue() - this.asset1sum.doubleValue() * this.asset1sum.doubleValue()) * (
/* 74 */       count * this.asset2sqr.doubleValue() - this.asset2sum.doubleValue() * this.asset2sum.doubleValue()))));
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/Correl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */