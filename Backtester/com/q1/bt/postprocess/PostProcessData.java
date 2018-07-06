package com.q1.bt.postprocess;


public class PostProcessData
{
  public String scripID = null;
  public String outputName;
  OutputShape outputShape;
  Integer outputPanel;
  public String outputValue;
  
  public PostProcessData(String scripID, String outputName, OutputShape outputShape, int outputPanel)
  {
    this.scripID = scripID;
    this.outputName = outputName;
    this.outputShape = outputShape;
    this.outputPanel = Integer.valueOf(outputPanel);
    updateValue(Double.valueOf(NaN.0D));
  }
  
  public PostProcessData(String outputName, OutputShape outputShape, int outputPanel)
  {
    this.outputName = outputName;
    this.outputShape = outputShape;
    this.outputPanel = Integer.valueOf(outputPanel);
    updateValue(Double.valueOf(NaN.0D));
  }
  
  public String getFileHeader()
  {
    return this.outputName + " " + this.outputShape.toString() + " " + this.outputPanel.toString();
  }
  
  public void updateValue(Object outputValue)
  {
    if (outputValue == null)
      outputValue = Double.valueOf(NaN.0D);
    this.outputValue = outputValue.toString();
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/bt/postprocess/PostProcessData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */