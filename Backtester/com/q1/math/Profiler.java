/*    */ package com.q1.math;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Profiler {
/*    */   public long startTick;
/*    */   public Double cumTick;
/*    */   
/*  9 */   public Profiler() { this.cumTick = Double.valueOf(0.0D); }
/*    */   
/*    */   public void start()
/*    */   {
/* 13 */     this.startTick = System.nanoTime();
/*    */   }
/*    */   
/*    */   public void stop()
/*    */   {
/* 18 */     this.cumTick = Double.valueOf(this.cumTick.doubleValue() + (System.nanoTime() - this.startTick) / 1.0E9D);
/*    */   }
/*    */   
/*    */   public void printStats() {
/* 22 */     System.out.println("Execution Time: " + this.cumTick + " seconds");
/*    */   }
/*    */   
/*    */   public Double getStats() {
/* 26 */     return Double.valueOf(MathLib.roundTick(this.cumTick.doubleValue(), 0.01D));
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/Profiler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */