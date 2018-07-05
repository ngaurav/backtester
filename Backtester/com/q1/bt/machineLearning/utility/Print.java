/*    */ package com.q1.bt.machineLearning.utility;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class Print {
/*    */   public static void print1dArray(int[] Array) {
/*  7 */     for (int i = 0; i < Array.length; i++) {
/*  8 */       System.out.print("," + Array[i]);
/*    */     }
/* 10 */     System.out.println();
/*    */   }
/*    */   
/*    */   public static void print1dArray(double[] Array)
/*    */   {
/* 15 */     for (int i = 0; i < Array.length; i++) {
/* 16 */       System.out.print("," + Array[i]);
/*    */     }
/* 18 */     System.out.println();
/*    */   }
/*    */   
/*    */   public static void print1dArray(String[] Array)
/*    */   {
/* 23 */     for (int i = 0; i < Array.length; i++) {
/* 24 */       System.out.print("," + Array[i]);
/*    */     }
/* 26 */     System.out.println();
/*    */   }
/*    */   
/*    */   public static void printMat(ArrayList<ArrayList<Double>> covarMat)
/*    */   {
/* 31 */     int row = covarMat.size();
/*    */     
/* 33 */     for (int i = 0; i < row; i++) {
/* 34 */       for (int j = 0; j < ((ArrayList)covarMat.get(i)).size(); j++) {
/* 35 */         System.out.print("," + ((ArrayList)covarMat.get(i)).get(j));
/*    */       }
/* 37 */       System.out.println();
/*    */     }
/*    */   }
/*    */   
/* 41 */   public static void printMat(double[][] covarMat) { int row = covarMat.length;
/* 42 */     int col = covarMat[0].length;
/*    */     
/* 44 */     for (int i = 0; i < row; i++) {
/* 45 */       for (int j = 0; j < col; j++) {
/* 46 */         System.out.print("," + covarMat[i][j]);
/*    */       }
/* 48 */       System.out.println();
/*    */     }
/*    */   }
/*    */   
/*    */   public static void print2DArrWithHeaders(ArrayList<Long> colNames, ArrayList<String> rowNames, ArrayList<ArrayList<Double>> elements)
/*    */   {
/* 54 */     for (Object o : colNames) {
/* 55 */       System.out.print("," + o.toString());
/*    */     }
/* 57 */     System.out.println();
/* 58 */     for (int i = 0; i < elements.size(); i++) {
/* 59 */       System.out.print((String)rowNames.get(i));
/* 60 */       for (int j = 0; j < ((ArrayList)elements.get(i)).size(); j++) {
/* 61 */         if (((ArrayList)elements.get(i)).get(j) == null) {
/* 62 */           System.out.print(",null");
/*    */         } else
/* 64 */           System.out.print("," + ((Double)((ArrayList)elements.get(i)).get(j)).toString());
/*    */       }
/* 66 */       System.out.println();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/Print.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */