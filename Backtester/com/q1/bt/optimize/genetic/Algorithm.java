package com.q1.bt.optimize.genetic;

import java.util.ArrayList;





public class Algorithm
{
  private static final double uniformRate = 0.5D;
  private static final double mutationRate = 0.015D;
  private static final int tournamentSize = 5;
  private static final boolean elitism = true;
  
  public static Population evolvePopulation(Population pop, ArrayList<Parameter> paramList)
  {
    Population newPopulation = new Population(pop.size(), false, paramList);
    


    newPopulation.saveIndividual(0, pop.getFittest());
    




    int elitismOffset = 1;
    




    for (int i = elitismOffset; i < pop.size(); i++) {
      ParameterSet indiv1 = tournamentSelection(pop, paramList);
      ParameterSet indiv2 = tournamentSelection(pop, paramList);
      ParameterSet newIndiv = crossover(indiv1, indiv2, paramList);
      newPopulation.saveIndividual(i, newIndiv);
    }
    

    for (int i = elitismOffset; i < newPopulation.size(); i++) {
      mutate(newPopulation.getIndividual(i));
    }
    
    return newPopulation;
  }
  

  private static ParameterSet crossover(ParameterSet indiv1, ParameterSet indiv2, ArrayList<Parameter> paramList)
  {
    ParameterSet newSol = new ParameterSet(paramList);
    newSol.generateParameters();
    
    for (int i = 0; i < indiv1.size(); i++)
    {
      if (Math.random() <= 0.5D) {
        newSol.setGene(i, indiv1.getGene(i));
      } else {
        newSol.setGene(i, indiv2.getGene(i));
      }
    }
    return newSol;
  }
  

  private static void mutate(ParameterSet indiv)
  {
    for (int i = 0; i < indiv.size(); i++) {
      if (Math.random() <= 0.015D)
      {
        byte gene = (byte)(int)Math.round(Math.random());
        indiv.setGene(i, gene);
      }
    }
  }
  


  private static ParameterSet tournamentSelection(Population pop, ArrayList<Parameter> paramList)
  {
    Population tournament = new Population(5, false, paramList);
    
    for (int i = 0; i < 5; i++) {
      int randomId = (int)(Math.random() * pop.size());
      tournament.saveIndividual(i, pop.getIndividual(randomId));
    }
    
    ParameterSet fittest = tournament.getFittest();
    return fittest;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Algorithm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */