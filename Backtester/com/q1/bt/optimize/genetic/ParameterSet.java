package com.q1.bt.optimize.genetic;

import com.q1.math.MathLib;
import java.util.ArrayList;
import java.util.Iterator;


public class ParameterSet
{
  ArrayList<Byte> geneList = new ArrayList();
  private double fitness = 0.0D;
  ArrayList<Parameter> paramList = new ArrayList();
  
  public ParameterSet(ArrayList<Parameter> paramList)
  {
    this.paramList = paramList;
  }
  

  public void generateParameters()
  {
    this.geneList = new ArrayList();
    int j; int i; for (Iterator localIterator = this.paramList.iterator(); localIterator.hasNext(); 
        
        i < j)
    {
      Parameter p = (Parameter)localIterator.next();
      byte[] value = p.generateValue();
      byte[] arrayOfByte1; j = (arrayOfByte1 = value).length;i = 0; continue;byte b = arrayOfByte1[i];
      this.geneList.add(Byte.valueOf(b));i++;
    }
  }
  


  public byte getGene(int index)
  {
    return ((Byte)this.geneList.get(index)).byteValue();
  }
  
  public void setGene(int index, byte value) {
    this.geneList.set(index, Byte.valueOf(value));
    this.fitness = 0.0D;
  }
  
  public int size()
  {
    return this.geneList.size();
  }
  
  public Double getFitness() {
    if (MathLib.doubleCompare(Double.valueOf(this.fitness), Double.valueOf(0.0D)).intValue() == 0) {
      this.fitness = FitnessFunction.getFitness(this).doubleValue();
    }
    return Double.valueOf(this.fitness);
  }
  
  public String toString()
  {
    String geneString = "";
    for (int i = 0; i < size(); i++) {
      geneString = geneString + getGene(i);
    }
    return geneString;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/ParameterSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */