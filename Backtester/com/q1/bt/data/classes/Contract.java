/*    */ package com.q1.bt.data.classes;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Contract implements Serializable { private static final long serialVersionUID = 1L;
/*    */   public Double op;
/*    */   public Double hi;
/*    */   public Double lo;
/*    */   public Double cl;
/*    */   public Double vol;
/*    */   public Double oi;
/* 12 */   public Double totalVol; public Double totalOI; public Double currency = Double.valueOf(1.0D);
/*    */   
/*    */   public Integer exp;
/*    */   public Integer actualExp;
/*    */   public Integer rolloverExp;
/*    */   public Double rolloverCl;
/*    */   public Double bid;
/*    */   
/*    */   public Contract(Contract c)
/*    */   {
/* 22 */     this.op = c.op;
/* 23 */     this.hi = c.hi;
/* 24 */     this.lo = c.lo;
/* 25 */     this.cl = c.cl;
/* 26 */     this.vol = c.vol;
/* 27 */     this.oi = c.oi;
/* 28 */     this.exp = c.exp;
/* 29 */     this.actualExp = c.actualExp;
/* 30 */     this.rolloverExp = c.rolloverExp;
/* 31 */     this.rolloverCl = c.rolloverCl;
/* 32 */     this.totalVol = c.totalVol;
/* 33 */     this.totalOI = c.totalOI;
/* 34 */     this.currency = c.currency;
/* 35 */     this.bid = c.bid;
/* 36 */     this.ask = c.ask;
/* 37 */     this.bidQty = c.bidQty;
/* 38 */     this.askQty = c.askQty;
/* 39 */     this.type = c.type;
/* 40 */     this.optionType = c.optionType;
/* 41 */     this.optionStrike = c.optionStrike;
/*    */   }
/*    */   
/*    */   public Double ask;
/*    */   public Double bidQty;
/*    */   public Double askQty;
/*    */   public String type;
/*    */   public String optionType;
/*    */   public Double optionStrike;
/*    */   public Contract() {}
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/Contract.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */