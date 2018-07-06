package com.q1.bt.data.classes;

import java.io.Serializable;

public class Contract implements Serializable { private static final long serialVersionUID = 1L;
  public Double op;
  public Double hi;
  public Double lo;
  public Double cl;
  public Double vol;
  public Double oi;
  public Double totalVol; public Double totalOI; public Double currency = Double.valueOf(1.0D);
  
  public Integer exp;
  public Integer actualExp;
  public Integer rolloverExp;
  public Double rolloverCl;
  public Double bid;
  
  public Contract(Contract c)
  {
    this.op = c.op;
    this.hi = c.hi;
    this.lo = c.lo;
    this.cl = c.cl;
    this.vol = c.vol;
    this.oi = c.oi;
    this.exp = c.exp;
    this.actualExp = c.actualExp;
    this.rolloverExp = c.rolloverExp;
    this.rolloverCl = c.rolloverCl;
    this.totalVol = c.totalVol;
    this.totalOI = c.totalOI;
    this.currency = c.currency;
    this.bid = c.bid;
    this.ask = c.ask;
    this.bidQty = c.bidQty;
    this.askQty = c.askQty;
    this.type = c.type;
    this.optionType = c.optionType;
    this.optionStrike = c.optionStrike;
  }
  
  public Double ask;
  public Double bidQty;
  public Double askQty;
  public String type;
  public String optionType;
  public Double optionStrike;
  public Contract() {}
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/data/classes/Contract.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */