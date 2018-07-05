/*    */ package com.q1.bt.postprocess;
/*    */ 
/*    */ 
/*    */ public class PostProcessData
/*    */ {
/*  6 */   public String scripID = null;
/*    */   public String outputName;
/*    */   OutputShape outputShape;
/*    */   Integer outputPanel;
/*    */   public String outputValue;
/*    */   
/*    */   public PostProcessData(String scripID, String outputName, OutputShape outputShape, int outputPanel)
/*    */   {
/* 14 */     this.scripID = scripID;
/* 15 */     this.outputName = outputName;
/* 16 */     this.outputShape = outputShape;
/* 17 */     this.outputPanel = Integer.valueOf(outputPanel);
/* 18 */     updateValue(Double.valueOf(NaN.0D));
/*    */   }
/*    */   
/*    */   public PostProcessData(String outputName, OutputShape outputShape, int outputPanel)
/*    */   {
/* 23 */     this.outputName = outputName;
/* 24 */     this.outputShape = outputShape;
/* 25 */     this.outputPanel = Integer.valueOf(outputPanel);
/* 26 */     updateValue(Double.valueOf(NaN.0D));
/*    */   }
/*    */   
/*    */   public String getFileHeader()
/*    */   {
/* 31 */     return this.outputName + " " + this.outputShape.toString() + " " + this.outputPanel.toString();
/*    */   }
/*    */   
/*    */   public void updateValue(Object outputValue)
/*    */   {
/* 36 */     if (outputValue == null)
/* 37 */       outputValue = Double.valueOf(NaN.0D);
/* 38 */     this.outputValue = outputValue.toString();
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/PostProcessData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */