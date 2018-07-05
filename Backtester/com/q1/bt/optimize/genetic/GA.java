/*    */ package com.q1.bt.optimize.genetic;
/*    */ 
/*    */ import com.q1.math.MathLib;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class GA
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 11 */     Double fitnessThresh = Double.valueOf(3.0D);
/*    */     
/* 13 */     ArrayList<Parameter> paramList = new ArrayList();
/*    */     
/* 15 */     Parameter p1 = new Parameter(Double.valueOf(0.0D), Double.valueOf(1.0D), Double.valueOf(21.0D));
/* 16 */     Parameter p2 = new Parameter(Double.valueOf(0.0D), Double.valueOf(0.5D), Double.valueOf(11.0D));
/* 17 */     paramList.add(p1);
/* 18 */     paramList.add(p2);
/*    */     
/*    */ 
/* 21 */     Population myPop = new Population(50, true, paramList);
/*    */     
/*    */ 
/* 24 */     int generationCount = 0;
/* 25 */     while (MathLib.doubleCompare(myPop.getFittest().getFitness(), fitnessThresh).intValue() < 0) {
/* 26 */       generationCount++;
/* 27 */       System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
/* 28 */       myPop = Algorithm.evolvePopulation(myPop, paramList);
/*    */     }
/* 30 */     System.out.println("Solution found!");
/* 31 */     System.out.println("Generation: " + generationCount);
/* 32 */     System.out.println("Genes:");
/* 33 */     System.out.println(myPop.getFittest());
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/GA.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */