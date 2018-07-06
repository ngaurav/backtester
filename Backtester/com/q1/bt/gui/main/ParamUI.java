package com.q1.bt.gui.main;

import com.q1.bt.execution.Strategy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;




public class ParamUI
{
  String strategyName;
  String strategyPackage;
  
  public ParamUI(String strategyName, String strategyPackage)
  {
    this.strategyName = strategyName;
    this.strategyPackage = strategyPackage;
  }
  

  public ArrayList<String[]> getParameters()
    throws Exception
  {
    Class<?> stratClass = Class.forName(this.strategyPackage + "." + this.strategyName);
    Constructor<?> stratTempConstructor = stratClass.getConstructor(new Class[0]);
    Strategy stratTempObject = (Strategy)stratTempConstructor.newInstance(new Object[0]);
    

    ArrayList<String[]> parameterList = stratTempObject.getParameterList();
    ArrayList<JTextField> paramFieldList = new ArrayList();
    ArrayList<Object> messageList = new ArrayList();
    

    for (String[] param : parameterList)
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
    int option = JOptionPane.showConfirmDialog(null, message, this.strategyName + " - Parameters", 
      2);
    

    ArrayList<String> paramVals = new ArrayList();
    if (option == 0) {
      for (JTextField param : paramFieldList) {
        paramVals.add(param.getText());
      }
    }
    

    ArrayList<String[]> newParamList = new ArrayList();
    for (int i = 0; i < parameterList.size(); i++) {
      String[] defParam = (String[])parameterList.get(i);
      String[] outLine = { defParam[0], (String)paramVals.get(i) };
      newParamList.add(outLine);
    }
    return newParamList;
  }
  
  public String getStrategyName()
  {
    return this.strategyName;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/gui/main/ParamUI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */