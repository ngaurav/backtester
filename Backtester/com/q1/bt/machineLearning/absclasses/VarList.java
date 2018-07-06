package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.process.parameter.MachineLearningParameter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class VarList
{
  private IndUpdateTimeFrame indTF;
  private boolean resetAtEnd;
  private ModelEvaluationTime modET;
  private int frequency;
  ArrayList<String> varNames;
  public MachineLearningParameter mlParameter;
  
  public VarList()
  {
    this.mlParameter = createMLParameter();
  }
  

  public abstract ArrayList<Factor> getFactorList(HashMap<String, CandleIndVar> paramHashMap);
  

  public abstract void populateNormalizerList();
  
  public abstract MachineLearningParameter createMLParameter();
  
  public abstract HashMap<String, CandleIndVar> getNormalizerList();
  
  public void generateFactors(String factorName, ArrayList<Factor> factorList, Integer... paramList)
    throws Exception
  {
    ArrayList<Factor> factorListPart = new ArrayList();
    boolean intraday = false;
    
    if (factorName.substring(factorName.length() - 2).equals("1M")) {
      intraday = true;
      factorName = factorName.substring(0, factorName.length() - 2);
    }
    
    Class<?> c = Class.forName(factorName);
    Constructor<?> cons = c.getConstructors()[0];
    Integer[] arrayOfInteger;
    int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
      String fName; String fName; if (intraday) {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
      } else {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
      }
      CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i) });
      factorListPart.add(new Factor(fName, o));
    }
    factorList.addAll(factorListPart);
  }
  
  public void generateFactors(String Name, ArrayList<Factor> factorList, CandleIndVar c)
  {
    factorList.add(new Factor(Name, c));
  }
  
  public void generateFactors(String factorName, ArrayList<Factor> factorList, CandleIndVar divFactor, Integer... paramList)
    throws Exception
  {
    ArrayList<Factor> factorListPart = new ArrayList();
    boolean intraday = false;
    
    if (factorName.substring(factorName.length() - 2).equals("1M")) {
      intraday = true;
      factorName = factorName.substring(0, factorName.length() - 2);
    }
    
    Class<?> c = Class.forName(factorName);
    Constructor<?> cons = c.getConstructors()[0];
    Integer[] arrayOfInteger;
    int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
      String fName; String fName; if (intraday) {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
      } else {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
      }
      CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i), divFactor });
      factorListPart.add(new Factor(fName, o));
    }
    factorList.addAll(factorListPart);
  }
  
  public void generateFactors(String factorName, ArrayList<Factor> factorList, CandleIndVar subFactor, CandleIndVar divFactor, Integer... paramList)
    throws Exception
  {
    ArrayList<Factor> factorListPart = new ArrayList();
    boolean intraday = false;
    
    if (factorName.substring(factorName.length() - 2).equals("1M")) {
      intraday = true;
      factorName = factorName.substring(0, factorName.length() - 2);
    }
    
    Class<?> c = Class.forName(factorName);
    Constructor<?> cons = c.getConstructors()[0];
    Integer[] arrayOfInteger;
    int j = (arrayOfInteger = paramList).length; for (int i = 0; i < j; i++) { int i = arrayOfInteger[i].intValue();
      String fName; String fName; if (intraday) {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i + "_1M";
      } else {
        fName = 
          factorName.split("\\.")[(factorName.split("\\.").length - 1)] + "_" + i;
      }
      CandleIndVar o = (CandleIndVar)cons.newInstance(new Object[] { Integer.valueOf(i), subFactor, 
        divFactor });
      factorListPart.add(new Factor(fName, o));
    }
    factorList.addAll(factorListPart);
  }
  

  public void setVarNames(ArrayList<Factor> factorList)
  {
    this.varNames = new ArrayList();
    for (Factor factor : factorList) {
      this.varNames.add(factor.getName());
    }
  }
  
  public ArrayList<String> getVarNames() {
    return this.varNames;
  }
  
  public void setIndUpdateTimeFrame(IndUpdateTimeFrame indTFUser, boolean resetAtEnd)
  {
    this.indTF = indTFUser;
    this.resetAtEnd = resetAtEnd;
  }
  
  public void setModelEvaluationTime(ModelEvaluationTime modEtUser, int frequency)
  {
    this.modET = modEtUser;
    this.frequency = frequency;
  }
  
  public IndUpdateCriterion getIndUpdateTimeFrame() {
    IndUpdateCriterion iuc = new IndUpdateCriterion(this.indTF, this.resetAtEnd);
    return iuc;
  }
  
  public ModelEvaluationCriterion getModelEvalTime() {
    ModelEvaluationCriterion mec = new ModelEvaluationCriterion(this.modET, 
      this.frequency);
    return mec;
  }
  
  public static enum IndUpdateTimeFrame
  {
    FULL_DAY,  SESSION1,  SESSION2,  SESSION3;
  }
  
  public static enum ModelEvaluationTime
  {
    FREQUENCY,  SESSION1_START,  SESSION2_START,  SESSION3_START,  DAY_START,  ALL_SESSION_START;
  }
  




  public class IndUpdateCriterion
  {
    public VarList.IndUpdateTimeFrame iut;
    


    public boolean reset;
    



    public IndUpdateCriterion(VarList.IndUpdateTimeFrame iut, boolean reset)
    {
      this.iut = iut;
      this.reset = reset;
    }
  }
  
  public class ModelEvaluationCriterion {
    public VarList.ModelEvaluationTime met;
    public int frequency;
    
    public ModelEvaluationCriterion(VarList.ModelEvaluationTime met, int frequency) {
      this.met = met;
      this.frequency = frequency;
    }
  }
  
  public void setMLParameter(MachineLearningParameter mlParameter) {
    this.mlParameter = mlParameter;
  }
  
  public MachineLearningParameter getMLParameter() {
    return this.mlParameter;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/VarList.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */