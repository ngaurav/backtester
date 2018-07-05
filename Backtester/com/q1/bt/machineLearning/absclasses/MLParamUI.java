/*     */ package com.q1.bt.machineLearning.absclasses;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MLParamUI
/*     */ {
/*     */   String mlAlgoName;
/*     */   String mlAlgoPath;
/*  14 */   ArrayList<String> paramVals = new ArrayList();
/*  15 */   ArrayList<String[]> parameterList = new ArrayList();
/*     */   boolean defaultCheck;
/*     */   
/*     */   public MLParamUI(MLParamUI pUI)
/*     */   {
/*  20 */     this.mlAlgoName = pUI.mlAlgoName;
/*  21 */     this.mlAlgoPath = pUI.mlAlgoPath;
/*  22 */     this.paramVals = new ArrayList(pUI.paramVals);
/*  23 */     this.parameterList = new ArrayList(pUI.parameterList);
/*  24 */     this.defaultCheck = pUI.defaultCheck;
/*     */   }
/*     */   
/*     */   public MLParamUI(String mlAlgoName, String mlAlgoPath, boolean defaultCheck)
/*     */   {
/*  29 */     this.mlAlgoName = mlAlgoName;
/*  30 */     this.mlAlgoPath = mlAlgoPath;
/*  31 */     this.defaultCheck = defaultCheck;
/*     */   }
/*     */   
/*     */   public MLParamUI(String mlAlgoName, String mlAlgoPath, ArrayList<String[]> parameterList)
/*     */   {
/*  36 */     this.mlAlgoName = mlAlgoName;
/*  37 */     this.mlAlgoPath = mlAlgoPath;
/*  38 */     this.parameterList = parameterList;
/*     */   }
/*     */   
/*     */   public void getParameters()
/*     */     throws Exception
/*     */   {
/*  44 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/*  45 */     Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/*  46 */     MLAlgo stratTempObject = (MLAlgo)stratTempConstructor.newInstance(new Object[0]);
/*     */     
/*  48 */     this.parameterList = stratTempObject.getParameterList();
/*  49 */     ArrayList<JTextField> paramFieldList = new ArrayList();
/*  50 */     ArrayList<Object> messageList = new ArrayList();
/*     */     
/*  52 */     if (this.defaultCheck) {
/*  53 */       for (String[] param : this.parameterList) {
/*  54 */         this.paramVals.add(param[1]);
/*     */       }
/*     */     }
/*     */     else {
/*  58 */       for (String[] param : this.parameterList)
/*     */       {
/*  60 */         JTextField newParam = new JTextField(param[1]);
/*  61 */         paramFieldList.add(newParam);
/*     */         
/*  63 */         messageList.add(param[0] + ":");
/*  64 */         messageList.add(newParam);
/*     */       }
/*  66 */       Object[] message = new Object[messageList.size()];
/*  67 */       for (int i = 0; i < messageList.size(); i++) {
/*  68 */         message[i] = messageList.get(i);
/*     */       }
/*  70 */       int option = JOptionPane.showConfirmDialog(null, message, 
/*  71 */         this.mlAlgoName + " - Parameters", 2);
/*     */       
/*     */ 
/*  74 */       if (option == 0) {
/*  75 */         for (JTextField param : paramFieldList) {
/*  76 */           this.paramVals.add(param.getText());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayList<String[]> printParameters()
/*     */   {
/*  85 */     if (this.defaultCheck) {
/*  86 */       return this.parameterList;
/*     */     }
/*  88 */     ArrayList<String[]> newParamList = new ArrayList();
/*  89 */     for (int i = 0; i < this.parameterList.size(); i++) {
/*  90 */       String[] defParam = (String[])this.parameterList.get(i);
/*  91 */       String[] outLine = { defParam[0], (String)this.paramVals.get(i) };
/*  92 */       newParamList.add(outLine);
/*     */     }
/*  94 */     return newParamList;
/*     */   }
/*     */   
/*     */ 
/*     */   public MLAlgo getMLAlgoInstance()
/*     */     throws Exception
/*     */   {
/* 101 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/* 102 */     Constructor<?> stratConstructor = stratClass
/* 103 */       .getConstructor(new Class[] {ArrayList.class });
/* 104 */     for (String[] param : this.parameterList) {
/* 105 */       this.paramVals.add(param[1]);
/*     */     }
/* 107 */     MLAlgo stratObject = 
/* 108 */       (MLAlgo)stratConstructor.newInstance(new Object[] { this.paramVals });
/* 109 */     return stratObject;
/*     */   }
/*     */   
/*     */   public MLAlgoR getMLAlgoRInstance()
/*     */     throws Exception
/*     */   {
/* 115 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/* 116 */     Constructor<?> stratConstructor = stratClass
/* 117 */       .getConstructor(new Class[] {ArrayList.class });
/* 118 */     for (String[] param : this.parameterList) {
/* 119 */       this.paramVals.add(param[1]);
/*     */     }
/* 121 */     MLAlgoR stratObject = 
/* 122 */       (MLAlgoR)stratConstructor.newInstance(new Object[] { this.paramVals });
/* 123 */     return stratObject;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/MLParamUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */