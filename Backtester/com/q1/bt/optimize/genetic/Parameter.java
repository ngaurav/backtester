package com.q1.bt.optimize.genetic;

public class Parameter
{
  Double startVal;
  Double step;
  Double maxVal;
  int binaryLength;
  
  public Parameter(Double startVal, Double step, Double maxVal) {
    this.startVal = startVal;
    this.step = step;
    this.maxVal = maxVal;
    int noOfValues = (int)((maxVal.doubleValue() - startVal.doubleValue()) / step.doubleValue());
    this.binaryLength = Integer.toBinaryString(noOfValues).length();
  }
  
  public byte[] generateValue()
  {
    byte[] gene = new byte[this.binaryLength];
    for (int i = 0; i < this.binaryLength; i++)
      gene[i] = ((byte)(int)Math.round(Math.random()));
    return gene;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Parameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */