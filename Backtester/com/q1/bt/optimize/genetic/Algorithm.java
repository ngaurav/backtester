/*    */ package com.q1.bt.optimize.genetic;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Algorithm
/*    */ {
/*    */   private static final double uniformRate = 0.5D;
/*    */   private static final double mutationRate = 0.015D;
/*    */   private static final int tournamentSize = 5;
/*    */   private static final boolean elitism = true;
/*    */   
/*    */   public static Population evolvePopulation(Population pop, ArrayList<Parameter> paramList)
/*    */   {
/* 18 */     Population newPopulation = new Population(pop.size(), false, paramList);
/*    */     
/*    */ 
/*    */ 
/* 22 */     newPopulation.saveIndividual(0, pop.getFittest());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 28 */     int elitismOffset = 1;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */     for (int i = elitismOffset; i < pop.size(); i++) {
/* 35 */       ParameterSet indiv1 = tournamentSelection(pop, paramList);
/* 36 */       ParameterSet indiv2 = tournamentSelection(pop, paramList);
/* 37 */       ParameterSet newIndiv = crossover(indiv1, indiv2, paramList);
/* 38 */       newPopulation.saveIndividual(i, newIndiv);
/*    */     }
/*    */     
/*    */ 
/* 42 */     for (int i = elitismOffset; i < newPopulation.size(); i++) {
/* 43 */       mutate(newPopulation.getIndividual(i));
/*    */     }
/*    */     
/* 46 */     return newPopulation;
/*    */   }
/*    */   
/*    */ 
/*    */   private static ParameterSet crossover(ParameterSet indiv1, ParameterSet indiv2, ArrayList<Parameter> paramList)
/*    */   {
/* 52 */     ParameterSet newSol = new ParameterSet(paramList);
/* 53 */     newSol.generateParameters();
/*    */     
/* 55 */     for (int i = 0; i < indiv1.size(); i++)
/*    */     {
/* 57 */       if (Math.random() <= 0.5D) {
/* 58 */         newSol.setGene(i, indiv1.getGene(i));
/*    */       } else {
/* 60 */         newSol.setGene(i, indiv2.getGene(i));
/*    */       }
/*    */     }
/* 63 */     return newSol;
/*    */   }
/*    */   
/*    */ 
/*    */   private static void mutate(ParameterSet indiv)
/*    */   {
/* 69 */     for (int i = 0; i < indiv.size(); i++) {
/* 70 */       if (Math.random() <= 0.015D)
/*    */       {
/* 72 */         byte gene = (byte)(int)Math.round(Math.random());
/* 73 */         indiv.setGene(i, gene);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static ParameterSet tournamentSelection(Population pop, ArrayList<Parameter> paramList)
/*    */   {
/* 82 */     Population tournament = new Population(5, false, paramList);
/*    */     
/* 84 */     for (int i = 0; i < 5; i++) {
/* 85 */       int randomId = (int)(Math.random() * pop.size());
/* 86 */       tournament.saveIndividual(i, pop.getIndividual(randomId));
/*    */     }
/*    */     
/* 89 */     ParameterSet fittest = tournament.getFittest();
/* 90 */     return fittest;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Algorithm.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */