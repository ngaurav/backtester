package com.q1.exception.bt;


public class ExpiredContractException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  
  public ExpiredContractException()
  {
    super("Position still exists in expired contract.");
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/exception/bt/ExpiredContractException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */