/*    */ package com.q1.bt.optimize.genetic;
/*    */ 
/*    */ import com.q1.math.MathLib;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ public class ParameterSet
/*    */ {
/* 10 */   ArrayList<Byte> geneList = new ArrayList();
/* 11 */   private double fitness = 0.0D;
/* 12 */   ArrayList<Parameter> paramList = new ArrayList();
/*    */   
/*    */   public ParameterSet(ArrayList<Parameter> paramList)
/*    */   {
/* 16 */     this.paramList = paramList;
/*    */   }
/*    */   
/*    */ 
/*    */   public void generateParameters()
/*    */   {
/* 22 */     this.geneList = new ArrayList();
/* 23 */     int j; int i; for (Iterator localIterator = this.paramList.iterator(); localIterator.hasNext(); 
/*    */         
/* 25 */         i < j)
/*    */     {
/* 23 */       Parameter p = (Parameter)localIterator.next();
/* 24 */       byte[] value = p.generateValue();
/* 25 */       byte[] arrayOfByte1; j = (arrayOfByte1 = value).length;i = 0; continue;byte b = arrayOfByte1[i];
/* 26 */       this.geneList.add(Byte.valueOf(b));i++;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public byte getGene(int index)
/*    */   {
/* 33 */     return ((Byte)this.geneList.get(index)).byteValue();
/*    */   }
/*    */   
/*    */   public void setGene(int index, byte value) {
/* 37 */     this.geneList.set(index, Byte.valueOf(value));
/* 38 */     this.fitness = 0.0D;
/*    */   }
/*    */   
/*    */   public int size()
/*    */   {
/* 43 */     return this.geneList.size();
/*    */   }
/*    */   
/*    */   public Double getFitness() {
/* 47 */     if (MathLib.doubleCompare(Double.valueOf(this.fitness), Double.valueOf(0.0D)).intValue() == 0) {
/* 48 */       this.fitness = FitnessFunction.getFitness(this).doubleValue();
/*    */     }
/* 50 */     return Double.valueOf(this.fitness);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 55 */     String geneString = "";
/* 56 */     for (int i = 0; i < size(); i++) {
/* 57 */       geneString = geneString + getGene(i);
/*    */     }
/* 59 */     return geneString;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/optimize/genetic/ParameterSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */