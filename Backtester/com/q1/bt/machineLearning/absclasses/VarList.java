/*     */ package com.q1.bt.machineLearning.absclasses;
/*     */ 
/*     */ import com.q1.bt.process.parameter.MachineLearningParameter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public abstract class VarList
/*     */ {
/*     */   private IndUpdateTimeFrame indTF;
/*     */   private boolean resetAtEnd;
/*     */   private ModelEvaluationTime modET;
/*     */   private int frequency;
/*     */   ArrayList<String> varNames;
/*     */   public MachineLearningParameter mlParameter;
/*     */   
/*     */   public VarList()
/*     */   {
/*  19 */     this.mlParameter = createMLParameter();
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract ArrayList<Factor> getFactorList(HashMap<String, CandleIndVar> paramHashMap);
/*     */   
/*     */ 
/*     */   public abstract void populateNormalizerList();
/*     */   
/*     */   public abstract MachineLearningParameter createMLParameter();
/*     */   
/*     */   public abstract HashMap<String, CandleIndVar> getNormalizerList();
/*     */   
/*     */   public void generateFactors(String factorName, ArrayList<Factor> factorList, Integer... paramList)
/*     */     throws Exception
/*     */   {
/*  35 */     ArrayList<Factor> factorListPart = new ArrayList();
/*  36 */     boolean intraday = false;
/*     */     
/*  38 */     if (factorName.substring(factorName.length() - 2).equals("1M")) {
/*  39 */       intraday = true;
/*  40 */       factorName = factorName.substring(0, factorName.length() - 2);
/*     */     }
/*     */     
/*  43 */     Class<?> c = Class.forName(factorName);
/*  44 */     Constructor<?> cons = c.getConstructors()[0];
/*     */     Integer[] arrayOfInteger;
/*  46 */     int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
/*  47 */       String fName; String fName; if (intraday) {
/*  48 */         fName = 
/*  49 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
/*     */       } else {
/*  51 */         fName = 
/*  52 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
/*     */       }
/*  54 */       CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i) });
/*  55 */       factorListPart.add(new Factor(fName, o));
/*     */     }
/*  57 */     factorList.addAll(factorListPart);
/*     */   }
/*     */   
/*     */   public void generateFactors(String Name, ArrayList<Factor> factorList, CandleIndVar c)
/*     */   {
/*  62 */     factorList.add(new Factor(Name, c));
/*     */   }
/*     */   
/*     */   public void generateFactors(String factorName, ArrayList<Factor> factorList, CandleIndVar divFactor, Integer... paramList)
/*     */     throws Exception
/*     */   {
/*  68 */     ArrayList<Factor> factorListPart = new ArrayList();
/*  69 */     boolean intraday = false;
/*     */     
/*  71 */     if (factorName.substring(factorName.length() - 2).equals("1M")) {
/*  72 */       intraday = true;
/*  73 */       factorName = factorName.substring(0, factorName.length() - 2);
/*     */     }
/*     */     
/*  76 */     Class<?> c = Class.forName(factorName);
/*  77 */     Constructor<?> cons = c.getConstructors()[0];
/*     */     Integer[] arrayOfInteger;
/*  79 */     int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
/*  80 */       String fName; String fName; if (intraday) {
/*  81 */         fName = 
/*  82 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
/*     */       } else {
/*  84 */         fName = 
/*  85 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
/*     */       }
/*  87 */       CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i), divFactor });
/*  88 */       factorListPart.add(new Factor(fName, o));
/*     */     }
/*  90 */     factorList.addAll(factorListPart);
/*     */   }
/*     */   
/*     */   public void generateFactors(String factorName, ArrayList<Factor> factorList, CandleIndVar subFactor, CandleIndVar divFactor, Integer... paramList)
/*     */     throws Exception
/*     */   {
/*  96 */     ArrayList<Factor> factorListPart = new ArrayList();
/*  97 */     boolean intraday = false;
/*     */     
/*  99 */     if (factorName.substring(factorName.length() - 2).equals("1M")) {
/* 100 */       intraday = true;
/* 101 */       factorName = factorName.substring(0, factorName.length() - 2);
/*     */     }
/*     */     
/* 104 */     Class<?> c = Class.forName(factorName);
/* 105 */     Constructor<?> cons = c.getConstructors()[0];
/*     */     Integer[] arrayOfInteger;
/* 107 */     int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
/* 108 */       String fName; String fName; if (intraday) {
/* 109 */         fName = 
/* 110 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
/*     */       } else {
/* 112 */         fName = 
/* 113 */           factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
/*     */       }
/* 115 */       CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i), subFactor, 
/* 116 */         divFactor });
/* 117 */       factorListPart.add(new Factor(fName, o));
/*     */     }
/* 119 */     factorList.addAll(factorListPart);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setVarNames(ArrayList<Factor> factorList)
/*     */   {
/* 125 */     this.varNames = new ArrayList();
/* 126 */     for (Factor factor : factorList) {
/* 127 */       this.varNames.add(factor.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<String> getVarNames() {
/* 132 */     return this.varNames;
/*     */   }
/*     */   
/*     */   public void setIndUpdateTimeFrame(IndUpdateTimeFrame indTFUser, boolean resetAtEnd)
/*     */   {
/* 137 */     this.indTF = indTFUser;
/* 138 */     this.resetAtEnd = resetAtEnd;
/*     */   }
/*     */   
/*     */   public void setModelEvaluationTime(ModelEvaluationTime modEtUser, int frequency)
/*     */   {
/* 143 */     this.modET = modEtUser;
/* 144 */     this.frequency = frequency;
/*     */   }
/*     */   
/*     */   public IndUpdateCriterion getIndUpdateTimeFrame() {
/* 148 */     IndUpdateCriterion iuc = new IndUpdateCriterion(this.indTF, this.resetAtEnd);
/* 149 */     return iuc;
/*     */   }
/*     */   
/*     */   public ModelEvaluationCriterion getModelEvalTime() {
/* 153 */     ModelEvaluationCriterion mec = new ModelEvaluationCriterion(this.modET, 
/* 154 */       this.frequency);
/* 155 */     return mec;
/*     */   }
/*     */   
/*     */   public static enum IndUpdateTimeFrame
/*     */   {
/* 160 */     FULL_DAY,  SESSION1,  SESSION2,  SESSION3;
/*     */   }
/*     */   
/*     */   public static enum ModelEvaluationTime
/*     */   {
/* 165 */     FREQUENCY,  SESSION1_START,  SESSION2_START,  SESSION3_START,  DAY_START,  ALL_SESSION_START;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public class IndUpdateCriterion
/*     */   {
/*     */     public VarList.IndUpdateTimeFrame iut;
/*     */     
/*     */ 
/*     */ 
/*     */     public boolean reset;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public IndUpdateCriterion(VarList.IndUpdateTimeFrame iut, boolean reset)
/*     */     {
/* 185 */       this.iut = iut;
/* 186 */       this.reset = reset;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ModelEvaluationCriterion {
/*     */     public VarList.ModelEvaluationTime met;
/*     */     public int frequency;
/*     */     
/*     */     public ModelEvaluationCriterion(VarList.ModelEvaluationTime met, int frequency) {
/* 195 */       this.met = met;
/* 196 */       this.frequency = frequency;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setMLParameter(MachineLearningParameter mlParameter) {
/* 201 */     this.mlParameter = mlParameter;
/*     */   }
/*     */   
/*     */   public MachineLearningParameter getMLParameter() {
/* 205 */     return this.mlParameter;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/VarList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */