/*    */ package com.q1.bt.gui.main;
/*    */ 
/*    */ import com.q1.bt.execution.Strategy;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.ArrayList;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JTextField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParamUI
/*    */ {
/*    */   String strategyName;
/*    */   String strategyPackage;
/*    */   
/*    */   public ParamUI(String strategyName, String strategyPackage)
/*    */   {
/* 19 */     this.strategyName = strategyName;
/* 20 */     this.strategyPackage = strategyPackage;
/*    */   }
/*    */   
/*    */ 
/*    */   public ArrayList<String[]> getParameters()
/*    */     throws Exception
/*    */   {
/* 27 */     Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyName);
/* 28 */     Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
/* 29 */     Strategy stratTempObject = (Strategy)stratTempConstructor.newInstance(new Object[0]);
/*    */     
/*    */ 
/* 32 */     ArrayList<String[]> parameterList = stratTempObject.getParameterList();
/* 33 */     ArrayList<JTextField> paramFieldList = new ArrayList();
/* 34 */     ArrayList<Object> messageList = new ArrayList();
/*    */     
/*    */ 
/* 37 */     for (String[] param : parameterList)
/*    */     {
/* 39 */       JTextField newParam = new JTextField(param[1]);
/* 40 */       paramFieldList.add(newParam);
/*    */       
/* 42 */       messageList.add(param[0] + ":");
/* 43 */       messageList.add(newParam);
/*    */     }
/* 45 */     Object[] message = new Object[messageList.size()];
/* 46 */     for (int i = 0; i < messageList.size(); i++) {
/* 47 */       message[i] = messageList.get(i);
/*    */     }
/* 49 */     int option = JOptionPane.showConfirmDialog(null, message, this.strategyName + " - Parameters", 
/* 50 */       2);
/*    */     
/*    */ 
/* 53 */     ArrayList<String> paramVals = new ArrayList();
/* 54 */     if (option == 0) {
/* 55 */       for (JTextField param : paramFieldList) {
/* 56 */         paramVals.add(param.getText());
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 61 */     ArrayList<String[]> newParamList = new ArrayList();
/* 62 */     for (int i = 0; i < parameterList.size(); i++) {
/* 63 */       String[] defParam = (String[])parameterList.get(i);
/* 64 */       String[] outLine = { defParam[0], (String)paramVals.get(i) };
/* 65 */       newParamList.add(outLine);
/*    */     }
/* 67 */     return newParamList;
/*    */   }
/*    */   
/*    */   public String getStrategyName()
/*    */   {
/* 72 */     return this.strategyName;
/*    */   }
/*    */ }


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/ParamUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */