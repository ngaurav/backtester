package com.q1.bt.optimize.genetic;

import com.q1.math.MathLib;
import java.io.PrintStream;
import java.util.ArrayList;

public class GA
{
  public static void main(String[] args)
  {
    Double fitnessThresh = Double.valueOf(3.0D);
    
    ArrayList<Parameter> paramList = new ArrayList();
    
    Parameter p1 = new Parameter(Double.valueOf(0.0D), Double.valueOf(1.0D), Double.valueOf(21.0D));
    Parameter p2 = new Parameter(Double.valueOf(0.0D), Double.valueOf(0.5D), Double.valueOf(11.0D));
    paramList.add(p1);
    paramList.add(p2);
    

    Population myPop = new Population(50, true, paramList);
    

    int generationCount = 0;
    while (MathLib.doubleCompare(myPop.getFittest().getFitness(), fitnessThresh).intValue() < 0) {
      generationCount++;
      System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
      myPop = Algorithm.evolvePopulation(myPop, paramList);
    }
    System.out.println("Solution found!");
    System.out.println("Generation: " + generationCount);
    System.out.println("Genes:");
    System.out.println(myPop.getFittest());
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/GA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */