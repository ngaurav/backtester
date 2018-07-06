package com.q1.bt.machineLearning.absclasses;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class MLParamUI
{
  String mlAlgoName;
  String mlAlgoPath;
  ArrayList<String> paramVals = new ArrayList();
  ArrayList<String[]> parameterList = new ArrayList();
  boolean defaultCheck;
  
  public MLParamUI(MLParamUI pUI)
  {
    this.mlAlgoName = pUI.mlAlgoName;
    this.mlAlgoPath = pUI.mlAlgoPath;
    this.paramVals = new ArrayList(pUI.paramVals);
    this.parameterList = new ArrayList(pUI.parameterList);
    this.defaultCheck = pUI.defaultCheck;
  }
  
  public MLParamUI(String mlAlgoName, String mlAlgoPath, boolean defaultCheck)
  {
    this.mlAlgoName = mlAlgoName;
    this.mlAlgoPath = mlAlgoPath;
    this.defaultCheck = defaultCheck;
  }
  
  public MLParamUI(String mlAlgoName, String mlAlgoPath, ArrayList<String[]> parameterList)
  {
    this.mlAlgoName = mlAlgoName;
    this.mlAlgoPath = mlAlgoPath;
    this.parameterList = parameterList;
  }
  
  public void getParameters()
    throws Exception
  {
    Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
    Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
    MLAlgo stratTempObject = (MLAlgo)stratTempConstructor.newInstance(new Object[0]);
    
    this.parameterList = stratTempObject.getParameterList();
    ArrayList<JTextField> paramFieldList = new ArrayList();
    ArrayList<Object> messageList = new ArrayList();
    
    if (this.defaultCheck) {
      for (String[] param : this.parameterList) {
        this.paramVals.add(param[1]);
      }
    }
    else {
      for (String[] param : this.parameterList)
      {
        JTextField newParam = new JTextField(param[1]);
        paramFieldList.add(newParam);
        
        messageList.add(param[0] + ":");
        messageList.add(newParam);
      }
      Object[] message = new Object[messageList.size()];
      for (int i = 0; i < messageList.size(); i++) {
        message[i] = messageList.get(i);
      }
      int option = JOptionPane.showConfirmDialog(null, message, 
        this.mlAlgoName + " - Parameters", 2);
      

      if (option == 0) {
        for (JTextField param : paramFieldList) {
          this.paramVals.add(param.getText());
        }
      }
    }
  }
  

  public ArrayList<String[]> printParameters()
  {
    if (this.defaultCheck) {
      return this.parameterList;
    }
    ArrayList<String[]> newParamList = new ArrayList();
    for (int i = 0; i < this.parameterList.size(); i++) {
      String[] defParam = (String[])this.parameterList.get(i);
      String[] outLine = { defParam[0], (String)this.paramVals.get(i) };
      newParamList.add(outLine);
    }
    return newParamList;
  }
  

  public MLAlgo getMLAlgoInstance()
    throws Exception
  {
    Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
    Constructor<?> stratConstructor = stratClass
      .getConstructor(new Class[] {ArrayList.class });
    for (String[] param : this.parameterList) {
      this.paramVals.add(param[1]);
    }
    MLAlgo stratObject = 
      (MLAlgo)stratConstructor.newInstance(new Object[] { this.paramVals });
    return stratObject;
  }
  
  public MLAlgoR getMLAlgoRInstance()
    throws Exception
  {
    Class<?> stratClass = Class.forName(this.mlAlgoPath + "." + this.mlAlgoName);
    Constructor<?> stratConstructor = stratClass
      .getConstructor(new Class[] {ArrayList.class });
    for (String[] param : this.parameterList) {
      this.paramVals.add(param[1]);
    }
    MLAlgoR stratObject = 
      (MLAlgoR)stratConstructor.newInstance(new Object[] { this.paramVals });
    return stratObject;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/MLParamUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */