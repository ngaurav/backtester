/*    */ package com.q1.bt.optimize.genetic;
/*    */ 
/*    */ import com.q1.math.MathLib;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Population
/*    */ {
/*    */   ParameterSet[] individuals;
/*    */   
/*    */   public Population(int populationSize, boolean initialise, ArrayList<Parameter> paramList)
/*    */   {
/* 19 */     this.individuals = new ParameterSet[populationSize];
/*    */     
/*    */ 
/* 22 */     if (initialise)
/*    */     {
/* 24 */       for (int i = 0; i < size(); i++) {
/* 25 */         ParameterSet pS = new ParameterSet(paramList);
/* 26 */         pS.generateParameters();
/* 27 */         saveIndividual(i, pS);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public ParameterSet getIndividual(int index)
/*    */   {
/* 35 */     return this.individuals[index];
/*    */   }
/*    */   
/*    */   public ParameterSet getFittest() {
/* 39 */     ParameterSet fittest = this.individuals[0];
/*    */     
/* 41 */     for (int i = 0; i < size(); i++) {
/* 42 */       Double curFitness = fittest.getFitness();
/* 43 */       Double indFitness = getIndividual(i).getFitness();
/* 44 */       if (MathLib.doubleCompare(indFitness, curFitness).intValue() > 0) {
/* 45 */         fittest = getIndividual(i);
/*    */       }
/*    */     }
/* 48 */     return fittest;
/*    */   }
/*    */   
/*    */ 
/*    */   public int size()
/*    */   {
/* 54 */     return this.individuals.length;
/*    */   }
/*    */   
/*    */   public void saveIndividual(int index, ParameterSet indiv)
/*    */   {
/* 59 */     this.individuals[index] = indiv;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/Population.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */