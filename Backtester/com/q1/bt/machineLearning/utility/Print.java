package com.q1.bt.machineLearning.utility;

import java.util.ArrayList;

public class Print {
  public static void print1dArray(int[] Array) {
    for (int i = 0; i < Array.length; i++) {
      System.out.print("," + Array[i]);
    }
    System.out.println();
  }
  
  public static void print1dArray(double[] Array)
  {
    for (int i = 0; i < Array.length; i++) {
      System.out.print("," + Array[i]);
    }
    System.out.println();
  }
  
  public static void print1dArray(String[] Array)
  {
    for (int i = 0; i < Array.length; i++) {
      System.out.print("," + Array[i]);
    }
    System.out.println();
  }
  
  public static void printMat(ArrayList<ArrayList<Double>> covarMat)
  {
    int row = covarMat.size();
    
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < ((ArrayList)covarMat.get(i)).size(); j++) {
        System.out.print("," + ((ArrayList)covarMat.get(i)).get(j));
      }
      System.out.println();
    }
  }
  
  public static void printMat(double[][] covarMat) { int row = covarMat.length;
    int col = covarMat[0].length;
    
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        System.out.print("," + covarMat[i][j]);
      }
      System.out.println();
    }
  }
  
  public static void print2DArrWithHeaders(ArrayList<Long> colNames, ArrayList<String> rowNames, ArrayList<ArrayList<Double>> elements)
  {
    for (Object o : colNames) {
      System.out.print("," + o.toString());
    }
    System.out.println();
    for (int i = 0; i < elements.size(); i++) {
      System.out.print((String)rowNames.get(i));
      for (int j = 0; j < ((ArrayList)elements.get(i)).size(); j++) {
        if (((ArrayList)elements.get(i)).get(j) == null) {
          System.out.print(",null");
        } else
          System.out.print("," + ((Double)((ArrayList)elements.get(i)).get(j)).toString());
      }
      System.out.println();
    }
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/utility/Print.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */