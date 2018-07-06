package com.q1.bt.machineLearning.absclasses;

import com.q1.bt.machineLearning.utility.CandleData;
import com.q1.bt.machineLearning.utility.DailyData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DailyIndColl
{
  ArrayList<Factor> factorList;
  HashMap<String, CandleIndVar> normalizerMap;
  ArrayList<Factor> finalFactorList;
  
  public DailyIndColl(ArrayList<String> factorFilter, VarList VarListType, FactorType fType, ValueType vType) throws Exception
  {
    VarListType.populateNormalizerList();
    this.normalizerMap = VarListType.getNormalizerList();
    this.factorList = VarListType.getFactorList(this.normalizerMap);
    this.finalFactorList = new ArrayList();
    Iterator localIterator2; for (Iterator localIterator1 = factorFilter.iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      String factorName = (String)localIterator1.next();
      localIterator2 = this.factorList.iterator(); continue;Factor factor = (Factor)localIterator2.next();
      if ((factor.factorName.equals(factorName)) && 
        (!factor.factorName.contains("1M")) && 
        (factor.dv.fType.equals(fType)) && 
        (factor.dv.vType.equals(vType))) {
        this.finalFactorList.add(factor);
      }
    }
  }
  
  public ArrayList<Factor> getFactorList()
  {
    return this.finalFactorList;
  }
  
  public HashMap<String, CandleIndVar> getNormaLizeMap() {
    return this.normalizerMap;
  }
  
  public ArrayList<Double[]> process(DailyData[] dd, Long date)
    throws java.io.IOException
  {
    if (dd[0].date == null)
      return null;
    ArrayList<Double[]> indValues = new ArrayList();
    

    for (CandleIndVar normalizer : this.normalizerMap.values()) {
      if (dd[0].date == date) {
        normalizer.updateInd(dd);
      }
    }
    if (this.finalFactorList.size() == 0) {
      return null;
    }
    for (Factor factor : this.finalFactorList)
    {
      if (dd[0].date == date) {
        factor.updateInd(dd);
        Double[] indVal = factor.getValue();
        indValues.add(indVal);
      } else {
        return null;
      }
    }
    return indValues;
  }
  
  public ArrayList<Double[]> process(CandleData[] cd, Long date) throws java.io.IOException
  {
    if (cd[0].date == null)
      return null;
    ArrayList<Double[]> indValues = new ArrayList();
    
    for (CandleIndVar normalizer : this.normalizerMap.values()) {
      if (cd[0].date == date) {
        normalizer.updateInd(cd);
      }
    }
    
    for (Factor factor : this.finalFactorList)
    {
      if (cd[0].date == date) {
        factor.updateInd(cd);
        Double[] indVal = factor.getValue();
        indValues.add(indVal);
      } else {
        return null;
      }
    }
    return indValues;
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/machineLearning/absclasses/DailyIndColl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */