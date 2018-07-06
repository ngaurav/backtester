package com.q1.exception.bt;


public class MissingExpiryException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  
  public MissingExpiryException()
  {
    super("Expiry for which order has been sent does not exist at current datapoint.");
  }
}


/* Location:              /Users/ng/Downloads/Backtester v8.15.jar!/com/q1/exception/bt/MissingExpiryException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */