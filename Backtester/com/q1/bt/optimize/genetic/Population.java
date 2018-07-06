package com.q1.bt.optimize.genetic;

import com.q1.math.MathLib;
import java.util.ArrayList;








public class Population
{
  ParameterSet[] individuals;
  
  public Population(int populationSize, boolean initialise, ArrayList<Parameter> paramList)
  {
    this.individuals = new ParameterSet[populationSize];
    

    if (initialise)
    {
      for (int i = 0; i < size(); i++) {
        ParameterSet pS = new ParameterSet(paramList);
        pS.generateParameters();
        saveIndividual(i, pS);
      }
    }
  }
  

  public ParameterSet getIndividual(int index)
  {
    return this.individuals[index];
  }
  
  public ParameterSet getFittest() {
    ParameterSet fittest = this.individuals[0];
    
    for (int i = 0; i < size(); i++) {
      Double curFitness = fittest.getFitness();
      Double indFitness = getIndividual(i).getFitness();
      if (MathLib.doubleCompare(indFitness, curFitness).intValue() > 0) {
        fittest = getIndividual(i);
      }
    }
    return fittest;
  }
  

  public int size()
  {
    return this.individuals.length;
  }
  
  public void saveIndividual(int index, ParameterSet indiv)
  {
    this.individuals[index] = indiv;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Population.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */