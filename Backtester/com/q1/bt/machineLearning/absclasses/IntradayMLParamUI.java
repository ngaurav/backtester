/*     */ package com.q1.bt.machineLearning.absclasses;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JTextField;
/*     */ 
/*     */ 
/*     */ public class IntradayMLParamUI
/*     */ {
/*     */   String mlAlgoName;
/*     */   String mlAlgoPath;
/*  14 */   ArrayList<String> paramVals = new ArrayList();
/*  15 */   ArrayList<String[]> parameterList = new ArrayList();
/*     */   boolean defaultCheck;
/*     */   
/*     */   public IntradayMLParamUI(IntradayMLParamUI pUI)
/*     */   {
/*  20 */     this.mlAlgoName = pUI.mlAlgoName;
/*  21 */     this.mlAlgoPath = pUI.mlAlgoPath;
/*  22 */     this.paramVals = new ArrayList(pUI.paramVals);
/*  23 */     this.parameterList = new ArrayList(pUI.parameterList);
/*  24 */     this.defaultCheck = pUI.defaultCheck;
/*     */   }
/*     */   
/*     */   public IntradayMLParamUI(String mlAlgoName, String mlAlgoPath, boolean defaultCheck)
/*     */   {
/*  29 */     this.mlAlgoName = mlAlgoName;
/*  30 */     this.mlAlgoPath = mlAlgoPath;
/*  31 */     this.defaultCheck = defaultCheck;
/*     */   }
/*     */   
/*     */   public IntradayMLParamUI(String mlAlgoName, String mlAlgoPath, ArrayList<String[]> parameterList)
/*     */   {
/*  36 */     this.mlAlgoName = mlAlgoName;
/*  37 */     this.mlAlgoPath = mlAlgoPath;
/*  38 */     this.parameterList = parameterList;
/*     */   }
/*     */   
/*     */   public void getParameters()
/*     */     throws Exception
/*     */   {
/*  44 */     System.out.println(this.mlAlgoPath + "." + this.mlAlgoName);
/*  45 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/*  46 */     Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/*  47 */     MLAlgo stratTempObject = (MLAlgo)stratTempConstructor.newInstance(new Object[0]);
/*     */     
/*  49 */     this.parameterList = stratTempObject.getParameterList();
/*  50 */     ArrayList<JTextField> paramFieldList = new ArrayList();
/*  51 */     ArrayList<Object> messageList = new ArrayList();
/*     */     
/*  53 */     if (this.defaultCheck) {
/*  54 */       for (String[] param : this.parameterList) {
/*  55 */         this.paramVals.add(param[1]);
/*     */       }
/*     */     }
/*     */     else {
/*  59 */       for (String[] param : this.parameterList)
/*     */       {
/*  61 */         JTextField newParam = new JTextField(param[1]);
/*  62 */         paramFieldList.add(newParam);
/*     */         
/*  64 */         messageList.add(param[0] + ":");
/*  65 */         messageList.add(newParam);
/*     */       }
/*  67 */       Object[] message = new Object[messageList.size()];
/*  68 */       for (int i = 0; i < messageList.size(); i++) {
/*  69 */         message[i] = messageList.get(i);
/*     */       }
/*  71 */       int option = JOptionPane.showConfirmDialog(null, message, 
/*  72 */         this.mlAlgoName + " - Parameters", 2);
/*     */       
/*     */ 
/*  75 */       if (option == 0) {
/*  76 */         for (JTextField param : paramFieldList) {
/*  77 */           this.paramVals.add(param.getText());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void getParametersR()
/*     */     throws Exception
/*     */   {
/*  86 */     System.out.println(this.mlAlgoPath + "." + this.mlAlgoName);
/*  87 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/*  88 */     Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/*  89 */     MLAlgoR stratTempObject = (MLAlgoR)stratTempConstructor.newInstance(new Object[0]);
/*     */     
/*  91 */     this.parameterList = stratTempObject.getParameterList();
/*  92 */     ArrayList<JTextField> paramFieldList = new ArrayList();
/*  93 */     ArrayList<Object> messageList = new ArrayList();
/*     */     
/*  95 */     if (this.defaultCheck) {
/*  96 */       for (String[] param : this.parameterList) {
/*  97 */         this.paramVals.add(param[1]);
/*     */       }
/*     */     }
/*     */     else {
/* 101 */       for (String[] param : this.parameterList)
/*     */       {
/* 103 */         JTextField newParam = new JTextField(param[1]);
/* 104 */         paramFieldList.add(newParam);
/*     */         
/* 106 */         messageList.add(param[0] + ":");
/* 107 */         messageList.add(newParam);
/*     */       }
/* 109 */       Object[] message = new Object[messageList.size()];
/* 110 */       for (int i = 0; i < messageList.size(); i++) {
/* 111 */         message[i] = messageList.get(i);
/*     */       }
/* 113 */       int option = JOptionPane.showConfirmDialog(null, message, 
/* 114 */         this.mlAlgoName + " - Parameters", 2);
/*     */       
/*     */ 
/* 117 */       if (option == 0) {
/* 118 */         for (JTextField param : paramFieldList) {
/* 119 */           this.paramVals.add(param.getText());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<String[]> printParameters() {
/* 126 */     if (this.defaultCheck) {
/* 127 */       return this.parameterList;
/*     */     }
/* 129 */     ArrayList<String[]> newParamList = new ArrayList();
/* 130 */     for (int i = 0; i < this.parameterList.size(); i++) {
/* 131 */       String[] defParam = (String[])this.parameterList.get(i);
/* 132 */       String[] outLine = { defParam[0], (String)this.paramVals.get(i) };
/* 133 */       newParamList.add(outLine);
/*     */     }
/* 135 */     return newParamList;
/*     */   }
/*     */   
/*     */ 
/*     */   public MLAlgo getMLAlgoInstance()
/*     */     throws Exception
/*     */   {
/* 142 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/* 143 */     Constructor<?> stratConstructor = stratClass
/* 144 */       .getConstructor(new Class[] {ArrayList.class });
/* 145 */     for (String[] param : this.parameterList) {
/* 146 */       this.paramVals.add(param[1]);
/*     */     }
/* 148 */     MLAlgo stratObject = 
/* 149 */       (MLAlgo)stratConstructor.newInstance(new Object[] { this.paramVals });
/* 150 */     return stratObject;
/*     */   }
/*     */   
/*     */   public MLAlgoR getMLAlgoRInstance()
/*     */     throws Exception
/*     */   {
/* 156 */     Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
/* 157 */     Constructor<?> stratConstructor = stratClass
/* 158 */       .getConstructor(new Class[] {ArrayList.class });
/* 159 */     for (String[] param : this.parameterList) {
/* 160 */       this.paramVals.add(param[1]);
/*     */     }
/* 162 */     MLAlgoR stratObject = 
/* 163 */       (MLAlgoR)stratConstructor.newInstance(new Object[] { this.paramVals });
/* 164 */     return stratObject;
/*     */   }
/*     */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/IntradayMLParamUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */