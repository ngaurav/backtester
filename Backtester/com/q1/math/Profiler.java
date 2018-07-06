package com.q1.math;

import java.io.PrintStream;

public class Profiler {
  public long startTick;
  public Double cumTick;
  
  public Profiler() { this.cumTick = Double.valueOf(0.0D); }
  
  public void start()
  {
    this.startTick = System.nanoTime();
  }
  
  public void stop() {
    this.cumTick = Double.valueOf(this.cumTick.doubleValue() + (System.nanoTime() - this.startTick) / 
      1.0E9D);
  }
  
  public void printStats() {
    System.out.println("Execution Time: " + this.cumTick + " seconds");
  }
  
  public Double getStats() {
    return Double.valueOf(MathLib.roundTick(this.cumTick.doubleValue(), 0.01D));
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/math/Profiler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */