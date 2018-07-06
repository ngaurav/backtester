package com.q1.bt.optimize.genetic;

import java.util.Random;



public class FitnessFunction
{
  static Double getFitness(ParameterSet individual)
  {
    Random r = new Random();
    double fitness = 3.00001D * r.nextDouble();
    
    return Double.valueOf(fitness);
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/FitnessFunction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */